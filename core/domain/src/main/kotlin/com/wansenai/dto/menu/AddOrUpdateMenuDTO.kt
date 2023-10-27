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
package com.wansenai.dto.menu

import lombok.Data

@Data
class AddOrUpdateMenuDTO {

    val id : Int? = null

    val menuType : Int? = null

    val name : String? = null

    val title: String? = null

    val parentId: Int? = null

    val sort: Int? = null

    val icon: String? = null

    val path: String? = null

    val component: String? = null

    val status: Int? = null

    val blank: Int? = null

    val ignoreKeepAlive: Int? = null

    val hideMenu: Int? = null
}