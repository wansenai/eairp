package com.wansensoft.service.accountItem;

import com.wansensoft.service.ResourceInfo;

import java.lang.annotation.*;

@ResourceInfo(value = "accountItem")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountItemResource {
}
