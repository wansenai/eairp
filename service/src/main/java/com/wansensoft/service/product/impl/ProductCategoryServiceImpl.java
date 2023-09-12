package com.wansensoft.service.product.impl;

import com.wansensoft.service.product.IProductCategoryService;
import com.wansensoft.entities.product.ProductCategory;
import com.wansensoft.mappers.product.ProductCategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品类型表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements IProductCategoryService {

}
