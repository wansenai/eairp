package com.wansensoft.service;

import com.wansensoft.entities.account.AccountItem;
import com.wansensoft.entities.material.Material;
import com.wansensoft.entities.material.MaterialInitialStock;
import com.wansensoft.entities.material.MaterialInitialStockExample;
import com.wansensoft.entities.material.MaterialVo4Unit;
import com.wansensoft.entities.role.Role;
import com.wansensoft.entities.supplier.Supplier;
import com.wansensoft.entities.supplier.SupplierExample;
import com.wansensoft.entities.unit.Unit;
import com.wansensoft.entities.user.User;
import com.wansensoft.mappers.account.AccountHeadMapperEx;
import com.wansensoft.mappers.account.AccountItemMapperEx;
import com.wansensoft.mappers.material.MaterialInitialStockMapper;
import com.wansensoft.mappers.material.MaterialMapper;
import com.wansensoft.mappers.material.MaterialMapperEx;
import com.wansensoft.mappers.role.RoleMapperEx;
import com.wansensoft.mappers.supplier.SupplierMapper;
import com.wansensoft.mappers.user.UserMapper;
import com.wansensoft.service.redis.RedisService;
import com.wansensoft.utils.constants.BusinessConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class CommonService {

    private final RedisService redisService;

    private final UserMapper userMapper;

    private final RoleMapperEx roleMapperEx;

    private final MaterialMapper materialMapper;

    private final MaterialMapperEx materialMapperEx;

    private final MaterialInitialStockMapper materialInitialStockMapper;

    private final SupplierMapper supplierMapper;

    private final AccountHeadMapperEx accountHeadMapperEx;

    private final AccountItemMapperEx accountItemMapperEx;

    public CommonService(RedisService redisService, UserMapper userMapper, RoleMapperEx roleMapperEx, MaterialMapper materialMapper, MaterialMapperEx materialMapperEx, MaterialInitialStockMapper materialInitialStockMapper, SupplierMapper supplierMapper, AccountHeadMapperEx accountHeadMapperEx, AccountItemMapperEx accountItemMapperEx) {
        this.redisService = redisService;
        this.userMapper = userMapper;
        this.roleMapperEx = roleMapperEx;
        this.materialMapper = materialMapper;
        this.materialMapperEx = materialMapperEx;
        this.materialInitialStockMapper = materialInitialStockMapper;
        this.supplierMapper = supplierMapper;
        this.accountHeadMapperEx = accountHeadMapperEx;
        this.accountItemMapperEx = accountItemMapperEx;
    }

    public User getCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        long userId = Long.parseLong(redisService.getObjectFromSessionByKey(request,"userId").toString());
        return userMapper.selectByPrimaryKey(userId);
    }

    public Long getUserId(HttpServletRequest request) {
        Object userIdObj = redisService.getObjectFromSessionByKey(request,"userId");
        Long userId = null;
        if(userIdObj != null) {
            userId = Long.parseLong(userIdObj.toString());
        }
        return userId;
    }

    public Role getRoleWithoutTenant(Long roleId) {
        return roleMapperEx.getRoleWithoutTenant(roleId);
    }

    public Material getMaterial(long id) {
        Material result=null;
        try{
            result = materialMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public Unit findUnit(Long mId){
        Unit unit = new Unit();
        try{
            List<Unit> list = materialMapperEx.findUnitList(mId);
            if(list!=null && !list.isEmpty()) {
                unit = list.get(0);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return unit;
    }

    public BigDecimal getInitStockByMidAndDepotList(List<Long> depotList, Long materialId) {
        BigDecimal stock = BigDecimal.ZERO;
        MaterialInitialStockExample example = new MaterialInitialStockExample();
        if(depotList!=null && !depotList.isEmpty()) {
            example.createCriteria().andMaterialIdEqualTo(materialId).andDepotIdIn(depotList)
                    .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        } else {
            example.createCriteria().andMaterialIdEqualTo(materialId)
                    .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        }
        List<MaterialInitialStock> list = materialInitialStockMapper.selectByExample(example);
        if(list!=null && !list.isEmpty()) {
            for(MaterialInitialStock ms: list) {
                if(ms!=null) {
                    stock = stock.add(ms.getNumber());
                }
            }
        }
        return stock;
    }

    public List<MaterialVo4Unit> getMaterialByBarCode(String barCode) {
        String [] barCodeArray=barCode.split(",");
        return materialMapperEx.getMaterialByBarCode(barCodeArray);
    }

    public List<Supplier> findBySelectCus() {
        SupplierExample example = new SupplierExample();
        example.createCriteria().andTypeLike("客户").andEnabledEqualTo(true).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        example.setOrderByClause("sort asc, id desc");
        List<Supplier> list=null;
        try{
            list = supplierMapper.selectByExample(example);
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public int updateAdvanceIn(Long supplierId, BigDecimal advanceIn) {
        Supplier supplier;
        supplier = supplierMapper.selectByPrimaryKey(supplierId);
        int result=0;
        if(supplier!=null) {
            supplier.setAdvanceIn(supplier.getAdvanceIn().add(advanceIn));  //增加预收款的金额，可能增加的是负值
            result = supplierMapper.updateByPrimaryKeySelective(supplier);
        }
        return result;
    }

    public Supplier getSupplier(long id) {
        Supplier result=null;
        try{
            result=supplierMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<AccountItem> getFinancialBillNoByBillIdList(List<Long> idList) {
        return accountHeadMapperEx.getFinancialBillNoByBillIdList(idList);
    }

    public BigDecimal getEachAmountByBillId(Long billId) {
        return accountItemMapperEx.getEachAmountByBillId(billId).abs();
    }
}
