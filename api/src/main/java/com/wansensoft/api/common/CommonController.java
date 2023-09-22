/*
 * Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://opensource.wansenai.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.wansensoft.api.common;

import com.wansensoft.service.common.CommonService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.utils.constants.ApiVersionConstants;
import com.wansensoft.utils.enums.CodeEnum;
import com.wansensoft.vo.CaptchaVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiVersionConstants.API_CLASS_VERSION_V2 + "common")
public class CommonController {

    private final CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping( "captcha")
    public Response<CaptchaVO> getCaptcha() {
        CaptchaVO captchaVo = commonService.getCaptcha();
        if(captchaVo == null) {
            return Response.responseMsg(CodeEnum.ERROR);
        }
        return Response.responseData(captchaVo);
    }

    @GetMapping("sms/{type}/{phoneNumber}")
    public Response<String> sendSmsCode(@PathVariable Integer type, @PathVariable String phoneNumber) {
        boolean result = commonService.sendSmsCode(type, phoneNumber);
        if(!result) {
            return Response.responseMsg(CodeEnum.PHONE_NUMBER_FORMAT_ERROR);
        }
        return Response.responseMsg(CodeEnum.SMS_VERIFY_SEND_SUCCESS);
    }
}
