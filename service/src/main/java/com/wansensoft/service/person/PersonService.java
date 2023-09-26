package com.wansensoft.service.person;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.person.Person;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface PersonService extends IService<Person> {

    Person getPerson(long id);

    List<Person> getPersonListByIds(String ids);

    List<Person> getPerson();

    List<Person> select(String name, String type, int offset, int rows);

    Long countPerson(String name, String type);

    int insertPerson(JSONObject obj, HttpServletRequest request);

    int updatePerson(JSONObject obj, HttpServletRequest request);

    int deletePerson(Long id, HttpServletRequest request);

    int batchDeletePerson(String ids, HttpServletRequest request);

    int batchDeletePersonByIds(String ids);

    int checkIsNameExist(Long id, String name);

    Map<Long,String> getPersonMap();

    String getPersonByMapAndIds(Map<Long,String> personMap, String personIds);

    List<Person> getPersonByType(String type);

    int batchSetStatus(Boolean status, String ids);


}
