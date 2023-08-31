package com.wansensoft.service.depotItem;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.service.ICommonQuery;
import com.wansensoft.utils.Constants;
import com.wansensoft.utils.QueryUtils;
import com.wansensoft.utils.StringUtil;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
@DepotItemResource
public class DepotItemComponent implements ICommonQuery {

    private final DepotItemServiceImpl depotItemServiceImpl;

    public DepotItemComponent(DepotItemServiceImpl depotItemServiceImpl) {
        this.depotItemServiceImpl = depotItemServiceImpl;
    }

    @Override
    public Object selectOne(Long id) throws Exception {
        return depotItemServiceImpl.getDepotItem(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getDepotItemList(map);
    }

    private List<?> getDepotItemList(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        Integer type = StringUtil.parseInteger(StringUtil.getInfo(search, "type"));
        String remark = StringUtil.getInfo(search, "remark");
        String order = QueryUtils.order(map);
        return depotItemServiceImpl.select(name, type, remark, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        Integer type = StringUtil.parseInteger(StringUtil.getInfo(search, "type"));
        String remark = StringUtil.getInfo(search, "remark");
        return depotItemServiceImpl.countDepotItem(name, type, remark);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request)throws Exception {
        return depotItemServiceImpl.insertDepotItem(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return depotItemServiceImpl.updateDepotItem(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return depotItemServiceImpl.deleteDepotItem(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return depotItemServiceImpl.batchDeleteDepotItem(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return depotItemServiceImpl.checkIsNameExist(id, name);
    }

}
