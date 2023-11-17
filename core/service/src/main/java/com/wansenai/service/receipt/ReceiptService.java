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
package com.wansenai.service.receipt;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.receipt.QueryReceiptDTO;
import com.wansenai.dto.report.QueryAccountStatisticsDTO;
import com.wansenai.dto.report.QueryProductStockDTO;
import com.wansenai.dto.report.QueryStockFlowDTO;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.ReceiptDetailVO;
import com.wansenai.vo.receipt.ReceiptVO;
import com.wansenai.vo.receipt.retail.RetailStatisticalDataVO;
import com.wansenai.vo.report.AccountStatisticsVO;
import com.wansenai.vo.report.ProductStockVO;
import com.wansenai.vo.report.StockFlowVO;

public interface ReceiptService {

    /**
     * Query the data summary of the six columns on the tenant's homepage.
     * <p>
     * 根据id集合和状态批量修改零售出库单状态
     *
     * @return Return to homepage data summary
     *         返回首页数据汇总
     */
    Response<RetailStatisticalDataVO> getRetailStatistics();

    Response<Page<ReceiptVO>> otherReceipt(QueryReceiptDTO receiptDTO);

    Response<Page<ReceiptDetailVO>> getOtherDetail(QueryReceiptDTO receiptDTO);

    Response<IPage<ProductStockVO>> getProductStock(QueryProductStockDTO queryProductStockDTO);

    Response<Page<StockFlowVO>> getStockFlow(QueryStockFlowDTO queryStockFlowDTO);

    Response<Page<AccountStatisticsVO>> getAccountStatistics(QueryAccountStatisticsDTO accountStatisticsDTO);
}
