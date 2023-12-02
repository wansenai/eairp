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
package com.wansenai.service.warehouse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansenai.dto.warehouse.DisassembleReceiptDTO;
import com.wansenai.dto.warehouse.QueryDisassembleReceiptDTO;
import com.wansenai.entities.warehouse.WarehouseReceiptMain;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.warehouse.DisassembleReceiptDetailVO;
import com.wansenai.vo.warehouse.DisassembleReceiptVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface DisassembleReceiptService extends IService<WarehouseReceiptMain> {

    Response<Page<DisassembleReceiptVO>> getDisassembleReceiptPageList(QueryDisassembleReceiptDTO queryDisassembleReceiptDTO);

    Response<DisassembleReceiptDetailVO> getDisassembleReceiptDetail(Long id);

    Response<String> addOrUpdateDisassembleReceipt(DisassembleReceiptDTO disassembleReceiptDTO);

    Response<String> deleteBatchDisassembleReceipt(List<Long> ids);

    Response<String> updateDisassembleReceiptStatus(List<Long> ids, Integer status);

    void exportDisAssembleReceipt(QueryDisassembleReceiptDTO queryDisassembleReceiptDTO, HttpServletResponse response) throws Exception;
}
