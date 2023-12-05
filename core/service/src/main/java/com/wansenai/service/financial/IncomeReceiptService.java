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
import com.wansenai.dto.financial.AddOrUpdateIncomeDTO;
import com.wansenai.dto.financial.QueryIncomeDTO;
import com.wansenai.entities.financial.FinancialMain;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.financial.IncomeDetailVO;
import com.wansenai.vo.financial.IncomeVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface IncomeReceiptService extends IService<FinancialMain> {

    Response<Page<IncomeVO>> getIncomeReceiptPageList(QueryIncomeDTO queryIncomeDTO);

    Response<IncomeDetailVO> getIncomeReceiptDetail(Long id);

    Response<String> addOrUpdateIncomeReceipt(AddOrUpdateIncomeDTO addOrUpdateIncomeDTO);

    Response<String> deleteBatchIncomeReceipt(List<Long> ids);

    Response<String> updateIncomeReceiptStatus(List<Long> ids, Integer status);

    void exportIncomeReceipt(QueryIncomeDTO queryIncomeDTO, HttpServletResponse response);

    void exportIncomeReceiptDetail(String receiptNumber, HttpServletResponse response);
}
