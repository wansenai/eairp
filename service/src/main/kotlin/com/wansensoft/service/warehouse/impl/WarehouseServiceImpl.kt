package com.wansensoft.service.warehouse.impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansensoft.dto.basic.AddOrUpdateWarehouseDTO
import com.wansensoft.dto.basic.QueryWarehouseDTO
import com.wansensoft.entities.warehouse.Warehouse
import com.wansensoft.mappers.warehouse.WarehouseMapper
import com.wansensoft.service.BaseService
import com.wansensoft.service.user.ISysUserService
import com.wansensoft.service.warehouse.WarehouseService
import com.wansensoft.utils.SnowflakeIdUtil
import com.wansensoft.utils.constants.CommonConstants
import com.wansensoft.utils.enums.BaseCodeEnum
import com.wansensoft.utils.enums.WarehouseCodeEnum
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.warehouse.WarehouseVO
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
@Slf4j
open class WarehouseServiceImpl (
    private val warehouseMapper: WarehouseMapper,
    private val baseService: BaseService,
    private val userService: ISysUserService,
) : ServiceImpl<WarehouseMapper, Warehouse>(), WarehouseService {

    override fun getWarehouseList(warehouseDTO: QueryWarehouseDTO?): Response<Page<WarehouseVO>> {
        val page = warehouseDTO?.run { Page<Warehouse>(page ?: 1, pageSize ?: 10) }
        val wrapper = LambdaQueryWrapper<Warehouse>().apply {
                warehouseDTO?.warehouseName?.let { like(Warehouse::getWarehouseName, it) }
                warehouseDTO?.remark?.let { like(Warehouse::getRemark, it) }
                eq(Warehouse::getDeleteFlag, CommonConstants.NOT_DELETED)
        }


        val result = page?.run {
            warehouseMapper.selectPage(this, wrapper)
            val listVo = records.map { warehouse ->
                val name = userService.getById(warehouse.warehouseManager)?.name
                WarehouseVO(
                    id = warehouse.id,
                    warehouseName = warehouse.warehouseName,
                    warehouseManager = name,
                    address = warehouse.address,
                    price = warehouse.price,
                    truckage = warehouse.truckage,
                    type = warehouse.type,
                    status = warehouse.status,
                    remark = warehouse.remark,
                    sort = warehouse.sort,
                    createTime = warehouse.createTime
                )
            }

            Page<WarehouseVO>().apply {
                records = listVo
                total = this@run.total
                pages = this@run.pages
                size = this@run.size
            }
        }
        return result?.let { Response.responseData(it) } ?: Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY)
    }

    @Transactional
    override fun addOrUpdateWarehouse(warehouseDTO: AddOrUpdateWarehouseDTO): Response<String> {
        val userId = baseService.getCurrentUserId()
        val isAdd = warehouseDTO.id == null

        val warehouse = Warehouse().apply {
            id = warehouseDTO.id ?: SnowflakeIdUtil.nextId()
            warehouseName = warehouseDTO.warehouseName
            warehouseManager = warehouseDTO.warehouseManager
            address = warehouseDTO.address
            price = warehouseDTO.price ?: BigDecimal.ZERO
            truckage = warehouseDTO.truckage ?: BigDecimal.ZERO
            type = warehouseDTO.type
            status = warehouseDTO.status
            remark = warehouseDTO.remark
            sort = warehouseDTO.sort
            isDefault = warehouseDTO.isDefault
            if (isAdd) {
                createTime = LocalDateTime.now()
                createBy = userId
            } else {
                updateTime = LocalDateTime.now()
                updateBy = userId
            }
        }
        val saveResult = saveOrUpdate(warehouse)
        return when {
            saveResult && isAdd -> Response.responseMsg(WarehouseCodeEnum.ADD_WAREHOUSE_SUCCESS)
            saveResult && !isAdd -> Response.responseMsg(WarehouseCodeEnum.UPDATE_WAREHOUSE_INFO_SUCCESS)
            else -> Response.fail()
        }
    }

    override fun deleteBatch(ids: List<Long>?): Response<String> {
        return ids.takeIf { it?.isNotEmpty() ?: false }
            ?.let {
                val updateResult = warehouseMapper.deleteBatchIds(it)
                if (updateResult > 0) {
                    Response.responseMsg(WarehouseCodeEnum.DELETE_WAREHOUSE_SUCCESS)
                } else {
                    Response.responseMsg(WarehouseCodeEnum.DELETE_WAREHOUSE_ERROR)
                }
            }
            ?: Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

    override fun updateBatchStatus(ids: List<Long>?, status: Int?): Response<String> {
        if (ids?.isEmpty() == true) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
        }

        val updateResult = lambdaUpdate()
            .`in`(Warehouse::getId, ids)
            .set(Warehouse::getStatus, status)
            .update()

        return if (!updateResult) {
            Response.responseMsg(WarehouseCodeEnum.UPDATE_WAREHOUSE_STATUS_ERROR)
        } else {
            Response.responseMsg(WarehouseCodeEnum.UPDATE_WAREHOUSE_STATUS_SUCCESS)
        }
    }
}