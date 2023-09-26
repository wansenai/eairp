package com.wansensoft.service.materialAttribute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.material.MaterialAttribute;
import com.wansensoft.entities.material.MaterialAttributeExample;
import com.wansensoft.mappers.material.MaterialAttributeMapper;
import com.wansensoft.mappers.material.MaterialAttributeMapperEx;
import com.wansensoft.service.log.LogService;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.plugins.exception.BusinessRunTimeException;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialAttributeServiceImpl extends ServiceImpl<MaterialAttributeMapper, MaterialAttribute> implements MaterialAttributeService{
    private Logger logger = LoggerFactory.getLogger(MaterialAttributeServiceImpl.class);

    private final LogService logService;
    private final MaterialAttributeMapper materialAttributeMapper;
    private final MaterialAttributeMapperEx materialAttributeMapperEx;

    public MaterialAttributeServiceImpl(LogService logService, MaterialAttributeMapper materialAttributeMapper, MaterialAttributeMapperEx materialAttributeMapperEx) {
        this.logService = logService;
        this.materialAttributeMapper = materialAttributeMapper;
        this.materialAttributeMapperEx = materialAttributeMapperEx;
    }

    public MaterialAttribute getMaterialAttribute(long id) {
        MaterialAttribute result=null;
        try{
            result=materialAttributeMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<MaterialAttribute> getMaterialAttribute() {
        MaterialAttributeExample example = new MaterialAttributeExample();
        example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        example.setOrderByClause("id desc");
        List<MaterialAttribute> list=null;
        try{
            list=materialAttributeMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<MaterialAttribute> select(String attributeName, int offset, int rows) {
        List<MaterialAttribute> list = new ArrayList<>();
        try{
            list = materialAttributeMapperEx.selectByConditionMaterialAttribute(attributeName, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countMaterialAttribute(String attributeField) {
        Long result =null;
        try{
            result= 5L;
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertMaterialAttribute(JSONObject obj, HttpServletRequest request) {
        MaterialAttribute m = JSONObject.parseObject(obj.toJSONString(), MaterialAttribute.class);
        try{
            materialAttributeMapper.insertSelective(m);
            logService.insertLog("商品属性",
                    BusinessConstants.LOG_OPERATION_TYPE_ADD + m.getAttributeName(), request);
            return 1;
        }
        catch (BusinessRunTimeException ex) {
            throw new BusinessRunTimeException(ex.getCode(), ex.getMessage());
        }
        catch(Exception e){
            JshException.writeFail(logger, e);
            return 0;
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateMaterialAttribute(JSONObject obj, HttpServletRequest request) {
        MaterialAttribute materialAttribute = JSONObject.parseObject(obj.toJSONString(), MaterialAttribute.class);
        try{
            materialAttributeMapper.updateByPrimaryKeySelective(materialAttribute);
            logService.insertLog("商品属性",
                    BusinessConstants.LOG_OPERATION_TYPE_EDIT + materialAttribute.getAttributeName(), request);
            return 1;
        }catch(Exception e){
            JshException.writeFail(logger, e);
            return 0;
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteMaterialAttribute(Long id, HttpServletRequest request) {
        return batchDeleteMaterialAttributeByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteMaterialAttribute(String ids, HttpServletRequest request) {
        return batchDeleteMaterialAttributeByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteMaterialAttributeByIds(String ids) {
        String [] idArray=ids.split(",");
        try{
            return materialAttributeMapperEx.batchDeleteMaterialAttributeByIds(idArray);
        }catch(Exception e){
            JshException.writeFail(logger, e);
            return 0;
        }
    }

    public int checkIsNameExist(Long id, String name) {
        MaterialAttributeExample example = new MaterialAttributeExample();
        example.createCriteria().andIdNotEqualTo(id).andAttributeNameEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<MaterialAttribute> list =null;
        try{
            list = materialAttributeMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    public JSONArray getValueArrById(Long id) {
        JSONArray valueArr = new JSONArray();
        MaterialAttribute ma = getInfoById(id);
        if(ma!=null) {
            String value = ma.getAttributeValue();
            if(StringUtil.isNotEmpty(value)){
                String[] arr = value.split("\\|");
                for(String v: arr) {
                    JSONObject item = new JSONObject();
                    item.put("value",v);
                    item.put("name",v);
                    valueArr.add(item);
                }
            }
        }
        return valueArr;
    }

    public MaterialAttribute getInfoById(Long id) {
        MaterialAttributeExample example = new MaterialAttributeExample();
        example.createCriteria().andIdEqualTo(id).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<MaterialAttribute> list = materialAttributeMapper.selectByExample(example);
        if(list!=null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
