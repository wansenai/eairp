package com.wansensoft.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaptchaVo {

    private String captchaId;

    private String imagePath;
}
