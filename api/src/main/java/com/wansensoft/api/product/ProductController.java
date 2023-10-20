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
package com.wansensoft.api.product;

import com.wansensoft.service.product.ProductExtendPriceService;
import com.wansensoft.service.product.ProductService;
import com.wansensoft.service.product.ProductStockService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.product.ProductStockVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author James Zow
 * @since 2023-09-05
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    private final ProductExtendPriceService extendPriceService;

    private final ProductStockService productStockService;

    public ProductController(ProductService productService, ProductExtendPriceService extendPriceService, ProductStockService productStockService) {
        this.productService = productService;
        this.extendPriceService = extendPriceService;
        this.productStockService = productStockService;
    }

    @GetMapping("getBarCode")
    public Response<Integer> getBarCode() {
        return extendPriceService.getBarCode();
    }

    @GetMapping("getStock")
    public Response<List<ProductStockVO>> getStock() {
        return productStockService.getProductStockList();
    }
}
