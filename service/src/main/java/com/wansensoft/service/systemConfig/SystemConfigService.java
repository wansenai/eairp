package com.wansensoft.service.systemConfig;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.system.SystemConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

public interface SystemConfigService extends IService<SystemConfig> {

    SystemConfig getSystemConfig(long id);

    List<SystemConfig> getSystemConfig();

    List<SystemConfig> select(String companyName, int offset, int rows);

    Long countSystemConfig(String companyName);

    int insertSystemConfig(JSONObject obj, HttpServletRequest request);

    int updateSystemConfig(JSONObject obj, HttpServletRequest request);

    int deleteSystemConfig(Long id, HttpServletRequest request);

    int batchDeleteSystemConfig(String ids, HttpServletRequest request);

    int batchDeleteSystemConfigByIds(String ids);

    int checkIsNameExist(Long id, String name);

    String uploadLocal(MultipartFile mf, String bizPath, String name, HttpServletRequest request);

    String uploadAliOss(MultipartFile mf, String bizPath, String name, HttpServletRequest request);

    String getFileUrlLocal(String imgPath);

    String getFileUrlAliOss(String imgPath);

    BufferedImage getImageMini(InputStream inputStream, int w);

    boolean getDepotFlag();

    boolean getCustomerFlag();

    boolean getMinusStockFlag();

    boolean getUpdateUnitPriceFlag();

    boolean getOverLinkBillFlag();

    boolean getForceApprovalFlag();

    boolean getMultiLevelApprovalFlag();
}
