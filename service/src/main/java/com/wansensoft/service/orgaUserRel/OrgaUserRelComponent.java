package com.wansensoft.service.orgaUserRel;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.service.ICommonQuery;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Description
 */
@Service
@OrgaUserRelResource
public class OrgaUserRelComponent implements ICommonQuery {
    private final OrgaUserRelServiceImpl orgaUserRelServiceImpl;

    public OrgaUserRelComponent(OrgaUserRelServiceImpl orgaUserRelServiceImpl) {
        this.orgaUserRelServiceImpl = orgaUserRelServiceImpl;
    }

    @Override
    public Object selectOne(Long id) throws Exception {
        return orgaUserRelServiceImpl.getOrgaUserRel(id);
    }

    @Override
    public List<?> select(Map<String, String> parameterMap)throws Exception {
        return getOrgaUserRelList(parameterMap);
    }
    private List<?> getOrgaUserRelList(Map<String, String> map)throws Exception {
        return null;
    }
    @Override
    public Long counts(Map<String, String> parameterMap)throws Exception {
        return null;
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request)throws Exception {
        return orgaUserRelServiceImpl.insertOrgaUserRel(obj,request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return orgaUserRelServiceImpl.updateOrgaUserRel(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return orgaUserRelServiceImpl.deleteOrgaUserRel(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return orgaUserRelServiceImpl.batchDeleteOrgaUserRel(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return 0;
    }
}
