package com.wansensoft.mappers.log;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansensoft.entities.log.Log;
import com.wansensoft.vo.LogVo4List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogMapperEx extends BaseMapper<Log> {

    List<LogVo4List> selectByConditionLog(
            @Param("operation") String operation,
            @Param("userInfo") String userInfo,
            @Param("clientIp") String clientIp,
            @Param("status") Integer status,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("content") String content,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByLog(
            @Param("operation") String operation,
            @Param("userInfo") String userInfo,
            @Param("clientIp") String clientIp,
            @Param("status") Integer status,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("content") String content);

    Long getCountByIpAndDate(
            @Param("userId") Long userId,
            @Param("moduleName") String moduleName,
            @Param("clientIp") String clientIp,
            @Param("createTime") String createTime);

    @InterceptorIgnore(tenantLine = "true")
    int insertLogWithUserId(Log log);
}