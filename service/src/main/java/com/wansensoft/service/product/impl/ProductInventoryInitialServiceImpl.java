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
package com.wansensoft.service.product.impl;

import com.wansensoft.service.product.IProductInventoryInitialService;
import com.wansensoft.entities.product.ProductInventoryInitial;
import com.wansensoft.mappers.product.ProductInventoryInitialMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品初始库存 服务实现类
 * </p>
 */
@Service
public class ProductInventoryInitialServiceImpl extends ServiceImpl<ProductInventoryInitialMapper, ProductInventoryInitial> implements IProductInventoryInitialService {

}
