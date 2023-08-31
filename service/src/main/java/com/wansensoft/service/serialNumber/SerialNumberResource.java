package com.wansensoft.service.serialNumber;

import com.wansensoft.service.ResourceInfo;

import java.lang.annotation.*;

/**
 * Description
 */
@ResourceInfo(value = "serialNumber")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SerialNumberResource {
}
