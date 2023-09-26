package com.wansensoft.vo;

import com.wansensoft.entities.log.Log;
import lombok.Data;

@Data
public class LogVo4List extends Log {

    private String loginName;

    private String userName;

    private String createTimeStr;
}