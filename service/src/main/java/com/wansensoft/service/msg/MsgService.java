package com.wansensoft.service.msg;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.msg.Msg;
import com.wansensoft.entities.msg.MsgEx;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface MsgService extends IService<Msg> {

    Msg getMsg(long id);

    List<Msg> getMsg();

    List<MsgEx> select(String name, int offset, int rows);

    Long countMsg(String name);

    int insertMsg(JSONObject obj, HttpServletRequest request);

    int updateMsg(JSONObject obj, HttpServletRequest request);

    int deleteMsg(Long id, HttpServletRequest request);

    int batchDeleteMsg(String ids, HttpServletRequest request);

    int checkIsNameExist(Long id, String name);

    int batchDeleteMsgByIds(String ids);

    List<MsgEx> getMsgByStatus(String status);

    void batchUpdateStatus(String ids, String status);

    Long getMsgCountByStatus(String status);

    Integer getMsgCountByType(String type);

    void readAllMsg();
}
