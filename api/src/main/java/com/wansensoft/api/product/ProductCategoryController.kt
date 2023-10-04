package com.wansensoft.api.product

import com.wansensoft.service.product.ProductCategoryService
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.ProductCategoryVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product/category")
class ProductCategoryController(private val productCategoryService: ProductCategoryService) {

    @GetMapping("/list")
    fun productCategoryList(): Response<List<ProductCategoryVO>> {
        return productCategoryService.productCategoryList()
    }

}