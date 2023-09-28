package com.wansensoft.dto.role

import lombok.Data
import lombok.EqualsAndHashCode

@Data
@EqualsAndHashCode(callSuper = true)
class RolePermissionDTO {

    var id: Long? = null

    var menuIds: List<Int> = emptyList()
}