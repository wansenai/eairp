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
package com.wansenai.api.report;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.report.QueryProductStock;
import com.wansenai.service.receipt.ReceiptRetailService;
import com.wansenai.service.receipt.ReceiptService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.retail.RetailStatisticalDataVO;
import com.wansenai.vo.report.ProductStockVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReceiptService receiptService;

    public ReportController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("homePage/statistics")
    public Response<RetailStatisticalDataVO> getStatisticalData() {
        return receiptService.getRetailStatistics();
    }

    @PostMapping("productStock")
    public Response<IPage<ProductStockVO>> getProductStock(@RequestBody QueryProductStock queryProductStock) {
        return receiptService.getProductStock(queryProductStock);
    }
}
