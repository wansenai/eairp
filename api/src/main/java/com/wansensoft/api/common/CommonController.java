package com.wansensoft.api.common;

import com.wansensoft.service.common.CommonService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.utils.constants.ApiVersionConstants;
import com.wansensoft.utils.enums.CodeEnum;
import com.wansensoft.vo.CaptchaVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiVersionConstants.API_VERSION_V2 + "common")
public class CommonController {

    private final CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping( "captcha")
    public Response<CaptchaVo> getCaptcha() {
        CaptchaVo captchaVo = commonService.getCaptcha();
        if(captchaVo == null) {
            return Response.responseMsg(CodeEnum.ERROR);
        }
        return Response.responseData(captchaVo);
    }
}
