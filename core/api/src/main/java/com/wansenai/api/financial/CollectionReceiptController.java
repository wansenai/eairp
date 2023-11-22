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
import com.wansenai.dto.financial.AddOrUpdateCollectionDTO;
import com.wansenai.dto.financial.QueryCollectionDTO;
import com.wansenai.service.financial.CollectionReceiptService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.financial.CollectionDetailVO;
import com.wansenai.vo.financial.CollectionVO;
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
@RequestMapping("/financial/collection")
public class CollectionReceiptController {

    private final CollectionReceiptService collectionReceiptService;

    public CollectionReceiptController(CollectionReceiptService collectionReceiptService) {
        this.collectionReceiptService = collectionReceiptService;
    }

    @PostMapping("addOrUpdate")
    public Response<String> addOrUpdateCollectionReceipt(@RequestBody AddOrUpdateCollectionDTO addOrUpdateCollectionDTO) {
        return collectionReceiptService.addOrUpdateCollectionReceipt(addOrUpdateCollectionDTO);
    }

    @PostMapping("pageList")
    public Response<Page<CollectionVO>> getCollectionReceiptPageList(@RequestBody QueryCollectionDTO queryCollectionDTO) {
        return collectionReceiptService.getCollectionReceiptPageList(queryCollectionDTO);
    }

    @GetMapping("getDetailById/{id}")
    public Response<CollectionDetailVO> getCollectionReceiptDetailById(@PathVariable("id") Long id) {
        return collectionReceiptService.getCollectionReceiptDetail(id);
    }

    @PutMapping("deleteByIds")
    public Response<String> deleteCollectionReceiptByIds(@RequestParam("ids") List<Long> ids) {
        return collectionReceiptService.deleteBatchCollectionReceipt(ids);
    }

    @PutMapping("updateStatusByIds")
    public Response<String> updateCollectionReceiptStatusByIds(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return collectionReceiptService.updateCollectionReceiptStatus(ids, status);
    }
}
