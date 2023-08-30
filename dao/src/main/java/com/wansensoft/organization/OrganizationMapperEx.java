package com.wansensoft.organization;

import com.wansensoft.entities.organization.Organization;
import com.wansensoft.vo.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrganizationMapperEx {


    List<TreeNode> getNodeTree(@Param("currentId")Long currentId);
    List<TreeNode> getNextNodeTree(Map<String, Object> parameterMap);

    int addOrganization(Organization org);

    List <Organization> getOrganizationByParentIds(@Param("ids") String ids[]);

    int batchDeleteOrganizationByIds(@Param("updateTime") Date updateTime, @Param("updater") Long updater, @Param("ids") String ids[]);

    int editOrganization(Organization org);
    List <Organization> getOrganizationRootByIds(@Param("ids") String ids[]);
}
