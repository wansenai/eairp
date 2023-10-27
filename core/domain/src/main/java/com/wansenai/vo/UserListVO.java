package com.wansenai.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserListVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> roleId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> deptId;

    private String username;

    private String name;

    private String roleName;

    private String email;

    private String phoneNumber;

    private Integer status;

    private String type;

    private String deptName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
