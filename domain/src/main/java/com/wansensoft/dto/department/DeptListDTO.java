package com.wansensoft.dto.department;

import com.wansensoft.dto.PageSizeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeptListDTO extends PageSizeDTO {

    private String deptName;
}
