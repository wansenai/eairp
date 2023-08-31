package com.wansensoft.service.platformConfig;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.platformConfig.PlatformConfig;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PlatformConfigService extends IService<PlatformConfig> {

    PlatformConfig getPlatformConfig(long id);

    List<PlatformConfig> getPlatformConfig();

    List<PlatformConfig> select(String platformKey, int offset, int rows);

    Long countPlatformConfig(String platformKey);

    int insertPlatformConfig(JSONObject obj, HttpServletRequest request);

    int updatePlatformConfig(JSONObject obj, HttpServletRequest request);

    int deletePlatformConfig(Long id, HttpServletRequest request);

    int batchDeletePlatformConfig(String ids, HttpServletRequest request);

    int updatePlatformConfigByKey(String platformKey, String platformValue);

    PlatformConfig getInfoByKey(String platformKey);

    PlatformConfig getPlatformConfigByKey(String platformKey);
}
