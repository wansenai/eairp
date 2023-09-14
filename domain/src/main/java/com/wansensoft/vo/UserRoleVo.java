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
public class UserRoleVo {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roleId;

    private String roleName;

    private String roleType;
}
