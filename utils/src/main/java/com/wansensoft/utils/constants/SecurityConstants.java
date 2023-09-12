package com.wansensoft.utils.constants;

public interface SecurityConstants {

    /**
     * 验证码缓存前缀
     */
    String VERIFY_CODE_CACHE_PREFIX = "AUTH:VERIFY_CODE:";

    /**
     * 用户权限集合缓存前缀
     */
    String USER_PERMS_CACHE_PREFIX = "AUTH:USER_PERMS:";

    /**
     * 黑名单Token缓存前缀
     */
    String BLACK_TOKEN_CACHE_PREFIX = "AUTH:BLACK_TOKEN:";
}
