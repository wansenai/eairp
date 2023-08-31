package com.wansensoft.service.account;

import com.wansensoft.service.ResourceInfo;

import java.lang.annotation.*;

@ResourceInfo(value = "account")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountResource {
}
