package com.wansensoft.service.system.impl;

import com.wansensoft.service.system.ISysConfigService;
import com.wansensoft.entities.SysConfig;
import com.wansensoft.mappers.system.SysConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统参数 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

}
