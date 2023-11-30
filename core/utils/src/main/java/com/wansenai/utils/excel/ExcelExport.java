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
package com.wansenai.utils.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface
ExcelExport {

    /** 字段名称 */
    String value();

    /** 导出排序先后: 数字越小越靠前（默认按Java类字段顺序导出） */
    int sort() default 0;

    /** 导出映射，格式如：0-未知;1-男;2-女 */
    String kv() default "";

    /** 导出模板示例值（有值的话，直接取该值，不做映射） */
    String example() default "";

    // 引用自定义序列化器
    Class<?> serializer() default Void.class;
}
