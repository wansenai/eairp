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
package com.wansenai.api.system

import com.wansenai.dto.department.AddOrUpdateDeptDTO
import com.wansenai.service.system.SysDepartmentService
import com.wansenai.utils.response.Response
import com.wansenai.vo.DeptListVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dept")
class SysDepartmentController(private val departmentService: SysDepartmentService) {

    @GetMapping("list")
    fun getDeptList(@RequestParam(value = "deptName", required = false) deptName: String?): Response<List<DeptListVO>> {
        return departmentService.getDeptList(deptName)
    }

    @GetMapping("userBindDept")
    fun userDept(): Response<List<DeptListVO>> {
        return departmentService.userDept()
    }

    @PostMapping("addOrUpdate")
    fun addOrUpdate(@RequestBody addOrUpdateDeptDTO: AddOrUpdateDeptDTO?): Response<String> {
        return departmentService.addOrSaveDept(addOrUpdateDeptDTO)
    }

    @PostMapping("delete")
    fun deleteDept(@RequestParam(value = "id", required = true) id: String?): Response<String> {
        return departmentService.deleteDept(id)
    }
}