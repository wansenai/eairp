package com.wansensoft.dto.role

import com.wansensoft.dto.PageSizeDTO
import lombok.Data
import lombok.EqualsAndHashCode

@Data
@EqualsAndHashCode(callSuper = true)
class AddOrUpdateRoleDTO : PageSizeDTO() {

    var id : Long? = null

    var roleName: String? = null

    var type: String? = null

    var priceLimit: Int? = null

    var status: Int? = null

    var description: String? = null
}