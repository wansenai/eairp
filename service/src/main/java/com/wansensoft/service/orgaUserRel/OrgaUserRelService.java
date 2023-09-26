package com.wansensoft.service.orgaUserRel;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.organization.OrgaUserRel;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrgaUserRelService extends IService<OrgaUserRel> {

    OrgaUserRel getOrgaUserRel(long id);

    int insertOrgaUserRel(JSONObject obj, HttpServletRequest request);

    int updateOrgaUserRel(JSONObject obj, HttpServletRequest request);

    int deleteOrgaUserRel(Long id, HttpServletRequest request);

    int batchDeleteOrgaUserRel(String ids, HttpServletRequest request);

    OrgaUserRel addOrgaUserRel(OrgaUserRel orgaUserRel);

    OrgaUserRel updateOrgaUserRel(OrgaUserRel orgaUserRel);

    String getUserIdListByUserId(Long userId);

    List<Long> getUserIdListByOrgId(Long orgId);
}
