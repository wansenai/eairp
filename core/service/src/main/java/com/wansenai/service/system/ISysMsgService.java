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
package com.wansenai.service.system;

import com.wansenai.dto.system.SystemMessageDTO;
import com.wansenai.dto.system.UpdateSystemMessageDTO;
import com.wansenai.entities.system.SysMsg;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.SystemMessageVO;

import java.util.List;

/**
 * <p>
 * 消息表 服务类
 * </p>
 */
public interface ISysMsgService extends IService<SysMsg> {

    void insertMessage(SystemMessageDTO systemMessageDTO);

    void insertBatchMessage(List<SystemMessageDTO> systemMessageDTOList);

    void deleteBatchMessage(List<Long> ids);

    Response<List<SystemMessageVO>> getMessagePageList();

    Response<String> updateMessageStatus(UpdateSystemMessageDTO updateSystemMessageDTO);
}
