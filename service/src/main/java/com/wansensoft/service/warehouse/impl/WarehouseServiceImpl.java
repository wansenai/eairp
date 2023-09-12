package com.wansensoft.service.warehouse.impl;

import com.wansensoft.service.warehouse.IWarehouseService;
import com.wansensoft.entities.warehouse.Warehouse;
import com.wansensoft.mappers.warehouse.WarehouseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 仓库表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements IWarehouseService {

}
