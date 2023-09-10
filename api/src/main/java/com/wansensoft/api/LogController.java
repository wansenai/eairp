package com.wansensoft.api;

import com.wansensoft.entities.log.Log;
import com.wansensoft.entities.user.User;
import com.wansensoft.service.log.LogService;
import com.wansensoft.utils.BaseResponseInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/log")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/getAllList")
    public BaseResponseInfo getAllList(HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            Map<String, Object> data = new HashMap<>();
            List<Log> dataList = logService.getLog();
            if(dataList!=null) {
                data.put("logList", dataList);
            }
            res.code = 200;
            res.data = data;
        } catch(Exception e){
            res.code = 500;
            res.data = "获取失败";
        }
        return res;
    }
}
