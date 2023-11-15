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
package com.wansenai.service.product.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.product.QueryProductStockKeepUnitDTO;
import com.wansenai.service.product.ProductStockKeepUnitService;
import com.wansenai.utils.response.Response;
import com.wansenai.entities.product.ProductStockKeepUnit;
import com.wansenai.mappers.product.ProductStockKeepUnitMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.vo.product.ProductStockKeepUnitVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 产品价格扩展 服务实现类
 * </p>
 */
@Service
public class ProductStockKeepUnitServiceImpl extends ServiceImpl<ProductStockKeepUnitMapper, ProductStockKeepUnit> implements ProductStockKeepUnitService {

    private final ProductStockKeepUnitMapper productSkuMapper;

    private static int currentBarcode = 1000;

    public ProductStockKeepUnitServiceImpl(ProductStockKeepUnitMapper productSkuMapper) {
        this.productSkuMapper = productSkuMapper;
    }

    public static int generateBarcode() {
        if (currentBarcode > 9999) {
            throw new IllegalStateException("No more barcodes available.");
        }

        int barcode = currentBarcode;
        currentBarcode++;
        return barcode;
    }

    @Override
    public Response<Long> getProductCode() {
        var data = lambdaQuery()
                .orderByDesc(ProductStockKeepUnit::getProductBarCode)
                .last("LIMIT 1").one();
        if(data == null){
            return Response.responseData(1000L);
        }
        return Response.responseData(data.getProductBarCode() + 1);
    }

    @Override
    public Boolean checkProductCode(List<String> barCodes) {
        return lambdaQuery().in(ProductStockKeepUnit::getProductBarCode, barCodes).exists();
    }
}
