package com.wansensoft.service.sequence;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.Sequence;
import com.wansensoft.entities.serialNumber.SerialNumber;
import com.wansensoft.entities.serialNumber.SerialNumberEx;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SequenceService extends IService<Sequence> {

    String buildOnlyNumber();

    SerialNumber getSequence(long id);

    List<SerialNumberEx> select(String name, Integer offset, Integer rows);

    Long countSequence(String name);

    int insertSequence(JSONObject obj, HttpServletRequest request);

    int updateSequence(JSONObject obj, HttpServletRequest request);

    int deleteSequence(Long id, HttpServletRequest request);

    int batchDeleteSequence(String ids, HttpServletRequest request);

    int checkIsNameExist(Long id, String serialNumber);
}
