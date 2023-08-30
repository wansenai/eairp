package com.wansensoft.erp.service.sequence;

import com.wansensoft.erp.service.ResourceInfo;

import java.lang.annotation.*;

/**
 * Description
 *
 * @Author: jishenghua
 * @Date: 2021/3/16 16:33
 */
@ResourceInfo(value = "sequence")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SequenceResource {
}
