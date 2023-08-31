package com.wansensoft.service.depotHead;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.depot.DepotHead;
import com.wansensoft.vo.DepotHeadVo4InDetail;
import com.wansensoft.vo.DepotHeadVo4InOutMCount;
import com.wansensoft.vo.DepotHeadVo4List;
import com.wansensoft.vo.DepotHeadVo4StatementAccount;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DepotHeadService extends IService<DepotHead> {

    DepotHead getDepotHead(long id);

    List<DepotHead> getDepotHead();

    List<DepotHeadVo4List> select(String type, String subType, String roleType, String hasDebt, String status, String purchaseStatus, String number, String linkNumber,
                                  String beginTime, String endTime, String materialParam, Long organId, Long creator, Long depotId, Long accountId, String remark, int offset, int rows);

    Long countDepotHead(String type, String subType, String roleType, String hasDebt, String status, String purchaseStatus, String number, String linkNumber,
                        String beginTime, String endTime, String materialParam, Long organId, Long creator, Long depotId, Long accountId, String remark);

    String[] getDepotArray(String subType);

    String[] getCreatorArray(String roleType);

    String[] getOrganArray(String subType, String purchaseStatus);

    String getCreatorByRoleType(String roleType);

    Map<String, BigDecimal> getFinishDepositMapByNumberList(List<String> numberList);

    Map<String, Integer> getBillSizeMapByLinkNumberList(List<String> numberList);

    Map<Long,Integer> getFinancialBillNoMapByBillIdList(List<Long> idList);

    int insertDepotHead(JSONObject obj, HttpServletRequest request);

    int updateDepotHead(JSONObject obj, HttpServletRequest request);

    int deleteDepotHead(Long id, HttpServletRequest request);

    int batchDeleteDepotHead(String ids, HttpServletRequest request);

    int batchDeleteBillByIds(String ids);

    int batchDeleteDepotHeadByIds(String ids);

    List<DepotHead> getDepotHeadListByIds(String ids);

    int checkIsBillNumberExist(Long id, String number);

    int batchSetStatus(String status, String depotHeadIDs);

    Map<Long,String> findMaterialsListMapByHeaderIdList(List<Long> idList);

    Map<Long,BigDecimal> getMaterialCountListMapByHeaderIdList(List<Long> idList);

    List<DepotHeadVo4InDetail> findInOutDetail(String beginTime, String endTime, String type, String [] creatorArray,
                                               String [] organArray, Boolean forceFlag, String materialParam, List<Long> depotList, Integer oId, String number,
                                               Long creator, String remark, Integer offset, Integer rows);

    int findInOutDetailCount(String beginTime, String endTime, String type, String [] creatorArray,
                             String [] organArray, Boolean forceFlag, String materialParam, List<Long> depotList, Integer oId, String number,
                             Long creator, String remark);

    List<DepotHeadVo4InOutMCount> findInOutMaterialCount(String beginTime, String endTime, String type, Boolean forceFlag, String materialParam,
                                                         List<Long> depotList, Integer oId, String roleType, Integer offset, Integer rows);

    int findInOutMaterialCountTotal(String beginTime, String endTime, String type, Boolean forceFlag, String materialParam,
                                    List<Long> depotList, Integer oId, String roleType);

    List<DepotHeadVo4InDetail> findAllocationDetail(String beginTime, String endTime, String subType, String number,
                                                    String [] creatorArray, Boolean forceFlag, String materialParam, List<Long> depotList, List<Long> depotFList,
                                                    String remark, Integer offset, Integer rows);

    int findAllocationDetailCount(String beginTime, String endTime, String subType, String number,
                                  String [] creatorArray, Boolean forceFlag, String materialParam, List<Long> depotList,  List<Long> depotFList,
                                  String remark);

    List<DepotHeadVo4StatementAccount> getStatementAccount(String beginTime, String endTime, Integer organId, String [] organArray,
                                                           String supplierType, String type, String subType, String typeBack, String subTypeBack, String billType, Integer offset, Integer rows);

    int getStatementAccountCount(String beginTime, String endTime, Integer organId,
                                 String [] organArray, String supplierType, String type, String subType, String typeBack, String subTypeBack, String billType);

    List<DepotHeadVo4StatementAccount> getStatementAccountTotalPay(String beginTime, String endTime, Integer organId,
                                                                   String [] organArray, String supplierType,
                                                                   String type, String subType, String typeBack, String subTypeBack, String billType);

    List<DepotHeadVo4List> getDetailByNumber(String number, HttpServletRequest request);

    List<DepotHead> getListByLinkNumberExceptCurrent(String linkNumber, String number, String type);

    List<DepotHead> getBillListByLinkNumberList(List<String> linkNumberList);

    List<DepotHead> getBillListByLinkNumber(String linkNumber);

    List<DepotHead> getBillListByLinkNumberExceptNumber(String linkNumber, String number);

    void addDepotHeadAndDetail(String beanJson, String rows,
                          HttpServletRequest request);

    void updateDepotHeadAndDetail(String beanJson, String rows,HttpServletRequest request);

    Map<String, Object> getBuyAndSaleStatistics(String today, String monthFirstDay, String yesterdayBegin, String yesterdayEnd,
                                                String yearBegin, String yearEnd, String roleType, HttpServletRequest request);

    BigDecimal getBuyAndSaleBasicStatistics(String type, String subType, Integer hasSupplier,
                                            String beginTime, String endTime, String[] creatorArray);

    BigDecimal getBuyAndSaleRetailStatistics(String type, String subType,
                                             String beginTime, String endTime, String[] creatorArray);

    DepotHead getDepotHead(String number);

    List<DepotHeadVo4List> debtList(Long organId, String materialParam, String number, String beginTime, String endTime,
                                    String roleType, String status, Integer offset, Integer rows);
    int debtListCount(Long organId, String materialParam, String number, String beginTime, String endTime,
                  String roleType, String status);

    String getBillCategory(String subType);
}
