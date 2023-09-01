package com.wansensoft.service.materialProperty;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.material.MaterialProperty;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface MaterialPropertyService extends IService<MaterialProperty> {

    MaterialProperty getMaterialProperty(long id);

    List<MaterialProperty> getMaterialProperty();

    List<MaterialProperty> select(String name, int offset, int rows);

    Long countMaterialProperty(String name);

    int insertMaterialProperty(JSONObject obj, HttpServletRequest request);

    int updateMaterialProperty(JSONObject obj, HttpServletRequest request);

    int deleteMaterialProperty(Long id, HttpServletRequest request);

    int batchDeleteMaterialProperty(String ids, HttpServletRequest request);

    int batchDeleteMaterialPropertyByIds(String ids);

    int checkIsNameExist(Long id, String name);
}
