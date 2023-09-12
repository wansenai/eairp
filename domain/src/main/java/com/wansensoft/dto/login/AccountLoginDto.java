package com.wansensoft.dto.login;

import lombok.Data;

@Data
public class AccountLoginDto {

    String username;

    String password;

    String captchaId;

    String captcha;
}
