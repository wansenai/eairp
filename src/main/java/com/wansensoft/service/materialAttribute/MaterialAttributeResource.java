package com.wansensoft.service.materialAttribute;

import com.wansensoft.service.ResourceInfo;

import java.lang.annotation.*;

/**
 * @author jishenghua qq752718920  2021-07-21 22:26:27
 */
@ResourceInfo(value = "materialAttribute")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaterialAttributeResource {
}
