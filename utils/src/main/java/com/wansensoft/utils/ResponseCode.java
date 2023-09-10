package com.wansensoft.utils;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;


public class ResponseCode {

    public final int code;
    public final Object data;




    @JSONCreator
    public ResponseCode(@JSONField(name = "code") int code, @JSONField(name = "data")Object data) {
        this.code = code;
        this.data = data;
    }
}
