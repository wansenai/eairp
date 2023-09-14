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
package com.wansensoft.middleware.mybatisplus;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import java.util.Collections;

public class MpCodeQuickGeneration {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/wansenerp2?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root", "PaSsw0Rd")
                .globalConfig(builder -> {
                    builder.author("James Zow") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("E:\\opensource\\WansenERP\\middleware\\src\\main\\resources"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {

                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent("com.wansensoft.mappers") // 设置父包名
                            .moduleName("dao") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "E:\\opensource\\WansenERP\\middleware\\src\\main\\resources")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("product_attribute") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}