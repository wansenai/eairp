package com.wansensoft.service.platformConfig;

import com.wansensoft.service.ResourceInfo;

import java.lang.annotation.*;

@ResourceInfo(value = "platformConfig")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlatformConfigResource {
}
