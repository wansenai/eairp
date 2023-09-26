package com.wansensoft.entities;

import lombok.Data;

@Data
public class Sequence {

    private String seqName;

    private Long minValue;

    private Long maxValue;

    private Long currentValue;

    private Integer incrementVal;

    private String remark;
}
