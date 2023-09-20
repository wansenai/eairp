package com.wansensoft.dto.user;

import lombok.Data;

@Data
public class MobileLoginDto {

    private String phoneNumber;

    private Integer type;

    private String sms;
}
