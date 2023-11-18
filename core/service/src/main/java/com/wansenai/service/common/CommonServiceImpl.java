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

import com.google.code.kaptcha.Producer;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.wansenai.bo.FileDataBO;
import com.wansenai.entities.warehouse.Warehouse;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.BaseService;
import com.wansenai.service.product.ProductStockKeepUnitService;
import com.wansenai.utils.ExcelUtil;
import com.wansenai.utils.FileUtil;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.constants.SecurityConstants;
import com.wansenai.utils.constants.SmsConstants;
import com.wansenai.utils.enums.*;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
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

    private final ProductService productService;

    private final ProductStockKeepUnitService productStockKeepUnitService;

    private final ProductStockService productStockService;

    private final ProductCategoryService productCategoryService;

    private final WarehouseService warehouseService;

    private final SysFileMapper fileMapper;

    private final String NullString = "";

    public CommonServiceImpl(RedisUtil redisUtil, Producer producer, SupplierService supplierService, CustomerService customerService, MemberService memberService, ISysPlatformConfigService platformConfigService, ProductService productService, ProductStockKeepUnitService productStockKeepUnitService, ProductStockService productStockService, ProductCategoryService productCategoryService, WarehouseService warehouseService, BaseService baseService, SysFileMapper fileMapper) {
        this.redisUtil = redisUtil;
        this.producer = producer;
        this.supplierService = supplierService;
        this.customerService = customerService;
        this.memberService = memberService;
        this.platformConfigService = platformConfigService;
        this.productService = productService;
        this.productStockKeepUnitService = productStockKeepUnitService;
        this.productStockService = productStockService;
        this.productCategoryService = productCategoryService;
        this.warehouseService = warehouseService;
        this.baseService = baseService;
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
            default -> "";
        };

        var key = switch (type) {
            case 0 -> SecurityConstants.REGISTER_VERIFY_CODE_CACHE_PREFIX;
            case 1 -> SecurityConstants.LOGIN_VERIFY_CODE_CACHE_PREFIX;
            case 2 -> SecurityConstants.UPDATE_PASSWORD_VERIFY_CODE_CACHE_PREFIX;
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

    @Override
    public Response<String> uploadExclsData(MultipartFile file) {
        if(!file.isEmpty()) {
            try {
                String filename = file.getOriginalFilename();
                 if(filename == null || filename.isEmpty()) {
                    return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_NO_FILENAME_MATCH);

                 } else if (filename.contains("供应商")) {
                    var result = readSuppliersFromExcel(file);
                    if(!result){
                        return Response.responseMsg(SupplierCodeEnum.ADD_SUPPLIER_ERROR);
                    }
                     return Response.responseMsg(SupplierCodeEnum.ADD_SUPPLIER_SUCCESS);

                } else if (filename.contains("客户")) {
                     var result = readCustomerFromExcel(file);
                     if(!result){
                         return Response.responseMsg(CustomerCodeEnum.ADD_CUSTOMER_ERROR);
                     }
                     return Response.responseMsg(CustomerCodeEnum.ADD_CUSTOMER_SUCCESS);
                } else if (filename.contains("会员")) {
                     var result = readMemberFromExcel(file);
                     if(!result){
                         return Response.responseMsg(MemberCodeEnum.ADD_MEMBER_ERROR);
                     }
                     return Response.responseMsg(MemberCodeEnum.ADD_MEMBER_SUCCESS);
                 } else if (filename.contains("商品")) {
                     var result = readProductFromExcel(file);
                     if(!result){
                         return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_ERROR);
                     }
                     return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ADD_SUCCESS);
                 } else {
                    log.error("上传Excel文件失败: 文件名不匹配");
                    return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_NO_FILENAME_MATCH);
                }
            } catch (Exception e) {
                log.error("上传Excel文件失败: " + e.getMessage());
                return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_ERROR);
            }
        }
        return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_ERROR);
    }

    private boolean readSuppliersFromExcel(MultipartFile file) throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        Workbook workbook = new HSSFWorkbook(file.getInputStream());
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
        Workbook workbook = new HSSFWorkbook(file.getInputStream());
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
        Workbook workbook = new HSSFWorkbook(file.getInputStream());
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

    private boolean readProductFromExcel(MultipartFile file) throws IOException {
        List<Product> products = new ArrayList<>();
        List<ProductStockKeepUnit> productStockKeepUnits = new ArrayList<>();
        List<ProductStock> productStocks = new ArrayList<>();

        Workbook workbook = new HSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        Long warehouseId = null;
        if(getCellValue(sheet.getRow(1).getCell(24), dataFormatter) != null) {
            var warehouse = warehouseService.getWarehouseByName(getCellValue(sheet.getRow(1).getCell(24), dataFormatter));
            warehouseId = warehouse.getId();
        }
        var userId = baseService.getCurrentUserId();

        for (int i = 2; i <= sheet.getLastRowNum(); ++i) {
            Row row = sheet.getRow(i);
            var productCode = getCellValue(row.getCell(9), dataFormatter);

            // 检查商品条码是否重复存在于数据库中 重复则不导入 比较商品条码是否有重复
            if (productStockKeepUnitService.checkProductCode(List.of(productCode))) {
                return false;
            }

            var productId = SnowflakeIdUtil.nextId();
            Long productCategoryId = null;

            if(getCellValue(row.getCell(4), dataFormatter) != null) {
                var productCategory = productCategoryService.getProductCategoryByName(getCellValue(row.getCell(4), dataFormatter));
                productCategoryId = productCategory.getId();
            }

            var product = Product.builder()
                    .id(productId)
                    .productName(getCellValue(row.getCell(0), dataFormatter))
                    .productStandard(getCellValue(row.getCell(1), dataFormatter))
                    .productModel(getCellValue(row.getCell(2), dataFormatter))
                    .productColor(getCellValue(row.getCell(3), dataFormatter))
                    .productCategoryId(productCategoryId)
                    .productWeight(getNumericCellValue(row.getCell(5)))
                    .productExpiryNum(getIntegerCellValue(row.getCell(6)))
                    .productUnit(getCellValue(row.getCell(7), dataFormatter))
                    .enableSerialNumber(getIntegerCellValue(row.getCell(16)))
                    .enableBatchNumber(getIntegerCellValue(row.getCell(17)))
                    .warehouseShelves(getCellValue(row.getCell(18), dataFormatter))
                    .productManufacturer(getCellValue(row.getCell(19), dataFormatter))
                    .otherFieldOne(getCellValue(row.getCell(20), dataFormatter))
                    .otherFieldTwo(getCellValue(row.getCell(21), dataFormatter))
                    .otherFieldThree(getCellValue(row.getCell(22), dataFormatter))
                    .remark(getCellValue(row.getCell(23), dataFormatter))
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            products.add(product);

            var productPrice = ProductStockKeepUnit.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .productId(productId)
                    .productBarCode(Long.valueOf(productCode))
                    .multiAttribute(getCellValue(row.getCell(11), dataFormatter))
                    .purchasePrice(getNumericCellValue(row.getCell(12)))
                    .retailPrice(getNumericCellValue(row.getCell(13)))
                    .salePrice(getNumericCellValue(row.getCell(14)))
                    .lowPrice(getNumericCellValue(row.getCell(15)))
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            productStockKeepUnits.add(productPrice);

            var productStock = ProductStock.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .productSkuId(productPrice.getId())
                    .warehouseId(warehouseId)
                    .initStockQuantity(getNumericCellValue(row.getCell(24)))
                    .currentStockQuantity(getNumericCellValue(row.getCell(24)))
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            productStocks.add(productStock);
            workbook.close();
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
                    productData[8] = item.getProductBarcode() != null ? item.getProductBarcode().toString() : "";
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
            case "零售出库" -> "LSCK";
            case "零售退货" -> "LSTH";
            case "销售出库" -> "XSCK";
            case "销售退货" -> "XSTH";
            case "采购入库" -> "CGRK";
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
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }
        return null;
    }

    private Long getLongCellValue(Cell cell) {
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            return (long) cell.getNumericCellValue();
        }
        return null;
    }

    private Integer getIntegerCellValue(Cell cell) {
        if (cell != null && cell.getCellType() == CellType.STRING) {
            return Integer.parseInt(cell.getStringCellValue());
        }
        return null;
    }
}
