package com.wansensoft.dto.role;

import com.wansensoft.dto.PageSizeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleListDTO extends PageSizeDTO {

    private String roleName;

    private Integer status;
}
