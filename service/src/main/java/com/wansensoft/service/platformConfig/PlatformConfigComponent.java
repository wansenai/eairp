package com.wansensoft.service.platformConfig;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.service.ICommonQuery;
import com.wansensoft.utils.Constants;
import com.wansensoft.utils.QueryUtils;
import com.wansensoft.utils.StringUtil;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
@PlatformConfigResource
public class PlatformConfigComponent implements ICommonQuery {

    private final PlatformConfigServiceImpl platformConfigServiceImpl;

    public PlatformConfigComponent(PlatformConfigServiceImpl platformConfigServiceImpl) {
        this.platformConfigServiceImpl = platformConfigServiceImpl;
    }

    @Override
    public Object selectOne(Long id) throws Exception {
        return platformConfigServiceImpl.getPlatformConfig(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getPlatformConfigList(map);
    }

    private List<?> getPlatformConfigList(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String platformKey = StringUtil.getInfo(search, "platformKey");
        return platformConfigServiceImpl.select(platformKey, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String platformKey = StringUtil.getInfo(search, "platformKey");
        return platformConfigServiceImpl.countPlatformConfig(platformKey);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request)throws Exception {
        return platformConfigServiceImpl.insertPlatformConfig(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return platformConfigServiceImpl.updatePlatformConfig(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return platformConfigServiceImpl.deletePlatformConfig(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return platformConfigServiceImpl.batchDeletePlatformConfig(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return 0;
    }

}
