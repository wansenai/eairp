package com.wansenai.vo.receipt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansenai.bo.BigDecimalSerializerBO;
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
public class RetailShipmentsVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String memberName;

    private String receiptNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime receiptDate;

    private String productInfo;

    private String operator;

    private Integer productNumber;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal totalPrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal collectionAmount;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal backAmount;

    private Integer status;
}
