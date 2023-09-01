package com.wansensoft.service.supplier;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.supplier.Supplier;
import com.wansensoft.utils.BaseResponseInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SupplierService extends IService<Supplier> {

    Supplier getSupplier(long id);

    List<Supplier> getSupplierListByIds(String ids);

    List<Supplier> getSupplier();

    List<Supplier> select(String supplier, String type, String phonenum, String telephone, String roleType, int offset, int rows);

    Long countSupplier(String supplier, String type, String phonenum, String telephone, String roleType);

    int insertSupplier(JSONObject obj, HttpServletRequest request);

    int updateSupplier(JSONObject obj, HttpServletRequest request);

    int deleteSupplier(Long id, HttpServletRequest request);

    int batchDeleteSupplier(String ids, HttpServletRequest request);

    int batchDeleteSupplierByIds(String ids);

    int checkIsNameExist(Long id, String name);

    int checkIsNameAndTypeExist(Long id, String name, String type);

    int updateAdvanceIn(Long supplierId, BigDecimal advanceIn);

    List<Supplier> findBySelectCus();

    List<Supplier> findBySelectSup();

    List<Supplier> findBySelectRetail();

    List<Supplier> findById(Long supplierId);

    int batchSetStatus(Boolean status, String ids);

    List<Supplier> findUserCustomer();

    List<Supplier> findByAll(String supplier, String type, String phonenum, String telephone);

    Map<String, Object> getBeginNeedByOrganId(Long organId);

    void importVendor(MultipartFile file, HttpServletRequest request);

    void importCustomer(MultipartFile file, HttpServletRequest request);

    void importMember(MultipartFile file, HttpServletRequest request);

    BaseResponseInfo importExcel(List<Supplier> mList, String type);

    BigDecimal parseBigDecimalEx(String str);

    File exportExcel(List<Supplier> dataList, String type);

}
