package com.wansensoft.service.product.impl;

import com.wansensoft.service.product.IProductInventoryInitialService;
import com.wansensoft.entities.product.ProductInventoryInitial;
import com.wansensoft.mappers.product.ProductInventoryInitialMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品初始库存 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class ProductInventoryInitialServiceImpl extends ServiceImpl<ProductInventoryInitialMapper, ProductInventoryInitial> implements IProductInventoryInitialService {

}
