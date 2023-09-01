package com.wansensoft.service.platformConfig;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.platformConfig.PlatformConfig;
import com.wansensoft.entities.platformConfig.PlatformConfigExample;
import com.wansensoft.service.CommonService;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.mappers.platformConfig.PlatformConfigMapper;
import com.wansensoft.mappers.platformConfig.PlatformConfigMapperEx;
import com.wansensoft.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class PlatformConfigServiceImpl extends ServiceImpl<PlatformConfigMapper, PlatformConfig> implements PlatformConfigService{
    private Logger logger = LoggerFactory.getLogger(PlatformConfigServiceImpl.class);

    private final CommonService commonService;
    private final PlatformConfigMapper platformConfigMapper;
    private final PlatformConfigMapperEx platformConfigMapperEx;

    public PlatformConfigServiceImpl(CommonService commonService, PlatformConfigMapper platformConfigMapper, PlatformConfigMapperEx platformConfigMapperEx) {
        this.commonService = commonService;
        this.platformConfigMapper = platformConfigMapper;
        this.platformConfigMapperEx = platformConfigMapperEx;
    }

    public PlatformConfig getPlatformConfig(long id) {
        PlatformConfig result=null;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                result = platformConfigMapper.selectByPrimaryKey(id);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<PlatformConfig> getPlatformConfig() {
        PlatformConfigExample example = new PlatformConfigExample();
        example.createCriteria();
        List<PlatformConfig> list=null;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                list = platformConfigMapper.selectByExample(example);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<PlatformConfig> select(String platformKey, int offset, int rows) {
        List<PlatformConfig> list=null;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                list = platformConfigMapperEx.selectByConditionPlatformConfig(platformKey, offset, rows);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countPlatformConfig(String platformKey) {
        Long result=null;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                result = platformConfigMapperEx.countsByPlatformConfig(platformKey);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertPlatformConfig(JSONObject obj, HttpServletRequest request) {
        PlatformConfig platformConfig = JSONObject.parseObject(obj.toJSONString(), PlatformConfig.class);
        int result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                result = platformConfigMapper.insertSelective(platformConfig);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updatePlatformConfig(JSONObject obj, HttpServletRequest request) {
        PlatformConfig platformConfig = JSONObject.parseObject(obj.toJSONString(), PlatformConfig.class);
        int result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                result = platformConfigMapper.updateByPrimaryKeySelective(platformConfig);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deletePlatformConfig(Long id, HttpServletRequest request) {
        int result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                result = platformConfigMapper.deleteByPrimaryKey(id);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeletePlatformConfig(String ids, HttpServletRequest request) {
        List<Long> idList = StringUtil.strToLongList(ids);
        PlatformConfigExample example = new PlatformConfigExample();
        example.createCriteria().andIdIn(idList);
        int result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                result = platformConfigMapper.deleteByExample(example);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public int updatePlatformConfigByKey(String platformKey, String platformValue) {
        int result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                PlatformConfig platformConfig = new PlatformConfig();
                platformConfig.setPlatformValue(platformValue);
                PlatformConfigExample example = new PlatformConfigExample();
                example.createCriteria().andPlatformKeyEqualTo(platformKey);
                result = platformConfigMapper.updateByExampleSelective(platformConfig, example);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public PlatformConfig getInfoByKey(String platformKey) {
        PlatformConfig platformConfig = new PlatformConfig();
        try{
            if(platformKey.contains("aliOss") || platformKey.contains("weixin")) {
                platformConfig = null;
            } else {
                PlatformConfigExample example = new PlatformConfigExample();
                example.createCriteria().andPlatformKeyEqualTo(platformKey);
                List<PlatformConfig> list = this.baseMapper.getInfoByKey(example);
                if(list!=null && !list.isEmpty()){
                    platformConfig = list.get(0);
                }
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return platformConfig;
    }

    /**
     * 根据key查询平台信息-内部专用方法
     * @param platformKey
     * @return
     * @throws Exception
     */
    public PlatformConfig getPlatformConfigByKey(String platformKey) {
        PlatformConfig platformConfig = new PlatformConfig();
        try{
            PlatformConfigExample example = new PlatformConfigExample();
            example.createCriteria().andPlatformKeyEqualTo(platformKey);
            List<PlatformConfig> list=platformConfigMapper.selectByExample(example);
            if(list!=null && !list.isEmpty()){
                platformConfig = list.get(0);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return platformConfig;
    }
}
