package com.wansensoft.mappers.organization;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.organization.OrgaUserRel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrgaUserRelMapperEx extends BaseMapper<OrgaUserRel> {

    int addOrgaUserRel(OrgaUserRel orgaUserRel);

    int updateOrgaUserRel(OrgaUserRel orgaUserRel);
}
