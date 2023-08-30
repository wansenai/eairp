package com.wansensoft.materialCategory;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.service.ICommonQuery;
import com.wansensoft.utils.Constants;
import com.wansensoft.utils.QueryUtils;
import com.wansensoft.utils.StringUtil;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "materialCategory_component")
@MaterialCategoryResource
public class MaterialCategoryComponent implements ICommonQuery {

    @Resource
    private MaterialCategoryService materialCategoryService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return materialCategoryService.getMaterialCategory(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getMaterialCategoryList(map);
    }

    private List<?> getMaterialCategoryList(Map<String, String> map) throws Exception{
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        Integer parentId = StringUtil.parseInteger(StringUtil.getInfo(search, "parentId"));
        String order = QueryUtils.order(map);
        return materialCategoryService.select(name, parentId, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        Integer parentId = StringUtil.parseInteger(StringUtil.getInfo(search, "parentId"));
        return materialCategoryService.countMaterialCategory(name, parentId);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request)throws Exception {
        return materialCategoryService.insertMaterialCategory(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return materialCategoryService.updateMaterialCategory(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return materialCategoryService.deleteMaterialCategory(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return materialCategoryService.batchDeleteMaterialCategory(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return materialCategoryService.checkIsNameExist(id, name);
    }

}
