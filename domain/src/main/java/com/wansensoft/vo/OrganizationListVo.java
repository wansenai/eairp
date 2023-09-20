package com.wansensoft.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationListVo {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String organizationNumber;

    private String organizationName;

    private String remark;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    private String sort;
}
