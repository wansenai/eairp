package com.wansensoft.service.organization;

import com.wansensoft.service.ResourceInfo;

import java.lang.annotation.*;

/**
 *  机构
 */
@ResourceInfo(value = "organization")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrganizationResource {
}
