package com.wansensoft.vo.financial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansensoft.bo.BigDecimalSerializerBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 名称
     */
    private String accountName;

    /**
     * 账户编号
     */
    private String accountNumber;

    /**
     * 期初金额
     */
    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal initialAmount;

    /**
     * 当前余额
     */
    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal currentAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否默认
     */
    private Integer isDefault;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
