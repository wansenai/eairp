package com.wansensoft.erp.service.person;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.erp.service.ICommonQuery;
import com.wansensoft.erp.utils.Constants;
import com.wansensoft.erp.utils.QueryUtils;
import com.wansensoft.erp.utils.StringUtil;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "person_component")
@PersonResource
public class PersonComponent implements ICommonQuery {

    @Resource
    private PersonService personService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return personService.getPerson(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getPersonList(map);
    }

    private List<?> getPersonList(Map<String, String> map) throws Exception{
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String type = StringUtil.getInfo(search, "type");
        String order = QueryUtils.order(map);
        return personService.select(name, type, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map) throws Exception{
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String type = StringUtil.getInfo(search, "type");
        return personService.countPerson(name, type);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request)throws Exception {
        return personService.insertPerson(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return personService.updatePerson(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return personService.deletePerson(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return personService.batchDeletePerson(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return personService.checkIsNameExist(id, name);
    }

}
