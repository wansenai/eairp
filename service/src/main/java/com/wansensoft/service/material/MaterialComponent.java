package com.wansensoft.service.material;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.service.ICommonQuery;
import com.wansensoft.utils.Constants;
import com.wansensoft.utils.QueryUtils;
import com.wansensoft.utils.StringUtil;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "material_component")
@MaterialResource
public class MaterialComponent implements ICommonQuery {

    private final MaterialServiceImpl materialServiceImpl;

    public MaterialComponent(MaterialServiceImpl materialServiceImpl) {
        this.materialServiceImpl = materialServiceImpl;
    }

    @Override
    public Object selectOne(Long id) throws Exception {
        return materialServiceImpl.getMaterial(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getMaterialList(map);
    }

    private List<?> getMaterialList(Map<String, String> map) throws Exception{
        String search = map.get(Constants.SEARCH);
        String categoryId = StringUtil.getInfo(search, "categoryId");
        String materialParam = StringUtil.getInfo(search, "materialParam");
        String color = StringUtil.getInfo(search, "color");
        String materialOther = StringUtil.getInfo(search, "materialOther");
        String weight = StringUtil.getInfo(search, "weight");
        String expiryNum = StringUtil.getInfo(search, "expiryNum");
        String enableSerialNumber = StringUtil.getInfo(search, "enableSerialNumber");
        String enableBatchNumber = StringUtil.getInfo(search, "enableBatchNumber");
        String position = StringUtil.getInfo(search, "position");
        String enabled = StringUtil.getInfo(search, "enabled");
        String remark = StringUtil.getInfo(search, "remark");
        String mpList = StringUtil.getInfo(search, "mpList");
        return materialServiceImpl.select(materialParam, color, materialOther, weight, expiryNum,
                enableSerialNumber, enableBatchNumber, position, enabled, remark, categoryId, mpList, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String categoryId = StringUtil.getInfo(search, "categoryId");
        String materialParam = StringUtil.getInfo(search, "materialParam");
        String color = StringUtil.getInfo(search, "color");
        String materialOther = StringUtil.getInfo(search, "materialOther");
        String weight = StringUtil.getInfo(search, "weight");
        String expiryNum = StringUtil.getInfo(search, "expiryNum");
        String enableSerialNumber = StringUtil.getInfo(search, "enableSerialNumber");
        String enableBatchNumber = StringUtil.getInfo(search, "enableBatchNumber");
        String position = StringUtil.getInfo(search, "position");
        String enabled = StringUtil.getInfo(search, "enabled");
        String remark = StringUtil.getInfo(search, "remark");
        String mpList = StringUtil.getInfo(search, "mpList");
        return materialServiceImpl.countMaterial(materialParam, color, materialOther, weight, expiryNum,
                enableSerialNumber, enableBatchNumber, position, enabled, remark, categoryId, mpList);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception{
        return materialServiceImpl.insertMaterial(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return materialServiceImpl.updateMaterial(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return materialServiceImpl.deleteMaterial(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return materialServiceImpl.batchDeleteMaterial(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return materialServiceImpl.checkIsNameExist(id, name);
    }

}
