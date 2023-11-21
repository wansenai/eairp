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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.basic.AddOrUpdateIncomeExpenseDTO;
import com.wansenai.dto.basic.QueryIncomeExpenseDTO;
import com.wansenai.entities.IncomeExpense;
import com.wansenai.mappers.IncomeExpenseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.service.basic.IncomeExpenseService;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.IncomeExpenseCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.basic.IncomeExpenseVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class IncomeExpenseServiceImpl extends ServiceImpl<IncomeExpenseMapper, IncomeExpense> implements IncomeExpenseService {

    private final IncomeExpenseMapper incomeExpenseMapper;

    private final ISysUserService userService;

    public IncomeExpenseServiceImpl(IncomeExpenseMapper incomeExpenseMapper, ISysUserService userService) {
        this.incomeExpenseMapper = incomeExpenseMapper;
        this.userService = userService;
    }

    @Override
    public Response<Page<IncomeExpenseVO>> getIncomeExpensePageList(QueryIncomeExpenseDTO queryIncomeExpenseDTO) {
        var result = new Page<IncomeExpenseVO>();

        var page = new Page<IncomeExpense>(queryIncomeExpenseDTO.getPage(), queryIncomeExpenseDTO.getPageSize());
        var incomeExpensePage = lambdaQuery()
                .eq(StringUtils.hasLength(queryIncomeExpenseDTO.getType()), IncomeExpense::getType, queryIncomeExpenseDTO.getType())
                .like(StringUtils.hasLength(queryIncomeExpenseDTO.getName()), IncomeExpense::getName, queryIncomeExpenseDTO.getName())
                .like(StringUtils.hasLength(queryIncomeExpenseDTO.getRemark()), IncomeExpense::getRemark, queryIncomeExpenseDTO.getRemark())
                .ge(queryIncomeExpenseDTO.getStartDate() != null, IncomeExpense::getCreateTime, queryIncomeExpenseDTO.getStartDate())
                .le(queryIncomeExpenseDTO.getEndDate() != null, IncomeExpense::getCreateTime, queryIncomeExpenseDTO.getEndDate())
                .page(page);

        var incomeExpenses = new ArrayList<IncomeExpenseVO>(page.getRecords().size() + 1);
        incomeExpensePage.getRecords().forEach(item -> {
            var incomeExpenseVO = IncomeExpenseVO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .type(item.getType())
                    .status(item.getStatus())
                    .sort(item.getSort())
                    .remark(item.getRemark())
                    .createTime(item.getCreateTime())
                    .build();
            incomeExpenses.add(incomeExpenseVO);
        });
        incomeExpenses.sort((o1, o2) -> {
            if (o1.getSort() == null) {
                return 1;
            }
            if (o2.getSort() == null) {
                return -1;
            }
            return o1.getSort().compareTo(o2.getSort());
        });

        result.setRecords(incomeExpenses);
        result.setTotal(incomeExpensePage.getTotal());
        return Response.responseData(result);
    }

    @Override
    public Response<String> addOrUpdateIncomeExpense(AddOrUpdateIncomeExpenseDTO addOrUpdateIncomeExpenseDTO) {
        if (addOrUpdateIncomeExpenseDTO == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var operator = userService.getCurrentUserId();

        if (addOrUpdateIncomeExpenseDTO.getId() == null) {
            var incomeExpense = IncomeExpense.builder()
                    .name(addOrUpdateIncomeExpenseDTO.getName())
                    .type(addOrUpdateIncomeExpenseDTO.getType())
                    .status(addOrUpdateIncomeExpenseDTO.getStatus())
                    .sort(addOrUpdateIncomeExpenseDTO.getSort())
                    .remark(addOrUpdateIncomeExpenseDTO.getRemark())
                    .createTime(LocalDateTime.now())
                    .createBy(operator)
                    .build();
            var saveResult = save(incomeExpense);
            if (!saveResult) {
                return Response.responseMsg(IncomeExpenseCodeEnum.ADD_INCOME_EXPENSE_ERROR);
            }
            return Response.responseMsg(IncomeExpenseCodeEnum.ADD_INCOME_EXPENSE_SUCCESS);

        } else {
            var incomeExpense = IncomeExpense.builder()
                    .id(addOrUpdateIncomeExpenseDTO.getId())
                    .name(addOrUpdateIncomeExpenseDTO.getName())
                    .type(addOrUpdateIncomeExpenseDTO.getType())
                    .status(addOrUpdateIncomeExpenseDTO.getStatus())
                    .sort(addOrUpdateIncomeExpenseDTO.getSort())
                    .remark(addOrUpdateIncomeExpenseDTO.getRemark())
                    .updateBy(operator)
                    .updateTime(LocalDateTime.now())
                    .build();
            var updateResult = updateById(incomeExpense);
            if (!updateResult) {
                return Response.responseMsg(IncomeExpenseCodeEnum.UPDATE_INCOME_EXPENSE_ERROR);
            }
            return Response.responseMsg(IncomeExpenseCodeEnum.UPDATE_INCOME_EXPENSE_SUCCESS);
        }
    }

    @Override
    public Response<String> deleteBatchIncomeExpense(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var deleteResult = incomeExpenseMapper.deleteBatchIds(ids);
        if (deleteResult <= 0) {
            return Response.responseMsg(IncomeExpenseCodeEnum.DELETE_INCOME_EXPENSE_ERROR);
        }
        return Response.responseMsg(IncomeExpenseCodeEnum.DELETE_INCOME_EXPENSE_SUCCESS);
    }

    @Override
    public Response<String> updateIncomeExpenseStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(IncomeExpense::getId, ids)
                .set(IncomeExpense::getStatus, status)
                .update();
        if (!updateResult) {
            return Response.responseMsg(IncomeExpenseCodeEnum.UPDATE_INCOME_EXPENSE_ERROR);
        }
        return Response.responseMsg(IncomeExpenseCodeEnum.UPDATE_INCOME_EXPENSE_SUCCESS);
    }

    @Override
    public Response<List<IncomeExpenseVO>> getIncomeExpenseListByType(String type) {
        var incomeExpenses = lambdaQuery()
                .eq(StringUtils.hasLength(type), IncomeExpense::getType, type)
                .list();

        var result = new ArrayList<IncomeExpenseVO>(incomeExpenses.size() + 1);
        if (!incomeExpenses.isEmpty()) {
            incomeExpenses.forEach(item -> {
                var incomeExpenseVO = IncomeExpenseVO.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .type(item.getType())
                        .status(item.getStatus())
                        .sort(item.getSort())
                        .remark(item.getRemark())
                        .createTime(item.getCreateTime())
                        .build();
                result.add(incomeExpenseVO);
            });
        }
        return Response.responseData(result);
    }
}
