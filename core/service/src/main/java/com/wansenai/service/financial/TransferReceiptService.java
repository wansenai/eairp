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
import com.wansenai.dto.financial.AddOrUpdateTransferDTO;
import com.wansenai.dto.financial.QueryTransferDTO;
import com.wansenai.entities.financial.FinancialMain;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.financial.TransferDetailVO;
import com.wansenai.vo.financial.TransferVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface TransferReceiptService extends IService<FinancialMain> {

    Response<Page<TransferVO>> getTransferReceiptPageList(QueryTransferDTO queryTransferDTO);

    Response<TransferDetailVO> getTransferReceiptDetail(Long id);

    Response<String> addOrUpdateTransferReceipt(AddOrUpdateTransferDTO addOrUpdateTransferDTO);

    Response<String> deleteBatchTransferReceipt(List<Long> ids);

    Response<String> updateTransferReceiptStatus(List<Long> ids, Integer status);

    void exportTransferReceipt(QueryTransferDTO queryTransferDTO, HttpServletResponse response);

    void exportTransferReceiptDetail(String receiptNumber, HttpServletResponse response);
}
