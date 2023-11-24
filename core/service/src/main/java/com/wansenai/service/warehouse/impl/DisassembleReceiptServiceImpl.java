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
package com.wansenai.service.warehouse.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.dto.warehouse.DisassembleReceiptDTO;
import com.wansenai.dto.warehouse.QueryDisassembleReceiptDTO;
import com.wansenai.entities.warehouse.WarehouseReceiptMain;
import com.wansenai.mappers.warehouse.WarehouseReceiptMainMapper;
import com.wansenai.service.warehouse.DisassembleReceiptService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.warehouse.DisassembleReceiptDetailVO;
import com.wansenai.vo.warehouse.DisassembleReceiptVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisassembleReceiptServiceImpl extends ServiceImpl<WarehouseReceiptMainMapper, WarehouseReceiptMain> implements DisassembleReceiptService {

    @Override
    public Response<Page<DisassembleReceiptVO>> getDisassembleReceiptPageList(QueryDisassembleReceiptDTO queryDisassembleReceiptDTO) {
        return null;
    }

    @Override
    public Response<DisassembleReceiptDetailVO> getDisassembleReceiptDetail(Long id) {
        return null;
    }

    @Override
    public Response<String> addOrUpdateDisassembleReceipt(DisassembleReceiptDTO disassembleReceiptDTO) {
        return null;
    }

    @Override
    public Response<String> deleteBatchDisassembleReceipt(List<Long> ids) {
        return null;
    }

    @Override
    public Response<String> updateDisassembleReceiptStatus(List<Long> ids, Integer status) {
        return null;
    }
}
