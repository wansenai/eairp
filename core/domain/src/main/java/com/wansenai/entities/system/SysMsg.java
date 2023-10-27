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
package com.wansenai.entities.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_msg")
public class SysMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 消息标题
     */
    private String msgTitle;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 接收人id
     */
    private Long userId;

    /**
     * 状态，1未读 2已读
     */
    private Boolean status;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 删除标记，0未删除，1删除
     */
    private Boolean deleteFlag;


}
