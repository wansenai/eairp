/*
 * Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://opensource.wansenai.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.wansensoft.service.common;

import com.google.code.kaptcha.Producer;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.wansensoft.bo.SmsInfoBO;
import com.wansensoft.entities.basic.Supplier;
import com.wansensoft.entities.system.SysPlatformConfig;
import com.wansensoft.service.basic.SupplierService;
import com.wansensoft.service.system.ISysPlatformConfigService;
import com.wansensoft.utils.SnowflakeIdUtil;
import com.wansensoft.utils.constants.SecurityConstants;
import com.wansensoft.utils.constants.SmsConstants;
import com.wansensoft.utils.enums.BaseCodeEnum;
import com.wansensoft.utils.enums.SupplierCodeEnum;
import com.wansensoft.utils.redis.RedisUtil;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.CaptchaVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService{

    private final RedisUtil redisUtil;

    private final Producer producer;

    private final SupplierService supplierService;

    private final ISysPlatformConfigService platformConfigService;

    public CommonServiceImpl(RedisUtil redisUtil, Producer producer, SupplierService supplierService, ISysPlatformConfigService platformConfigService) {
        this.redisUtil = redisUtil;
        this.producer = producer;
        this.supplierService = supplierService;
        this.platformConfigService = platformConfigService;
    }

    private SmsInfoBO getSmsInfo() {
        var platform = platformConfigService.list().stream().filter(item -> item.getPlatformKey().startsWith("tencent_sms")).toList();
        var smsInfoMap = platform.stream().collect(Collectors.toMap(SysPlatformConfig::getPlatformKey, SysPlatformConfig::getPlatformValue));
        return SmsInfoBO.builder()
                .secretId(smsInfoMap.get("tencent_sms_secret_id"))
                .secretKey(smsInfoMap.get("tencent_sms_secret_key"))
                .smsClint(smsInfoMap.get("tencent_sms_client"))
                .sdkAppId(smsInfoMap.get("tencent_sms_sdk_appId"))
                .build();
    }

    @Override
    public CaptchaVO getCaptcha() {
        String captchaId = "CAPTCHA" + SnowflakeIdUtil.nextId();
        String text = producer.createText();
        String imgEncode = "";
        BufferedImage bi = producer.createImage(text);
        try (FastByteArrayOutputStream fos = new FastByteArrayOutputStream()) {
            ImageIO.write(bi, "jpg", fos);
            imgEncode = Base64.getEncoder().encodeToString(fos.toByteArray());
            redisUtil.set(SecurityConstants.EMAIL_VERIFY_CODE_CACHE_PREFIX + captchaId, text, 180);
            fos.flush();
        } catch (Exception e) {
            log.error("获取验证码失败: " + e.getMessage());
            return null;
        }
        return CaptchaVO.builder()
                .captchaId(captchaId)
                .imagePath("data:image/jpeg;base64," + imgEncode)
                .build();
    }

    @Override
    public Boolean sendSmsCode(Integer type, String phoneNumber) {
        if(!StringUtils.hasText(phoneNumber) && type == null) {
            return false;
        }
        var regex = "^(0|86|17951)?(13[0-9]|15[012356789]|16[6]|19[89]]|17[01345678]|18[0-9]|14[579])[0-9]{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        if(!matcher.matches()) {
            return false;
        }
        var templateId = switch (type) {
            case 0 -> SmsConstants.SMS_TEMPLATE_ID_REGISTER_USER;
            case 1 -> SmsConstants.SMS_TEMPLATE_ID_PHONE_LOGIN;
            case 2 -> SmsConstants.SMS_TEMPLATE_ID_UPDATE_PASSWORD;
            default -> "";
        };

        var key = switch (type) {
            case 0 -> SecurityConstants.REGISTER_VERIFY_CODE_CACHE_PREFIX;
            case 1 -> SecurityConstants.LOGIN_VERIFY_CODE_CACHE_PREFIX;
            case 2 -> SecurityConstants.UPDATE_PASSWORD_VERIFY_CODE_CACHE_PREFIX;
            default -> "";
        };

        try {
            SmsInfoBO smsInfo = getSmsInfo();
            Credential cred = new Credential(smsInfo.getSecretId(), smsInfo.getSecretKey());
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, smsInfo.getSmsClint(),clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppid(smsInfo.getSdkAppId());
            req.setSign(SmsConstants.SMS_SIGN_NAME);
            req.setSessionContext(phoneNumber);
            req.setTemplateID(templateId);
            req.setPhoneNumberSet(new String[]{"86"+phoneNumber});

            Random random = new Random();
            var code = String.valueOf(random.nextInt(900000) + 100000);
            redisUtil.set(key + phoneNumber, code, 120);
            req.setTemplateParamSet(new String[]{code});

            client.SendSms(req);

            return true;
        }catch (Exception e) {
            log.error(String.format("用户手机号:%s, 验证码发送失败，错误消息:%s", phoneNumber, e.getMessage()));
            return false;
        }
    }

    @Override
    public Response<String> uploadExclsData(MultipartFile file) {
        if(!file.isEmpty()) {
            try {
                String filename = file.getOriginalFilename();
                 if(filename == null || filename.isEmpty()) {
                    return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_NO_FILENAME_MATCH);

                 } else if (filename.contains("供应商模板.xlsx")) {
                    var result = readSuppliersFromExcel(file);
                    if(!result){
                        return Response.responseMsg(SupplierCodeEnum.ADD_SUPPLIER_ERROR);
                    }
                     return Response.responseMsg(SupplierCodeEnum.ADD_SUPPLIER_SUCCESS);

                } else if (filename.contains("222.xlsx")) {
                    System.out.println("其他文件");

                } else {
                    log.error("上传Excel文件失败: 文件名不匹配");
                    return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_NO_FILENAME_MATCH);
                }
            } catch (Exception e) {
                log.error("上传Excel文件失败: " + e.getMessage());
                return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_ERROR);
            }
        }
        return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_ERROR);
    }

    private boolean readSuppliersFromExcel(MultipartFile file) throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        Workbook workbook = new HSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        for (int i = 2; i <= sheet.getLastRowNum(); ++i) {
            Row row = sheet.getRow(i);
            var supplier = Supplier.builder()
                    .supplierName(getCellValue(row.getCell(0), dataFormatter))
                    .contact(getCellValue(row.getCell(1), dataFormatter))
                    .phoneNumber(getCellValue(row.getCell(2), dataFormatter))
                    .contactNumber(getCellValue(row.getCell(3), dataFormatter))
                    .email(getCellValue(row.getCell(4), dataFormatter))
                    .fax(getCellValue(row.getCell(5), dataFormatter))
                    .firstQuarterAccountReceivable(getNumericCellValue(row.getCell(6)))
                    .secondQuarterAccountReceivable(getNumericCellValue(row.getCell(7)))
                    .thirdQuarterAccountReceivable(getNumericCellValue(row.getCell(8)))
                    .fourthQuarterAccountReceivable(getNumericCellValue(row.getCell(9)))
                    .firstQuarterAccountPayment(getNumericCellValue(row.getCell(10)))
                    .secondQuarterAccountPayment(getNumericCellValue(row.getCell(11)))
                    .thirdQuarterAccountPayment(getNumericCellValue(row.getCell(12)))
                    .fourthQuarterAccountPayment(getNumericCellValue(row.getCell(13)))
                    .taxNumber(getCellValue(row.getCell(14), dataFormatter))
                    .taxRate(getNumericCellValue(row.getCell(15)))
                    .bankName(getCellValue(row.getCell(16), dataFormatter))
                    .accountNumber(getLongCellValue(row.getCell(17)))
                    .address(getCellValue(row.getCell(18), dataFormatter))
                    .remark(getCellValue(row.getCell(19), dataFormatter))
                    .build();
            suppliers.add(supplier);
            workbook.close();
        }
        return supplierService.batchAddSupplier(suppliers);
    }

    private String getCellValue(Cell cell, DataFormatter dataFormatter) {
        if (cell != null) {
            String value = dataFormatter.formatCellValue(cell);
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        return null;
    }

    private BigDecimal getNumericCellValue(Cell cell) {
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }
        return null;
    }

    private Long getLongCellValue(Cell cell) {
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            return (long) cell.getNumericCellValue();
        }
        return null;
    }
}
