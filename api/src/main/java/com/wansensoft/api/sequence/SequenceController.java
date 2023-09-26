package com.wansensoft.api.sequence;

import com.wansensoft.service.sequence.SequenceService;
import com.wansensoft.utils.BaseResponseInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/sequence")
@Api(tags = {"单据编号"})
public class SequenceController {
    private Logger logger = LoggerFactory.getLogger(SequenceController.class);

    private final SequenceService sequenceService;

    public SequenceController(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    /**
     * 单据编号生成接口
     * @param request
     * @return
     */
    @GetMapping(value = "/buildNumber")
    @ApiOperation(value = "单据编号生成接口")
    public BaseResponseInfo buildNumber(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String number = sequenceService.buildOnlyNumber();
            map.put("defaultNumber", number);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

}
