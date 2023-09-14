package com.wansensoft.vo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuVo {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Integer id;

    private Integer parentId;

    private String name;

    private String path;

    private String component;

    private Integer sort;

    private String icon;

    private Integer menuType;

    private String type;

    private String redirect;

    private JSONObject meta;
}
