package com.wansensoft.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeptListVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String deptNumber;

    private String deptName;

    private String remark;

    private String sort;

    private List<DeptChildrenVO> children;

    @Data
    public static class DeptChildrenVO {

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long id;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long parentId;

        private String deptNumber;

        private String deptName;

        private String remark;

        private String sort;
    }
}
