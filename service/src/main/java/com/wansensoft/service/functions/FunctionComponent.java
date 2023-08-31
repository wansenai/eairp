package com.wansensoft.service.functions;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.service.ICommonQuery;
import com.wansensoft.utils.Constants;
import com.wansensoft.utils.QueryUtils;
import com.wansensoft.utils.StringUtil;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "function_component")
@FunctionResource
public class FunctionComponent implements ICommonQuery {

    private final FunctionServiceImpl functionServiceImpl;

    public FunctionComponent(FunctionServiceImpl functionServiceImpl) {
        this.functionServiceImpl = functionServiceImpl;
    }

    @Override
    public Object selectOne(Long id) throws Exception {
        return functionServiceImpl.getFunction(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getFunctionsList(map);
    }

    private List<?> getFunctionsList(Map<String, String> map) throws Exception{
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String type = StringUtil.getInfo(search, "type");
        String order = QueryUtils.order(map);
        return functionServiceImpl.select(name, type, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map) throws Exception{
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String type = StringUtil.getInfo(search, "type");
        return functionServiceImpl.countFunction(name, type);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request)throws Exception {
        return functionServiceImpl.insertFunction(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return functionServiceImpl.updateFunction(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return functionServiceImpl.deleteFunction(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return functionServiceImpl.batchDeleteFunction(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return functionServiceImpl.checkIsNameExist(id, name);
    }

}
