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
package com.wansenai.mappers.product;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.product.QueryProductStockKeepUnitDTO;
import com.wansenai.dto.report.QueryProductStockDTO;
import com.wansenai.entities.product.ProductStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansenai.vo.product.ProductStockKeepUnitVO;
import com.wansenai.vo.report.ProductStockSkuVO;

import java.util.List;

/**
 * <p>
 * 产品初始库存 Mapper 接口
 * </p>
 */
public interface ProductStockMapper extends BaseMapper<ProductStock> {

    IPage<ProductStockKeepUnitVO> getProductSkuList(IPage<QueryProductStockKeepUnitDTO> pageObject, QueryProductStockKeepUnitDTO queryProductStockKeepUnitDTO);

    ProductStockKeepUnitVO getProductSkuByBarCode(String barCode, Long warehouseId);

    ProductStockKeepUnitVO getProductSkuDetail(Long productId, Long warehouseId, String barCode);

    Page<ProductStockSkuVO> getProductStock(IPage<QueryProductStockDTO> pageObject, QueryProductStockDTO queryProductStockDTO);

    List<ProductStockSkuVO> getProductStockList(QueryProductStockDTO queryProductStockDTO);

    // 检查商品是否存在
    Boolean productStockExist(Long productSkuId, Long warehouseId);

    Boolean saveBatch(List<ProductStock> productStockList);
}
