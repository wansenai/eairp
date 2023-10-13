/*
 * Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://opensource.wansenai.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.wansensoft.service.system.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansensoft.dto.department.AddOrUpdateDeptDTO
import com.wansensoft.entities.SysDepartment
import com.wansensoft.entities.user.SysUserDeptRel
import com.wansensoft.mappers.system.SysDepartmentMapper
import com.wansensoft.mappers.user.SysUserDeptRelMapper
import com.wansensoft.service.system.SysDepartmentService
import com.wansensoft.service.user.ISysUserService
import com.wansensoft.utils.SnowflakeIdUtil
import com.wansensoft.utils.constants.CommonConstants
import com.wansensoft.utils.enums.BaseCodeEnum
import com.wansensoft.utils.enums.DeptCodeEnum
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.DeptListVO
import org.jetbrains.annotations.NotNull
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.time.LocalDateTime

@Service
open class SysDepartmentServiceImpl(
    private val userService: ISysUserService,
    private val userDeptRelMapper: SysUserDeptRelMapper
) : ServiceImpl<SysDepartmentMapper, SysDepartment>(), SysDepartmentService {

    override fun userDept(): Response<List<DeptListVO>> {
        val results = ArrayList<DeptListVO>(10)

        val userRoleWrapper = QueryWrapper<SysUserDeptRel>()
            .eq("user_id", userService.getCurrentUserId())
        val userDeptRelList = userDeptRelMapper.selectList(userRoleWrapper)
            .stream().map(SysUserDeptRel::getDeptId).toList()

        val departments = lambdaQuery()
            .`in`(SysDepartment::getId, userDeptRelList)
            .eq(SysDepartment::getDeleteFlag, CommonConstants.NOT_DELETED)
            .list()
        if (departments.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY)
        }

        return assemblePcNodesList(results, departments, null)
    }

    /**
     * P: Parent
     * C: Children
     * 组装部门树
     * @param results 返回结果
     * @param departments 部门列表
     * @return Response<List<DeptListVO>>
     */
    @NotNull
    private fun assemblePcNodesList(
        results: ArrayList<DeptListVO>,
        departments: List<SysDepartment>,
        deptName: String?
    ): Response<List<DeptListVO>> {
        if (deptName != null) {
            departments.forEach { item ->
                val parent = lambdaQuery()
                    .eq(SysDepartment::getId, item.parentId)
                    .eq(SysDepartment::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .one()
                val children = ArrayList<DeptListVO>(3)
                val childrenVo = DeptListVO.builder()
                    .id(item.id)
                    .parentId(item.parentId)
                    .deptNumber(item.number)
                    .deptName(item.name)
                    .status(item.status)
                    .leader(item.leader)
                    .remark(item.remark)
                    .sort(item.sort)
                    .createTime(item.createTime)
                    .build()
                children.add(childrenVo)
                val deptChildrenVO = DeptListVO.builder()
                    .id(parent.id)
                    .parentId(parent.parentId)
                    .deptNumber(parent.number)
                    .deptName(parent.name)
                    .status(item.status)
                    .leader(item.leader)
                    .remark(parent.remark)
                    .sort(parent.sort)
                    .createTime(parent.createTime)
                    .children(children)
                    .build()

                results.add(deptChildrenVO)
            }
        } else {
            departments.forEach { item ->
                val deptListVO = DeptListVO.builder()
                    .id(item.id)
                    .parentId(item.parentId)
                    .deptNumber(item.number)
                    .deptName(item.name)
                    .status(item.status)
                    .leader(item.leader)
                    .remark(item.remark)
                    .sort(item.sort)
                    .createTime(item.createTime)
                    .build()
                if (item.parentId == null) {
                    val children = ArrayList<DeptListVO>(10)
                    val childrenList = lambdaQuery()
                        .eq(SysDepartment::getParentId, item.id)
                        .eq(SysDepartment::getDeleteFlag, CommonConstants.NOT_DELETED)
                        .list()
                    childrenList.forEach { childrenItem ->
                        val deptChildrenVO = DeptListVO.builder()
                            .id(childrenItem.id)
                            .parentId(childrenItem.parentId)
                            .deptNumber(childrenItem.number)
                            .deptName(childrenItem.name)
                            .status(childrenItem.status)
                            .leader(childrenItem.leader)
                            .remark(childrenItem.remark)
                            .sort(childrenItem.sort)
                            .createTime(childrenItem.createTime)
                            .build()
                        children.add(deptChildrenVO)
                        deptListVO.children = children
                    }
                    results.add(deptListVO)
                }
            }
        }

        return Response.responseData(results)
    }

    override fun getDeptList(deptName: String?): Response<List<DeptListVO>> {
        val results = ArrayList<DeptListVO>(10)
        val tenantId = userService.getCurrentTenantId()

        val departments = lambdaQuery()
            .eq(StringUtils.hasText(deptName), SysDepartment::getName, deptName)
            .`in`(SysDepartment::getTenantId, tenantId)
            .eq(SysDepartment::getDeleteFlag, CommonConstants.NOT_DELETED)
            .list()

        return assemblePcNodesList(results, departments, deptName)
    }

    override fun addOrSaveDept(addOrUpdateDeptDTO: AddOrUpdateDeptDTO?): Response<String> {
        addOrUpdateDeptDTO?.let { dto ->
            if (dto.id == null) {
                val userId = userService.getCurrentTenantId().toLong()
                val dept = SysDepartment.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .tenantId(userId)
                    .parentId(dto.parentId)
                    .name(dto.deptName)
                    .number(dto.deptName)
                    .status(dto.status)
                    .leader(dto.leader)
                    .remark(dto.remark)
                    .sort(dto.sort)
                    .createTime(LocalDateTime.now())
                    .build()
                val saveResult = save(dept)
                // add user_dept_rel
                val userDeptRel = SysUserDeptRel.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .tenantId(userId)
                    .userId(userId)
                    .deptId(dept.id)
                    .createTime(LocalDateTime.now())
                    .build()
                userDeptRelMapper.insert(userDeptRel)
                if (!saveResult) {
                    return Response.responseMsg(DeptCodeEnum.ADD_DEPARTMENT_ERROR)
                }
                return Response.responseMsg(DeptCodeEnum.ADD_DEPARTMENT_SUCCESS)
            } else {
                val saveResult = lambdaUpdate()
                    .eq(SysDepartment::getId, dto.id)
                    .apply {
                        set(StringUtils.hasText(dto.deptName), SysDepartment::getName, dto.deptName)
                        set(StringUtils.hasText(dto.deptNumber), SysDepartment::getNumber, dto.deptNumber)
                        set(dto.parentId != null, SysDepartment::getParentId, dto.parentId)
                        set(dto.status != null, SysDepartment::getStatus, dto.status)
                        set(StringUtils.hasText(dto.leader), SysDepartment::getLeader, dto.leader)
                        set(StringUtils.hasText(dto.remark), SysDepartment::getRemark, dto.remark)
                        set(StringUtils.hasText(dto.sort), SysDepartment::getSort, dto.sort)
                        set(SysDepartment::getUpdateTime, LocalDateTime.now())
                    }
                    .update()

                if (!saveResult) {
                    return Response.responseMsg(DeptCodeEnum.UPDATE_DEPARTMENT_ERROR)
                }
                return Response.responseMsg(DeptCodeEnum.UPDATE_DEPARTMENT_SUCCESS)
            }
        }
        return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

    override fun deleteDept(id: String?): Response<String> {
        if (id.isNullOrBlank()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
        }

        val deleteResult = lambdaUpdate()
            .eq(SysDepartment::getId, id)
            .set(SysDepartment::getDeleteFlag, CommonConstants.DELETED)
            .update()

        if (!deleteResult) {
            return Response.responseMsg(DeptCodeEnum.DELETE_DEPARTMENT_ERROR)
        }
        return Response.responseMsg(DeptCodeEnum.DELETE_DEPARTMENT_SUCCESS)
    }
}