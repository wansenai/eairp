package com.wansensoft.dto.user;

import lombok.Data;

@Data
public class AccountRegisterDto {

    String username;

    String password;

    String email;

    String captchaId;

    String captcha;
}
