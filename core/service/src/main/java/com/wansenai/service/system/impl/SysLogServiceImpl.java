package com.wansenai.service.system.impl;

import com.wansenai.service.system.ISysLogService;
import com.wansenai.entities.system.SysLog;
import com.wansenai.mappers.system.SysLogMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

}
