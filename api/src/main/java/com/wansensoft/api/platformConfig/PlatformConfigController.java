package com.wansensoft.api.platformConfig;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.entities.platformConfig.PlatformConfig;
import com.wansensoft.service.platformConfig.PlatformConfigService;
import com.wansensoft.utils.BaseResponseInfo;
import com.wansensoft.utils.ErpInfo;
import com.wansensoft.utils.ResponseJsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/platformConfig")
@Api(tags = {"平台参数"})
public class PlatformConfigController {
    private Logger logger = LoggerFactory.getLogger(PlatformConfigController.class);

    private final PlatformConfigService platformConfigService;

    public PlatformConfigController(PlatformConfigService platformConfigService) {
        this.platformConfigService = platformConfigService;
    }

    /**
     * 获取平台名称
     * @param request
     * @return
     */
    @GetMapping(value = "/getPlatform/name")
    @ApiOperation(value = "获取平台名称")
    public String getPlatformName(HttpServletRequest request) {
        String res;
        try {
            String platformKey = "platform_name";
            PlatformConfig platformConfig = platformConfigService.getInfoByKey(platformKey);
            res = platformConfig.getPlatformValue();
        } catch(Exception e){
            e.printStackTrace();
            res = "ERP系统";
        }
        return res;
    }

    /**
     * 获取官方网站地址
     * @param request
     * @return
     */
    @GetMapping(value = "/getPlatform/url")
    @ApiOperation(value = "获取官方网站地址")
    public String getPlatformUrl(HttpServletRequest request) {
        String res;
        try {
            String platformKey = "platform_url";
            PlatformConfig platformConfig = platformConfigService.getInfoByKey(platformKey);
            res = platformConfig.getPlatformValue();
        } catch(Exception e){
            e.printStackTrace();
            res = "#";
        }
        return res;
    }

    /**
     * 获取是否开启注册
     * @param request
     * @return
     */
    @GetMapping(value = "/getPlatform/registerFlag")
    @ApiOperation(value = "获取是否开启注册")
    public String getPlatformRegisterFlag(HttpServletRequest request) {
        String res;
        try {
            String platformKey = "register_flag";
            PlatformConfig platformConfig = platformConfigService.getInfoByKey(platformKey);
            res = platformConfig.getPlatformValue();
        } catch(Exception e){
            e.printStackTrace();
            res = "#";
        }
        return res;
    }

    /**
     * 根据platformKey更新platformValue
     * @param object
     * @param request
     * @return
     */
    @PostMapping(value = "/updatePlatformConfigByKey")
    @ApiOperation(value = "根据platformKey更新platformValue")
    public String updatePlatformConfigByKey(@RequestBody JSONObject object,
                                            HttpServletRequest request)throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        String platformKey = object.getString("platformKey");
        String platformValue = object.getString("platformValue");
        int res = platformConfigService.updatePlatformConfigByKey(platformKey, platformValue);
        if(res > 0) {
            return ResponseJsonUtil.returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else {
            return ResponseJsonUtil.returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }

    /**
     * 根据platformKey查询信息
     * @param platformKey
     * @param request
     * @return
     */
    @GetMapping(value = "/getInfoByKey")
    @ApiOperation(value = "根据platformKey查询信息")
    public BaseResponseInfo getInfoByKey(@RequestParam("platformKey") String platformKey,
                                         HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            PlatformConfig platformConfig = platformConfigService.getInfoByKey(platformKey);
            res.code = 200;
            res.data = platformConfig;
        } catch(Exception e){
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }
}
