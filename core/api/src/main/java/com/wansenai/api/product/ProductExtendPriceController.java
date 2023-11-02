/*
 * Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://opensource.wansenai.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.wansenai.api.product;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wansenai.dto.product.QueryProductExtendPriceDTO;
import com.wansenai.service.product.ProductExtendPriceService;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.product.ProductExtendPriceVO;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 产品价格扩展 前端控制器
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@RestController
@RequestMapping("/product/extend-price")
public class ProductExtendPriceController {

    private final ProductExtendPriceService productExtendPriceService;

    public ProductExtendPriceController(ProductExtendPriceService productExtendPriceService) {
        this.productExtendPriceService = productExtendPriceService;
    }

    @PostMapping("pageList")
    public Response<IPage<ProductExtendPriceVO>> getProductExtendPrice(@RequestBody QueryProductExtendPriceDTO priceDTO) {
        var result = productExtendPriceService.getProductExtendPriceInfo(priceDTO);
        if(result != null) {
            return Response.responseData(result);
        }
        return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
    }

    @GetMapping("getProduct/{barCode}")
    public Response<ProductExtendPriceVO> getProductByBarCode(@PathVariable("barCode") Long barCode) {
        return productExtendPriceService.getProductByBarCode(barCode);
    }
}
