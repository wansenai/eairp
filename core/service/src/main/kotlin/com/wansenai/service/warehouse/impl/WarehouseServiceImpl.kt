package com.wansenai.service.warehouse.impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansenai.entities.warehouse.Warehouse
import com.wansenai.dto.basic.AddOrUpdateWarehouseDTO
import com.wansenai.dto.basic.QueryWarehouseDTO
import com.wansenai.mappers.warehouse.WarehouseMapper
import com.wansenai.service.BaseService
import com.wansenai.service.user.ISysUserService
import com.wansenai.utils.SnowflakeIdUtil
import com.wansenai.utils.constants.CommonConstants
import com.wansenai.utils.enums.BaseCodeEnum
import com.wansenai.service.warehouse.WarehouseService
import com.wansenai.utils.enums.WarehouseCodeEnum
import com.wansenai.utils.response.Response
import com.wansenai.vo.warehouse.WarehouseVO
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.BeanUtils
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

    override fun getWarehousePageList(warehouseDTO: QueryWarehouseDTO?): Response<Page<WarehouseVO>> {
        val page = warehouseDTO?.run { Page<Warehouse>(page ?: 1, pageSize ?: 10) }
        val wrapper = LambdaQueryWrapper<Warehouse>().apply {
                warehouseDTO?.warehouseName?.let { like(Warehouse::getWarehouseName, it) }
                warehouseDTO?.remark?.let { like(Warehouse::getRemark, it) }
                eq(Warehouse::getDeleteFlag, CommonConstants.NOT_DELETED)
                orderByDesc(Warehouse::getCreateTime)
        }

        val result = page?.run {
            warehouseMapper.selectPage(this, wrapper)
            val listVo = records.map { warehouse ->
                val name = userService.getById(warehouse.warehouseManager)?.name
                WarehouseVO(
                    id = warehouse.id,
                    warehouseName = warehouse.warehouseName,
                    warehouseManager = warehouse.warehouseManager,
                    warehouseManagerName = name,
                    address = warehouse.address,
                    price = warehouse.price,
                    truckage = warehouse.truckage,
                    type = warehouse.type,
                    status = warehouse.status,
                    remark = warehouse.remark,
                    sort = warehouse.sort,
                    isDefault = warehouse.isDefault,
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
        return result?.let { Response.responseData(it) } ?: Response.responseMsg(
            BaseCodeEnum.QUERY_DATA_EMPTY)
    }

    fun updateDefaultAccount(id: Long) {
        lambdaQuery()
            .eq(Warehouse::getIsDefault, CommonConstants.IS_DEFAULT)
            .eq(Warehouse::getDeleteFlag, CommonConstants.NOT_DELETED)
            .one()
            ?.apply {
                setIsDefault(CommonConstants.NOT_DEFAULT)
                updateById(this)
            }

        getById(id)?.apply {
            setIsDefault(CommonConstants.IS_DEFAULT)
            updateById(this)
        }
    }

    @Transactional
    override fun addOrUpdateWarehouse(warehouseDTO: AddOrUpdateWarehouseDTO): Response<String> {
        val userId = baseService.getCurrentUserId()
        val isAdd = warehouseDTO.id == null
        val systemLanguage = baseService.currentUserSystemLanguage
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
        if(warehouse.isDefault == CommonConstants.IS_DEFAULT) {
            updateDefaultAccount(warehouse.id)
        }
        val saveResult = saveOrUpdate(warehouse)
        return if (systemLanguage == "zh_CN") {
            when {
                saveResult && isAdd -> Response.responseMsg(WarehouseCodeEnum.ADD_WAREHOUSE_SUCCESS)
                saveResult && !isAdd -> Response.responseMsg(WarehouseCodeEnum.UPDATE_WAREHOUSE_INFO_SUCCESS)
                else -> Response.fail()
            }
        } else {
            when {
                saveResult && isAdd -> Response.responseMsg(WarehouseCodeEnum.ADD_WAREHOUSE_SUCCESS_EN)
                saveResult && !isAdd -> Response.responseMsg(WarehouseCodeEnum.UPDATE_WAREHOUSE_INFO_SUCCESS_EN)
                else -> Response.fail()
            }
        }
    }

    override fun deleteBatch(ids: List<Long>?): Response<String> {
        return ids.takeIf { it?.isNotEmpty() ?: false }
            ?.let {
                val updateResult = warehouseMapper.deleteBatchIds(it)
                val systemLanguage = baseService.currentUserSystemLanguage
                if (updateResult > 0) {
                    if (systemLanguage == "zh_CN") {
                        Response.responseMsg(WarehouseCodeEnum.DELETE_WAREHOUSE_SUCCESS)
                    } else {
                        Response.responseMsg(WarehouseCodeEnum.DELETE_WAREHOUSE_SUCCESS_EN)
                    }
                } else {
                    if (systemLanguage == "zh_CN") {
                        Response.responseMsg(WarehouseCodeEnum.DELETE_WAREHOUSE_ERROR)
                    } else {
                        Response.responseMsg(WarehouseCodeEnum.DELETE_WAREHOUSE_ERROR_EN)
                    }
                }
            }
            ?: Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

    override fun getWarehouse(): Response<List<WarehouseVO>> {
        val warehouseList = mutableListOf<WarehouseVO>()
        val warehouses = list()
        for (warehouse in warehouses) {
            BeanUtils.copyProperties(warehouse, WarehouseVO())
            warehouseList += WarehouseVO(
                id = warehouse.id,
                warehouseName = warehouse.warehouseName,
                warehouseManager = warehouse.warehouseManager,
                address = warehouse.address,
                price = warehouse.price,
                truckage = warehouse.truckage,
                type = warehouse.type,
                status = warehouse.status,
                remark = warehouse.remark,
                sort = warehouse.sort,
                isDefault = warehouse.isDefault,
                createTime = warehouse.createTime
            )
        }
        return Response.responseData(warehouseList)
    }

    override fun updateBatchStatus(ids: List<Long>?, status: Int?): Response<String> {
        if (ids?.isEmpty() == true) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
        }

        val updateResult = lambdaUpdate()
            .`in`(Warehouse::getId, ids)
            .set(Warehouse::getStatus, status)
            .update()

        val systemLanguage = baseService.currentUserSystemLanguage
        return if (!updateResult) {
            if (systemLanguage == "zh_CN") {
                Response.responseMsg(WarehouseCodeEnum.UPDATE_WAREHOUSE_STATUS_ERROR)
            } else {
                Response.responseMsg(WarehouseCodeEnum.UPDATE_WAREHOUSE_STATUS_ERROR_EN)
            }
        } else {
            if (systemLanguage == "zh_CN") {
                Response.responseMsg(WarehouseCodeEnum.UPDATE_WAREHOUSE_STATUS_SUCCESS)
            } else {
                Response.responseMsg(WarehouseCodeEnum.UPDATE_WAREHOUSE_STATUS_SUCCESS_EN)
            }
        }
    }

    override fun getWarehouseByName(name: String?): Warehouse {
        if (name.isNullOrEmpty()) {
            return Warehouse()
        }

        return lambdaQuery()
            .eq(Warehouse::getWarehouseName, name)
            .eq(Warehouse::getDeleteFlag, CommonConstants.NOT_DELETED)
            .one()
            ?: Warehouse()
    }

    override fun getWarehouseList(): Response<List<WarehouseVO>> {
        val warehouseList = mutableListOf<WarehouseVO>()
        val warehouses = list()
        for (warehouse in warehouses) {
            val warehouseVO = WarehouseVO(
                id = warehouse.id,
                warehouseName = warehouse.warehouseName,
                warehouseManager = warehouse.warehouseManager,
                address = warehouse.address,
                price = warehouse.price,
                truckage = warehouse.truckage,
                type = warehouse.type,
                status = warehouse.status,
                remark = warehouse.remark,
                sort = warehouse.sort,
                isDefault = warehouse.isDefault,
                createTime = warehouse.createTime
            )
            warehouseList.add(warehouseVO)
        }
        return Response.responseData(warehouseList)
    }

    override fun getDefaultWarehouse(): Response<WarehouseVO> {
        val warehouse = lambdaQuery()
            .eq(Warehouse::getIsDefault, CommonConstants.IS_DEFAULT)
            .eq(Warehouse::getDeleteFlag, CommonConstants.NOT_DELETED)
            .one()
        return if (warehouse != null) {
            val warehouseVO = WarehouseVO(
                id = warehouse.id,
                warehouseName = warehouse.warehouseName,
                warehouseManager = warehouse.warehouseManager,
                address = warehouse.address,
                price = warehouse.price,
                truckage = warehouse.truckage,
                type = warehouse.type,
                status = warehouse.status,
                remark = warehouse.remark,
                sort = warehouse.sort,
                isDefault = warehouse.isDefault,
                createTime = warehouse.createTime
            )
            Response.responseData(warehouseVO)
        } else {
            Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY)
        }
    }
}