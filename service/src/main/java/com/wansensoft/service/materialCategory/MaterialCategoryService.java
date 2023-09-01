package com.wansensoft.service.materialCategory;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.material.MaterialCategory;
import com.wansensoft.vo.TreeNode;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface MaterialCategoryService extends IService<MaterialCategory> {

    MaterialCategory getMaterialCategory(long id);

    List<MaterialCategory> getMaterialCategoryListByIds(String ids);

    List<MaterialCategory> getMaterialCategory();

    List<MaterialCategory> getAllList(Long parentId);

    List<MaterialCategory> getMCList(Long parentId);

    List<MaterialCategory> select(String name, Integer parentId, int offset, int rows);

    Long countMaterialCategory(String name, Integer parentId);

    int insertMaterialCategory(JSONObject obj, HttpServletRequest request);

    int updateMaterialCategory(JSONObject obj, HttpServletRequest request);

    int deleteMaterialCategory(Long id, HttpServletRequest request);

    int batchDeleteMaterialCategory(String ids, HttpServletRequest request);

    int batchDeleteMaterialCategoryByIds(String ids);

    int checkIsNameExist(Long id, String name);

    List<MaterialCategory> findById(Long id);

    List<TreeNode> getMaterialCategoryTree(Long id);

    int addMaterialCategory(MaterialCategory mc);

    int editMaterialCategory(MaterialCategory mc);

    void checkMaterialCategorySerialNo(MaterialCategory mc);

    Long getCategoryIdByName(String name);
}
