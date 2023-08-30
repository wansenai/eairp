package com.wansensoft.erp.service.materialExtend;

import com.wansensoft.erp.service.ResourceInfo;

import java.lang.annotation.*;

/**
 * @author jishenghua qq752718920  2018-10-7 15:26:27
 */
@ResourceInfo(value = "materialExtend")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaterialExtendResource {
}
