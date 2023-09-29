package com.wansensoft.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wansensoft.dto.organization.DeptListDTO;
import com.wansensoft.entities.user.SysUserDeptRel;
import com.wansensoft.mappers.system.SysDepartmentMapper;
import com.wansensoft.mappers.user.SysUserDeptRelMapper;
import com.wansensoft.service.system.ISysDepartmentService;
import com.wansensoft.entities.SysDepartment;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.service.user.ISysUserService;
import com.wansensoft.utils.enums.BaseCodeEnum;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.DeptListVO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

    private final SysUserDeptRelMapper userDeptRelMapper;

    public SysDepartmentServiceImpl(ISysUserService userService, SysUserDeptRelMapper userDeptRelMapper) {
        this.userService = userService;
        this.userDeptRelMapper = userDeptRelMapper;
    }

    @Override
    public Response<List<DeptListVO>> userDept() {
        var results = new ArrayList<DeptListVO>(10);
        var tenantId = userService.getCurrentTenantId();

        var userRoleWrapper = new QueryWrapper<SysUserDeptRel>();
        userRoleWrapper.eq("tenant_id", tenantId);
        userRoleWrapper.eq("user_id", userService.getCurrentUserId());
        var userDeptRelList = userDeptRelMapper.selectList(userRoleWrapper)
                .stream().map(SysUserDeptRel::getDeptId).toList();

        var departments = lambdaQuery()
                .in(SysDepartment::getId, userDeptRelList)
                .list();
        if(departments.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
        }

        return assemblePcNodesList(results, departments, null);
    }

    /**
     * P: Parent
     * C: Children
     * 组装部门树
     * @param results   返回结果
     * @param departments   部门列表
     * @return  Response<List<DeptListVO>>
     */
    @NotNull
    private Response<List<DeptListVO>> assemblePcNodesList(ArrayList<DeptListVO> results, List<SysDepartment> departments, String deptName) {
        if (deptName != null) {
            departments.forEach(item -> {
                var parent = lambdaQuery()
                        .eq(SysDepartment::getId, item.getParentId())
                        .one();
                var children = new ArrayList<DeptListVO>(3);
                var childrenVo = DeptListVO.builder()
                        .id(item.getId())
                        .deptNumber(item.getNumber())
                        .deptName(item.getName())
                        .remark(item.getRemark())
                        .sort(item.getSort())
                        .createTime(item.getCreateTime())
                        .build();
                children.add(childrenVo);
                var deptChildrenVO = DeptListVO.builder()
                        .id(parent.getId())
                        .parentId(parent.getParentId())
                        .deptNumber(parent.getNumber())
                        .deptName(parent.getName())
                        .remark(parent.getRemark())
                        .sort(parent.getSort())
                        .createTime(parent.getCreateTime())
                        .children(children)
                        .build();

                results.add(deptChildrenVO);
            });
        } else {
            departments.forEach(item -> {
                var deptListVO = DeptListVO.builder()
                        .id(item.getId())
                        .deptNumber(item.getNumber())
                        .deptName(item.getName())
                        .remark(item.getRemark())
                        .sort(item.getSort())
                        .createTime(item.getCreateTime())
                        .build();
                if (item.getParentId() == null) {
                    // 遍历父节点，然后找到父节点的子节点，parentId为父节点id的
                    // 把子节点放到父节点的children里面
                    var children = new ArrayList<DeptListVO>(10);
                    var childrenList = lambdaQuery()
                            .eq(SysDepartment::getParentId, item.getId())
                            .list();
                    childrenList.forEach(childrenItem -> {
                        var deptChildrenVO = DeptListVO.builder()
                                .id(childrenItem.getId())
                                .parentId(childrenItem.getParentId())
                                .deptNumber(childrenItem.getNumber())
                                .deptName(childrenItem.getName())
                                .remark(childrenItem.getRemark())
                                .sort(childrenItem.getSort())
                                .createTime(childrenItem.getCreateTime())
                                .build();
                        children.add(deptChildrenVO);
                        deptListVO.setChildren(children);
                    });
                    results.add(deptListVO);
                }
            });
        }

        return Response.responseData(results);
    }

    @Override
    public Response<List<DeptListVO>> getDeptList(String deptName) {
        var results = new ArrayList<DeptListVO>(10);
        var tenantId = userService.getCurrentTenantId();

        var departments = lambdaQuery()
                .in(SysDepartment::getTenantId, tenantId)
                .like(StringUtils.hasText(deptName), SysDepartment::getName, deptName)
                .list();

        return assemblePcNodesList(results, departments, deptName);
    }
}
