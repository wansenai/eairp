package com.wansensoft.service.product.impl;

import com.wansensoft.service.product.IProductInventoryCurrentService;
import com.wansensoft.entities.product.ProductInventoryCurrent;
import com.wansensoft.mappers.product.ProductInventoryCurrentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品当前库存 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class ProductInventoryCurrentServiceImpl extends ServiceImpl<ProductInventoryCurrentMapper, ProductInventoryCurrent> implements IProductInventoryCurrentService {

}
