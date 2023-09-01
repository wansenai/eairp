package com.wansensoft.service.systemConfig;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.system.SystemConfig;
import com.wansensoft.entities.system.SystemConfigExample;
import com.wansensoft.entities.user.User;
import com.wansensoft.mappers.system.SystemConfigMapper;
import com.wansensoft.mappers.system.SystemConfigMapperEx;
import com.wansensoft.service.CommonService;
import com.wansensoft.service.log.LogService;
import com.wansensoft.service.platformConfig.PlatformConfigService;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.utils.FileUtils;
import com.wansensoft.utils.StringUtil;
import com.wansensoft.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService{
    private Logger logger = LoggerFactory.getLogger(SystemConfigServiceImpl.class);

    private final SystemConfigMapper systemConfigMapper;
    private final SystemConfigMapperEx systemConfigMapperEx;
    private final PlatformConfigService platformConfigService;
    private final CommonService commonService;
    private final LogService logService;

    @Value(value="${file.path}")
    private String filePath;

    public SystemConfigServiceImpl(SystemConfigMapper systemConfigMapper, SystemConfigMapperEx systemConfigMapperEx, PlatformConfigService platformConfigService, CommonService commonService, LogService logService) {
        this.systemConfigMapper = systemConfigMapper;
        this.systemConfigMapperEx = systemConfigMapperEx;
        this.platformConfigService = platformConfigService;
        this.commonService = commonService;
        this.logService = logService;
    }

    public SystemConfig getSystemConfig(long id) {
        SystemConfig result=null;
        try{
            result=systemConfigMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<SystemConfig> getSystemConfig() {
        SystemConfigExample example = new SystemConfigExample();
        example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<SystemConfig> list=null;
        try{
            list=systemConfigMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }
    public List<SystemConfig> select(String companyName, int offset, int rows) {
        List<SystemConfig> list=null;
        try{
            list=systemConfigMapperEx.selectByConditionSystemConfig(companyName, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countSystemConfig(String companyName) {
        Long result=null;
        try{
            result=systemConfigMapperEx.countsBySystemConfig(companyName);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertSystemConfig(JSONObject obj, HttpServletRequest request) {
        SystemConfig systemConfig = JSONObject.parseObject(obj.toJSONString(), SystemConfig.class);
        int result=0;
        try{
            result=systemConfigMapper.insertSelective(systemConfig);
            logService.insertLog("系统配置",
                    BusinessConstants.LOG_OPERATION_TYPE_ADD + systemConfig.getCompanyName(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateSystemConfig(JSONObject obj, HttpServletRequest request) {
        SystemConfig systemConfig = JSONObject.parseObject(obj.toJSONString(), SystemConfig.class);
        int result=0;
        try{
            result = systemConfigMapper.updateByPrimaryKeySelective(systemConfig);
            logService.insertLog("系统配置",
                    BusinessConstants.LOG_OPERATION_TYPE_EDIT + systemConfig.getCompanyName(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteSystemConfig(Long id, HttpServletRequest request) {
        return batchDeleteSystemConfigByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteSystemConfig(String ids, HttpServletRequest request) {
        return batchDeleteSystemConfigByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteSystemConfigByIds(String ids) {
        logService.insertLog("系统配置",
                BusinessConstants.LOG_OPERATION_TYPE_DELETE + ids,
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        User userInfo = commonService.getCurrentUser();
        String [] idArray=ids.split(",");
        int result=0;
        try{
            result = systemConfigMapperEx.batchDeleteSystemConfigByIds(new Date(), userInfo == null ? null : userInfo.getId(), idArray);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name) {
        SystemConfigExample example = new SystemConfigExample();
        example.createCriteria().andIdNotEqualTo(id).andCompanyNameEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<SystemConfig> list =null;
        try{
            list=systemConfigMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    /**
     * 本地文件上传
     * @param mf 文件
     * @param bizPath  自定义路径
     * @param name  自定义文件名
     * @return
     */
    public String uploadLocal(MultipartFile mf, String bizPath, String name, HttpServletRequest request) {
        try {
            if(StringUtil.isEmpty(bizPath)){
                bizPath = "";
            }
            String token = request.getHeader("X-Access-Token");
            Long tenantId = Tools.getTenantIdByToken(token);
            bizPath = bizPath + File.separator + tenantId;
            String ctxPath = filePath;
            String fileName = null;
            File file = new File(ctxPath + File.separator + bizPath + File.separator );
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            String orgName = mf.getOriginalFilename();// 获取文件名
            orgName = FileUtils.getFileName(orgName);
            if(orgName.contains(".")){
                if(StringUtil.isNotEmpty(name)) {
                    fileName = name.substring(0, name.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
                } else {
                    fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
                }
            }else{
                fileName = orgName+ "_" + System.currentTimeMillis();
            }
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            // 保存缩略图
            // String fileUrl = getFileUrlLocal(bizPath + File.separator + fileName);
            // InputStream imgInputStream = new BufferedInputStream(new FileInputStream(fileUrl));
            // BufferedImage smallImage = getImageMini(imgInputStream, 80);
            // int index = fileName.lastIndexOf(".");
            // String ext = fileName.substring(index + 1);
            // String smallUrl = filePath + "-small" + File.separator + bizPath + File.separator + fileName;
            // FileUtils.createFile(smallUrl);
            // File saveSmallFile = new File(smallUrl);
            // ImageIO.write(smallImage, ext, saveSmallFile);
            // 返回路径
            String dbpath = null;
            if(StringUtil.isNotEmpty(bizPath)){
                dbpath = bizPath + File.separator + fileName;
            }else{
                dbpath = fileName;
            }
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            return dbpath;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 阿里Oss文件上传
     * @param mf 文件
     * @param bizPath  自定义路径
     * @param name  自定义文件名
     * @return
     */
    public String uploadAliOss(MultipartFile mf, String bizPath, String name, HttpServletRequest request) {
        if (StringUtil.isEmpty(bizPath)) {
            bizPath = "";
        }
        String token = request.getHeader("X-Access-Token");
        Long tenantId = Tools.getTenantIdByToken(token);
        bizPath = bizPath + "/" + tenantId;
        String endpoint = platformConfigService.getPlatformConfigByKey("aliOss_endpoint").getPlatformValue();
        String accessKeyId = platformConfigService.getPlatformConfigByKey("aliOss_accessKeyId").getPlatformValue();
        String accessKeySecret = platformConfigService.getPlatformConfigByKey("aliOss_accessKeySecret").getPlatformValue();
        String bucketName = platformConfigService.getPlatformConfigByKey("aliOss_bucketName").getPlatformValue();
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String fileName = "";
        String orgName = mf.getOriginalFilename();// 获取文件名
        orgName = FileUtils.getFileName(orgName);
        if (orgName.contains(".")) {
            if (StringUtil.isNotEmpty(name)) {
                fileName = name.substring(0, name.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            } else {
                fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            }
        } else {
            fileName = orgName + "_" + System.currentTimeMillis();
        }
        String filePathStr = StringUtil.isNotEmpty(filePath) ? filePath.substring(1) : "";
        String objectName = filePathStr + "/" + bizPath + "/" + fileName;
        String smallObjectName = filePathStr + "-small/" + bizPath + "/" + fileName;
        // 创建OSSClient实例。
        OSS ossClient = null;
        try {
            // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
            byte[] byteArr = mf.getBytes();
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 保存原文件
            InputStream inputStream = new ByteArrayInputStream(byteArr);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            ossClient.putObject(putObjectRequest);
            // 如果是图片-保存缩略图
            int index = fileName.lastIndexOf(".");
            String ext = fileName.substring(index + 1);
            if (ext.contains("gif") || ext.contains("jpg") || ext.contains("jpeg") || ext.contains("png")
                    || ext.contains("GIF") || ext.contains("JPG") || ext.contains("JPEG") || ext.contains("PNG")) {
                String fileUrl = getFileUrlAliOss(bizPath + "/" + fileName);
                URL url = new URL(fileUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5 * 1000);
                InputStream imgInputStream = conn.getInputStream();// 通过输入流获取图片数据
                BufferedImage smallImage = getImageMini(imgInputStream, 80);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
                ImageIO.write(smallImage, ext, imOut);
                InputStream isImg = new ByteArrayInputStream(bs.toByteArray());
                PutObjectRequest putSmallObjectRequest = new PutObjectRequest(bucketName, smallObjectName, isImg);
                ossClient.putObject(putSmallObjectRequest);
            }
            // 返回路径
            return bizPath + "/" + fileName;
        } catch (OSSException oe) {
            logger.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            logger.error("Error Message:" + oe.getErrorMessage());
            logger.error("Error Code:" + oe.getErrorCode());
            logger.error("Request ID:" + oe.getRequestId());
            logger.error("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            logger.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (IOException ioe) {
            logger.error("Caught an IOException while uploading file to OSS.");
            System.out.println("Error Message:" + ioe.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return "";
    }

    public String getFileUrlLocal(String imgPath) {
        return filePath + File.separator + imgPath;
    }

    public String getFileUrlAliOss(String imgPath) {
        String linkUrl = platformConfigService.getPlatformConfigByKey("aliOss_linkUrl").getPlatformValue();
        return linkUrl + filePath + "/" + imgPath;
    }

    public BufferedImage getImageMini(InputStream inputStream, int w) {
        try {
            BufferedImage img = ImageIO.read(inputStream);
            //获取图片的长和宽
            int width = img.getWidth();
            int height = img.getHeight();
            int tempw = 0;
            int temph = 0;
            if (width > height) {
                tempw = w;
                temph = height * w / width;
            } else {
                tempw = w * width / height;
                temph = w;
            }
            Image _img = img.getScaledInstance(tempw, temph, Image.SCALE_DEFAULT);
            BufferedImage image = new BufferedImage(tempw, temph, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.drawImage(_img, 0, 0, null);
            graphics.dispose();
            return image;
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取仓库开关
     * @return
     * @throws Exception
     */
    public boolean getDepotFlag() {
        boolean depotFlag = false;
        List<SystemConfig> list = getSystemConfig();
        if(!list.isEmpty()) {
            String flag = list.get(0).getDepotFlag();
            if(("1").equals(flag)) {
                depotFlag = true;
            }
        }
        return depotFlag;
    }

    /**
     * 获取客户开关
     * @return
     * @throws Exception
     */
    public boolean getCustomerFlag() {
        boolean customerFlag = false;
        List<SystemConfig> list = getSystemConfig();
        if(!list.isEmpty()) {
            String flag = list.get(0).getCustomerFlag();
            if(("1").equals(flag)) {
                customerFlag = true;
            }
        }
        return customerFlag;
    }

    /**
     * 获取负库存开关
     * @return
     * @throws Exception
     */
    public boolean getMinusStockFlag() {
        boolean minusStockFlag = false;
        List<SystemConfig> list = getSystemConfig();
        if(!list.isEmpty()) {
            String flag = list.get(0).getMinusStockFlag();
            if(("1").equals(flag)) {
                minusStockFlag = true;
            }
        }
        return minusStockFlag;
    }

    /**
     * 获取更新单价开关
     * @return
     * @throws Exception
     */
    public boolean getUpdateUnitPriceFlag() {
        boolean updateUnitPriceFlag = true;
        List<SystemConfig> list = getSystemConfig();
        if(!list.isEmpty()) {
            String flag = list.get(0).getUpdateUnitPriceFlag();
            if(("0").equals(flag)) {
                updateUnitPriceFlag = false;
            }
        }
        return updateUnitPriceFlag;
    }

    /**
     * 获取超出关联单据开关
     * @return
     * @throws Exception
     */
    public boolean getOverLinkBillFlag() {
        boolean overLinkBillFlag = false;
        List<SystemConfig> list = getSystemConfig();
        if(!list.isEmpty()) {
            String flag = list.get(0).getOverLinkBillFlag();
            if(("1").equals(flag)) {
                overLinkBillFlag = true;
            }
        }
        return overLinkBillFlag;
    }

    /**
     * 获取强审核开关
     * @return
     * @throws Exception
     */
    public boolean getForceApprovalFlag() {
        boolean forceApprovalFlag = false;
        List<SystemConfig> list = getSystemConfig();
        if(list.size()>0) {
            String flag = list.get(0).getForceApprovalFlag();
            if(("1").equals(flag)) {
                forceApprovalFlag = true;
            }
        }
        return forceApprovalFlag;
    }

    /**
     * 获取多级审核开关
     * @return
     * @throws Exception
     */
    public boolean getMultiLevelApprovalFlag() {
        boolean multiLevelApprovalFlag = false;
        List<SystemConfig> list = getSystemConfig();
        if(list.size()>0) {
            String flag = list.get(0).getMultiLevelApprovalFlag();
            if(("1").equals(flag)) {
                multiLevelApprovalFlag = true;
            }
        }
        return multiLevelApprovalFlag;
    }
}