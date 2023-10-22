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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansensoft.dto.product.*;
import com.wansensoft.entities.product.*;
import com.wansensoft.mappers.product.ProductImageMapper;
import com.wansensoft.mappers.product.ProductMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.service.BaseService;
import com.wansensoft.service.product.*;
import com.wansensoft.utils.SnowflakeIdUtil;
import com.wansensoft.utils.constants.CommonConstants;
import com.wansensoft.utils.enums.BaseCodeEnum;
import com.wansensoft.utils.enums.ProdcutCodeEnum;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.product.ProductDetailVO;
import com.wansensoft.vo.product.ProductImageVO;
import com.wansensoft.vo.product.ProductPriceVO;
import com.wansensoft.vo.product.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;

    private final ProductExtendPriceService productExtendPriceService;

    private final ProductStockService productStockService;

    private final ProductExtendPropertyService productExtendPropertyService;

    private final ProductCategoryService productCategoryService;

    private final ProductImageService productImageService;

    private final BaseService baseService;

    public ProductServiceImpl(ProductMapper productMapper, ProductExtendPriceService productExtendPriceService, ProductStockService productStockService, ProductExtendPropertyService productExtendPropertyService, ProductCategoryService productCategoryService, ProductImageService productImageService, BaseService baseService) {
        this.productMapper = productMapper;
        this.productExtendPriceService = productExtendPriceService;
        this.productStockService = productStockService;
        this.productExtendPropertyService = productExtendPropertyService;
        this.productCategoryService = productCategoryService;
        this.productImageService = productImageService;
        this.baseService = baseService;
    }

    public String getStringValue(String str) {
        return Optional.ofNullable(str)
                .filter(s -> !s.trim().isEmpty())
                .orElse(null);
    }

    public <T extends Number> T getNumberValue(T number) {
        return Optional.ofNullable(number)
                .filter(n -> !n.equals(0))
                .orElse(null);
    }

    public BigDecimal getBigDecimalValue(Double value) {
        return Optional.ofNullable(value)
                .map(BigDecimal::valueOf)
                .map(bd -> bd.setScale(3, RoundingMode.HALF_UP))
                .orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<String> addProduct(AddProductDTO productDTO) {
        if(productDTO.getPriceList().isEmpty() && !StringUtils.hasLength(productDTO.getProductName()) &&
           !StringUtils.hasLength(productDTO.getProductUnit())) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var barCodeList = productDTO.getPriceList().stream()
                .map(ProductPriceDTO::getBarCode)
                .toList();

        var barCodeSet = new HashSet<>(barCodeList);
        var existBarCode = productExtendPriceService.lambdaQuery()
                    .in(ProductExtendPrice::getProductBarCode, barCodeList)
                    .exists();

        if(barCodeList.size() != barCodeSet.size() || existBarCode) {
            return Response.responseMsg(ProdcutCodeEnum.PRODUCT_BAR_CODE_EXIST);
        }

        var userId = baseService.getCurrentUserId();
        var productNumber = productDTO.getPriceList();
        var productExtendPrices = new ArrayList<ProductExtendPrice>(productNumber.size());

        var productId = SnowflakeIdUtil.nextId();
        Product product = Product.builder()
                .id(productId)
                .productCategoryId(getNumberValue(productDTO.getProductCategoryId()))
                .productName(getStringValue(productDTO.getProductName()))
                .productModel(getStringValue(productDTO.getProductModel()))
                .productStandard(getStringValue(productDTO.getProductStandard()))
                .productColor(getStringValue(productDTO.getProductColor()))
                // 只有选择多单位的时候会才会设置unitId的字段
                .productUnitId(getNumberValue(productDTO.getProductUnitId()))
                .productUnit(getStringValue(productDTO.getProductUnit()))
                .productExpiryNum(getNumberValue(productDTO.getProductExpiryNum()))
                .productWeight(getBigDecimalValue(productDTO.getProductWeight()))
                .warehouseShelves(getStringValue(productDTO.getWarehouseShelves()))
                .enableBatchNumber(getNumberValue(productDTO.getEnableBatchNumber()))
                .enableSerialNumber(getNumberValue(productDTO.getEnableSerialNumber()))
                .productManufacturer(getStringValue(productDTO.getProductManufacturer()))
                .otherFieldOne(getStringValue(productDTO.getOtherFieldOne()))
                .otherFieldTwo(getStringValue(productDTO.getOtherFieldTwo()))
                .otherFieldThree(getStringValue(productDTO.getOtherFieldThree()))
                .remark(getStringValue(productDTO.getRemark()))
                .status(CommonConstants.STATUS_NORMAL)
                .deleteFlag(CommonConstants.NOT_DELETED)
                .createTime(LocalDateTime.now())
                .createBy(userId)
                .build();
        boolean addResult = save(product);

        for (ProductPriceDTO priceDTO : productNumber) {
            ProductExtendPrice price = ProductExtendPrice.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .productId(productId)
                    .productBarCode(getNumberValue(priceDTO.getBarCode()))
                    .productUnit(getStringValue(priceDTO.getProductUnit()))
                    .multiAttribute(getStringValue(priceDTO.getMultiAttribute()))
                    .purchasePrice(getBigDecimalValue(priceDTO.getPurchasePrice()))
                    .retailPrice(getBigDecimalValue(priceDTO.getRetailPrice()))
                    .salePrice(getBigDecimalValue(priceDTO.getSalesPrice()))
                    .lowPrice(getBigDecimalValue(priceDTO.getLowSalesPrice()))
                    .deleteFlag(CommonConstants.NOT_DELETED)
                    .createTime(LocalDateTime.now())
                    .createBy(userId)
                    .build();
            productExtendPrices.add(price);
        }
        boolean addPriceResult = productExtendPriceService.saveBatch(productExtendPrices);

        var productStocks = new ArrayList<ProductStock>(productDTO.getStockList().size() + 2);
        if(!productDTO.getStockList().isEmpty()) {
            for (ProductStockDTO productStockDTO : productDTO.getStockList()) {
                ProductStock productStock = ProductStock.builder()
                        .id(SnowflakeIdUtil.nextId())
                        .productId(productId)
                        .warehouseId(getNumberValue(productStockDTO.getWarehouseId()))
                        .initStockQuantity(getBigDecimalValue(productStockDTO.getInitStockQuantity()))
                        // 把当前库存数量设置成初始库存数量
                        .currentStockQuantity(getBigDecimalValue(productStockDTO.getInitStockQuantity()))
                        .lowStockQuantity(getBigDecimalValue(productStockDTO.getLowStockQuantity()))
                        .highStockQuantity(getBigDecimalValue(productStockDTO.getHighStockQuantity()))
                        .createBy(userId)
                        .createTime(LocalDateTime.now())
                        .build();

                productStocks.add(productStock);
            }
        }
        boolean addStockResult = productStockService.saveBatch(productStocks);

        if(!productDTO.getImageList().isEmpty()) {
            var imageList = new ArrayList<ProductImage>(productDTO.getImageList().size() + 1);
            for (ProductImageDTO image : productDTO.getImageList()) {
                ProductImage productImage = new ProductImage();
                BeanUtils.copyProperties(image, productImage);

                productImage.setId(SnowflakeIdUtil.nextId());
                productImage.setProductId(productId);
                productImage.setCreateBy(userId);
                productImage.setCreateTime(LocalDateTime.now());
                imageList.add(productImage);
            }
            productImageService.saveBatch(imageList);
        }

        if(addResult && addPriceResult && addStockResult) {
            return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_SUCCESS);
        }

        return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_ERROR);
    }

    @Override
    public Response<Page<ProductVO>> getProductInfo(QueryProductDTO queryProductDTO) {
        var result = new Page<ProductVO>();
        var productVos = new ArrayList<ProductVO>();

        Page<Product> productPage = new Page<>(queryProductDTO.getPage(), queryProductDTO.getPageSize());
        // 关键字keywords 这里暂时不做处理
        var wrapper = new LambdaQueryWrapper<Product>()
                .like(StringUtils.hasLength(queryProductDTO.getProductColor()), Product::getProductColor, queryProductDTO.getProductColor())
                .like(StringUtils.hasLength(queryProductDTO.getExtendInfo()), Product::getOtherFieldOne, queryProductDTO.getExtendInfo())
                .like(StringUtils.hasLength(queryProductDTO.getRemark()), Product::getRemark, queryProductDTO.getRemark())
                .like(StringUtils.hasLength(queryProductDTO.getWarehouseShelves()), Product::getWarehouseShelves, queryProductDTO.getWarehouseShelves())
                .eq(getNumberValue(queryProductDTO.getStatus()) != null, Product::getStatus, queryProductDTO.getStatus())
                .eq(getNumberValue(queryProductDTO.getEnableSerialNumber()) != null, Product::getEnableSerialNumber, queryProductDTO.getEnableSerialNumber())
                .eq(getNumberValue(queryProductDTO.getEnableBatchNumber()) != null, Product::getEnableBatchNumber, queryProductDTO.getEnableBatchNumber())
                .eq(Product::getDeleteFlag, CommonConstants.NOT_DELETED);

        productMapper.selectPage(productPage, wrapper);
        productPage.getRecords().forEach(item -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(item, productVO);

            var productCategoryName = productCategoryService.lambdaQuery()
                    .eq(ProductCategory::getId, item.getProductCategoryId())
                    .eq(ProductCategory::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .oneOpt()
                    .map(ProductCategory::getCategoryName)
                    .orElse(null);
            productVO.setProductCategoryName(productCategoryName);

            // 查询价格 如果是多个结果获取第一个
            var price = productExtendPriceService.lambdaQuery()
                    .eq(ProductExtendPrice::getProductId, item.getId())
                    .eq(ProductExtendPrice::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if(price != null) {
                productVO.setProductBarcode(price.getProductBarCode());
                productVO.setPurchasePrice(price.getPurchasePrice());
                productVO.setRetailPrice(price.getRetailPrice());
                productVO.setSalePrice(price.getSalePrice());
                productVO.setLowPrice(price.getLowPrice());
            }

            // 查询库存如果不为空计算所有当前库存数量
            var stock = productStockService.lambdaQuery()
                    .eq(ProductStock::getProductId, item.getId())
                    .eq(ProductStock::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            if(!stock.isEmpty()) {
                var currentStockQuantity = stock.stream()
                        .map(ProductStock::getCurrentStockQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                productVO.setProductStock(currentStockQuantity);
            }
            productVos.add(productVO);
        });
        result.setRecords(productVos);
        result.setTotal(productPage.getTotal());
        result.setSize(productPage.getSize());
        result.setPages(productPage.getPages());

        return Response.responseData(result);
    }

    @Override
    public Response<ProductDetailVO> getProductInfoDetail(Long productId) {
        if(productId == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var product = getById(productId);
        if(product == null) {
            return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
        }

        ProductDetailVO productDetailVO = new ProductDetailVO();
        BeanUtils.copyProperties(product, productDetailVO);
        productDetailVO.setProductId(product.getId());

        var prices = productExtendPriceService.lambdaQuery()
                .eq(ProductExtendPrice::getProductId, productId)
                .eq(ProductExtendPrice::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        if(prices != null && !prices.isEmpty()) {
            var productPrices = new ArrayList<ProductPriceVO>();
            for (ProductExtendPrice price : prices) {
                ProductPriceVO productPriceVO = ProductPriceVO.builder()
                        .barCode(price.getProductBarCode())
                        .multiAttribute(price.getMultiAttribute())
                        .productUnit(price.getProductUnit())
                        .purchasePrice(price.getPurchasePrice())
                        .salesPrice(price.getSalePrice())
                        .retailPrice(price.getRetailPrice())
                        .lowSalesPrice(price.getLowPrice())
                        .build();
                productPrices.add(productPriceVO);
            }
            productDetailVO.setPriceList(productPrices);
        }

        var productStocks = productStockService.getProductStockList(productId);
        if (productStocks != null && !productStocks.isEmpty()) {
            productDetailVO.setStockList(productStocks);
        }

        var productImages = productImageService.lambdaQuery()
                .eq(ProductImage::getProductId, productId)
                .eq(ProductImage::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();
        if(productImages != null && !productImages.isEmpty()) {
            var imagesVo = new ArrayList<ProductImageVO>();
            for (ProductImage image : productImages) {
                ProductImageVO productImageVO = new ProductImageVO();
                BeanUtils.copyProperties(image, productImageVO);
                imagesVo.add(productImageVO);
            }
            productDetailVO.setImageList(imagesVo);
        }

        return Response.responseData(productDetailVO);
    }
}
