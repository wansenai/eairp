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
package com.wansenai.service.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.wansenai.bo.FileDataBO;
import com.wansenai.entities.basic.Operator;
import com.wansenai.entities.financial.FinancialAccount;
import com.wansenai.entities.product.ProductCategory;
import com.wansenai.entities.warehouse.Warehouse;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.BaseService;
import com.wansenai.service.basic.IOperatorService;
import com.wansenai.service.financial.IFinancialAccountService;
import com.wansenai.service.product.ProductStockKeepUnitService;
import com.wansenai.utils.ExcelUtil;
import com.wansenai.utils.FileUtil;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.constants.SecurityConstants;
import com.wansenai.utils.constants.SmsConstants;
import com.wansenai.utils.email.EmailUtils;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.SupplierCodeEnum;
import com.wansenai.utils.enums.MemberCodeEnum;
import com.wansenai.utils.enums.ProdcutCodeEnum;
import com.wansenai.utils.enums.CustomerCodeEnum;
import com.wansenai.utils.redis.RedisUtil;
import com.wansenai.utils.response.Response;
import com.wansenai.bo.SmsInfoBO;
import com.wansenai.entities.basic.Customer;
import com.wansenai.entities.basic.Member;
import com.wansenai.entities.basic.Supplier;
import com.wansenai.entities.product.Product;
import com.wansenai.entities.product.ProductStockKeepUnit;
import com.wansenai.entities.product.ProductStock;
import com.wansenai.entities.system.SysPlatformConfig;
import com.wansenai.middleware.oss.TencentOSS;
import com.wansenai.service.basic.CustomerService;
import com.wansenai.service.basic.MemberService;
import com.wansenai.service.basic.SupplierService;
import com.wansenai.service.product.ProductCategoryService;
import com.wansenai.service.product.ProductService;
import com.wansenai.service.product.ProductStockService;
import com.wansenai.service.system.ISysPlatformConfigService;
import com.wansenai.service.warehouse.WarehouseService;
import com.wansenai.vo.CaptchaVO;
import com.wansenai.vo.basic.CustomerVO;
import com.wansenai.vo.basic.MemberVO;
import com.wansenai.vo.basic.SupplierVO;
import com.wansenai.vo.product.ExportProductVO;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Optional;
import java.util.Base64;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService{

    private final RedisUtil redisUtil;

    private final Producer producer;

    private final BaseService baseService;

    private final SupplierService supplierService;

    private final CustomerService customerService;

    private final MemberService memberService;

    private final ISysPlatformConfigService platformConfigService;

    private final IFinancialAccountService accountService;

    private final ProductService productService;

    private final ProductStockKeepUnitService productStockKeepUnitService;

    private final ProductStockService productStockService;

    private final ProductCategoryService productCategoryService;

    private final WarehouseService warehouseService;

    private final IOperatorService operatorService;

    private final SysFileMapper fileMapper;

    private final String NullString = "";

    public CommonServiceImpl(RedisUtil redisUtil, Producer producer, SupplierService supplierService, CustomerService customerService, MemberService memberService, ISysPlatformConfigService platformConfigService, IFinancialAccountService accountService, ProductService productService, ProductStockKeepUnitService productStockKeepUnitService, ProductStockService productStockService, ProductCategoryService productCategoryService, WarehouseService warehouseService, BaseService baseService, IOperatorService operatorService, SysFileMapper fileMapper) {
        this.redisUtil = redisUtil;
        this.producer = producer;
        this.supplierService = supplierService;
        this.customerService = customerService;
        this.memberService = memberService;
        this.platformConfigService = platformConfigService;
        this.accountService = accountService;
        this.productService = productService;
        this.productStockKeepUnitService = productStockKeepUnitService;
        this.productStockService = productStockService;
        this.productCategoryService = productCategoryService;
        this.warehouseService = warehouseService;
        this.baseService = baseService;
        this.operatorService = operatorService;
        this.fileMapper = fileMapper;
    }

    private SmsInfoBO getSmsInfo() {
        var platform = platformConfigService.list().stream().filter(item -> item.getPlatformKey().startsWith("tencent_sms")).toList();
        var smsInfoMap = platform.stream().collect(Collectors.toMap(SysPlatformConfig::getPlatformKey, SysPlatformConfig::getPlatformValue));
        return SmsInfoBO.builder()
                .secretId(smsInfoMap.get("tencent_sms_secret_id"))
                .secretKey(smsInfoMap.get("tencent_sms_secret_key"))
                .smsClint(smsInfoMap.get("tencent_sms_client"))
                .sdkAppId(smsInfoMap.get("tencent_sms_sdk_appId"))
                .build();
    }

    @Override
    public CaptchaVO getCaptcha() {
        String captchaId = "CAPTCHA" + SnowflakeIdUtil.nextId();
        String text = producer.createText();
        String imgEncode = "";
        BufferedImage bi = producer.createImage(text);
        try (FastByteArrayOutputStream fos = new FastByteArrayOutputStream()) {
            ImageIO.write(bi, "jpg", fos);
            imgEncode = Base64.getEncoder().encodeToString(fos.toByteArray());
            redisUtil.set(SecurityConstants.EMAIL_VERIFY_CODE_CACHE_PREFIX + captchaId, text, 180);
            fos.flush();
        } catch (Exception e) {
            log.error("获取验证码失败: " + e.getMessage());
            return null;
        }
        return CaptchaVO.builder()
                .captchaId(captchaId)
                .imagePath("data:image/jpeg;base64," + imgEncode)
                .build();
    }

    @Override
    public Boolean sendSmsCode(Integer type, String phoneNumber) {
        if(!StringUtils.hasText(phoneNumber) && type == null) {
            return false;
        }
        var regex = "^(0|86|17951)?(13[0-9]|15[012356789]|16[6]|19[89]]|17[01345678]|18[0-9]|14[579])[0-9]{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        if(!matcher.matches()) {
            return false;
        }
        var templateId = switch (type) {
            case 0 -> SmsConstants.SMS_TEMPLATE_ID_REGISTER_USER;
            case 1 -> SmsConstants.SMS_TEMPLATE_ID_PHONE_LOGIN;
            case 2 -> SmsConstants.SMS_TEMPLATE_ID_UPDATE_PASSWORD;
            case 3 -> SmsConstants.SMS_TEMPLATE_ID_UPDATE_PHONE;
            default -> "";
        };

        var key = switch (type) {
            case 0 -> SecurityConstants.REGISTER_VERIFY_CODE_CACHE_PREFIX;
            case 1 -> SecurityConstants.LOGIN_VERIFY_CODE_CACHE_PREFIX;
            case 2 -> SecurityConstants.UPDATE_PASSWORD_VERIFY_CODE_CACHE_PREFIX;
            case 3 -> SecurityConstants.UPDATE_PHONE_VERIFY_CODE_CACHE_PREFIX;
            default -> "";
        };

        try {
            SmsInfoBO smsInfo = getSmsInfo();
            Credential cred = new Credential(smsInfo.getSecretId(), smsInfo.getSecretKey());
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, smsInfo.getSmsClint(),clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppid(smsInfo.getSdkAppId());
            req.setSign(SmsConstants.SMS_SIGN_NAME);
            req.setSessionContext(phoneNumber);
            req.setTemplateID(templateId);
            req.setPhoneNumberSet(new String[]{"86"+phoneNumber});

            Random random = new Random();
            var code = String.valueOf(random.nextInt(900000) + 100000);
            redisUtil.set(key + phoneNumber, code, 120);
            req.setTemplateParamSet(new String[]{code});

            client.SendSms(req);

            return true;
        }catch (Exception e) {
            log.error(String.format("用户手机号:%s, 验证码发送失败，错误消息:%s", phoneNumber, e.getMessage()));
            return false;
        }
    }

    public static String getForgetCode() {
        Random random = new Random();
        //把随机生成的数字转成字符串
        StringBuilder str = new StringBuilder(String.valueOf(random.nextInt(9)));
        for (int i = 0; i < 5; i++) {
            str.append(random.nextInt(9));
        }
        return str.toString();
    }

    @Override
    public Response<String> sendEmailCode(Integer type, String email) {
        if (!StringUtils.hasLength(email)) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        try {
            switch (type)
            {
                case 0:
                    String resultCode = "";
                    if (redisUtil.hasKey(SecurityConstants.EMAIL_RESET_PASSWORD_LOGIN_VERIFY_CODE_CACHE_PREFIX + email)) {
                        resultCode = redisUtil.getString(SecurityConstants.EMAIL_RESET_PASSWORD_LOGIN_VERIFY_CODE_CACHE_PREFIX + email);
                        EmailUtils.forgetPasswordEmailNotice(resultCode, email);
                    } else {
                        resultCode = getForgetCode();
                        redisUtil.set(SecurityConstants.EMAIL_RESET_PASSWORD_LOGIN_VERIFY_CODE_CACHE_PREFIX + email, resultCode);
                        redisUtil.expire(SecurityConstants.EMAIL_RESET_PASSWORD_LOGIN_VERIFY_CODE_CACHE_PREFIX + email, 180);
                        EmailUtils.forgetPasswordEmailNotice(resultCode, email);
                    }
                    break;
                case 1:
                    String resultCode2 = "";
                    if (redisUtil.hasKey(SecurityConstants.EMAIL_RESET_VERIFY_CODE_CACHE_PREFIX + email)) {
                        resultCode2 = redisUtil.getString(SecurityConstants.EMAIL_RESET_VERIFY_CODE_CACHE_PREFIX + email);
                        EmailUtils.resetEmailNotice(resultCode2, email);
                    } else {
                        resultCode2 = getForgetCode();
                        redisUtil.set(SecurityConstants.EMAIL_RESET_VERIFY_CODE_CACHE_PREFIX + email, resultCode2);
                        redisUtil.expire(SecurityConstants.EMAIL_RESET_VERIFY_CODE_CACHE_PREFIX + email, 180);
                        EmailUtils.resetEmailNotice(resultCode2, email);
                    }
                    break;
                case 2:
                    String resultCode3 = "";
                    if (redisUtil.hasKey(SecurityConstants.EMAIL_LOGIN_VERIFY_CODE_CACHE_PREFIX + email)) {
                        resultCode3 = redisUtil.getString(SecurityConstants.EMAIL_LOGIN_VERIFY_CODE_CACHE_PREFIX + email);
                        EmailUtils.loginEmailNotice(resultCode3, email);
                    } else {
                        resultCode3 = getForgetCode();
                        redisUtil.set(SecurityConstants.EMAIL_LOGIN_VERIFY_CODE_CACHE_PREFIX + email, resultCode3);
                        redisUtil.expire(SecurityConstants.EMAIL_LOGIN_VERIFY_CODE_CACHE_PREFIX + email, 180);
                        EmailUtils.loginEmailNotice(resultCode3, email);
                    }
                    break;
                default:
                    break;
            }
        }catch (Exception e) {
            log.error("邮箱验证码发送失败：" + e.getMessage());
        }

        return Response.responseMsg(BaseCodeEnum.EMAIL_VERIFY_SEND_SUCCESS);
    }

    @Override
    public Response<String> uploadExclsData(MultipartFile file) {
        if(!file.isEmpty()) {
            try {
                String filename = file.getOriginalFilename();
                 if(filename == null || filename.isEmpty()) {
                    return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_NO_FILENAME_MATCH);

                 } else if (filename.contains("供应商") || filename.contains("supplier")) {
                    var result = readSuppliersFromExcel(file);
                    if(!result){
                        return Response.responseMsg(SupplierCodeEnum.ADD_SUPPLIER_ERROR);
                    }
                     return Response.responseMsg(SupplierCodeEnum.ADD_SUPPLIER_SUCCESS);

                } else if (filename.contains("客户") || filename.contains("customer") || filename.contains("Customer")) {
                     var result = readCustomerFromExcel(file);
                     if(!result){
                         return Response.responseMsg(CustomerCodeEnum.ADD_CUSTOMER_ERROR);
                     }
                     return Response.responseMsg(CustomerCodeEnum.ADD_CUSTOMER_SUCCESS);
                } else if (filename.contains("会员") || filename.contains("member") || filename.contains("Member")) {
                     var result = readMemberFromExcel(file);
                     if(!result){
                         return Response.responseMsg(MemberCodeEnum.ADD_MEMBER_ERROR);
                     }
                     return Response.responseMsg(MemberCodeEnum.ADD_MEMBER_SUCCESS);
                 } else if (filename.contains("商品") || filename.contains("product") || filename.contains("Product")
                 || filename.contains("Commodity")) {
                     var message = checkProductBarCodeExist(file);
                     if (StringUtils.hasLength(message)) {
                         return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_ERROR.getCode(), message);
                     }
                     var result = readProductFromExcel(file, 0);
                     if(!result){
                         return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_ERROR);
                     }
                     return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_SUCCESS);
                 } else {
                    log.warn("上传Excel文件失败: 文件名不匹配");
                    return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_NO_FILENAME_MATCH);
                }
            } catch (Exception e) {
                log.error("上传Excel文件失败: " + e.getMessage());
                return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_ERROR);
            }
        }
        return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_ERROR);
    }

    @Override
    public Response<String> productCoverUpload(MultipartFile file, Integer type) {
        try {
            var result = readProductFromExcel(file, type);
            if(!result){
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_ERROR);
            }
            return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_SUCCESS);
        } catch (Exception e) {
            log.error("上传Excel文件失败: " + e.getMessage());
            return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    private boolean readSuppliersFromExcel(MultipartFile file) throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        for (int i = 2; i <= sheet.getLastRowNum(); ++i) {
            Row row = sheet.getRow(i);
            var supplier = Supplier.builder()
                    .supplierName(getCellValue(row.getCell(0), dataFormatter))
                    .contact(getCellValue(row.getCell(1), dataFormatter))
                    .phoneNumber(getCellValue(row.getCell(2), dataFormatter))
                    .contactNumber(getCellValue(row.getCell(3), dataFormatter))
                    .email(getCellValue(row.getCell(4), dataFormatter))
                    .fax(getCellValue(row.getCell(5), dataFormatter))
                    .firstQuarterAccountPayment(getNumericCellValue(row.getCell(6)))
                    .secondQuarterAccountPayment(getNumericCellValue(row.getCell(7)))
                    .thirdQuarterAccountPayment(getNumericCellValue(row.getCell(8)))
                    .fourthQuarterAccountPayment(getNumericCellValue(row.getCell(9)))
                    .taxNumber(getCellValue(row.getCell(10), dataFormatter))
                    .taxRate(getNumericCellValue(row.getCell(11)))
                    .bankName(getCellValue(row.getCell(12), dataFormatter))
                    .accountNumber(getCellValue(row.getCell(13), dataFormatter))
                    .address(getCellValue(row.getCell(14), dataFormatter))
                    .remark(getCellValue(row.getCell(15), dataFormatter))
                    .build();
            suppliers.add(supplier);
            workbook.close();
        }
        return supplierService.batchAddSupplier(suppliers);
    }

    private boolean readCustomerFromExcel(MultipartFile file) throws IOException {
        List<Customer> customers = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        for (int i = 2; i <= sheet.getLastRowNum(); ++i) {
            Row row = sheet.getRow(i);
            var customer = Customer.builder()
                    .customerName(getCellValue(row.getCell(0), dataFormatter))
                    .contact(getCellValue(row.getCell(1), dataFormatter))
                    .phoneNumber(getCellValue(row.getCell(2), dataFormatter))
                    .email(getCellValue(row.getCell(3), dataFormatter))
                    .firstQuarterAccountReceivable(getNumericCellValue(row.getCell(4)))
                    .secondQuarterAccountReceivable(getNumericCellValue(row.getCell(5)))
                    .thirdQuarterAccountReceivable(getNumericCellValue(row.getCell(6)))
                    .fourthQuarterAccountReceivable(getNumericCellValue(row.getCell(7)))
                    .taxNumber(getCellValue(row.getCell(8), dataFormatter))
                    .taxRate(getNumericCellValue(row.getCell(9)))
                    .bankName(getCellValue(row.getCell(10), dataFormatter))
                    .accountNumber(getCellValue(row.getCell(11), dataFormatter))
                    .address(getCellValue(row.getCell(12), dataFormatter))
                    .remark(getCellValue(row.getCell(13), dataFormatter))
                    .build();
            customers.add(customer);
            workbook.close();
        }
        return customerService.batchAddCustomer(customers);
    }

    private boolean readMemberFromExcel(MultipartFile file) throws IOException {
        List<Member> members = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        for (int i = 2; i <= sheet.getLastRowNum(); ++i) {
            Row row = sheet.getRow(i);
            var member = Member.builder()
                    .memberNumber(getCellValue(row.getCell(0), dataFormatter))
                    .memberName(getCellValue(row.getCell(1), dataFormatter))
                    .phoneNumber(getCellValue(row.getCell(2), dataFormatter))
                    .email(getCellValue(row.getCell(3), dataFormatter))
                    .advancePayment(getNumericCellValue(row.getCell(4)))
                    .remark(getCellValue(row.getCell(5), dataFormatter))
                    .build();
            members.add(member);
            workbook.close();
        }
        return memberService.batchAddMember(members);
    }

    private String checkProductBarCodeExist(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        var codeListMap = new ArrayList<Map<String, String>>();
        for (int i = 2; i <= sheet.getLastRowNum(); ++i) {
            Row row = sheet.getRow(i);
            var productBarcode = getCellValue(row.getCell(1), dataFormatter);
            var productName = getCellValue(row.getCell(0), dataFormatter);
            if (StringUtils.hasLength(productBarcode)) {
                var codeMap = new HashMap<String, String>();
                codeMap.put("productBarcode", productBarcode);
                codeMap.put("productName", productName);
                codeListMap.add(codeMap);
            }
        }
        var codeList = codeListMap.stream().map(item -> item.get("productBarcode")).toList();
        var codeMap = new HashMap<String, Integer>();
        for (var code : codeList) {
            if (codeMap.containsKey(code)) {
                codeMap.put(code, codeMap.get(code) + 1);
            } else {
                codeMap.put(code, 1);
            }
        }
        var message = NullString;
        // 获取codeListMap的productBarcode 检查DB里是否存在重复的barcode
        var productCodes = codeListMap.stream()
                .map(item -> item.get("productBarcode"))
                .collect(Collectors.toList());
        if (productStockKeepUnitService.checkProductCode(productCodes)) {
            message += "existDataBase";
            return message;
        }

        JSONArray jsonArray = new JSONArray();
        for (var entry : codeMap.entrySet()) {
            if (entry.getValue() > 1) {
                var jsonObject = new JSONObject();
                jsonObject.put("productCode", entry.getKey());
                var productNameList = codeListMap.stream().filter(item -> item.get("productBarcode").equals(entry.getKey())).map(item -> item.get("productName")).collect(Collectors.toList());
                jsonObject.put("productName", productNameList);
                jsonArray.add(jsonObject);
                message = jsonArray.toJSONString();
            }
        }
        if (StringUtils.hasLength(message)) {
            workbook.close();
            return message;
        }
        workbook.close();
        return message;
    }

    private boolean readProductFromExcel(MultipartFile file, int type) throws IOException, InvalidFormatException {
        List<Product> products = new ArrayList<>();
        List<ProductStockKeepUnit> productStockKeepUnits = new ArrayList<>();
        List<ProductStock> productStocks = new ArrayList<>();

        InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        var warehouseId = warehouseService.getDefaultWarehouse().getData().getId();
        var userId = baseService.getCurrentUserId();

        // Read header row to create the mapping
        Row headerRow = sheet.getRow(1);
        Map<String, Integer> headerMap = new HashMap<>();
        for (Cell cell : headerRow) {
            String cellValue = dataFormatter.formatCellValue(cell);
            if (cellValue != null) {
                headerMap.put(cellValue, cell.getColumnIndex());
            }
        }

        // Helper method to get cell value by column name and row index
        BiFunction<String, Integer, String> getCellValueByName = (columnName, rowIndex) -> {
            Integer columnIndex = headerMap.get(columnName);
            if (columnIndex != null) {
                return getCellValue(sheet.getRow(rowIndex).getCell(columnIndex), dataFormatter);
            }
            return null;
        };

        Map<String, String[]> fieldMappings = new HashMap<>();
        fieldMappings.put("barCode", new String[]{"Bar code", "产品条码"});
        fieldMappings.put("productName", new String[]{"Name of commodity", "产品名称"});
        fieldMappings.put("productCategory", new String[]{"Category", "产品分类"});
        fieldMappings.put("productStandard", new String[]{"Specifications", "产品规格"});
        fieldMappings.put("productModel", new String[]{"Model", "产品型号"});
        fieldMappings.put("productColor", new String[]{"Color", "产品颜色"});
        fieldMappings.put("productWeight", new String[]{"Base weight", "产品重量"});
        fieldMappings.put("guaranteePeriod", new String[]{"Guarantee period", "产品保质期"});
        fieldMappings.put("productUnit", new String[]{"Commodity measurement unit", "产品单位"});
        fieldMappings.put("serialNumber", new String[]{"Serial number", "启用序列号"});
        fieldMappings.put("batchNumber", new String[]{"Batch number", "启用批号"});
        fieldMappings.put("warehouseShelves", new String[]{"Position shelf", "仓库货架"});
        fieldMappings.put("productManufacturer", new String[]{"Manufacturer", "产品制造商"});
        fieldMappings.put("otherFieldOne", new String[]{"Extended field 1", "其他字段一"});
        fieldMappings.put("otherFieldTwo", new String[]{"Extended field 2", "其他字段二"});
        fieldMappings.put("otherFieldThree", new String[]{"Extended field 3", "其他字段三"});
        fieldMappings.put("remark", new String[]{"Remark", "备注"});
        fieldMappings.put("multiAttribute", new String[]{"Multiple attributes", "多属性"});
        fieldMappings.put("retailPrice", new String[]{"Retail price", "零售价格"});
        fieldMappings.put("purchasePrice", new String[]{"Purchase price", "采购价格"});
        fieldMappings.put("salePrice", new String[]{"Market price", "销售价格"});
        fieldMappings.put("lowPrice", new String[]{"Minimum selling price", "最低价格"});
        fieldMappings.put("initStockQuantity", new String[]{"Initial inventory quantity", "初始库存数量"});
        fieldMappings.put("currentStockQuantity", new String[]{"Current inventory quantity", "当前库存数量"});

        // Helper method to get the cell value for a field name (supports both English and Chinese)
        BiFunction<String, Integer, String> getFieldCellValue = (fieldName, rowIndex) -> {
            String[] possibleNames = fieldMappings.get(fieldName);
            if (possibleNames != null) {
                for (String name : possibleNames) {
                    String value = getCellValueByName.apply(name, rowIndex);
                    if (value != null) {
                        return value;
                    }
                }
            }
            return null;
        };

        for (int i = 2; i <= sheet.getLastRowNum(); ++i) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) {
                continue;
            }

            var productCode = getFieldCellValue.apply("barCode", i);
            var productId = SnowflakeIdUtil.nextId();
            Long productCategoryId = null;

            if (getFieldCellValue.apply("productCategory", i) != null) {
                var productCategory = productCategoryService.getProductCategoryByName(getFieldCellValue.apply("productCategory", i));
                productCategoryId = productCategory.getId();
            }

            var product = Product.builder()
                    .id(productId)
                    .productName(getFieldCellValue.apply("productName", i))
                    .productStandard(getFieldCellValue.apply("productStandard", i))
                    .productModel(getFieldCellValue.apply("productModel", i))
                    .productColor(getFieldCellValue.apply("productColor", i))
                    .productCategoryId(productCategoryId)
                    .productWeight(getNumericCellValue(getFieldCellValue.apply("productWeight",i)))
                    .productExpiryNum(getIntegerCellValue(getFieldCellValue.apply("guaranteePeriod",i)))
                    .productUnit(getFieldCellValue.apply("productUnit", i))
                    .enableSerialNumber(getIntegerCellValue(getFieldCellValue.apply("serialNumber", i)))
                    .enableBatchNumber(getIntegerCellValue(getFieldCellValue.apply("batchNumber", i)))
                    .warehouseShelves(getFieldCellValue.apply("warehouseShelves", i))
                    .productManufacturer(getFieldCellValue.apply("productManufacturer", i))
                    .otherFieldOne(getFieldCellValue.apply("otherFieldOne", i))
                    .otherFieldTwo(getFieldCellValue.apply("otherFieldTwo", i))
                    .otherFieldThree(getFieldCellValue.apply("otherFieldThree", i))
                    .status(0)
                    .remark(getFieldCellValue.apply("remark", i))
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            products.add(product);

            var productPrice = ProductStockKeepUnit.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .productId(productId)
                    .productBarCode(productCode)
                    .multiAttribute(getFieldCellValue.apply("multiAttribute", i))
                    .purchasePrice(getNumericCellValue(getFieldCellValue.apply("purchasePrice", i)))
                    .retailPrice(getNumericCellValue(getFieldCellValue.apply("retailPrice", i)))
                    .salePrice(getNumericCellValue(getFieldCellValue.apply("salePrice", i)))
                    .lowPrice(getNumericCellValue(getFieldCellValue.apply("lowPrice", i)))
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            productStockKeepUnits.add(productPrice);

            var productStock = ProductStock.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .productSkuId(productPrice.getId())
                    .warehouseId(warehouseId)
                    .initStockQuantity(getNumericCellValue(getFieldCellValue.apply("initStockQuantity", i)))
                    .currentStockQuantity(getNumericCellValue(getFieldCellValue.apply("currentStockQuantity", i)))
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            productStocks.add(productStock);
        }
        workbook.close();

        if (type == 1) {
            Set<String> existingBarcodes = new HashSet<>();
            Iterator<ProductStockKeepUnit> iterator = productStockKeepUnits.iterator();
            while (iterator.hasNext()) {
                ProductStockKeepUnit productStockKeepUnit = iterator.next();
                String barcode = productStockKeepUnit.getProductBarCode();
                // 如果当前 barcode 已经存在于集合中，则移除相关数据
                if (existingBarcodes.contains(barcode)) {
                    iterator.remove();
                    // 从 products 中移除对应 productId 的数据
                    products.removeIf(product -> product.getId().equals(productStockKeepUnit.getProductId()));
                    productStocks.removeIf(productStock ->
                            productStock.getProductSkuId().equals(productStockKeepUnit.getId()));
                } else {
                    existingBarcodes.add(barcode);
                }
            }
        } else if (type == 0) {
            // 用于存储每个 barcode 的最后一条数据的索引
            Map<String, Integer> lastBarcodeIndexes = new HashMap<>();

            // 遍历 productStockKeepUnits 列表以确定每个 barcode 的最后一条数据的索引
            for (int i = productStockKeepUnits.size() - 1; i >= 0; i--) {
                ProductStockKeepUnit productStockKeepUnit = productStockKeepUnits.get(i);
                String barcode = productStockKeepUnit.getProductBarCode();
                // 如果当前 barcode 已经存在于集合中，则更新索引
                if (!lastBarcodeIndexes.containsKey(barcode)) {
                    lastBarcodeIndexes.put(barcode, i);
                }
            }

            // 创建一个新的 productStockKeepUnits 列表来存储最后一条数据
            List<ProductStockKeepUnit> updatedProductStockKeepUnits = new ArrayList<>();

            // 遍历 productStockKeepUnits 列表以只保留每个 barcode 的最后一条数据
            for (int i = 0; i < productStockKeepUnits.size(); i++) {
                ProductStockKeepUnit productStockKeepUnit = productStockKeepUnits.get(i);
                String barcode = productStockKeepUnit.getProductBarCode();
                int lastIndex = lastBarcodeIndexes.get(barcode);
                // 只添加最后一条数据
                if (i == lastIndex) {
                    updatedProductStockKeepUnits.add(productStockKeepUnit);
                } else {
                    // 如果不是最后一条数据，则从 products 和 productStocks 中移除对应的条目
                    products.removeIf(product -> product.getId().equals(productStockKeepUnit.getProductId()));
                    productStocks.removeIf(productStock ->
                            productStock.getProductSkuId().equals(productStockKeepUnit.getId()));
                }
            }

            // 更新 productStockKeepUnits 列表
            productStockKeepUnits = updatedProductStockKeepUnits;
        }

        boolean addProductResult = productService.batchAddProduct(products);
        boolean addProductPriceResult = productStockKeepUnitService.saveBatch(productStockKeepUnits);
        boolean addProductStockResult = productStockService.saveBatch(productStocks);

        return addProductResult && addProductPriceResult && addProductStockResult;
    }


    public File exportExcel(String type) {
        if (!StringUtils.hasLength(type)) {
            return null;
        }
        File file = null;

        if (type.contains("供应商")) {
            List<Supplier> suppliersList = supplierService.list();
            List<SupplierVO> supplierData = new ArrayList<>();

            for (Supplier supplier : suppliersList) {
                SupplierVO supplierVO = new SupplierVO();
                BeanUtils.copyProperties(supplier, supplierVO);
                supplierData.add(supplierVO);
            }

            String[] columnNames = {"供应商名称", "联系人", "手机号码", "联系电话", "电子邮箱", "传真",
                    "第一季度应付账款", "第二季度应付账款", "第三季度应付账款", "第四季度应付账款",
                    "纳税人识别号", "税率(%)", "开户行", "账号", "地址", "备注"};

            List<String[]> data = new ArrayList<>();
            String title = "信息内容";
            for (SupplierVO item : supplierData) {
                String[] supplier = new String[columnNames.length];
                supplier[0] = StringUtils.hasText(item.getSupplierName()) ? item.getSupplierName() : "";
                supplier[1] = StringUtils.hasText(item.getContact()) ? item.getContact() : "";
                supplier[2] = StringUtils.hasText(item.getPhoneNumber()) ? item.getPhoneNumber() : "";
                supplier[3] = StringUtils.hasText(item.getContactNumber()) ? item.getContactNumber() : "";
                supplier[4] = StringUtils.hasText(item.getEmail()) ? item.getEmail() : "";
                supplier[5] = StringUtils.hasText(item.getFax()) ? item.getFax() : "";
                supplier[6] = item.getFirstQuarterAccountPayment() != null ? item.getFirstQuarterAccountPayment().toString() : "";
                supplier[7] = item.getSecondQuarterAccountPayment() != null ? item.getSecondQuarterAccountPayment().toString() : "";
                supplier[8] = item.getThirdQuarterAccountPayment() != null ? item.getThirdQuarterAccountPayment().toString() : "";
                supplier[9] = item.getFourthQuarterAccountPayment() != null ? item.getFourthQuarterAccountPayment().toString() : "";
                supplier[10] = StringUtils.hasText(item.getTaxNumber()) ? item.getTaxNumber() : "";
                supplier[11] = item.getTaxRate() != null ? item.getTaxRate().toString() : "";
                supplier[12] = StringUtils.hasText(item.getBankName()) ? item.getBankName() : "";
                supplier[13] = item.getAccountNumber() != null ? item.getAccountNumber() : "";
                supplier[14] = StringUtils.hasText(item.getAddress()) ? item.getAddress() : "";
                supplier[15] = StringUtils.hasText(item.getRemark()) ? item.getRemark() : "";
                data.add(supplier);
            }
            file = ExcelUtil.exportObjectsWithoutTitle(type + ".xlsx", "*导入时本行内容请勿删除，切记！", columnNames, title, data);

        } else if (type.contains("客户")) {
            List<Customer> customerList = customerService.list();
            List<CustomerVO> customerData = new ArrayList<>();

            for (Customer customer : customerList) {
                CustomerVO customerVO = new CustomerVO();
                BeanUtils.copyProperties(customer, customerVO);
                customerData.add(customerVO);
            }

            String[] columnNames = {"客户名称", "联系人", "手机号码", "电子邮箱",
                    "第一季度应收账款", "第二季度应收账款", "第三季度应收账款", "第四季度应收账款",
                    "纳税人识别号", "税率(%)", "开户行", "账号", "地址", "备注"};

            List<String[]> data = new ArrayList<>();
            String title = "信息内容";
            for (CustomerVO item : customerData) {
                String[] customer = new String[columnNames.length];
                customer[0] = StringUtils.hasText(item.getCustomerName()) ? item.getCustomerName() : "";
                customer[1] = StringUtils.hasText(item.getContact()) ? item.getContact() : "";
                customer[2] = StringUtils.hasText(item.getPhoneNumber()) ? item.getPhoneNumber() : "";
                customer[3] = StringUtils.hasText(item.getEmail()) ? item.getEmail() : "";
                customer[4] = item.getFirstQuarterAccountReceivable() != null ? item.getFirstQuarterAccountReceivable().toString() : "";
                customer[5] = item.getSecondQuarterAccountReceivable() != null ? item.getSecondQuarterAccountReceivable().toString() : "";
                customer[6] = item.getThirdQuarterAccountReceivable() != null ? item.getThirdQuarterAccountReceivable().toString() : "";
                customer[7] = item.getFirstQuarterAccountReceivable() != null ? item.getFirstQuarterAccountReceivable().toString() : "";
                customer[8] = StringUtils.hasText(item.getTaxNumber()) ? item.getTaxNumber() : "";
                customer[9] = item.getTaxRate() != null ? item.getTaxRate().toString() : "";
                customer[10] = StringUtils.hasText(item.getBankName()) ? item.getBankName() : "";
                customer[11] = item.getAccountNumber() != null ? item.getAccountNumber() : "";
                customer[12] = StringUtils.hasText(item.getAddress()) ? item.getAddress() : "";
                customer[13] = StringUtils.hasText(item.getRemark()) ? item.getRemark() : "";
                data.add(customer);
            }

            file = ExcelUtil.exportObjectsWithoutTitle(type + ".xlsx", "*导入时本行内容请勿删除，切记！", columnNames, title, data);

        } else if (type.contains("会员")) {
            List<Member> memberList = memberService.list();
            List<MemberVO> memberData = new ArrayList<>();

            for (Member member : memberList) {
                MemberVO memberVO = new MemberVO();
                BeanUtils.copyProperties(member, memberVO);
                memberData.add(memberVO);
            }

            String[] columnNames = {"会员卡号", "会员名称", "手机号码", "电子邮箱", "预付款", "备注"};
            List<String[]> data = new ArrayList<>();
            String title = "信息内容";
            for (MemberVO item : memberData) {
                String[] member = new String[columnNames.length];
                member[0] = StringUtils.hasText(item.getMemberNumber()) ? item.getMemberNumber() : "";
                member[1] = StringUtils.hasText(item.getMemberName()) ? item.getMemberName() : "";
                member[2] = StringUtils.hasText(item.getPhoneNumber()) ? item.getPhoneNumber() : "";
                member[3] = StringUtils.hasText(item.getEmail()) ? item.getEmail() : "";
                member[4] = item.getAdvancePayment() != null ? item.getAdvancePayment().toString() : "";
                member[5] = StringUtils.hasText(item.getRemark()) ? item.getRemark() : "";
                data.add(member);
            }
            file = ExcelUtil.exportObjectsWithoutTitle(type + ".xlsx", "*导入时本行内容请勿删除，切记！", columnNames, title, data);
        } else if (type.contains("商品")) {
            List<Product> products = productService.list();
            List<ExportProductVO> productVOS = new ArrayList<>(products.size() + 1);
            List<ProductStockKeepUnit> productStockKeepUnits = productStockKeepUnitService.list();
            List<ProductStock> productStocks = productStockService.list();

            for (Product product : products) {

                // 查询商品类别名称
                var productCategory = productCategoryService.getById(product.getProductCategoryId());
                String productCategoryName = "";
                if(productCategory != null) {
                    productCategoryName = productCategory.getCategoryName();
                }

                var productPrice = productStockKeepUnits.stream()
                        .filter(item -> item.getProductId().equals(product.getId()))
                        .findFirst()
                        .orElse(null);

                ExportProductVO productVO = ExportProductVO.builder()
                        .productName(product.getProductName())
                        .productStandard(product.getProductStandard())
                        .productModel(product.getProductModel())
                        .productColor(product.getProductColor())
                        .productCategoryName(productCategoryName)
                        .productWeight(product.getProductWeight())
                        .productExpiryNum(product.getProductExpiryNum())
                        .productUnit(product.getProductUnit())
                        .build();
                if(productPrice != null) {
                    productVO.setProductBarcode(productPrice.getProductBarCode());
                    productVO.setMultiAttribute(productPrice.getMultiAttribute());
                    productVO.setPurchasePrice(productPrice.getPurchasePrice());
                    productVO.setRetailPrice(productPrice.getRetailPrice());
                    productVO.setSalesPrice(productPrice.getSalePrice());
                    productVO.setLowSalesPrice(productPrice.getLowPrice());
                    productVO.setEnableSerialNumber(String.valueOf(product.getEnableSerialNumber()));
                    productVO.setEnableBatchNumber(String.valueOf(product.getEnableBatchNumber()));
                    productVO.setWarehouseShelves(product.getWarehouseShelves());
                    productVO.setProductManufacturer(product.getProductManufacturer());
                    productVO.setOtherFieldOne(product.getOtherFieldOne());
                    productVO.setOtherFieldTwo(product.getOtherFieldTwo());
                    productVO.setOtherFieldThree(product.getOtherFieldThree());
                }
                productStocks.stream()
                        .filter(item -> item.getProductSkuId().equals(productPrice.getId()))
                        .findFirst().ifPresent(productStock -> productVO.setStock(productStock.getCurrentStockQuantity()));

                productVOS.add(productVO);

                String[] columnNames =
                        {"名称*", "规格", "型号", "颜色", "类别", "基础重量(kg)", "保质期(天)", "基本单位*", "商品条码*",
                        "多属性", "采购价", "零售价", "销售价", "最低售价", "序列号", "批次号", "仓库货架", "制造商",
                        "其他字段1", "其他字段2", "其他字段3", "库存"};
                List<String[]> data = new ArrayList<>();
                String title = "信息内容";
                for (ExportProductVO item : productVOS) {
                    String[] productData = new String[columnNames.length];
                    productData[0] = StringUtils.hasText(item.getProductName()) ? item.getProductName() : "";
                    productData[1] = StringUtils.hasText(item.getProductStandard()) ? item.getProductStandard() : "";
                    productData[2] = StringUtils.hasText(item.getProductModel()) ? item.getProductModel() : "";
                    productData[3] = StringUtils.hasText(item.getProductColor()) ? item.getProductColor() : "";
                    productData[4] = StringUtils.hasText(item.getProductCategoryName()) ? item.getProductCategoryName() : "";
                    productData[5] = item.getProductWeight() != null ? item.getProductWeight().toString() : "";
                    productData[6] = item.getProductExpiryNum() != null ? item.getProductExpiryNum().toString() : "";
                    productData[7] = StringUtils.hasText(item.getProductUnit()) ? item.getProductUnit() : "";
                    productData[8] = item.getProductBarcode() != null ? item.getProductBarcode() : "";
                    productData[9] = StringUtils.hasText(item.getMultiAttribute()) ? item.getMultiAttribute() : "";
                    productData[10] = item.getPurchasePrice() != null ? item.getPurchasePrice().toString() : "";
                    productData[11] = item.getRetailPrice() != null ? item.getRetailPrice().toString() : "";
                    productData[12] = item.getSalesPrice() != null ? item.getSalesPrice().toString() : "";
                    productData[13] = item.getLowSalesPrice() != null ? item.getLowSalesPrice().toString() : "";
                    productData[14] = StringUtils.hasText(item.getEnableSerialNumber()) ? item.getEnableSerialNumber() : "";
                    productData[15] = StringUtils.hasText(item.getEnableBatchNumber()) ? item.getEnableBatchNumber() : "";
                    productData[16] = StringUtils.hasText(item.getWarehouseShelves()) ? item.getWarehouseShelves() : "";
                    productData[17] = StringUtils.hasText(item.getProductManufacturer()) ? item.getProductManufacturer() : "";
                    productData[18] = StringUtils.hasText(item.getOtherFieldOne()) ? item.getOtherFieldOne() : "";
                    productData[19] = StringUtils.hasText(item.getOtherFieldTwo()) ? item.getOtherFieldTwo() : "";
                    productData[20] = StringUtils.hasText(item.getOtherFieldThree()) ? item.getOtherFieldThree() : "";
                    productData[21] = item.getStock() != null ? item.getStock().toString() : "";
                    data.add(productData);
                }
                file = ExcelUtil.exportObjectsWithoutTitle(type + ".xlsx", "*导入时本行内容请勿删除，切记！", columnNames, title, data);
            }
        }

        return file;
    }

    @Override
    public Response<List<String>> uploadOss(List<MultipartFile> files) {
        var platform = platformConfigService.list().stream().filter(item -> item.getPlatformKey().startsWith("tencent_oss")).toList();
        var ossInfoMap = platform.stream().collect(Collectors.toMap(SysPlatformConfig::getPlatformKey, SysPlatformConfig::getPlatformValue));

       if (ossInfoMap.get("tencent_oss_secret_id") == null || ossInfoMap.get("tencent_oss_secret_key") == null
               || ossInfoMap.get("tencent_oss_region") == null || ossInfoMap.get("tencent_oss_bucket") == null) {
            return Response.responseMsg(BaseCodeEnum.OSS_KEY_NOT_EXIST);
        }

        TencentOSS.getInstance().setBucket(ossInfoMap.get("tencent_oss_bucket"));
        TencentOSS.getInstance().setRegion(ossInfoMap.get("tencent_oss_region"));
        TencentOSS.getInstance().setSecretid(ossInfoMap.get("tencent_oss_secret_id"));
        TencentOSS.getInstance().setSecretkey(ossInfoMap.get("tencent_oss_secret_key"));
        var instance = TencentOSS.getInstance();
        if(instance == null) {
            return Response.responseMsg(BaseCodeEnum.OSS_GET_INSTANCE_ERROR);
        }
        try {
            List<String> keys = new ArrayList<>(files.size() + 2);
            files.forEach(file -> {
                keys.add("temp" + "_" + SnowflakeIdUtil.nextId() + "_" + file.getOriginalFilename());
            });
            var result = instance.uploadBatch(FileUtil.convertMultipartFilesToFiles(files), keys);
            log.info("上传文件信息: " + result);
            return Response.responseData(result);
        }catch (Exception e) {
            log.error("上传文件失败: " + e.getMessage());
            return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public Response<String> generateSnowflakeId(String type) {
        if (!StringUtils.hasText(type)) {
            return Response.responseMsg(BaseCodeEnum.SNOWFLAKE_ID_GENERATE_ERROR);
        }
        var id = SnowflakeIdUtil.nextId();
        StringBuilder idStr = new StringBuilder(String.valueOf(id));
        var idLength = idStr.length();
        if (idLength < 18) {
            var zero = 18 - idLength;
            for (int i = 0; i < zero; ++i) {
                idStr.insert(0, "0");
            }
        }
        var idPrefix = switch (type) {
            case "供应商" -> "S";
            case "客户" -> "C";
            case "会员" -> "V";
            case "商品" -> "P";
            case "订单" -> "O";
            case "收入单" -> "SRD";
            case "支出单" -> "ZCD";
            case "转账单" -> "ZZD";
            case "收款单" -> "SKD";
            case "付款单" -> "FKD";
            case "采购单" -> "CGD";
            case "销售单" -> "XSD";
            case "退货单" -> "THD";
            case "入库单" -> "RKD";
            case "出库单" -> "CKD";
            case "调拨单" -> "DBD";
            case "盘点单" -> "PDD";
            case "报损单" -> "BSD";
            case "报溢单" -> "BZD";
            case "调价单" -> "DJD";
            case "组装单" -> "ADD";
            case "拆卸单" -> "CXD";
            case "零售出库" -> "LSCK";
            case "零售退货" -> "LSTH";
            case "销售出库" -> "XSCK";
            case "销售退货" -> "XSTH";
            case "采购入库" -> "CGRK";
            case "其他入库" -> "QTRK";
            case "其他出库" -> "QTCK";
            case "调拨出库" -> "DBCK";
            case "采购退货" -> "CGTH";
            case "收预付款" -> "ACD";
            default -> "";
        };
        return Response.responseData(idPrefix + idStr);
    }

    @Override
    public List<FileDataBO> getFileList(String fileId) {
        List<FileDataBO> fileList = new ArrayList<>();
        if (StringUtils.hasLength(fileId)) {
            List<Long> ids = Arrays.stream(fileId.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            fileList.addAll(fileMapper.selectBatchIds(ids)
                    .stream()
                    .map(item ->
                            FileDataBO.builder(
                                    item.getFileName(),
                                    item.getFileUrl(),
                                    item.getId(),
                                    item.getUid(),
                                    item.getFileType(),
                                    item.getFileSize()
                            ))
                    .toList());
        }
        return fileList;
    }

    @Override
    public String getProductName(Long productId) {
        return Optional.ofNullable(productService.getById(productId))
                .map(Product::getProductName)
                .orElse(NullString);
    }

    @Override
    public String getProductCategoryName(Long productCategoryId) {
        return Optional.ofNullable(productCategoryService.getById(productCategoryId))
                .map(ProductCategory::getCategoryName)
                .orElse(NullString);
    }

    @Override
    public String getWarehouseName(Long warehouseId) {
        return Optional.ofNullable(warehouseService.getById(warehouseId))
                .map(Warehouse::getWarehouseName)
                .orElse(NullString);
    }

    @Override
    public String getMemberName(Long memberId) {
        return Optional.ofNullable(memberService.getById(memberId))
                .map(Member::getMemberName)
                .orElse(NullString);
    }

    @Override
    public String getSupplierName(Long supplierId) {
        return Optional.ofNullable(supplierService.getById(supplierId))
                .map(Supplier::getSupplierName)
                .orElse(NullString);
    }

    @Override
    public String getCustomerName(Long customerId) {
        return Optional.ofNullable(customerService.getById(customerId))
                .map(Customer::getCustomerName)
                .orElse(NullString);
    }

    @Override
    public String getOperatorName(Long operatorId) {
        return Optional.ofNullable(operatorService.getById(operatorId))
                .map(Operator::getName)
                .orElse(NullString);
    }

    @Override
    public String getRelatedPersonName(Long relatedPersonId) {
        // 查询供应商 客户 会员  哪一个不为空就返回哪一个
        var member = memberService.getById(relatedPersonId);
        if(member != null) {
            return member.getMemberName();
        }
        var customer = customerService.getById(relatedPersonId);
        if(customer != null) {
            return customer.getCustomerName();
        }
        var supplier = supplierService.getById(relatedPersonId);
        if(supplier != null) {
            return supplier.getSupplierName();
        }
        return NullString;
    }

    @Override
    public String getAccountName(Long accountId) {
        return Optional.ofNullable(accountService.getById(accountId))
                .map(FinancialAccount::getAccountName)
                .orElse(NullString);
    }

    private String getCellValue(Cell cell, DataFormatter dataFormatter) {
        if (cell != null) {
            String value = dataFormatter.formatCellValue(cell);
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        return null;
    }

    private BigDecimal getNumericCellValue(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC) {
                return BigDecimal.valueOf(cell.getNumericCellValue());
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    return new BigDecimal(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    // Log and handle the number format exception if necessary
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getNumericCellValue(String cellValue) {
        if (cellValue != null) {
            try {
                return new BigDecimal(cellValue);
            } catch (NumberFormatException e) {
                // Log and handle the number format exception if necessary
            }
        }
        return BigDecimal.ZERO;
    }

    private Integer getIntegerCellValue(String cellValue) {
        if (cellValue != null) {
            try {
                return Integer.valueOf(cellValue);
            } catch (NumberFormatException e) {
                // Log and handle the number format exception if necessary
            }
        }
        return 0;
    }

    private boolean isRowEmpty(Row row) {
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
