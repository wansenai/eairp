package com.wansenai.dto.user;

import lombok.Data;

@Data
public class UpdatePasswordByEmailDto {

    String password;

    String email;

    String emailCode;
}
