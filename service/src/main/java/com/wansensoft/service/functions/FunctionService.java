package com.wansensoft.service.functions;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.function.Function;
import com.wansensoft.entities.function.FunctionEx;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface FunctionService extends IService<Function> {

    Function getFunction(long id);

    List<Function> getFunctionListByIds(String ids);

    List<Function> getFunction();

    List<FunctionEx> select(String name, String type, int offset, int rows);

    Long countFunction(String name, String type);

    int insertFunction(JSONObject obj, HttpServletRequest request);

    int updateFunction(JSONObject obj, HttpServletRequest request);

    int deleteFunction(Long id, HttpServletRequest request);

    int batchDeleteFunction(String ids, HttpServletRequest request);

    int batchDeleteFunctionByIds(String ids);

    int checkIsNameExist(Long id, String name);

    int checkIsNumberExist(Long id, String number);

    List<Function> getRoleFunction(String pNumber);

    List<Function> findRoleFunction(String pNumber);

    List<Function> findByIds(String functionsIds);
}
