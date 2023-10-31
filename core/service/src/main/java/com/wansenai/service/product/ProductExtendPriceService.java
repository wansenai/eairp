/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wansenai.service.product;

import com.wansenai.utils.response.Response;
import com.wansenai.entities.product.ProductExtendPrice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 产品价格扩展 服务类
 * </p>
 */
public interface ProductExtendPriceService extends IService<ProductExtendPrice> {

    Response<Integer> getBarCode();

    Boolean checkProductCode(List<String> barCodes);
}
