package com.wansensoft.service.inOutItem;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.inOutItem.InOutItem;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface InOutItemService extends IService<InOutItem> {

    InOutItem getInOutItem(long id);

    List<InOutItem> getInOutItemListByIds(String ids);

    List<InOutItem> getInOutItem();

    List<InOutItem> select(String name, String type, String remark, int offset, int rows);

    Long countInOutItem(String name, String type, String remark);

    int insertInOutItem(JSONObject obj, HttpServletRequest request);

    int updateInOutItem(JSONObject obj, HttpServletRequest request);

    int deleteInOutItem(Long id, HttpServletRequest request);

    int batchDeleteInOutItem(String ids, HttpServletRequest request);

    int batchDeleteInOutItemByIds(String ids);

    int checkIsNameExist(Long id, String name);

    List<InOutItem> findBySelect(String type);

    int batchSetStatus(Boolean status, String ids);
}
