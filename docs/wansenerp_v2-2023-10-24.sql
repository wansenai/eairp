/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : wansenerp_v2

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 24/10/2023 15:07:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户名称',
  `contact` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `phone_number` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机',
  `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `first_quarter_account_receivable` decimal(12, 3) NULL DEFAULT 0.000 COMMENT '一季度应收账款',
  `second_quarter_account_receivable` decimal(12, 3) NULL DEFAULT NULL COMMENT '二季度应收账款',
  `third_quarter_account_receivable` decimal(12, 3) NULL DEFAULT NULL COMMENT '三季度应收账款',
  `fourth_quarter_account_receivable` decimal(12, 3) NULL DEFAULT NULL COMMENT '四季度应收账款',
  `total_account_receivable` decimal(24, 3) NULL DEFAULT NULL COMMENT '累计应收账款',
  `address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '地址',
  `tax_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '纳税人识别号',
  `bank_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `account_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '账号',
  `tax_rate` decimal(24, 3) NULL DEFAULT NULL COMMENT '税率',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态（0-启用，1-停用）默认启用',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (1162569363274858496, 1159563649187053568, '客户1', '赵伟', '178151615', '666666@qq.com', 0.000, 0.000, 0.000, 0.000, 0.000, '111', 'DHC15610555FXITAL1', '中国银行', '', 0.000, 0, '111', 0, '2023-10-14 01:55:36', '2023-10-14 02:02:25', 1159563649187053568, 1159563649187053568, 0);
INSERT INTO `customer` VALUES (1162571026857459712, 1159563649187053568, '万森222', '赵伟', '17715151638', '', 0.000, 0.000, 815.360, 0.000, 815.360, '', '', '', '1231232131231', 0.000, 0, '', 0, '2023-10-14 02:02:12', '2023-10-14 02:02:55', 1159563649187053568, 1159563649187053568, 0);
INSERT INTO `customer` VALUES (1712897693711888385, 1159563649187053568, '客户4444', '小赵', '16621211505', '', 100.000, 0.000, 0.000, 21.850, 121.850, '', '', '', '', 7.000, 1, '', 0, '2023-10-14 02:26:49', '2023-10-14 02:27:24', 1159563649187053568, 1159563649187053568, 0);
INSERT INTO `customer` VALUES (1712897693711888386, 1159563649187053568, '客户3', '小李', '19918181620', '', 8915.000, 0.000, 95.230, 785.320, 9795.550, '', '', '', '', 3.000, 0, '', 0, '2023-10-14 02:26:49', '2023-10-14 02:29:35', 1159563649187053568, 1159563649187053568, 0);
INSERT INTO `customer` VALUES (1713135907563495426, 0, '客户a', '小赵', '16621211505', NULL, 100.000, NULL, NULL, 21.850, 121.850, NULL, NULL, NULL, NULL, 7.000, 0, NULL, NULL, '2023-10-14 18:13:24', NULL, 0, NULL, 0);
INSERT INTO `customer` VALUES (1713135957840617474, 0, '客户b', '小李', '19918181620', NULL, 0.000, NULL, 95.230, NULL, 95.230, NULL, NULL, NULL, NULL, 3.000, 0, NULL, NULL, '2023-10-14 18:13:36', NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for financial_account
-- ----------------------------
DROP TABLE IF EXISTS `financial_account`;
CREATE TABLE `financial_account`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `account_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `account_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '编号',
  `initial_amount` decimal(12, 3) NULL DEFAULT NULL COMMENT '期初金额',
  `current_amount` decimal(12, 3) NULL DEFAULT NULL COMMENT '当前余额',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '启用',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `is_default` tinyint(1) NULL DEFAULT NULL COMMENT '是否默认',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '账户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of financial_account
-- ----------------------------
INSERT INTO `financial_account` VALUES (17, 63, '账户1', 'zzz111', 100.000, 829.000, 'aabb', 1, NULL, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_account` VALUES (18, 63, '账户2', '1234131324', 200.000, -1681.000, 'bbbb', 1, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_account` VALUES (24, NULL, 'aaa', 'aaa', 0.000, NULL, NULL, 1, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_account` VALUES (1713836226953875457, 0, '支付宝', 'ZFB00001', 89165.580, 222232.620, '1', 0, 1, 0, '2023-10-16 16:36:13', '2023-10-16 17:15:43', 0, 0, 0);
INSERT INTO `financial_account` VALUES (1713836732996739074, 0, 'Visa中国银行', '6123151550', 95636.000, 200.580, '1', 0, 2, 0, '2023-10-16 16:38:13', '2023-10-16 17:15:40', 0, 0, 0);
INSERT INTO `financial_account` VALUES (1713851069471657986, 0, '微信支付', 'WX00005', 8623.520, 9500.000, '1', 0, 1, 0, '2023-10-16 17:35:11', '2023-10-16 17:36:09', 0, 0, 0);

-- ----------------------------
-- Table structure for financial_main
-- ----------------------------
DROP TABLE IF EXISTS `financial_main`;
CREATE TABLE `financial_main`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `organization_id` bigint NULL DEFAULT NULL COMMENT '机构id(收款/付款单位)',
  `hands_person_id` bigint NULL DEFAULT NULL COMMENT '经手人id',
  `account_id` bigint NULL DEFAULT NULL COMMENT '账户(收款/付款)',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类型(支出/收入/收款/付款/转账)',
  `change_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '变动金额(优惠/收款/付款/实付)',
  `discount_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '优惠金额',
  `total_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '合计金额',
  `receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单据编号',
  `receipt_source` tinyint(1) NULL DEFAULT 0 COMMENT '单据来源，0-pc，1-手机',
  `receipt_time` datetime NULL DEFAULT NULL COMMENT '单据日期',
  `remark` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `file_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '附件名称',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态，0未审核、1已审核、9审核中',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK9F4C0D8DB610FC06`(`organization_id` ASC) USING BTREE,
  INDEX `FK9F4C0D8DAAE50527`(`account_id` ASC) USING BTREE,
  INDEX `FK9F4C0D8DC4170B37`(`hands_person_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '财务主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of financial_main
-- ----------------------------
INSERT INTO `financial_main` VALUES (118, 63, 58, 16, 17, '收入', 55.000000, NULL, 55.000000, 'SR00000000643', 0, '2021-06-02 00:24:49', NULL, NULL, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_main` VALUES (119, 63, 68, 16, 17, '支出', -66.000000, NULL, -66.000000, 'ZC00000000644', 0, '2021-06-02 00:25:01', NULL, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_main` VALUES (122, 63, NULL, 17, 17, '转账', -11.000000, NULL, -11.000000, 'ZZ00000000647', 0, '2021-06-02 00:25:32', NULL, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_main` VALUES (124, 63, 60, 17, NULL, '收预付款', 80.000000, 0.000000, 80.000000, 'SYF00000000649', 0, '2021-07-06 23:43:48', NULL, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_main` VALUES (125, 63, 58, 17, 17, '收款', 10.000000, 0.000000, 10.000000, 'SK00000000653', 0, '2021-07-06 23:46:38', NULL, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_main` VALUES (126, 63, 57, 17, 17, '付款', -50.000000, 0.000000, -50.000000, 'FK00000000654', 0, '2021-07-06 23:47:23', NULL, NULL, 0, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for financial_sub
-- ----------------------------
DROP TABLE IF EXISTS `financial_sub`;
CREATE TABLE `financial_sub`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `table_header_id` bigint NOT NULL COMMENT '表头Id',
  `account_id` bigint NULL DEFAULT NULL COMMENT '账户Id',
  `income_expense_id` bigint NULL DEFAULT NULL COMMENT '收支项目Id',
  `receipt_id` bigint NULL DEFAULT NULL COMMENT '单据id',
  `accounts_receivable` decimal(24, 6) NULL DEFAULT NULL COMMENT '应收欠款',
  `accounts_received` decimal(24, 6) NULL DEFAULT NULL COMMENT '已收欠款',
  `single_amount` decimal(24, 6) NULL DEFAULT NULL COMMENT '单项金额',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单据备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK9F4CBAC0AAE50527`(`account_id` ASC) USING BTREE,
  INDEX `FK9F4CBAC0C5FE6007`(`table_header_id` ASC) USING BTREE,
  INDEX `FK9F4CBAC0D203EDC5`(`income_expense_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '财务子表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of financial_sub
-- ----------------------------
INSERT INTO `financial_sub` VALUES (143, 63, 118, NULL, 23, NULL, NULL, NULL, 55.000000, '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_sub` VALUES (144, 63, 119, NULL, 21, NULL, NULL, NULL, 66.000000, '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_sub` VALUES (147, 63, 122, 17, NULL, NULL, NULL, NULL, 11.000000, '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_sub` VALUES (149, 63, 124, 17, NULL, NULL, NULL, NULL, 80.000000, '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_sub` VALUES (150, 63, 125, NULL, NULL, 272, 20.000000, 0.000000, 10.000000, '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_sub` VALUES (151, 63, 126, NULL, NULL, 271, 60.000000, 0.000000, -50.000000, '', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for income_expense
-- ----------------------------
DROP TABLE IF EXISTS `income_expense`;
CREATE TABLE `income_expense`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类型',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '启用',
  `sort` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '收支项目' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of income_expense
-- ----------------------------
INSERT INTO `income_expense` VALUES (21, 63, '快递费', '支出', '', 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `income_expense` VALUES (22, 63, '房租收入', '收入', '', 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `income_expense` VALUES (23, 63, '利息收入', '收入', '收入', 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `income_expense` VALUES (28, NULL, 'wansentech', '支出', NULL, 1, '1', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `member_number` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员卡号',
  `member_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员名称',
  `phone_number` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机',
  `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `advance_payment` decimal(12, 3) NULL DEFAULT NULL COMMENT '预付款',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态（0-启用，1-停用）默认启用',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (1713110644611874818, 0, 'MU517161561', '测试会员卡', '18027431919', 'shanxiaozhang@163.com', 851.320, 1, '测试会员', 1, '2023-10-14 16:33:00', '2023-10-14 17:08:29', 0, 0, 0);
INSERT INTO `member` VALUES (1713136000454746114, 0, 'VIP005739952', '小赵', '13000000000', '', 981.000, 0, '', 0, '2023-10-14 18:13:46', '2023-10-14 18:14:27', 0, 0, 0);
INSERT INTO `member` VALUES (1713136000454746115, 0, 'SVIP7186333745', '李哥', '17815151515', '', 792.000, 0, '', 0, '2023-10-14 18:13:46', '2023-10-14 18:14:21', 0, 0, 0);
INSERT INTO `member` VALUES (1713750610601861121, 1159563649187053568, 'MU517161561', '测试会员卡', '18027431919', '666666@qq.com', 8532.150, 0, '', 0, '2023-10-16 10:56:00', NULL, 1159563649187053568, NULL, 0);

-- ----------------------------
-- Table structure for operator
-- ----------------------------
DROP TABLE IF EXISTS `operator`;
CREATE TABLE `operator`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id（预留字段后续考虑加到用户表关联角色）',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类型',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态（0-启用, 1-停用）',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '经手人表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of operator
-- ----------------------------
INSERT INTO `operator` VALUES (14, 63, NULL, '小李', '业务员', 1, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `operator` VALUES (15, 63, NULL, '小军', '仓管员', 1, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `operator` VALUES (16, 63, NULL, '小夏', '财务员', 1, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `operator` VALUES (17, 63, NULL, '小曹', '财务员', 1, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `operator` VALUES (18, NULL, NULL, '赵伟', '业务员', 1, 2, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `operator` VALUES (1713915466231676930, 0, NULL, '小赵', '业务员', 1, 11, '测试', '2023-10-16 21:51:05', '2023-10-16 21:53:27', 0, 0, 0);
INSERT INTO `operator` VALUES (1713915547055915009, 0, NULL, '测试', '财务员', 0, 12, '财务人员', '2023-10-16 21:51:24', '2023-10-16 21:53:05', 0, 0, 0);

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_category_id` bigint NULL DEFAULT NULL COMMENT '产品类型id',
  `product_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `product_manufacturer` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '制造商',
  `product_model` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '型号',
  `product_standard` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '规格',
  `product_color` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '颜色',
  `product_unit_id` bigint NULL DEFAULT NULL COMMENT '计量单位Id',
  `product_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单位-单个',
  `product_expiry_num` int NULL DEFAULT NULL COMMENT '保质期天数',
  `product_weight` decimal(12, 3) NULL DEFAULT NULL COMMENT '基础重量(kg)',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '启用 0-禁用  1-启用',
  `other_field_one` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '自定义1',
  `other_field_two` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '自定义2',
  `other_field_three` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '自定义3',
  `enable_serial_number` tinyint(1) NULL DEFAULT 0 COMMENT '是否开启序列号，0否，1是',
  `enable_batch_number` tinyint(1) NULL DEFAULT 0 COMMENT '是否开启批号，0否，1是',
  `warehouse_shelves` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '仓位货架',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK675951272AB6672C`(`product_category_id` ASC) USING BTREE,
  INDEX `UnitId`(`product_unit_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '产品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1166049114958331904, 0, 1, '香菜', '江苏泰州', '江苏泰州', '有机', '绿色', NULL, '斤', 365, 1.000, '商品有存货', 0, NULL, NULL, NULL, 0, 1, '西安仓库', '2023-10-23 16:39:52', NULL, 0, NULL, 0);
INSERT INTO `product` VALUES (1166083272434778112, 0, 1160291314386862080, 'NVIDIA GPU', 'NVIDIA', 'RTX 4090', '24GB GDDR6X', '绿色', NULL, '张', 185, 5.300, '商品有存货', 0, 'Ray Tracing Cores', '第3代', '3个 PCIe8-pin 转接线（附赠适配器）或 1根支持450W 及更大额定功率的第5代 PCIe 接口电源线', 1, 1, '西安仓库', '2023-10-23 18:38:37', NULL, 0, NULL, 0);
INSERT INTO `product` VALUES (1166154435198451712, 0, NULL, '可乐', NULL, '百事', '250ml', '可乐色', NULL, '瓶', NULL, 5.000, NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, '2023-10-23 23:21:23', NULL, 0, NULL, 0);
INSERT INTO `product` VALUES (1166154435227811840, 0, NULL, '英伟达显卡', NULL, 'RTX 4090', 'GDDR 6X', '黑色', NULL, '张', NULL, 2.000, NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, '2023-10-23 23:21:23', NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for product_attribute
-- ----------------------------
DROP TABLE IF EXISTS `product_attribute`;
CREATE TABLE `product_attribute`  (
  `id` bigint NOT NULL,
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `attribute_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `attribute_value` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '属性值',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '产品属性表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_attribute
-- ----------------------------
INSERT INTO `product_attribute` VALUES (1, 0, '多颜色', '红色|橙色|黄色|绿色|蓝色|紫色', NULL, NULL, '2023-10-08 18:08:50', NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (2, 0, '多尺寸', 'S|M|L|XL|XXL|XXXL', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (3, 0, '自定义1', '小米|华为', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (4, 0, '自定义2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (5, 0, '自定义3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (6, 0, '自定义3', NULL, NULL, NULL, '2023-10-08 18:08:55', NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (7, 0, '属性测试', 'T11|T222|测试', 1, '测试', NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (8, 0, '自定义4', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (9, 0, '自定义66', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (10, 0, '属性测试222', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (11, 0, '自定义8', '313123|213123|dsadasd', NULL, NULL, '2023-10-08 18:08:53', '2023-10-08 18:46:09', NULL, 0, 0);
INSERT INTO `product_attribute` VALUES (1161071494588006400, 0, '111', '阿三大苏打', NULL, NULL, '2023-10-09 22:43:36', NULL, 0, NULL, 0);
INSERT INTO `product_attribute` VALUES (1162520415466160128, 1159563649187053568, '多颜色', '红色|橙色|黄色|绿色|蓝色|紫色', NULL, '颜色', '2023-10-13 14:41:06', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `product_attribute` VALUES (1162520548366876672, 1159563649187053568, '手机型号', '华为|小米|苹果|三星', NULL, NULL, '2023-10-13 14:41:37', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `product_attribute` VALUES (1162520603031240704, 1159563649187053568, '多尺寸', 'S|M|L|XL|XXL|XXXL', NULL, NULL, '2023-10-13 14:41:50', NULL, 1159563649187053568, NULL, 0);

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `category_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `category_number` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '编号',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '上级id',
  `sort` int NULL DEFAULT NULL COMMENT '显示顺序',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK3EE7F725237A77D8`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '产品类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_category
-- ----------------------------
INSERT INTO `product_category` VALUES (1, 0, '蔬菜瓜果', 'HX12113123', NULL, 1, '测试', '2023-10-04 21:42:34', '2023-10-07 15:35:41', NULL, 0, 0);
INSERT INTO `product_category` VALUES (17, 0, '计算机硬件', 'wae12', NULL, 1, 'eee', '2019-04-10 22:18:12', '2023-10-07 15:35:52', NULL, 0, 0);
INSERT INTO `product_category` VALUES (21, 0, 'CPU', 'SN115156188', 17, 3, '硬件设备', '2020-07-20 23:08:44', '2023-10-07 15:35:37', NULL, 0, 0);
INSERT INTO `product_category` VALUES (29, 0, '菠菜', 'SN115156188', 1, 1111, '11', '2023-08-30 14:55:13', '2023-10-07 15:02:10', NULL, 0, 0);
INSERT INTO `product_category` VALUES (2131, 63, '测试水产', 'HX0001', NULL, 1, '111', '2023-08-30 15:27:51', '2023-08-30 15:27:51', NULL, NULL, 0);
INSERT INTO `product_category` VALUES (1159622687736201200, 0, '苹果', '2222', 1, 22, 'test', '2023-10-05 22:46:34', '2023-10-07 15:14:41', 0, 0, 0);
INSERT INTO `product_category` VALUES (1160233844880703488, 0, '测试', '2', NULL, 2, '1', '2023-10-07 15:15:05', '2023-10-07 15:16:59', 0, 0, 1);
INSERT INTO `product_category` VALUES (1160268801896349696, 0, '1111', NULL, NULL, NULL, NULL, '2023-10-07 17:33:59', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160269747464437760, 0, '123213213', NULL, NULL, NULL, NULL, '2023-10-07 17:37:45', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160272129015414784, 0, '撒旦', NULL, NULL, NULL, NULL, '2023-10-07 17:47:12', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160272471975264256, 0, '重新注册', NULL, NULL, NULL, NULL, '2023-10-07 17:48:34', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160275621096456192, 0, '阿斯顿', NULL, NULL, NULL, NULL, '2023-10-07 18:01:05', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160278084553801728, 0, '啊实打实', NULL, NULL, NULL, NULL, '2023-10-07 18:10:52', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160278783773638656, 0, 'sadasdasda', NULL, NULL, NULL, NULL, '2023-10-07 18:13:39', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160278896638164992, 0, 'asdasdasd', NULL, NULL, NULL, NULL, '2023-10-07 18:14:06', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160280667230044160, 0, '啊实打实大苏打', NULL, NULL, NULL, NULL, '2023-10-07 18:21:08', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160280887565221888, 0, '蔬菜瓜果啊实打实', '', NULL, NULL, '', '2023-10-07 18:22:01', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160289302903521280, 0, '啊实打实的', NULL, NULL, NULL, NULL, '2023-10-07 18:55:27', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160289846040723456, 0, '大苹果', 'APP151515', 1159622687736201200, NULL, NULL, '2023-10-07 18:57:36', '2023-10-08 10:34:47', 0, 0, 0);
INSERT INTO `product_category` VALUES (1160291314386862080, 0, 'GPU', 'GPU3060', 17, NULL, NULL, '2023-10-07 19:03:27', '2023-10-08 10:34:56', 0, 0, 0);
INSERT INTO `product_category` VALUES (1160294633922625536, 0, '撒大苏打', NULL, NULL, NULL, NULL, '2023-10-07 19:16:38', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160301285883248640, 0, '撒大苏打撒旦', NULL, NULL, NULL, NULL, '2023-10-07 19:43:04', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160301756370911232, 0, '啊实打实的', '', NULL, NULL, '', '2023-10-07 19:44:56', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1160512619069571072, 0, '内存条', 'Huawei570', 17, NULL, NULL, '2023-10-08 09:42:50', '2023-10-08 10:35:09', 0, 0, 0);
INSERT INTO `product_category` VALUES (1160656505502957568, 0, 'asdasdasd', 'asdasd', NULL, NULL, NULL, '2023-10-08 19:14:35', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1162519685913116672, 1159563649187053568, '瓜果蔬菜', 'GGSC', NULL, 1, NULL, '2023-10-13 14:38:12', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `product_category` VALUES (1162519746885713920, 1159563649187053568, '电子产品', 'DZCP', NULL, NULL, NULL, '2023-10-13 14:38:26', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `product_category` VALUES (1162519891966689280, 1159563649187053568, '英特尔', 'Intel', 1162519746885713920, NULL, '', '2023-10-13 14:39:01', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `product_category` VALUES (1162520031897059328, 1159563649187053568, '英伟达', 'NVIDIA', 1162519746885713920, NULL, NULL, '2023-10-13 14:39:34', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `product_category` VALUES (1162520120057135104, 1159563649187053568, 'GPU', 'GPU3060', 1162520031897059328, NULL, NULL, '2023-10-13 14:39:55', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `product_category` VALUES (1162520205109231616, 1159563649187053568, '菠菜', 'BCOne1', 1162519685913116672, NULL, NULL, '2023-10-13 14:40:15', NULL, 1159563649187053568, NULL, 0);

-- ----------------------------
-- Table structure for product_extend_price
-- ----------------------------
DROP TABLE IF EXISTS `product_extend_price`;
CREATE TABLE `product_extend_price`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品id',
  `product_bar_code` int NULL DEFAULT NULL COMMENT '商品条码',
  `product_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '商品单位',
  `multi_attribute` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '多属性',
  `purchase_price` decimal(12, 3) NULL DEFAULT NULL COMMENT '采购价格',
  `retail_price` decimal(12, 3) NULL DEFAULT NULL COMMENT '零售价格',
  `sale_price` decimal(12, 3) NULL DEFAULT NULL COMMENT '销售价格',
  `low_price` decimal(12, 3) NULL DEFAULT NULL COMMENT '最低售价',
  `default_flag` tinyint(1) NULL DEFAULT 1 COMMENT '是否为默认单位，1是，0否',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '产品价格扩展' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of product_extend_price
-- ----------------------------
INSERT INTO `product_extend_price` VALUES (1166049115008663552, 0, 1166049114958331904, 1001, '斤', NULL, 0.850, 1.820, 1.830, 1.800, 1, '2023-10-23 16:39:52', NULL, 0, NULL, 0);
INSERT INTO `product_extend_price` VALUES (1166083272451555328, 0, 1166083272434778112, 1005, '张', NULL, 13599.000, 14999.000, 23000.000, 21580.000, 1, '2023-10-23 18:38:37', NULL, 0, NULL, 0);
INSERT INTO `product_extend_price` VALUES (1166083272451555329, 0, 1166083272434778112, 1006, '张', NULL, 13599.000, 14999.000, 23000.000, 21580.000, 1, '2023-10-23 18:38:37', NULL, 0, NULL, 0);
INSERT INTO `product_extend_price` VALUES (1166154435198451713, 0, 1166154435198451712, 1007, NULL, NULL, 5.000, 12.000, 10.000, 9.000, 1, '2023-10-23 23:21:23', NULL, 0, NULL, 0);
INSERT INTO `product_extend_price` VALUES (1166154435227811841, 0, 1166154435227811840, 1008, NULL, NULL, 13599.000, 14999.000, 23000.000, 21580.000, 1, '2023-10-23 23:21:23', NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for product_extend_property
-- ----------------------------
DROP TABLE IF EXISTS `product_extend_property`;
CREATE TABLE `product_extend_property`  (
  `id` bigint NOT NULL COMMENT '主键',
  `native_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '原始名称',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '是否启用',
  `another_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '别名',
  `sort` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '产品扩展字段表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_extend_property
-- ----------------------------
INSERT INTO `product_extend_property` VALUES (1, '制造商', 1, '制造商', '01', NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_property` VALUES (2, '自定义1', 1, '自定义1', '02', NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_property` VALUES (3, '自定义2', 1, '自定义2', '03', NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_property` VALUES (4, '自定义3', 1, '自定义3', '04', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for product_image
-- ----------------------------
DROP TABLE IF EXISTS `product_image`;
CREATE TABLE `product_image`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品id',
  `uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片上传的uid',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片类型（png jpg jpeg）',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片上传状态',
  `image_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片名称',
  `image_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片地址',
  `image_size` int NULL DEFAULT NULL COMMENT '图片大小 mb',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_image
-- ----------------------------
INSERT INTO `product_image` VALUES (1166053388077498368, 0, 1166049114958331904, '__AUTO__1698050388800_0__', NULL, 'done', 'a61e1eb250fca44b.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1166048982808395776_a61e1eb250fca44b.jpg', NULL, '2023-10-23 16:39:52', NULL, 0, NULL, 0);
INSERT INTO `product_image` VALUES (1166083272568995840, 0, 1166083272434778112, 'vc-upload-1698056359845-3', 'image/jpeg', 'done', '4444.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1166082846599675904_4444.jpg', 65229, '2023-10-23 18:38:37', NULL, 0, NULL, 0);
INSERT INTO `product_image` VALUES (1166083272568995841, 0, 1166083272434778112, 'vc-upload-1698056359845-5', 'image/jpeg', 'done', '13ba1c60bcf7da5e9533ad1c6e82955545ad6524.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1166082869416689664_13ba1c60bcf7da5e9533ad1c6e82955545ad6524.jpg', 320110, '2023-10-23 18:38:37', NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for product_stock
-- ----------------------------
DROP TABLE IF EXISTS `product_stock`;
CREATE TABLE `product_stock`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NULL DEFAULT NULL COMMENT '产品id',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库id',
  `init_stock_quantity` decimal(12, 3) NULL DEFAULT NULL COMMENT '初始库存数量',
  `low_stock_quantity` decimal(12, 3) NULL DEFAULT NULL COMMENT '最低库存数量',
  `high_stock_quantity` decimal(12, 3) NULL DEFAULT NULL COMMENT '最高库存数量',
  `current_stock_quantity` decimal(12, 3) NULL DEFAULT NULL COMMENT '当前库存数量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '产品初始库存' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of product_stock
-- ----------------------------
INSERT INTO `product_stock` VALUES (1166049115046412288, 0, 1166049114958331904, 1163491458020278272, 500.000, 100.000, 300.000, 500.000, '2023-10-23 16:39:52', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1166049115046412289, 0, 1166049114958331904, 1163492331714772992, 500.000, 100.000, 400.000, 500.000, '2023-10-23 16:39:52', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1166083272510275584, 0, 1166083272434778112, 1163491458020278272, 38.000, 200.000, 300.000, 38.000, '2023-10-23 18:38:37', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1166083272514469888, 0, 1166083272434778112, 1163492331714772992, 75.000, 200.000, 300.000, 75.000, '2023-10-23 18:38:37', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1166154435198451714, 0, 1166154435198451712, 1163491458020278272, 10.000, NULL, NULL, 10.000, '2023-10-23 23:21:23', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1166154435227811842, 0, 1166154435227811840, 1163491458020278272, 20.000, NULL, NULL, 20.000, '2023-10-23 23:21:23', NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for product_unit
-- ----------------------------
DROP TABLE IF EXISTS `product_unit`;
CREATE TABLE `product_unit`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `compute_unit` varchar(300) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '计量单位，计算得出',
  `basic_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '基础单位',
  `other_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '副单位',
  `other_unit_two` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '副单位2',
  `other_unit_three` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '副单位3',
  `ratio` decimal(24, 3) NULL DEFAULT NULL COMMENT '比例',
  `ratio_two` decimal(24, 3) NULL DEFAULT NULL COMMENT '比例2',
  `ratio_three` decimal(24, 3) NULL DEFAULT NULL COMMENT '比例3',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '启用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '多单位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_unit
-- ----------------------------
INSERT INTO `product_unit` VALUES (15, 0, '个/(箱=12个)', '个', '箱', NULL, NULL, 12.000, NULL, NULL, 0, '2023-10-12 23:35:47', NULL, NULL, NULL, 0);
INSERT INTO `product_unit` VALUES (19, 0, '个/(盒=15个)', '个', '盒', NULL, NULL, 15.000, NULL, NULL, 1, '2023-10-11 23:35:44', NULL, NULL, NULL, 0);
INSERT INTO `product_unit` VALUES (20, 0, '盒/(箱=8盒)', '盒', '箱', NULL, NULL, 8.000, NULL, NULL, 1, '2023-10-17 23:35:49', NULL, NULL, NULL, 0);
INSERT INTO `product_unit` VALUES (21, 0, '瓶/(箱=12瓶)', '瓶', '箱', NULL, NULL, 12.000, NULL, NULL, 0, '2023-10-20 23:35:51', NULL, NULL, NULL, 0);
INSERT INTO `product_unit` VALUES (1160733719233822720, 0, '瓶/(箱=10瓶)/(中箱=24.018瓶)/(很大箱=36瓶)', '瓶', '箱', '中箱', '很大箱', 10.000, 24.018, 36.000, 0, '2023-10-09 00:21:24', '2023-10-09 01:13:58', 0, 0, 0);
INSERT INTO `product_unit` VALUES (1160747104402931712, 0, '袋/(小袋子=3袋)/(中袋子=9袋)/(大袋子=12袋)', '袋', '小袋子', '中袋子', '大袋子', 3.000, 9.000, 12.000, 0, '2023-10-09 01:14:35', '2023-10-09 01:15:32', 0, 0, 0);
INSERT INTO `product_unit` VALUES (1162520735537692672, 1159563649187053568, '瓶/(小箱=6瓶)/(中箱=12瓶)/(大箱=24瓶)', '瓶', '小箱', '中箱', '大箱', 6.000, 12.000, 24.000, 0, '2023-10-13 14:42:22', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `product_unit` VALUES (1162520946993528832, 1159563649187053568, '批发袋/(批发小袋=200批发袋)/(批发中袋=500批发袋)/(批发超大袋=985.32批发袋)', '批发袋', '批发小袋', '批发中袋', '批发超大袋', 200.000, 500.000, 985.320, 0, '2023-10-13 14:43:12', NULL, 1159563649187053568, NULL, 0);

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `supplier_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '供应商名称',
  `contact` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_number` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_system` tinyint NULL DEFAULT NULL COMMENT '是否系统自带 0==系统 1==非系统',
  `type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类型',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态（0-启用，1-停用）默认启用',
  `first_quarter_account_payment` decimal(12, 3) NULL DEFAULT NULL COMMENT '一季度应付账款',
  `second_quarter_account_payment` decimal(12, 3) NULL DEFAULT NULL COMMENT '二季度应付账款',
  `third_quarter_account_payment` decimal(12, 3) NULL DEFAULT NULL COMMENT '三季度应付账款',
  `fourth_quarter_account_payment` decimal(12, 3) NULL DEFAULT NULL COMMENT '四季度应付账款',
  `total_account_payment` decimal(24, 3) NULL DEFAULT NULL COMMENT '累计应付账款',
  `fax` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '传真',
  `phone_number` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机',
  `address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '地址',
  `tax_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '纳税人识别号',
  `bank_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `account_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '账号',
  `tax_rate` decimal(24, 3) NULL DEFAULT NULL COMMENT '税率',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of supplier
-- ----------------------------
INSERT INTO `supplier` VALUES (1712724937206738945, 0, '伺服电机供应商', '赵伟', '021-6714891', NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0.000, NULL, '16621211605', NULL, '1', NULL, NULL, NULL, NULL, '2023-10-13 07:00:21', '2023-10-13 07:00:34', 0, 0, 0);
INSERT INTO `supplier` VALUES (1712724937252876290, 0, '六轴齿轮供应商', '小伟', '021-78151562', NULL, NULL, NULL, NULL, 0, 180.000, NULL, NULL, NULL, 180.000, NULL, '16621211605', NULL, '1', NULL, NULL, 2.000, NULL, '2023-10-13 07:00:21', '2023-10-14 18:12:30', 0, 0, 0);
INSERT INTO `supplier` VALUES (1712724937257070594, 0, '软件供应商', '闻兴珍', '13379362915', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, 0.000, NULL, '16621211605', NULL, '1', NULL, NULL, NULL, NULL, '2023-10-13 07:00:21', NULL, 0, NULL, 0);
INSERT INTO `supplier` VALUES (1712839652868173826, 1159563649187053568, '天津永胜食品有限公司', '王永胜', '021-1781516', 'shanxiaozhang@163.com', NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, 1757.000, NULL, '18027431919', NULL, 'DHC15610555FXITAL1', '中国银行', NULL, NULL, NULL, '2023-10-13 14:36:11', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `supplier` VALUES (1712842191592304642, 1159563649187053568, '万森（陕西）机器人有限公司', '赵伟', '16621211605', 'team@wansenai.com', NULL, NULL, NULL, 0, 850.000, NULL, NULL, NULL, 20466.250, NULL, '16621211605', '西安软件新城研发基地二期', '91610131MAC9KMEQ8J', '中国农业银行(西安科技路中段支行)', NULL, 3.000, NULL, '2023-10-13 14:46:16', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `supplier` VALUES (1713135795982426114, 0, '供应商a', '小赵', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, 0.000, NULL, '17818156216', NULL, NULL, 'VISA国际', '69181665523', 3.000, NULL, '2023-10-14 18:12:57', NULL, 0, NULL, 0);
INSERT INTO `supplier` VALUES (1713135795982426115, 0, '供应商b', '小张', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 500.000, NULL, 500.000, NULL, '19991915192', NULL, NULL, '中国银行', '617819815222', 7.000, NULL, '2023-10-14 18:12:57', NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `company_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `company_contact` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '公司联系人',
  `company_address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '公司地址',
  `company_phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '公司电话',
  `company_fax` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '公司传真',
  `company_post_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '公司邮编',
  `sale_agreement` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '销售协议',
  `warehouse_status` tinyint(1) NULL DEFAULT 0 COMMENT '仓库启用标记，0未启用，1启用',
  `customer_status` tinyint(1) NULL DEFAULT 0 COMMENT '客户启用标记，0未启用，1启用',
  `minus_stock_status` tinyint(1) NULL DEFAULT 0 COMMENT '负库存启用标记，0未启用，1启用',
  `purchase_by_sale_status` tinyint(1) NULL DEFAULT 0 COMMENT '以销定购启用标记，0未启用，1启用',
  `multi_level_approval_status` tinyint(1) NULL DEFAULT 0 COMMENT '多级审核启用标记，0未启用，1启用',
  `process_type` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '流程类型，可多选',
  `force_approval_status` tinyint(1) NULL DEFAULT 0 COMMENT '强审核启用标记，0未启用，1启用',
  `update_unit_price_status` tinyint(1) NULL DEFAULT 1 COMMENT '更新单价启用标记，0未启用，1启用',
  `over_link_bill_status` tinyint(1) NULL DEFAULT 0 COMMENT '超出关联单据启用标记，0未启用，1启用',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统参数' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (11, 0, '万森（陕西）机器人有限公司', '赵伟', '陕西省西安市高新区软件新城A6', '16621211605', NULL, NULL, '注：本单为我公司与客户约定账期内结款的依据，由客户或其单位员工签字生效，并承担法律责任。', 0, 0, 1, 0, 0, '', 0, 1, 0, 0);

-- ----------------------------
-- Table structure for sys_department
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级部门id',
  `number` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '部门编号',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '部门简称',
  `leader` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '部门负责任人',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态 0启用，1停用 默认启用',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `sort` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '部门显示顺序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '机构表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_department
-- ----------------------------
INSERT INTO `sys_department` VALUES (1154490573429018634, 0, 1154756575114956805, '1154490573429018634', '技术团队', NULL, 0, NULL, '2', '2023-09-21 18:53:51', '2023-09-21 18:53:48', NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1154756575114956805, 0, NULL, '1154756575114956805', '万森智能部门', '赵伟', 0, '硬件设备', '1', '2023-06-23 12:53:31', NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1154794589170044930, 0, 1154794589174239277, '1154794589170044930', '硬件研发团队', '李楚德', 0, NULL, '3', '2023-01-18 17:53:36', NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1154794589174239242, 0, 1154794589174239277, '1154794589174239242', '销售团队', '王友德', 0, NULL, '2', '2023-09-13 02:53:42', NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1154794589174239268, 0, 1154756575114956805, '1154794589174239268', '运营团队', '张峰', 0, NULL, '2', '2023-09-16 08:53:47', NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1154794589174239277, 0, NULL, '1154794589174239277', '万森机器人', '赵伟', 0, '智能机器人', '2', '2019-07-25 11:53:52', NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1157397928067727360, 0, 5574799175374231982, '1157397928067727360', '法务办公室', 'James', 1, NULL, '1', '2023-09-29 19:26:09', '2023-09-29 19:26:27', NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1159563040610320384, 0, 1154794589174239277, '测试', '测试', '赵伟', 1, NULL, NULL, '2023-10-05 18:49:33', '2023-10-05 18:49:39', NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1159563649187053570, 1159563649187053568, NULL, 'DT0000', '默认部门', '1159563649187053568', 0, '租户注册后的默认部门 该部门为父级部门', NULL, '2023-10-05 18:51:58', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `sys_department` VALUES (1160275990509780992, 0, NULL, '测试', '测试', '', 0, '', NULL, '2023-10-07 18:02:33', NULL, NULL, NULL, 1);
INSERT INTO `sys_department` VALUES (1160276228167434240, 0, NULL, '啊实打实的', '啊实打实的', '1111', 0, '11', '11', '2023-10-07 18:03:30', NULL, NULL, NULL, 1);
INSERT INTO `sys_department` VALUES (1160277580603981824, 0, NULL, '阿斯顿萨达', '阿斯顿萨达', NULL, 0, NULL, NULL, '2023-10-07 18:08:52', NULL, NULL, NULL, 1);
INSERT INTO `sys_department` VALUES (1160278001280090112, 0, NULL, '啊实打实的', '啊实打实的', NULL, 0, NULL, NULL, '2023-10-07 18:10:32', NULL, NULL, NULL, 1);
INSERT INTO `sys_department` VALUES (1160291544184389632, 0, NULL, '啊实打实打算', '啊实打实打算', '111', 0, NULL, NULL, '2023-10-07 19:04:21', NULL, NULL, NULL, 1);
INSERT INTO `sys_department` VALUES (1160296114805538816, 0, NULL, '撒大苏打', '撒大苏打', NULL, 0, NULL, NULL, '2023-10-07 19:22:31', NULL, NULL, NULL, 1);
INSERT INTO `sys_department` VALUES (1160296275728400384, 0, 1160296114805538816, '撒大苏打撒旦', '撒大苏打撒旦', NULL, 0, NULL, NULL, '2023-10-07 19:23:09', NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1160318685722705920, 0, NULL, '啊实打实大声道', '啊实打实大声道', NULL, 0, NULL, NULL, '2023-10-07 20:52:12', NULL, NULL, NULL, 1);
INSERT INTO `sys_department` VALUES (1160321457767579648, 0, NULL, '测试部门', '测试部门', NULL, 0, NULL, NULL, '2023-10-07 21:03:13', NULL, NULL, NULL, 1);
INSERT INTO `sys_department` VALUES (1160323505707810816, 0, NULL, '啊实打实大师', '啊实打实大师', NULL, 0, NULL, NULL, '2023-10-07 21:11:22', NULL, NULL, NULL, 1);
INSERT INTO `sys_department` VALUES (1160327420079767552, 0, NULL, '2222', '2222', NULL, 0, NULL, NULL, '2023-10-07 21:26:55', NULL, NULL, NULL, 1);
INSERT INTO `sys_department` VALUES (1160511794343575552, 0, NULL, '是大大大', '是大大大', 'asda', 0, NULL, NULL, '2023-10-08 09:39:33', NULL, NULL, NULL, 1);
INSERT INTO `sys_department` VALUES (1162519348091289600, 1159563649187053568, 1159563649187053570, '技术团队', '技术团队', '测试', 0, NULL, NULL, '2023-10-13 14:36:51', NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1162519486230691840, 1159563649187053568, NULL, '西安分公司', '西安分公司', '赵伟', 0, '分公司测试', '3', '2023-10-13 14:37:24', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `operate_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '操作模块名称',
  `client_ip` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '客户端IP',
  `status` tinyint NULL DEFAULT NULL COMMENT '操作状态 0==成功，1==失败',
  `content` varchar(5000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '详情',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKF2696AA13E226853`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '操作日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (7559, 63, 63, '用户', '127.0.0.1/127.0.0.1', 0, '登录jsh', '2023-08-30 14:50:36', NULL);
INSERT INTO `sys_log` VALUES (7560, NULL, 63, '商品类型', '127.0.0.1/127.0.0.1', 0, '新增海鲜水产', '2023-08-30 14:55:13', NULL);
INSERT INTO `sys_log` VALUES (7561, NULL, 63, '商品类型', '127.0.0.1/127.0.0.1', 0, '新增测试水产', '2023-08-30 15:27:51', NULL);
INSERT INTO `sys_log` VALUES (7562, NULL, 63, '商品', '127.0.0.1/127.0.0.1', 0, '新增wansentech', '2023-08-30 15:30:06', NULL);
INSERT INTO `sys_log` VALUES (7563, 0, 120, '用户', '127.0.0.1/127.0.0.1', 0, '登录admin', '2023-08-30 15:33:52', NULL);
INSERT INTO `sys_log` VALUES (7564, 63, 63, '用户', '127.0.0.1/127.0.0.1', 0, '登录jsh', '2023-08-30 15:37:57', NULL);
INSERT INTO `sys_log` VALUES (7565, 63, 63, '用户', '127.0.0.1/127.0.0.1', 0, '登录jsh', '2023-08-30 15:38:14', NULL);
INSERT INTO `sys_log` VALUES (7566, 0, 120, '用户', '127.0.0.1/127.0.0.1', 0, '登录admin', '2023-08-30 15:38:30', NULL);
INSERT INTO `sys_log` VALUES (7567, 63, 63, '用户', '127.0.0.1/127.0.0.1', 0, '登录jsh', '2023-08-30 15:39:12', NULL);
INSERT INTO `sys_log` VALUES (7568, NULL, 63, '商家', '127.0.0.1/127.0.0.1', 0, '新增供应商666', '2023-08-30 15:40:47', NULL);
INSERT INTO `sys_log` VALUES (7569, NULL, 63, '关联关系', '127.0.0.1/127.0.0.1', 0, '修改', '2023-08-30 15:41:05', NULL);
INSERT INTO `sys_log` VALUES (7570, NULL, 63, '商家', '127.0.0.1/127.0.0.1', 0, '新增0724198719', '2023-08-30 15:41:05', NULL);
INSERT INTO `sys_log` VALUES (7571, NULL, 63, '商家', '127.0.0.1/127.0.0.1', 0, '新增0724198719', '2023-08-30 15:41:13', NULL);
INSERT INTO `sys_log` VALUES (7572, NULL, 63, '关联关系', '127.0.0.1/127.0.0.1', 0, '修改', '2023-08-30 15:41:26', NULL);
INSERT INTO `sys_log` VALUES (7573, NULL, 63, '仓库', '127.0.0.1/127.0.0.1', 0, '新增仓库666', '2023-08-30 15:41:26', NULL);
INSERT INTO `sys_log` VALUES (7574, NULL, 63, '收支项目', '127.0.0.1/127.0.0.1', 0, '新增wansentech', '2023-08-30 15:41:34', NULL);
INSERT INTO `sys_log` VALUES (7575, NULL, 63, '账户', '127.0.0.1/127.0.0.1', 0, '新增aaa', '2023-08-30 15:41:39', NULL);
INSERT INTO `sys_log` VALUES (7576, NULL, 63, '经手人', '127.0.0.1/127.0.0.1', 0, '新增赵伟', '2023-08-30 15:41:53', NULL);
INSERT INTO `sys_log` VALUES (7577, NULL, 63, '关联关系', '127.0.0.1/127.0.0.1', 0, '修改', '2023-08-30 15:42:32', NULL);
INSERT INTO `sys_log` VALUES (7578, NULL, 63, '关联关系', '127.0.0.1/127.0.0.1', 0, '修改角色的按钮权限', '2023-08-30 15:43:12', NULL);
INSERT INTO `sys_log` VALUES (7579, NULL, 63, '系统配置', '127.0.0.1/127.0.0.1', 0, '修改万森（陕西）机器人有限公司', '2023-08-30 15:44:11', NULL);
INSERT INTO `sys_log` VALUES (7580, 63, 63, '用户', '127.0.0.1/127.0.0.1', 0, '登录jsh', '2023-08-30 15:44:21', NULL);
INSERT INTO `sys_log` VALUES (7581, NULL, 63, '单据', '127.0.0.1/127.0.0.1', 0, '新增LSCK00000000663', '2023-08-30 18:09:51', NULL);
INSERT INTO `sys_log` VALUES (7582, NULL, 63, '单据', '127.0.0.1/127.0.0.1', 0, '修改LSCK00000000663', '2023-08-30 18:10:11', NULL);
INSERT INTO `sys_log` VALUES (7583, NULL, 63, '单据', '127.0.0.1/127.0.0.1', 0, '删除[LSCK00000000663]', '2023-08-30 18:10:30', NULL);
INSERT INTO `sys_log` VALUES (7584, NULL, 63, '单据', '127.0.0.1/127.0.0.1', 0, '新增LSTH00000000665', '2023-08-30 18:11:10', NULL);
INSERT INTO `sys_log` VALUES (7585, NULL, 63, '单据', '127.0.0.1/127.0.0.1', 0, '修改LSTH00000000637', '2023-08-30 18:11:22', NULL);
INSERT INTO `sys_log` VALUES (7586, NULL, 63, '单据', '127.0.0.1/127.0.0.1', 0, '新增CGDD00000000666', '2023-08-30 18:12:02', NULL);
INSERT INTO `sys_log` VALUES (7587, 146, 146, '用户', '127.0.0.1/127.0.0.1', 0, '登录test66', '2023-08-30 18:13:26', NULL);
INSERT INTO `sys_log` VALUES (7588, 63, 63, '用户', '127.0.0.1/127.0.0.1', 0, '登录wansen', '2023-09-01 23:56:12', NULL);
INSERT INTO `sys_log` VALUES (7589, 63, 63, '用户', '127.0.0.1/127.0.0.1', 0, '登录wansen', '2023-09-01 23:56:45', NULL);
INSERT INTO `sys_log` VALUES (7590, NULL, 63, '单据', '127.0.0.1/127.0.0.1', 0, '修改DBCK00000000640', '2023-09-01 23:58:48', NULL);
INSERT INTO `sys_log` VALUES (7591, 63, 63, '用户', '127.0.0.1/127.0.0.1', 0, '登录wansen', '2023-09-02 00:09:58', NULL);
INSERT INTO `sys_log` VALUES (7592, 63, 63, '用户', '127.0.0.1/127.0.0.1', 0, '登录wansen', '2023-09-02 13:39:48', NULL);
INSERT INTO `sys_log` VALUES (7593, NULL, 63, '单据', '127.0.0.1/127.0.0.1', 0, '新增CGTH00000000668', '2023-09-02 13:40:06', NULL);
INSERT INTO `sys_log` VALUES (7594, NULL, 63, '单据', '127.0.0.1/127.0.0.1', 0, '修改CGTH00000000632', '2023-09-02 13:40:14', NULL);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '标题（菜单显示）',
  `parent_id` int NULL DEFAULT NULL COMMENT '父级菜单id',
  `menu_type` int NULL DEFAULT NULL COMMENT '类型',
  `path` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '链接',
  `component` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '组件',
  `redirect` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '重定向地址',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态（0-启用，1-停用）',
  `icon` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '图标',
  `hide_menu` tinyint(1) NULL DEFAULT 0 COMMENT '隐藏路由不在菜单显示',
  `blank` tinyint(1) NULL DEFAULT NULL COMMENT '是否外链(target = _blank)',
  `hide_breadcrumb` tinyint(1) NULL DEFAULT 0 COMMENT '隐藏该路由在面包屑上面的显示',
  `ignore_keep_alive` tinyint(1) NULL DEFAULT 0 COMMENT '是否忽略KeepAlive缓存',
  `hide_tab` tinyint(1) NULL DEFAULT 0 COMMENT '隐藏路由不在标签页显示',
  `carry_param` tinyint(1) NULL DEFAULT 0 COMMENT '如果该路由会携带参数，且需要在tab页上面显示。则需要设置为true',
  `hide_children_in_menu` tinyint(1) NULL DEFAULT 0 COMMENT '隐藏所有子菜单',
  `affix` tinyint(1) NULL DEFAULT 0 COMMENT '是否固定标签',
  `frameSrc` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '内嵌iframe的地址',
  `realPath` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '动态路由的实际Path, 即去除路由的动态部分;',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `url`(`path` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 287 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '功能模块表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 'Dashboard', '首页', 0, 1, '/dashboard', '/dashboard/analysis/index', NULL, 1, 0, 'ant-design:dashboard-outlined', 0, NULL, 0, 0, 0, 0, 0, 1, NULL, NULL, '2023-06-23 14:36:55', '2023-09-30 18:46:44', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (2, 'workbench', '工作台', 0, 1, '/dashboard/workbench', '/dashboard/workbench/index', NULL, 2, 0, 'ant-design:home-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-02 16:01:53', '2023-10-15 01:14:24', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (3, 'RetailManagement', '零售管理', 0, 0, '/fms/file', 'LAYOUT', NULL, 3, 0, 'ant-design:folder-open-outlined', 0, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-08-07 14:36:50', '2023-09-30 18:44:57', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (4, 'SystemManagement', '系统管理', 0, 0, '/sys', 'LAYOUT', NULL, 1, 0, 'ant-design:setting-outlined', 0, NULL, 0, 0, 0, 0, 0, 0, '', '', '2023-09-30 14:36:33', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (5, 'PurchaseManagement', '采购管理', 0, 0, '/purchase', 'LAYOUT', NULL, 1, 0, 'ant-design:retweet-outlined', 0, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-10-02 14:39:13', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (6, 'SaleManagement', '销售管理', 0, 0, '/sale', 'LAYOUT', NULL, 1, 0, 'ant-design:shop-outlined', 0, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-09-30 14:39:29', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (7, 'WarehouseManagement', '仓库管理', 0, 0, '/warehouse', 'LAYOUT', NULL, 1, 0, 'ant-design:bank-outlined', 0, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-10-02 14:39:15', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (8, 'FinancialManagement', '财务管理', 0, 0, '/financial', 'LAYOUT', NULL, 1, 0, 'ant-design:transaction-outlined', 0, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-10-02 14:39:18', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (9, 'Reports', '报表查询', 0, 0, '/reports', 'LAYOUT', NULL, 1, 0, 'ant-design:pie-chart-outlined', 0, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-09-30 14:39:25', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (10, 'ProductManagement', '商品管理', 0, 0, '/product', 'LAYOUT', NULL, 1, 0, 'ant-design:shopping-outlined', 0, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-10-04 14:39:20', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (11, 'BasicInformation', '基本资料', 0, 0, '/basic', 'LAYOUT', NULL, 1, 0, 'ant-design:appstore-outlined', 0, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-10-01 14:39:22', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (12, 'RoleManagement', '角色管理', 4, 1, '/role', '/sys/role/index', NULL, 1, 0, 'ant-design:solution-outlined', 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-09-20 14:36:37', '2023-10-04 21:32:47', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (13, 'UserManagement', '用户管理', 4, 1, '/user', '/sys/user/index', NULL, 2, 0, 'ant-design:user-outlined', 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-08-25 14:36:39', '2023-10-04 21:33:04', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (14, 'DepartmentManagement', '部门管理', 4, 1, '/department', '/sys/department/index', NULL, 3, 0, 'ic:outline-people-alt', 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-10-04 14:36:43', '2023-10-04 21:32:53', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (15, 'MenuManagement', '菜单管理', 4, 1, '/menu', '/sys/menu/index', NULL, 4, 0, 'ant-design:menu-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-09-13 14:36:47', '2023-10-04 21:32:58', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 'ProductCategory', '商品类别', 10, 1, '/product/category', '/product/category/index', NULL, 1, 0, 'ant-design:share-alt-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-02 15:06:55', '2023-10-04 17:31:45', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 'ProductInfo', '商品信息', 10, 1, '/product/info', '/product/info/index', NULL, 1, 0, 'ant-design:rocket-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-16 22:49:18', '2023-10-16 22:49:20', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, 'ProductAttribute', '商品属性', 10, 1, '/product/attributes', '/product/attributes/index', NULL, 2, 0, 'ant-design:tags-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-08 14:05:43', '2023-10-08 14:07:38', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, 'ProductUnit', '计量单位', 10, 1, '/product/units', '/product/units/index', NULL, 3, 0, 'ant-design:percentage-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-08 22:38:05', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, 'SupplierInformation', '供应商信息', 11, 1, '/basic/supplier', '/basic/supplier/index', NULL, 1, 0, 'ant-design:taobao-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-09 15:26:40', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, 'customerInformation', '客户信息', 11, 1, '/basic/customer', '/basic/customer/index', NULL, 2, 0, 'ant-design:team-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-09 15:27:59', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, 'MemberInformation', '会员信息', 11, 1, '/basic/member', '/basic/member/index', NULL, 3, 0, 'ant-design:user-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-09 15:29:36', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, 'warehouseInformation', '仓库信息', 11, 1, '/basic/warehouse', '/basic/warehouse/index', NULL, 4, 0, 'ant-design:home-filled', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-09 15:31:20', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, 'SettlementAccount', '结算账户', 11, 1, '/basic/settlement-account', '/basic/settlement-account/index', NULL, 5, 0, 'ant-design:pay-circle-filled', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-09 15:32:56', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (25, 'wallahManagement', '经手人管理', 11, 1, '/basic/operator', '/basic/operator/index', NULL, 6, 0, 'ant-design:pushpin-twotone', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-09 15:34:09', '2023-10-16 21:47:58', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (26, '/gpt', 'GPT AI微调模型', 0, 0, '/gpt', 'LAYOUT', NULL, 1, 0, 'ant-design:rocket-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-09 15:35:34', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_msg
-- ----------------------------
DROP TABLE IF EXISTS `sys_msg`;
CREATE TABLE `sys_msg`  (
  `id` bigint NOT NULL COMMENT '主键',
  `msg_title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '消息标题',
  `msg_content` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '消息内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '消息类型',
  `user_id` bigint NULL DEFAULT NULL COMMENT '接收人id',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态，1未读 2已读',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `delete_Flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '消息表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_msg
-- ----------------------------
INSERT INTO `sys_msg` VALUES (2, '标题1', '内容1', '2019-09-10 00:11:39', '类型1', 63, 2, 63, 0);

-- ----------------------------
-- Table structure for sys_platform_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_platform_config`;
CREATE TABLE `sys_platform_config`  (
  `id` bigint NOT NULL,
  `platform_key` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '关键词',
  `platform_key_info` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '关键词名称',
  `platform_value` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '值',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '平台参数' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_platform_config
-- ----------------------------
INSERT INTO `sys_platform_config` VALUES (1, 'platform_name', '平台名称', 'Wan Sen ERP', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (2, 'activation_code', '激活码', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (3, 'platform_url', '官方网站', 'http://wansenai.com/', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (4, 'bill_print_flag', '三联打印启用标记', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (5, 'bill_print_url', '三联打印地址', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (6, 'pay_fee_url', '租户续费地址', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (7, 'register_flag', '注册启用标记', '1', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (8, 'app_activation_code', '手机端激活码', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (9, 'send_workflow_url', '发起流程地址', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (10, 'weixinUrl', '微信url', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (11, 'weixinAppid', '微信appid', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (12, 'weixinSecret', '微信secret', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (13, 'aliOss_endpoint', '阿里OSS-endpoint', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (14, 'aliOss_accessKeyId', '阿里OSS-accessKeyId', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (15, 'aliOss_accessKeySecret', '阿里OSS-accessKeySecret', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (16, 'aliOss_bucketName', '阿里OSS-bucketName', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (17, 'aliOss_linkUrl', '阿里OSS-linkUrl', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (18, 'bill_excel_url', '单据Excel地址', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (19, 'tencent_sms_secret_id', '腾讯短信服务SId', 'AKIDOegXR8brtjLitqZ6SaT8YmaHkzMWS9dv', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (20, 'tencent_sms_secret_key', '腾讯短信服务SKey', '94nGBtNfjZCdsthJm385pNkvvlRvGNI0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (21, 'tencent_sms_client', '腾讯短信服务地区', 'ap-shanghai', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (22, 'tencent_sms_sdk_appId', '腾讯短信服务SDK', '1400856421', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (23, 'tencent_oss_secret_id', '腾讯对象存储SId', 'AKIDOegXR8brtjLitqZ6SaT8YmaHkzMWS9dv', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (24, 'tencent_oss_secret_key', '腾讯对象存储Skey', '94nGBtNfjZCdsthJm385pNkvvlRvGNI0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (25, 'tencent_oss_region', '腾讯对象存储服务地区', 'ap-shanghai', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (26, 'tencent_oss_bucket', '腾讯对象存储桶', 'wansen-1317413588', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `role_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类型',
  `price_limit` tinyint(1) NULL DEFAULT NULL COMMENT '价格屏蔽 1-屏蔽采购价 2-屏蔽零售价 3-屏蔽销售价',
  `description` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态（0-启用，1-停用）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (0, 0, '管理员', '全部数据', 1, '管理员数据', 0, '2023-09-25 19:51:51', '2023-09-26 15:18:41', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (1, 0, '租户', '全部数据', 1, '通用数据', 0, '2023-09-25 19:51:47', '2023-09-26 15:19:20', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (1159563649187053569, 1159563649187053568, '租户管理员', '全部数据', NULL, '租户注册后的默认角色 租户管理员有所有权限', 0, '2023-10-05 18:51:58', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `sys_role` VALUES (1159564417168310272, 1159563649187053568, '测试角色', '全部数据', NULL, '', 0, '2023-10-05 18:55:01', NULL, NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (1159565451349458944, 1159563649187053569, '财务人员', '全部数据', NULL, NULL, 0, '2023-10-05 18:59:07', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_role_menu_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu_rel`;
CREATE TABLE `sys_role_menu_rel`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  `menu_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '菜单资源id []分割',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu_rel
-- ----------------------------
INSERT INTO `sys_role_menu_rel` VALUES (0, 0, 0, '[1][2][3][4][5][6][7][8][9][10][11][12][13][14][15][16][17][18][19][20][21][22][23][24][25][26]', '2023-09-12 19:52:22', '2023-09-12 19:52:24', 0, 0);
INSERT INTO `sys_role_menu_rel` VALUES (1159563649203830784, 1159563649187053568, 1159563649187053569, '[1][2][3][4][5][6][7][8][9][10][11][12][13][14][16][17][18][19][20][21][22][23][24][25][26]', '2023-10-05 18:51:58', NULL, 1159563649187053568, NULL);
INSERT INTO `sys_role_menu_rel` VALUES (1709572272098492417, 0, 1, '[1][2]', '2023-10-05 18:45:52', '2023-10-05 18:45:57', 0, 0);
INSERT INTO `sys_role_menu_rel` VALUES (1709885426275819522, NULL, 1159564417168310272, '[1][2][3][5][11]', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_menu_rel` VALUES (1709885982713159682, NULL, 1159565451349458944, '[1][2][8]', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_serial_number
-- ----------------------------
DROP TABLE IF EXISTS `sys_serial_number`;
CREATE TABLE `sys_serial_number`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NULL DEFAULT NULL COMMENT '产品表id',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库id',
  `serial_number` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '序列号',
  `sell_status` tinyint(1) NULL DEFAULT 0 COMMENT '是否卖出，0未卖出，1卖出',
  `inbound_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '入库单号',
  `outbound_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '出库单号',
  `remark` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '序列号表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_serial_number
-- ----------------------------
INSERT INTO `sys_serial_number` VALUES (105, 63, 586, 14, '12312323423223', 0, NULL, NULL, 'abab', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_serial_number` VALUES (108, 63, 586, 14, '3215952626621201', 0, NULL, NULL, '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_serial_number` VALUES (109, 63, 586, 14, '3215952626621202', 0, NULL, NULL, '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_serial_number` VALUES (110, NULL, 586, 14, '500', 0, 'LSTH00000000665', NULL, NULL, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '租户名称',
  `user_num_limit` int NULL DEFAULT NULL COMMENT '用户数量限制',
  `type` tinyint(1) NULL DEFAULT 0 COMMENT '租户类型，0免费租户，1付费租户',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '启用 0-禁用  1-启用',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '到期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '租户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO `sys_tenant` VALUES (1151153829895868454, 1151153829895868454, '测试租户', 3, 0, 1, NULL, '2023-08-30 18:13:17', NULL, '2031-11-16 18:13:17');
INSERT INTO `sys_tenant` VALUES (1151153829895868463, 1151153829895868463, '万森租户', 2000, 1, 1, NULL, '2021-02-17 23:19:17', NULL, '2099-02-17 23:19:17');

-- ----------------------------
-- Table structure for sys_tenant_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_user`;
CREATE TABLE `sys_tenant_user`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态（0-启用，1-停用）',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tenant_user
-- ----------------------------
INSERT INTO `sys_tenant_user` VALUES (1087504242027003904, 0, '石平', '石平', 'veniam do eiusmod dolore', 'o.cutbrn@qq.com', '16', 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_tenant_user` VALUES (1087514228476084224, 1071, '夏勇', 'xiayong', 'xy123456', 'l.mafisbh@qq.com', '17715151625', 0, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户姓名--例如张三',
  `user_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '登录用户名',
  `password` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '登陆密码',
  `leader_flag` tinyint(1) NULL DEFAULT 0 COMMENT '是否经理，0否，1是',
  `position` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '职位',
  `email` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `phone_number` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `is_manager` tinyint NOT NULL DEFAULT 1 COMMENT '是否为管理者 0==管理者 1==员工',
  `is_system` tinyint NOT NULL DEFAULT 0 COMMENT '是否系统自带数据 ',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态，0：正常，1：删除，2封禁',
  `description` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户描述信息',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `wechat_open_id` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '微信绑定',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_Flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (0, 0, '管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 0, '集团管理员', 'jameszow@wansen.email', '16621211608', 'https://points.wansen.cloud/group1/default/20230821/12/50/4/tmp_b389e880bb493e7249181dd7f6708cfd.jpg?download=0', 1, 0, 0, NULL, NULL, NULL, '2023-09-14 22:00:28', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132865, 0, '王有田', 'youtian', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'testyu@wansenai.com', '17015963215', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132870, 0, '李法群', 'tli18716', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'htomassl@qq.com', '13379815236', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132879, 0, '张晓东', 'xiaodongzhang', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'yuxiuaa@tecia.com', '18015156235', NULL, 1, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132893, 0, '黄磊', 'hl6789', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'biosss@126.com', '15618529781', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_user` VALUES (1153648835588132895, 0, '王一亭', 'wangyt', 'e10adc3949ba59abbe56e057f20f883e', 0, NULL, 'ciarsit@163.com', '15015151623', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132897, 0, '梁伟', 'lw17816152316', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'cestuis@163.com', '17816152316', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132900, 0, '梁超飞', 'chaofei7788', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'hjunweiu@163.com', '17715151621', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132912, 0, '孙婷', 'sunting', 'e10adc3949ba59abbe56e057f20f883e', 0, NULL, 'asdjjamsai@hotmail.com', '18027431919', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1159563649187053568, 1159563649187053568, '测试租户', 'wansen', 'e10adc3949ba59abbe56e057f20f883e', 0, NULL, 'wansenerp@163.com', '16616616661', NULL, 1, 0, 0, NULL, NULL, NULL, '2023-10-05 18:51:58', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1159564587188617216, 1159563649187053568, '测试租户用户一', 'test', 'e10adc3949ba59abbe56e057f20f883e', 0, NULL, '666666@qq.com', '16616616662', NULL, 1, 0, 0, NULL, NULL, NULL, '2023-10-05 18:55:41', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `sys_user` VALUES (1159565124227301376, 1159563649187053568, '测试租户用户二', 'test2', 'e10adc3949ba59abbe56e057f20f883e', 0, NULL, '666666@163.com', '16616616663', NULL, 1, 0, 0, NULL, NULL, NULL, '2023-10-05 18:57:49', NULL, 1159563649187053568, NULL, 0);

-- ----------------------------
-- Table structure for sys_user_business
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_business`;
CREATE TABLE `sys_user_business`  (
  `id` bigint NOT NULL COMMENT '主键',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类别',
  `key_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '主id',
  `value` varchar(10000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '值',
  `btn_str` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '按钮权限',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户/角色/模块关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_business
-- ----------------------------
INSERT INTO `sys_user_business` VALUES (5, 'RoleFunctions', '4', '[210][225][211][241][32][33][199][242][38][41][200][201][239][202][40][232][233][197][44][203][204][205][206][212][246][207][208][209][226][227][228][229][59][235][237][244][22][21][23][220][240][247][25][24][217][218][26][194][195][31][13][1][14][243][15][234][16][18][236][245][248][198][258][259]', '[{\"funId\":13,\"btnStr\":\"1\"},{\"funId\":14,\"btnStr\":\"1\"},{\"funId\":243,\"btnStr\":\"1\"},{\"funId\":234,\"btnStr\":\"1\"},{\"funId\":16,\"btnStr\":\"1\"},{\"funId\":18,\"btnStr\":\"1\"},{\"funId\":236,\"btnStr\":\"1\"},{\"funId\":245,\"btnStr\":\"1\"},{\"funId\":22,\"btnStr\":\"1\"},{\"funId\":23,\"btnStr\":\"1\"},{\"funId\":220,\"btnStr\":\"1\"},{\"funId\":240,\"btnStr\":\"1\"},{\"funId\":247,\"btnStr\":\"1\"},{\"funId\":25,\"btnStr\":\"1\"},{\"funId\":217,\"btnStr\":\"1\"},{\"funId\":218,\"btnStr\":\"1\"},{\"funId\":26,\"btnStr\":\"1\"},{\"funId\":194,\"btnStr\":\"1\"},{\"funId\":195,\"btnStr\":\"1\"},{\"funId\":31,\"btnStr\":\"1\"},{\"funId\":241,\"btnStr\":\"1,2,7\"},{\"funId\":33,\"btnStr\":\"1,2,7\"},{\"funId\":199,\"btnStr\":\"1,2,7\"},{\"funId\":242,\"btnStr\":\"1,2,7\"},{\"funId\":41,\"btnStr\":\"1,2,7\"},{\"funId\":200,\"btnStr\":\"1,2,7\"},{\"funId\":210,\"btnStr\":\"1,2,7\"},{\"funId\":211,\"btnStr\":\"1,2,7\"},{\"funId\":197,\"btnStr\":\"1,7,2\"},{\"funId\":203,\"btnStr\":\"1,7,2\"},{\"funId\":204,\"btnStr\":\"1,7,2\"},{\"funId\":205,\"btnStr\":\"1,7,2\"},{\"funId\":206,\"btnStr\":\"1,2,7\"},{\"funId\":212,\"btnStr\":\"1,7,2\"},{\"funId\":201,\"btnStr\":\"1,2,7\"},{\"funId\":202,\"btnStr\":\"1,2,7\"},{\"funId\":40,\"btnStr\":\"1,2,7\"},{\"funId\":232,\"btnStr\":\"1,2,7\"},{\"funId\":233,\"btnStr\":\"1,2,7\"}]', NULL, 0);
INSERT INTO `sys_user_business` VALUES (6, 'RoleFunctions', '5', '[22][23][25][26][194][195][31][33][200][201][41][199][202]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (7, 'RoleFunctions', '6', '[22][23][220][240][25][217][218][26][194][195][31][59][207][208][209][226][227][228][229][235][237][210][211][241][33][199][242][41][200][201][202][40][232][233][197][203][204][205][206][212]', '[{\"funId\":\"33\",\"btnStr\":\"4\"}]', NULL, 0);
INSERT INTO `sys_user_business` VALUES (9, 'RoleFunctions', '7', '[168][13][12][16][14][15][189][18][19][132]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (10, 'RoleFunctions', '8', '[168][13][12][16][14][15][189][18][19][132][22][23][25][26][27][157][158][155][156][125][31][127][126][128][33][34][35][36][37][39][40][41][42][43][46][47][48][49][50][51][52][53][54][55][56][57][192][59][60][61][62][63][65][66][68][69][70][71][73][74][76][77][79][191][81][82][83][85][89][161][86][176][165][160][28][134][91][92][29][94][95][97][104][99][100][101][102][105][107][108][110][111][113][114][116][117][118][120][121][131][135][123][122][20][130][146][147][138][148][149][153][140][145][184][152][143][170][171][169][166][167][163][164][172][173][179][178][181][182][183][186][187][247]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (11, 'RoleFunctions', '9', '[168][13][12][16][14][15][189][18][19][132][22][23][25][26][27][157][158][155][156][125][31][127][126][128][33][34][35][36][37][39][40][41][42][43][46][47][48][49][50][51][52][53][54][55][56][57][192][59][60][61][62][63][65][66][68][69][70][71][73][74][76][77][79][191][81][82][83][85][89][161][86][176][165][160][28][134][91][92][29][94][95][97][104][99][100][101][102][105][107][108][110][111][113][114][116][117][118][120][121][131][135][123][122][20][130][146][147][138][148][149][153][140][145][184][152][143][170][171][169][166][167][163][164][172][173][179][178][181][182][183][186][187][188]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (12, 'UserRole', '1', '[5]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (13, 'UserRole', '2', '[6][7]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (14, 'UserDepot', '2', '[1][2][6][7]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (15, 'UserDepot', '1', '[1][2][5][6][7][10][12][14][15][17]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (16, 'UserRole', '63', '[10]', NULL, 63, 0);
INSERT INTO `sys_user_business` VALUES (18, 'UserDepot', '63', '[14][15][19]', NULL, 63, 0);
INSERT INTO `sys_user_business` VALUES (19, 'UserDepot', '5', '[6][45][46][50]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (20, 'UserRole', '5', '[5]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (21, 'UserRole', '64', '[13]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (22, 'UserDepot', '64', '[1]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (23, 'UserRole', '65', '[5]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (24, 'UserDepot', '65', '[1]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (25, 'UserCustomer', '64', '[5][2]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (26, 'UserCustomer', '65', '[6]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (27, 'UserCustomer', '63', '[58][91]', NULL, 63, 0);
INSERT INTO `sys_user_business` VALUES (28, 'UserDepot', '96', '[7]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (29, 'UserRole', '96', '[6]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (30, 'UserRole', '113', '[10]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (32, 'RoleFunctions', '10', '[210][225][211][241][32][33][199][242][38][41][200][201][239][202][40][232][233][197][44][203][204][205][206][212][246][207][208][209][226][227][228][229][59][235][237][244][22][21][23][220][240][247][25][24][217][218][26][194][195][31][13][14][243][15][234][248][198][259]', '[{\"funId\":13,\"btnStr\":\"1\"},{\"funId\":14,\"btnStr\":\"1\"},{\"funId\":243,\"btnStr\":\"1\"},{\"funId\":234,\"btnStr\":\"1\"},{\"funId\":22,\"btnStr\":\"1\"},{\"funId\":23,\"btnStr\":\"1\"},{\"funId\":220,\"btnStr\":\"1\"},{\"funId\":240,\"btnStr\":\"1\"},{\"funId\":247,\"btnStr\":\"1\"},{\"funId\":25,\"btnStr\":\"1\"},{\"funId\":217,\"btnStr\":\"1\"},{\"funId\":218,\"btnStr\":\"1\"},{\"funId\":26,\"btnStr\":\"1\"},{\"funId\":194,\"btnStr\":\"1\"},{\"funId\":195,\"btnStr\":\"1\"},{\"funId\":31,\"btnStr\":\"1\"},{\"funId\":241,\"btnStr\":\"1,2,7\"},{\"funId\":33,\"btnStr\":\"1,2,7\"},{\"funId\":199,\"btnStr\":\"1,7,2\"},{\"funId\":242,\"btnStr\":\"1,2,7\"},{\"funId\":41,\"btnStr\":\"1,2,7\"},{\"funId\":200,\"btnStr\":\"1,2,7\"},{\"funId\":210,\"btnStr\":\"1,2,7\"},{\"funId\":211,\"btnStr\":\"1,2,7\"},{\"funId\":197,\"btnStr\":\"1,2,7\"},{\"funId\":203,\"btnStr\":\"1,7,2\"},{\"funId\":204,\"btnStr\":\"1,7,2\"},{\"funId\":205,\"btnStr\":\"1,2,7\"},{\"funId\":206,\"btnStr\":\"1,7,2\"},{\"funId\":212,\"btnStr\":\"1,2,7\"},{\"funId\":201,\"btnStr\":\"1,2,7\"},{\"funId\":202,\"btnStr\":\"1,2,7\"},{\"funId\":40,\"btnStr\":\"1,2,7\"},{\"funId\":232,\"btnStr\":\"1,2,7\"},{\"funId\":233,\"btnStr\":\"1,2,7\"}]', NULL, 0);
INSERT INTO `sys_user_business` VALUES (34, 'UserRole', '115', '[10]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (35, 'UserRole', '117', '[10]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (36, 'UserDepot', '117', '[8][9]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (37, 'UserCustomer', '117', '[52]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (38, 'UserRole', '120', '[4]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (41, 'RoleFunctions', '12', '', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (48, 'RoleFunctions', '13', '[59][207][208][209][226][227][228][229][235][237][210][211][241][33][199][242][41][200]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (51, 'UserRole', '74', '[10]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (52, 'UserDepot', '121', '[13]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (54, 'UserDepot', '115', '[13]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (56, 'UserCustomer', '115', '[56]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (57, 'UserCustomer', '121', '[56]', NULL, NULL, 0);
INSERT INTO `sys_user_business` VALUES (67, 'UserRole', '131', '[17]', NULL, 63, 0);
INSERT INTO `sys_user_business` VALUES (68, 'RoleFunctions', '16', '[210][211][225]', '[{\"funId\":210,\"btnStr\":\"1\"}]', 63, 0);
INSERT INTO `sys_user_business` VALUES (69, 'RoleFunctions', '17', '[210][211][241][33][199][242][41][200][201][202][40][232][233][197][203][204][205][206][212]', '[{\"funId\":\"241\",\"btnStr\":\"1,2\"},{\"funId\":\"33\",\"btnStr\":\"1,2\"},{\"funId\":\"199\",\"btnStr\":\"1,2\"},{\"funId\":\"242\",\"btnStr\":\"1,2\"},{\"funId\":\"41\",\"btnStr\":\"1,2\"},{\"funId\":\"200\",\"btnStr\":\"1,2\"},{\"funId\":\"210\",\"btnStr\":\"1,2\"},{\"funId\":\"211\",\"btnStr\":\"1,2\"},{\"funId\":\"197\",\"btnStr\":\"1\"},{\"funId\":\"203\",\"btnStr\":\"1\"},{\"funId\":\"204\",\"btnStr\":\"1\"},{\"funId\":\"205\",\"btnStr\":\"1\"},{\"funId\":\"206\",\"btnStr\":\"1\"},{\"funId\":\"212\",\"btnStr\":\"1\"},{\"funId\":\"201\",\"btnStr\":\"1,2\"},{\"funId\":\"202\",\"btnStr\":\"1,2\"},{\"funId\":\"40\",\"btnStr\":\"1,2\"},{\"funId\":\"232\",\"btnStr\":\"1,2\"},{\"funId\":\"233\",\"btnStr\":\"1,2\"}]', 63, 0);
INSERT INTO `sys_user_business` VALUES (83, 'UserRole', '146', '[10]', NULL, 146, 0);

-- ----------------------------
-- Table structure for sys_user_dept_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept_rel`;
CREATE TABLE `sys_user_dept_rel`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `dept_id` bigint NOT NULL COMMENT '部门id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `sort` int NULL DEFAULT NULL COMMENT '用户在所属部门中显示顺序',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1162519548306391041 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '机构用户关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_dept_rel
-- ----------------------------
INSERT INTO `sys_user_dept_rel` VALUES (1157714147601809409, 0, 1154756575114956805, 0, NULL, 0, '2023-09-30 16:22:42', NULL, 1151247731927683077, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159252910790410240, 0, 1154794589174239277, 0, NULL, 0, '2023-10-04 22:17:12', NULL, 1159252418010021888, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562436399857664, 0, 1154794589170044930, 1151247731927683082, NULL, 0, '2023-10-05 18:47:08', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562481442488320, 0, 1154794589170044930, 1153648835588132865, NULL, 0, '2023-10-05 18:47:19', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562506344071168, 0, 1154794589170044930, 1153648835588132870, NULL, 0, '2023-10-05 18:47:25', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562541039353856, 0, 1154794589174239242, 1153648835588132879, NULL, 0, '2023-10-05 18:47:33', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562541043548160, 0, 1154794589174239268, 1153648835588132879, NULL, 0, '2023-10-05 18:47:33', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562576258924544, 0, 1154794589174239268, 1153648835588132895, NULL, 0, '2023-10-05 18:47:42', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562576258924545, 0, 1154794589174239242, 1153648835588132895, NULL, 0, '2023-10-05 18:47:42', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562603685478400, 0, 1154490573429018634, 1153648835588132897, NULL, 0, '2023-10-05 18:47:48', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562603689672704, 0, 1154794589174239268, 1153648835588132897, NULL, 0, '2023-10-05 18:47:48', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562627454599168, 0, 1154794589174239242, 1153648835588132900, NULL, 0, '2023-10-05 18:47:54', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562650217086976, 0, 1154794589174239242, 1153648835588132912, NULL, 0, '2023-10-05 18:47:59', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159563040627097600, 0, 1159563040610320384, 0, NULL, 0, '2023-10-05 18:49:33', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159563921766481921, 1159563649187053568, 1159563649187053570, 1159563649187053568, NULL, 0, '2023-10-05 18:53:03', NULL, 1159563649187053568, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159564587213783040, 1159563649187053568, 1159563649187053570, 1159564587188617216, NULL, 0, '2023-10-05 18:55:41', NULL, 1159563649187053568, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1160275990530752512, 0, 1160275990509780992, 0, NULL, 0, '2023-10-07 18:02:33', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1160276228180017152, 0, 1160276228167434240, 0, NULL, 0, '2023-10-07 18:03:30', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1160277580616564736, 0, 1160277580603981824, 0, NULL, 0, '2023-10-07 18:08:52', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1160278001296867328, 0, 1160278001280090112, 0, NULL, 0, '2023-10-07 18:10:32', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1160291544196972544, 0, 1160291544184389632, 0, NULL, 0, '2023-10-07 19:04:21', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1160296115011059712, 0, 1160296114805538816, 0, NULL, 0, '2023-10-07 19:22:31', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1160296275740983296, 0, 1160296275728400384, 0, NULL, 0, '2023-10-07 19:23:09', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1160318685781426176, 0, 1160318685722705920, 0, NULL, 0, '2023-10-07 20:52:12', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1160321457784356864, 0, 1160321457767579648, 0, NULL, 0, '2023-10-07 21:03:13', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1160323505716199424, 0, 1160323505707810816, 0, NULL, 0, '2023-10-07 21:11:22', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1160327420092350464, 0, 1160327420079767552, 0, NULL, 0, '2023-10-07 21:26:55', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1160511794360352768, 0, 1160511794343575552, 0, NULL, 0, '2023-10-08 09:39:33', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1162519348175175680, 1159563649187053568, 1162519348091289600, 1159563649187053568, NULL, 0, '2023-10-13 14:36:51', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1162519486297800704, 1159563649187053568, 1162519486230691840, 1159563649187053568, NULL, 0, '2023-10-13 14:37:24', NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1162519548306391040, 1159563649187053568, 1159563649187053570, 1159565124227301376, NULL, 0, '2023-10-13 14:37:39', NULL, 1159563649187053568, NULL);

-- ----------------------------
-- Table structure for sys_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role_rel`;
CREATE TABLE `sys_user_role_rel`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role_rel
-- ----------------------------
INSERT INTO `sys_user_role_rel` VALUES (1155232990910353443, 0, 0, 0, '2023-09-23 20:04:31', NULL, NULL, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159562436357914624, 0, 1151247731927683082, 1, '2023-10-05 18:47:08', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159562481434099712, 0, 1153648835588132865, 1, '2023-10-05 18:47:19', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159562506327293952, 0, 1153648835588132870, 1, '2023-10-05 18:47:25', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159562541030965248, 0, 1153648835588132879, 1, '2023-10-05 18:47:33', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159562576246341632, 0, 1153648835588132895, 1, '2023-10-05 18:47:42', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159562603677089792, 0, 1153648835588132897, 1, '2023-10-05 18:47:48', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159562627446210560, 0, 1153648835588132900, 1, '2023-10-05 18:47:54', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159562650204504064, 0, 1153648835588132912, 1, '2023-10-05 18:47:59', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159563921766481920, 1159563649187053568, 1159563649187053568, 1159563649187053569, '2023-10-05 18:53:03', NULL, 1159563649187053568, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159564587205394432, 1159563649187053568, 1159564587188617216, 1159564417168310272, '2023-10-05 18:55:41', NULL, 1159563649187053568, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1162519548251865088, 1159563649187053568, 1159565124227301376, 1159564417168310272, '2023-10-13 14:37:39', NULL, 1159563649187053568, NULL);

-- ----------------------------
-- Table structure for sys_user_warehouse_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_warehouse_rel`;
CREATE TABLE `sys_user_warehouse_rel`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `warehouse_id` bigint NOT NULL COMMENT '仓库id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_warehouse_rel
-- ----------------------------

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `warehouse_manager` bigint NULL DEFAULT NULL COMMENT '负责人',
  `warehouse_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '仓库地址',
  `price` decimal(24, 6) NULL DEFAULT NULL COMMENT '仓储费',
  `truckage` decimal(24, 6) NULL DEFAULT NULL COMMENT '搬运费',
  `type` int NULL DEFAULT NULL COMMENT '类型',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态（0-启用，1-停用）',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '描述',
  `sort` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '排序',
  `is_default` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认仓库（0-启用，1-停用）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_Flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '仓库表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of warehouse
-- ----------------------------
INSERT INTO `warehouse` VALUES (14, 63, 131, '仓库1', 'dizhi', 12.000000, 12.000000, 0, 1, '描述', '1', 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse` VALUES (15, 63, 131, '仓库2', '地址100', 555.000000, 666.000000, 0, 1, 'dfdf', '2', 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse` VALUES (17, 63, 131, '仓库3', '123123', 123.000000, 123.000000, 0, 1, '123', '3', 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse` VALUES (19, NULL, NULL, '仓库666', NULL, 11.000000, NULL, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse` VALUES (1163491458020278272, 0, 1153648835588132897, '西安仓库', '西安灞桥区', 200.000000, 7936.320000, NULL, 0, '测试', '1', 0, '2023-10-16 14:59:40', '2023-10-16 17:43:45', 0, 0, 0);
INSERT INTO `warehouse` VALUES (1163492331714772992, 0, 1153648835588132865, '河北仓库', '河北保定', 850.000000, 120.000000, NULL, 0, NULL, NULL, 1, '2023-10-16 15:03:09', '2023-10-16 17:57:01', 0, 0, 0);

-- ----------------------------
-- Table structure for warehouse_head
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_head`;
CREATE TABLE `warehouse_head`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '主类型 (出库/入库)',
  `sub_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '子类型（采购订单/采购退货/销售订单/组装单/拆卸单）',
  `init_bill_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '初始票据号',
  `bill_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '票据号',
  `supplier_id` bigint NULL DEFAULT NULL COMMENT '供应商id',
  `account_id` bigint NULL DEFAULT NULL COMMENT '账户id',
  `change_amount` decimal(24, 6) NULL DEFAULT NULL COMMENT '变动金额(收款/付款)',
  `back_amount` decimal(24, 6) NULL DEFAULT NULL COMMENT '找零金额',
  `total_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '合计金额',
  `pay_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '付款类型(现金、记账等)',
  `bill_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单据类型',
  `remark` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `file_name` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '附件名称',
  `sales_man` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '业务员（可以多个）',
  `account_id_list` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '多账户ID列表',
  `account_money_list` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '多账户金额列表',
  `discount` decimal(24, 6) NULL DEFAULT NULL COMMENT '优惠率',
  `discount_money` decimal(24, 6) NULL DEFAULT NULL COMMENT '优惠金额',
  `discount_last_money` decimal(24, 6) NULL DEFAULT NULL COMMENT '优惠后金额',
  `other_money` decimal(24, 6) NULL DEFAULT NULL COMMENT '销售或采购费用合计',
  `deposit` decimal(24, 6) NULL DEFAULT NULL COMMENT '订金',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态，0未审核、1已审核、2完成采购|销售、3部分采购|销售、9审核中',
  `purchase_status` tinyint(1) NULL DEFAULT NULL COMMENT '采购状态，0未采购、2完成采购、3部分采购',
  `source` tinyint(1) NULL DEFAULT 0 COMMENT '单据来源，0-pc，1-手机',
  `correlation_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '关联订单号',
  `operate_time` datetime NULL DEFAULT NULL COMMENT '操作时间（出/入库时间）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK2A80F214B610FC06`(`supplier_id` ASC) USING BTREE,
  INDEX `FK2A80F214AAE50527`(`account_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '单据主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of warehouse_head
-- ----------------------------
INSERT INTO `warehouse_head` VALUES (258, 63, '其它', '采购订单', 'CGDD00000000630', 'CGDD00000000630', 57, NULL, NULL, NULL, -110.000000, '现付', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 0, 0, NULL, '2021-06-02 00:21:44', '2021-06-02 00:21:54', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (259, 63, '入库', '采购', 'CGRK00000000631', 'CGRK00000000631', 57, 17, -110.000000, NULL, -110.000000, '现付', NULL, NULL, NULL, NULL, '', '', 0.000000, 0.000000, 110.000000, 0.000000, NULL, 0, 0, 0, 'CGDD00000000630', '2021-06-02 00:22:05', '2021-06-02 00:22:23', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (260, 63, '出库', '采购退货', 'CGTH00000000632', 'CGTH00000000632', 57, 17, 22.000000, 0.000000, 22.000000, '现付', NULL, NULL, '', NULL, '', '', 0.000000, 0.000000, 22.000000, 0.000000, 0.000000, 1, 0, 0, NULL, '2021-06-02 00:22:26', '2021-06-02 00:22:35', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (261, 63, '其它', '销售订单', 'XSDD00000000633', 'XSDD00000000633', 58, NULL, NULL, NULL, 44.000000, '现付', NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 0, 0, NULL, '2021-06-02 00:22:39', '2021-06-02 00:22:48', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (262, 63, '出库', '销售', 'XSCK00000000634', 'XSCK00000000634', 58, 17, 44.000000, NULL, 44.000000, '现付', NULL, NULL, NULL, '', '', '', 0.000000, 0.000000, 44.000000, 0.000000, NULL, 0, 0, 0, 'XSDD00000000633', '2021-06-02 00:22:54', '2021-06-02 00:23:03', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (263, 63, '入库', '销售退货', 'XSTH00000000635', 'XSTH00000000635', 71, 17, -22.000000, NULL, -22.000000, '现付', NULL, NULL, NULL, '', '', '', 0.000000, 0.000000, 22.000000, 0.000000, NULL, 0, 0, 0, NULL, '2021-06-02 00:23:05', '2021-06-02 00:23:12', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (264, 63, '出库', '零售', 'LSCK00000000636', 'LSCK00000000636', 60, 17, 22.000000, NULL, 22.000000, '现付', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, NULL, '2021-06-02 00:23:14', '2021-06-02 00:23:21', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (265, 63, '入库', '零售退货', 'LSTH00000000637', 'LSTH00000000637', 60, 17, -1000.000000, 0.000000, -1000.000000, '现付', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, 0.000000, NULL, 0.000000, 0, 0, 0, NULL, '2021-06-02 00:23:23', '2021-06-02 00:23:29', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (266, 63, '入库', '其它', 'QTRK00000000638', 'QTRK00000000638', 57, NULL, NULL, NULL, -55.000000, '现付', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 0, NULL, '2021-06-02 00:23:36', '2021-06-02 00:23:48', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (267, 63, '出库', '其它', 'QTCK00000000639', 'QTCK00000000639', 58, NULL, NULL, NULL, 30.000000, '现付', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, NULL, '2021-06-02 00:23:50', '2021-06-02 00:23:59', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (268, 63, '出库', '调拨', 'DBCK00000000640', 'DBCK00000000640', NULL, NULL, 0.000000, 0.000000, 2442.000000, '现付', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, 0.000000, NULL, 0.000000, 0, 0, 0, NULL, '2021-06-02 00:24:00', '2021-06-02 00:24:09', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (269, 63, '其它', '组装单', 'ZZD00000000641', 'ZZD00000000641', NULL, NULL, NULL, NULL, 0.000000, '现付', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, NULL, '2021-06-02 00:24:11', '2021-06-02 00:24:29', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (270, 63, '其它', '拆卸单', 'CXD00000000642', 'CXD00000000642', NULL, NULL, NULL, NULL, 0.000000, '现付', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, NULL, '2021-06-02 00:24:32', '2021-06-02 00:24:45', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (271, 63, '入库', '采购', 'CGRK00000000651', 'CGRK00000000651', 57, 17, -20.000000, NULL, -80.000000, '现付', NULL, NULL, NULL, NULL, '', '', 0.000000, 0.000000, 80.000000, 0.000000, NULL, 0, 0, 0, NULL, '2021-07-06 23:44:45', '2021-07-06 23:45:20', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (272, 63, '出库', '销售', 'XSCK00000000652', 'XSCK00000000652', 58, 17, 8.000000, NULL, 28.000000, '现付', NULL, NULL, NULL, '', '', '', 0.000000, 0.000000, 28.000000, 0.000000, NULL, 0, 0, 0, NULL, '2021-07-06 23:45:24', '2021-07-06 23:46:07', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (273, 63, '入库', '采购', 'CGRK00000000658', 'CGRK00000000658', 57, 17, -60.000000, NULL, -60.000000, '现付', NULL, NULL, NULL, NULL, '', '', 0.000000, 0.000000, 60.000000, 0.000000, NULL, 0, 0, 0, NULL, '2021-07-28 00:58:02', '2021-07-28 00:58:12', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (274, NULL, '出库', '零售', 'LSCK00000000663', 'LSCK00000000663', 92, 17, 100.000000, 0.000000, 100.000000, '现付', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, 0.000000, NULL, 0.000000, 0, 0, 0, NULL, '2023-08-30 18:09:18', '2023-08-30 18:09:51', NULL, 63, NULL, 1);
INSERT INTO `warehouse_head` VALUES (277, NULL, '入库', '零售退货', 'LSTH00000000665', 'LSTH00000000665', 60, 17, -15.000000, 0.000000, -15.000000, '现付', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, NULL, '2023-08-30 18:10:40', '2023-08-30 18:11:10', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (278, NULL, '其它', '采购订单', 'CGDD00000000666', 'CGDD00000000666', 68, 17, 0.000000, NULL, -240.000000, '现付', NULL, NULL, '', NULL, '', '', 0.000000, 0.000000, 240.000000, NULL, NULL, 0, 0, 0, NULL, '2023-08-30 18:11:31', '2023-08-30 18:12:02', NULL, 63, NULL, 0);
INSERT INTO `warehouse_head` VALUES (279, NULL, '出库', '采购退货', 'CGTH00000000668', 'CGTH00000000668', 57, 17, 22.000000, 0.000000, 22.000000, '现付', '', NULL, '', NULL, '', '', 0.000000, 0.000000, 22.000000, 0.000000, NULL, 1, 0, 0, '', '2023-09-02 13:40:02', '2023-09-02 13:40:06', NULL, 63, NULL, 0);

-- ----------------------------
-- Table structure for warehouse_item
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_item`;
CREATE TABLE `warehouse_item`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `header_id` bigint NOT NULL COMMENT '表头Id',
  `product_id` bigint NOT NULL COMMENT '商品Id',
  `product_extend_id` bigint NULL DEFAULT NULL COMMENT '商品扩展id',
  `product_unit` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '商品计量单位',
  `multi_attribute` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '多属性',
  `oper_number` decimal(24, 6) NULL DEFAULT NULL COMMENT '数量',
  `basic_number` decimal(24, 6) NULL DEFAULT NULL COMMENT '基础数量，如kg、瓶',
  `unit_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '单价',
  `purchase_unit_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '采购单价',
  `tax_unit_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '含税单价',
  `total_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '金额',
  `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库ID',
  `another_warehouse_id` bigint NULL DEFAULT NULL COMMENT '调拨时，对方仓库Id',
  `tax_rate` decimal(24, 6) NULL DEFAULT NULL COMMENT '税率',
  `tax_money` decimal(24, 6) NULL DEFAULT NULL COMMENT '税额',
  `tax_last_money` decimal(24, 6) NULL DEFAULT NULL COMMENT '价税合计',
  `product_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '商品类型',
  `serial_numbers_list` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '序列号列表',
  `batch_number` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '批次号',
  `effective_date` datetime NULL DEFAULT NULL COMMENT '有效日期',
  `correlation_id` bigint NULL DEFAULT NULL COMMENT '关联明细id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK2A819F475D61CCF7`(`product_id` ASC) USING BTREE,
  INDEX `FK2A819F474BB6190E`(`header_id` ASC) USING BTREE,
  INDEX `FK2A819F479485B3F5`(`warehouse_id` ASC) USING BTREE,
  INDEX `FK2A819F47729F5392`(`another_warehouse_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '单据子表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of warehouse_item
-- ----------------------------
INSERT INTO `warehouse_item` VALUES (312, 63, 258, 588, 10, '个', NULL, 10.000000, 10.000000, 11.000000, NULL, NULL, 110.000000, NULL, 14, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (313, 63, 259, 588, 10, '个', NULL, 10.000000, 10.000000, 11.000000, NULL, NULL, 110.000000, NULL, 14, NULL, NULL, 0.000000, 110.000000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (315, 63, 261, 588, 10, '个', NULL, 2.000000, 2.000000, 22.000000, NULL, NULL, 44.000000, NULL, 14, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (316, 63, 262, 588, 10, '个', NULL, 2.000000, 2.000000, 22.000000, NULL, NULL, 44.000000, NULL, 14, NULL, NULL, 0.000000, 44.000000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (317, 63, 263, 588, 10, '个', NULL, 1.000000, 1.000000, 22.000000, NULL, 22.000000, 22.000000, NULL, 14, NULL, 0.000000, 0.000000, 22.000000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (318, 63, 264, 588, 10, '个', NULL, 1.000000, 1.000000, 22.000000, NULL, NULL, 22.000000, NULL, 14, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (320, 63, 266, 568, 2, '个', NULL, 5.000000, 5.000000, 11.000000, NULL, NULL, 55.000000, NULL, 14, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (321, 63, 267, 568, 2, '个', NULL, 2.000000, 2.000000, 15.000000, NULL, NULL, 30.000000, NULL, 14, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (323, 63, 269, 588, 10, '个', NULL, 1.000000, 1.000000, 0.000000, NULL, NULL, 0.000000, NULL, 14, NULL, NULL, NULL, NULL, '组合件', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (324, 63, 269, 568, 2, '个', NULL, 1.000000, 1.000000, 0.000000, NULL, NULL, 0.000000, NULL, 14, NULL, NULL, NULL, NULL, '普通子件', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (325, 63, 270, 588, 10, '个', NULL, 1.000000, 1.000000, 0.000000, NULL, NULL, 0.000000, NULL, 14, NULL, NULL, NULL, NULL, '组合件', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (326, 63, 270, 568, 2, '个', NULL, 1.000000, 1.000000, 0.000000, NULL, NULL, 0.000000, NULL, 14, NULL, NULL, NULL, NULL, '普通子件', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (327, 63, 271, 570, 4, '个', NULL, 10.000000, 10.000000, 8.000000, NULL, 8.000000, 80.000000, NULL, 14, NULL, 0.000000, 0.000000, 80.000000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (328, 63, 272, 570, 4, '个', NULL, 2.000000, 2.000000, 14.000000, NULL, 14.000000, 28.000000, NULL, 14, NULL, 0.000000, 0.000000, 28.000000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (330, 63, 273, 619, 37, '件', '橙色,L', 5.000000, 5.000000, 12.000000, NULL, 12.000000, 60.000000, NULL, 14, NULL, 0.000000, 0.000000, 60.000000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (333, NULL, 274, 619, 37, '件', '橙色,L', 1.000000, 1.000000, 80.000000, 12.000000, NULL, 80.000000, NULL, 15, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `warehouse_item` VALUES (334, NULL, 274, 619, 37, '件', '橙色,L', 1.000000, 1.000000, 20.000000, 12.000000, NULL, 20.000000, NULL, 14, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `warehouse_item` VALUES (335, NULL, 277, 586, 9, '个', NULL, 1.000000, 1.000000, 15.000000, 12.000000, NULL, 15.000000, NULL, 14, NULL, NULL, NULL, NULL, NULL, '500', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (336, NULL, 265, 588, 10, '个', NULL, 20.000000, 20.000000, 50.000000, 11.000000, NULL, 1000.000000, NULL, 14, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (337, NULL, 278, 619, 38, '件', '绿色,M', 20.000000, 20.000000, 12.000000, NULL, NULL, 240.000000, NULL, NULL, NULL, 0.000000, 0.000000, 240.000000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (338, NULL, 268, 568, 2, '个', NULL, 222.000000, 222.000000, 11.000000, NULL, NULL, 2442.000000, NULL, 14, 15, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (339, NULL, 279, 588, 10, '个', NULL, 2.000000, 2.000000, 11.000000, NULL, NULL, 22.000000, NULL, 14, NULL, 0.000000, 0.000000, 22.000000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `warehouse_item` VALUES (340, NULL, 260, 588, 10, '个', NULL, 2.000000, 2.000000, 11.000000, NULL, NULL, 22.000000, NULL, 14, NULL, 0.000000, 0.000000, 22.000000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
