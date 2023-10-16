package com.wansensoft.api.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansensoft.dto.basic.AddOrUpdateOperatorDTO;
import com.wansensoft.dto.basic.QueryOperatorDTO;
import com.wansensoft.service.basic.IOperatorService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.basic.OperatorVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basic/operator")
public class OperatorController {

    private final IOperatorService operatorService;

    public OperatorController(IOperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @PostMapping("list")
    public Response<Page<OperatorVO>> getOperatorList(@RequestBody QueryOperatorDTO queryOperatorDTO) {
        return operatorService.getOperatorList(queryOperatorDTO);
    }

    @PostMapping("addOrUpdate")
    public Response<String> addOrUpdateOperator(@RequestBody AddOrUpdateOperatorDTO addOrUpdateOperatorDTO) {
        return operatorService.addOrUpdateOperator(addOrUpdateOperatorDTO);
    }

    @DeleteMapping("delete")
    public Response<String> deleteOperator(@RequestParam("ids") List<Long> ids) {
        return operatorService.deleteBatchOperator(ids);
    }

    @PostMapping("updateStatus")
    public Response<String> updateOperatorStatus(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return operatorService.updateOperatorStatus(ids, status);
    }

}
