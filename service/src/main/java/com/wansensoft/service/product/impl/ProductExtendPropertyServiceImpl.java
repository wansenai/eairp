package com.wansensoft.service.product.impl;

import com.wansensoft.service.product.IProductExtendPropertyService;
import com.wansensoft.entities.product.ProductExtendProperty;
import com.wansensoft.mappers.product.ProductExtendPropertyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品扩展字段表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class ProductExtendPropertyServiceImpl extends ServiceImpl<ProductExtendPropertyMapper, ProductExtendProperty> implements IProductExtendPropertyService {

}
