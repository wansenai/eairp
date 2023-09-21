package com.wansensoft.dto.organization;

import com.wansensoft.dto.PageSizeDto;
import lombok.Data;

@Data
public class OrganizationListDto extends PageSizeDto {

    private String organizationName;

    private String leader;
}
