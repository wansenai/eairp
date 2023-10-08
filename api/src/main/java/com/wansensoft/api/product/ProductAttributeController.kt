package com.wansensoft.api.product

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.wansensoft.dto.product.AddOrUpdateProductAttributeDTO
import com.wansensoft.dto.product.ProductAttributeQueryDTO
import com.wansensoft.service.product.ProductAttributeService
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.product.ProductAttributeVO
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product/attribute")
class ProductAttributeController(private val productAttributeService: ProductAttributeService) {

    @PostMapping("/list")
    fun productAttributeList(@RequestBody productAttributeQueryDTO: ProductAttributeQueryDTO): Response<Page<ProductAttributeVO>> {
        return productAttributeService.productAttributeList(productAttributeQueryDTO)
    }

    @PostMapping("/addOrUpdate")
    fun addOrUpdateProductAttribute(@RequestBody productAttributeDTO: AddOrUpdateProductAttributeDTO): Response<String> {
        return productAttributeService.addOrUpdateProductAttribute(productAttributeDTO)
    }

    @DeleteMapping("/deleteBatch")
    fun deleteProductAttribute(@RequestParam ids: List<Long>): Response<String> {
        return productAttributeService.batchDeleteProductAttribute(ids)
    }
}