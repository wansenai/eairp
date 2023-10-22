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
package com.wansensoft.service.product.impl;

import com.wansensoft.service.product.ProductStockService;
import com.wansensoft.entities.product.ProductStock;
import com.wansensoft.mappers.product.ProductInventoryInitialMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.service.warehouse.WarehouseService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.product.ProductStockVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductStockServiceImpl extends ServiceImpl<ProductInventoryInitialMapper, ProductStock> implements ProductStockService {

    private final WarehouseService warehouseService;

    public ProductStockServiceImpl(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Override
    public List<ProductStockVO> getProductStockList(Long productId) {
        var productStockVos = new ArrayList<ProductStockVO>();

        var productStocks = lambdaQuery()
                .eq(ProductStock::getProductId, productId)
                .list();
        productStocks.forEach(productStock -> {
            ProductStockVO productStockVO = new ProductStockVO();
            BeanUtils.copyProperties(productStock, productStockVO);
            productStockVos.add(productStockVO);

            // set warehouse name
            var warehouse = warehouseService.getById(productStock.getWarehouseId());
            productStockVO.setWarehouseName(warehouse.getWarehouseName());
        });

        return productStockVos;
    }
}
