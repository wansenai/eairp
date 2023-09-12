package com.wansensoft.service.product.impl;

import com.wansensoft.service.product.IProductUnitService;
import com.wansensoft.entities.product.ProductUnit;
import com.wansensoft.mappers.product.ProductUnitMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 多单位表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class ProductUnitServiceImpl extends ServiceImpl<ProductUnitMapper, ProductUnit> implements IProductUnitService {

}
