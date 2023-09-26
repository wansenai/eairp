package com.wansensoft.service.organization;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.organization.Organization;
import com.wansensoft.vo.TreeNode;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrganizationService extends IService<Organization> {

    Organization getOrganization(long id);

    List<Organization> getOrganizationListByIds(String ids);

    int insertOrganization(JSONObject obj, HttpServletRequest request);

    int updateOrganization(JSONObject obj, HttpServletRequest request);

    int deleteOrganization(Long id, HttpServletRequest request);

    int batchDeleteOrganization(String ids, HttpServletRequest request);

    int batchDeleteOrganizationByIds(String ids);

    int checkIsNameExist(Long id, String name);

    int addOrganization(Organization org);

    int editOrganization(Organization org);

    List<TreeNode> getOrganizationTree(Long id);

    List<Organization> findById(Long id);

    List<Organization> findByParentId(Long parentId);

    List<Organization> findByOrgNo(String orgNo);

    void checkOrgNoIsExists(String orgNo,Long id);

    List<Long> getOrgIdByParentId(Long orgId);

    void getOrgIdByParentNo(List<Long> idList,Long id);
}
