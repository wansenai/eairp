package com.wansensoft.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wansensoft.dto.organization.OrganizationListDto;
import com.wansensoft.entities.user.SysUserOrgRel;
import com.wansensoft.mappers.system.SysDepartmentMapper;
import com.wansensoft.mappers.user.SysUserOrgRelMapper;
import com.wansensoft.service.system.ISysDepartmentService;
import com.wansensoft.entities.SysDepartment;
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
public class SysDepartmentServiceImpl extends ServiceImpl<SysDepartmentMapper, SysDepartment> implements ISysDepartmentService {

    private final ISysUserService userService;

    private final SysUserOrgRelMapper userOrgRelMapper;

    private final SysDepartmentMapper organizationMapper;

    public SysDepartmentServiceImpl(ISysUserService userService, SysUserOrgRelMapper userOrgRelMapper, SysDepartmentMapper organizationMapper) {
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
