package com.wansensoft.service.product.impl;

import com.wansensoft.service.product.IProductAttributeService;
import com.wansensoft.entities.ProductAttribute;
import com.wansensoft.mappers.product.ProductAttributeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品属性表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class ProductAttributeServiceImpl extends ServiceImpl<ProductAttributeMapper, ProductAttribute> implements IProductAttributeService {

}
