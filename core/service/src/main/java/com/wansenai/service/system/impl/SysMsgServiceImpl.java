/*
 * Copyright 2024-2033 WanSen AI Team, Inc. All Rights Reserved.
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
package com.wansenai.service.system.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wansenai.dto.system.SystemMessageDTO;
import com.wansenai.dto.system.UpdateSystemMessageDTO;
import com.wansenai.service.system.ISysMsgService;
import com.wansenai.entities.system.SysMsg;
import com.wansenai.mappers.system.SysMsgMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.constants.MessageConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.redis.RedisUtil;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.SystemMessageItemVO;
import com.wansenai.vo.SystemMessageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2024-07-11
 */
@Service
public class SysMsgServiceImpl extends ServiceImpl<SysMsgMapper, SysMsg> implements ISysMsgService {

    private final RedisUtil redisUtil;

    private final ISysUserService userService;

    private final SysMsgMapper messageMapper;

    public SysMsgServiceImpl(RedisUtil redisUtil, ISysUserService userService, SysMsgMapper messageMapper) {
        this.redisUtil = redisUtil;
        this.userService = userService;
        this.messageMapper = messageMapper;
    }

    @Override
    public void insertMessage(SystemMessageDTO systemMessageDTO) {
        if(systemMessageDTO == null) {
            return;
        }
        SysMsg sysMsg = new SysMsg();
        sysMsg.setId(SnowflakeIdUtil.nextId());
        sysMsg.setUserId(systemMessageDTO.getUserId());
        sysMsg.setMsgTitle(systemMessageDTO.getMsgTitle());
        sysMsg.setMsgContent(systemMessageDTO.getMsgContent());
        sysMsg.setDescription(systemMessageDTO.getDescription());
        sysMsg.setType(systemMessageDTO.getType());
        sysMsg.setStatus(MessageConstants.SYSTEM_MESSAGE_UNREAD);
        sysMsg.setCreateTime(LocalDateTime.now());
        var msg = JSONObject.toJSONString(sysMsg);
        redisUtil.lSet(MessageConstants.SYSTEM_MESSAGE_PREFIX + sysMsg.getUserId(), msg, MessageConstants.SYSTEM_EXPIRE_DATE);
        save(sysMsg);
    }

    @Override
    @Transactional
    public void insertBatchMessage(List<SystemMessageDTO> systemMessageDTOList) {
        if (systemMessageDTOList.isEmpty()) {
            return;
        }
        List<SysMsg> msgList = new ArrayList<>();
        systemMessageDTOList.forEach(item -> {
            SysMsg sysMsg = new SysMsg();
            sysMsg.setId(SnowflakeIdUtil.nextId());
            sysMsg.setUserId(item.getUserId());
            sysMsg.setMsgTitle(item.getMsgTitle());
            sysMsg.setMsgContent(item.getMsgContent());
            sysMsg.setDescription(item.getDescription());
            sysMsg.setType(item.getType());
            sysMsg.setStatus(MessageConstants.SYSTEM_MESSAGE_UNREAD);
            sysMsg.setCreateTime(LocalDateTime.now());
            var msg = JSONObject.toJSONString(sysMsg);
            redisUtil.lSet(MessageConstants.SYSTEM_MESSAGE_PREFIX + sysMsg.getUserId(), msg, MessageConstants.SYSTEM_EXPIRE_DATE);
            msgList.add(sysMsg);

        });
        saveBatch(msgList);
    }

    @Override
    @Transactional
    public void deleteBatchMessage(List<Long> ids) {
        if (ids.isEmpty()) {
            return;
        }
        removeByIds(ids);
    }

    @Override
    public Response<List<SystemMessageVO>> getMessagePageList() {
        var result = new ArrayList<SystemMessageVO>();

        var todoList = new ArrayList<SystemMessageItemVO>();
        var noticeList = new ArrayList<SystemMessageItemVO>();
        var userId = userService.getCurrentUserId();

        List<Object> data;
        var redisData = redisUtil.lGet(MessageConstants.SYSTEM_MESSAGE_PREFIX + userId, 0, -1);
        if(!redisData.isEmpty()) {
            data = new ArrayList<>(redisData);
        } else {
            var queryWrapper = new QueryWrapper<SysMsg>();
            queryWrapper.eq("user_id", userId);
            queryWrapper.eq("status", 0);
            data = new ArrayList<>(messageMapper.selectList(queryWrapper));
        }

        var messageVo = new SystemMessageVO();
        data.forEach(item -> {
            SysMsg msg;
            if (item instanceof SysMsg) {
                msg = (SysMsg) item;
            } else {
                msg = JSONObject.parseObject(item.toString(), SysMsg.class);
            }
            if(Objects.nonNull(msg)) {
                var vo = new SystemMessageItemVO();
                vo.setId(msg.getId());
                vo.setTitle(msg.getMsgTitle());
                vo.setMsgContent(msg.getMsgContent());
                vo.setDescription(msg.getDescription());
                vo.setType(msg.getType());
                vo.setStatus(msg.getStatus());
                vo.setDatetime(msg.getCreateTime());
                if ("todo".equals(msg.getType())) {
                    vo.setAvatar("https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png");
                    todoList.add(vo);
                } else if ("notice".equals(msg.getType())) {
                    vo.setAvatar("https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png");
                    noticeList.add(vo);
                }
            }
        });
        // 对todoList和noticeList进行时间排序
        todoList.sort((a, b) -> b.getDatetime().compareTo(a.getDatetime()));
        noticeList.sort((a, b) -> b.getDatetime().compareTo(a.getDatetime()));

        var systemLanguage = userService.getUserSystemLanguage(userId);
        String nameOne = "", nameTwo = "", nameThree = "";
        if ("zh_CN".equals(systemLanguage)) {
            nameOne = "通知";
            nameTwo = "消息";
            nameThree = "待办";
        } else if("en_US".equals(systemLanguage)) {
            nameOne = "Notification";
            nameTwo = "Message";
            nameThree = "To do";
        }

        messageVo.setName(nameOne);
        messageVo.setKey("1");
        messageVo.setList(noticeList);
        result.add(messageVo);

//        var messageVo2 = new SystemMessageVO();
//        messageVo2.setName(nameTwo);
//        messageVo2.setKey("2");
//        messageVo2.setList(new ArrayList<>());
//        result.add(messageVo2);

        var messageVo3 = new SystemMessageVO();
        messageVo3.setName(nameThree);
        messageVo3.setKey("3");
        messageVo3.setList(todoList);
        result.add(messageVo3);

        return Response.responseData(result);
    }

    @Override
    public Response<String> updateMessageStatus(UpdateSystemMessageDTO updateSystemMessageDTO) {
        if (updateSystemMessageDTO.getId() == null || updateSystemMessageDTO.getStatus() == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateObject = new SysMsg();
        updateObject.setId(updateSystemMessageDTO.getId());
        updateObject.setStatus(MessageConstants.SYSTEM_MESSAGE_READ);
        updateById(updateObject);

        // remove redis message data
        var dataList = redisUtil.lGet(MessageConstants.SYSTEM_MESSAGE_PREFIX + updateSystemMessageDTO.getUserId(), 0, -1);
        dataList.forEach(item -> {
            var msg = JSONObject.parseObject(item.toString(), SysMsg.class);
            if(Objects.nonNull(msg) && msg.getId().equals(updateSystemMessageDTO.getId())) {
                redisUtil.lRemove(MessageConstants.SYSTEM_MESSAGE_PREFIX + updateSystemMessageDTO.getUserId(), 1, item);
            }
        });
        return Response.success();
    }
}
