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
import com.wansenai.dto.warehouse.AllotReceiptDTO;
import com.wansenai.dto.warehouse.QueryAllotReceiptDTO;
import com.wansenai.entities.warehouse.WarehouseReceiptMain;
import com.wansenai.mappers.warehouse.WarehouseReceiptMainMapper;
import com.wansenai.service.warehouse.AllotShipmentsService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.warehouse.AllotReceiptDetailVO;
import com.wansenai.vo.warehouse.AllotReceiptVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllotShipmentsServiceImpl extends ServiceImpl<WarehouseReceiptMainMapper, WarehouseReceiptMain> implements AllotShipmentsService {

    @Override
    public Response<Page<AllotReceiptVO>> getAllotReceiptPageList(QueryAllotReceiptDTO queryAllotReceiptDTO) {
        return null;
    }

    @Override
    public Response<AllotReceiptDetailVO> getAllotReceiptDetail(Long id) {
        return null;
    }

    @Override
    public Response<String> addOrUpdateAllotReceipt(AllotReceiptDTO allotReceiptDTO) {
        return null;
    }

    @Override
    public Response<String> deleteBatchAllotReceipt(List<Long> ids) {
        return null;
    }

    @Override
    public Response<String> updateAllotReceiptStatus(List<Long> ids, Integer status) {
        return null;
    }
}
