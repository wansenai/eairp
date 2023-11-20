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
package com.wansenai.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.basic.AddOrUpdateIncomeExpenseDTO;
import com.wansenai.dto.basic.QueryIncomeExpenseDTO;
import com.wansenai.entities.IncomeExpense;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.basic.IncomeExpenseVO;

import java.util.List;

public interface IncomeExpenseService extends IService<IncomeExpense> {

    Response<Page<IncomeExpenseVO>> getIncomeExpensePageList(QueryIncomeExpenseDTO queryIncomeExpenseDTO);

    Response<String> addOrUpdateIncomeExpense(AddOrUpdateIncomeExpenseDTO addOrUpdateIncomeExpenseDTO);

    Response<String> deleteBatchIncomeExpense(List<Long> ids);

    Response<String> updateIncomeExpenseStatus(List<Long> ids, Integer status);

    Response<List<IncomeExpenseVO>> getIncomeExpenseListByType(String type);
}
