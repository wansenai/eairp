package com.wansensoft.dto.user;

import lombok.Data;

@Data
public class MobileLoginDTO {

    private String phoneNumber;

    private Integer type;

    private String sms;
}
