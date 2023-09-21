package com.wansensoft.dto.organization;

import com.wansensoft.dto.PageSizeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeptListDto extends PageSizeDto {

    private String deptName;

    private String leader;
}
