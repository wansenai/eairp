package com.wansenai.service.product.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.service.product.ProductImageService;
import com.wansenai.entities.product.ProductImage;
import com.wansenai.mappers.product.ProductImageMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductImageServiceImpl extends ServiceImpl<ProductImageMapper, ProductImage> implements ProductImageService {
}
