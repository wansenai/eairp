package com.wansensoft.service.log;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.log.Log;
import com.wansensoft.vo.LogVo4List;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface LogService extends IService<Log> {
    Log getLog(long id);

    List<Log> getLog();

    List<LogVo4List> select(String operation, String userInfo, String clientIp, Integer status, String beginTime, String endTime,
                            String content, int offset, int rows);

    Long countLog(String operation, String userInfo, String clientIp, Integer status, String beginTime, String endTime,
                  String content);

    int insertLog(JSONObject obj, HttpServletRequest request);

    int updateLog(JSONObject obj, HttpServletRequest request);

    int deleteLog(Long id, HttpServletRequest request);

    int batchDeleteLog(String ids, HttpServletRequest request);

    void insertLog(String moduleName, String content, HttpServletRequest request);

    void insertLogWithUserId(Long userId, Long tenantId, String moduleName, String content, HttpServletRequest request);
}
