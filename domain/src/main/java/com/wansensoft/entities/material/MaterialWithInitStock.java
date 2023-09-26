package com.wansensoft.entities.material;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class MaterialWithInitStock extends Material {

    private Map<Long, BigDecimal> stockMap;

    private JSONObject materialExObj;
}