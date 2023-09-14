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
import com.wansensoft.utils.SnowflakeIdUtil;
import com.wansensoft.utils.constants.SecurityConstants;
import com.wansensoft.utils.redis.RedisUtil;
import com.wansensoft.vo.CaptchaVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Base64;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService{

    private final RedisUtil redisUtil;

    private final Producer producer;

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
            redisUtil.set(SecurityConstants.VERIFY_CODE_CACHE_PREFIX + captchaId, text, 180);
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
}
