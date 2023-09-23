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
import org.springframework.stereotype.Service;

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

    private final SysDepartmentMapper departmentMapper;

    public SysDepartmentServiceImpl(ISysUserService userService, SysUserDeptRelMapper userDeptRelMapper, SysDepartmentMapper departmentMapper) {
        this.userService = userService;
        this.userDeptRelMapper = userDeptRelMapper;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public Response<DeptListVO> deptList(DeptListDTO deptListDto) {
        return null;
    }

    @Override
    public Response<List<DeptListVO>> getUserDeptRel() {
        var results = new ArrayList<DeptListVO>(5);
        var tenantId = userService.getCurrentTenantId();

        var userRoleWrapper = new QueryWrapper<SysUserDeptRel>();
        userRoleWrapper.eq("tenant_id", tenantId);
        var userDeptRelList = userDeptRelMapper.selectList(userRoleWrapper)
                .stream().map(SysUserDeptRel::getDeptId).toList();

        var departments = departmentMapper.selectBatchIds(userDeptRelList);
        if(departments.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
        }
        // find children department only 2 leave
        departments.forEach(item -> {
            if(item.getParentId() == null) {
                // parent Dept Node
                var deptVo = new DeptListVO();
                deptVo.setId(item.getId());
                deptVo.setDeptName(item.getName());
                deptVo.setDeptNumber(item.getNumber());
                deptVo.setSort(item.getSort());
                deptVo.setRemark(item.getRemark());
                // Query Children Node List
                var deptQuery = new QueryWrapper<SysDepartment>();
                deptQuery.eq("parent_id", item.getId());
                var childrenDept = departmentMapper.selectList(deptQuery);
                // Assemble Children Node Data
                if (!childrenDept.isEmpty()) {
                    var DeptChildrenVOs = new ArrayList<DeptListVO.DeptChildrenVO>();
                    childrenDept.forEach(childrenItem -> {
                        var childrenDeptVo = new DeptListVO.DeptChildrenVO();
                        childrenDeptVo.setId(childrenItem.getId());
                        childrenDeptVo.setParentId(childrenItem.getParentId());
                        childrenDeptVo.setDeptName(childrenItem.getName());
                        childrenDeptVo.setDeptNumber(childrenItem.getNumber());
                        childrenDeptVo.setSort(childrenItem.getSort());
                        childrenDeptVo.setRemark(childrenItem.getRemark());

                        DeptChildrenVOs.add(childrenDeptVo);
                    });
                    deptVo.setChildren(DeptChildrenVOs);
                }
                // Add multiple child nodes to the parent node
                results.add(deptVo);
            }
        });
        return Response.responseData(results);
    }
}
