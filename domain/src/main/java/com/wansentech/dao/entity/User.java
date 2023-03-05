/*
 * Copyright 2023 wansentech.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wansentech.dao.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jameszow
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -7992197375083373335L;
    
    private Long id;

    private String name;

    private String userName;

    private String password;

    private String email;

    private String phoneNumber;

    private int status;

    private String remark;

    private Long tenantId;

}
