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
package com.wansenai.service.financial;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.utils.response.Response;
import com.wansenai.dto.financial.AddOrUpdateAccountDTO;
import com.wansenai.dto.financial.QueryAccountDTO;
import com.wansenai.entities.financial.FinancialAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansenai.vo.financial.AccountVO;

import java.util.List;

/**
 * <p>
 * 账户信息 服务类
 * </p>
 */
public interface IFinancialAccountService extends IService<FinancialAccount> {

    Response<Page<AccountVO>> getAccountList(QueryAccountDTO queryAccountDTO);

    Response<String> addOrUpdateAccount(AddOrUpdateAccountDTO addOrUpdateAccountDTO);

    Response<String> deleteBatchAccount(List<Long> ids);

    Response<String> updateAccountStatus(List<Long> ids, Integer status);
}
