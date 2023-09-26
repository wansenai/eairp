package com.wansensoft.service.orgaUserRel;

import com.wansensoft.service.ResourceInfo;

import java.lang.annotation.*;

/**
 *  机构用户关系
 */
@ResourceInfo(value = "orgaUserRel")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrgaUserRelResource {

}
