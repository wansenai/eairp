package com.wansensoft.organization;

import com.wansensoft.entities.organization.OrgaUserRel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrgaUserRelMapperEx {

    int addOrgaUserRel(OrgaUserRel orgaUserRel);

    int updateOrgaUserRel(OrgaUserRel orgaUserRel);
}
