package com.wansensoft.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class SmsInfoBO {

    private String secretId;

    private String secretKey;

    private String smsClint;

    private String sdkAppId;
}
