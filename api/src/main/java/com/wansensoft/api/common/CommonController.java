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
import com.wansensoft.utils.ExcelUtil;
import com.wansensoft.utils.response.Response;
import com.wansensoft.utils.constants.ApiVersionConstants;
import com.wansensoft.utils.enums.BaseCodeEnum;
import com.wansensoft.vo.CaptchaVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;


@RestController
@RequestMapping(ApiVersionConstants.API_CLASS_VERSION_V2 + "common")
@Slf4j
public class CommonController {

    private final CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping( "captcha")
    public Response<CaptchaVO> getCaptcha() {
        CaptchaVO captchaVo = commonService.getCaptcha();
        if(captchaVo == null) {
            return Response.responseMsg(BaseCodeEnum.ERROR);
        }
        return Response.responseData(captchaVo);
    }

    @GetMapping("sms/{type}/{phoneNumber}")
    public Response<String> sendSmsCode(@PathVariable Integer type, @PathVariable String phoneNumber) {
        boolean result = commonService.sendSmsCode(type, phoneNumber);
        if(!result) {
            return Response.responseMsg(BaseCodeEnum.PHONE_NUMBER_FORMAT_ERROR);
        }
        return Response.responseMsg(BaseCodeEnum.SMS_VERIFY_SEND_SUCCESS);
    }

    @PostMapping("upload/excel")
    public Response<String> uploadExclsData(@RequestParam("file") MultipartFile file) {
        return commonService.uploadExclsData(file);
    }

    @GetMapping("/export/excel")
    public void exportExcel(@RequestParam("type") String type, HttpServletResponse response) {
        try {
            File file = commonService.exportExcel(type);
            ExcelUtil.downloadExcel(file, file.getName(), response);
        }catch (Exception e) {
            log.error("导出excel异常", e);
        }
    }
}
