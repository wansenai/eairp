package com.wansenai.service.user.impl;

import com.wansenai.service.user.ISysUserBusinessService;
import com.wansenai.entities.user.SysUserBusiness;
import com.wansenai.mappers.user.SysUserBusinessMapper;
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
