package com.wansensoft.service.materialProperty;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.material.MaterialProperty;
import com.wansensoft.entities.material.MaterialPropertyExample;
import com.wansensoft.entities.user.User;
import com.wansensoft.mappers.material.MaterialPropertyMapper;
import com.wansensoft.mappers.material.MaterialPropertyMapperEx;
import com.wansensoft.service.log.LogService;
import com.wansensoft.service.user.UserService;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.plugins.exception.JshException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class MaterialPropertyServiceImpl extends ServiceImpl<MaterialPropertyMapper, MaterialProperty> implements MaterialPropertyService {
    private Logger logger = LoggerFactory.getLogger(MaterialPropertyServiceImpl.class);

    private final MaterialPropertyMapper materialPropertyMapper;
    private final MaterialPropertyMapperEx materialPropertyMapperEx;
    private final UserService userService;
    private final LogService logService;

    public MaterialPropertyServiceImpl(MaterialPropertyMapper materialPropertyMapper, MaterialPropertyMapperEx materialPropertyMapperEx, UserService userService, LogService logService) {
        this.materialPropertyMapper = materialPropertyMapper;
        this.materialPropertyMapperEx = materialPropertyMapperEx;
        this.userService = userService;
        this.logService = logService;
    }

    public MaterialProperty getMaterialProperty(long id) {
        MaterialProperty result=null;
        try{
            result=materialPropertyMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<MaterialProperty> getMaterialProperty() {
        MaterialPropertyExample example = new MaterialPropertyExample();
        example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<MaterialProperty>  list=null;
        try{
            list=materialPropertyMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<MaterialProperty> select(String name, int offset, int rows) {
        List<MaterialProperty>  list=null;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(userService.getCurrentUser().getLoginName())) {
                list = materialPropertyMapperEx.selectByConditionMaterialProperty(name, offset, rows);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countMaterialProperty(String name) {
        Long  result=null;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(userService.getCurrentUser().getLoginName())) {
                result = materialPropertyMapperEx.countsByMaterialProperty(name);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertMaterialProperty(JSONObject obj, HttpServletRequest request) {
        MaterialProperty materialProperty = JSONObject.parseObject(obj.toJSONString(), MaterialProperty.class);
        int  result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(userService.getCurrentUser().getLoginName())) {
                result = materialPropertyMapper.insertSelective(materialProperty);
                logService.insertLog("商品属性",
                        BusinessConstants.LOG_OPERATION_TYPE_ADD + materialProperty.getNativeName(), request);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateMaterialProperty(JSONObject obj, HttpServletRequest request) {
        MaterialProperty materialProperty = JSONObject.parseObject(obj.toJSONString(), MaterialProperty.class);
        int  result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(userService.getCurrentUser().getLoginName())) {
                result = materialPropertyMapper.updateByPrimaryKeySelective(materialProperty);
                logService.insertLog("商品属性",
                        BusinessConstants.LOG_OPERATION_TYPE_EDIT + materialProperty.getNativeName(), request);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteMaterialProperty(Long id, HttpServletRequest request) {
        return batchDeleteMaterialPropertyByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteMaterialProperty(String ids, HttpServletRequest request) {
        return batchDeleteMaterialPropertyByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteMaterialPropertyByIds(String ids) {
        User userInfo= userService.getCurrentUser();
        String [] idArray=ids.split(",");
        int  result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(userService.getCurrentUser().getLoginName())) {
                result = materialPropertyMapperEx.batchDeleteMaterialPropertyByIds(new Date(), userInfo == null ? null : userInfo.getId(), idArray);
                logService.insertLog("商品属性",
                        BusinessConstants.LOG_OPERATION_TYPE_DELETE + ids,
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name) {
        return 0;
    }
}
