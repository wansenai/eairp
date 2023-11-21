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
package com.wansenai.api.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.basic.AddOrUpdateIncomeExpenseDTO;
import com.wansenai.dto.basic.QueryIncomeExpenseDTO;
import com.wansenai.service.basic.IncomeExpenseService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.basic.IncomeExpenseVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/basic/incomeExpense")
public class IncomeExpenseController {

    private final IncomeExpenseService incomeExpenseService;

    public IncomeExpenseController(IncomeExpenseService incomeExpenseService) {
        this.incomeExpenseService = incomeExpenseService;
    }

    @PostMapping("pageList")
    public Response<Page<IncomeExpenseVO>> getIncomeExpensePageList(@RequestBody QueryIncomeExpenseDTO queryIncomeExpenseDTO) {
        return incomeExpenseService.getIncomeExpensePageList(queryIncomeExpenseDTO);
    }

    @PostMapping("addOrUpdate")
    public Response<String> addOrUpdateIncomeExpense(@RequestBody AddOrUpdateIncomeExpenseDTO addOrUpdateIncomeExpenseDTO) {
        return incomeExpenseService.addOrUpdateIncomeExpense(addOrUpdateIncomeExpenseDTO);
    }

    @DeleteMapping("deleteBatch")
    public Response<String> deleteIncomeExpense(@RequestParam("ids") List<Long> ids) {
        return incomeExpenseService.deleteBatchIncomeExpense(ids);
    }

    @PostMapping("updateStatus")
    public Response<String> updateIncomeExpenseStatus(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return incomeExpenseService.updateIncomeExpenseStatus(ids, status);
    }

    @GetMapping("list/{type}")
    public Response<List<IncomeExpenseVO>> getIncomeExpenseList(@PathVariable("type") String type) {
        return incomeExpenseService.getIncomeExpenseListByType(type);
    }
}
