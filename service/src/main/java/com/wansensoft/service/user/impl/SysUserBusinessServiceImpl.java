package com.wansensoft.service.user.impl;

import com.wansensoft.entities.user.SysUserBusiness;
import com.wansensoft.mappers.user.SysUserBusinessMapper;
import com.wansensoft.service.user.ISysUserBusinessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户/角色/模块关系表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class SysUserBusinessServiceImpl extends ServiceImpl<SysUserBusinessMapper, SysUserBusiness> implements ISysUserBusinessService {

}
