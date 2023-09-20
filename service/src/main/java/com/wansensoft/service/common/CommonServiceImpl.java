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
import com.wansensoft.utils.SnowflakeIdUtil;
import com.wansensoft.utils.constants.SecurityConstants;
import com.wansensoft.utils.constants.SmsConstants;
import com.wansensoft.utils.redis.RedisUtil;
import com.wansensoft.vo.CaptchaVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService{

    private final RedisUtil redisUtil;

    private final Producer producer;

    @Value("${sms.secretId}")
    private String secretId;

    @Value("${sms.secretKey}")
    private String secretKey;

    @Value("${sms.smsClint}")
    private String smsClint;

    @Value("${sms.sdkAppId}")
    private String sdkAppId;

    public CommonServiceImpl(RedisUtil redisUtil, Producer producer) {
        this.redisUtil = redisUtil;
        this.producer = producer;
    }

    @Override
    public CaptchaVo getCaptcha() {
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
        return CaptchaVo.builder()
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
        var templateId = "";
        var key = "";
        if (type == 0) {
            templateId = SmsConstants.SMS_TEMPLATE_ID_REGISTER_USER;
            key = SecurityConstants.REGISTER_VERIFY_CODE_CACHE_PREFIX;
        }else if (type == 1) {
            templateId = SmsConstants.SMS_TEMPLATE_ID_PHONE_LOGIN;
            key = SecurityConstants.LOGIN_VERIFY_CODE_CACHE_PREFIX;
        }else if (type == 2) {
            templateId = SmsConstants.SMS_TEMPLATE_ID_UPDATE_PASSWORD;
            key = SecurityConstants.UPDATE_PASSWORD_VERIFY_CODE_CACHE_PREFIX;
        }else {
            return false;
        }

        try {
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, smsClint,clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppid(sdkAppId);
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
}
