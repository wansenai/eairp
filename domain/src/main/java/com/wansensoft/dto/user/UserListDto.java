package com.wansensoft.dto.user;

import com.wansensoft.dto.PageSizeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserListDto extends PageSizeDto {

    private String username;

    private String email;

    private String name;

    private String phoneNumber;
}
