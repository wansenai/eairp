package com.wansensoft.erp.datasource.mappers;

import com.wansensoft.erp.datasource.entities.AccountItem;
import com.wansensoft.erp.datasource.vo.AccountItemVo4List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface AccountItemMapperEx {

    List<AccountItem> selectByConditionAccountItem(
            @Param("name") String name,
            @Param("type") Integer type,
            @Param("remark") String remark,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByAccountItem(
            @Param("name") String name,
            @Param("type") Integer type,
            @Param("remark") String remark);

    List<AccountItemVo4List> getDetailList(
            @Param("headerId") Long headerId);

    int batchDeleteAccountItemByIds(@Param("updateTime") Date updateTime, @Param("updater") Long updater, @Param("ids") String[] ids);

    List<AccountItem> getAccountItemListByAccountIds(@Param("accountIds") String[] accountIds);

    List<AccountItem> getAccountItemListByHeaderIds(@Param("headerIds") String[] headerIds);

    List<AccountItem> getAccountItemListByInOutItemIds(@Param("inOutItemIds") String[] inOutItemIds);

    int batchDeleteAccountItemByHeadIds(@Param("updateTime") Date updateTime, @Param("updater") Long updater, @Param("ids") String[] ids);

    BigDecimal getEachAmountByBillId(@Param("billId") Long billId);

    BigDecimal getFinishDebtByOrganId(@Param("organId") Long organId);
}