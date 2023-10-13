package com.wansensoft.dto.menu

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