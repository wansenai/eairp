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
package com.wansenai.api.financial;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.financial.AddOrUpdateIncomeDTO;
import com.wansenai.dto.financial.QueryIncomeDTO;
import com.wansenai.service.financial.IncomeReceiptService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.financial.IncomeDetailVO;
import com.wansenai.vo.financial.IncomeVO;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RestController
@RequestMapping("/financial/income")
public class IncomeReceiptController {

    private final IncomeReceiptService incomeReceiptService;

    public IncomeReceiptController(IncomeReceiptService incomeReceiptService) {
        this.incomeReceiptService = incomeReceiptService;
    }

    @PostMapping("addOrUpdate")
    public Response<String> addOrUpdateIncomeReceipt(@RequestBody AddOrUpdateIncomeDTO addOrUpdateIncomeDTO) {
        return incomeReceiptService.addOrUpdateIncomeReceipt(addOrUpdateIncomeDTO);
    }

    @PostMapping("pageList")
    public Response<Page<IncomeVO>> getIncomeReceiptPageList(@RequestBody QueryIncomeDTO queryIncomeDTO) {
        return incomeReceiptService.getIncomeReceiptPageList(queryIncomeDTO);
    }

    @GetMapping("getDetailById/{id}")
    public Response<IncomeDetailVO> getIncomeReceiptDetailById(@PathVariable("id") Long id) {
        return incomeReceiptService.getIncomeReceiptDetail(id);
    }

    @PutMapping("deleteByIds")
    public Response<String> deleteIncomeReceiptByIds(@RequestParam("ids") List<Long> ids) {
        return incomeReceiptService.deleteBatchIncomeReceipt(ids);
    }

    @PutMapping("updateStatusByIds")
    public Response<String> updateIncomeReceiptStatusByIds(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return incomeReceiptService.updateIncomeReceiptStatus(ids, status);
    }
}
