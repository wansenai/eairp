package com.wansensoft.service.user.impl;

import com.wansensoft.entities.SysUser;
import com.wansensoft.mappers.user.SysUserMapper;
import com.wansensoft.service.user.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
