package com.wansensoft.service.unit;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.entities.material.Material;
import com.wansensoft.entities.unit.Unit;
import com.wansensoft.entities.unit.UnitExample;
import com.wansensoft.entities.user.User;
import com.wansensoft.mappers.material.MaterialMapperEx;
import com.wansensoft.service.log.LogServiceImpl;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.utils.constants.ExceptionConstants;
import com.wansensoft.plugins.exception.BusinessRunTimeException;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.mappers.unit.UnitMapper;
import com.wansensoft.mappers.unit.UnitMapperEx;
import com.wansensoft.service.user.UserServiceImpl;
import com.wansensoft.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UnitService {
    private Logger logger = LoggerFactory.getLogger(UnitService.class);

    private final UnitMapper unitMapper;
    private final UnitMapperEx unitMapperEx;
    private final UserServiceImpl userServiceImpl;
    private final LogServiceImpl logServiceImpl;
    private final MaterialMapperEx materialMapperEx;

    public UnitService(UnitMapper unitMapper, UnitMapperEx unitMapperEx, UserServiceImpl userServiceImpl, LogServiceImpl logServiceImpl, MaterialMapperEx materialMapperEx) {
        this.unitMapper = unitMapper;
        this.unitMapperEx = unitMapperEx;
        this.userServiceImpl = userServiceImpl;
        this.logServiceImpl = logServiceImpl;
        this.materialMapperEx = materialMapperEx;
    }

    public Unit getUnit(long id) {
        Unit result=null;
        try{
            result=unitMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<Unit> getUnitListByIds(String ids)throws Exception {
        List<Long> idList = StringUtil.strToLongList(ids);
        List<Unit> list = new ArrayList<>();
        try{
            UnitExample example = new UnitExample();
            example.createCriteria().andIdIn(idList);
            list = unitMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Unit> getUnit()throws Exception {
        UnitExample example = new UnitExample();
        example.createCriteria().andEnabledEqualTo(true).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Unit> list=null;
        try{
            list=unitMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Unit> select(String name, int offset, int rows)throws Exception {
        List<Unit> list=null;
        try{
            list=unitMapperEx.selectByConditionUnit(name, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countUnit(String name)throws Exception {
        Long result=null;
        try{
            result=unitMapperEx.countsByUnit(name);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertUnit(JSONObject obj, HttpServletRequest request)throws Exception {
        Unit unit = JSONObject.parseObject(obj.toJSONString(), Unit.class);
        int result=0;
        try{
            parseNameByUnit(unit);
            unit.setEnabled(true);
            result=unitMapper.insertSelective(unit);
            logServiceImpl.insertLog("计量单位",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(unit.getName()).toString(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateUnit(JSONObject obj, HttpServletRequest request)throws Exception {
        Unit unit = JSONObject.parseObject(obj.toJSONString(), Unit.class);
        int result=0;
        try{
            parseNameByUnit(unit);
            result=unitMapper.updateByPrimaryKeySelective(unit);
            if(unit.getRatioTwo()==null) {
                unitMapperEx.updateRatioTwoById(unit.getId());
            }
            if(unit.getRatioThree()==null) {
                unitMapperEx.updateRatioThreeById(unit.getId());
            }
            logServiceImpl.insertLog("计量单位",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(unit.getName()).toString(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    /**
     * 根据单位信息生成名称的格式
     * @param unit
     */
    private void parseNameByUnit(Unit unit) {
        String unitName = unit.getBasicUnit() + "/" + "(" +  unit.getOtherUnit() + "=" + unit.getRatio().toString() + unit.getBasicUnit() + ")";
        if(StringUtil.isNotEmpty(unit.getOtherUnitTwo()) && unit.getRatioTwo()!=null) {
            unitName += "/" + "(" + unit.getOtherUnitTwo() + "=" + unit.getRatioTwo().toString() + unit.getBasicUnit() + ")";
            if(StringUtil.isNotEmpty(unit.getOtherUnitThree()) && unit.getRatioThree()!=null) {
                unitName += "/" + "(" + unit.getOtherUnitThree() + "=" + unit.getRatioThree().toString() + unit.getBasicUnit() + ")";
            }
        }
        unit.setName(unitName);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteUnit(Long id, HttpServletRequest request)throws Exception {
        return batchDeleteUnitByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteUnit(String ids, HttpServletRequest request) throws Exception{
        return batchDeleteUnitByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteUnitByIds(String ids)throws Exception {
        int result=0;
        String [] idArray=ids.split(",");
        //校验产品表	jsh_material
        List<Material> materialList=null;
        try{
            materialList=materialMapperEx.getMaterialListByUnitIds(idArray);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if(materialList!=null&&materialList.size()>0){
            logger.error("异常码[{}],异常提示[{}],参数,UnitIds[{}]",
                    ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,ExceptionConstants.DELETE_FORCE_CONFIRM_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,
                    ExceptionConstants.DELETE_FORCE_CONFIRM_MSG);
        }
        //记录日志
        StringBuffer sb = new StringBuffer();
        sb.append(BusinessConstants.LOG_OPERATION_TYPE_DELETE);
        List<Unit> list = getUnitListByIds(ids);
        for(Unit unit: list){
            sb.append("[").append(unit.getName()).append("]");
        }
        logServiceImpl.insertLog("计量单位", sb.toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        User userInfo= userServiceImpl.getCurrentUser();
        //校验通过执行删除操作
        try{
            result=unitMapperEx.batchDeleteUnitByIds(new Date(),userInfo==null?null:userInfo.getId(),idArray);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name)throws Exception {
        UnitExample example = new UnitExample();
        example.createCriteria().andIdNotEqualTo(id).andNameEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Unit> list=null;
        try{
            list=unitMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    /**
     * 根据条件查询单位id
     * @param basicUnit
     * @param otherUnit
     * @param ratio
     * @return
     */
    public Long getUnitIdByParam(String basicUnit, String otherUnit, BigDecimal ratio){
        Long unitId = null;
        UnitExample example = new UnitExample();
        example.createCriteria().andBasicUnitEqualTo(basicUnit).andOtherUnitEqualTo(otherUnit).andRatioEqualTo(ratio)
                .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Unit> list = unitMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            unitId = list.get(0).getId();
        }
        return unitId;
    }

    /**
     * 根据多单位的比例进行库存换算（保留两位小数）
     * @param stock
     * @param unitInfo
     * @param materialUnit
     * @return
     */
    public BigDecimal parseStockByUnit(BigDecimal stock, Unit unitInfo, String materialUnit) {
        if(materialUnit.equals(unitInfo.getOtherUnit()) && unitInfo.getRatio()!=null && unitInfo.getRatio().compareTo(BigDecimal.ZERO)!=0) {
            stock = stock.divide(unitInfo.getRatio(),2,BigDecimal.ROUND_HALF_UP);
        }
        if(materialUnit.equals(unitInfo.getOtherUnitTwo()) && unitInfo.getRatioTwo()!=null && unitInfo.getRatioTwo().compareTo(BigDecimal.ZERO)!=0) {
            stock = stock.divide(unitInfo.getRatioTwo(),2,BigDecimal.ROUND_HALF_UP);
        }
        if(materialUnit.equals(unitInfo.getOtherUnitThree()) && unitInfo.getRatioThree()!=null && unitInfo.getRatioThree().compareTo(BigDecimal.ZERO)!=0) {
            stock = stock.divide(unitInfo.getRatioThree(),2,BigDecimal.ROUND_HALF_UP);
        }
        return stock;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchSetStatus(Boolean status, String ids)throws Exception {
        logServiceImpl.insertLog("计量单位",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ENABLED).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        List<Long> unitIds = StringUtil.strToLongList(ids);
        Unit unit = new Unit();
        unit.setEnabled(status);
        UnitExample example = new UnitExample();
        example.createCriteria().andIdIn(unitIds);
        int result=0;
        try{
            result = unitMapper.updateByExampleSelective(unit, example);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
}
