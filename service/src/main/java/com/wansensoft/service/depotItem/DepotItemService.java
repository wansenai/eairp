package com.wansensoft.service.depotItem;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.depot.DepotHead;
import com.wansensoft.entities.depot.DepotItem;
import com.wansensoft.entities.depot.DepotItemVo4DetailByTypeAndMId;
import com.wansensoft.entities.depot.DepotItemVo4WithInfoEx;
import com.wansensoft.entities.unit.Unit;
import com.wansensoft.vo.DepotItemStockWarningCount;
import com.wansensoft.vo.DepotItemVoBatchNumberList;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DepotItemService extends IService<DepotItem> {

    DepotItem getDepotItem(long id);

    List<DepotItem> select(String name, Integer type, String remark, int offset, int rows);

    Long countDepotItem(String name, Integer type, String remark);

    int insertDepotItem(JSONObject obj, HttpServletRequest request);

    int updateDepotItem(JSONObject obj, HttpServletRequest request);

    int deleteDepotItem(Long id, HttpServletRequest request);

    int batchDeleteDepotItem(String ids, HttpServletRequest request);

    int checkIsNameExist(Long id, String name);

    List<DepotItemVo4DetailByTypeAndMId> findDetailByDepotIdsAndMaterialIdList(String depotIds, Boolean forceFlag, String sku, String batchNumber,
                                                                               String number, String beginTime, String endTime, Long mId, int offset, int rows);

    Long findDetailByDepotIdsAndMaterialIdCount(String depotIds, Boolean forceFlag, String sku, String batchNumber,
                                                String number, String beginTime, String endTime, Long mId);

    int insertDepotItemWithObj(DepotItem depotItem);

    int updateDepotItemWithObj(DepotItem depotItem);

    List<DepotItem> getListByHeaderId(Long headerId);

    DepotItem getItemByHeaderIdAndMaterial(Long headerId, Long meId);

    DepotItem getPreItemByHeaderIdAndMaterial(String linkNumber, Long meId, Long linkId);

    List<DepotItemVo4WithInfoEx> getDetailList(Long headerId);

    List<DepotItemVo4WithInfoEx> findByAll(String materialParam, List<Long> categoryIdList, String endTime, Integer offset, Integer rows);

    int findByAllCount(String materialParam, List<Long> categoryIdList, String endTime);

    List<DepotItemVo4WithInfoEx> getListWithBugOrSale(String materialParam, String billType,
                                                      String beginTime, String endTime, String[] creatorArray, Long organId, String [] organArray, List<Long> depotList, Boolean forceFlag, Integer offset, Integer rows);

    int getListWithBugOrSaleCount(String materialParam, String billType,
                                  String beginTime, String endTime, String[] creatorArray, Long organId, String [] organArray, List<Long> depotList, Boolean forceFlag);

    BigDecimal buyOrSale(String type, String subType, Long MId, String beginTime, String endTime,
                         String[] creatorArray, Long organId, String [] organArray, List<Long> depotList, Boolean forceFlag, String sumType);

    BigDecimal inOrOutPrice(String type, String subType, String month, String roleType);

    BigDecimal inOrOutRetailPrice(String type, String subType, String month, String roleType);

    void saveDetials(String rows, Long headerId, String actionType, HttpServletRequest request);

    String getBillStatusByParam(DepotHead depotHead);

    void changeBillStatus(DepotHead depotHead, String billStatus);

    void changeBillPurchaseStatus(DepotHead depotHead, String billStatus);

    DepotItem getDepotItemByBatchNumber(String batchNumber);

    void deleteDepotItemHeadId(Long headerId);

    void deleteOrCancelSerialNumber(String actionType, DepotHead depotHead, Long headerId);

    void checkAssembleWithMaterialType(JSONArray rowArr, String subType);

    void updateMaterialExtendPrice(Long meId, String subType, JSONObject rowObj);

    List<DepotItemStockWarningCount> findStockWarningCount(Integer offset, Integer rows, String materialParam, List<Long> depotList);

    int findStockWarningCountTotal(String materialParam, List<Long> depotList);

    BigDecimal getSkuStockByParam(Long depotId, Long meId, String beginTime, String endTime);

    BigDecimal getStockByParam(Long depotId, Long mId, String beginTime, String endTime);

    BigDecimal getStockByParamWithDepotList(List<Long> depotList, Long mId, String beginTime, String endTime);

    Map<String, BigDecimal> getIntervalMapByParamWithDepotList(List<Long> depotList, Long mId, String beginTime, String endTime);

    void updateCurrentStock(DepotItem depotItem);

    void updateCurrentStockFun(Long mId, Long dId);

    BigDecimal getFinishNumber(Long meId, Long id, Long headerId, Unit unitInfo, String materialUnit, String linkType);

    BigDecimal getRealFinishNumber(String currentSubType, Long meId, Long linkId, Long preHeaderId, Long currentHeaderId, Unit unitInfo, String materialUnit);

    List<DepotItemVoBatchNumberList> getBatchNumberList(String number, String name, Long depotId, String barCode, String batchNumber);

    Long getCountByMaterialAndDepot(Long mId, Long depotId);

    JSONObject parseMapByExcelData(String barCodes, List<Map<String, String>> detailList, String prefixNo);

    BigDecimal getLastUnitPriceByParam(Long organId, Long meId, String prefixNo);

    BigDecimal getCurrentStockByParam(Long depotId, Long mId);
}