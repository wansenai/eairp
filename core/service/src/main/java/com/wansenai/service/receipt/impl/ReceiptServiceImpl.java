package com.wansenai.service.receipt.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.bo.FileDataBO;
import com.wansenai.bo.SalesDataBO;
import com.wansenai.bo.ShipmentsDataBO;
import com.wansenai.dto.receipt.QueryRetailRefundDTO;
import com.wansenai.dto.receipt.QueryShipmentsDTO;
import com.wansenai.dto.receipt.RetailRefundDTO;
import com.wansenai.dto.receipt.RetailShipmentsDTO;
import com.wansenai.dto.receipt.sale.QuerySaleOrderDTO;
import com.wansenai.dto.receipt.sale.SaleOrderDTO;
import com.wansenai.entities.product.ProductStockKeepUnit;
import com.wansenai.entities.receipt.ReceiptMain;
import com.wansenai.entities.receipt.ReceiptSub;
import com.wansenai.entities.system.SysFile;
import com.wansenai.mappers.product.ProductStockKeepUnitMapper;
import com.wansenai.mappers.receipt.ReceiptMainMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.basic.CustomerService;
import com.wansenai.service.basic.MemberService;
import com.wansenai.service.product.ProductService;
import com.wansenai.service.receipt.ReceiptSubService;
import com.wansenai.service.receipt.ReceiptService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.constants.ReceiptConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.RetailCodeEnum;
import com.wansenai.utils.enums.SaleCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.*;
import com.wansenai.vo.receipt.sale.SaleOrderDetailVO;
import com.wansenai.vo.receipt.sale.SaleOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReceiptServiceImpl extends ServiceImpl<ReceiptMainMapper, ReceiptMain> implements ReceiptService {

    private final ReceiptMainMapper receiptMainMapper;

    private final ReceiptSubService receiptSubService;

    private final MemberService memberService;

    private final ISysUserService userService;

    private final SysFileMapper fileMapper;

    private final ProductStockKeepUnitMapper productStockKeepUnitMapper;

    private final ProductService productService;

    private final CustomerService customerService;

    public ReceiptServiceImpl(ReceiptMainMapper receiptMainMapper, ReceiptSubService receiptSubService, MemberService memberService, ISysUserService userService, SysFileMapper fileMapper, ProductStockKeepUnitMapper productStockKeepUnitMapper, ProductService productService, CustomerService customerService) {
        this.receiptMainMapper = receiptMainMapper;
        this.receiptSubService = receiptSubService;
        this.memberService = memberService;
        this.userService = userService;
        this.fileMapper = fileMapper;
        this.productStockKeepUnitMapper = productStockKeepUnitMapper;
        this.productService = productService;
        this.customerService = customerService;
    }

    @Override
    public Response<Page<RetailShipmentsVO>> getRetailShipmentsPage(QueryShipmentsDTO shipmentsDTO) {
        var result = new Page<RetailShipmentsVO>();
        var retailShipmentsVOList = new ArrayList<RetailShipmentsVO>();
        var page = new Page<ReceiptMain>(shipmentsDTO.getPage(), shipmentsDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptMain>()
                .eq(ReceiptMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .in(ReceiptMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_SHIPMENTS)
                .eq(StringUtils.hasText(shipmentsDTO.getReceiptNumber()), ReceiptMain::getReceiptNumber, shipmentsDTO.getReceiptNumber())
                .like(StringUtils.hasText(shipmentsDTO.getRemark()), ReceiptMain::getRemark, shipmentsDTO.getRemark())
                .eq(shipmentsDTO.getMemberId() != null, ReceiptMain::getMemberId, shipmentsDTO.getMemberId())
                .eq(shipmentsDTO.getAccountId() != null, ReceiptMain::getAccountId, shipmentsDTO.getAccountId())
                .eq(shipmentsDTO.getOperatorId() != null, ReceiptMain::getCreateBy, shipmentsDTO.getOperatorId())
                .eq(shipmentsDTO.getStatus() != null, ReceiptMain::getStatus, shipmentsDTO.getStatus())
                .eq(ReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(shipmentsDTO.getStartDate()), ReceiptMain::getCreateTime, shipmentsDTO.getStartDate())
                .le(StringUtils.hasText(shipmentsDTO.getEndDate()), ReceiptMain::getCreateTime, shipmentsDTO.getEndDate());

        var queryResult = receiptMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            String memberName = null;
            if (item.getMemberId() != null) {
                var member = memberService.getMemberById(item.getMemberId());
                if (member != null) {
                    memberName = member.getMemberName();
                }
            }
            String crateBy = null;
            if (item.getCreateBy() != null) {
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
            retailShipmentsVOList.add(retailShipmentsVO);
        });
        result.setRecords(retailShipmentsVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }

    @Override
    public Response<List<RetailShipmentsVO>> getRetailShipmentsList(QueryShipmentsDTO shipmentsDTO) {
        var query = lambdaQuery()
                .eq(ReceiptMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .in(ReceiptMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_SHIPMENTS)
                .eq(StringUtils.hasText(shipmentsDTO.getReceiptNumber()), ReceiptMain::getReceiptNumber, shipmentsDTO.getReceiptNumber())
                .like(StringUtils.hasText(shipmentsDTO.getRemark()), ReceiptMain::getRemark, shipmentsDTO.getRemark())
                .eq(shipmentsDTO.getMemberId() != null, ReceiptMain::getMemberId, shipmentsDTO.getMemberId())
                .eq(shipmentsDTO.getAccountId() != null, ReceiptMain::getAccountId, shipmentsDTO.getAccountId())
                .eq(shipmentsDTO.getOperatorId() != null, ReceiptMain::getCreateBy, shipmentsDTO.getOperatorId())
                .eq(shipmentsDTO.getStatus() != null, ReceiptMain::getStatus, shipmentsDTO.getStatus())
                .eq(ReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(shipmentsDTO.getStartDate()), ReceiptMain::getCreateTime, shipmentsDTO.getStartDate())
                .le(StringUtils.hasText(shipmentsDTO.getEndDate()), ReceiptMain::getCreateTime, shipmentsDTO.getEndDate())
                .list();

        var result = new ArrayList<RetailShipmentsVO>(query.size() + 2);
        for (ReceiptMain receiptMain : query) {

            String memberName = "";
            if (receiptMain.getMemberId() != null) {
                var member = memberService.getMemberById(receiptMain.getMemberId());
                if (member != null) {
                    memberName = member.getMemberName();
                }
            }
            String crateBy = null;
            if (receiptMain.getCreateBy() != null) {
                var user = userService.getById(receiptMain.getCreateBy());
                if (user != null) {
                    crateBy = user.getName();
                }
            }
            var productNumber = receiptSubService.lambdaQuery()
                    .eq(ReceiptSub::getReceiptMainId, receiptMain.getId())
                    .list()
                    .stream()
                    .mapToInt(ReceiptSub::getProductNumber)
                    .sum();
            var retailShipmentsVO = RetailShipmentsVO.builder()
                    .id(receiptMain.getId())
                    .memberName(memberName)
                    .receiptNumber(receiptMain.getReceiptNumber())
                    .receiptDate(receiptMain.getCreateTime())
                    .productInfo(receiptMain.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalPrice(receiptMain.getTotalPrice())
                    .collectionAmount(receiptMain.getTotalPrice())
                    .backAmount(receiptMain.getBackAmount())
                    .status(receiptMain.getStatus())
                    .build();
            result.add(retailShipmentsVO);
        }
        return Response.responseData(result);
    }

    @Override
    @Transactional
    public Response<String> addOrUpdateRetailShipments(RetailShipmentsDTO shipmentsDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = shipmentsDTO.getId() != null;

        if (isUpdate) {
            receiptSubService.lambdaUpdate()
                    .eq(ReceiptSub::getReceiptMainId, shipmentsDTO.getId())
                    .remove();

            var receiptSubList = shipmentsDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptSub.builder()
                            .receiptMainId(shipmentsDTO.getId())
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .productPrice(item.getUnitPrice())
                            .productTotalPrice(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = receiptSubService.saveBatch(receiptList);

            var fid = new ArrayList<>();
            if (!shipmentsDTO.getFiles().isEmpty()) {
                var receiptMain = getById(shipmentsDTO.getId());
                if (StringUtils.hasLength(receiptMain.getFileId())) {
                    var ids = Arrays.stream(receiptMain.getFileId().split(","))
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
                    var result = fileMapper.insert(file);
                    if (result > 0) {
                        fid.add(file.getId());
                    }
                });
            }
            var fileIds = fid.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            var updateMainResult = lambdaUpdate()
                    .eq(ReceiptMain::getId, shipmentsDTO.getId())
                    .set(shipmentsDTO.getMemberId() != null, ReceiptMain::getMemberId, shipmentsDTO.getMemberId())
                    .set(shipmentsDTO.getAccountId() != null, ReceiptMain::getAccountId, shipmentsDTO.getAccountId())
                    .set(shipmentsDTO.getCollectAmount() != null, ReceiptMain::getChangeAmount, shipmentsDTO.getCollectAmount())
                    .set(shipmentsDTO.getReceiptAmount() != null, ReceiptMain::getTotalPrice, shipmentsDTO.getReceiptAmount())
                    .set(shipmentsDTO.getBackAmount() != null, ReceiptMain::getBackAmount, shipmentsDTO.getBackAmount())
                    .set(shipmentsDTO.getStatus() != null, ReceiptMain::getStatus, shipmentsDTO.getStatus())
                    .set(StringUtils.hasText(shipmentsDTO.getPaymentType()), ReceiptMain::getPaymentType, shipmentsDTO.getPaymentType())
                    .set(StringUtils.hasText(shipmentsDTO.getRemark()), ReceiptMain::getRemark, shipmentsDTO.getRemark())
                    .set(StringUtils.hasText(shipmentsDTO.getReceiptDate()), ReceiptMain::getCreateTime, shipmentsDTO.getReceiptDate())
                    .set(StringUtils.hasLength(fileIds), ReceiptMain::getFileId, fileIds)
                    .set(ReceiptMain::getUpdateBy, userId)
                    .set(ReceiptMain::getUpdateTime, LocalDateTime.now())
                    .update();

            if (updateMainResult && updateSubResult) {
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_SUCCESS);
            } else {
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_ERROR);
            }
        } else {
            var id = SnowflakeIdUtil.nextId();

            var fid = new ArrayList<>();
            if (!shipmentsDTO.getFiles().isEmpty()) {
                shipmentsDTO.getFiles().forEach(item -> {
                    var file = SysFile.builder()
                            .id(item.getId())
                            .uid(item.getUid())
                            .fileName(item.getFileName())
                            .fileType(item.getFileType())
                            .fileSize(item.getFileSize())
                            .fileUrl(item.getFileUrl())
                            .build();
                    var result = fileMapper.insert(file);
                    if (result > 0) {
                        fid.add(file.getId());
                    }
                });
            }
            var fileIds = fid.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            var receiptMain = ReceiptMain.builder()
                    .id(id)
                    .type(ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                    .subType(ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_SHIPMENTS)
                    .initReceiptNumber(shipmentsDTO.getReceiptNumber())
                    .receiptNumber(shipmentsDTO.getReceiptNumber())
                    .memberId(shipmentsDTO.getMemberId())
                    .accountId(shipmentsDTO.getAccountId())
                    .paymentType(shipmentsDTO.getPaymentType())
                    .accountId(shipmentsDTO.getAccountId())
                    .changeAmount(shipmentsDTO.getCollectAmount())
                    .totalPrice(shipmentsDTO.getReceiptAmount())
                    .backAmount(shipmentsDTO.getBackAmount())
                    .remark(shipmentsDTO.getRemark())
                    .fileId(fileIds)
                    .status(shipmentsDTO.getStatus())
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();

            var saveMainResult = save(receiptMain);

            var receiptSubList = shipmentsDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptSub.builder()
                            .receiptMainId(id)
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .productPrice(item.getUnitPrice())
                            .productTotalPrice(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .build())
                    .collect(Collectors.toList());

            var saveSubResult = receiptSubService.saveBatch(receiptList);

            if (saveMainResult && saveSubResult) {
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
        var updateResult = lambdaUpdate()
                .in(ReceiptMain::getId, ids)
                .set(ReceiptMain::getDeleteFlag, CommonConstants.DELETED)
                .update();

        receiptSubService.lambdaUpdate()
                .in(ReceiptSub::getReceiptMainId, ids)
                .set(ReceiptSub::getDeleteFlag, CommonConstants.DELETED)
                .update();

        if (updateResult) {
            return Response.responseMsg(RetailCodeEnum.DELETE_RETAIL_SHIPMENTS_SUCCESS);
        } else {
            return Response.responseMsg(RetailCodeEnum.DELETE_RETAIL_SHIPMENTS_ERROR);
        }
    }

    @Override
    public Response<RetailShipmentsDetailVO> getRetailShipmentsDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var shipment = getById(id);

        List<FileDataBO> fileList = new ArrayList<>();
        if (StringUtils.hasLength(shipment.getFileId())) {
            List<Long> ids = Arrays.stream(shipment.getFileId().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            fileList.addAll(fileMapper.selectBatchIds(ids)
                    .stream()
                    .map(item ->
                            FileDataBO.builder(
                                    item.getFileName(),
                                    item.getFileUrl(),
                                    item.getId(),
                                    item.getUid(),
                                    item.getFileType(),
                                    item.getFileSize()
                            ))
                    .toList());
        }

        var receiptSubList = receiptSubService.lambdaQuery()
                .eq(ReceiptSub::getReceiptMainId, id)
                .list();

        var tableData = new ArrayList<ShipmentsDataBO>(receiptSubList.size() + 1);
        for (ReceiptSub item : receiptSubList) {
            var shipmentBo = ShipmentsDataBO.builder()
                    .productId(item.getProductId())
                    .barCode(item.getProductBarcode())
                    .productNumber(item.getProductNumber())
                    .unitPrice(item.getProductPrice())
                    .amount(item.getProductTotalPrice())
                    .warehouseId(item.getWarehouseId())
                    .build();

            var data = productStockKeepUnitMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
            if(data != null) {
                shipmentBo.setProductName(data.getProductName());
                shipmentBo.setProductStandard(data.getProductStandard());
                shipmentBo.setProductUnit(data.getProductUnit());
                shipmentBo.setStock(data.getStock());

            }

            tableData.add(shipmentBo);
        }

        var retailShipmentsDetailVO = RetailShipmentsDetailVO.builder()
                .receiptNumber(shipment.getReceiptNumber())
                .receiptDate(shipment.getCreateTime())
                .memberId(shipment.getMemberId())
                .accountId(shipment.getAccountId())
                .paymentType(shipment.getPaymentType())
                .collectAmount(shipment.getChangeAmount())
                .receiptAmount(shipment.getTotalPrice())
                .backAmount(shipment.getBackAmount())
                .remark(shipment.getRemark())
                .tableData(tableData)
                .files(fileList)
                .build();

        return Response.responseData(retailShipmentsDetailVO);
    }

    @Override
    public Response<String> updateRetailShipmentsStatus(List<Long> ids, Integer status) {
        if (ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptMain::getId, ids)
                .set(ReceiptMain::getStatus, status)
                .update();
        if (updateResult) {
            return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_SUCCESS);
        } else {
            return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_ERROR);
        }
    }

    @Override
    public Response<RetailStatisticalDataVO> getRetailStatistics() {
        var now = LocalDateTime.now();

        var retailData = lambdaQuery()
                .eq(ReceiptMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .in(ReceiptMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_SHIPMENTS)
                // .eq(ReceiptMain::getStatus, 1)
                .eq(ReceiptMain::getDeleteFlag, 0)
                .list();
        var retailRefundData = lambdaQuery()
                .eq(ReceiptMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .eq(ReceiptMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_REFUND)
               // .eq(ReceiptMain::getStatus, 1)
                .eq(ReceiptMain::getDeleteFlag, 0)
                .list();

        var salesData = lambdaQuery()
                .eq(ReceiptMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .in(ReceiptMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_SALES_SHIPMENTS)
            //    .eq(ReceiptMain::getStatus, 1)
                .eq(ReceiptMain::getDeleteFlag, 0)
                .list();
        var salesRefundData = lambdaQuery()
                .eq(ReceiptMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .eq(ReceiptMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_SALES_REFUND)
           //     .eq(ReceiptMain::getStatus, 1)
                .eq(ReceiptMain::getDeleteFlag, 0)
                .list();

        var purchaseData = lambdaQuery()
                .eq(ReceiptMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .eq(ReceiptMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_STORAGE)
             //   .eq(ReceiptMain::getStatus, 1)
                .eq(ReceiptMain::getDeleteFlag, 0)
                .list();
        var purchaseRefundData = lambdaQuery()
                .eq(ReceiptMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .eq(ReceiptMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_REFUND)
             //   .eq(ReceiptMain::getStatus, 1)
                .eq(ReceiptMain::getDeleteFlag, 0)
                .list();

        var todayRetailSales = calculateTotalPrice(retailData, retailRefundData, now.with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yesterdayRetailSales = calculateTotalPrice(retailData, retailRefundData, now.minusDays(1).with(LocalTime.MIN), now.minusDays(1).with(LocalTime.MAX));
        var monthRetailSales = calculateTotalPrice(retailData, retailRefundData, now.withDayOfMonth(1).with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yearRetailSales = calculateTotalPrice(retailData, retailRefundData, now.withDayOfYear(1).with(LocalTime.MIN), now.with(LocalTime.MAX));

        var todaySales = calculateTotalPrice(salesData, salesRefundData, now.with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yesterdaySales = calculateTotalPrice(salesData, salesRefundData, now.minusDays(1).with(LocalTime.MIN), now.minusDays(1).with(LocalTime.MAX));
        var monthSales = calculateTotalPrice(salesData, salesRefundData, now.withDayOfMonth(1).with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yearSales = calculateTotalPrice(salesData, salesRefundData, now.withDayOfYear(1).with(LocalTime.MIN), now.with(LocalTime.MAX));

        var todayPurchase = calculateTotalPrice(purchaseData, purchaseRefundData, now.with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yesterdayPurchase = calculateTotalPrice(purchaseData, purchaseRefundData, now.minusDays(1).with(LocalTime.MIN), now.minusDays(1).with(LocalTime.MAX));
        var monthPurchase = calculateTotalPrice(purchaseData, purchaseRefundData, now.withDayOfMonth(1).with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yearPurchase = calculateTotalPrice(purchaseData, purchaseRefundData, now.withDayOfYear(1).with(LocalTime.MIN), now.with(LocalTime.MAX));

        var retailStatisticalDataVO = RetailStatisticalDataVO.builder()
                .todayRetailSales(todayRetailSales)
                .yesterdayRetailSales(yesterdayRetailSales)
                .monthRetailSales(monthRetailSales)
                .yearRetailSales(yearRetailSales)
                .todaySales(todaySales)
                .yesterdaySales(yesterdaySales)
                .monthSales(monthSales)
                .yearSales(yearSales)
                .todayPurchase(todayPurchase)
                .yesterdayPurchase(yesterdayPurchase)
                .monthPurchase(monthPurchase)
                .yearPurchase(yearPurchase)
                .build();

        return Response.responseData(retailStatisticalDataVO);
    }

    private BigDecimal calculateTotalPrice(List<ReceiptMain> data, List<ReceiptMain> backData, LocalDateTime start, LocalDateTime end) {
        var dataTotalPrice = data.stream()
                .filter(item -> item.getCreateTime().isAfter(start) && item.getCreateTime().isBefore(end))
                .map(ReceiptMain::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        var backDataTotalPrice = backData.stream()
                .filter(item -> item.getCreateTime().isAfter(start) && item.getCreateTime().isBefore(end))
                .map(ReceiptMain::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        return dataTotalPrice.add(backDataTotalPrice);
    }

    @Override
    public Response<Page<RetailRefundVO>> getRetailRefund(QueryRetailRefundDTO refundDTO) {
        var result = new Page<RetailRefundVO>();
        var retailRefundVOList = new ArrayList<RetailRefundVO>();
        var page = new Page<ReceiptMain>(refundDTO.getPage(), refundDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptMain>()
                .eq(ReceiptMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .in(ReceiptMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_REFUND)
                .eq(StringUtils.hasText(refundDTO.getReceiptNumber()), ReceiptMain::getReceiptNumber, refundDTO.getReceiptNumber())
                .like(StringUtils.hasText(refundDTO.getRemark()), ReceiptMain::getRemark, refundDTO.getRemark())
                .eq(refundDTO.getMemberId() != null, ReceiptMain::getMemberId, refundDTO.getMemberId())
                .eq(refundDTO.getAccountId() != null, ReceiptMain::getAccountId, refundDTO.getAccountId())
                .eq(refundDTO.getOperatorId() != null, ReceiptMain::getCreateBy, refundDTO.getOperatorId())
                .eq(refundDTO.getStatus() != null, ReceiptMain::getStatus, refundDTO.getStatus())
                .eq(ReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(refundDTO.getStartDate()), ReceiptMain::getCreateTime, refundDTO.getStartDate())
                .le(StringUtils.hasText(refundDTO.getEndDate()), ReceiptMain::getCreateTime, refundDTO.getEndDate());

        var queryResult = receiptMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            String memberName = null;
            if (item.getMemberId() != null) {
                var member = memberService.getMemberById(item.getMemberId());
                if (member != null) {
                    memberName = member.getMemberName();
                }
            }
            String crateBy = null;
            if (item.getCreateBy() != null) {
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
            var retailRefundVO = RetailRefundVO.builder()
                    .id(item.getId())
                    .memberName(memberName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getCreateTime())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalPrice(item.getTotalPrice())
                    .paymentAmount(item.getTotalPrice())
                    .backAmount(item.getBackAmount())
                    .status(item.getStatus())
                    .build();
            retailRefundVOList.add(retailRefundVO);
        });
        result.setRecords(retailRefundVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }

    @Override
    public Response<String> addOrUpdateRetailRefund(RetailRefundDTO refundDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = refundDTO.getId() != null;

        if (isUpdate) {
            var updateMainResult = lambdaUpdate()
                    .eq(ReceiptMain::getId, refundDTO.getId())
                    .set(refundDTO.getMemberId() != null, ReceiptMain::getMemberId, refundDTO.getMemberId())
                    .set(refundDTO.getAccountId() != null, ReceiptMain::getAccountId, refundDTO.getAccountId())
                    .set(refundDTO.getPaymentAmount() != null, ReceiptMain::getChangeAmount, refundDTO.getPaymentAmount().negate())
                    .set(refundDTO.getReceiptAmount() != null, ReceiptMain::getTotalPrice, refundDTO.getReceiptAmount().negate())
                    .set(refundDTO.getBackAmount() != null, ReceiptMain::getBackAmount, refundDTO.getBackAmount().negate())
                    .set(refundDTO.getStatus() != null, ReceiptMain::getStatus, refundDTO.getStatus())
                    .set(StringUtils.hasText(refundDTO.getOtherReceipt()), ReceiptMain::getOtherReceipt, refundDTO.getOtherReceipt())
                    .set(StringUtils.hasText(refundDTO.getRemark()), ReceiptMain::getRemark, refundDTO.getRemark())
                    .set(StringUtils.hasText(refundDTO.getReceiptDate()), ReceiptMain::getCreateTime, refundDTO.getReceiptDate())
                    .set(ReceiptMain::getUpdateBy, userId)
                    .set(ReceiptMain::getUpdateTime, LocalDateTime.now())
                    .update();

            receiptSubService.lambdaUpdate()
                    .eq(ReceiptSub::getReceiptMainId, refundDTO.getId())
                    .remove();

            var receiptSubList = refundDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptSub.builder()
                            .receiptMainId(refundDTO.getId())
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .productPrice(item.getUnitPrice())
                            .productTotalPrice(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = receiptSubService.saveBatch(receiptList);

            if (!refundDTO.getFiles().isEmpty()) {
                var receiptMain = getById(refundDTO.getId());
                if (receiptMain != null) {
                    var ids = Arrays.stream(receiptMain.getFileId().split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                    fileMapper.deleteBatchIds(ids);
                }
                refundDTO.getFiles().forEach(item -> {
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

            if (updateMainResult && updateSubResult) {
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_REFUND_SUCCESS);
            } else {
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_REFUND_ERROR);
            }
        } else {
            var id = SnowflakeIdUtil.nextId();

            var fid = new ArrayList<>();
            if (!refundDTO.getFiles().isEmpty()) {
                refundDTO.getFiles().forEach(item -> {
                    var file = SysFile.builder()
                            .id(item.getId())
                            .uid(item.getUid())
                            .fileName(item.getFileName())
                            .fileType(item.getFileType())
                            .fileSize(item.getFileSize())
                            .fileUrl(item.getFileUrl())
                            .build();
                    var result = fileMapper.insert(file);
                    if (result > 0) {
                        fid.add(file.getId());
                    }
                });
            }
            var fileIds = fid.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            var receiptMain = ReceiptMain.builder()
                    .id(id)
                    .type(ReceiptConstants.RECEIPT_TYPE_STORAGE)
                    .subType(ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_REFUND)
                    .initReceiptNumber(refundDTO.getReceiptNumber())
                    .receiptNumber(refundDTO.getReceiptNumber())
                    .memberId(refundDTO.getMemberId())
                    .accountId(refundDTO.getAccountId())
                    .otherReceipt(refundDTO.getOtherReceipt())
                    .accountId(refundDTO.getAccountId())
                    .changeAmount(refundDTO.getPaymentAmount().negate())
                    .totalPrice(refundDTO.getReceiptAmount().negate())
                    .backAmount(refundDTO.getBackAmount().negate())
                    .remark(refundDTO.getRemark())
                    .status(refundDTO.getStatus())
                    .fileId(fileIds)
                    .status(refundDTO.getStatus())
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();

            var saveMainResult = save(receiptMain);

            var receiptSubList = refundDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptSub.builder()
                            .receiptMainId(id)
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .productPrice(item.getUnitPrice())
                            .productTotalPrice(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .build())
                    .collect(Collectors.toList());

            var saveSubResult = receiptSubService.saveBatch(receiptList);

            if (saveMainResult && saveSubResult) {
                return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_REFUND_SUCCESS);
            } else {
                return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_REFUND_ERROR);
            }
        }
    }

    @Override
    public Response<List<ReceiptDetailVO>> retailDetail(Long id) {
        if(id == null) {
            Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var receiptSubList = receiptSubService.lambdaQuery()
                .eq(ReceiptSub::getReceiptMainId, id)
                .eq(ReceiptSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();
        var result = new ArrayList<ReceiptDetailVO>(receiptSubList.size() + 1);

        for (ReceiptSub receiptSub : receiptSubList) {
            var receiptDetail = ReceiptDetailVO.builder()
                    .id(receiptSub.getId())
                    .warehouseId(receiptSub.getWarehouseId())
                    .productId(receiptSub.getProductId())
                    .productBarcode(receiptSub.getProductBarcode())
                    .productNumber(receiptSub.getProductNumber())
                    .productPrice(receiptSub.getProductPrice())
                    .productTotalPrice(receiptSub.getProductTotalPrice())
                    .remark(receiptSub.getProductRemark())
                    .build();

            var receiptMain = getById(id);
            if(receiptMain != null) {
                receiptDetail.setMemberId(receiptMain.getMemberId());
                receiptDetail.setDiscountRate(receiptMain.getDiscountRate());
                receiptDetail.setDiscountAmount(receiptMain.getDiscountAmount());
                receiptDetail.setDiscountLastAmount(receiptMain.getDiscountLastAmount());
            }

            var product = productService.getById(receiptSub.getProductId());
            if (product != null) {
                receiptDetail.setProductName(product.getProductName());
                receiptDetail.setProductStandard(product.getProductStandard());
                receiptDetail.setProductModel(product.getProductModel());

                var productSku = productStockKeepUnitMapper.selectOne(new LambdaQueryWrapper<ProductStockKeepUnit>()
                        .eq(ProductStockKeepUnit::getProductBarCode, receiptSub.getProductBarcode())
                        .eq(ProductStockKeepUnit::getProductId, product.getId()));
                receiptDetail.setUnit(productSku.getProductUnit());
            }
            result.add(receiptDetail);
        }

        return Response.responseData(result);
    }

    @Override
    public Response<RetailRefundDetailVO> getRetailRefundDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var shipment = getById(id);

        List<FileDataBO> fileList = new ArrayList<>();
        if (StringUtils.hasLength(shipment.getFileId())) {
            List<Long> ids = Arrays.stream(shipment.getFileId().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            fileList.addAll(fileMapper.selectBatchIds(ids)
                    .stream()
                    .map(item ->
                            FileDataBO.builder(
                                    item.getFileName(),
                                    item.getFileUrl(),
                                    item.getId(),
                                    item.getUid(),
                                    item.getFileType(),
                                    item.getFileSize()
                            ))
                    .toList());
        }

        var receiptSubList = receiptSubService.lambdaQuery()
                .eq(ReceiptSub::getReceiptMainId, id)
                .eq(ReceiptSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        var tableData = new ArrayList<ShipmentsDataBO>(receiptSubList.size() + 1);
        for (ReceiptSub item : receiptSubList) {
            var shipmentBo = ShipmentsDataBO.builder()
                    .productId(item.getProductId())
                    .barCode(item.getProductBarcode())
                    .productNumber(item.getProductNumber())
                    .unitPrice(item.getProductPrice())
                    .amount(item.getProductTotalPrice())
                    .warehouseId(item.getWarehouseId())
                    .build();

            var data = productStockKeepUnitMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
            if(data != null) {
                shipmentBo.setProductName(data.getProductName());
                shipmentBo.setProductStandard(data.getProductStandard());
                shipmentBo.setProductUnit(data.getProductUnit());
                shipmentBo.setStock(data.getStock());

            }

            tableData.add(shipmentBo);
        }

        var retailRefundDetailVO = RetailRefundDetailVO.builder()
                .receiptNumber(shipment.getReceiptNumber())
                .receiptDate(shipment.getCreateTime())
                .memberId(shipment.getMemberId())
                .accountId(shipment.getAccountId())
                .otherReceipt(shipment.getOtherReceipt())
                .paymentAmount(shipment.getChangeAmount())
                .receiptAmount(shipment.getTotalPrice())
                .backAmount(shipment.getBackAmount())
                .remark(shipment.getRemark())
                .tableData(tableData)
                .files(fileList)
                .build();

        return Response.responseData(retailRefundDetailVO);
    }

    @Override
    public Response<String> deleteRetailRefund(List<Long> ids) {
        if (ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptMain::getId, ids)
                .set(ReceiptMain::getDeleteFlag, CommonConstants.DELETED)
                .update();

        if (updateResult) {
            return Response.responseMsg(RetailCodeEnum.DELETE_RETAIL_REFUND_SUCCESS);
        } else {
            return Response.responseMsg(RetailCodeEnum.DELETE_RETAIL_REFUND_ERROR);
        }
    }

    @Override
    public Response<String> updateRetailRefundStatus(List<Long> ids, Integer status) {
       if (ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptMain::getId, ids)
                .set(ReceiptMain::getStatus, status)
                .update();
        if (updateResult) {
            return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_REFUND_SUCCESS);
        } else {
            return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_REFUND_ERROR);
        }
    }

    @Override
    public Response<Page<SaleOrderVO>> getSaleOrderPage(QuerySaleOrderDTO querySaleOrderDTO) {
        var result = new Page<SaleOrderVO>();
        var saleOrderVOList = new ArrayList<SaleOrderVO>();
        var page = new Page<ReceiptMain>(querySaleOrderDTO.getPage(), querySaleOrderDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptMain>()
                .eq(ReceiptMain::getType, ReceiptConstants.RECEIPT_TYPE_ORDER)
                .in(ReceiptMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_SALES_ORDER)
                .eq(StringUtils.hasText(querySaleOrderDTO.getReceiptNumber()), ReceiptMain::getReceiptNumber, querySaleOrderDTO.getReceiptNumber())
                .like(StringUtils.hasText(querySaleOrderDTO.getRemark()), ReceiptMain::getRemark, querySaleOrderDTO.getRemark())
                .eq(querySaleOrderDTO.getCustomerId() != null, ReceiptMain::getMemberId, querySaleOrderDTO.getCustomerId())
                .eq(querySaleOrderDTO.getOperatorId() != null, ReceiptMain::getCreateBy, querySaleOrderDTO.getOperatorId())
                .eq(querySaleOrderDTO.getStatus() != null, ReceiptMain::getStatus, querySaleOrderDTO.getStatus())
                .eq(ReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(querySaleOrderDTO.getStartDate()), ReceiptMain::getCreateTime, querySaleOrderDTO.getStartDate())
                .le(StringUtils.hasText(querySaleOrderDTO.getEndDate()), ReceiptMain::getCreateTime, querySaleOrderDTO.getEndDate());

        var queryResult = receiptMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            String customerName = null;
            if (item.getCustomerId() != null) {
                var customer = customerService.getById(item.getCustomerId());
                if (customer != null) {
                    customerName = customer.getCustomerName();
                }
            }
            String crateBy = null;
            if (item.getCreateBy() != null) {
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

            var taxAmount = receiptSubService.lambdaQuery()
                    .eq(ReceiptSub::getReceiptMainId, item.getId())
                    .list()
                    .stream()
                    .map(ReceiptSub::getTaxAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            var taxRateTotalPrice = receiptSubService.lambdaQuery()
                    .eq(ReceiptSub::getReceiptMainId, item.getId())
                    .list()
                    .stream()
                    .map(ReceiptSub::getTaxTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            var saleOrderVO = SaleOrderVO.builder()
                    .id(item.getId())
                    .customerName(customerName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getCreateTime())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalPrice(taxAmount)
                    .taxRateTotalPrice(taxRateTotalPrice)
                    .deposit(item.getDeposit())
                    .status(item.getStatus())
                    .build();
            saleOrderVOList.add(saleOrderVO);
        });
        result.setRecords(saleOrderVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }

    @Override
    public Response<SaleOrderDetailVO> getSaleOrderDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var sale = getById(id);

        List<FileDataBO> fileList = new ArrayList<>();
        if (StringUtils.hasLength(sale.getFileId())) {
            List<Long> ids = Arrays.stream(sale.getFileId().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            fileList.addAll(fileMapper.selectBatchIds(ids)
                    .stream()
                    .map(item ->
                            FileDataBO.builder(
                                    item.getFileName(),
                                    item.getFileUrl(),
                                    item.getId(),
                                    item.getUid(),
                                    item.getFileType(),
                                    item.getFileSize()
                            ))
                    .toList());
        }

        var receiptSubList = receiptSubService.lambdaQuery()
                .eq(ReceiptSub::getReceiptMainId, id)
                .list();

        var tableData = new ArrayList<SalesDataBO>(receiptSubList.size() + 1);
        for (ReceiptSub item : receiptSubList) {
            var saleData = SalesDataBO.builder()
                    .productId(item.getProductId())
                    .barCode(item.getProductBarcode())
                    .productNumber(item.getProductNumber())
                    .unitPrice(item.getProductPrice())
                    .amount(item.getProductTotalPrice())
                    .taxRate(item.getTaxRate())
                    .taxAmount(item.getTaxAmount())
                    .taxTotalPrice(item.getTaxTotalPrice())
                    .warehouseId(item.getWarehouseId())
                    .build();

            var data = productStockKeepUnitMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
            if(data != null) {
                saleData.setProductName(data.getProductName());
                saleData.setProductStandard(data.getProductStandard());
                saleData.setProductUnit(data.getProductUnit());
                saleData.setStock(data.getStock());

            }

            tableData.add(saleData);
        }

        // 获取sale的多账户信息 分隔字符串 放到一个Long类型的list中 如果为空 则返回空list

        List<Long> accountIds = new ArrayList<>();
        if (StringUtils.hasLength(sale.getMultipleAccount())) {
            accountIds = Arrays.stream(sale.getMultipleAccount().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }

        List<Long> operatorIds = new ArrayList<>();
        if (StringUtils.hasLength(sale.getOperatorId())) {
            operatorIds = Arrays.stream(sale.getOperatorId().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }

        var saleOrderDetailVO = SaleOrderDetailVO.builder()
                .receiptNumber(sale.getReceiptNumber())
                .receiptDate(sale.getCreateTime())
                .customerId(sale.getCustomerId())
                .accountIds(accountIds)
                .operatorIds(operatorIds)
                .discountRate(sale.getDiscountRate())
                .discountAmount(sale.getDiscountAmount())
                .discountLastAmount(sale.getDiscountLastAmount())
                .deposit(sale.getDeposit())
                .remark(sale.getRemark())
                .tableData(tableData)
                .files(fileList)
                .build();

        return Response.responseData(saleOrderDetailVO);
    }

    @Override
    public Response<String> addOrUpdateSaleOrder(SaleOrderDTO saleOrderDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = saleOrderDTO.getId() != null;

        var operatorIds = new StringBuilder();
        if (!saleOrderDTO.getOperatorIds().isEmpty()) {
            var operatorList = saleOrderDTO.getOperatorIds();
            for (Long aLong : operatorList) {
                operatorIds.append(aLong).append(",");
            }
        }

        var multipleAccountIds = new StringBuilder();
        if (!saleOrderDTO.getMultipleAccountIds().isEmpty()) {
            var multipleAccountList = saleOrderDTO.getMultipleAccountIds();
            for (Long aLong : multipleAccountList) {
                multipleAccountIds.append(aLong).append(",");
            }
        }

        var multipleAccountAmounts = new StringBuilder();
        if (!saleOrderDTO.getMultipleAccountAmounts().isEmpty()) {
            var multipleAccountList = saleOrderDTO.getMultipleAccountAmounts();
            for (Long amount : multipleAccountList) {
                multipleAccountAmounts.append(amount).append(",");
            }
        }

        if (isUpdate) {
            var updateMainResult = lambdaUpdate()
                    .eq(ReceiptMain::getId, saleOrderDTO.getId())
                    .set(saleOrderDTO.getCustomerId() != null, ReceiptMain::getCustomerId, saleOrderDTO.getCustomerId())
                    .set(saleOrderDTO.getDiscountRate() != null, ReceiptMain::getDiscountRate, saleOrderDTO.getDiscountRate())
                    .set(saleOrderDTO.getDiscountAmount() != null, ReceiptMain::getDiscountAmount, saleOrderDTO.getDiscountAmount())
                    .set(saleOrderDTO.getDiscountLastAmount() != null, ReceiptMain::getDiscountLastAmount, saleOrderDTO.getDiscountLastAmount())
                    .set(saleOrderDTO.getDeposit() != null, ReceiptMain::getDeposit, saleOrderDTO.getDeposit())
                    .set(saleOrderDTO.getStatus() != null, ReceiptMain::getStatus, saleOrderDTO.getStatus())
                    .set(!multipleAccountIds.isEmpty(), ReceiptMain::getMultipleAccount, String.valueOf(multipleAccountIds))
                    .set(!multipleAccountAmounts.isEmpty(), ReceiptMain::getMultipleAccountAmount, String.valueOf(multipleAccountAmounts))
                    .set(!operatorIds.isEmpty(), ReceiptMain::getOperatorId, String.valueOf(operatorIds))
                    .set(saleOrderDTO.getAccountId() != null, ReceiptMain::getMultipleAccount, saleOrderDTO.getAccountId())
                    .set(ReceiptMain::getUpdateBy, userId)
                    .set(ReceiptMain::getUpdateTime, LocalDateTime.now())
                    .update();

            receiptSubService.lambdaUpdate()
                    .eq(ReceiptSub::getReceiptMainId, saleOrderDTO.getId())
                    .remove();

            var receiptSubList = saleOrderDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptSub.builder()
                            .receiptMainId(saleOrderDTO.getId())
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .productPrice(item.getUnitPrice())
                            .productTotalPrice(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .taxRate(item.getTaxRate())
                            .taxAmount(item.getTaxAmount())
                            .taxTotalPrice(item.getTaxTotalPrice())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = receiptSubService.saveBatch(receiptList);

            if (!saleOrderDTO.getFiles().isEmpty()) {
                var receiptMain = getById(saleOrderDTO.getId());
                if (receiptMain != null) {
                    var ids = Arrays.stream(receiptMain.getFileId().split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                    fileMapper.deleteBatchIds(ids);
                }
                saleOrderDTO.getFiles().forEach(item -> {
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

            if (updateMainResult && updateSubResult) {
                return Response.responseMsg(SaleCodeEnum.UPDATE_SALE_ORDER_SUCCESS);
            } else {
                return Response.responseMsg(SaleCodeEnum.UPDATE_SALE_ORDER_ERROR);
            }
        } else {
            var id = SnowflakeIdUtil.nextId();

            var fid = new ArrayList<>();
            if (!saleOrderDTO.getFiles().isEmpty()) {
                saleOrderDTO.getFiles().forEach(item -> {
                    var file = SysFile.builder()
                            .id(item.getId())
                            .uid(item.getUid())
                            .fileName(item.getFileName())
                            .fileType(item.getFileType())
                            .fileSize(item.getFileSize())
                            .fileUrl(item.getFileUrl())
                            .build();
                    var result = fileMapper.insert(file);
                    if (result > 0) {
                        fid.add(file.getId());
                    }
                });
            }
            var fileIds = fid.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            var receiptMain = ReceiptMain.builder()
                    .id(id)
                    .type(ReceiptConstants.RECEIPT_TYPE_ORDER)
                    .subType(ReceiptConstants.RECEIPT_SUB_TYPE_SALES_ORDER)
                    .initReceiptNumber(saleOrderDTO.getReceiptNumber())
                    .receiptNumber(saleOrderDTO.getReceiptNumber())
                    .customerId(saleOrderDTO.getCustomerId())
                    .operatorId(String.valueOf(operatorIds))
                    .discountRate(saleOrderDTO.getDiscountRate())
                    .accountId(saleOrderDTO.getAccountId())
                    .discountAmount(saleOrderDTO.getDiscountAmount())
                    .discountLastAmount(saleOrderDTO.getDiscountLastAmount())
                    .deposit(saleOrderDTO.getDeposit())
                    .remark(saleOrderDTO.getRemark())
                    .fileId(fileIds)
                    .status(saleOrderDTO.getStatus())
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            if(StringUtils.hasLength(multipleAccountIds)) {
                receiptMain.setMultipleAccount(String.valueOf(multipleAccountIds));
            }
            if(StringUtils.hasLength(multipleAccountAmounts)) {
                receiptMain.setMultipleAccountAmount(String.valueOf(multipleAccountAmounts));
            }
            var saveMainResult = save(receiptMain);

            var receiptSubList = saleOrderDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptSub.builder()
                            .receiptMainId(id)
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .productPrice(item.getUnitPrice())
                            .productTotalPrice(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .taxRate(item.getTaxRate())
                            .taxAmount(item.getTaxAmount())
                            .taxTotalPrice(item.getTaxTotalPrice())
                            .build())
                    .collect(Collectors.toList());

            var saveSubResult = receiptSubService.saveBatch(receiptList);

            if (saveMainResult && saveSubResult) {
                return Response.responseMsg(SaleCodeEnum.ADD_SALE_ORDER_SUCCESS);
            } else {
                return Response.responseMsg(SaleCodeEnum.ADD_SALE_ORDER_ERROR);
            }
        }
    }

    @Override
    public Response<String> deleteSaleOrder(List<Long> ids) {
        if(ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var updateResult = lambdaUpdate()
                .in(ReceiptMain::getId, ids)
                .set(ReceiptMain::getDeleteFlag, CommonConstants.DELETED)
                .update();
        if (updateResult) {
            return Response.responseMsg(SaleCodeEnum.DELETE_SALE_ORDER_SUCCESS);
        } else {
            return Response.responseMsg(SaleCodeEnum.DELETE_SALE_ORDER_ERROR);
        }

    }

    @Override
    public Response<String> updateSaleOrderStatus(List<Long> ids, Integer status) {
        if (ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptMain::getId, ids)
                .set(ReceiptMain::getStatus, status)
                .update();
        if (updateResult) {
            return Response.responseMsg(SaleCodeEnum.UPDATE_SALE_ORDER_SUCCESS);
        } else {
            return Response.responseMsg(SaleCodeEnum.UPDATE_SALE_ORDER_ERROR);
        }
    }

}
