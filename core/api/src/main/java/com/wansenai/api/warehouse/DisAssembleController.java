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
package com.wansenai.api.warehouse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.warehouse.DisassembleReceiptDTO;
import com.wansenai.dto.warehouse.QueryDisassembleReceiptDTO;
import com.wansenai.service.warehouse.DisassembleReceiptService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.warehouse.DisassembleReceiptDetailVO;
import com.wansenai.vo.warehouse.DisassembleReceiptVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("warehouse/disassemble")
public class DisAssembleController {

    private final DisassembleReceiptService disassembleService;

    public DisAssembleController(DisassembleReceiptService disassembleService) {
        this.disassembleService = disassembleService;
    }

    @PostMapping("addOrUpdate")
    public Response<String> addOrUpdateDisAssembleReceipt(@RequestBody DisassembleReceiptDTO disassembleReceiptDTO) {
        return disassembleService.addOrUpdateDisassembleReceipt(disassembleReceiptDTO);
    }

    @PostMapping("pageList")
    public Response<Page<DisassembleReceiptVO>> getDisAssembleReceiptPageList(@RequestBody QueryDisassembleReceiptDTO queryDisassembleReceiptDTO) {
        return disassembleService.getDisassembleReceiptPageList(queryDisassembleReceiptDTO);
    }

    @GetMapping("getDetailById/{id}")
    public Response<DisassembleReceiptDetailVO> getDisAssembleReceiptDetailById(@PathVariable("id") Long id) {
        return disassembleService.getDisassembleReceiptDetail(id);
    }

    @PutMapping("deleteByIds")
    public Response<String> deleteDisAssembleReceiptByIds(@RequestParam("ids") List<Long> ids) {
        return disassembleService.deleteBatchDisassembleReceipt(ids);
    }

    @PutMapping("updateStatusByIds")
    public Response<String> updateDisAssembleReceiptStatusByIds(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return disassembleService.updateDisassembleReceiptStatus(ids, status);
    }

    @GetMapping("export")
    public void exportDisAssembleReceipt(@ModelAttribute QueryDisassembleReceiptDTO queryDisassembleReceiptDTO, HttpServletResponse response) throws Exception {
        disassembleService.exportDisAssembleReceipt(queryDisassembleReceiptDTO, response);
    }
}
