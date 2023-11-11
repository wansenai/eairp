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
package com.wansenai.api.receipt;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.receipt.QueryReceiptDTO;
import com.wansenai.service.receipt.ReceiptRetailService;
import com.wansenai.service.receipt.ReceiptService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.ReceiptDetailVO;
import com.wansenai.vo.receipt.ReceiptRetailDetailVO;
import com.wansenai.vo.receipt.ReceiptVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private final ReceiptRetailService receiptRetailService;

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptRetailService receiptRetailService, ReceiptService receiptService) {
        this.receiptRetailService = receiptRetailService;
        this.receiptService = receiptService;
    }

    @GetMapping("otherReceipt")
    public Response<Page<ReceiptVO>> getOtherReceipt(@ModelAttribute QueryReceiptDTO queryReceiptDTO) {
        return receiptService.otherReceipt(queryReceiptDTO);
    }

    @GetMapping("otherReceiptDetail")
    public Response<Page<ReceiptDetailVO>> getOtherDetail(@ModelAttribute QueryReceiptDTO queryReceiptDTO) {
        return receiptService.getOtherDetail(queryReceiptDTO);
    }

    @GetMapping("/detail/{id}")
    @Deprecated(since = "2021-11-09", forRemoval = true)
    public Response<List<ReceiptRetailDetailVO>> getRetailDetail(@PathVariable("id") Long id) {
        return receiptRetailService.retailDetail(id);
    }
}
