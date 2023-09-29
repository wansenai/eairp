package com.wansensoft.dto.department

import lombok.Data
import lombok.EqualsAndHashCode

@Data
@EqualsAndHashCode(callSuper = true)
class AddOrUpdateDeptDTO {

    var id : Long? = null

    var deptName: String? = null

    var parentId: Long? = null

    var deptNumber: String? = null

    var leader: String? = null

    var status: Int? = null

    var remark: String? = null

    var sort: String? = null
}