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
import com.wansenai.dto.warehouse.AllotReceiptDTO;
import com.wansenai.dto.warehouse.QueryAllotReceiptDTO;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.warehouse.AllotReceiptDetailVO;
import com.wansenai.vo.warehouse.AllotReceiptVO;

import java.util.List;

public interface AllotShipmentsService {

    Response<Page<AllotReceiptVO>> getAllotReceiptPageList(QueryAllotReceiptDTO queryAllotReceiptDTO);

    Response<AllotReceiptDetailVO> getAllotReceiptDetail(Long id);

    Response<String> addOrUpdateAllotReceipt(AllotReceiptDTO allotReceiptDTO);

    Response<String> deleteBatchAllotReceipt(List<Long> ids);

    Response<String> updateAllotReceiptStatus(List<Long> ids, Integer status);
}
