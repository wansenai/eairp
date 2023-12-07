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
package com.wansenai.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.service.BaseService;
import com.wansenai.service.basic.IOperatorService;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.OperatorCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.dto.basic.AddOrUpdateOperatorDTO;
import com.wansenai.dto.basic.QueryOperatorDTO;
import com.wansenai.entities.basic.Operator;
import com.wansenai.mappers.OperatorMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.vo.basic.OperatorVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OperatorServiceImpl extends ServiceImpl<OperatorMapper, Operator> implements IOperatorService {

    private final OperatorMapper operatorMapper;

    private final BaseService baseService;

    public OperatorServiceImpl(OperatorMapper operatorMapper, BaseService baseService) {
        this.operatorMapper = operatorMapper;
        this.baseService = baseService;
    }

    @Override
    public Response<Page<OperatorVO>> getOperatorPageList(QueryOperatorDTO queryOperatorDTO) {
        var result = new Page<OperatorVO>();
        var operatorVOS = new ArrayList<OperatorVO>();

        Page<Operator> page = new Page<>(queryOperatorDTO.getPage(), queryOperatorDTO.getPageSize());
        var wrapper = new LambdaQueryWrapper<Operator>()
                .like(StringUtils.hasLength(queryOperatorDTO.getName()), Operator::getName, queryOperatorDTO.getName())
                .like(StringUtils.hasLength(queryOperatorDTO.getType()), Operator::getType, queryOperatorDTO.getType())
                .orderByAsc(Operator::getSort)
                .eq(Operator::getDeleteFlag, CommonConstants.NOT_DELETED);

        operatorMapper.selectPage(page, wrapper);
        page.getRecords().forEach(item -> {
            OperatorVO operatorVO = new OperatorVO();
            BeanUtils.copyProperties(item, operatorVO);
            operatorVOS.add(operatorVO);
        });

        result.setRecords(operatorVOS);
        result.setTotal(page.getTotal());
        result.setSize(page.getSize());
        result.setPages(page.getPages());

        return Response.responseData(result);
    }

    @Override
    public Response<String> addOrUpdateOperator(AddOrUpdateOperatorDTO addOrUpdateOperatorDTO) {
        var operateId = baseService.getCurrentUserId();
        if (addOrUpdateOperatorDTO.getId() == null) {
            var operator = new Operator();
            BeanUtils.copyProperties(addOrUpdateOperatorDTO, operator);
            operator.setStatus(CommonConstants.STATUS_NORMAL);
            operator.setCreateBy(operateId);
            operator.setCreateTime(LocalDateTime.now());
            var saveResult = operatorMapper.insert(operator);
            if(saveResult == 0) {
                return Response.responseMsg(OperatorCodeEnum.ADD_OPERATOR_ERROR);
            }
            return Response.responseMsg(OperatorCodeEnum.ADD_OPERATOR_SUCCESS);
        } else {
            var operator = new Operator();
            BeanUtils.copyProperties(addOrUpdateOperatorDTO, operator);
            operator.setUpdateBy(operateId);
            operator.setUpdateTime(LocalDateTime.now());
            var updateResult = operatorMapper.updateById(operator);
            if(updateResult == 0) {
                return Response.responseMsg(OperatorCodeEnum.UPDATE_OPERATOR_ERROR);
            }
            return Response.responseMsg(OperatorCodeEnum.UPDATE_OPERATOR_SUCCESS);
        }
    }

    @Override
    @Transactional
    public Response<String> deleteBatchOperator(List<Long> ids) {
        if(ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var deleteResult = removeBatchByIds(ids);
        if(!deleteResult) {
            return Response.responseMsg(OperatorCodeEnum.DELETE_OPERATOR_ERROR);
        }
        return Response.responseMsg(OperatorCodeEnum.DELETE_OPERATOR_SUCCESS);
    }

    @Override
    public Response<String> updateOperatorStatus(List<Long> ids, Integer status) {
        if(ids.isEmpty() && status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var updateStatus = lambdaUpdate()
                .in(Operator::getId, ids)
                .set(Operator::getStatus, status)
                .update();

        if(!updateStatus) {
            return Response.responseMsg(OperatorCodeEnum.UPDATE_OPERATOR_STATUS_ERROR);
        }

        return Response.responseMsg(OperatorCodeEnum.UPDATE_OPERATOR_STATUS_SUCCESS);
    }

    @Override
    public Operator getOperatorById(Long id) {
        if(id == null) {
            return null;
        }
        return getById(id);
    }

    @Override
    public Response<List<OperatorVO>> getOperatorListByType(String type) {
        var operatorVOS = new ArrayList<OperatorVO>();
        if (type.equals("所有")) {
            var operator = lambdaQuery()
                    .eq(Operator::getStatus, CommonConstants.STATUS_NORMAL)
                    .eq(Operator::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            if (!operator.isEmpty()) {
                operator.forEach(item -> {
                    OperatorVO operatorVO = new OperatorVO();
                    BeanUtils.copyProperties(item, operatorVO);
                    operatorVOS.add(operatorVO);
                });
            }
        } else {
            var operator = lambdaQuery()
                    .eq(Operator::getType, type)
                    .eq(Operator::getStatus, CommonConstants.STATUS_NORMAL)
                    .eq(Operator::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            if (!operator.isEmpty()) {
                operator.forEach(item -> {
                    OperatorVO operatorVO = new OperatorVO();
                    BeanUtils.copyProperties(item, operatorVO);
                    operatorVOS.add(operatorVO);
                });
            }
        }
        return Response.responseData(operatorVOS);
    }
}
