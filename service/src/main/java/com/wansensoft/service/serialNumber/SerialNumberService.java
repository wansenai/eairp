package com.wansensoft.service.serialNumber;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.depot.DepotItem;
import com.wansensoft.entities.serialNumber.SerialNumber;
import com.wansensoft.entities.serialNumber.SerialNumberEx;
import com.wansensoft.entities.serialNumber.SerialNumberExample;
import com.wansensoft.entities.user.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SerialNumberService extends IService<SerialNumber> {

    SerialNumber getSerialNumber(long id);

    List<SerialNumber> getSerialNumberListByIds(String ids);

    List<SerialNumber> getSerialNumber();

    int insertSerialNumber(JSONObject obj, HttpServletRequest request);

    int updateSerialNumber(JSONObject obj, HttpServletRequest request);

    int deleteSerialNumber(Long id, HttpServletRequest request);

    int batchDeleteSerialNumber(String ids, HttpServletRequest request);

    int batchDeleteSerialNumberByIds(String ids);

    int checkIsNameExist(Long id, String serialNumber);

    Long checkMaterialName(String materialName);

    Long getSerialNumberMaterialIdByBarCode(String materialCode);

    void checkAndUpdateSerialNumber(DepotItem depotItem, String outBillNo, User userInfo, String snList);

    int sellSerialNumber(Long materialId, String outBillNo, String snList, User user);

    int cancelSerialNumber(Long materialId, String outBillNo,int count,User user);

    int batAddSerialNumber(String materialCode, String serialNumberPrefix, Integer batAddTotal, String remark);

    List<SerialNumberEx> getEnableSerialNumberList(String number, String name, Long depotId, String barCode, Integer offset, Integer rows);

    Long getEnableSerialNumberCount(String number, String name, Long depotId, String barCode);

    void addSerialNumberByBill(String type, String subType, String inBillNo, Long materialId, Long depotId, String snList);

    void deleteByExample(SerialNumberExample example);

    List<SerialNumberEx> select(String serialNumber, String materialName, Integer offset, Integer rows);

    Long countSerialNumber(String serialNumber,String materialName);
}
