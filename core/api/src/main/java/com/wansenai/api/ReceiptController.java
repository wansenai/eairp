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
package com.wansenai.api;

import com.wansenai.service.receipt.ReceiptRetailService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.retail.ReceiptRetailDetailVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private final ReceiptRetailService receiptRetailService;

    public ReceiptController(ReceiptRetailService receiptRetailService) {
        this.receiptRetailService = receiptRetailService;
    }

    @GetMapping("/detail/{id}")
    public Response<List<ReceiptRetailDetailVO>> getRetailDetail(@PathVariable("id") Long id) {
        return receiptRetailService.retailDetail(id);
    }
}
