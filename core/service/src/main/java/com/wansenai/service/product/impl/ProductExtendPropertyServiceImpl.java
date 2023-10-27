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
package com.wansenai.service.product.impl;

import com.wansenai.service.product.ProductExtendPropertyService;
import com.wansenai.entities.product.ProductExtendProperty;
import com.wansenai.mappers.product.ProductExtendPropertyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品扩展字段表 服务实现类
 * </p>
 */
@Service
public class ProductExtendPropertyServiceImpl extends ServiceImpl<ProductExtendPropertyMapper, ProductExtendProperty> implements ProductExtendPropertyService {

}
