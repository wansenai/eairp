package com.wansensoft.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoVo {

    /** 用户id */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /** 用户昵称（别名 姓名） */
    private String name;

    /** 用户名（登陆的账户） */
    private String userName;

    /** 用户头像地址 */
    private String avatar;

    /** 用户token */
    private String token;

    /** 过期 */
    private long expire;
}
