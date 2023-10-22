package com.wansensoft.service.product.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.product.ProductImage;
import com.wansensoft.mappers.product.ProductImageMapper;
import com.wansensoft.service.product.ProductImageService;
import org.springframework.stereotype.Service;

@Service
public class ProductImageServiceImpl extends ServiceImpl<ProductImageMapper, ProductImage> implements ProductImageService {
}
