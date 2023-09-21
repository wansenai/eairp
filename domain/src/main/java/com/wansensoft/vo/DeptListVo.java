package com.wansensoft.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeptListVo {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String deptNumber;

    private String deptName;

    private String remark;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    private String sort;
}
