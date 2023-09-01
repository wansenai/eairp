package com.wansensoft.service.materialAttribute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.material.MaterialAttribute;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface MaterialAttributeService extends IService<MaterialAttribute> {

    MaterialAttribute getMaterialAttribute(long id);

    List<MaterialAttribute> getMaterialAttribute();

    List<MaterialAttribute> select(String attributeName, int offset, int rows);

    Long countMaterialAttribute(String attributeField);

    int insertMaterialAttribute(JSONObject obj, HttpServletRequest request);

    int updateMaterialAttribute(JSONObject obj, HttpServletRequest request);

    int deleteMaterialAttribute(Long id, HttpServletRequest request);

    int batchDeleteMaterialAttribute(String ids, HttpServletRequest request);

    int batchDeleteMaterialAttributeByIds(String ids);

    int checkIsNameExist(Long id, String name);

    JSONArray getValueArrById(Long id);

    MaterialAttribute getInfoById(Long id);
}
