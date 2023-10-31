package com.wansenai.service.receipt.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.bo.FileDataBO;
import com.wansenai.dto.receipt.QueryShipmentsDTO;
import com.wansenai.dto.receipt.RetailShipmentsDTO;
import com.wansenai.entities.receipt.ReceiptMain;
import com.wansenai.entities.receipt.ReceiptSub;
import com.wansenai.entities.system.SysFile;
import com.wansenai.mappers.receipt.ReceiptMainMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.basic.MemberService;
import com.wansenai.service.receipt.ReceiptSubService;
import com.wansenai.service.receipt.RetailService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.RetailCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.RetailShipmentsDetailVO;
import com.wansenai.vo.receipt.RetailShipmentsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RetailServiceImpl extends ServiceImpl<ReceiptMainMapper, ReceiptMain> implements RetailService {

    private final ReceiptMainMapper receiptMainMapper;

    private final ReceiptSubService receiptSubService;

    private final MemberService memberService;

    private final ISysUserService userService;

    private final SysFileMapper fileMapper;

    public RetailServiceImpl(ReceiptMainMapper receiptMainMapper, ReceiptSubService receiptSubService, MemberService memberService, ISysUserService userService, SysFileMapper fileMapper) {
        this.receiptMainMapper = receiptMainMapper;
        this.receiptSubService = receiptSubService;
        this.memberService = memberService;
        this.userService = userService;
        this.fileMapper = fileMapper;
    }

    @Override
    public Response<Page<RetailShipmentsVO>> getRetailShipments(QueryShipmentsDTO shipmentsDTO) {
        var result = new Page<RetailShipmentsVO>();

        var page = new Page<ReceiptMain>(shipmentsDTO.getPage(), shipmentsDTO.getPageSize());
        var queryWrapper = lambdaQuery()
                .eq(StringUtils.hasText(shipmentsDTO.getReceiptNumber()), ReceiptMain::getReceiptNumber, shipmentsDTO.getReceiptNumber())
                .like(StringUtils.hasText(shipmentsDTO.getRemark()), ReceiptMain::getRemark, shipmentsDTO.getRemark())
                .eq(shipmentsDTO.getMemberId() != null, ReceiptMain::getMemberId, shipmentsDTO.getMemberId())
                .eq(shipmentsDTO.getAccountId() != null, ReceiptMain::getAccountId, shipmentsDTO.getAccountId())
                .eq(shipmentsDTO.getOperatorId() != null, ReceiptMain::getCreateBy, shipmentsDTO.getOperatorId())
                .eq(shipmentsDTO.getStatus() != null, ReceiptMain::getStatus, shipmentsDTO.getStatus())
                .ge(StringUtils.hasText(shipmentsDTO.getStartDate()), ReceiptMain::getCreateTime, shipmentsDTO.getStartDate())
                .le(StringUtils.hasText(shipmentsDTO.getEndDate()), ReceiptMain::getCreateTime, shipmentsDTO.getEndDate());

        var queryResult = receiptMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            String memberName = null;
            if(item.getMemberId() != null) {
                var member = memberService.getMemberById(item.getMemberId());
                if (member != null) {
                    memberName = member.getMemberName();
                }
            }
            String crateBy = null;
            if(item.getCreateBy() != null) {
                var user = userService.getById(item.getCreateBy());
                if (user != null) {
                    crateBy = user.getName();
                }
            }
            var productNumber = receiptSubService.lambdaQuery()
                    .eq(ReceiptSub::getReceiptMainId, item.getId())
                    .list()
                    .stream()
                    .mapToInt(ReceiptSub::getProductNumber)
                    .sum();
            var retailShipmentsVO = RetailShipmentsVO.builder()
                    .id(item.getId())
                    .memberName(memberName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getCreateTime())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalPrice(item.getTotalPrice())
                    .collectionAmount(item.getTotalPrice())
                    .backAmount(item.getBackAmount())
                    .status(item.getStatus())
                    .build();

            result.setRecords(List.of(retailShipmentsVO));
        });
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }

    @Override
    @Transactional
    public Response<String> addorUpdateRetailShipments(RetailShipmentsDTO shipmentsDTO) {
        var userId = userService.getCurrentUserId();
        if (shipmentsDTO.getId() != null) {

            var updateMainResult = lambdaUpdate()
                    .eq(ReceiptMain::getId, shipmentsDTO.getId())
                    .set(shipmentsDTO.getMemberId()!= null, ReceiptMain::getMemberId, shipmentsDTO.getMemberId())
                    .set(shipmentsDTO.getAccountId()!= null, ReceiptMain::getAccountId, shipmentsDTO.getAccountId())
                    .set(StringUtils.hasText(shipmentsDTO.getReceiptType()), ReceiptMain::getReceiptType, shipmentsDTO.getReceiptType())
                    .set(shipmentsDTO.getCollectionAmount()!= null, ReceiptMain::getChangeAmount, shipmentsDTO.getCollectionAmount())
                    .set(shipmentsDTO.getReceiptAmount()!= null, ReceiptMain::getTotalPrice, shipmentsDTO.getReceiptAmount())
                    .set(shipmentsDTO.getBackAmount()!= null, ReceiptMain::getBackAmount, shipmentsDTO.getBackAmount())
                    .set(StringUtils.hasText(shipmentsDTO.getRemark()), ReceiptMain::getRemark, shipmentsDTO.getRemark())
                    .set(ReceiptMain::getUpdateBy, userId)
                    .set(ReceiptMain::getUpdateTime, LocalDateTime.now())
                    .update();

            receiptSubService.lambdaUpdate()
                    .eq(ReceiptSub::getReceiptMainId, shipmentsDTO.getId())
                    .remove();

            var receiptSubList = shipmentsDTO.getTableData();
            var receiptList = new ArrayList<ReceiptSub>(receiptSubList.size() + 3);
            receiptSubList.forEach(item -> {
                var receiptSub = ReceiptSub.builder()
                        .receiptMainId(shipmentsDTO.getId())
                        .productId(item.getProductId())
                        .productNumber(item.getProductNumber())
                        .productPrice(item.getUnitPrice())
                        .productTotalPrice(item.getAmount())
                        .productRemark(item.getRemark())
                        .build();
                receiptList.add(receiptSub);
            });
            var updateSubResult = receiptSubService.saveBatch(receiptList);

            if(!shipmentsDTO.getFiles().isEmpty()) {
                var receiptMain = getById(shipmentsDTO.getId());
                if (receiptMain != null) {
                    String[] idStrings = receiptMain.getFileId().split(",");
                    List<Long> ids = Arrays.stream(idStrings)
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                    fileMapper.deleteBatchIds(ids);
                }
                shipmentsDTO.getFiles().forEach(item -> {
                    var file = SysFile.builder()
                            .id(item.getId())
                            .uid(item.getUid())
                            .fileName(item.getFileName())
                            .fileType(item.getFileType())
                            .fileSize(item.getFileSize())
                            .fileUrl(item.getFileUrl())
                            .build();
                    fileMapper.insert(file);
                });
            }

            if(updateMainResult && updateSubResult) {
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_SUCCESS);
            } else {
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_ERROR);
            }

        } else {
            var id = SnowflakeIdUtil.nextId();
            var fileIds = "";
            if (!shipmentsDTO.getFiles().isEmpty()) {
                fileIds = shipmentsDTO.getFiles().stream()
                        .map(FileDataBO::getId)
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
            }

            var receiptMain = ReceiptMain.builder()
                    .id(id)
                    .type("出库")
                    .subType("零售出库")
                    .initReceiptNumber(shipmentsDTO.getReceiptNumber())
                    .receiptNumber(shipmentsDTO.getReceiptNumber())
                    .memberId(shipmentsDTO.getMemberId())
                    .accountId(shipmentsDTO.getAccountId())
                    .receiptType(shipmentsDTO.getReceiptType())
                    .changeAmount(shipmentsDTO.getCollectionAmount())
                    .totalPrice(shipmentsDTO.getReceiptAmount())
                    .backAmount(shipmentsDTO.getBackAmount())
                    .remark(shipmentsDTO.getRemark())
                    .fileId(fileIds)
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();

            var saveMainResult = save(receiptMain);

            var receiptSubList = shipmentsDTO.getTableData();
            var receiptList = new ArrayList<ReceiptSub>(receiptSubList.size() + 3);
            receiptSubList.forEach(item -> {
                var receiptSub = ReceiptSub.builder()
                        .receiptMainId(id)
                        .productId(item.getProductId())
                        .productNumber(item.getProductNumber())
                        .productPrice(item.getUnitPrice())
                        .productTotalPrice(item.getAmount())
                        .productRemark(item.getRemark())
                        .build();
                receiptList.add(receiptSub);
            });

            var saveSubResult = receiptSubService.saveBatch(receiptList);

            if(!shipmentsDTO.getFiles().isEmpty()) {
                shipmentsDTO.getFiles().forEach(item -> {
                    var file = SysFile.builder()
                            .id(item.getId())
                            .uid(item.getUid())
                            .fileName(item.getFileName())
                            .fileType(item.getFileType())
                            .fileSize(item.getFileSize())
                            .fileUrl(item.getFileUrl())
                            .build();
                    fileMapper.insert(file);
                });
            }

            if(saveMainResult && saveSubResult) {
                return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_SHIPMENTS_SUCCESS);
            } else {
                return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_SHIPMENTS_ERROR);
            }
        }
    }

    @Override
    public Response<String> deleteRetailShipments(List<Long> ids) {
        if (ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        return null;
    }

    @Override
    public Response<RetailShipmentsDetailVO> getRetailShipmentsDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var shipment = getById(id);
      return null;
    }

    @Override
    public Response<String> updateRetailShipmentsStatus(List<Long> id, Integer status) {
        return null;
    }
}
