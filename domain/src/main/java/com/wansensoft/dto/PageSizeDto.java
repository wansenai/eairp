package com.wansensoft.dto;

import lombok.Data;

/**
 * 默认分页数据传输对象，针对需要进行分页查询的请求
 * 如果需要就继承该类
 */
@Data
public class PageSizeDto {

    /**
     * 查询列表总记录数
     */
    int pageTotal = 0;

    /**
     * 每页显示条数，默认10
     */
    int pageSize = 10;

}
