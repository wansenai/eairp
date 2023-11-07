package com.wansenai.service.warehouse

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.wansenai.dto.basic.AddOrUpdateWarehouseDTO
import com.wansenai.dto.basic.QueryWarehouseDTO
import com.wansenai.entities.warehouse.Warehouse
import com.wansenai.utils.response.Response
import com.wansenai.vo.warehouse.WarehouseVO

interface WarehouseService : IService<Warehouse> {

    fun getWarehousePageList(warehouseDTO: QueryWarehouseDTO?): Response<Page<WarehouseVO>>

    fun addOrUpdateWarehouse(warehouseDTO: AddOrUpdateWarehouseDTO): Response<String>

    fun deleteBatch(ids: List<Long>?): Response<String>

    fun getWarehouse(): Response<List<WarehouseVO>>

    fun updateBatchStatus(ids: List<Long>?, status: Int?): Response<String>

    fun getWarehouseByName(name: String?): Warehouse

    fun getWarehouseList(): Response<List<WarehouseVO>>

    fun getDefaultWarehouse(): Response<WarehouseVO>
}