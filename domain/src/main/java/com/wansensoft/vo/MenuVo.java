package com.wansensoft.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuVo {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Long id;

    private Integer parentId;

    private String name;

    private String path;

    private String component;

    private Integer sort;

    private String icon;

    private Integer menuType;

    private String type;
}
