package com.wansensoft.dto.user;

import com.wansensoft.dto.PageSizeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserListDTO extends PageSizeDTO {

    private String username;

    private String email;

    private String name;

    private String phoneNumber;

    private String departmentId;
}
