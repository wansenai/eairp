package com.wansenai.dto.user;

import lombok.Data;

@Data
public class EmailLoginDTO {

    private String email;

    private Integer type;

    private String emailCode;
}
