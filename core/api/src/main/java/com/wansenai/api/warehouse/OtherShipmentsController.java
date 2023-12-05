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
import com.wansenai.dto.warehouse.OtherShipmentDTO;
import com.wansenai.dto.warehouse.QueryOtherShipmentDTO;
import com.wansenai.service.warehouse.OtherShipmentsService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.warehouse.OtherShipmentDetailVO;
import com.wansenai.vo.warehouse.OtherShipmentVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("warehouse/otherShipments")
public class OtherShipmentsController {

    private final OtherShipmentsService otherShipmentsService;

    public OtherShipmentsController(OtherShipmentsService otherShipmentsService) {
        this.otherShipmentsService = otherShipmentsService;
    }

    @PostMapping("addOrUpdate")
    public Response<String> addOrUpdateOtherShipments(@RequestBody OtherShipmentDTO otherShipmentDTO) {
        return otherShipmentsService.addOrUpdateOtherShipments(otherShipmentDTO);
    }

    @PostMapping("pageList")
    public Response<Page<OtherShipmentVO>> getOtherShipmentsPageList(@RequestBody QueryOtherShipmentDTO queryOtherShipmentDTO) {
        return otherShipmentsService.getOtherShipmentsPageList(queryOtherShipmentDTO);
    }

    @GetMapping("getDetailById/{id}")
    public Response<OtherShipmentDetailVO> getOtherShipmentsDetailById(@PathVariable("id") Long id) {
        return otherShipmentsService.getOtherShipmentsDetail(id);
    }

    @PutMapping("deleteByIds")
    public Response<String> deleteOtherShipmentsByIds(@RequestParam("ids") List<Long> ids) {
        return otherShipmentsService.deleteBatchOtherShipments(ids);
    }

    @PutMapping("updateStatusByIds")
    public Response<String> updateOtherShipmentsStatusByIds(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return otherShipmentsService.updateOtherShipmentsStatus(ids, status);
    }

    @GetMapping("export")
    public void exportOtherShipments(@ModelAttribute QueryOtherShipmentDTO queryOtherShipmentDTO, HttpServletResponse response) throws Exception {
        otherShipmentsService.exportOtherShipments(queryOtherShipmentDTO, response);
    }

    @GetMapping("exportDetail/{receiptNumber}")
    public void exportOtherShipmentsDetail(@PathVariable("receiptNumber") String receiptNumber, HttpServletResponse response) throws Exception {
        otherShipmentsService.exportOtherShipmentsDetail(receiptNumber, response);
    }
}
