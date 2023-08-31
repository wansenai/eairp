package com.wansensoft.service.material;

import com.wansensoft.service.ResourceInfo;

import java.lang.annotation.*;

@ResourceInfo(value = "material")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaterialResource {
}
