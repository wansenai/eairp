package com.wansensoft.service.systemConfig;

import com.wansensoft.service.ResourceInfo;

import java.lang.annotation.*;

@ResourceInfo(value = "systemConfig")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemConfigResource {
}
