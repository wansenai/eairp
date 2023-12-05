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
package com.wansenai.service.financial;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansenai.dto.financial.AddOrUpdateExpenseDTO;
import com.wansenai.dto.financial.QueryExpenseDTO;
import com.wansenai.entities.financial.FinancialMain;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.financial.ExpenseDetailVO;
import com.wansenai.vo.financial.ExpenseVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ExpenseReceiptService extends IService<FinancialMain> {

    Response<Page<ExpenseVO>> getExpenseReceiptPageList(QueryExpenseDTO queryExpenseDTO);

    Response<ExpenseDetailVO> getExpenseReceiptDetail(Long id);

    Response<String> addOrUpdateExpenseReceipt(AddOrUpdateExpenseDTO addOrUpdateExpenseDTO);

    Response<String> deleteBatchExpenseReceipt(List<Long> ids);

    Response<String> updateExpenseReceiptStatus(List<Long> ids, Integer status);

    void exportExpenseReceipt(QueryExpenseDTO queryExpenseDTO, HttpServletResponse response);

    void exportExpenseReceiptDetail(String receiptNumber, HttpServletResponse response);
}
