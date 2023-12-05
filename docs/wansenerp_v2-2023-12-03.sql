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

 Date: 03/12/2023 18:06:33
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
  `first_quarter_account_receivable` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '一季度应收账款',
  `second_quarter_account_receivable` decimal(12, 2) NULL DEFAULT NULL COMMENT '二季度应收账款',
  `third_quarter_account_receivable` decimal(12, 2) NULL DEFAULT NULL COMMENT '三季度应收账款',
  `fourth_quarter_account_receivable` decimal(12, 2) NULL DEFAULT NULL COMMENT '四季度应收账款',
  `total_account_receivable` decimal(24, 2) NULL DEFAULT NULL COMMENT '累计应收账款',
  `address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '地址',
  `tax_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '纳税人识别号',
  `bank_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `account_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '账号',
  `tax_rate` decimal(12, 2) NULL DEFAULT NULL COMMENT '税率',
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
INSERT INTO `customer` VALUES (1162569363274858496, 1159563649187053568, '客户1', '赵伟', '178151615', '666666@qq.com', 0.00, 0.00, 0.00, 0.00, 0.00, '111', 'DHC15610555FXITAL1', '中国银行', '', 0.00, 0, '111', 0, '2023-10-14 01:55:36', '2023-10-14 02:02:25', 1159563649187053568, 1159563649187053568, 0);
INSERT INTO `customer` VALUES (1162571026857459712, 1159563649187053568, '万森222', '赵伟', '17715151638', '', 0.00, 0.00, 815.36, 0.00, 815.36, '', '', '', '1231232131231', 0.00, 0, '', 0, '2023-10-14 02:02:12', '2023-10-14 02:02:55', 1159563649187053568, 1159563649187053568, 0);
INSERT INTO `customer` VALUES (1171837619361808384, 0, '赵伟', '赵先生', '16621211605', '666666@qq.com', 20.00, 30.59, 40.00, 12.00, 102.59, '', '', '', '', 0.00, 0, '', 0, '2023-11-08 15:44:20', '2023-11-27 19:56:57', 0, 0, 0);
INSERT INTO `customer` VALUES (1712897693711888385, 1159563649187053568, '客户4444', '小赵', '16621211505', '', 100.00, 0.00, 0.00, 21.85, 121.85, '', '', '', '', 7.00, 1, '', 0, '2023-10-14 02:26:49', '2023-10-14 02:27:24', 1159563649187053568, 1159563649187053568, 0);
INSERT INTO `customer` VALUES (1712897693711888386, 1159563649187053568, '客户3', '小李', '19918181620', '', 8915.00, 0.00, 95.23, 785.32, 9795.55, '', '', '', '', 3.00, 0, '', 0, '2023-10-14 02:26:49', '2023-10-14 02:29:35', 1159563649187053568, 1159563649187053568, 0);
INSERT INTO `customer` VALUES (1713135907563495426, 0, '客户A', '小刘', '16621211505', 'xiaozhangsan@163.com', 100.00, 0.00, 0.00, 21.85, 121.85, '', '', '', '', 7.00, 0, '', 0, '2023-10-14 18:13:24', '2023-11-27 19:57:10', 0, 0, 0);
INSERT INTO `customer` VALUES (1713135957840617474, 0, '客户b', '小李', '19918181620', 'test1158163a@qq.com', 0.00, 0.00, 0.00, 0.00, 0.00, '', '', '', '', 3.00, 0, '', 0, '2023-10-14 18:13:36', '2023-11-29 16:32:41', 0, 0, 0);

-- ----------------------------
-- Table structure for financial_account
-- ----------------------------
DROP TABLE IF EXISTS `financial_account`;
CREATE TABLE `financial_account`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `account_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `account_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '编号',
  `initial_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '期初金额',
  `current_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '当前余额',
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
INSERT INTO `financial_account` VALUES (17, 63, '账户1', 'zzz111', 100.00, 829.00, 'aabb', 1, NULL, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_account` VALUES (18, 63, '账户2', '1234131324', 200.00, -1681.00, 'bbbb', 1, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_account` VALUES (24, NULL, 'aaa', 'aaa', 0.00, NULL, NULL, 1, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_account` VALUES (1713836226953875457, 0, '支付宝', 'ZFB00001', 2000.00, 1346.04, '1', 0, 1, 1, '2023-10-16 16:36:13', '2023-11-29 16:34:03', 0, 0, 0);
INSERT INTO `financial_account` VALUES (1713851069471657986, 0, '微信支付', 'WX00005', 1000.00, 1503.37, '1', 0, 1, 0, '2023-10-16 17:35:11', '2023-11-19 17:32:48', 0, 0, 0);
INSERT INTO `financial_account` VALUES (1726136760402522114, 0, '中国邮政储蓄银行卡', 'YZ005615661', 1000.00, 1028.00, NULL, 0, NULL, 0, '2023-11-19 15:14:08', NULL, 0, NULL, 0);
INSERT INTO `financial_account` VALUES (1726168904420032514, 0, '余额宝', 'YEB9919161', 1000.00, 501.31, NULL, 0, NULL, 0, '2023-11-19 17:21:52', NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for financial_main
-- ----------------------------
DROP TABLE IF EXISTS `financial_main`;
CREATE TABLE `financial_main`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `related_person_id` bigint NULL DEFAULT NULL COMMENT '关联人id(会员/客户/供应商)',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '经手人id',
  `account_id` bigint NULL DEFAULT NULL COMMENT '账户id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类型(支出/收入/收款/付款/转账)',
  `change_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '变动金额(优惠/收款/付款/实付)',
  `discount_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `total_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '合计金额',
  `receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单据编号',
  `receipt_source` tinyint(1) NULL DEFAULT 0 COMMENT '单据来源，0-pc，1-手机',
  `receipt_date` datetime NULL DEFAULT NULL COMMENT '单据日期',
  `remark` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `file_id` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '附件ID（多个用逗号分隔）',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态，0未审核、1已审核、9审核中',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK9F4C0D8DB610FC06`(`related_person_id` ASC) USING BTREE,
  INDEX `FK9F4C0D8DAAE50527`(`account_id` ASC) USING BTREE,
  INDEX `FK9F4C0D8DC4170B37`(`operator_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '财务主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of financial_main
-- ----------------------------
INSERT INTO `financial_main` VALUES (1176520392559296512, 0, 1713136000454746115, 1713915547055915009, NULL, '收预付款', 850.56, NULL, 850.56, 'ACD1176520293313675264', 0, '2023-10-31 13:54:53', '收款测试22242', '1179444908709117952', 0, '2023-11-21 13:52:00', '2023-11-29 15:32:59', 0, 0, 0);
INSERT INTO `financial_main` VALUES (1176592721779884032, 0, 1713135907563495426, 1718358441116385282, 1713836226953875457, '收入', NULL, NULL, 5.58, 'SRD1176592056907202560', 0, '2023-11-21 18:36:46', '测试首次收入单', '1176604819884867584', 0, '2023-11-21 18:39:25', '2023-11-21 19:27:29', 0, 0, 0);
INSERT INTO `financial_main` VALUES (1176599052909805568, 0, 1723264058620678146, 1718358441116385282, 1713851069471657986, '收入', NULL, NULL, 7.19, 'SRD1176598913994457088', 0, '2023-11-21 19:04:01', '测试修改', '1176600180141588480', 0, '2023-11-21 19:04:34', '2023-11-21 19:09:03', 0, 0, 1);
INSERT INTO `financial_main` VALUES (1176604969973841920, 0, 1713136000454746114, 1718358441116385282, 1726136760402522114, '收入', NULL, NULL, 21.00, 'SRD1176604878785478656', 0, '2023-11-21 19:27:43', '测试22', '', 0, '2023-11-21 19:28:05', '2023-11-29 15:39:54', 0, 0, 0);
INSERT INTO `financial_main` VALUES (1176605374170529792, 0, 1713136000454746114, 1713915547055915009, 1726136760402522114, '收入', NULL, NULL, 8.00, 'SRD1176605312883359744', 0, '2023-11-21 19:29:26', '', '', 1, '2023-11-21 19:29:41', NULL, 0, NULL, 0);
INSERT INTO `financial_main` VALUES (1176894146565111808, 0, 1712724937206738945, 1718358441116385282, 1726168904420032514, '支出', NULL, NULL, 205.00, 'ZCD1176893947427946496', 0, '2023-11-22 14:36:22', '测试支出', '', 0, '2023-11-22 14:37:10', '2023-11-29 15:41:24', 0, 0, 0);
INSERT INTO `financial_main` VALUES (1176894594533556224, 0, 1712724937252876290, 1718358441116385282, 1713851069471657986, '支出', NULL, NULL, 26.00, 'ZCD1176894456599674880', 0, '2023-11-22 14:38:24', '', '1179446604420087808', 1, '2023-11-22 14:38:57', '2023-11-29 15:39:44', 0, 0, 0);
INSERT INTO `financial_main` VALUES (1176940408647712768, 0, NULL, 1718358441116385282, 1726136760402522114, '转账', NULL, NULL, 12.00, 'ZZD1176939881671163904', 0, '2023-11-14 20:41:57', '修改测试', '1179447525401165824', 0, '2023-11-22 17:41:00', '2023-11-29 15:43:23', 0, 0, 0);
INSERT INTO `financial_main` VALUES (1177310046128701440, 0, 1171837619361808384, 1718358441116385282, 1713836226953875457, '收款', 3.00, 5.00, 3.00, 'SKD1177309865551331328', 0, '2023-11-23 18:09:05', '测试', '1179448094756962304', 0, '2023-11-23 18:09:48', '2023-11-29 15:45:39', 0, 0, 0);
INSERT INTO `financial_main` VALUES (1177311184295034880, 0, 1713135907563495426, 1713915547055915009, 1713851069471657986, '收款', 10.86, 0.00, 10.86, 'SKD1177311033740492800', 0, '2023-11-23 18:13:43', '全收款', '1177311184286646272', 1, '2023-11-23 18:14:20', NULL, 0, NULL, 0);
INSERT INTO `financial_main` VALUES (1177343988869365760, 0, 1723264058620678146, 1713915547055915009, 1713836226953875457, '付款', 10.00, 5.00, 10.00, 'FKD1177343753300475904', 0, '2023-11-23 20:23:44', '测试付款单', '1179448135022280704', 0, '2023-11-23 20:24:41', '2023-11-29 15:45:48', 0, 0, 0);

-- ----------------------------
-- Table structure for financial_sub
-- ----------------------------
DROP TABLE IF EXISTS `financial_sub`;
CREATE TABLE `financial_sub`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `financial_main_id` bigint NOT NULL COMMENT '财务主表id',
  `account_id` bigint NULL DEFAULT NULL COMMENT '账户Id',
  `income_expense_id` bigint NULL DEFAULT NULL COMMENT '收支项目Id',
  `other_receipt` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '关联单据',
  `receivable_payment_arrears` decimal(12, 2) NULL DEFAULT NULL COMMENT '应收/付 欠款',
  `received_prepaid_arrears` decimal(12, 2) NULL DEFAULT NULL COMMENT '已收/付 欠款',
  `single_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '单项金额',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单据备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK9F4CBAC0AAE50527`(`account_id` ASC) USING BTREE,
  INDEX `FK9F4CBAC0C5FE6007`(`financial_main_id` ASC) USING BTREE,
  INDEX `FK9F4CBAC0D203EDC5`(`income_expense_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '财务子表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of financial_sub
-- ----------------------------
INSERT INTO `financial_sub` VALUES (1176600180204503040, 0, 1176599052909805568, 1713851069471657986, 1726907491121950721, NULL, NULL, NULL, 1.23, '测试4444', '2023-11-21 19:09:03', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1176600180204503041, 0, 1176599052909805568, 1713851069471657986, 1726907515809624065, NULL, NULL, NULL, 5.96, '测试333', '2023-11-21 19:09:03', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1176604819926810624, 0, 1176592721779884032, 1713836226953875457, 1726791316987826178, NULL, NULL, NULL, 2.00, '测试33', '2023-11-21 19:27:29', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1176604819926810625, 0, 1176592721779884032, 1713836226953875457, 1726791316987826178, NULL, NULL, NULL, 3.58, '测试444', '2023-11-21 19:27:29', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1176605374191501312, 0, 1176605374170529792, 1726136760402522114, 1726791316987826178, NULL, NULL, NULL, 8.00, NULL, '2023-11-21 19:29:41', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1177311184303423488, 0, 1177311184295034880, 1713851069471657986, NULL, 'XSCK1176242940419244032', 10.86, 10.86, 10.86, '测试', '2023-11-23 18:14:20', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1179444908730089472, 0, 1176520392559296512, 1713836226953875457, NULL, NULL, NULL, NULL, 850.56, '测试333', NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_sub` VALUES (1179446604470419456, 0, 1176894594533556224, 1713851069471657986, 1727214912503635970, NULL, NULL, NULL, 26.00, '测试2', '2023-11-29 15:39:44', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1179446646962913280, 0, 1176604969973841920, 1726136760402522114, 1726907491121950721, NULL, NULL, NULL, 21.00, '测试BTC', '2023-11-29 15:39:54', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1179447026312544256, 0, 1176894146565111808, 1726168904420032514, 1726792532551659522, NULL, NULL, NULL, 105.00, '测试1', '2023-11-29 15:41:24', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1179447026312544257, 0, 1176894146565111808, 1726168904420032514, 1726792532551659522, NULL, NULL, NULL, 100.00, '测试2', '2023-11-29 15:41:24', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1179447525434720256, 0, 1176940408647712768, 1713836226953875457, NULL, NULL, NULL, NULL, 12.00, '4', '2023-11-29 15:43:23', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1179448094777933824, 0, 1177310046128701440, 1713836226953875457, NULL, 'XSCK1176165194678665216', 25.21, 15.00, 1.00, '测试22', '2023-11-29 15:45:39', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1179448094777933825, 0, 1177310046128701440, 1713836226953875457, NULL, 'XSCK1177262513750802432', 204.80, 27.00, 2.00, '测试1', '2023-11-29 15:45:39', NULL, 0, NULL, 0);
INSERT INTO `financial_sub` VALUES (1179448135047446528, 0, 1177343988869365760, 1713836226953875457, NULL, 'CGRK1177342662022266880', 120.00, 3072.00, 10.00, '先付60', '2023-11-29 15:45:48', NULL, 0, NULL, 0);

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
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '启用',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
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
INSERT INTO `income_expense` VALUES (1726791316987826178, 0, 'USDT', '收入', '测试', 0, 2, NULL, '2023-11-21 10:42:36', NULL, 0, 0);
INSERT INTO `income_expense` VALUES (1726792532551659522, 0, '房租', '支出', '每个月的支出', 0, 3, '2023-11-21 10:39:57', '2023-11-21 10:42:42', 0, 0, 0);
INSERT INTO `income_expense` VALUES (1726907491121950721, 0, 'BTC', '收入', NULL, 0, NULL, '2023-11-21 18:16:45', NULL, 0, NULL, 0);
INSERT INTO `income_expense` VALUES (1727214912503635970, 0, '机械零件', '支出', '测试', 0, NULL, '2023-11-22 14:38:20', NULL, 0, NULL, 0);

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
INSERT INTO `member` VALUES (1713136000454746114, 0, 'VIP005739952', '小赵', '13000000000', '', 481327.620, 0, '', 0, '2023-10-14 18:13:46', '2023-10-14 18:14:27', 0, 0, 0);
INSERT INTO `member` VALUES (1713136000454746115, 0, 'SVIP7186333745', '李哥', '17815151515', '测试', 1624648.600, 0, '', 0, '2023-10-14 18:13:46', '2023-11-29 16:33:20', 0, 0, 0);
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
INSERT INTO `operator` VALUES (1718358441116385282, 0, NULL, '公司财务', '财务员', 0, 3, NULL, '2023-10-29 04:05:53', NULL, 0, NULL, 0);
INSERT INTO `operator` VALUES (1721744363082063873, 0, NULL, '小王同学', '销售员', 0, NULL, '销售', '2023-11-07 12:20:19', NULL, 0, NULL, 0);
INSERT INTO `operator` VALUES (1721773354815930370, 0, NULL, '小李同学', '销售员', 0, NULL, NULL, '2023-11-07 14:15:31', NULL, 0, NULL, 0);
INSERT INTO `operator` VALUES (1721773395324518401, 0, NULL, '张小山', '销售员', 0, NULL, NULL, '2023-11-07 14:15:41', NULL, 0, NULL, 0);

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
  `product_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单位',
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
INSERT INTO `product` VALUES (1170089611624448000, 0, 1170001102565801984, '牡丹香烟', '上海烟草集团有限公司', '上海烟厂', '(软/粗支)', '红色包装', 1169999650594226176, NULL, 365, 5.000, NULL, 0, '焦油:10mg', '一氧化碳:12mg', '烟碱:0.9mg', 1, 1, '西安', '2023-11-03 20:01:00', NULL, 0, NULL, 0);
INSERT INTO `product` VALUES (1170091260111749120, 0, 1170090488699551744, '宝矿力水特', '宝矿力电解质', '电解质饮料', '500ML', '白色', 21, NULL, 365, 8.000, NULL, 0, '保质期12个月', '富含维生素C', NULL, 1, 1, '河北', '2023-11-03 20:06:31', NULL, 0, NULL, 0);
INSERT INTO `product` VALUES (1172242448127098880, 0, 17, '32K60型无线装订本', '上汇', '8562', '140x200mm', '白色', NULL, '本', 999, 0.740, NULL, 0, '页数:28张', '32K60型无线装订本', NULL, 0, 0, '西安', '2023-11-09 18:32:59', NULL, 0, NULL, 0);
INSERT INTO `product` VALUES (1177571511913938944, 0, NULL, '测试', NULL, NULL, NULL, NULL, NULL, '个', NULL, 0.000, NULL, 0, NULL, NULL, NULL, 0, 0, NULL, '2023-12-02 18:30:23', NULL, 0, NULL, 0);

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
INSERT INTO `product_attribute` VALUES (7, 0, '属性测试', 'T11|T222|测试', 1, '测试', NULL, NULL, NULL, NULL, 0);
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
INSERT INTO `product_category` VALUES (1, 0, '蔬菜瓜果', 'HX12113123', NULL, 1, '测试333', '2023-10-04 21:42:34', '2023-11-29 16:05:51', NULL, 0, 0);
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
INSERT INTO `product_category` VALUES (1170001102565801984, 0, '香烟', 'XY0751563', NULL, 22, NULL, '2023-11-03 14:06:40', '2023-11-29 15:58:41', 0, 0, 0);
INSERT INTO `product_category` VALUES (1170090488699551744, 0, '饮料', 'YL1591615', NULL, NULL, '测试', '2023-11-03 20:01:52', '2023-11-29 16:00:34', 0, 0, 0);
INSERT INTO `product_category` VALUES (1179453238131294208, 0, '测试', '1111', NULL, NULL, NULL, '2023-11-29 16:06:05', NULL, 0, NULL, 1);
INSERT INTO `product_category` VALUES (1179453438887460864, 0, '测试333', '3333', NULL, NULL, NULL, '2023-11-29 16:06:53', NULL, 0, NULL, 1);

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

-- ----------------------------
-- Table structure for product_property
-- ----------------------------
DROP TABLE IF EXISTS `product_property`;
CREATE TABLE `product_property`  (
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
-- Records of product_property
-- ----------------------------
INSERT INTO `product_property` VALUES (1, '制造商', 1, '制造商', '01', NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_property` VALUES (2, '自定义1', 1, '自定义1', '02', NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_property` VALUES (3, '自定义2', 1, '自定义2', '03', NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_property` VALUES (4, '自定义3', 1, '自定义3', '04', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for product_sku
-- ----------------------------
DROP TABLE IF EXISTS `product_sku`;
CREATE TABLE `product_sku`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品id',
  `product_bar_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '商品条码',
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
-- Records of product_sku
-- ----------------------------
INSERT INTO `product_sku` VALUES (1170089611653808128, 0, 1170089611624448000, '6901028075862', '包', NULL, 13.000, 15.000, 18.000, 17.000, 1, '2023-11-03 20:01:00', NULL, 0, NULL, 0);
INSERT INTO `product_sku` VALUES (1170089611653808131, 0, 1170089611624448000, '6901028089185', '条', NULL, 130.000, 150.000, 180.000, 175.000, 1, '2023-11-03 20:01:00', NULL, 0, NULL, 0);
INSERT INTO `product_sku` VALUES (1170091260136914944, 0, 1170091260111749120, '6932529211107', '瓶.', NULL, 4.800, 5.500, 5.500, 5.500, 1, '2023-11-03 20:06:31', NULL, 0, NULL, 0);
INSERT INTO `product_sku` VALUES (1170091260136914947, 0, 1170091260111749120, '6932529981586', '箱', NULL, 57.600, 66.000, 66.000, 66.000, 1, '2023-11-03 20:06:31', NULL, 0, NULL, 0);
INSERT INTO `product_sku` VALUES (1172242448252928000, 0, 1172242448127098880, '6941536185622', '本', NULL, 3.500, 4.500, 4.500, 4.500, 1, '2023-11-09 18:32:59', NULL, 0, NULL, 0);
INSERT INTO `product_sku` VALUES (1177571512094294016, 0, 1177571511913938944, '689191191', '个', NULL, 333.000, 0.000, 0.000, 0.000, 1, '2023-12-02 18:30:23', NULL, 0, NULL, 0);
INSERT INTO `product_sku` VALUES (1177571512094294019, 0, 1177571511913938944, '2131231DK', '条', NULL, 0.000, 0.000, 222.000, 0.000, 1, '2023-12-02 18:30:23', NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for product_stock
-- ----------------------------
DROP TABLE IF EXISTS `product_stock`;
CREATE TABLE `product_stock`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_sku_id` bigint NULL DEFAULT NULL COMMENT '产品扩展id',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库id',
  `init_stock_quantity` decimal(12, 2) NULL DEFAULT NULL COMMENT '初始库存数量',
  `low_stock_quantity` decimal(12, 2) NULL DEFAULT NULL COMMENT '最低库存数量',
  `high_stock_quantity` decimal(12, 2) NULL DEFAULT NULL COMMENT '最高库存数量',
  `current_stock_quantity` decimal(12, 2) NULL DEFAULT NULL COMMENT '当前库存数量',
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
INSERT INTO `product_stock` VALUES (1170089611653808129, 0, 1170089611653808128, 1163491458020278272, 280.00, 375.00, 500.00, 171.00, '2023-11-03 20:01:00', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1170089611653808130, 0, 1170089611653808128, 1163492331714772992, 128.00, 300.00, 23.00, 172.00, '2023-11-03 20:01:00', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1170089611653808132, 0, 1170089611653808131, 1163491458020278272, 16.00, 19.00, 500.00, -76.00, '2023-11-03 20:01:00', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1170089611653808133, 0, 1170089611653808131, 1163492331714772992, 128.00, 300.00, 19.00, -62.00, '2023-11-03 20:01:00', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1170091260136914945, 0, 1170091260136914944, 1163491458020278272, 300.00, 400.00, 360.00, 99.00, '2023-11-03 20:06:31', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1170091260136914946, 0, 1170091260136914944, 1163492331714772992, 122.00, 450.00, 350.00, 99.00, '2023-11-03 20:06:31', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1170091260136914948, 0, 1170091260136914947, 1163491458020278272, 300.00, 400.00, 360.00, 119.00, '2023-11-03 20:06:31', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1170091260136914949, 0, 1170091260136914947, 1163492331714772992, 177.00, 450.00, 418.00, 117.00, '2023-11-03 20:06:31', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1172242448252928001, 0, 1172242448252928000, 1163491458020278272, 200.00, 233.00, 25.00, 205.00, '2023-11-09 18:32:59', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1172242448252928002, 0, 1172242448252928000, 1163492331714772992, 185.00, 211.00, 36.00, 208.00, '2023-11-09 18:32:59', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1177571512094294017, 0, 1177571512094294016, 1163491458020278272, 12.00, 999.00, 666.00, 12.00, '2023-12-02 18:30:23', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1177571512094294018, 0, 1177571512094294016, 1163492331714772992, 12.00, 0.00, 0.00, 12.00, '2023-12-02 18:30:23', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1177571512094294020, 0, 1177571512094294019, 1163491458020278272, 2222.00, 13.00, 0.00, 2222.00, '2023-12-02 18:30:23', NULL, 0, NULL, 0);
INSERT INTO `product_stock` VALUES (1177571512094294021, 0, 1177571512094294019, 1163492331714772992, 0.00, 0.00, 14.00, 0.00, '2023-12-02 18:30:23', NULL, 0, NULL, 0);

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
INSERT INTO `product_unit` VALUES (1169999650594226176, 0, '包/(条=5包)', '包', '条', NULL, NULL, 5.000, NULL, NULL, 0, '2023-11-03 14:00:54', '2023-11-29 16:11:21', 0, 0, 0);

-- ----------------------------
-- Table structure for receipt_purchase_main
-- ----------------------------
DROP TABLE IF EXISTS `receipt_purchase_main`;
CREATE TABLE `receipt_purchase_main`  (
  `id` bigint NOT NULL COMMENT '采购单据主表id（主键）',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '主类型 (订单/出库/入库)',
  `sub_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '子类型（采购订单/采购入库/采购退货）',
  `init_receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '初始单据编号',
  `receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单据编号',
  `receipt_date` datetime NULL DEFAULT NULL COMMENT '单据日期',
  `supplier_id` bigint NULL DEFAULT NULL COMMENT '供应商id',
  `account_id` bigint NULL DEFAULT NULL COMMENT '账户id',
  `change_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '变动金额(退款/付款)',
  `total_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '总计金额\r\n',
  `discount_rate` decimal(12, 2) NULL DEFAULT NULL COMMENT '优惠率',
  `discount_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `discount_last_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '优惠后金额',
  `arrears_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '欠款金额',
  `other_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '采购费用合计（其他金额）',
  `deposit` decimal(12, 2) NULL DEFAULT NULL COMMENT '定金',
  `file_id` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '附件id（可以多个 逗号隔开）',
  `multiple_account` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '多账户（可以多个 逗号隔开）',
  `multiple_account_amount` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '多账户金额 （可以多个 逗号隔开）',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '备注',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态，0未审核、1已审核、2审核中、3部分采购、4完成采购',
  `other_receipt` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '关联单据',
  `receipt_source` tinyint(1) NULL DEFAULT 0 COMMENT '单据来源，0-pc，1-手机',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of receipt_purchase_main
-- ----------------------------
INSERT INTO `receipt_purchase_main` VALUES (1176155059990298624, 0, '订单', '采购订单', 'CGD1176154989282721792', 'CGD1176154989282721792', '2023-11-20 13:40:01', 1712724937257070594, 1713851069471657986, NULL, NULL, 6.60, 3.00, 42.45, NULL, NULL, 0.00, '', '', '', '', 0, NULL, 0, '2023-11-20 13:40:18', '2023-11-29 13:37:20', 0, 0, 0);
INSERT INTO `receipt_purchase_main` VALUES (1176159472175808512, 0, '入库', '采购入库', 'CGRK1176159358942183424', 'CGRK1176159358942183424', '2023-11-20 13:57:23', 1712724937252876290, 1726168904420032514, -304.73, NULL, 6.35, 20.00, 294.73, 0.00, 10.00, NULL, '', '', '', '', 0, '', 0, '2023-11-20 13:57:50', NULL, 0, NULL, 0);
INSERT INTO `receipt_purchase_main` VALUES (1176161001070919680, 0, '入库', '采购入库', 'CGRK1176160870397378560', 'CGRK1176160870397378560', '2023-11-20 14:03:23', 1713135795982426115, 1726168904420032514, -270.96, NULL, 0.00, 0.00, 270.96, 0.00, 0.00, NULL, '', '', '', '', 1, '', 0, '2023-11-18 14:03:55', NULL, 0, NULL, 0);
INSERT INTO `receipt_purchase_main` VALUES (1176161393292869632, 0, '入库', '采购退货', 'CGTH1176161261734330368', 'CGTH1176161261734330368', '2023-11-20 14:04:56', 1713135795982426115, 1713851069471657986, 298.86, NULL, 0.00, 0.00, 298.86, 0.00, 0.00, NULL, '', '', '', '测试采访稿', 0, '', 0, '2023-11-19 14:05:28', '2023-11-30 14:04:51', 0, 0, 0);
INSERT INTO `receipt_purchase_main` VALUES (1177342812178350080, 0, '入库', '采购入库', 'CGRK1177342662022266880', 'CGRK1177342662022266880', '2023-11-23 20:19:24', 1723264058620678146, 1713836226953875457, -150.00, NULL, 3.96, 12.00, 291.00, 191.00, 20.00, NULL, '1178826754979004416', '', '', '', 1, '', 0, '2023-11-23 20:20:00', '2023-11-27 22:36:40', 0, 0, 0);
INSERT INTO `receipt_purchase_main` VALUES (1179435455544819712, 0, '订单', '采购订单', 'CGD1179435400154841088', 'CGD1179435400154841088', '2023-11-29 14:55:12', 1712724937252876290, NULL, NULL, NULL, 0.22, 1.00, 449.00, NULL, NULL, 1.00, '', '', '', '', 0, NULL, 0, '2023-11-29 14:55:25', NULL, 0, NULL, 1);
INSERT INTO `receipt_purchase_main` VALUES (1180911307587584000, 0, '入库', '采购退货', 'CGTH1180911238553534464', 'CGTH1180911238553534464', '2023-12-03 16:39:39', 1723264058620678146, 1713836226953875457, 45.45, NULL, 0.00, 0.00, 45.45, 0.00, 0.00, NULL, '', '', '', '测试333', 0, '', 0, '2023-12-03 16:39:56', '2023-12-03 16:40:02', 0, 0, 0);

-- ----------------------------
-- Table structure for receipt_purchase_sub
-- ----------------------------
DROP TABLE IF EXISTS `receipt_purchase_sub`;
CREATE TABLE `receipt_purchase_sub`  (
  `id` bigint NOT NULL COMMENT '采购单据子表id（主键）',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `receipt_purchase_main_id` bigint NOT NULL COMMENT '采购单据主表id',
  `product_id` bigint NOT NULL COMMENT '商品Id',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库ID',
  `product_barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品条码',
  `product_number` int NULL DEFAULT NULL COMMENT '商品数量',
  `unit_price` decimal(13, 2) NULL DEFAULT NULL COMMENT '单价（这里不等于商品表的字段）因为单据会变动',
  `total_amount` decimal(13, 2) NULL DEFAULT NULL COMMENT '总金额（这里不等于商品表的字段）因为单据会变动',
  `tax_rate` decimal(13, 2) NULL DEFAULT NULL COMMENT '税率',
  `tax_amount` decimal(13, 2) NULL DEFAULT NULL COMMENT '税额',
  `tax_included_amount` decimal(13, 2) NULL DEFAULT NULL COMMENT '价税合计（含税金额）',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of receipt_purchase_sub
-- ----------------------------
INSERT INTO `receipt_purchase_sub` VALUES (1726479945779642370, 0, 1176159472175808512, 1170089611624448000, 1163492331714772992, '6901028075862', 3, 15.00, 45.00, 1.00, 0.45, 45.45, NULL, '2023-11-20 13:57:50', NULL, 0, NULL, 0);
INSERT INTO `receipt_purchase_sub` VALUES (1726479945779642371, 0, 1176159472175808512, 1170091260111749120, 1163491458020278272, '6932529981586', 4, 66.00, 264.00, 2.00, 5.28, 269.28, NULL, '2023-11-20 13:57:50', NULL, 0, NULL, 0);
INSERT INTO `receipt_purchase_sub` VALUES (1726481474662289410, 0, 1176161001070919680, 1170089611624448000, 1163492331714772992, '6901028075862', 4, 15.00, 60.00, 3.00, 1.80, 61.80, NULL, '2023-11-20 14:03:55', NULL, 0, NULL, 0);
INSERT INTO `receipt_purchase_sub` VALUES (1726481474674872322, 0, 1176161001070919680, 1170091260111749120, 1163492331714772992, '6932529981586', 3, 66.00, 198.00, 1.00, 1.98, 199.98, NULL, '2023-11-20 14:03:55', NULL, 0, NULL, 0);
INSERT INTO `receipt_purchase_sub` VALUES (1726481474674872323, 0, 1176161001070919680, 1172242448127098880, 1163491458020278272, '6941536185622', 2, 4.50, 9.00, 2.00, 0.18, 9.18, NULL, '2023-11-20 14:03:55', NULL, 0, NULL, 0);
INSERT INTO `receipt_purchase_sub` VALUES (1729147228545130497, 0, 1177342812178350080, 1170089611624448000, 1163491458020278272, '6901028075862', 20, 15.00, 300.00, 1.00, 3.00, 303.00, NULL, '2023-11-27 22:36:40', '2023-11-27 22:36:40', NULL, 0, 0);
INSERT INTO `receipt_purchase_sub` VALUES (1729736278423498753, 0, 1176155059990298624, 1170089611624448000, 1163491458020278272, '6901028075862', 3, 15.00, 45.00, 1.00, 0.45, 45.45, NULL, '2023-11-29 13:37:20', '2023-11-29 13:37:20', NULL, 0, 0);
INSERT INTO `receipt_purchase_sub` VALUES (1729755929140269058, 0, 1179435455544819712, 1170089611624448000, 1163491458020278272, '6901028089185', 3, 150.00, 450.00, 0.00, 0.00, 450.00, NULL, '2023-11-29 14:55:25', NULL, 0, NULL, 1);
INSERT INTO `receipt_purchase_sub` VALUES (1730105588107042818, 0, 1176161393292869632, 1170089611624448000, 1163492331714772992, '6901028075862', 2, 15.00, 30.00, 3.00, 0.90, 30.90, NULL, '2023-11-30 14:04:51', '2023-11-30 14:04:51', NULL, 0, 0);
INSERT INTO `receipt_purchase_sub` VALUES (1730105588107042819, 0, 1176161393292869632, 1170091260111749120, 1163491458020278272, '6932529981586', 2, 66.00, 132.00, 1.00, 1.32, 133.32, NULL, '2023-11-30 14:04:51', '2023-11-30 14:04:51', NULL, 0, 0);
INSERT INTO `receipt_purchase_sub` VALUES (1730105588107042820, 0, 1176161393292869632, 1170091260111749120, 1163492331714772992, '6932529981586', 2, 66.00, 132.00, 2.00, 2.64, 134.64, NULL, '2023-11-30 14:04:51', '2023-11-30 14:04:51', NULL, 0, 0);
INSERT INTO `receipt_purchase_sub` VALUES (1731231805090648066, 0, 1180911307587584000, 1170089611624448000, 1163491458020278272, '6901028075862', 3, 15.00, 45.00, 1.00, 0.45, 45.45, NULL, '2023-12-03 16:40:02', '2023-12-03 16:40:02', NULL, 0, 0);

-- ----------------------------
-- Table structure for receipt_retail_main
-- ----------------------------
DROP TABLE IF EXISTS `receipt_retail_main`;
CREATE TABLE `receipt_retail_main`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '主类型 (出库/入库)',
  `sub_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '子类型（采购订单/采购退货/销售订单/组装单/拆卸单）',
  `init_receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '初始单据编号',
  `receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单据编号',
  `receipt_date` datetime NULL DEFAULT NULL COMMENT '单据日期',
  `member_id` bigint NULL DEFAULT NULL COMMENT '会员id',
  `account_id` bigint NULL DEFAULT NULL COMMENT '账户id',
  `change_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '变动金额(收款/付款)',
  `back_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '找零金额',
  `total_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '合计金额',
  `total_product_number` int NULL DEFAULT NULL COMMENT '商品总数量',
  `payment_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '付款类型(现金、记账等)',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '备注',
  `file_id` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '附件id（可以多个 逗号隔开）',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态，0未审核、1已审核、2完成采购|销售、3部分采购|销售、9审核中',
  `source` tinyint(1) NULL DEFAULT 0 COMMENT '单据来源，0-pc，1-手机',
  `other_receipt` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '关联单据',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK2A80F214B610FC06`(`member_id` ASC) USING BTREE,
  INDEX `FK2A80F214AAE50527`(`account_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '单据主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of receipt_retail_main
-- ----------------------------
INSERT INTO `receipt_retail_main` VALUES (1176109095669727232, 0, '出库', '零售出库', 'LSCK1176109014258286592', 'LSCK1176109014258286592', '2023-11-20 10:37:39', 1713136000454746114, 1726194331922579458, 75.00, 0.00, 75.00, NULL, '现付', '测试', '', 0, 0, NULL, '2023-02-20 10:37:39', NULL, 0, NULL, 1);
INSERT INTO `receipt_retail_main` VALUES (1176109169703387136, 0, '出库', '零售出库', 'LSCK1176109108126810112', 'LSCK1176109108126810112', '2023-11-20 10:37:57', 1713110644611874818, NULL, 198.00, 0.00, 198.00, NULL, '', '测试2', '', 1, 0, NULL, '2023-10-20 10:37:57', '2023-12-03 14:56:14', 0, 0, 0);
INSERT INTO `receipt_retail_main` VALUES (1176109259373412352, 0, '出库', '零售出库', 'LSCK1176109212413984768', 'LSCK1176109212413984768', '2023-11-20 10:38:18', 1713136000454746115, NULL, 9.00, 0.00, 9.00, NULL, '', '', '', 0, 0, NULL, '2023-11-20 10:38:18', '2023-11-29 11:30:34', 0, 0, 0);
INSERT INTO `receipt_retail_main` VALUES (1176110049626423296, 0, '入库', '零售退货', 'LSTH1176109980336521216', 'LSTH1176109980336521216', '2023-11-20 10:41:27', 1713136000454746114, 1713851069471657986, -13.50, 0.00, -13.50, NULL, NULL, '退回', '', 0, 0, '', '2023-08-20 10:41:27', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_main` VALUES (1176115404146212864, 0, '出库', '零售出库', 'LSCK1176115317508669440', 'LSCK1176115317508669440', '2023-11-20 11:02:44', 1713136000454746115, 1713851069471657986, 30.00, 0.00, 40.89, NULL, '现付', '测试3333', '', 0, 0, NULL, '2023-06-20 11:02:44', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_main` VALUES (1176116087385751552, 0, '入库', '零售退货', 'LSTH1176116009132621824', 'LSTH1176116009132621824', '2023-11-20 11:05:26', 1713136000454746115, 1713851069471657986, -15.00, 0.00, -15.00, NULL, NULL, '测试4444', '', 1, 0, '', '2023-11-20 11:05:26', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_main` VALUES (1176116186451017728, 0, '入库', '零售退货', 'LSTH1176116132591960064', 'LSTH1176116132591960064', '2023-11-20 11:05:50', 1718893160883040257, 1726168904420032514, -45.00, 0.00, -45.00, NULL, NULL, '', '', 1, 0, '', '2023-12-20 11:05:50', '2023-11-29 11:39:03', 0, 0, 0);
INSERT INTO `receipt_retail_main` VALUES (1176163004958375936, 0, '出库', '零售出库', 'LSCK1176162871470456832', 'LSCK1176162871470456832', '2023-11-20 14:11:52', 1713136000454746115, NULL, 258.00, 0.00, 258.00, NULL, '', '', '', 1, 0, NULL, '2023-11-20 14:11:52', '2023-11-29 11:15:26', 0, 0, 0);
INSERT INTO `receipt_retail_main` VALUES (1176163546484965376, 0, '入库', '零售退货', 'LSTH1176163399982120960', 'LSTH1176163399982120960', '2023-11-20 14:14:02', 1713136000454746115, 1713836226953875457, -81.00, 0.00, -81.00, NULL, NULL, '退货', '', 0, 0, '', '2023-09-12 14:14:02', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_main` VALUES (1179371712567836672, 0, '出库', '零售出库', 'LSCK1179371662890500096', 'LSCK1179371662890500096', '2023-11-29 10:42:08', 1713136000454746114, NULL, 750.00, 0.00, 750.00, NULL, '现付', '12', '', 0, 0, NULL, '2023-09-15 10:42:08', '2023-11-29 11:15:54', 0, 0, 0);
INSERT INTO `receipt_retail_main` VALUES (1179373700839899136, 0, '出库', '零售出库', 'LSCK1179373639267516416', 'LSCK1179373639267516416', '2023-11-29 10:50:02', 1713136000454746115, 1713851069471657986, 45.00, 5.00, 40.00, NULL, '现付', '测试', '1179772936404336640', 1, 0, NULL, '2023-12-29 10:50:02', '2023-11-30 13:16:27', 0, 0, 0);
INSERT INTO `receipt_retail_main` VALUES (1179373780435206144, 0, '出库', '零售出库', 'LSCK1179373743382724608', 'LSCK1179373743382724608', '2023-11-29 10:50:21', 1713136000454746114, NULL, 15.00, 0.00, 15.00, NULL, '现付', '', '', 0, 0, NULL, '2023-11-29 10:50:21', NULL, 0, NULL, 1);
INSERT INTO `receipt_retail_main` VALUES (1179374122967236608, 0, '出库', '零售出库', 'LSCK1179374072606228480', 'LSCK1179374072606228480', '2023-11-29 10:51:43', 1713136000454746115, NULL, 15.00, 0.00, 15.00, NULL, '预付款', '测试', '', 0, 0, NULL, '2023-11-29 10:51:43', NULL, 0, NULL, 1);
INSERT INTO `receipt_retail_main` VALUES (1179884687712059392, 0, '出库', '零售出库', 'LSCK1179884662823059456', 'LSCK1179884662823059456', '2023-11-30 20:40:31', 1713136000454746115, NULL, 15.00, 0.00, 15.00, NULL, '', '', '', 0, 0, NULL, '2023-11-30 20:40:31', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_main` VALUES (1179884726765223936, 0, '出库', '零售出库', 'LSCK1179884696369102848', 'LSCK1179884696369102848', '2023-11-30 20:40:40', 1713110644611874818, NULL, 58.50, 0.00, 58.50, NULL, '', '', '', 0, 0, NULL, '2023-11-30 20:40:40', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_main` VALUES (1179884763381497856, 0, '出库', '零售出库', 'LSCK1179884734428217344', 'LSCK1179884734428217344', '2023-11-30 20:40:49', 1713136000454746115, NULL, 216.00, 0.00, 216.00, NULL, '现付', '', '', 1, 0, NULL, '2023-11-30 20:40:49', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_main` VALUES (1179884798563319808, 0, '出库', '零售出库', 'LSCK1179884771149348864', 'LSCK1179884771149348864', '2023-11-30 20:40:57', 1713136000454746115, 1713851069471657986, 150.00, 0.00, 150.00, NULL, '现付', '', '', 1, 0, NULL, '2023-11-30 20:40:57', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_main` VALUES (1179884853693251584, 0, '出库', '零售出库', 'LSCK1179884809732751360', 'LSCK1179884809732751360', '2023-11-30 20:41:10', 1713110644611874818, 1726168904420032514, 150.00, 0.00, 150.00, NULL, '预付款', '', '', 0, 0, NULL, '2023-11-30 20:41:10', NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for receipt_retail_sub
-- ----------------------------
DROP TABLE IF EXISTS `receipt_retail_sub`;
CREATE TABLE `receipt_retail_sub`  (
  `id` bigint NOT NULL COMMENT '主键',
  `receipt_main_id` bigint NOT NULL COMMENT '仓库主表id',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NOT NULL COMMENT '商品Id',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库ID',
  `product_barcode` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '商品条码',
  `product_number` int NULL DEFAULT NULL COMMENT '商品数量',
  `unit_price` decimal(13, 2) NULL DEFAULT NULL COMMENT '单价（这里不等于商品表的字段）因为单据会变动',
  `total_amount` decimal(13, 2) NULL DEFAULT NULL COMMENT '总金额（这里不等于商品表的字段）因为单据会变动',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '商品备注',
  `correlation_id` bigint NULL DEFAULT NULL COMMENT '关联明细id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK2A819F475D61CCF7`(`product_id` ASC) USING BTREE,
  INDEX `FK2A819F474BB6190E`(`receipt_main_id` ASC) USING BTREE,
  INDEX `FK2A819F479485B3F5`(`warehouse_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '单据子表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of receipt_retail_sub
-- ----------------------------
INSERT INTO `receipt_retail_sub` VALUES (1726429569235894273, 1176109095669727232, 0, 1170089611624448000, 1163491458020278272, '6901028075862', 5, 15.00, 75.00, NULL, NULL, '2023-11-20 10:37:39', NULL, 0, NULL, 1);
INSERT INTO `receipt_retail_sub` VALUES (1726430523196768258, 1176110049626423296, 0, 1172242448127098880, 1163492331714772992, '6941536185622', 3, 4.50, 13.50, NULL, NULL, '2023-11-20 10:41:27', NULL, 0, NULL, 1);
INSERT INTO `receipt_retail_sub` VALUES (1726435877729120257, 1176115404146212864, 0, 1170089611624448000, 1163491458020278272, '6901028075862', 2, 15.00, 30.00, NULL, NULL, '2023-11-20 11:02:44', NULL, 0, NULL, 1);
INSERT INTO `receipt_retail_sub` VALUES (1726436560960286721, 1176116087385751552, 0, 1170089611624448000, 1163491458020278272, '6901028075862', 1, 15.00, 15.00, NULL, NULL, '2023-11-20 11:05:26', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_sub` VALUES (1726484020051070977, 1176163546484965376, 0, 1170091260111749120, 1163492331714772992, '6932529981586', 1, 66.00, 66.00, NULL, NULL, '2023-11-20 14:14:02', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_sub` VALUES (1726484020051070978, 1176163546484965376, 0, 1170089611624448000, 1163491458020278272, '6901028075862', 1, 15.00, 15.00, NULL, NULL, '2023-11-20 14:14:02', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_sub` VALUES (1729694253934309377, 1179373780435206144, 0, 1170089611624448000, 1163492331714772992, '6901028075862', 1, 15.00, 15.00, NULL, NULL, '2023-11-29 10:50:21', NULL, 0, NULL, 1);
INSERT INTO `receipt_retail_sub` VALUES (1729694596441174018, 1179374122967236608, 0, 1170089611624448000, 1163491458020278272, '6901028075862', 1, 15.00, 15.00, NULL, NULL, '2023-11-29 10:51:43', NULL, 0, NULL, 1);
INSERT INTO `receipt_retail_sub` VALUES (1729700564881240065, 1176163004958375936, 0, 1170089611624448000, 1163491458020278272, '6901028075862', 4, 15.00, 60.00, NULL, NULL, '2023-11-29 11:15:26', '2023-11-29 11:15:26', NULL, 0, 0);
INSERT INTO `receipt_retail_sub` VALUES (1729700564881240066, 1176163004958375936, 0, 1170091260111749120, 1163492331714772992, '6932529981586', 3, 66.00, 198.00, NULL, NULL, '2023-11-29 11:15:26', '2023-11-29 11:15:26', NULL, 0, 0);
INSERT INTO `receipt_retail_sub` VALUES (1729700682959286274, 1179371712567836672, 0, 1170089611624448000, 1163491458020278272, '6901028089185', 5, 150.00, 750.00, NULL, NULL, '2023-11-29 11:15:54', '2023-11-29 11:15:54', NULL, 0, 0);
INSERT INTO `receipt_retail_sub` VALUES (1729704373749673985, 1176109259373412352, 0, 1172242448127098880, 1163492331714772992, '6941536185622', 2, 4.50, 9.00, NULL, NULL, '2023-11-29 11:30:34', '2023-11-29 11:30:34', NULL, 0, 0);
INSERT INTO `receipt_retail_sub` VALUES (1729706508935622657, 1176116186451017728, 0, 1170089611624448000, 1163492331714772992, '6901028075862', 3, 15.00, 45.00, NULL, NULL, '2023-11-29 11:39:03', '2023-11-29 11:39:03', NULL, 0, 1);
INSERT INTO `receipt_retail_sub` VALUES (1730093409916018690, 1179373700839899136, 0, 1170089611624448000, 1163491458020278272, '6901028075862', 2, 15.00, 30.00, NULL, NULL, '2023-11-30 13:16:27', '2023-11-30 13:16:27', NULL, 0, 0);
INSERT INTO `receipt_retail_sub` VALUES (1730093409916018691, 1179373700839899136, 0, 1170091260111749120, 1163492331714772992, '6932529211107', 1, 5.50, 5.50, NULL, NULL, '2023-11-30 13:16:27', '2023-11-30 13:16:27', NULL, 0, 0);
INSERT INTO `receipt_retail_sub` VALUES (1730093409916018692, 1179373700839899136, 0, 1172242448127098880, 1163492331714772992, '6941536185622', 1, 4.50, 4.50, NULL, NULL, '2023-11-30 13:16:27', '2023-11-30 13:16:27', NULL, 0, 0);
INSERT INTO `receipt_retail_sub` VALUES (1730205161299234817, 1179884687712059392, 0, 1170089611624448000, 1163492331714772992, '6901028075862', 1, 15.00, 15.00, NULL, NULL, '2023-11-30 20:40:31', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_sub` VALUES (1730205200260124673, 1179884726765223936, 0, 1172242448127098880, 1163492331714772992, '6941536185622', 13, 4.50, 58.50, NULL, NULL, '2023-11-30 20:40:40', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_sub` VALUES (1730205236834455554, 1179884763381497856, 0, 1170089611624448000, 1163492331714772992, '6901028089185', 1, 150.00, 150.00, NULL, NULL, '2023-11-30 20:40:49', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_sub` VALUES (1730205236897370113, 1179884763381497856, 0, 1170091260111749120, 1163492331714772992, '6932529981586', 1, 66.00, 66.00, NULL, NULL, '2023-11-30 20:40:49', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_sub` VALUES (1730205272054026241, 1179884798563319808, 0, 1170089611624448000, 1163491458020278272, '6901028089185', 1, 150.00, 150.00, NULL, NULL, '2023-11-30 20:40:57', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_sub` VALUES (1730205327200735234, 1179884853693251584, 0, 1170089611624448000, 1163491458020278272, '6901028089185', 1, 150.00, 150.00, NULL, NULL, '2023-11-30 20:41:10', NULL, 0, NULL, 0);
INSERT INTO `receipt_retail_sub` VALUES (1731205684211060738, 1176109169703387136, 0, 1170091260111749120, 1163492331714772992, '6932529981586', 3, 66.00, 198.00, NULL, NULL, '2023-12-03 14:56:14', '2023-12-03 14:56:14', NULL, 0, 0);

-- ----------------------------
-- Table structure for receipt_sale_main
-- ----------------------------
DROP TABLE IF EXISTS `receipt_sale_main`;
CREATE TABLE `receipt_sale_main`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '主类型 (出库/入库)',
  `sub_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '子类型（销售订单/销售出库/销售退货）',
  `init_receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '初始单据编号',
  `receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单据编号',
  `receipt_date` datetime NULL DEFAULT NULL COMMENT '单据日期',
  `customer_id` bigint NULL DEFAULT NULL COMMENT '客户id',
  `account_id` bigint NULL DEFAULT NULL COMMENT '账户id',
  `change_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '变动金额(收款/付款)',
  `total_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '合计金额',
  `file_id` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '附件id（可以多个 逗号隔开）',
  `operator_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '业务员（可以多个 逗号隔开）',
  `multiple_account` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '多账户（可以多个 逗号隔开）',
  `multiple_account_amount` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '多账户金额 （可以多个 逗号隔开）',
  `discount_rate` decimal(12, 2) NULL DEFAULT NULL COMMENT '优惠率',
  `discount_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `discount_last_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '优惠后金额',
  `other_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '销售其他金额',
  `arrears_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '欠款金额',
  `deposit` decimal(12, 2) NULL DEFAULT NULL COMMENT '定金',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '备注',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态，0未审核、1已审核、2审核中、3部分销售、4完成销售',
  `receipt_source` tinyint(1) NULL DEFAULT 0 COMMENT '单据来源，0-pc，1-手机',
  `other_receipt` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '关联单据',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of receipt_sale_main
-- ----------------------------
INSERT INTO `receipt_sale_main` VALUES (1176164477033250816, 0, '订单', '销售订单', 'XSD1176164094814715904', 'XSD1176164094814715904', '2023-11-20 14:16:12', 1171837619361808384, 1713836226953875457, NULL, NULL, '', '1721744363082063873,1721773354815930370', '', '', 0.00, 0.00, 75.21, NULL, 0.00, 15.21, '', 1, 0, NULL, '2023-11-20 14:17:43', NULL, 0, NULL, 0);
INSERT INTO `receipt_sale_main` VALUES (1176165486291845120, 0, '出库', '销售出库', 'XSCK1176165194678665216', 'XSCK1176165194678665216', '2023-11-20 14:20:34', 1171837619361808384, 1713836226953875457, 50.00, NULL, '', '', '', '', 0.00, 0.00, 75.21, 0.00, 25.21, NULL, '测试销售出库', 0, 0, 'XSD1176164094814715904', '2023-11-20 14:21:44', '2023-11-23 14:48:18', 0, 0, 0);
INSERT INTO `receipt_sale_main` VALUES (1176165729255292928, 0, '入库', '销售退货', 'XSTH1176165592428707840', 'XSTH1176165592428707840', '2023-11-20 14:22:09', 1171837619361808384, 1726194331922579458, -10.00, NULL, '', '1721744363082063873', '', '', 0.00, 0.00, 18.00, 2.00, 10.00, NULL, '', 0, 0, '', '2023-11-20 14:22:42', '2023-12-02 20:59:32', 0, 0, 0);
INSERT INTO `receipt_sale_main` VALUES (1176243896158519296, 0, '出库', '销售出库', 'XSCK1176242940419244032', 'XSCK1176242940419244032', '2023-11-20 19:29:30', 1713135907563495426, 1713836226953875457, 12.00, NULL, '', '', '', '', 0.00, 0.00, 22.86, 0.00, 10.86, NULL, '', 0, 0, '', '2023-11-20 19:33:18', '2023-11-23 14:48:23', 0, 0, 0);
INSERT INTO `receipt_sale_main` VALUES (1177262614384738304, 0, '出库', '销售出库', 'XSCK1177262513750802432', 'XSCK1177262513750802432', '2023-11-23 15:00:55', 1171837619361808384, 1713836226953875457, 280.00, NULL, '', '1721744363082063873', '', '', 0.00, 0.00, 484.80, 0.00, 204.80, NULL, '测试', 0, 0, '', '2023-11-23 15:01:20', NULL, 0, NULL, 0);
INSERT INTO `receipt_sale_main` VALUES (1178789419788795904, 0, '出库', '销售出库', 'XSCK1178789376079953920', 'XSCK1178789376079953920', '2023-11-27 20:08:07', 1713135957840617474, NULL, 76.20, NULL, '', '', '', '', 0.00, 0.00, 76.20, 0.00, 0.00, NULL, '', 0, 0, '', '2023-11-27 20:08:19', NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for receipt_sale_sub
-- ----------------------------
DROP TABLE IF EXISTS `receipt_sale_sub`;
CREATE TABLE `receipt_sale_sub`  (
  `id` bigint NOT NULL COMMENT '销售单据子表id（主键）',
  `receipt_sale_main_id` bigint NOT NULL COMMENT '销售单据主表id',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NOT NULL COMMENT '商品Id',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库ID',
  `product_barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品条码',
  `product_number` int NULL DEFAULT NULL COMMENT '商品数量',
  `unit_price` decimal(13, 2) NULL DEFAULT NULL COMMENT '单价（这里不等于商品表的字段）因为单据会变动',
  `total_amount` decimal(13, 2) NULL DEFAULT NULL COMMENT '金额（这里不等于商品表的字段）因为单据会变动',
  `tax_rate` decimal(13, 2) NULL DEFAULT NULL COMMENT '税率',
  `tax_amount` decimal(13, 2) NULL DEFAULT NULL COMMENT '税额',
  `tax_included_amount` decimal(13, 2) NULL DEFAULT NULL COMMENT '价税合计（含税金额）',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '商品备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of receipt_sale_sub
-- ----------------------------
INSERT INTO `receipt_sale_sub` VALUES (1726484950611980289, 1176164477033250816, 0, 1170089611624448000, 1163492331714772992, '6901028075862', 3, 15.00, 45.00, 1.00, 0.45, 45.45, NULL, '2023-11-20 14:17:43', NULL, 0, NULL, 0);
INSERT INTO `receipt_sale_sub` VALUES (1726484950611980290, 1176164477033250816, 0, 1170091260111749120, 1163491458020278272, '6932529211107', 2, 5.50, 11.00, 2.00, 0.22, 11.22, NULL, '2023-11-20 14:17:43', NULL, 0, NULL, 0);
INSERT INTO `receipt_sale_sub` VALUES (1726484950611980291, 1176164477033250816, 0, 1172242448127098880, 1163492331714772992, '6941536185622', 4, 4.50, 18.00, 3.00, 0.54, 18.54, NULL, '2023-11-20 14:17:43', NULL, 0, NULL, 0);
INSERT INTO `receipt_sale_sub` VALUES (1727579808042848257, 1176165486291845120, 0, 1170089611624448000, 1163492331714772992, '6901028075862', 3, 15.00, 45.00, 1.00, 0.45, 45.45, NULL, '2023-11-23 14:48:18', '2023-11-23 14:48:18', NULL, 0, 0);
INSERT INTO `receipt_sale_sub` VALUES (1727579808042848258, 1176165486291845120, 0, 1170091260111749120, 1163491458020278272, '6932529211107', 2, 5.50, 11.00, 2.00, 0.22, 11.22, NULL, '2023-11-23 14:48:18', '2023-11-23 14:48:18', NULL, 0, 0);
INSERT INTO `receipt_sale_sub` VALUES (1727579808042848259, 1176165486291845120, 0, 1172242448127098880, 1163492331714772992, '6941536185622', 4, 4.50, 18.00, 3.00, 0.54, 18.54, NULL, '2023-11-23 14:48:18', '2023-11-23 14:48:18', NULL, 0, 0);
INSERT INTO `receipt_sale_sub` VALUES (1727579830113275905, 1176243896158519296, 0, 1172242448127098880, 1163492331714772992, '6941536185622', 2, 4.50, 9.00, 1.00, 0.09, 9.09, NULL, '2023-11-23 14:48:23', '2023-11-23 14:48:23', NULL, 0, 0);
INSERT INTO `receipt_sale_sub` VALUES (1727579830113275906, 1176243896158519296, 0, 1172242448127098880, 1163491458020278272, '6941536185622', 3, 4.50, 13.50, 2.00, 0.27, 13.77, NULL, '2023-11-23 14:48:23', '2023-11-23 14:48:23', NULL, 0, 0);
INSERT INTO `receipt_sale_sub` VALUES (1727583088508731394, 1177262614384738304, 0, 1170089611624448000, 1163492331714772992, '6901028075862', 32, 15.00, 480.00, 1.00, 4.80, 484.80, NULL, '2023-11-23 15:01:20', NULL, 0, NULL, 0);
INSERT INTO `receipt_sale_sub` VALUES (1729109893375881217, 1178789419788795904, 0, 1170089611624448000, 1163491458020278272, '6901028075862', 2, 15.00, 30.00, 1.00, 0.30, 30.30, NULL, '2023-11-27 20:08:19', NULL, 0, NULL, 0);
INSERT INTO `receipt_sale_sub` VALUES (1729109893384269825, 1178789419788795904, 0, 1170089611624448000, 1163492331714772992, '6901028075862', 3, 15.00, 45.00, 2.00, 0.90, 45.90, NULL, '2023-11-27 20:08:19', NULL, 0, NULL, 0);
INSERT INTO `receipt_sale_sub` VALUES (1730934724534845441, 1176165729255292928, 0, 1172242448127098880, 1163491458020278272, '6941536185622', 2, 4.50, 9.00, 0.00, 0.00, 9.00, NULL, '2023-12-02 20:59:32', '2023-12-02 20:59:32', NULL, 0, 0);
INSERT INTO `receipt_sale_sub` VALUES (1730934724534845442, 1176165729255292928, 0, 1172242448127098880, 1163492331714772992, '6941536185622', 2, 4.50, 9.00, 0.00, 0.00, 9.00, NULL, '2023-12-02 20:59:32', '2023-12-02 20:59:32', NULL, 0, 0);

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
INSERT INTO `supplier` VALUES (1712724937252876290, 0, '六轴齿轮供应商', '小伟', '021-78151562', NULL, NULL, NULL, NULL, 0, 180.000, NULL, NULL, NULL, 180.000, NULL, '16621211605', NULL, '1', NULL, NULL, 2.000, NULL, '2023-10-13 07:00:21', '2023-11-29 16:11:33', 0, 0, 0);
INSERT INTO `supplier` VALUES (1712724937257070594, 0, '软件供应商', '测试', '13379362915', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, 0.000, NULL, '16621211605', NULL, '1', NULL, NULL, NULL, NULL, '2023-10-13 07:00:21', '2023-11-29 16:29:41', 0, 0, 0);
INSERT INTO `supplier` VALUES (1712839652868173826, 1159563649187053568, '天津永胜食品有限公司', '王永胜', '021-1781516', 'shanxiaozhang@163.com', NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, 1757.000, NULL, '18027431919', NULL, 'DHC15610555FXITAL1', '中国银行', NULL, NULL, NULL, '2023-10-13 14:36:11', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `supplier` VALUES (1712842191592304642, 1159563649187053568, '万森（陕西）机器人有限公司', '赵伟', '16621211605', 'team@wansenai.com', NULL, NULL, NULL, 0, 850.000, NULL, NULL, NULL, 20466.250, NULL, '16621211605', '西安软件新城研发基地二期', '91610131MAC9KMEQ8J', '中国农业银行(西安科技路中段支行)', NULL, 3.000, NULL, '2023-10-13 14:46:16', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `supplier` VALUES (1713135795982426115, 0, '小张供应商', '小张', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 500.000, NULL, 500.000, NULL, '19991915192', NULL, NULL, '中国银行', '617819815222', 7.000, NULL, '2023-10-14 18:12:57', '2023-11-29 16:29:33', 0, 0, 0);
INSERT INTO `supplier` VALUES (1723264058620678146, 0, '天津永胜食品有限公司', '王永胜', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, 0.000, NULL, '16621211605', NULL, NULL, NULL, NULL, NULL, NULL, '2023-11-11 16:59:03', '2023-11-29 16:29:53', 0, 0, 0);

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
INSERT INTO `sys_department` VALUES (1154794589174239277, 0, NULL, '1154794589174239277', '万森机器人', '赵伟', 0, '智能机器人', '1', '2019-07-25 11:53:52', '2023-11-29 12:31:02', NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1157397928067727360, 0, 5574799175374231982, '1157397928067727360', '法务办公室', 'James', 1, NULL, '1', '2023-09-29 19:26:09', '2023-09-29 19:26:27', NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1159563040610320384, 0, 1154794589174239277, '测试', '测试', '赵伟', 1, NULL, '1', '2023-10-05 18:49:33', '2023-11-29 12:26:31', NULL, NULL, 1);
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
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件UID',
  `file_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文件名称',
  `file_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文件url（预览地址）',
  `file_download_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文件下载url',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小（KB）',
  `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES (1168258825359196160, 0, 'vc-upload-1698576085715-2', 'temp_1168064177592336384_会员信息模板.xlsx', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1168258445875347456_temp_1168064177592336384_%E4%BC%9A%E5%91%98%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF.xlsx', NULL, 18944, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1168258825380167680, 0, 'vc-upload-1698576085715-4', 'goods_template (1).xls', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1168258455224451072_goods_template%20%281%29.xls', NULL, 20992, 'application/vnd.ms-excel', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1168262660760797184, 0, 'vc-upload-1698577084191-4', 'temp_1168064177592336384_会员信息模板 (1).xlsx', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1168262570960748544_temp_1168064177592336384_%E4%BC%9A%E5%91%98%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF%20%281%29.xlsx', NULL, 18944, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1168355815812235264, 0, 'vc-upload-1698599283762-2', 'temp_1168064177592336384_会员信息模板 (1).xlsx', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1168355803401289728_temp_1168064177592336384_%E4%BC%9A%E5%91%98%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF%20%281%29.xlsx', NULL, 18944, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1168564888004460544, 0, '__AUTO__1698649179369_0__', 'temp_1168064177592336384_会员信息模板.xlsx', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1168333773792608256_temp_1168064177592336384_%E4%BC%9A%E5%91%98%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF.xlsx', NULL, 18944, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1171840430300463104, 0, 'vc-upload-1699427794717-17', 'SecurityRiskOf100030313822AK2023107.csv', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171840388462280704_SecurityRiskOf100030313822AK2023107.csv', NULL, 177, 'text/csv', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1171840430329823232, 0, 'vc-upload-1699427794717-19', 'C.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171840415809142784_C.png', NULL, 23656, 'image/png', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1171840878830944256, 0, 'vc-upload-1699427794717-21', 'AI改图-file-1722x1722.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171840767899992064_AI%E6%94%B9%E5%9B%BE-file-1722x1722.png', NULL, 156981, 'image/png', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1171840878839332864, 0, 'vc-upload-1699427794717-23', 'wansenai.svg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171840783108538368_wansenai.svg', NULL, 13521, 'image/svg+xml', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1172175282962956288, 0, 'vc-upload-1699509616448-2', '新建 文本文档 (2).txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1172174617175916544_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3%20%282%29.txt', NULL, 1389, 'text/plain', '2023-11-09 14:06:05', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1172264407397302272, 0, 'vc-upload-1699530321973-5', '微信图片_20230926113550.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1172261229440270336_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926113550.png', NULL, 21726, 'image/png', '2023-11-09 20:00:14', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1172264407409885184, 0, 'vc-upload-1699530321973-9', '新建 文本文档.txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1172264361171877888_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3.txt', NULL, 520, 'text/plain', '2023-11-09 20:00:14', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1172272935784677376, 0, 'vc-upload-1699531811003-15', '商品信息组件需求.doc', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1172272930558574592_%E5%95%86%E5%93%81%E4%BF%A1%E6%81%AF%E7%BB%84%E4%BB%B6%E9%9C%80%E6%B1%82.doc', NULL, 416768, 'application/msword', '2023-11-09 20:34:08', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1172536734873812992, 0, 'vc-upload-1699595111148-4', '新建 文本文档.txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1172532744018722816_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3.txt', NULL, 520, 'text/plain', '2023-11-10 14:02:22', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1172578230729703424, 0, 'vc-upload-1699604269248-16', '新建 文本文档 (2).txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1172578196621623296_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3%20%282%29.txt', NULL, 1389, 'text/plain', '2023-11-10 16:47:16', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1172657653965389824, 0, 'vc-upload-1699624873895-4', '新建 文本文档.txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1172657623120478208_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3.txt', NULL, 520, 'text/plain', '2023-11-10 22:02:52', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1172946865243553792, 0, 'vc-upload-1699693067656-11', '新建 文本文档 (2).txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1172946828354650112_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3%20%282%29.txt', NULL, 1389, 'text/plain', '2023-11-11 17:12:05', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1173012768773636096, 0, 'vc-upload-1699704402299-47', '微信图片_20230926162244.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1173008497445437441_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926162244.jpg', NULL, 356896, 'image/jpeg', '2023-11-11 21:33:58', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1173209981344808960, 0, 'vc-upload-1699710197882-3', '微信图片_20230926162244.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1173017040357687296_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926162244.jpg', NULL, 356896, 'image/jpeg', '2023-11-12 10:37:37', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1173225043589595136, 0, 'vc-upload-1699760091215-2', '新建 文本文档 (2).txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1173225034282434560_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3%20%282%29.txt', NULL, 1389, 'text/plain', '2023-11-12 11:37:28', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1173276343505256448, 0, 'vc-upload-1699624873895-6', '微信图片_20230926113550.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1172657744025485312_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926113550.png', NULL, 21726, 'image/png', '2023-11-12 15:01:19', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1173289850384678912, 0, 'vc-upload-1699693067656-11', '新建 文本文档 (2).txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1172946828354650112_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3%20%282%29.txt', NULL, 1389, 'text/plain', '2023-11-12 15:54:59', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1173289850401456128, 0, 'vc-upload-1699693067656-15', 'deleteTemp.bat', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1172948567627661312_deleteTemp.bat', NULL, 30, '', '2023-11-12 15:54:59', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1173311692528943104, 0, 'vc-upload-1699717044138-2', '微信图片_20230907220924.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1173043993974407168_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230907220924.jpg', NULL, 123807, 'image/jpeg', '2023-11-12 17:21:47', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1176592261249499136, 0, 'vc-upload-1700562670143-12', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1176592239850160128_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', '2023-11-21 18:37:35', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1176600180141588480, 0, 'vc-upload-1700563659103-13', '新建 文本文档 (2).txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1176599037957111808_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3%20%282%29.txt', NULL, 1389, 'text/plain', '2023-11-21 19:09:03', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1176604819884867584, 0, 'vc-upload-1700562670143-12', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1176592239850160128_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', '2023-11-21 19:27:29', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177268679323156480, 0, 'vc-upload-1700723356085-15', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177268512586989568_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', '2023-11-23 15:25:26', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177269668461674496, 0, 'vc-upload-1700723356085-21', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177269631996395520_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', '2023-11-23 15:29:22', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177269918983258112, 0, 'vc-upload-1700723356085-23', '微信图片_20230926162244.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177269877484814336_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926162244.jpg', NULL, 356896, 'image/jpeg', '2023-11-23 15:30:21', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177271644532506624, 0, 'vc-upload-1700723356085-23', '微信图片_20230926162244.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177269877484814336_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926162244.jpg', NULL, 356896, 'image/jpeg', '2023-11-23 15:37:13', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177308964426088448, 0, 'vc-upload-1700729691549-29', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177305340643901440_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', '2023-11-23 18:05:30', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177311184286646272, 0, 'vc-upload-1700734121075-4', '微信图片_20230907220924.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177311167069028352_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230907220924.jpg', NULL, 123807, 'image/jpeg', '2023-11-23 18:14:20', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177721356121276416, 0, 'vc-upload-1700830987717-19', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177721320339668992_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', '2023-11-24 21:24:12', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177722598251823104, 0, 'vc-upload-1700830987717-28', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177722569420177408_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', '2023-11-24 21:29:08', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177725125647138816, 0, 'vc-upload-1700830987717-31', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177723029313028096_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', '2023-11-24 21:39:11', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177725459530514432, 0, 'vc-upload-1700830987717-33', '微信图片_20230926113550.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177725351351025664_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926113550.png', NULL, 21726, 'image/png', '2023-11-24 21:40:31', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177726060104515584, 0, 'vc-upload-1700830987717-35', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177726055276871680_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', '2023-11-24 21:42:54', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177729012923891712, 0, 'vc-upload-1700830987717-37', '微信图片_20230926162244.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177728086934814720_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926162244.jpg', NULL, 356896, 'image/jpeg', '2023-11-24 21:54:38', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1177996670839816192, 0, 'vc-upload-1700897266468-8', 'logo.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177996237941506048_logo.png', NULL, 16025, 'image/png', '2023-11-25 15:38:12', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1178039130458685440, 0, 'vc-upload-1700907731406-2', '微信图片_20230926162244.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1178039108577001472_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926162244.jpg', NULL, 356896, 'image/jpeg', '2023-11-25 18:26:56', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1178039202034483200, 0, 'vc-upload-1700907731406-2', '微信图片_20230926162244.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1178039108577001472_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926162244.jpg', NULL, 356896, 'image/jpeg', '2023-11-25 18:27:13', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1178722328566038528, 0, 'vc-upload-1701070670363-5', '微信图片_20230625230336.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1178722296253120512_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230625230336.png', NULL, 48306, 'image/png', '2023-11-27 15:41:43', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1178725738946232320, 0, 'vc-upload-1701070670363-7', '微信图片_20230315092318.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1178725708290064384_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230315092318.jpg', NULL, 238622, 'image/jpeg', '2023-11-27 15:55:16', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1178731468852035584, 0, 'vc-upload-1701070670363-10', '微信图片_20230625230336.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1178730307017244672_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230625230336.png', NULL, 48306, 'image/png', '2023-11-27 16:18:02', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1178826754979004416, 0, 'vc-upload-1700741829362-3', '微信图片_20230926162244.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177342800694345728_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926162244.jpg', NULL, 356896, 'image/jpeg', '2023-11-27 22:36:40', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1179439660028395520, 0, 'vc-upload-1700897266468-4', '微信图片_20230926162244.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177994277364432896_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926162244.jpg', NULL, 356896, 'image/jpeg', '2023-11-29 15:12:08', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1179441176931991552, 0, 'vc-upload-1700908288049-2', '微信图片_20230926113550.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1178040394038902784_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926113550.png', NULL, 21726, 'image/png', '2023-11-29 15:18:10', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1179441213770563584, 0, 'vc-upload-1701070670363-15', '微信图片_20230625230336.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1178737143904731136_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230625230336.png', NULL, 48306, 'image/png', '2023-11-29 15:18:18', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1179444908709117952, 0, '__AUTO__1701243178071_0__', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1176521156740513792_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1179446604420087808, 0, 'vc-upload-1700634956248-2', '新建 文本文档 (2).txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1176894571200643072_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3%20%282%29.txt', NULL, 1389, 'text/plain', '2023-11-29 15:39:44', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1179447525401165824, 0, 'vc-upload-1700645811923-7', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1176940721869946880_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', '2023-11-29 15:43:23', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1179448094756962304, 0, 'vc-upload-1700734121075-2', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177310024536424448_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', '2023-11-29 15:45:39', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1179448135022280704, 0, 'vc-upload-1700741829362-10', '微信图片_20231119162439.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177343976856879104_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231119162439.png', NULL, 7893, 'image/png', '2023-11-29 15:45:48', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1179772936404336640, 0, 'vc-upload-1701226163241-2', '微信图片_20230926113550.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1179373675548246016_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926113550.png', NULL, 21726, 'image/png', '2023-11-30 13:16:27', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1179788203834474496, 0, 'vc-upload-1700898008786-2', '微信图片_20230926162244.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1177997259808178176_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926162244.jpg', NULL, 356896, 'image/jpeg', '2023-11-30 14:17:07', NULL, 0, NULL, 0);
INSERT INTO `sys_file` VALUES (1719996069041520642, 0, 'vc-upload-1698911527038-25', 'temp_1168064177592336384_会员信息模板 (1).xlsx', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1169675543767941120_temp_1168064177592336384_%E4%BC%9A%E5%91%98%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF%20%281%29.xlsx', NULL, 18944, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1720095533739618309, 0, 'vc-upload-1698937580538-3', 'yan2.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1169775022269530112_yan2.jpg', NULL, 176871, 'image/jpeg', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1720096968430710786, 0, 'vc-upload-1698938021668-2', 'yan1.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1169776460777390080_yan1.jpg', NULL, 191188, 'image/jpeg', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1720096968455876610, 0, 'vc-upload-1698938021668-4', 'yan2.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1169776467513442304_yan2.jpg', NULL, 176871, 'image/jpeg', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1720433064322605058, 0, 'vc-upload-1699016862890-3', '2023-09-27.doc', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1170112551011221504_2023-09-27.doc', NULL, 422400, 'application/msword', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1720488499687723009, 0, 'vc-upload-1699031089229-2', '8269db1f9ef8b798638378b6d3ea38b.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1170167257943244800_8269db1f9ef8b798638378b6d3ea38b.jpg', NULL, 53843, 'image/jpeg', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1720502736573247489, 0, 'vc-upload-1699034705019-2', '新建 文本文档 (2).txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1170182083683811328_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3%20%282%29.txt', NULL, 1389, 'text/plain', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1720502736573247490, 0, 'vc-upload-1699034705019-4', 'deleteTemp.bat', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1170182101497020416_deleteTemp.bat', NULL, 30, '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1720813758483890177, 0, 'vc-upload-1699108614227-2', '微信图片_20230926162244.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1170493216332447744_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230926162244.jpg', NULL, 356896, 'image/jpeg', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1721449701070925828, 0, 'vc-upload-1699259290026-2', '新建 文本文档 (2).txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171123977804840960_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3%20%282%29.txt', NULL, 1389, 'text/plain', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1721690231755300866, 0, 'vc-upload-1699317109177-7', '微信图片_20231107080923.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171368921115131904_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231107080923.png', NULL, 59461, 'image/png', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1721690231818215425, 0, 'vc-upload-1699317109177-9', '新建 文本文档.txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171368933425414144_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3.txt', NULL, 520, 'text/plain', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1721691375562919938, 0, 'vc-upload-1699317109177-11', 'deleteTemp.bat', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171370756538368000_deleteTemp.bat', NULL, 30, '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1721691375562919939, 0, 'vc-upload-1699317109177-15', '商品信息组件需求_1699275616305.doc', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171370895357247488_%E5%95%86%E5%93%81%E4%BF%A1%E6%81%AF%E7%BB%84%E4%BB%B6%E9%9C%80%E6%B1%82_1699275616305.doc', NULL, 416768, 'application/msword', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1721691470354190337, 0, 'vc-upload-1699317109177-17', '会员信息模板 (2).xlsx', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171370952341061632_%E4%BC%9A%E5%91%98%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF%20%282%29.xlsx', NULL, 18944, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1721917711099457537, 0, 'vc-upload-1699372065722-2', '会员信息模板 (3).xlsx', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171597033832710144_%E4%BC%9A%E5%91%98%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF%20%283%29.xlsx', NULL, 18944, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1721917711099457538, 0, 'vc-upload-1699372065722-4', '商品信息组件需求_1699275616305.doc', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171597043001458688_%E5%95%86%E5%93%81%E4%BF%A1%E6%81%AF%E7%BB%84%E4%BB%B6%E9%9C%80%E6%B1%82_1699275616305.doc', NULL, 416768, 'application/msword', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1722066238983233538, 0, 'vc-upload-1699407199908-4', '会员信息模板 (3).xlsx', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171745756583821312_%E4%BC%9A%E5%91%98%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF%20%283%29.xlsx', NULL, 18944, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1722145265827790849, 0, 'vc-upload-1699425657398-2', '新建 文本文档.txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171823322153877504_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3.txt', NULL, 520, 'text/plain', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1722145890242224129, 0, 'vc-upload-1699425657398-8', '微信图片_20230907220924.jpg', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171825370903609344_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20230907220924.jpg', NULL, 123807, 'image/jpeg', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1722150137109217283, 0, 'vc-upload-1699427522220-2', '2023-09-27.doc', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171829548166152192_2023-09-27.doc', NULL, 422400, 'application/msword', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1722158924025065474, 0, 'vc-upload-1699427794717-7', 'deleteTemp.bat', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171838284205129728_deleteTemp.bat', NULL, 30, '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1722159553095237634, 0, 'vc-upload-1699427794717-9', 'deleteTemp.bat', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171838928227926016_deleteTemp.bat', NULL, 30, '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1722159559239892994, 0, 'vc-upload-1699427794717-11', '新建 文本文档.txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171838939170865152_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3.txt', NULL, 520, 'text/plain', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1722160384637005826, 0, 'vc-upload-1699427794717-13', '微信图片_20231107080842.png', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171839858558107648_%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20231107080842.png', NULL, 8280, 'image/png', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_file` VALUES (1722160384637005827, 0, 'vc-upload-1699427794717-15', '新建 文本文档.txt', 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/temp_1171839904963887104_%E6%96%B0%E5%BB%BA%20%E6%96%87%E6%9C%AC%E6%96%87%E6%A1%A3.txt', NULL, 520, 'text/plain', NULL, NULL, NULL, NULL, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 319 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '功能模块表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 'Dashboard', '首页', 0, 1, '/dashboard', '/dashboard/analysis/index', NULL, 1, 0, 'ant-design:dashboard-outlined', 0, NULL, 0, 0, 0, 0, 0, 1, NULL, NULL, '2023-06-23 14:36:55', '2023-09-30 18:46:44', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (2, 'workbench', '工作台', 0, 1, '/dashboard/workbench', '/dashboard/workbench/index', NULL, 2, 0, 'ant-design:home-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-02 16:01:53', '2023-10-15 01:14:24', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (3, 'RetailManagement', '零售管理', 0, 0, '/retail', 'LAYOUT', NULL, 3, 0, 'ant-design:folder-open-outlined', 0, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-08-07 14:36:50', '2023-09-30 18:44:57', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (4, 'SystemManagement', '系统管理', 0, 0, '/sys', 'LAYOUT', NULL, 9, 0, 'ant-design:setting-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-09-30 14:36:33', '2023-11-04 20:53:12', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (5, 'PurchaseManagement', '采购管理', 0, 0, '/purchase', 'LAYOUT', NULL, 1, 0, 'ant-design:retweet-outlined', 0, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-10-02 14:39:13', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (6, 'SaleManagement', '销售管理', 0, 1, '/sales', 'LAYOUT', NULL, 1, 0, 'ant-design:shop-outlined', 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, '2023-09-30 14:39:29', '2023-11-04 20:55:47', NULL, NULL, 0);
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
INSERT INTO `sys_menu` VALUES (287, 'RetailShipments', '零售出库', 3, 1, '/retail/shipments', '/retail/shipments/index', NULL, 1, 0, 'ant-design:gift-twotone', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-25 20:28:51', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (288, 'advanceCharge', '收预付款', 8, 1, '/financial/advance-charge', '/financial/advance-charge/index', NULL, 6, 0, 'ant-design:pay-circle-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-10-28 19:15:46', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (289, 'RetailRefund', '零售退货', 3, 1, '/retail/refund', '/retail/refund/index', NULL, 2, 0, 'ant-design:history-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-04 20:54:58', '2023-11-29 12:32:20', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (290, 'SalesOrder', '销售订单', 6, 1, '/sales/order', '/sales/order/index', NULL, 1, 0, 'ant-design:pay-circle-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-04 20:57:23', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (291, 'SalesShipments', '销售出库', 6, 1, '/sales/shipments', '/sales/shipments/index', NULL, 2, 0, 'ant-design:shopping-cart-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-04 20:58:43', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (292, 'SalesRefund', '销售退货', 6, 1, '/sales/refund', '/sales/refund/index', NULL, 3, 0, 'ant-design:sync-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-04 20:59:44', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (293, 'PurchaseOrder', '采购订单', 5, 1, '/purchase/order', '/purchase/order/index', NULL, 1, 0, 'ant-design:star-twotone', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-04 21:01:10', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (294, 'PurchaseStorage', '采购入库', 5, 1, '/purchase/storage', '/purchase/storage/index', NULL, 2, 0, 'ant-design:home-twotone', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-04 21:02:22', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (295, 'PurchaseRefund', '采购退货', 5, 1, '/purchase/refund', '/purchase/refund/index', NULL, 3, 0, 'ant-design:send-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-04 21:03:14', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (296, 'ProductStock', '商品库存', 9, 1, '/report/productStock', '/report/productStock', NULL, 1, 0, 'ant-design:pie-chart-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-15 12:49:20', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (297, 'AccountStatistics', '账户统计', 9, 1, '/report/accountStatistics', '/report/accountStatistics', NULL, 2, 0, 'ant-design:skype-filled', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-17 21:13:28', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (298, 'RetailStatistics', '零售统计', 9, 1, '/report/retailStatistics', '/report/retailStatistics', NULL, 3, 0, 'ant-design:pie-chart-twotone', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-19 20:24:51', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (299, 'PurchaseStatistics', '采购统计', 9, 1, '/report/purchaseStatistics', '/report/purchaseStatistics', NULL, 3, 0, 'ant-design:signal-filled', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-20 13:34:31', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (300, 'SaleStatistics', '销售统计', 9, 1, '/report/saleStatistics', '/report/saleStatistics', NULL, 5, 0, 'ant-design:schedule-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-20 13:35:34', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (301, 'ShipmentsDetailStatistics', '出库明细', 9, 1, '/report/shipmentsDetail', '/report/shipmentsDetail', NULL, 6, 0, 'ant-design:line-chart-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-20 17:27:39', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (302, 'StorageDetailStatistics', '入库明细', 9, 1, '/report/storageDetail', '/report/storageDetail', NULL, 7, 0, 'ant-design:money-collect-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-20 17:28:53', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (303, 'ShipmentsSummary', '出库汇总', 9, 1, '/report/shipmentsSummary', '/report/shipmentsSummary', NULL, 8, 0, 'ant-design:area-chart-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-20 21:20:09', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (304, 'StorageSummary', '入库汇总', 9, 1, '/report/storageSummary', '/report/storageSummary', NULL, 9, 0, 'ant-design:bar-chart-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-20 21:20:58', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (305, 'IncomeExpense', '收支项目', 11, 1, '/basic/income-expense', '/basic/income-expense/index', NULL, 7, 0, 'ant-design:bank-twotone', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-21 10:34:47', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (306, 'IncomeReceipt', '收入单', 8, 1, '/financial/income', '/financial/income/index', NULL, 2, 0, 'ant-design:pay-circle-filled', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-21 16:59:36', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (307, 'ExpenseReceipt', '支出单', 8, 1, '/financial/expense', '/financial/expense/index', NULL, 3, 0, 'ant-design:money-collect-twotone', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-22 14:34:18', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (308, 'TransferReceipt', '转账单', 8, 1, '/financial/transfer', '/financial/transfer/index', NULL, 4, 0, 'ant-design:pound-circle-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-22 17:36:48', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (309, 'CollectionReceipt', '收款单', 8, 1, '/financial/collection', '/financial/collection/index', NULL, 5, 0, 'ant-design:instagram-filled', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-22 22:44:51', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (310, 'PaymentReceipt', '付款单', 8, 1, '/financial/payment', '/financial/payment/index', NULL, 6, 0, 'ant-design:account-book-twotone', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-23 20:17:06', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (311, 'OtherStorage', '其他入库', 7, 1, '/warehouse/storage', '/warehouse/storage/index', NULL, 1, 0, 'ant-design:appstore-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-24 21:01:38', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (312, 'OtherShipments', '其他出库', 7, 1, '/warehouse/shipments', '/warehouse/shipments/index', NULL, 2, 0, 'ant-design:shop-twotone', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-25 15:25:32', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (313, 'AllotShipments', '调拨出库', 7, 1, '/warehouse/allot', '/warehouse/allot/index', NULL, 3, 0, 'ant-design:tags-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-25 18:17:47', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (314, 'AssembleReceipt', '组装单', 7, 1, '/warehouse/assemble', '/warehouse/assemble/index', NULL, 4, 0, 'ant-design:bar-chart-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-27 14:37:56', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (315, 'DisAssembleReceipt', '拆卸单', 7, 1, '/warehouse/disassemble', '/warehouse/disassemble/index', NULL, 5, 0, 'ant-design:bars-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-27 14:38:40', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (316, 'CustomerBillStatistics', '客户对账', 9, 1, '/report/customerBill', '/report/customerBill', NULL, 10, 0, 'ant-design:pay-circle-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-27 19:52:49', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (317, 'SupplierBillStatistics', '供应商对账', 9, 1, '/report/supplierBill', '/report/supplierBill', NULL, 11, 0, 'ant-design:like-outlined', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-27 22:26:03', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (318, '测试', '12321', NULL, 1, '32131', '', NULL, 3123, 0, '123', 0, 0, 0, 0, 0, 0, 0, 0, '', '', '2023-11-29 12:32:32', NULL, NULL, NULL, 1);

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
INSERT INTO `sys_platform_config` VALUES (19, 'tencent_sms_secret_id', '腾讯短信服务SId', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (20, 'tencent_sms_secret_key', '腾讯短信服务SKey', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (21, 'tencent_sms_client', '腾讯短信服务地区', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (22, 'tencent_sms_sdk_appId', '腾讯短信服务SDK', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (23, 'tencent_oss_secret_id', '腾讯对象存储SId', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (24, 'tencent_oss_secret_key', '腾讯对象存储Skey', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (25, 'tencent_oss_region', '腾讯对象存储服务地区', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_platform_config` VALUES (26, 'tencent_oss_bucket', '腾讯对象存储桶', '', NULL, NULL, NULL, NULL);

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
INSERT INTO `sys_role` VALUES (1, 0, '租户', '全部数据', 2, '通用数据', 0, '2023-09-25 19:51:47', '2023-11-29 11:49:27', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (1159563649187053569, 1159563649187053568, '租户管理员', '全部数据', NULL, '租户注册后的默认角色 租户管理员有所有权限', 0, '2023-10-05 18:51:58', NULL, 1159563649187053568, NULL, 0);
INSERT INTO `sys_role` VALUES (1159564417168310272, 1159563649187053568, '测试角色', '全部数据', NULL, '', 0, '2023-10-05 18:55:01', NULL, NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (1159565451349458944, 1159563649187053569, '财务人员', '全部数据', NULL, NULL, 0, '2023-10-05 18:59:07', NULL, NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (1179388709980602368, 0, '测试', '个人数据', 1, '啊啊', 1, '2023-11-29 11:49:40', '2023-11-29 11:49:52', NULL, NULL, 0);

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
INSERT INTO `sys_role_menu_rel` VALUES (0, 0, 0, '[1][2][3][4][5][6][7][8][9][10][11][12][13][14][15][16][17][18][19][20][21][22][23][24][25][26][287][288][289][290][291][292][293][294][295][296][297][298][299][300][301][302][303][304][305][306][307][308][309][310][311][312][313][314][315][316][317][318]', '2023-09-12 19:52:22', '2023-09-12 19:52:24', 0, 0);
INSERT INTO `sys_role_menu_rel` VALUES (1159563649203830784, 1159563649187053568, 1159563649187053569, '[1][2][3][4][5][6][7][8][9][10][11][12][13][14][16][17][18][19][20][21][22][23][24][25][26][287][288][289][290][291][292][293][294][295]', '2023-10-05 18:51:58', NULL, 1159563649187053568, NULL);
INSERT INTO `sys_role_menu_rel` VALUES (1709885426275819522, NULL, 1159564417168310272, '[1][2][3][5][11]', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_menu_rel` VALUES (1709885982713159682, NULL, 1159565451349458944, '[1][2][8]', NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_menu_rel` VALUES (1729707728492425218, NULL, 1, '[1][2][26][11][20][21][22][23][24][25][305]', NULL, NULL, NULL, NULL);

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
INSERT INTO `sys_user` VALUES (0, 0, '管理员', 'admin', '6931072e53348972c5309447a5ae3c0c', 0, '集团管理员', 'jameszow@wansen.email', '16621211605', 'https://points.wansen.cloud/group1/default/20230821/12/50/4/tmp_b389e880bb493e7249181dd7f6708cfd.jpg?download=0', 1, 0, 0, NULL, NULL, NULL, '2023-09-14 22:00:28', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132865, 0, '王有田', 'youtian', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'testyu@wansenai.com', '17015963215', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132870, 0, '李法群', 'tli18716', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'htomassl@qq.com', '13379815236', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132879, 0, '张晓东', 'xiaodongzhang', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'yuxiuaa@tecia.com', '18015156235', NULL, 1, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132893, 0, '黄磊', 'hl6789', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'biosss@126.com', '15618529781', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_user` VALUES (1153648835588132895, 0, '王一亭', 'wangyt', 'e10adc3949ba59abbe56e057f20f883e', 0, NULL, 'ciarsit@163.com', '16621211606', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
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
  `id` bigint NOT NULL COMMENT '主键',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '机构用户关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_dept_rel
-- ----------------------------
INSERT INTO `sys_user_dept_rel` VALUES (1157714147601809409, 0, 1154756575114956805, 0, NULL, 0, '2023-09-30 16:22:42', NULL, 1151247731927683077, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159252910790410240, 0, 1154794589174239277, 0, NULL, 0, '2023-10-04 22:17:12', NULL, 1159252418010021888, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562436399857664, 0, 1154794589170044930, 1151247731927683082, NULL, 0, '2023-10-05 18:47:08', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562506344071168, 0, 1154794589170044930, 1153648835588132870, NULL, 0, '2023-10-05 18:47:25', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562541039353856, 0, 1154794589174239242, 1153648835588132879, NULL, 0, '2023-10-05 18:47:33', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562541043548160, 0, 1154794589174239268, 1153648835588132879, NULL, 0, '2023-10-05 18:47:33', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562576258924544, 0, 1154794589174239268, 1153648835588132895, NULL, 0, '2023-10-05 18:47:42', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562576258924545, 0, 1154794589174239242, 1153648835588132895, NULL, 0, '2023-10-05 18:47:42', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562603685478400, 0, 1154490573429018634, 1153648835588132897, NULL, 0, '2023-10-05 18:47:48', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562603689672704, 0, 1154794589174239268, 1153648835588132897, NULL, 0, '2023-10-05 18:47:48', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1159562627454599168, 0, 1154794589174239242, 1153648835588132900, NULL, 0, '2023-10-05 18:47:54', NULL, 0, NULL);
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
INSERT INTO `sys_user_dept_rel` VALUES (1179396887015325696, 0, 1154794589170044930, 1153648835588132865, NULL, 0, '2023-11-29 12:22:10', NULL, 0, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1179397790309023744, 0, 1154794589174239242, 1153648835588132912, NULL, 0, '2023-11-29 12:25:45', NULL, 0, NULL);

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
INSERT INTO `sys_user_role_rel` VALUES (1159562506327293952, 0, 1153648835588132870, 1, '2023-10-05 18:47:25', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159562541030965248, 0, 1153648835588132879, 1, '2023-10-05 18:47:33', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159562576246341632, 0, 1153648835588132895, 1, '2023-10-05 18:47:42', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159562603677089792, 0, 1153648835588132897, 1, '2023-10-05 18:47:48', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159562627446210560, 0, 1153648835588132900, 1, '2023-10-05 18:47:54', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159563921766481920, 1159563649187053568, 1159563649187053568, 1159563649187053569, '2023-10-05 18:53:03', NULL, 1159563649187053568, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1159564587205394432, 1159563649187053568, 1159564587188617216, 1159564417168310272, '2023-10-05 18:55:41', NULL, 1159563649187053568, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1162519548251865088, 1159563649187053568, 1159565124227301376, 1159564417168310272, '2023-10-13 14:37:39', NULL, 1159563649187053568, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1179396887006937088, 0, 1153648835588132865, 1, '2023-11-29 12:22:10', NULL, 0, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1179397790304829440, 0, 1153648835588132912, 1, '2023-11-29 12:25:45', NULL, 0, NULL);

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
-- Table structure for warehouse_receipt_main
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_receipt_main`;
CREATE TABLE `warehouse_receipt_main`  (
  `id` bigint NOT NULL COMMENT '主键id',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `related_person_id` bigint NULL DEFAULT NULL COMMENT '关联人id(客户/供应商)',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类型(入库/出库/调拨/组装/拆卸)',
  `init_receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '初始单据编号',
  `receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单据编号',
  `receipt_date` datetime NULL DEFAULT NULL COMMENT '单据日期',
  `total_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '合计金额',
  `total_product_number` int NULL DEFAULT NULL COMMENT '商品总数量',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '备注',
  `file_id` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '附件id（可以多个 逗号隔开）',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态，0未审核、1已审核',
  `source` tinyint(1) NULL DEFAULT 0 COMMENT '单据来源，0-pc，1-手机',
  `other_receipt` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '关联单据',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of warehouse_receipt_main
-- ----------------------------
INSERT INTO `warehouse_receipt_main` VALUES (1177729881304203264, 0, 1712724937206738945, 1170089611624448000, '其他入库', 'QTRK1177729840250355712', '7', '2023-11-24 21:57:55', 73.50, 7, '测试3333', '1177996670839816192', 0, 0, NULL, '2023-11-24 21:58:05', '2023-11-25 15:38:12', 1153648835588132895, 0, 1);
INSERT INTO `warehouse_receipt_main` VALUES (1177994283773329408, 0, 1171837619361808384, 1170089611624448000, '其他出库', 'QTCK1177994142098128896', 'QTCK1177994142098128896', '2023-11-25 15:28:09', 28.50, 4, '各个出库一件 32K60追加2件。', '1179439660028395520', 0, 0, NULL, '2023-11-25 15:28:43', '2023-11-29 15:12:08', 0, 0, 0);
INSERT INTO `warehouse_receipt_main` VALUES (1177997271464148992, 0, 1713135795982426115, 1170089611624448000, '其他入库', 'QTRK1177997179533393920', 'QTRK1177997179533393920', '2023-11-25 15:40:13', 54.00, 5, '入库测试222', '1179788203834474496', 0, 0, NULL, '2023-11-25 15:40:36', '2023-11-30 14:17:07', 0, 0, 0);
INSERT INTO `warehouse_receipt_main` VALUES (1178040400124837888, 0, NULL, 1170089611624448000, '调拨出库', 'DBCK1178040288422133760', 'DBCK1178040288422133760', '2023-11-25 18:31:31', 30.00, 2, '调出2包', '1179441176931991552', 0, 0, NULL, '2023-11-25 18:31:58', '2023-11-29 15:18:10', 0, 0, 0);
INSERT INTO `warehouse_receipt_main` VALUES (1178730314160144384, 0, NULL, 1170089611624448000, '组装单', 'ADD1178730233033916416', 'ADD1178730233033916416', '2023-11-27 16:13:07', 99.00, 8, '测试111', '1178731468852035584', 0, 0, NULL, '2023-11-27 16:13:27', '2023-11-27 16:18:02', 0, 0, 0);
INSERT INTO `warehouse_receipt_main` VALUES (1178737148237447168, 0, NULL, 1170089611624448000, '拆卸单', 'CXD1178737105661067264', 'CXD1178737105661067264', '2023-11-27 16:40:25', 97.50, 10, '测试3', '1179441213770563584', 0, 0, NULL, '2023-11-27 16:40:36', '2023-11-29 15:18:18', 0, 0, 0);
INSERT INTO `warehouse_receipt_main` VALUES (1179437846067412992, 0, 1712724937252876290, 1170089611624448000, '其他入库', 'QTRK1179437805151977472', 'QTRK1179437805151977472', '2023-11-29 15:04:45', 450.00, 3, '333', '', 0, 0, NULL, '2023-11-29 15:04:55', NULL, 0, NULL, 1);
INSERT INTO `warehouse_receipt_main` VALUES (1180541458994692096, 0, NULL, 1172242448127098880, '拆卸单', 'CXD1180541361917526016', 'CXD1180541361917526016', '2023-12-02 16:09:53', 13.50, 3, '撤销', '', 1, 0, NULL, '2023-12-02 16:10:17', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_main` VALUES (1180885016524095488, 0, 1713135957840617474, 1170089611624448000, '其他出库', 'QTCK1180884972924305408', 'QTCK1180884972924305408', '2023-12-03 14:55:17', 81.00, 2, '测试', '', 0, 0, NULL, '2023-12-03 14:55:28', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_main` VALUES (1180895986340331520, 0, 1712724937252876290, 1172242448127098880, '其他入库', 'QTRK1180895943722008576', 'QTRK1180895943722008576', '2023-12-03 15:38:52', 13.50, 3, '测试', '', 0, 0, NULL, '2023-12-03 15:39:03', NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for warehouse_receipt_sub
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_receipt_sub`;
CREATE TABLE `warehouse_receipt_sub`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `warehouse_receipt_main_id` bigint NULL DEFAULT NULL COMMENT '仓库主表id',
  `product_id` bigint NOT NULL COMMENT '商品Id',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库ID',
  `other_warehouse_id` bigint NULL DEFAULT NULL COMMENT '调拨对方仓库ID',
  `product_barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品条码',
  `product_number` int NULL DEFAULT NULL COMMENT '商品数量',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型（组合件/普通子件）',
  `unit_price` decimal(13, 2) NULL DEFAULT NULL COMMENT '单价（这里不等于商品表的字段）因为单据会变动',
  `total_amount` decimal(13, 2) NULL DEFAULT NULL COMMENT '总金额（这里不等于商品表的字段）因为单据会变动',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '商品备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of warehouse_receipt_sub
-- ----------------------------
INSERT INTO `warehouse_receipt_sub` VALUES (1177996671057920000, 0, 1177729881304203264, 1170089611624448000, 1163491458020278272, NULL, '6901028075862', 4, NULL, 15.00, 60.00, '测试4444', '2023-11-25 15:38:13', NULL, 0, NULL, 1);
INSERT INTO `warehouse_receipt_sub` VALUES (1177996671057920001, 0, 1177729881304203264, 1172242448127098880, 1163492331714772992, NULL, '6941536185622', 3, NULL, 4.50, 13.50, '测试33', '2023-11-25 15:38:13', NULL, 0, NULL, 1);
INSERT INTO `warehouse_receipt_sub` VALUES (1178731469170802688, 0, 1178730314160144384, 1170089611624448000, 1163491458020278272, NULL, '6901028075862', 6, '组合件', 15.00, 90.00, '', '2023-11-27 16:18:02', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1178731469170802689, 0, 1178730314160144384, 1172242448127098880, 1163491458020278272, NULL, '6941536185622', 2, '普通子件', 4.50, 9.00, NULL, '2023-11-27 16:18:02', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1179437846067412993, 0, 1179437846067412992, 1170089611624448000, 1163492331714772992, NULL, '6901028089185', 3, NULL, 150.00, 450.00, NULL, '2023-11-29 15:04:55', NULL, 0, NULL, 1);
INSERT INTO `warehouse_receipt_sub` VALUES (1179439660150030336, 0, 1177994283773329408, 1170089611624448000, 1163492331714772992, NULL, '6901028075862', 1, NULL, 15.00, 15.00, '出库', '2023-11-29 15:12:08', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1179439660150030337, 0, 1177994283773329408, 1172242448127098880, 1163491458020278272, NULL, '6941536185622', 3, NULL, 4.50, 13.50, '出库', '2023-11-29 15:12:08', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1179441177020071936, 0, 1178040400124837888, 1170089611624448000, 1163491458020278272, 1163492331714772992, '6901028075862', 2, NULL, 15.00, 30.00, NULL, '2023-11-29 15:18:10', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1179441213854449664, 0, 1178737148237447168, 1170089611624448000, 1163491458020278272, NULL, '6901028075862', 5, '组合件', 15.00, 75.00, '', '2023-11-29 15:18:18', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1179441213854449665, 0, 1178737148237447168, 1172242448127098880, 1163491458020278272, NULL, '6941536185622', 5, '普通子件', 4.50, 22.50, NULL, '2023-11-29 15:18:18', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1179788203943526400, 0, 1177997271464148992, 1170089611624448000, 1163491458020278272, NULL, '6901028075862', 3, NULL, 15.00, 45.00, NULL, '2023-11-30 14:17:07', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1179788203943526401, 0, 1177997271464148992, 1172242448127098880, 1163492331714772992, NULL, '6941536185622', 2, NULL, 4.50, 9.00, NULL, '2023-11-30 14:17:07', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1180541458994692097, 0, 1180541458994692096, 1172242448127098880, 1163492331714772992, NULL, '6941536185622', 1, '组合件', 4.50, 4.50, '', '2023-12-02 16:10:17', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1180541458998886400, 0, 1180541458994692096, 1172242448127098880, 1163491458020278272, NULL, '6941536185622', 2, '普通子件', 4.50, 9.00, NULL, '2023-12-02 16:10:17', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1180885016528289792, 0, 1180885016524095488, 1170089611624448000, 1163492331714772992, NULL, '6901028075862', 1, NULL, 15.00, 15.00, NULL, '2023-12-03 14:55:28', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1180885016528289793, 0, 1180885016524095488, 1170091260111749120, 1163492331714772992, NULL, '6932529981586', 1, NULL, 66.00, 66.00, NULL, '2023-12-03 14:55:28', NULL, 0, NULL, 0);
INSERT INTO `warehouse_receipt_sub` VALUES (1180895986340331521, 0, 1180895986340331520, 1172242448127098880, 1163492331714772992, NULL, '6941536185622', 3, NULL, 4.50, 13.50, '测试', '2023-12-03 15:39:03', NULL, 0, NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
