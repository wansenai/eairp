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
package com.wansenai.utils.constants;

/**
 * receipt constant interface
 * 单据常量接口
 */
public interface ReceiptConstants {

    String RECEIPT_TYPE_SHIPMENT = "出库";

    String RECEIPT_TYPE_STORAGE = "入库";

    String RECEIPT_SUB_TYPE_RETAIL_SHIPMENTS = "零售出库";

    String RECEIPT_SUB_TYPE_RETAIL_REFUND = "零售退货";

    String RECEIPT_SUB_TYPE_SALES_ORDER = "销售订单";

    String RECEIPT_SUB_TYPE_SALES_SHIPMENTS = "销售出库";

    String RECEIPT_SUB_TYPE_SALES_REFUND = "销售退货";

    String RECEIPT_SUB_TYPE_PURCHASE_ORDER  = "采购订单";

    String RECEIPT_SUB_TYPE_PURCHASE_STORAGE = "采购入库";

    String RECEIPT_SUB_TYPE_PURCHASE_REFUND = "采购退货";
}
