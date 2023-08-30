package com.wansensoft;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.datasource.entities.Unit;
import com.wansensoft.service.unit.UnitService;
import com.wansensoft.utils.BaseResponseInfo;
import com.wansensoft.utils.ErpInfo;
import com.wansensoft.utils.ResponseJsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @Author: qiankunpingtai
 * @Date: 2019/4/1 15:38
 */
@RestController
@RequestMapping(value = "/unit")
@Api(tags = {"单位管理"})
public class UnitController {

    @Resource
    private UnitService unitService;

    /**
     * 单位列表
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getAllList")
    @ApiOperation(value = "单位列表")
    public BaseResponseInfo getAllList(HttpServletRequest request) throws Exception{
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            List<Unit> unitList = unitService.getUnit();
            res.code = 200;
            res.data = unitList;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 批量设置状态-启用或者禁用
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping(value = "/batchSetStatus")
    @ApiOperation(value = "批量设置状态")
    public String batchSetStatus(@RequestBody JSONObject jsonObject,
                                 HttpServletRequest request)throws Exception {
        Boolean status = jsonObject.getBoolean("status");
        String ids = jsonObject.getString("ids");
        Map<String, Object> objectMap = new HashMap<>();
        int res = unitService.batchSetStatus(status, ids);
        if(res > 0) {
            return ResponseJsonUtil.returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else {
            return ResponseJsonUtil.returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }
}
