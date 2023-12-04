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
import com.wansenai.dto.financial.AddOrUpdateTransferDTO;
import com.wansenai.dto.financial.QueryTransferDTO;
import com.wansenai.service.financial.TransferReceiptService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.financial.TransferDetailVO;
import com.wansenai.vo.financial.TransferVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@RestController
@RequestMapping("/financial/transfer")
public class TransferReceiptController {

    private final TransferReceiptService transferReceiptService;

    public TransferReceiptController(TransferReceiptService transferReceiptService) {
        this.transferReceiptService = transferReceiptService;
    }

    @PostMapping("addOrUpdate")
    public Response<String> addOrUpdateTransferReceipt(@RequestBody AddOrUpdateTransferDTO addOrUpdateTransferDTO) {
        return transferReceiptService.addOrUpdateTransferReceipt(addOrUpdateTransferDTO);
    }

    @PostMapping("pageList")
    public Response<Page<TransferVO>> getTransferReceiptPageList(@RequestBody QueryTransferDTO queryTransferDTO) {
        return transferReceiptService.getTransferReceiptPageList(queryTransferDTO);
    }

    @GetMapping("getDetailById/{id}")
    public Response<TransferDetailVO> getTransferReceiptDetailById(@PathVariable("id") Long id) {
        return transferReceiptService.getTransferReceiptDetail(id);
    }

    @PutMapping("deleteByIds")
    public Response<String> deleteTransferReceiptByIds(@RequestParam("ids") List<Long> ids) {
        return transferReceiptService.deleteBatchTransferReceipt(ids);
    }

    @PutMapping("updateStatusByIds")
    public Response<String> updateTransferReceiptStatusByIds(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return transferReceiptService.updateTransferReceiptStatus(ids, status);
    }

    @GetMapping("export")
    public void exportTransferReceipt(@ModelAttribute QueryTransferDTO queryTransferDTO, HttpServletResponse response) {
        transferReceiptService.exportTransferReceipt(queryTransferDTO, response);
    }
}
