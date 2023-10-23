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
package com.wansensoft.service.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansensoft.dto.product.AddOrUpdateProductDTO;
import com.wansensoft.dto.product.QueryProductDTO;
import com.wansensoft.dto.product.UpdateBatchProductDTO;
import com.wansensoft.entities.product.Product;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.product.ProductDetailVO;
import com.wansensoft.vo.product.ProductVO;

import java.util.List;

public interface ProductService {

    Response<String> addOrUpdateProduct(AddOrUpdateProductDTO addOrUpdateProductDTO);

    Response<Page<ProductVO>> getProductInfo(QueryProductDTO queryProductDTO);

    Response<ProductDetailVO> getProductInfoDetail(Long productId);

    Response<String> deleteProduct(List<Long> productIds);

    Response<String> updateProductStatus(List<Long> productIds, Integer status);

    Response<String> updateBatchProductInfo(UpdateBatchProductDTO updateBatchProductDTO);

    boolean batchAddProduct(List<Product> products);
}
