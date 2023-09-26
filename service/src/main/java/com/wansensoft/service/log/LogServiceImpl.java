package com.wansensoft.service.log;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.log.Log;
import com.wansensoft.entities.log.LogExample;
import com.wansensoft.service.CommonService;
import com.wansensoft.service.user.UserService;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.mappers.log.LogMapper;
import com.wansensoft.mappers.log.LogMapperEx;
import com.wansensoft.service.redis.RedisService;
import com.wansensoft.utils.StringUtil;
import com.wansensoft.utils.Tools;
import com.wansensoft.vo.LogVo4List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static com.wansensoft.utils.Tools.getLocalIp;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    private Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    private final LogMapper logMapper;
    private final LogMapperEx logMapperEx;
    private final CommonService commonService;
    private final RedisService redisService;

    public LogServiceImpl(LogMapper logMapper, LogMapperEx logMapperEx, CommonService commonService, RedisService redisService) {
        this.logMapper = logMapper;
        this.logMapperEx = logMapperEx;
        this.commonService = commonService;
        this.redisService = redisService;
    }

    public Log getLog(long id) {
        Log result=null;
        try{
            result=logMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<Log> getLog() {
        LogExample example = new LogExample();
        List<Log> list=null;
        try{
            list=logMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<LogVo4List> select(String operation, String userInfo, String clientIp, Integer status, String beginTime, String endTime,
                                   String content, int offset, int rows) {
        List<LogVo4List> list=null;
        try{
            beginTime = Tools.parseDayToTime(beginTime,BusinessConstants.DAY_FIRST_TIME);
            endTime = Tools.parseDayToTime(endTime,BusinessConstants.DAY_LAST_TIME);
            list=logMapperEx.selectByConditionLog(operation, userInfo, clientIp, status, beginTime, endTime,
                    content, offset, rows);
            if (null != list) {
                for (LogVo4List log : list) {
                    log.setCreateTimeStr(Tools.getCenternTime(log.getCreateTime()));
                }
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countLog(String operation, String userInfo, String clientIp, Integer status, String beginTime, String endTime,
                        String content) {
        Long result=null;
        try{
            beginTime = Tools.parseDayToTime(beginTime,BusinessConstants.DAY_FIRST_TIME);
            endTime = Tools.parseDayToTime(endTime,BusinessConstants.DAY_LAST_TIME);
            result=logMapperEx.countsByLog(operation, userInfo, clientIp, status, beginTime, endTime, content);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertLog(JSONObject obj, HttpServletRequest request) {
        Log log = JSONObject.parseObject(obj.toJSONString(), Log.class);
        int result=0;
        try{
            result=logMapper.insertSelective(log);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateLog(JSONObject obj, HttpServletRequest request) {
        Log log = JSONObject.parseObject(obj.toJSONString(), Log.class);
        int result=0;
        try{
            result=logMapper.updateByPrimaryKeySelective(log);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteLog(Long id, HttpServletRequest request) {
        int result=0;
        try{
            result=logMapper.deleteByPrimaryKey(id);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteLog(String ids, HttpServletRequest request) {
        List<Long> idList = StringUtil.strToLongList(ids);
        LogExample example = new LogExample();
        example.createCriteria().andIdIn(idList);
        int result=0;
        try{
            result=logMapper.deleteByExample(example);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public void insertLog(String moduleName, String content, HttpServletRequest request) {
        try{
            Long userId = commonService.getUserId(request);
            if(userId!=null) {
                String clientIp = getLocalIp(request);
                String createTime = Tools.getNow3();
                Long count = logMapperEx.getCountByIpAndDate(userId, moduleName, clientIp, createTime);
                if(count > 0) {
                    //如果某个用户某个IP在同1秒内连续操作两遍，此时需要删除该redis记录，使其退出，防止恶意攻击
                    redisService.deleteObjectByUserAndIp(userId, clientIp);
                } else {
                    Log log = new Log();
                    log.setUserId(userId);
                    log.setOperation(moduleName);
                    log.setClientIp(getLocalIp(request));
                    log.setCreateTime(new Date());
                    Byte status = 0;
                    log.setStatus(status);
                    log.setContent(content);
                    logMapper.insertSelective(log);
                }
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
    }

    public void insertLogWithUserId(Long userId, Long tenantId, String moduleName, String content, HttpServletRequest request) {
        try{
            if(userId!=null) {
                Log log = new Log();
                log.setUserId(userId);
                log.setOperation(moduleName);
                log.setClientIp(getLocalIp(request));
                log.setCreateTime(new Date());
                Byte status = 0;
                log.setStatus(status);
                log.setContent(content);
                log.setTenantId(tenantId);
                logMapperEx.insertLogWithUserId(log);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
    }
}
