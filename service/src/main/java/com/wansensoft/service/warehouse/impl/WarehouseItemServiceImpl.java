package com.wansensoft.service.warehouse.impl;

import com.wansensoft.service.warehouse.IWarehouseItemService;
import com.wansensoft.entities.warehouse.WarehouseItem;
import com.wansensoft.mappers.warehouse.WarehouseItemMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 单据子表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class WarehouseItemServiceImpl extends ServiceImpl<WarehouseItemMapper, WarehouseItem> implements IWarehouseItemService {

}
