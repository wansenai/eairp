package com.wansensoft.erp.service.user;

import com.wansensoft.erp.service.ResourceInfo;

import java.lang.annotation.*;

/**
 * @author jishenghua qq752718920  2018-10-7 15:26:27
 */
@ResourceInfo(value = "user")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserResource {
}
