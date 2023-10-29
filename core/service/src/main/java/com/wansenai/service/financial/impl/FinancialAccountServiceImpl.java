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
package com.wansenai.service.financial.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.service.BaseService;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.FinancialCodeEnum;
import com.wansenai.service.financial.IFinancialAccountService;
import com.wansenai.utils.response.Response;
import com.wansenai.dto.financial.AddOrUpdateAccountDTO;
import com.wansenai.dto.financial.QueryAccountDTO;
import com.wansenai.entities.financial.FinancialAccount;
import com.wansenai.mappers.financial.FinancialAccountMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.vo.financial.AccountVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinancialAccountServiceImpl extends ServiceImpl<FinancialAccountMapper, FinancialAccount> implements IFinancialAccountService {

    private final BaseService baseService;

    private final FinancialAccountMapper accountMapper;

    public FinancialAccountServiceImpl(BaseService baseService, FinancialAccountMapper accountMapper) {
        this.baseService = baseService;
        this.accountMapper = accountMapper;
    }


    @Override
    public Response<Page<AccountVO>> getAccountPageList(QueryAccountDTO queryAccountDTO) {
        var result = new Page<AccountVO>();
        var accountVos = new ArrayList<AccountVO>();

        Page<FinancialAccount> page = new Page<>(queryAccountDTO.getPage(), queryAccountDTO.getPageSize());
        var wrapper = new LambdaQueryWrapper<FinancialAccount>()
                .like(StringUtils.hasLength(queryAccountDTO.getAccountName()), FinancialAccount::getAccountName, queryAccountDTO.getAccountName())
                .like(StringUtils.hasLength(queryAccountDTO.getAccountNumber()), FinancialAccount::getAccountNumber, queryAccountDTO.getAccountNumber())
                .eq(FinancialAccount::getDeleteFlag, CommonConstants.NOT_DELETED);

        accountMapper.selectPage(page, wrapper);
        page.getRecords().forEach(item -> {
            AccountVO accountVO = new AccountVO();
            BeanUtils.copyProperties(item, accountVO);
            accountVos.add(accountVO);
        });

        result.setRecords(accountVos);
        result.setTotal(page.getTotal());
        result.setSize(page.getSize());
        result.setPages(page.getPages());

        return Response.responseData(result);
    }

    private void updateDefaultAccount(Long id){
        // Find the default account and set it as non default 0- default 1- non default
        var defaultAccount = lambdaQuery()
                .eq(FinancialAccount::getIsDefault, CommonConstants.IS_DEFAULT)
                .eq(FinancialAccount::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();
        if(defaultAccount != null) {
            defaultAccount.setIsDefault(CommonConstants.NOT_DEFAULT);
            updateById(defaultAccount);
        }
        // Set current account as default
        var currentAccount = getById(id);
        currentAccount.setIsDefault(CommonConstants.IS_DEFAULT);
        updateById(currentAccount);
    }

    @Override
    public Response<String> addOrUpdateAccount(AddOrUpdateAccountDTO addOrUpdateAccountDTO) {
        var userId = baseService.getCurrentUserId();
        if (addOrUpdateAccountDTO.getId() == null) {
            // Add Account
            var account = new FinancialAccount();
            BeanUtils.copyProperties(addOrUpdateAccountDTO, account);
            account.setIsDefault(CommonConstants.NOT_DELETED);
            account.setStatus(CommonConstants.STATUS_NORMAL);
            account.setCreateBy(userId);
            account.setCreateTime(LocalDateTime.now());
            var saveResult = accountMapper.insert(account);
            if(saveResult == 0) {
                return Response.responseMsg(FinancialCodeEnum.ADD_ACCOUNT_ERROR);
            }
            if(addOrUpdateAccountDTO.getIsDefault() == CommonConstants.IS_DEFAULT) {
                updateDefaultAccount(account.getId());
            }
            return Response.responseMsg(FinancialCodeEnum.ADD_ACCOUNT_SUCCESS);
        } else {
            // Update Account
            var account = new FinancialAccount();
            BeanUtils.copyProperties(addOrUpdateAccountDTO, account);
            account.setUpdateBy(userId);
            account.setUpdateTime(LocalDateTime.now());
            var updateResult = accountMapper.updateById(account);
            if(updateResult == 0) {
                return Response.responseMsg(FinancialCodeEnum.UPDATE_ACCOUNT_ERROR);
            }
            if(addOrUpdateAccountDTO.getIsDefault() == CommonConstants.IS_DEFAULT) {
                updateDefaultAccount(account.getId());
            }
            return Response.responseMsg(FinancialCodeEnum.UPDATE_ACCOUNT_SUCCESS);
        }
    }

    @Override
    @Transactional
    public Response<String> deleteBatchAccount(List<Long> ids) {
        if(ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var deleteResult = removeBatchByIds(ids);
        if(!deleteResult) {
            return Response.responseMsg(FinancialCodeEnum.DELETE_ACCOUNT_ERROR);
        }
        return Response.responseMsg(FinancialCodeEnum.DELETE_ACCOUNT_SUCCESS);
    }

    @Override
    public Response<String> updateAccountStatus(List<Long> ids, Integer status) {
        if(ids.isEmpty() && status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var updateStatus = lambdaUpdate()
                .in(FinancialAccount::getId, ids)
                .set(FinancialAccount::getStatus, status)
                .update();

        if(!updateStatus) {
            return Response.responseMsg(FinancialCodeEnum.UPDATE_ACCOUNT_STATUS_ERROR);
        }

        return Response.responseMsg(FinancialCodeEnum.UPDATE_ACCOUNT_STATUS_SUCCESS);
    }

    @Override
    public Response<List<AccountVO>> getAccountList() {
        var accountList = lambdaQuery()
                .eq(FinancialAccount::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();
        if(accountList.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
        }
        var accountVos = new ArrayList<AccountVO>();
        accountList.forEach(item -> {
            AccountVO accountVO = new AccountVO();
            BeanUtils.copyProperties(item, accountVO);
            accountVos.add(accountVO);
        });
        return Response.responseData(accountVos);
    }
}
