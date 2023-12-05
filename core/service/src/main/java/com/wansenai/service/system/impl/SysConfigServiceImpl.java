package com.wansenai.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wansenai.dto.system.SystemConfigDTO;
import com.wansenai.service.system.SysConfigService;
import com.wansenai.entities.system.SysConfig;
import com.wansenai.mappers.system.SysConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.SystemConfigVO;
import org.springframework.beans.BeanUtils;
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
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    private final SysConfigMapper configMapper;

    public SysConfigServiceImpl(SysConfigMapper configMapper) {
        this.configMapper = configMapper;
    }

    @Override
    public Response<SystemConfigVO> getSystemConfigInfo() {
        var wrapper = new QueryWrapper<SysConfig>();
        wrapper.eq("delete_flag", CommonConstants.NOT_DELETED);
        var configData = configMapper.selectOne(wrapper);
        var systemConfigVO = new SystemConfigVO();
        if (configData != null) {
            BeanUtils.copyProperties(configData, systemConfigVO);
            return Response.responseData(systemConfigVO);
        }
        return Response.responseData(systemConfigVO);
    }

    @Override
    public Response<String> addOrUpdateCompanyInfo(SystemConfigDTO systemConfigDTO) {
        var id = systemConfigDTO.getId();
        var config = new SysConfig();
        BeanUtils.copyProperties(systemConfigDTO, config);
        if (id == null) {
            configMapper.insert(config);
        } else {
            configMapper.updateById(config);
        }
        return Response.responseMsg(BaseCodeEnum.SUCCESS);
    }
}
