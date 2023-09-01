package com.wansensoft.api.material;

import com.wansensoft.entities.material.MaterialProperty;
import com.wansensoft.service.materialProperty.MaterialPropertyService;
import com.wansensoft.utils.BaseResponseInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/materialProperty")
@Api(tags = {"商品扩展字段"})
public class MaterialPropertyController {

    private final MaterialPropertyService materialPropertyService;

    public MaterialPropertyController(MaterialPropertyService materialPropertyService) {
        this.materialPropertyService = materialPropertyService;
    }

    @GetMapping(value = "/getAllList")
    @ApiOperation(value = "查询全部商品扩展字段信息")
    public BaseResponseInfo getAllList(HttpServletRequest request) throws Exception{
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            List<MaterialProperty> list = materialPropertyService.getMaterialProperty();
            res.code = 200;
            res.data = list;
        } catch(Exception e){
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

}
