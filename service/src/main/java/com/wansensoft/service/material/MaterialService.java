package com.wansensoft.service.material;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.depot.Depot;
import com.wansensoft.entities.material.*;
import com.wansensoft.entities.unit.Unit;
import com.wansensoft.entities.user.User;
import com.wansensoft.utils.BaseResponseInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jxl.Sheet;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MaterialService extends IService<Material> {

    Material getMaterial(long id);

    List<Material> getMaterialListByIds(String ids);

    List<Material> getMaterial();

    List<MaterialVo4Unit> select(String materialParam, String color, String materialOther, String weight, String expiryNum,
                                 String enableSerialNumber, String enableBatchNumber, String position, String enabled,
                                 String remark, String categoryId, String mpList, int offset, int rows);

    Long countMaterial(String materialParam, String color, String materialOther, String weight, String expiryNum,
                  String enableSerialNumber, String enableBatchNumber, String position, String enabled,
                  String remark, String categoryId,String mpList);

    int insertMaterial(JSONObject obj, HttpServletRequest request);

    int updateMaterial(JSONObject obj, HttpServletRequest request);

    int deleteMaterial(Long id, HttpServletRequest request);

    int batchDeleteMaterial(String ids, HttpServletRequest request);

    int batchDeleteMaterialByIds(String ids);

    int checkIsNameExist(Long id, String name);

    int checkIsExist(Long id, String name, String model, String color, String standard, String mfrs,
                     String otherField1, String otherField2, String otherField3, String unit, Long unitId);

    int batchSetStatus(Boolean status, String ids);

    Unit findUnit(Long mId);

    List<MaterialVo4Unit> findById(Long id);

    List<MaterialVo4Unit> findByIdWithBarCode(Long meId);

    List<Long> getListByParentId(Long parentId);

    List<Long> getIdListByParentId(List<Long> idList, Long parentId);

    JSONArray getMaterialByParam(String materialParam);

    List<MaterialVo4Unit> findBySelectWithBarCode(Long categoryId, String q, String enableSerialNumber,
                                                  String enableBatchNumber, Integer offset, Integer rows);

    int findBySelectWithBarCodeCount(Long categoryId, String q, String enableSerialNumber,
                                     String enableBatchNumber);

    void exportExcel(String categoryId, String materialParam, String color, String materialOther, String weight,
                     String expiryNum, String enabled, String enableSerialNumber, String enableBatchNumber,
                     String remark, HttpServletResponse response);

    BaseResponseInfo importExcel(MultipartFile file, HttpServletRequest request);

    void batchCheckExistMaterialListByParam(List<MaterialWithInitStock> mList, String name, String standard,
                                            String model, String color, String unit, String sku);

    void batchCheckExistBarCodeByParam(List<MaterialWithInitStock> mList,
                                       String barCode, String manyBarCode);

    void insertOrUpdateMaterialExtend(JSONObject materialExObj, String type, String defaultFlag, Long mId, User user);

    String getBasicBarCode(MaterialWithInitStock m);

    void insertInitialStockByMaterialAndDepot(Long depotId, Long mId, BigDecimal stock, BigDecimal lowSafeStock, BigDecimal highSafeStock);

    void insertCurrentStockByMaterialAndDepot(Long depotId, Long mId, BigDecimal stock);

    void batchDeleteInitialStockByMaterialList(List<Long> mIdList);

    void batchDeleteCurrentStockByMaterialList(List<Long> mIdList);

    List<MaterialVo4Unit> getMaterialEnableSerialNumberList(String q, Integer offset, Integer rows);

    Long getMaterialEnableSerialNumberCount(String q);

    BigDecimal parseBigDecimalEx(String str);

    BigDecimal parsePrice(String price, String ratio);

    BigDecimal getInitStockByMidAndDepotList(List<Long> depotList, Long materialId);

    BigDecimal getInitStock(Long materialId, Long depotId);

    BigDecimal getCurrentStockByMaterialIdAndDepotId(Long materialId, Long depotId);

    Map<Long,BigDecimal> getCurrentStockMapByMaterialList(List<MaterialVo4Unit> list);

    MaterialInitialStock getSafeStock(Long materialId, Long depotId);

    List<MaterialVo4Unit> getMaterialByMeId(Long meId);

    String getMaxBarCode();

    List<String> getMaterialNameList();

    List<MaterialVo4Unit> getMaterialByBarCode(String barCode);

    List<MaterialVo4Unit> getMaterialByBarCodeAndWithOutMId(String barCode, Long mId);

    List<MaterialInitialStockWithMaterial> getInitialStockWithMaterial(List<Long> depotList);

    List<MaterialVo4Unit> getListWithStock(List<Long> depotList, List<Long> idList, String position, String materialParam, Integer zeroStock,
                                           String column, String order, Integer offset, Integer rows);

    int getListWithStockCount(List<Long> depotList, List<Long> idList, String position, String materialParam, Integer zeroStock);

    MaterialVo4Unit getTotalStockAndPrice(List<Long> depotList, List<Long> idList, String position, String materialParam);

    String getBigUnitStock(BigDecimal stock, Long unitId);

    String getMaterialOtherByParam(String[] mpArr, MaterialVo4Unit m);

    int batchSetMaterialCurrentStock(String ids);

    int batchUpdate(JSONObject jsonObject);

    MaterialExtend getMaterialExtendBySerialNumber(String serialNumber);
}