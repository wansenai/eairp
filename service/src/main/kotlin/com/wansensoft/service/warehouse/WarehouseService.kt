package com.wansensoft.service.warehouse

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.wansensoft.dto.basic.AddOrUpdateWarehouseDTO
import com.wansensoft.dto.basic.QueryWarehouseDTO
import com.wansensoft.entities.warehouse.Warehouse
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.warehouse.WarehouseVO

interface WarehouseService : IService<Warehouse> {

    fun getWarehouseList(warehouseDTO: QueryWarehouseDTO?): Response<Page<WarehouseVO>>

    fun addOrUpdateWarehouse(warehouseDTO: AddOrUpdateWarehouseDTO): Response<String>

    fun deleteBatch(ids: List<Long>?): Response<String>

    fun updateBatchStatus(ids: List<Long>?, status: Int?): Response<String>
}