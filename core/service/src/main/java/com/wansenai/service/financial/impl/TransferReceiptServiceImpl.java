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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.dto.financial.AddOrUpdateTransferDTO;
import com.wansenai.dto.financial.QueryTransferDTO;
import com.wansenai.entities.financial.FinancialMain;
import com.wansenai.mappers.financial.FinancialMainMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.financial.FinancialSubService;
import com.wansenai.service.financial.IFinancialAccountService;
import com.wansenai.service.financial.TransferReceiptService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.financial.TransferDetailVO;
import com.wansenai.vo.financial.TransferVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferReceiptServiceImpl extends ServiceImpl<FinancialMainMapper, FinancialMain> implements TransferReceiptService {

    private final FinancialSubService financialSubService;

    private final CommonService commonService;

    private final ISysUserService userService;

    private final SysFileMapper fileMapper;

    private final IFinancialAccountService accountService;

    public TransferReceiptServiceImpl(FinancialSubService financialSubService, CommonService commonService, ISysUserService userService, SysFileMapper fileMapper, IFinancialAccountService accountService) {
        this.financialSubService = financialSubService;
        this.commonService = commonService;
        this.userService = userService;
        this.fileMapper = fileMapper;
        this.accountService = accountService;
    }


    @Override
    public Response<Page<TransferVO>> getTransferReceiptPageList(QueryTransferDTO queryTransferDTO) {
        return null;
    }

    @Override
    public Response<TransferDetailVO> getTransferReceiptDetail(Long id) {
        return null;
    }

    @Override
    public Response<String> addOrUpdateTransferReceipt(AddOrUpdateTransferDTO addOrUpdateTransferDTO) {
        return null;
    }

    @Override
    public Response<String> deleteBatchTransferReceipt(List<Long> ids) {
        return null;
    }

    @Override
    public Response<String> updateTransferReceiptStatus(List<Long> ids, Integer status) {
        return null;
    }
}
