package com.wansensoft.service.materialExtend;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.material.MaterialExtend;
import com.wansensoft.vo.MaterialExtendVo4List;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface MaterialExtendService extends IService<MaterialExtend> {

    MaterialExtend getMaterialExtend(long id);

    List<MaterialExtendVo4List> getDetailList(Long materialId);

    List<MaterialExtend> getListByMIds(List<Long> idList);

    String saveDetials(JSONObject obj, String sortList, Long materialId, String type);

    int insertMaterialExtend(MaterialExtend materialExtend);

    int updateMaterialExtend(MaterialExtend materialExtend);

    int checkIsBarCodeExist(Long id, String barCode);

    int deleteMaterialExtend(Long id, HttpServletRequest request);

    int batchDeleteMaterialExtendByIds(String ids, HttpServletRequest request);

    int insertMaterialExtend(JSONObject obj, HttpServletRequest request);

    int updateMaterialExtend(JSONObject obj, HttpServletRequest request);

    List<MaterialExtend> getMaterialExtendByTenantAndTime(Long tenantId, Long lastTime, Long syncNum);

    Long selectIdByMaterialIdAndDefaultFlag(Long materialId, String defaultFlag);

    Long selectIdByMaterialIdAndBarCode(Long materialId, String barCode);

    List<MaterialExtend> getListByMaterialIdAndDefaultFlagAndBarCode(Long materialId, String defaultFlag, String barCode);

    MaterialExtend getInfoByBarCode(String barCode);

    List<MaterialExtend> getMeListByBarCodeAndMid(List<String> barCodeList, Long mId);
}
