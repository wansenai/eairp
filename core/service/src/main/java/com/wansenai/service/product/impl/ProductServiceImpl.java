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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.service.BaseService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.dto.product.*;
import com.wansenai.entities.product.*;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.ProdcutCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.mappers.product.ProductMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.service.product.*;
import com.wansenai.vo.product.ProductDetailVO;
import com.wansenai.vo.product.ProductImageVO;
import com.wansenai.vo.product.ProductPriceVO;
import com.wansenai.vo.product.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;

    private final ProductStockKeepUnitService productStockKeepUnitService;

    private final ProductStockService productStockService;

    private final ProductCategoryService productCategoryService;

    private final ProductImageService productImageService;

    private final ProductUnitService productUnitService;

    private final BaseService baseService;

    public ProductServiceImpl(ProductMapper productMapper, ProductStockKeepUnitService productStockKeepUnitService, ProductStockService productStockService, ProductCategoryService productCategoryService, ProductImageService productImageService, ProductUnitService productUnitService, BaseService baseService) {
        this.productMapper = productMapper;
        this.productStockKeepUnitService = productStockKeepUnitService;
        this.productStockService = productStockService;
        this.productCategoryService = productCategoryService;
        this.productImageService = productImageService;
        this.productUnitService = productUnitService;
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
    public Response<String> addOrUpdateProduct(AddOrUpdateProductDTO productDTO) {
        if (productDTO.getPriceList().isEmpty() && !StringUtils.hasLength(productDTO.getProductName()) &&
                !StringUtils.hasLength(productDTO.getProductUnit())) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var systemLanguage = baseService.getCurrentUserSystemLanguage();
        if (!productDTO.getPriceList().isEmpty()) {
            var barCodeList = productDTO.getPriceList().stream()
                    .map(ProductStockKeepUnitDTO::getBarCode)
                    .filter(Objects::nonNull) // 检查字符串是否为空或具有长度
                    .toList();
            if (!barCodeList.isEmpty()) {
                boolean existBarCode = productStockKeepUnitService.lambdaQuery()
                        .in(ProductStockKeepUnit::getProductBarCode, barCodeList)
                        .ne(productDTO.getProductId() != null, ProductStockKeepUnit::getProductId, productDTO.getProductId())
                        .exists();
                // 比较是否有相同的条码 如果有就返回错误信息 如果没有就继续执行
                boolean theSameBarCode = barCodeList.stream()
                        .distinct()
                        .count() != barCodeList.size();
                if ("zh_CN".equals(systemLanguage)) {
                    if (theSameBarCode) {
                        return Response.responseMsg(ProdcutCodeEnum.PRODUCT_BAR_CODE_NOT_DUPLICATED);
                    }

                    if (existBarCode) {
                        return Response.responseMsg(ProdcutCodeEnum.PRODUCT_BAR_CODE_EXIST);
                    }
                } else {
                    if (theSameBarCode) {
                        return Response.responseMsg(ProdcutCodeEnum.PRODUCT_BAR_CODE_NOT_DUPLICATED_EN);
                    }

                    if (existBarCode) {
                        return Response.responseMsg(ProdcutCodeEnum.PRODUCT_BAR_CODE_EXIST_EN);
                    }
                }
            }
        }

        var userId = baseService.getCurrentUserId();
        var productNumber = productDTO.getPriceList();
        var productExtendPrices = new ArrayList<ProductStockKeepUnit>(productNumber.size());
        var productStocks = new ArrayList<ProductStock>(productDTO.getStockList().size() + 2);

        // 如果productId就赋值给productId 否则生成一个id
        var productId = Optional.ofNullable(productDTO.getProductId())
                .orElse(SnowflakeIdUtil.nextId());
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

        // 2023-10-23 16:00 这里将来格式需要统一修复 这里getEnableBatchNumber() 和 getProductManufacturer如果是空的话就不进行转换 会报错
        if (StringUtils.hasLength(productDTO.getEnableBatchNumber())) {
            product.setEnableBatchNumber(Integer.valueOf(productDTO.getEnableBatchNumber()));
        }
        if (StringUtils.hasLength(productDTO.getEnableSerialNumber())) {
            product.setEnableSerialNumber(Integer.valueOf(productDTO.getEnableSerialNumber()));
        }
        boolean addOrUpdateResult = saveOrUpdate(product);

        // 删除原有的价格信息
        var priceIds = productStockKeepUnitService.lambdaQuery()
                .eq(ProductStockKeepUnit::getProductId, productId)
                .eq(ProductStockKeepUnit::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list()
                .stream()
                .map(ProductStockKeepUnit::getId)
                .toList();
        if (!priceIds.isEmpty()) {
            productStockKeepUnitService.removeByIds(priceIds);
        }

        for (ProductStockKeepUnitDTO priceDTO : productNumber) {
            var productSkuId = Optional.ofNullable(priceDTO.getProductPriceId())
                    .orElse(SnowflakeIdUtil.nextId());

            ProductStockKeepUnit price = ProductStockKeepUnit.builder()
                    .id(productSkuId)
                    .productId(productId)
                    .productBarCode(priceDTO.getBarCode())
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

            if (!productDTO.getStockList().isEmpty()) {
                for (ProductStockDTO productStockDTO : productDTO.getStockList()) {
                    var productStockId = Optional.ofNullable(productStockDTO.getProductStockId())
                            .orElse(SnowflakeIdUtil.nextId());

                    ProductStock productStock = ProductStock.builder()
                            .id(productStockId)
                            .warehouseId(getNumberValue(productStockDTO.getWarehouseId()))
                            .initStockQuantity(getBigDecimalValue(productStockDTO.getInitStockQuantity()))
                            // 把当前库存数量设置成初始库存数量
                            .currentStockQuantity(getBigDecimalValue(productStockDTO.getInitStockQuantity()))
                            .lowStockQuantity(getBigDecimalValue(productStockDTO.getLowStockQuantity()))
                            .highStockQuantity(getBigDecimalValue(productStockDTO.getHighStockQuantity()))
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build();
                    // 如果是新增就设置skuId 否则就不设置
                    if (productStockDTO.getProductStockId() == null) {
                        productStock.setProductSkuId(productSkuId);
                    }
                    productStocks.add(productStock);
                }
            }
        }
        boolean addPriceResult = productStockKeepUnitService.saveBatch(productExtendPrices);
        boolean addStockResult = productStockService.saveOrUpdateBatch(productStocks);

        // image
        if (!productDTO.getImageList().isEmpty()) {
            var imageIds = productImageService.lambdaQuery()
                    .eq(ProductImage::getProductId, productId)
                    .eq(ProductImage::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list()
                    .stream()
                    .map(ProductImage::getId)
                    .toList();
            if (!imageIds.isEmpty()) {
                productImageService.removeByIds(imageIds);
            }

            var imageList = new ArrayList<ProductImage>(productDTO.getImageList().size() + 1);
            for (ProductImageDTO image : productDTO.getImageList()) {
                var productImageId = Optional.ofNullable(image.getProductImageId())
                        .orElse(SnowflakeIdUtil.nextId());
                ProductImage productImage = new ProductImage();
                BeanUtils.copyProperties(image, productImage);

                productImage.setId(productImageId);
                productImage.setProductId(productId);
                productImage.setCreateBy(userId);
                productImage.setCreateTime(LocalDateTime.now());
                imageList.add(productImage);
            }
            productImageService.saveBatch(imageList);
        }

        if (productDTO.getProductId() == null) {
            if (addOrUpdateResult && addPriceResult && addStockResult) {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_SUCCESS);
                }
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_SUCCESS_EN);
            } else {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_ERROR);
                }
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_ERROR_EN);
            }
        }

        if (addOrUpdateResult && addPriceResult && addStockResult) {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_UPDATE_SUCCESS);
            }
            return Response.responseMsg(ProdcutCodeEnum.PRODUCT_UPDATE_SUCCESS_EN);
        } else {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_UPDATE_ERROR);
            }
            return Response.responseMsg(ProdcutCodeEnum.PRODUCT_UPDATE_ERROR_EN);
        }
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
                .like(StringUtils.hasLength(queryProductDTO.getKeywords()), Product::getProductName, queryProductDTO.getKeywords())
                .or()
                .like(StringUtils.hasLength(queryProductDTO.getKeywords()), Product::getProductStandard, queryProductDTO.getKeywords())
                .or()
                .like(StringUtils.hasLength(queryProductDTO.getKeywords()), Product::getProductModel, queryProductDTO.getKeywords())
                .or()
                .like(StringUtils.hasLength(queryProductDTO.getKeywords()), Product::getProductColor, queryProductDTO.getKeywords())
                .eq(queryProductDTO.getProductCategoryId() != null, Product::getProductCategoryId, queryProductDTO.getProductCategoryId())
                .eq(queryProductDTO.getStatus() != null, Product::getStatus, queryProductDTO.getStatus())
                .eq(queryProductDTO.getEnableSerialNumber() != null, Product::getEnableSerialNumber, queryProductDTO.getEnableSerialNumber())
                .eq(queryProductDTO.getEnableBatchNumber() != null, Product::getEnableBatchNumber, queryProductDTO.getEnableBatchNumber())
                .eq(Product::getDeleteFlag, CommonConstants.NOT_DELETED)
                .orderByDesc(Product::getCreateTime);

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
            var price = productStockKeepUnitService.lambdaQuery()
                    .eq(ProductStockKeepUnit::getProductId, item.getId())
                    .eq(ProductStockKeepUnit::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (price != null) {
                productVO.setProductBarcode(price.getProductBarCode());
                productVO.setPurchasePrice(price.getPurchasePrice());
                productVO.setRetailPrice(price.getRetailPrice());
                productVO.setSalePrice(price.getSalePrice());
                productVO.setLowPrice(price.getLowPrice());

                // 查询库存如果不为空计算所有当前库存数量
                var stock = productStockService.lambdaQuery()
                        .eq(ProductStock::getProductSkuId, price.getId())
                        .eq(ProductStock::getDeleteFlag, CommonConstants.NOT_DELETED)
                        .list();
                if (!stock.isEmpty()) {
                    var currentStockQuantity = stock.stream()
                            .map(ProductStock::getCurrentStockQuantity)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    productVO.setProductStock(currentStockQuantity);
                }
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
        if (productId == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var product = getById(productId);
        if (product == null) {
            return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
        }

        ProductDetailVO productDetailVO = new ProductDetailVO();
        BeanUtils.copyProperties(product, productDetailVO);
        productDetailVO.setProductId(product.getId());

        // 判断单位是否是多单位 如果productUnitId不为空则是多单位就需要进行查询单位信息 否则productUnit不为空就是单位信息

        if (product.getProductUnitId() != null) {
            var productUnit = productUnitService.lambdaQuery()
                    .eq(ProductUnit::getId, product.getProductUnitId())
                    .eq(ProductUnit::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .oneOpt()
                    .map(ProductUnit::getComputeUnit)
                    .orElse(null);
            productDetailVO.setProductUnit(productUnit);
        } else if (StringUtils.hasLength(product.getProductUnit())) {
            productDetailVO.setProductUnit(product.getProductUnit());
        }

        var prices = productStockKeepUnitService.lambdaQuery()
                .eq(ProductStockKeepUnit::getProductId, productId)
                .eq(ProductStockKeepUnit::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        if (prices != null && !prices.isEmpty()) {
            var productPrices = new ArrayList<ProductPriceVO>();
            for (ProductStockKeepUnit price : prices) {
                ProductPriceVO productPriceVO = ProductPriceVO.builder()
                        .productPriceId(price.getId())
                        .barCode(price.getProductBarCode())
                        .multiAttribute(price.getMultiAttribute())
                        .productUnit(price.getProductUnit())
                        .purchasePrice(price.getPurchasePrice())
                        .salesPrice(price.getSalePrice())
                        .retailPrice(price.getRetailPrice())
                        .lowSalesPrice(price.getLowPrice())
                        .stockList(productStockService.getProductStockList(price.getId()))
                        .build();
                productPrices.add(productPriceVO);
            }
            productDetailVO.setPriceList(productPrices);
        }

        var productImages = productImageService.lambdaQuery()
                .eq(ProductImage::getProductId, productId)
                .eq(ProductImage::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();
        if (productImages != null && !productImages.isEmpty()) {
            var imagesVo = new ArrayList<ProductImageVO>();
            for (ProductImage image : productImages) {
                ProductImageVO productImageVO = new ProductImageVO();
                BeanUtils.copyProperties(image, productImageVO);
                productImageVO.setProductImageId(image.getId());
                imagesVo.add(productImageVO);
            }
            productDetailVO.setImageList(imagesVo);
        }

        return Response.responseData(productDetailVO);
    }

    @Override
    @Transactional
    public Response<String> deleteProduct(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var imageIds = productImageService.lambdaQuery()
                .in(ProductImage::getProductId, productIds)
                .eq(ProductImage::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list()
                .stream()
                .map(ProductImage::getId)
                .toList();
        if (!imageIds.isEmpty()) {
            productImageService.removeByIds(imageIds);
        }

        var skuIds = productStockKeepUnitService.lambdaQuery()
                .in(ProductStockKeepUnit::getProductId, productIds)
                .eq(ProductStockKeepUnit::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list()
                .stream()
                .map(ProductStockKeepUnit::getId)
                .toList();
        if (!skuIds.isEmpty()) {
            productStockKeepUnitService.removeByIds(skuIds);
        }

        var stockIds = productStockService.lambdaQuery()
                .in(ProductStock::getProductSkuId, skuIds)
                .eq(ProductStock::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list()
                .stream()
                .map(ProductStock::getId)
                .toList();
        if (!stockIds.isEmpty()) {
            productStockService.removeByIds(stockIds);
        }

        // 物理删除商品信息
        var deleteProductResult = removeByIds(productIds);
        var systemLanguage = baseService.getCurrentUserSystemLanguage();
        if (deleteProductResult) {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_DELETE_SUCCESS);
            }
            return Response.responseMsg(ProdcutCodeEnum.PRODUCT_DELETE_SUCCESS_EN);
        } else {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_DELETE_ERROR);
            }
            return Response.responseMsg(ProdcutCodeEnum.PRODUCT_DELETE_ERROR_EN);
        }
    }

    @Override
    public Response<String> updateProductStatus(List<Long> productIds, Integer status) {
        if (productIds == null || productIds.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        // 批量修改状态
        var updateResult = lambdaUpdate()
                .set(Product::getStatus, status)
                .in(Product::getId, productIds)
                .eq(Product::getDeleteFlag, CommonConstants.NOT_DELETED)
                .update();
        var systemLanguage = baseService.getCurrentUserSystemLanguage();
        if (updateResult) {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_STATUS_UPDATE_SUCCESS);
            }
            return Response.responseMsg(ProdcutCodeEnum.PRODUCT_STATUS_UPDATE_SUCCESS_EN);
        } else {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_STATUS_UPDATE_ERROR);
            }
            return Response.responseMsg(ProdcutCodeEnum.PRODUCT_STATUS_UPDATE_ERROR_EN);
        }
    }

    @Override
    public Response<String> updateBatchProductInfo(UpdateBatchProductDTO productDTO) {
        if (productDTO.getProductIds() == null || productDTO.getProductIds().isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        if (productDTO.getProductCategoryId() != null) {
            lambdaUpdate()
                    .set(Product::getProductCategoryId, productDTO.getProductCategoryId())
                    .in(Product::getId, productDTO.getProductIds())
                    .eq(Product::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .update();
        }

        var updateResult = lambdaUpdate()
                .set(StringUtils.hasLength(productDTO.getProductColor()), Product::getProductColor, productDTO.getProductColor())
                .set(productDTO.getProductWeight() != null, Product::getProductWeight, productDTO.getProductWeight())
                .set(productDTO.getProductExpiryNum() != null, Product::getProductExpiryNum, productDTO.getProductExpiryNum())
                .set(StringUtils.hasLength(productDTO.getEnableSerialNumber()), Product::getEnableSerialNumber, productDTO.getEnableSerialNumber())
                .set(StringUtils.hasLength(productDTO.getEnableBatchNumber()), Product::getEnableBatchNumber, productDTO.getEnableBatchNumber())
                .set(StringUtils.hasLength(productDTO.getRemark()), Product::getRemark, productDTO.getRemark())
                .in(Product::getId, productDTO.getProductIds())
                .eq(Product::getDeleteFlag, CommonConstants.NOT_DELETED)
                .update();
        var systemLanguage = baseService.getCurrentUserSystemLanguage();
        if (updateResult) {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_BATCH_UPDATE_SUCCESS);
            }
            return Response.responseMsg(ProdcutCodeEnum.PRODUCT_BATCH_UPDATE_SUCCESS_EN);
        } else {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_BATCH_UPDATE_ERROR);
            }
            return Response.responseMsg(ProdcutCodeEnum.PRODUCT_BATCH_UPDATE_ERROR_EN);
        }
    }

    @Override
    @Transactional
    public boolean batchAddProduct(List<Product> productVos) {
        return saveBatch(productVos);
    }
}
