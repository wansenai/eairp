package com.wansensoft.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wansensoft.dto.organization.OrganizationListDto;
import com.wansensoft.entities.user.SysUserOrgRel;
import com.wansensoft.mappers.user.SysUserOrgRelMapper;
import com.wansensoft.service.system.ISysOrganizationService;
import com.wansensoft.entities.SysOrganization;
import com.wansensoft.mappers.system.SysOrganizationMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.service.user.ISysUserService;
import com.wansensoft.utils.enums.CodeEnum;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.OrganizationListVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 机构表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class SysOrganizationServiceImpl extends ServiceImpl<SysOrganizationMapper, SysOrganization> implements ISysOrganizationService {

    private final ISysUserService userService;

    private final SysUserOrgRelMapper userOrgRelMapper;

    private final SysOrganizationMapper organizationMapper;

    public SysOrganizationServiceImpl(ISysUserService userService, SysUserOrgRelMapper userOrgRelMapper, SysOrganizationMapper organizationMapper) {
        this.userService = userService;
        this.userOrgRelMapper = userOrgRelMapper;
        this.organizationMapper = organizationMapper;
    }

    @Override
    public Response<OrganizationListVo> queryOrganization(OrganizationListDto organizationListDto) {
        if (organizationListDto == null) {
            return Response.responseMsg(CodeEnum.PARAMETER_NULL);
        }

        var userId = userService.getCurrentUserId();

        var userRoleWrapper = new QueryWrapper<SysUserOrgRel>();
        userRoleWrapper.eq("user_id", userId);
        var userOrgRelList = userOrgRelMapper.selectList(userRoleWrapper)
                .stream().map(SysUserOrgRel::getOrganizationId).toList();

        var organizations = organizationMapper.selectBatchIds(userOrgRelList);


        return null;
    }
}
