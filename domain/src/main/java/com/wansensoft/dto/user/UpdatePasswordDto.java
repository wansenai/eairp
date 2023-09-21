package com.wansensoft.dto.user;

import lombok.Data;

@Data
public class UpdatePasswordDto {

    String username;

    String password;

    String phoneNumber;

    String sms;
}
