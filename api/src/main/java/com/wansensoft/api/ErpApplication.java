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
package com.wansensoft.api;

import com.wansensoft.utils.ComputerInfo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@EnableScheduling
@MapperScan("com.wansensoft.mappers")
@ComponentScan("com.wansensoft")
@SpringBootApplication
public class ErpApplication {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(ErpApplication.class, args);
        Environment environment = context.getBean(Environment.class);
        System.out.println("启动成功，后端服务API地址：http://" + ComputerInfo.getIpAddr() + ":"
                + environment.getProperty("server.port") + "/wansenerp/doc.html");
        System.out.println("您还需启动前端服务，启动命令：yarn run serve 或 npm run serve，测试用户：wansenerp，密码：123456");
    }

}
