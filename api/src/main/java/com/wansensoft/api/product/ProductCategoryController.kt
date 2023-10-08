package com.wansensoft.api.product

import com.wansensoft.dto.product.AddOrUpdateProductCategoryDTO
import com.wansensoft.service.product.ProductCategoryService
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.product.ProductCategoryVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product/category")
class ProductCategoryController(private val productCategoryService: ProductCategoryService) {

    @GetMapping("/list")
    fun productCategoryList(): Response<List<ProductCategoryVO>> {
        return productCategoryService.productCategoryList()
    }

    @PostMapping("/addOrUpdate")
    fun addOrUpdateProductCategory(@RequestBody productCategory: AddOrUpdateProductCategoryDTO): Response<String> {
        return productCategoryService.addOrUpdateProductCategory(productCategory)
    }

    @PostMapping("/deleteBatch")
    fun deleteProductCategory(@RequestParam ids: List<Long>): Response<String> {
        return productCategoryService.deleteProductCategory(ids)
    }
}