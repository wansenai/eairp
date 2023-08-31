package com.wansensoft.service.depot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.depot.Depot;
import com.wansensoft.entities.depot.DepotEx;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface DepotService extends IService<Depot> {

    Depot getDepot(long id);

    List<Depot> getDepotListByIds(String ids);

    List<Depot> getDepot();

    List<Depot> getAllList();

    List<DepotEx> select(String name, Integer type, String remark, int offset, int rows);

    Long countDepot(String name, Integer type, String remark);

    int insertDepot(JSONObject obj, HttpServletRequest request);

    int updateDepot(JSONObject obj, HttpServletRequest request);

    int deleteDepot(Long id, HttpServletRequest request);

    int batchDeleteDepot(String ids, HttpServletRequest request);

    int batchDeleteDepotByIds(String ids);

    int checkIsNameExist(Long id, String name);

    List<Depot> findUserDepot();

    int updateIsDefault(Long depotId);

    Long getIdByName(String name);

    List<Long> parseDepotList(Long depotId);

    JSONArray findDepotByCurrentUser();

    String findDepotStrByCurrentUser();

    int batchSetStatus(Boolean status, String ids);
}
