/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wansenai.service.common;

import com.wansenai.utils.response.Response;
import com.wansenai.vo.CaptchaVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface CommonService {
    CaptchaVO getCaptcha();
    Boolean sendSmsCode(Integer type, String phoneNumber);

    Response<String> uploadExclsData(MultipartFile file);

    File exportExcel(String type);

    Response<List<String>> uploadOss(List<MultipartFile> files);

    Response<String> generateSnowflakeId(String type);
}
