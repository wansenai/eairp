/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : wansenerp2

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 23/09/2023 21:48:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for financial_account
-- ----------------------------
DROP TABLE IF EXISTS `financial_account`;
CREATE TABLE `financial_account`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `account_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `serial_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '编号',
  `initial_amount` decimal(24, 6) NULL DEFAULT NULL COMMENT '期初金额',
  `current_amount` decimal(24, 6) NULL DEFAULT NULL COMMENT '当前余额',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '启用',
  `sort` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '排序',
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
INSERT INTO `financial_account` VALUES (17, 63, '账户1', 'zzz111', 100.000000, 829.000000, 'aabb', 1, NULL, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_account` VALUES (18, 63, '账户2', '1234131324', 200.000000, -1681.000000, 'bbbb', 1, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `financial_account` VALUES (24, NULL, 'aaa', 'aaa', 0.000000, NULL, NULL, 1, NULL, 0, NULL, NULL, NULL, NULL, 0);

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
-- Table structure for operator
-- ----------------------------
DROP TABLE IF EXISTS `operator`;
CREATE TABLE `operator`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类型',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态（0-启用, 1-停用）',
  `sort` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '排序',
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
INSERT INTO `operator` VALUES (14, 63, '小李', '业务员', 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `operator` VALUES (15, 63, '小军', '仓管员', 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `operator` VALUES (16, 63, '小夏', '财务员', 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `operator` VALUES (17, 63, '小曹', '财务员', 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `operator` VALUES (18, NULL, '赵伟', '业务员', 1, '2', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_category_id` bigint NULL DEFAULT NULL COMMENT '产品类型id',
  `product_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `product_manufacturer` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '制造商',
  `product_model` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '型号',
  `product_standard` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '规格',
  `product_color` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '颜色',
  `product_unit_id` bigint NULL DEFAULT NULL COMMENT '计量单位Id',
  `product_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单位-单个',
  `product_expiry_num` int NULL DEFAULT NULL COMMENT '保质期天数',
  `product_img_name` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '图片名称',
  `product_weight` decimal(24, 6) NULL DEFAULT NULL COMMENT '基础重量(kg)',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '启用 0-禁用  1-启用',
  `other_field_one` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '自定义1',
  `other_field_two` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '自定义2',
  `other_field_three` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '自定义3',
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
INSERT INTO `product` VALUES (568, 63, 17, '商品1', '制1', 'sp1', '', '', NULL, '个', NULL, NULL, NULL, '', 1, '', '', '', 0, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product` VALUES (569, 63, 17, '商品2', '', 'sp2', '', '', NULL, '只', NULL, NULL, NULL, '', 1, '', '', '', 0, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product` VALUES (570, 63, 17, '商品3', '', 'sp3', '', '', NULL, '个', NULL, NULL, NULL, '', 1, '', '', '', 0, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product` VALUES (577, 63, NULL, '商品8', '', 'sp8', '', '', 15, '', NULL, NULL, NULL, '', 1, '', '', '', 0, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product` VALUES (579, 63, 21, '商品17', '', 'sp17', '', '', 15, '', NULL, NULL, NULL, '', 1, '', '', '', 0, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product` VALUES (586, 63, 17, '序列号商品测试', '', 'xlh123', '', '', NULL, '个', NULL, NULL, NULL, '', 1, '', '', '', 1, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product` VALUES (587, 63, 17, '商品test1', '南通中远', '', 'test1', '', NULL, '个', NULL, NULL, NULL, '', 1, '', '', '', 0, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product` VALUES (588, 63, 21, '商品200', 'fafda', 'weqwe', '300ml', '红色', NULL, '个', NULL, NULL, NULL, 'aaaabbbbb', 1, '', '', '', 0, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product` VALUES (619, 63, NULL, '衣服', NULL, NULL, NULL, NULL, NULL, '件', NULL, '', NULL, NULL, 1, NULL, NULL, NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product` VALUES (620, NULL, NULL, 'wansentech', NULL, NULL, NULL, NULL, 15, '', 111, '', NULL, NULL, 1, NULL, NULL, NULL, 0, 0, '技术支持', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for product_attribute
-- ----------------------------
DROP TABLE IF EXISTS `product_attribute`;
CREATE TABLE `product_attribute`  (
  `id` bigint NOT NULL,
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `attribute_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `attribute_value` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '属性值',
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
INSERT INTO `product_attribute` VALUES (1, 63, '多颜色', '红色|橙色|黄色|绿色|蓝色|紫色', NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (2, 63, '多尺寸', 'S|M|L|XL|XXL|XXXL', NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (3, 63, '自定义1', '小米|华为', NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (4, 63, '自定义2', NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_attribute` VALUES (5, 63, '自定义3', NULL, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `category_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `category_level` smallint NULL DEFAULT NULL COMMENT '等级',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '上级id',
  `sort` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '显示顺序',
  `serial_number` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '编号',
  `remark` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
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
INSERT INTO `product_category` VALUES (17, 63, '目录1', NULL, NULL, '11', 'wae12', 'eee', '2019-04-10 22:18:12', '2021-02-17 15:11:35', NULL, NULL, 0);
INSERT INTO `product_category` VALUES (21, 63, '目录2', NULL, 17, '22', 'ada112', 'ddd', '2020-07-20 23:08:44', '2020-07-20 23:08:44', NULL, NULL, 0);
INSERT INTO `product_category` VALUES (29, 63, '海鲜水产', NULL, NULL, NULL, 'HX0001', NULL, '2023-08-30 14:55:13', '2023-08-30 14:55:13', NULL, NULL, 0);
INSERT INTO `product_category` VALUES (30, 63, '测试水产', NULL, NULL, '1', 'HX0001', '111', '2023-08-30 15:27:51', '2023-08-30 15:27:51', NULL, NULL, 0);

-- ----------------------------
-- Table structure for product_extend_price
-- ----------------------------
DROP TABLE IF EXISTS `product_extend_price`;
CREATE TABLE `product_extend_price`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品id',
  `product_bar_code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '商品条码',
  `product_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '商品单位',
  `multi_attribute` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '多属性',
  `purchase_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '采购价格',
  `retail_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '零售价格',
  `sale_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '销售价格',
  `low_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '最低售价',
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
INSERT INTO `product_extend_price` VALUES (1, 63, 587, '1000', '个', NULL, 11.000000, 22.000000, 22.000000, 22.000000, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (2, 63, 568, '1001', '个', NULL, 11.000000, 15.000000, 15.000000, 15.000000, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (3, 63, 569, '1002', '只', NULL, 10.000000, 15.000000, 15.000000, 13.000000, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (4, 63, 570, '1003', '个', NULL, 8.000000, 15.000000, 14.000000, 13.000000, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (5, 63, 577, '1004', '个', NULL, 10.000000, 20.000000, 20.000000, 20.000000, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (6, 63, 577, '1005', '箱', NULL, 120.000000, 240.000000, 240.000000, 240.000000, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (7, 63, 579, '1006', '个', NULL, 20.000000, 30.000000, 30.000000, 30.000000, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (8, 63, 579, '1007', '箱', NULL, 240.000000, 360.000000, 360.000000, 360.000000, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (9, 63, 586, '1008', '个', NULL, 12.000000, 15.000000, 15.000000, 15.000000, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (10, 63, 588, '1009', '个', NULL, 11.000000, 22.000000, 22.000000, 22.000000, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (36, 63, 619, '1014', '件', '橙色,M', 12.000000, 15.000000, 14.000000, NULL, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (37, 63, 619, '1015', '件', '橙色,L', 12.000000, 20.000000, 14.000000, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (38, 63, 619, '1016', '件', '绿色,M', 12.000000, 15.000000, 14.000000, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (39, 63, 619, '1017', '件', '绿色,L', 12.000000, 15.000000, 14.000000, NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (40, NULL, 620, '1020', '个', '', 1.000000, 2.000000, 3.000000, 4.000000, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_extend_price` VALUES (41, NULL, 620, '1021', '箱', '', 33.000000, 32.000000, 12.000000, 6.000000, 0, NULL, NULL, NULL, NULL, 0);

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
-- Table structure for product_inventory_current
-- ----------------------------
DROP TABLE IF EXISTS `product_inventory_current`;
CREATE TABLE `product_inventory_current`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NULL DEFAULT NULL COMMENT '产品id',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库id',
  `current_stock_quantity` decimal(24, 6) NULL DEFAULT NULL COMMENT '当前库存数量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '产品当前库存' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of product_inventory_current
-- ----------------------------
INSERT INTO `product_inventory_current` VALUES (19, 63, 588, 14, 24.000000, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_inventory_current` VALUES (20, 63, 568, 14, -219.000000, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_inventory_current` VALUES (21, 63, 568, 15, 222.000000, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_inventory_current` VALUES (22, 63, 570, 14, 8.000000, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_inventory_current` VALUES (23, 63, 619, 14, 5.000000, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_inventory_current` VALUES (24, 63, 619, 15, 0.000000, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_inventory_current` VALUES (25, 63, 619, 17, 0.000000, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_inventory_current` VALUES (26, NULL, 586, 14, 1.000000, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for product_inventory_initial
-- ----------------------------
DROP TABLE IF EXISTS `product_inventory_initial`;
CREATE TABLE `product_inventory_initial`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NULL DEFAULT NULL COMMENT '产品id',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库id',
  `init_stock_quantity` decimal(24, 6) NULL DEFAULT NULL COMMENT '初始库存数量',
  `low_stock_quantity` decimal(24, 6) NULL DEFAULT NULL COMMENT '最低库存数量',
  `high_stock_quantity` decimal(24, 6) NULL DEFAULT NULL COMMENT '最高库存数量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '产品初始库存' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of product_inventory_initial
-- ----------------------------

-- ----------------------------
-- Table structure for product_unit
-- ----------------------------
DROP TABLE IF EXISTS `product_unit`;
CREATE TABLE `product_unit`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称，支持多单位',
  `basic_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '基础单位',
  `other_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '副单位',
  `other_unit_two` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '副单位2',
  `other_unit_three` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '副单位3',
  `ratio` decimal(24, 3) NULL DEFAULT NULL COMMENT '比例',
  `ratio_two` decimal(24, 3) NULL DEFAULT NULL COMMENT '比例2',
  `ratio_three` decimal(24, 3) NULL DEFAULT NULL COMMENT '比例3',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '启用',
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
INSERT INTO `product_unit` VALUES (15, 63, '个/(箱=12个)', '个', '箱', NULL, NULL, 12.000, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_unit` VALUES (19, 63, '个/(盒=15个)', '个', '盒', NULL, NULL, 15.000, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_unit` VALUES (20, 63, '盒/(箱=8盒)', '盒', '箱', NULL, NULL, 8.000, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `product_unit` VALUES (21, 63, '瓶/(箱=12瓶)', '瓶', '箱', NULL, NULL, 12.000, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  `supplier_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '供应商名称',
  `contact` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_number` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `remake` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_system` tinyint NULL DEFAULT NULL COMMENT '是否系统自带 0==系统 1==非系统',
  `type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类型',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态（0-启用，1-停用）',
  `advance_receivable` decimal(24, 6) NULL DEFAULT 0.000000 COMMENT '预收款',
  `begin_account_receivable` decimal(24, 6) NULL DEFAULT NULL COMMENT '期初应收',
  `begin_account_payment` decimal(24, 6) NULL DEFAULT NULL COMMENT '期初应付',
  `total_receivable` decimal(24, 6) NULL DEFAULT NULL COMMENT '累计应收',
  `total_payment` decimal(24, 6) NULL DEFAULT NULL COMMENT '累计应付',
  `fax` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '传真',
  `phone_number` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机',
  `address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '地址',
  `tax_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '纳税人识别号',
  `bank_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `account_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '账号',
  `tax_rate` decimal(24, 6) NULL DEFAULT NULL COMMENT '税率',
  `sort` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '供应商/客户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of supplier
-- ----------------------------
INSERT INTO `supplier` VALUES (57, 63, '供应商1', '小军', '12345678', '', '', NULL, '供应商', 1, 0.000000, 0.000000, 0.000000, 0.000000, 4.000000, '', '15000000000', '地址1', '', '', '', 12.000000, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `supplier` VALUES (58, 63, '客户1', '小李', '12345678', '', '', NULL, '客户', 1, 0.000000, 0.000000, 0.000000, -100.000000, NULL, '', '', '', '', '', '', 12.000000, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `supplier` VALUES (59, 63, '客户2', '小陈', '', '', '', NULL, '客户', 1, 0.000000, 0.000000, 0.000000, 0.000000, NULL, '', '', '', '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `supplier` VALUES (60, 63, '12312666', '小曹', '', '', '', NULL, '会员', 1, 970.000000, 0.000000, 0.000000, NULL, NULL, '', '13000000000', '', '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `supplier` VALUES (68, 63, '供应商3', '晓丽', '12345678', '', 'fasdfadf', NULL, '供应商', 1, 0.000000, 0.000000, 0.000000, 0.000000, -35.000000, '', '13000000000', 'aaaa', '1341324', '', '', 13.000000, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `supplier` VALUES (71, 63, '客户3', '小周', '', '', '', NULL, '客户', 1, 0.000000, 0.000000, 0.000000, 0.000000, NULL, '', '', '', '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `supplier` VALUES (74, 63, '供应商5', '小季', '77779999', '', '', NULL, '供应商', 1, 0.000000, 0.000000, 5.000000, 0.000000, 5.000000, '', '15806283912', '', '', '', '', 3.000000, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `supplier` VALUES (90, NULL, '供应商666', '赵伟', '021-1515165', 'jameszow@wansen.mail', NULL, NULL, '供应商', 1, 0.000000, NULL, 20.000000, NULL, NULL, NULL, '16621211605', '陕西省西安市', NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL, NULL, 0);
INSERT INTO `supplier` VALUES (91, NULL, '0724198719', NULL, '166 2121 1605', 'jameszow@wansen.mail', NULL, NULL, '客户', 1, 0.000000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `supplier` VALUES (92, NULL, '0724198719', '112131', NULL, NULL, NULL, NULL, '会员', 1, 0.000000, NULL, NULL, NULL, NULL, NULL, '16621211605', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);

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
INSERT INTO `sys_config` VALUES (11, 63, '万森（陕西）机器人有限公司', '赵伟', '陕西省西安市高新区软件新城A6', '16621211605', NULL, NULL, '注：本单为我公司与客户约定账期内结款的依据，由客户或其单位员工签字生效，并承担法律责任。', 0, 0, 1, 0, 0, '', 0, 1, 0, 0);

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
INSERT INTO `sys_department` VALUES (12, 63, NULL, '001', '测试机构', NULL, 'aaaa2', '2', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (13, 63, 12, 'jg1', '机构1', NULL, '', '3', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (14, 63, 13, '12', '机构2', NULL, '', '4', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1154490573429018634, 0, 1154756575114956805, '2', '技术团队', NULL, NULL, '2', '2023-09-21 18:53:51', '2023-09-21 18:53:48', NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1154756575114956805, 0, NULL, '1', '万森智能部门', NULL, NULL, '1', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1154794589170044930, 0, 1154794589174239277, '2', '硬件研发团队', NULL, NULL, '1', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1154794589174239242, 0, 1154794589174239277, '1', '销售团队', NULL, NULL, '2', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1154794589174239268, 0, 1154756575114956805, NULL, '运营团队', NULL, NULL, '3', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_department` VALUES (1154794589174239277, 0, NULL, NULL, '万森机器人', NULL, NULL, '2', NULL, NULL, NULL, NULL, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 260 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '功能模块表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 'Dashboard', '首页', NULL, 1, '/dashboard', '/dashboard/workbench/index', NULL, 1, 1, 'ant-design:home-outlined', 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (2, 'FileManagement', '零售管理', NULL, 1, '/fms/file', '/fms/file/index', NULL, 2, 1, 'ant-design:folder-open-outlined', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (3, 'SystemManagement', '系统管理', NULL, 0, '/sys', 'LAYOUT', NULL, 999, NULL, 'ant-design:tool-outlined', 0, 0, 0, 0, 0, 0, 0, '', '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (13, 'RoleManagement', '角色管理', 3, 1, '/role', '/sys/role/index', NULL, 2, 1, 'ant-design:user-outlined', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (14, 'UserManagement', '用户管理', 3, 1, '/user', '/sys/user/index', NULL, 3, 1, 'ant-design:user-outlined', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (15, 'DepartmentManagement', '部门管理', 3, 1, '/department', '/sys/department/index', NULL, 160, 1, 'ic:outline-people-alt', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, '功能管理', NULL, 1, 1, '/system/function', '/system/FunctionList', NULL, 166, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, '租户管理', NULL, 1, 1, '/system/tenant', '/system/TenantList', NULL, 167, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, '商品管理', NULL, 0, 1, '/material', '/layouts/TabLayout', NULL, 620, 1, 'shopping', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, '商品类别', NULL, 101, 1, '/material/material_category', '/material/MaterialCategoryList', NULL, 230, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, '商品信息', NULL, 101, 1, '/material/material', '/material/MaterialList', NULL, 240, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, '基本资料', NULL, 0, 1, '/systemA', '/layouts/TabLayout', NULL, 750, 1, 'appstore', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (25, '供应商信息', NULL, 102, 1, '/system/vendor', '/system/VendorList', NULL, 260, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (26, '仓库信息', NULL, 102, 1, '/system/depot', '/system/DepotList', NULL, 270, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (31, '经手人管理', NULL, 102, 1, '/system/person', '/system/PersonList', NULL, 284, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (32, '采购管理', NULL, 0, 1, '/bill', '/layouts/TabLayout', NULL, 330, 1, 'retweet', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (33, '采购入库', NULL, 502, 1, '/bill/purchase_in', '/bill/PurchaseInList', NULL, 340, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (38, '销售管理', NULL, 0, 1, '/billB', '/layouts/TabLayout', NULL, 390, 1, 'shopping-cart', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (40, '调拨出库', NULL, 801, 1, '/bill/allocation_out', '/bill/AllocationOutList', NULL, 807, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (41, '销售出库', NULL, 603, 1, '/bill/sale_out', '/bill/SaleOutList', NULL, 394, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (44, '财务管理', NULL, 0, 1, '/financial', '/layouts/TabLayout', NULL, 450, 1, 'money-collect', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (59, '进销存统计', NULL, 301, 1, '/report/in_out_stock_report', '/report/InOutStockReport', NULL, 658, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (194, '收支项目', NULL, 102, 1, '/system/in_out_item', '/system/InOutItemList', NULL, 282, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (195, '结算账户', NULL, 102, 1, '/system/account', '/system/AccountList', NULL, 283, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (197, '收入单', NULL, 704, 1, '/financial/item_in', '/financial/ItemInList', NULL, 465, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (198, '报表查询', NULL, 0, 1, '/report', '/layouts/TabLayout', NULL, 570, 1, 'pie-chart', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (199, '采购退货', NULL, 502, 1, '/bill/purchase_back', '/bill/PurchaseBackList', NULL, 345, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (200, '销售退货', NULL, 603, 1, '/bill/sale_back', '/bill/SaleBackList', NULL, 396, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (201, '其它入库', NULL, 801, 1, '/bill/other_in', '/bill/OtherInList', NULL, 803, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (202, '其它出库', NULL, 801, 1, '/bill/other_out', '/bill/OtherOutList', NULL, 805, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (203, '支出单', NULL, 704, 1, '/financial/item_out', '/financial/ItemOutList', NULL, 470, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (204, '收款单', NULL, 704, 1, '/financial/money_in', '/financial/MoneyInList', NULL, 475, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (205, '付款单', NULL, 704, 1, '/financial/money_out', '/financial/MoneyOutList', NULL, 480, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (206, '转账单', NULL, 704, 1, '/financial/giro', '/financial/GiroList', NULL, 490, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (207, '账户统计', NULL, 301, 1, '/report/account_report', '/report/AccountReport', NULL, 610, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (208, '采购统计', NULL, 301, 1, '/report/buy_in_report', '/report/BuyInReport', NULL, 620, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (209, '销售统计', NULL, 301, 1, '/report/sale_out_report', '/report/SaleOutReport', NULL, 630, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (210, '零售出库', NULL, 401, 1, '/bill/retail_out', '/bill/RetailOutList', NULL, 405, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (211, '零售退货', NULL, 401, 1, '/bill/retail_back', '/bill/RetailBackList', NULL, 407, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (212, '收预付款', NULL, 704, 1, '/financial/advance_in', '/financial/AdvanceInList', NULL, 495, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (217, '客户信息', NULL, 102, 1, '/system/customer', '/system/CustomerList', NULL, 262, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (218, '会员信息', NULL, 102, 1, '/system/member', '/system/MemberList', NULL, 263, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (220, '计量单位', NULL, 101, 1, '/system/unit', '/system/UnitList', NULL, 245, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (226, '入库明细', NULL, 301, 1, '/report/in_detail', '/report/InDetail', NULL, 640, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (227, '出库明细', NULL, 301, 1, '/report/out_detail', '/report/OutDetail', NULL, 645, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (228, '入库汇总', NULL, 301, 1, '/report/in_material_count', '/report/InMaterialCount', NULL, 650, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (229, '出库汇总', NULL, 301, 1, '/report/out_material_count', '/report/OutMaterialCount', NULL, 655, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (232, '组装单', NULL, 801, 1, '/bill/assemble', '/bill/AssembleList', NULL, 809, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (233, '拆卸单', NULL, 801, 1, '/bill/disassemble', '/bill/DisassembleList', NULL, 811, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (234, '系统配置', NULL, 1, 1, '/system/system_config', '/system/SystemConfigList', NULL, 165, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (235, '客户对账', NULL, 301, 1, '/report/customer_account', '/report/CustomerAccount', NULL, 660, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (236, '商品属性', NULL, 1, 1, '/material/material_property', '/material/MaterialPropertyList', NULL, 168, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (237, '供应商对账', NULL, 301, 1, '/report/vendor_account', '/report/VendorAccount', NULL, 665, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (239, '仓库管理', NULL, 0, 1, '/billD', '/layouts/TabLayout', NULL, 420, 1, 'hdd', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (241, '采购订单', NULL, 502, 1, '/bill/purchase_order', '/bill/PurchaseOrderList', NULL, 335, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (242, '销售订单', NULL, 603, 1, '/bill/sale_order', '/bill/SaleOrderList', NULL, 392, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (243, '机构管理', NULL, 1, 1, '/system/organization', '/system/OrganizationList', NULL, 150, 1, 'profile', 1, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (244, '库存预警', NULL, 301, 1, '/report/stock_warning_report', '/report/StockWarningReport', NULL, 670, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (245, '插件管理', NULL, 1, 1, '/system/plugin', '/system/PluginList', NULL, 170, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (246, '商品库存', NULL, 301, 1, '/report/material_stock', '/report/MaterialStock', NULL, 605, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (247, '多属性', NULL, 101, 1, '/material/material_attribute', '/material/MaterialAttributeList', NULL, 250, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (248, '调拨明细', NULL, 301, 1, '/report/allocation_detail', '/report/AllocationDetail', NULL, 646, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (258, '平台配置', NULL, 1, 1, '/system/platform_config', '/system/PlatformConfigList', NULL, 175, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (259, '零售统计', NULL, 301, 1, '/report/retail_out_report', '/report/RetailOutReport', NULL, 615, 1, 'profile', 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);

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

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类型',
  `price_limit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '价格屏蔽 1-屏蔽采购价 2-屏蔽零售价 3-屏蔽销售价',
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
INSERT INTO `sys_role` VALUES (1151153250280804356, '租户', '全部数据', NULL, '', 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (1151153250280804397, '销售经理', '全部数据', NULL, '查询全部数据', 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (1151153250280804400, '销售代表', '个人数据', NULL, '智能查询个人数据', 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (1151247731923488769, '管理员', '全部数据', NULL, NULL, 1, NULL, NULL, NULL, NULL, 0);

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu_rel
-- ----------------------------
INSERT INTO `sys_role_menu_rel` VALUES (111, 0, 3333333, '[1]', '2023-09-12 20:59:26', NULL, NULL, NULL);
INSERT INTO `sys_role_menu_rel` VALUES (1151243989895483414, 0, 1151247731923488769, '[1][2][3][13][14][15]', '2023-09-12 19:52:22', '2023-09-12 19:52:24', 0, 0);

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
INSERT INTO `sys_user` VALUES (1151247731923488777, 1151153829895868454, '测试用户2', 'test2', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, NULL, NULL, NULL, 1, 0, 0, NULL, NULL, NULL, '2023-09-14 22:00:26', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1151247731927683073, 0, '万森小杨', 'xiaoyang', 'e10adc3949ba59abbe56e057f20f883e', 0, '总监', '7777777@qq.com', '', 'https://points.wansen.cloud/group1/default/20230821/12/50/4/tmp_b389e880bb493e7249181dd7f6708cfd.jpg?download=0', 1, 0, 0, '', NULL, NULL, '2023-09-14 22:00:30', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1151247731927683077, 0, '管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 0, '集团管理员', 'jameszow@wansen.email', '16621211605', 'https://points.wansen.cloud/group1/default/20230821/12/50/4/tmp_b389e880bb493e7249181dd7f6708cfd.jpg?download=0', 1, 0, 0, NULL, NULL, NULL, '2023-09-14 22:00:28', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1151247731927683082, 0, '测试用户', 'test', 'e10adc3949ba59abbe56e057f20f883e', 0, '主管', '666666@qq.com', '', NULL, 1, 1, 0, '', NULL, NULL, '2023-09-14 22:00:32', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1151934773204090880, 1151934773204090881, '赵伟', 'zhaowei', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, '', NULL, NULL, 1, 0, 0, NULL, NULL, NULL, '2023-09-14 22:00:35', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132865, 0, '王有田', 'youtian', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'testyu@wansenai.com', '17015963215', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132870, 0, '李法群', 'tli18716', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'htomassl@qq.com', '13379815236', NULL, 1, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132879, 0, '张晓东', 'xiaodongzhang', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'yuxiuaa@tecia.com', '18015156235', NULL, 1, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132893, 0, '黄磊', 'hl6789', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'biosss@126.com', '15618529781', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132895, 0, '王一亭', 'wangyt', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'ciarsit@163.com', '15015151623', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132897, 0, '梁伟', 'lw17816152316', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'cestuis@163.com', '17816152316', NULL, 1, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132900, 0, '梁超飞', 'chaofei7788', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'hjunweiu@163.com', '17715151623', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1153648835588132912, 0, '孙婷', 'sunting', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, 'asdjjamsai@hotmail.com', '18027431919', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1154127100467216384, 1154127100471410688, '李丽红', 'ouoppy', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, NULL, '16621211608', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (1154139420769648640, 1154139420773842944, '杨雅', 'yangxiaoya', '7bc7b277e3436b88e2494da6194ce807', 0, NULL, NULL, '16621211606', NULL, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);

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
  `user_dept_sort` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户在所属部门中显示顺序',
  `delete_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1154812104042483719 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '机构用户关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_dept_rel
-- ----------------------------
INSERT INTO `sys_user_dept_rel` VALUES (1154490573424824344, 0, 1154756575114956805, 1151247731927683077, '1', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1154756575110762507, 0, 1154794589174239277, 1151247731927683077, '0', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1154812104038289408, NULL, 1154490573429018634, 1153648835588132900, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1154812104038289409, NULL, 1154490573429018634, 1153648835588132912, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1154812104038289412, NULL, 1154794589174239268, 1153648835588132897, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1154812104038289420, NULL, 1154794589174239268, 1153648835588132895, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1154812104038289431, NULL, 1154794589174239242, 1153648835588132893, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1154812104038289436, NULL, 1154794589174239242, 1153648835588132879, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1154812104038289446, NULL, 1154756575114956805, 1151247731927683073, '1', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1154812104038289447, NULL, 1154794589170044930, 1153648835588132870, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1154812104042483713, NULL, 1154756575114956805, 1151247731927683082, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1154812104042483715, NULL, 1154794589170044930, 1153648835588132865, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_dept_rel` VALUES (1154812104042483718, NULL, 1154794589170044930, 1151934773204090880, NULL, 0, NULL, NULL, NULL, NULL);

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role_rel
-- ----------------------------
INSERT INTO `sys_user_role_rel` VALUES (2, 1151153829895868463, 1151247731927683077, 3333333, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1151154778412552229, 0, 1151247731927683077, 1151247731923488769, '2023-09-12 13:59:06', '2023-09-12 13:59:08', 0, 0);
INSERT INTO `sys_user_role_rel` VALUES (1155222611291410455, 0, 1153648835588132870, 1151153250280804356, '2023-09-23 19:22:39', '2023-09-23 19:22:42', NULL, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1155222611291410462, 0, 1153648835588132895, 1151153250280804400, '2023-09-23 19:23:14', '2023-09-23 19:23:17', NULL, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1155232990910353443, 0, 1153648835588132897, 1151153250280804397, '2023-09-23 20:04:31', NULL, NULL, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1155240913224994821, 0, 1153648835588132865, 1151153250280804397, '2023-09-23 20:35:26', NULL, NULL, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1155240913224994822, 0, 1153648835588132912, 1151153250280804400, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1155240913224994825, 0, 1153648835588132879, 1151153250280804400, '2023-09-23 20:35:45', NULL, NULL, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1155240913224994826, 0, 1153648835588132893, 1151153250280804356, '2023-09-23 20:36:09', NULL, NULL, NULL);
INSERT INTO `sys_user_role_rel` VALUES (1155240913224994831, 0, 1153648835588132900, 1151153250280804400, '2023-09-23 20:36:33', NULL, NULL, NULL);

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
  `principal` bigint NULL DEFAULT NULL COMMENT '负责人',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '仓库地址',
  `warehous_price` decimal(24, 6) NULL DEFAULT NULL COMMENT '仓储费',
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
