package com.wansensoft.service.unit;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.unit.Unit;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;

public interface UnitService extends IService<Unit> {

    Unit getUnit(long id);

    List<Unit> getUnitListByIds(String ids);

    List<Unit> getUnit();

    List<Unit> select(String name, int offset, int rows);

    Long countUnit(String name);

    int insertUnit(JSONObject obj, HttpServletRequest request);

    int updateUnit(JSONObject obj, HttpServletRequest request);

    int deleteUnit(Long id, HttpServletRequest request);

    int batchDeleteUnit(String ids, HttpServletRequest request);

    int batchDeleteUnitByIds(String ids);

    int checkIsNameExist(Long id, String name);

    Long getUnitIdByParam(String basicUnit, String otherUnit, BigDecimal ratio);

    BigDecimal parseStockByUnit(BigDecimal stock, Unit unitInfo, String materialUnit);

    int batchSetStatus(Boolean status, String ids);
}
