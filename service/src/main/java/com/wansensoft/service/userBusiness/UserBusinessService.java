package com.wansensoft.service.userBusiness;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.user.UserBusiness;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserBusinessService extends IService<UserBusiness> {

    UserBusiness getUserBusiness(long id);

    List<UserBusiness> getUserBusiness();

    int insertUserBusiness(JSONObject obj, HttpServletRequest request);

    int updateUserBusiness(JSONObject obj, HttpServletRequest request);

    int deleteUserBusiness(Long id, HttpServletRequest request);

    int batchDeleteUserBusiness(String ids, HttpServletRequest request);

    int batchDeleteUserBusinessByIds(String ids);

    List<UserBusiness> getBasicData(String keyId, String type);

    List<UserBusiness> getListBy(String keyId, String type);

    String getUBValueByTypeAndKeyId(String type, String keyId);

    Long checkIsValueExist(String type, String keyId);

    int updateBtnStr(String keyId, String type, String btnStr);
}
