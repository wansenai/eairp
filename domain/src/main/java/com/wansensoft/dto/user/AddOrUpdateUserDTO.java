package com.wansensoft.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class AddOrUpdateUserDTO {

    private Long id;

    private List<Long> deptId;

    private List<Long> roleId;

    private String name;

    private String username;

    private String password;

    private String phoneNumber;

    private String email;

    private String remake;
}
