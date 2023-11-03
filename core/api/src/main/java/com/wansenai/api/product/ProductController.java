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
package com.wansenai.api.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.product.AddOrUpdateProductDTO;
import com.wansenai.dto.product.QueryProductDTO;
import com.wansenai.dto.product.UpdateBatchProductDTO;
import com.wansenai.service.product.ProductStockKeepUnitService;
import com.wansenai.service.product.ProductService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.product.ProductDetailVO;
import com.wansenai.vo.product.ProductVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author James Zow
 * @since 2023-09-05
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    private final ProductStockKeepUnitService extendPriceService;

    public ProductController(ProductService productService, ProductStockKeepUnitService extendPriceService) {
        this.productService = productService;
        this.extendPriceService = extendPriceService;
    }

    @GetMapping("getProductCode")
    public Response<Long> getProductCode() {
        return extendPriceService.getProductCode();
    }

    @PostMapping("addOrUpdateProduct")
    public Response<String> addOrUpdateProduct(@RequestBody AddOrUpdateProductDTO addOrUpdateProductDTO) {
        return productService.addOrUpdateProduct(addOrUpdateProductDTO);
    }

    @PostMapping("getProductInfo")
    public Response<Page<ProductVO>> getProductInfo(@RequestBody QueryProductDTO queryProductDTO) {
        return productService.getProductInfo(queryProductDTO);
    }

    @GetMapping("getProductInfoDetail/{productId}")
    public Response<ProductDetailVO> getProductInfoDetail(@PathVariable Long productId) {
        return productService.getProductInfoDetail(productId);
    }

    @DeleteMapping("deleteProduct/{productIds}")
    public Response<String> deleteProduct(@PathVariable List<Long> productIds) {
        return productService.deleteProduct(productIds);
    }

    @PutMapping("updateProductStatus/{productIds}/{status}")
    public Response<String> updateProductStatus(@PathVariable List<Long> productIds, @PathVariable Integer status) {
        return productService.updateProductStatus(productIds, status);
    }

    @PutMapping("updateBatchProductInfo")
    public Response<String> updateBatchProductInfo(@RequestBody UpdateBatchProductDTO updateBatchProductDTO) {
        return productService.updateBatchProductInfo(updateBatchProductDTO);
    }
}
