package com.wansensoft.service.product.impl;

import com.wansensoft.service.product.IProductService;
import com.wansensoft.entities.product.Product;
import com.wansensoft.mappers.product.ProductMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

}
