-- MySQL dump 10.13  Distrib 8.4.3, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: eairp
-- ------------------------------------------------------
-- Server version	8.4.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS eairp CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE eairp;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户名称',
  `contact` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '联系人',
  `phone_number` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '手机',
  `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '电子邮箱',
  `fax` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '传真',
  `first_quarter_account_receivable` decimal(12,2) DEFAULT '0.00' COMMENT '一季度应收账款',
  `second_quarter_account_receivable` decimal(12,2) DEFAULT NULL COMMENT '二季度应收账款',
  `third_quarter_account_receivable` decimal(12,2) DEFAULT NULL COMMENT '三季度应收账款',
  `fourth_quarter_account_receivable` decimal(12,2) DEFAULT NULL COMMENT '四季度应收账款',
  `total_account_receivable` decimal(24,2) DEFAULT NULL COMMENT '累计应收账款',
  `address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '地址',
  `tax_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '纳税人识别号',
  `bank_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '开户行',
  `account_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '账号',
  `tax_rate` decimal(12,2) DEFAULT NULL COMMENT '税率',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-启用，1-停用）默认启用',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `financial_account`
--

DROP TABLE IF EXISTS `financial_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `financial_account` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `account_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '名称',
  `account_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '编号',
  `initial_amount` decimal(12,2) DEFAULT '0.00' COMMENT '期初金额',
  `current_amount` decimal(12,2) DEFAULT '0.00' COMMENT '当前余额',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT NULL COMMENT '启用',
  `sort` int DEFAULT NULL COMMENT '排序',
  `is_default` tinyint(1) DEFAULT NULL COMMENT '是否默认',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='账户信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `financial_account`
--

LOCK TABLES `financial_account` WRITE;
/*!40000 ALTER TABLE `financial_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `financial_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `financial_main`
--

DROP TABLE IF EXISTS `financial_main`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `financial_main` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `related_person_id` bigint DEFAULT NULL COMMENT '关联人id(会员/客户/供应商)',
  `operator_id` bigint DEFAULT NULL COMMENT '经手人id',
  `account_id` bigint DEFAULT NULL COMMENT '账户id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '类型(支出/收入/收款/付款/转账)',
  `change_amount` decimal(12,2) DEFAULT NULL COMMENT '变动金额(优惠/收款/付款/实付)',
  `discount_amount` decimal(12,2) DEFAULT NULL COMMENT '优惠金额',
  `total_amount` decimal(12,2) DEFAULT NULL COMMENT '合计金额',
  `receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '单据编号',
  `receipt_source` tinyint(1) DEFAULT '0' COMMENT '单据来源，0-pc，1-手机',
  `receipt_date` datetime DEFAULT NULL COMMENT '单据日期',
  `remark` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `file_id` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '附件ID（多个用逗号分隔）',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态，0未审核、1已审核、9审核中',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK9F4C0D8DB610FC06` (`related_person_id`) USING BTREE,
  KEY `FK9F4C0D8DAAE50527` (`account_id`) USING BTREE,
  KEY `FK9F4C0D8DC4170B37` (`operator_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='财务主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `financial_main`
--

LOCK TABLES `financial_main` WRITE;
/*!40000 ALTER TABLE `financial_main` DISABLE KEYS */;
/*!40000 ALTER TABLE `financial_main` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `financial_sub`
--

DROP TABLE IF EXISTS `financial_sub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `financial_sub` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `financial_main_id` bigint NOT NULL COMMENT '财务主表id',
  `account_id` bigint DEFAULT NULL COMMENT '账户Id',
  `income_expense_id` bigint DEFAULT NULL COMMENT '收支项目Id',
  `other_receipt` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联单据',
  `receivable_payment_arrears` decimal(12,2) DEFAULT NULL COMMENT '应收/付 欠款',
  `received_prepaid_arrears` decimal(12,2) DEFAULT NULL COMMENT '已收/付 欠款',
  `single_amount` decimal(12,2) DEFAULT NULL COMMENT '单项金额',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '单据备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK9F4CBAC0AAE50527` (`account_id`) USING BTREE,
  KEY `FK9F4CBAC0C5FE6007` (`financial_main_id`) USING BTREE,
  KEY `FK9F4CBAC0D203EDC5` (`income_expense_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='财务子表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `financial_sub`
--

LOCK TABLES `financial_sub` WRITE;
/*!40000 ALTER TABLE `financial_sub` DISABLE KEYS */;
/*!40000 ALTER TABLE `financial_sub` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `income_expense`
--

DROP TABLE IF EXISTS `income_expense`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `income_expense` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '名称',
  `type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '类型',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT '0' COMMENT '启用',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='收支项目';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `income_expense`
--

LOCK TABLES `income_expense` WRITE;
/*!40000 ALTER TABLE `income_expense` DISABLE KEYS */;
/*!40000 ALTER TABLE `income_expense` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `member_number` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '会员卡号',
  `member_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员名称',
  `phone_number` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '手机',
  `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '电子邮箱',
  `advance_payment` decimal(12,3) DEFAULT NULL COMMENT '预付款',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-启用，1-停用）默认启用',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operator`
--

DROP TABLE IF EXISTS `operator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operator` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id（预留字段后续考虑加到用户表关联角色）',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '姓名',
  `type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '类型',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态（0-启用, 1-停用）',
  `sort` int DEFAULT NULL COMMENT '排序',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='经手人表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operator`
--

LOCK TABLES `operator` WRITE;
/*!40000 ALTER TABLE `operator` DISABLE KEYS */;
/*!40000 ALTER TABLE `operator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `product_category_id` bigint DEFAULT NULL COMMENT '产品类型id',
  `product_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '名称',
  `product_manufacturer` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '制造商',
  `product_model` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '型号',
  `product_standard` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '规格',
  `product_color` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '颜色',
  `product_unit_id` bigint DEFAULT NULL COMMENT '计量单位Id',
  `product_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '单位',
  `product_expiry_num` int DEFAULT NULL COMMENT '保质期天数',
  `product_weight` decimal(12,3) DEFAULT NULL COMMENT '基础重量(kg)',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT NULL COMMENT '启用 0-禁用  1-启用',
  `other_field_one` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '自定义1',
  `other_field_two` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '自定义2',
  `other_field_three` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '自定义3',
  `enable_serial_number` tinyint(1) DEFAULT '0' COMMENT '是否开启序列号，0否，1是',
  `enable_batch_number` tinyint(1) DEFAULT '0' COMMENT '是否开启批号，0否，1是',
  `warehouse_shelves` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '仓位货架',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK675951272AB6672C` (`product_category_id`) USING BTREE,
  KEY `UnitId` (`product_unit_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='产品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_attribute`
--

DROP TABLE IF EXISTS `product_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_attribute` (
  `id` bigint NOT NULL,
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `attribute_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性名',
  `attribute_value` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性值',
  `sort` int DEFAULT NULL COMMENT '排序',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='产品属性表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_attribute`
--

LOCK TABLES `product_attribute` WRITE;
/*!40000 ALTER TABLE `product_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_category`
--

DROP TABLE IF EXISTS `product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_category` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `category_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '名称',
  `category_number` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '编号',
  `parent_id` bigint DEFAULT NULL COMMENT '上级id',
  `sort` int DEFAULT NULL COMMENT '显示顺序',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK3EE7F725237A77D8` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='产品类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_category`
--

LOCK TABLES `product_category` WRITE;
/*!40000 ALTER TABLE `product_category` DISABLE KEYS */;
INSERT INTO `product_category` VALUES (1336722611224182784,0,'11','11',NULL,NULL,NULL,'2025-02-05 15:38:26',NULL,0,NULL,0);
/*!40000 ALTER TABLE `product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_extend_price`
--

DROP TABLE IF EXISTS `product_extend_price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_extend_price` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `product_id` bigint DEFAULT NULL COMMENT '商品id',
  `product_bar_code` int DEFAULT NULL COMMENT '商品条码',
  `product_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品单位',
  `multi_attribute` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '多属性',
  `purchase_price` decimal(12,3) DEFAULT NULL COMMENT '采购价格',
  `retail_price` decimal(12,3) DEFAULT NULL COMMENT '零售价格',
  `sale_price` decimal(12,3) DEFAULT NULL COMMENT '销售价格',
  `low_price` decimal(12,3) DEFAULT NULL COMMENT '最低售价',
  `default_flag` tinyint(1) DEFAULT '1' COMMENT '是否为默认单位，1是，0否',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='产品价格扩展';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_extend_price`
--

LOCK TABLES `product_extend_price` WRITE;
/*!40000 ALTER TABLE `product_extend_price` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_extend_price` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_extend_property`
--

DROP TABLE IF EXISTS `product_extend_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_extend_property` (
  `id` bigint NOT NULL COMMENT '主键',
  `native_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '原始名称',
  `status` tinyint(1) DEFAULT NULL COMMENT '是否启用',
  `another_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '别名',
  `sort` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='产品扩展字段表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_extend_property`
--

LOCK TABLES `product_extend_property` WRITE;
/*!40000 ALTER TABLE `product_extend_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_extend_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_image`
--

DROP TABLE IF EXISTS `product_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_image` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `product_id` bigint DEFAULT NULL COMMENT '商品id',
  `uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片上传的uid',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片类型（png jpg jpeg）',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片上传状态',
  `image_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '图片名称',
  `image_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '图片地址',
  `image_size` int DEFAULT NULL COMMENT '图片大小 mb',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_image`
--

LOCK TABLES `product_image` WRITE;
/*!40000 ALTER TABLE `product_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_inventory_current`
--

DROP TABLE IF EXISTS `product_inventory_current`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_inventory_current` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `product_id` bigint DEFAULT NULL COMMENT '产品id',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库id',
  `current_stock_quantity` decimal(24,6) DEFAULT NULL COMMENT '当前库存数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='产品当前库存';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_inventory_current`
--

LOCK TABLES `product_inventory_current` WRITE;
/*!40000 ALTER TABLE `product_inventory_current` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_inventory_current` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_inventory_initial`
--

DROP TABLE IF EXISTS `product_inventory_initial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_inventory_initial` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `product_id` bigint DEFAULT NULL COMMENT '产品id',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库id',
  `init_stock_quantity` decimal(24,6) DEFAULT NULL COMMENT '初始库存数量',
  `low_stock_quantity` decimal(24,6) DEFAULT NULL COMMENT '最低库存数量',
  `high_stock_quantity` decimal(24,6) DEFAULT NULL COMMENT '最高库存数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='产品初始库存';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_inventory_initial`
--

LOCK TABLES `product_inventory_initial` WRITE;
/*!40000 ALTER TABLE `product_inventory_initial` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_inventory_initial` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_property`
--

DROP TABLE IF EXISTS `product_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_property` (
  `id` bigint NOT NULL COMMENT '主键',
  `native_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '原始名称',
  `status` tinyint(1) DEFAULT NULL COMMENT '是否启用',
  `another_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '别名',
  `sort` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='产品扩展字段表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_property`
--

LOCK TABLES `product_property` WRITE;
/*!40000 ALTER TABLE `product_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_sku`
--

DROP TABLE IF EXISTS `product_sku`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_sku` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `product_id` bigint DEFAULT NULL COMMENT '商品id',
  `product_bar_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品条码',
  `product_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品单位',
  `multi_attribute` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '多属性',
  `purchase_price` decimal(12,3) DEFAULT NULL COMMENT '采购价格',
  `retail_price` decimal(12,3) DEFAULT NULL COMMENT '零售价格',
  `sale_price` decimal(12,3) DEFAULT NULL COMMENT '销售价格',
  `low_price` decimal(12,3) DEFAULT NULL COMMENT '最低售价',
  `default_flag` tinyint(1) DEFAULT '1' COMMENT '是否为默认单位，1是，0否',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='产品价格扩展';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_sku`
--

LOCK TABLES `product_sku` WRITE;
/*!40000 ALTER TABLE `product_sku` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_sku` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_stock`
--

DROP TABLE IF EXISTS `product_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_stock` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `product_sku_id` bigint DEFAULT NULL COMMENT '产品扩展id',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库id',
  `init_stock_quantity` decimal(12,2) DEFAULT NULL COMMENT '初始库存数量',
  `low_stock_quantity` decimal(12,2) DEFAULT NULL COMMENT '最低库存数量',
  `high_stock_quantity` decimal(12,2) DEFAULT NULL COMMENT '最高库存数量',
  `current_stock_quantity` decimal(12,2) DEFAULT NULL COMMENT '当前库存数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='产品初始库存';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_stock`
--

LOCK TABLES `product_stock` WRITE;
/*!40000 ALTER TABLE `product_stock` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_unit`
--

DROP TABLE IF EXISTS `product_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_unit` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `compute_unit` varchar(300) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '计量单位，计算得出',
  `basic_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '基础单位',
  `other_unit` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '副单位',
  `other_unit_two` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '副单位2',
  `other_unit_three` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '副单位3',
  `ratio` decimal(24,3) DEFAULT NULL COMMENT '比例',
  `ratio_two` decimal(24,3) DEFAULT NULL COMMENT '比例2',
  `ratio_three` decimal(24,3) DEFAULT NULL COMMENT '比例3',
  `status` tinyint(1) DEFAULT '0' COMMENT '启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='多单位表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_unit`
--

LOCK TABLES `product_unit` WRITE;
/*!40000 ALTER TABLE `product_unit` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receipt_main`
--

DROP TABLE IF EXISTS `receipt_main`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipt_main` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '主类型 (出库/入库)',
  `sub_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '子类型（采购订单/采购退货/销售订单/组装单/拆卸单）',
  `init_receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '初始单据编号',
  `receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '单据编号',
  `member_id` bigint DEFAULT NULL COMMENT '会员id',
  `account_id` bigint DEFAULT NULL COMMENT '账户id',
  `change_amount` decimal(12,2) DEFAULT NULL COMMENT '变动金额(收款/付款)',
  `back_amount` decimal(12,2) DEFAULT NULL COMMENT '找零金额',
  `total_price` decimal(12,2) DEFAULT NULL COMMENT '合计金额',
  `payment_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '付款类型(现金、记账等)',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '备注',
  `file_id` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '附件id（可以多个 逗号隔开）',
  `operator_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '业务员（可以多个 逗号隔开）',
  `multiple_account` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '多账户（可以多个 逗号隔开）',
  `multiple_account_amount` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '多账户金额 （可以多个 逗号隔开）',
  `discount_rate` decimal(12,2) DEFAULT NULL COMMENT '优惠率',
  `discount_amount` decimal(12,2) DEFAULT NULL COMMENT '优惠金额',
  `discount_last_amount` decimal(12,2) DEFAULT NULL COMMENT '优惠后金额',
  `other_amount` decimal(12,2) DEFAULT NULL COMMENT '销售或采购费用合计（其他金额）',
  `deposit` decimal(12,2) DEFAULT NULL COMMENT '订金',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态，0未审核、1已审核、2完成采购|销售、3部分采购|销售、9审核中',
  `purchase_status` tinyint(1) DEFAULT NULL COMMENT '采购状态，0未采购、2完成采购、3部分采购',
  `source` tinyint(1) DEFAULT '0' COMMENT '单据来源，0-pc，1-手机',
  `other_receipt` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联单据',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK2A80F214B610FC06` (`member_id`) USING BTREE,
  KEY `FK2A80F214AAE50527` (`account_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='单据主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receipt_main`
--

LOCK TABLES `receipt_main` WRITE;
/*!40000 ALTER TABLE `receipt_main` DISABLE KEYS */;
/*!40000 ALTER TABLE `receipt_main` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receipt_purchase_main`
--

DROP TABLE IF EXISTS `receipt_purchase_main`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipt_purchase_main` (
  `id` bigint NOT NULL COMMENT '采购单据主表id（主键）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '主类型 (订单/出库/入库)',
  `sub_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '子类型（采购订单/采购入库/采购退货）',
  `init_receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '初始单据编号',
  `receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '单据编号',
  `receipt_date` datetime DEFAULT NULL COMMENT '单据日期',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商id',
  `account_id` bigint DEFAULT NULL COMMENT '账户id',
  `operator_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '采购人员（可以多个 逗号隔开）',
  `change_amount` decimal(12,2) DEFAULT NULL COMMENT '变动金额(退款/付款)',
  `total_amount` decimal(12,2) DEFAULT NULL COMMENT '总计金额\r\n',
  `discount_rate` decimal(12,2) DEFAULT NULL COMMENT '优惠率',
  `discount_amount` decimal(12,2) DEFAULT NULL COMMENT '优惠金额',
  `discount_last_amount` decimal(12,2) DEFAULT NULL COMMENT '优惠后金额',
  `arrears_amount` decimal(12,2) DEFAULT NULL COMMENT '欠款金额',
  `other_amount` decimal(12,2) DEFAULT NULL COMMENT '采购费用合计（其他金额）',
  `deposit` decimal(12,2) DEFAULT NULL COMMENT '定金',
  `file_id` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '附件id（可以多个 逗号隔开）',
  `multiple_account` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '多账户（可以多个 逗号隔开）',
  `multiple_account_amount` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '多账户金额 （可以多个 逗号隔开）',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '备注',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态，0未审核、1已审核、2审核中、3部分采购、4完成采购',
  `other_receipt` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联单据',
  `receipt_source` tinyint(1) DEFAULT '0' COMMENT '单据来源，0-pc，1-手机',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receipt_purchase_main`
--

LOCK TABLES `receipt_purchase_main` WRITE;
/*!40000 ALTER TABLE `receipt_purchase_main` DISABLE KEYS */;
/*!40000 ALTER TABLE `receipt_purchase_main` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receipt_purchase_sub`
--

DROP TABLE IF EXISTS `receipt_purchase_sub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipt_purchase_sub` (
  `id` bigint NOT NULL COMMENT '采购单据子表id（主键）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `receipt_purchase_main_id` bigint NOT NULL COMMENT '采购单据主表id',
  `product_id` bigint NOT NULL COMMENT '商品Id',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID',
  `product_barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品条码',
  `product_number` int DEFAULT NULL COMMENT '商品数量',
  `unit_price` decimal(13,2) DEFAULT NULL COMMENT '单价（这里不等于商品表的字段）因为单据会变动',
  `total_amount` decimal(13,2) DEFAULT NULL COMMENT '总金额（这里不等于商品表的字段）因为单据会变动',
  `tax_rate` decimal(13,2) DEFAULT NULL COMMENT '税率',
  `tax_amount` decimal(13,2) DEFAULT NULL COMMENT '税额',
  `tax_included_amount` decimal(13,2) DEFAULT NULL COMMENT '价税合计（含税金额）',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receipt_purchase_sub`
--

LOCK TABLES `receipt_purchase_sub` WRITE;
/*!40000 ALTER TABLE `receipt_purchase_sub` DISABLE KEYS */;
/*!40000 ALTER TABLE `receipt_purchase_sub` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receipt_retail_main`
--

DROP TABLE IF EXISTS `receipt_retail_main`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipt_retail_main` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '主类型 (出库/入库)',
  `sub_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '子类型（采购订单/采购退货/销售订单/组装单/拆卸单）',
  `init_receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '初始单据编号',
  `receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '单据编号',
  `receipt_date` datetime DEFAULT NULL COMMENT '单据日期',
  `member_id` bigint DEFAULT NULL COMMENT '会员id',
  `account_id` bigint DEFAULT NULL COMMENT '账户id',
  `change_amount` decimal(12,2) DEFAULT NULL COMMENT '变动金额(收款/付款)',
  `back_amount` decimal(12,2) DEFAULT NULL COMMENT '找零金额',
  `total_amount` decimal(12,2) DEFAULT NULL COMMENT '合计金额',
  `total_product_number` int DEFAULT NULL COMMENT '商品总数量',
  `payment_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '付款类型(现金、记账等)',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '备注',
  `file_id` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '附件id（可以多个 逗号隔开）',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态，0未审核、1已审核、2完成采购|销售、3部分采购|销售、9审核中',
  `source` tinyint(1) DEFAULT '0' COMMENT '单据来源，0-pc，1-手机',
  `other_receipt` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联单据',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK2A80F214B610FC06` (`member_id`) USING BTREE,
  KEY `FK2A80F214AAE50527` (`account_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='单据主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receipt_retail_main`
--

LOCK TABLES `receipt_retail_main` WRITE;
/*!40000 ALTER TABLE `receipt_retail_main` DISABLE KEYS */;
/*!40000 ALTER TABLE `receipt_retail_main` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receipt_retail_sub`
--

DROP TABLE IF EXISTS `receipt_retail_sub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipt_retail_sub` (
  `id` bigint NOT NULL COMMENT '主键',
  `receipt_main_id` bigint NOT NULL COMMENT '仓库主表id',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NOT NULL COMMENT '商品Id',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID',
  `product_barcode` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品条码',
  `product_number` int DEFAULT NULL COMMENT '商品数量',
  `unit_price` decimal(13,2) DEFAULT NULL COMMENT '单价（这里不等于商品表的字段）因为单据会变动',
  `total_amount` decimal(13,2) DEFAULT NULL COMMENT '总金额（这里不等于商品表的字段）因为单据会变动',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '商品备注',
  `correlation_id` bigint DEFAULT NULL COMMENT '关联明细id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK2A819F475D61CCF7` (`product_id`) USING BTREE,
  KEY `FK2A819F474BB6190E` (`receipt_main_id`) USING BTREE,
  KEY `FK2A819F479485B3F5` (`warehouse_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='单据子表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receipt_retail_sub`
--

LOCK TABLES `receipt_retail_sub` WRITE;
/*!40000 ALTER TABLE `receipt_retail_sub` DISABLE KEYS */;
/*!40000 ALTER TABLE `receipt_retail_sub` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receipt_sale_main`
--

DROP TABLE IF EXISTS `receipt_sale_main`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipt_sale_main` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '主类型 (出库/入库)',
  `sub_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '子类型（销售订单/销售出库/销售退货）',
  `init_receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '初始单据编号',
  `receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '单据编号',
  `receipt_date` datetime DEFAULT NULL COMMENT '单据日期',
  `customer_id` bigint DEFAULT NULL COMMENT '客户id',
  `account_id` bigint DEFAULT NULL COMMENT '账户id',
  `change_amount` decimal(12,2) DEFAULT NULL COMMENT '变动金额(收款/付款)',
  `total_amount` decimal(12,2) DEFAULT NULL COMMENT '合计金额',
  `file_id` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '附件id（可以多个 逗号隔开）',
  `operator_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '业务员（可以多个 逗号隔开）',
  `multiple_account` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '多账户（可以多个 逗号隔开）',
  `multiple_account_amount` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '多账户金额 （可以多个 逗号隔开）',
  `discount_rate` decimal(12,2) DEFAULT NULL COMMENT '优惠率',
  `discount_amount` decimal(12,2) DEFAULT NULL COMMENT '优惠金额',
  `discount_last_amount` decimal(12,2) DEFAULT NULL COMMENT '优惠后金额',
  `other_amount` decimal(12,2) DEFAULT NULL COMMENT '销售其他金额',
  `arrears_amount` decimal(12,2) DEFAULT NULL COMMENT '欠款金额',
  `deposit` decimal(12,2) DEFAULT NULL COMMENT '定金',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '备注',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态，0未审核、1已审核、2审核中、3部分销售、4完成销售',
  `receipt_source` tinyint(1) DEFAULT '0' COMMENT '单据来源，0-pc，1-手机',
  `other_receipt` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联单据',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receipt_sale_main`
--

LOCK TABLES `receipt_sale_main` WRITE;
/*!40000 ALTER TABLE `receipt_sale_main` DISABLE KEYS */;
/*!40000 ALTER TABLE `receipt_sale_main` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receipt_sale_sub`
--

DROP TABLE IF EXISTS `receipt_sale_sub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipt_sale_sub` (
  `id` bigint NOT NULL COMMENT '销售单据子表id（主键）',
  `receipt_sale_main_id` bigint NOT NULL COMMENT '销售单据主表id',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NOT NULL COMMENT '商品Id',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID',
  `product_barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品条码',
  `product_number` int DEFAULT NULL COMMENT '商品数量',
  `unit_price` decimal(13,2) DEFAULT NULL COMMENT '单价（这里不等于商品表的字段）因为单据会变动',
  `total_amount` decimal(13,2) DEFAULT NULL COMMENT '金额（这里不等于商品表的字段）因为单据会变动',
  `tax_rate` decimal(13,2) DEFAULT NULL COMMENT '税率',
  `tax_amount` decimal(13,2) DEFAULT NULL COMMENT '税额',
  `tax_included_amount` decimal(13,2) DEFAULT NULL COMMENT '价税合计（含税金额）',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '商品备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receipt_sale_sub`
--

LOCK TABLES `receipt_sale_sub` WRITE;
/*!40000 ALTER TABLE `receipt_sale_sub` DISABLE KEYS */;
/*!40000 ALTER TABLE `receipt_sale_sub` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receipt_sub`
--

DROP TABLE IF EXISTS `receipt_sub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipt_sub` (
  `id` bigint NOT NULL COMMENT '主键',
  `receipt_main_id` bigint NOT NULL COMMENT '仓库主表id',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `product_id` bigint NOT NULL COMMENT '商品Id',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID',
  `product_barcode` bigint DEFAULT NULL COMMENT '商品条码',
  `product_number` int DEFAULT NULL COMMENT '商品数量',
  `product_price` decimal(13,2) DEFAULT NULL COMMENT '商品单价（这里不等于商品表的字段）因为单据会变动',
  `product_total_price` decimal(13,2) DEFAULT NULL COMMENT '商品金额（这里不等于商品表的字段）因为单据会变动',
  `product_remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '商品备注',
  `product_extend_id` bigint DEFAULT NULL COMMENT '商品扩展id',
  `another_warehouse_id` bigint DEFAULT NULL COMMENT '调拨时，对方仓库Id',
  `correlation_id` bigint DEFAULT NULL COMMENT '关联明细id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK2A819F475D61CCF7` (`product_id`) USING BTREE,
  KEY `FK2A819F474BB6190E` (`receipt_main_id`) USING BTREE,
  KEY `FK2A819F479485B3F5` (`warehouse_id`) USING BTREE,
  KEY `FK2A819F47729F5392` (`another_warehouse_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='单据子表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receipt_sub`
--

LOCK TABLES `receipt_sub` WRITE;
/*!40000 ALTER TABLE `receipt_sub` DISABLE KEYS */;
/*!40000 ALTER TABLE `receipt_sub` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `supplier_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '供应商名称',
  `contact` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '联系人',
  `contact_number` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '电子邮箱',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `is_system` tinyint DEFAULT NULL COMMENT '是否系统自带 0==系统 1==非系统',
  `type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '类型',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-启用，1-停用）默认启用',
  `first_quarter_account_payment` decimal(12,3) DEFAULT NULL COMMENT '一季度应付账款',
  `second_quarter_account_payment` decimal(12,3) DEFAULT NULL COMMENT '二季度应付账款',
  `third_quarter_account_payment` decimal(12,3) DEFAULT NULL COMMENT '三季度应付账款',
  `fourth_quarter_account_payment` decimal(12,3) DEFAULT NULL COMMENT '四季度应付账款',
  `total_account_payment` decimal(24,3) DEFAULT NULL COMMENT '累计应付账款',
  `fax` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '传真',
  `phone_number` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '手机',
  `address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '地址',
  `tax_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '纳税人识别号',
  `bank_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '开户行',
  `account_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '账号',
  `tax_rate` decimal(24,3) DEFAULT NULL COMMENT '税率',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `company_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '公司名称',
  `company_contact` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '公司联系人',
  `company_address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '公司地址',
  `company_phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '公司电话',
  `company_fax` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '公司传真',
  `company_post_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '公司邮编',
  `sale_agreement` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '销售协议',
  `warehouse_status` tinyint(1) DEFAULT '0' COMMENT '仓库启用标记，0未启用，1启用',
  `customer_status` tinyint(1) DEFAULT '0' COMMENT '客户启用标记，0未启用，1启用',
  `minus_stock_status` tinyint(1) DEFAULT '0' COMMENT '负库存启用标记，0未启用，1启用',
  `purchase_by_sale_status` tinyint(1) DEFAULT '0' COMMENT '以销定购启用标记，0未启用，1启用',
  `multi_level_approval_status` tinyint(1) DEFAULT '0' COMMENT '多级审核启用标记，0未启用，1启用',
  `process_type` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '流程类型，可多选',
  `force_approval_status` tinyint(1) DEFAULT '0' COMMENT '强审核启用标记，0未启用，1启用',
  `update_unit_price_status` tinyint(1) DEFAULT '1' COMMENT '更新单价启用标记，0未启用，1启用',
  `over_link_bill_status` tinyint(1) DEFAULT '0' COMMENT '超出关联单据启用标记，0未启用，1启用',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='系统参数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_department`
--

DROP TABLE IF EXISTS `sys_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_department` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `parent_id` bigint DEFAULT NULL COMMENT '父级部门id',
  `number` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '部门编号',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '部门简称',
  `leader` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '部门负责任人',
  `status` tinyint DEFAULT '0' COMMENT '状态 0启用，1停用 默认启用',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `sort` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '部门显示顺序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='机构表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_department`
--

LOCK TABLES `sys_department` WRITE;
/*!40000 ALTER TABLE `sys_department` DISABLE KEYS */;
INSERT INTO `sys_department` VALUES (1253081454150156289,0,NULL,'DT0000','默认部门','1253081454145961984',0,'租户注册后的默认部门 该部门为父级部门',NULL,'2024-06-19 12:18:20',NULL,1253081454145961984,NULL,0);
/*!40000 ALTER TABLE `sys_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_file`
--

DROP TABLE IF EXISTS `sys_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_file` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件UID',
  `file_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '文件名称',
  `file_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '文件url（预览地址）',
  `file_download_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '文件下载url',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小（KB）',
  `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_file`
--

LOCK TABLES `sys_file` WRITE;
/*!40000 ALTER TABLE `sys_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_log` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `operate_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '操作模块名称',
  `client_ip` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '客户端IP',
  `status` tinyint DEFAULT NULL COMMENT '操作状态 0==成功，1==失败',
  `content` varchar(5000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '详情',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FKF2696AA13E226853` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='操作日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '名称',
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '标题（菜单显示）',
  `title_english` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '英文标题（菜单显示）',
  `parent_id` int DEFAULT NULL COMMENT '父级菜单id',
  `menu_type` int DEFAULT NULL COMMENT '类型',
  `path` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '链接',
  `component` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '组件',
  `redirect` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '重定向地址',
  `sort` int DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态（0-启用，1-停用）',
  `icon` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '图标',
  `hide_menu` tinyint(1) DEFAULT '0' COMMENT '隐藏路由不在菜单显示',
  `blank` tinyint(1) DEFAULT NULL COMMENT '是否外链(target = _blank)',
  `hide_breadcrumb` tinyint(1) DEFAULT '0' COMMENT '隐藏该路由在面包屑上面的显示',
  `ignore_keep_alive` tinyint(1) DEFAULT '0' COMMENT '是否忽略KeepAlive缓存',
  `hide_tab` tinyint(1) DEFAULT '0' COMMENT '隐藏路由不在标签页显示',
  `carry_param` tinyint(1) DEFAULT '0' COMMENT '如果该路由会携带参数，且需要在tab页上面显示。则需要设置为true',
  `hide_children_in_menu` tinyint(1) DEFAULT '0' COMMENT '隐藏所有子菜单',
  `affix` tinyint(1) DEFAULT '0' COMMENT '是否固定标签',
  `frameSrc` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT '内嵌iframe的地址',
  `realPath` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT '动态路由的实际Path, 即去除路由的动态部分;',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `url` (`path`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=328 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='功能模块表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'Dashboard','首页','Home Page',0,1,'/dashboard','/dashboard/analysis/index',NULL,1,0,'ant-design:dashboard-outlined',0,NULL,0,0,0,0,0,1,NULL,NULL,'2023-06-23 14:36:55','2023-09-30 18:46:44',NULL,NULL,0),(2,'workbench','工作台','Dashboard',0,1,'/dashboard/workbench','/dashboard/workbench/index',NULL,2,0,'ant-design:home-outlined',0,0,0,0,0,0,0,0,'','','2023-10-02 16:01:53','2023-10-15 01:14:24',NULL,NULL,0),(3,'RetailManagement','零售管理','Retail Management',0,0,'/retail','LAYOUT',NULL,3,0,'ant-design:folder-open-outlined',0,0,0,0,0,0,0,0,NULL,NULL,'2023-08-07 14:36:50','2024-03-22 11:25:54',NULL,NULL,0),(4,'SystemManagement','系统管理','System Management',0,0,'/sys','LAYOUT',NULL,10,0,'ant-design:setting-outlined',0,0,0,0,0,0,0,0,'','','2023-09-30 14:36:33','2024-03-23 00:26:19',NULL,NULL,0),(5,'PurchaseManagement','采购管理','Purchase Management',0,0,'/purchase','LAYOUT',NULL,5,0,'ant-design:retweet-outlined',0,0,0,0,0,0,0,0,NULL,NULL,'2023-10-02 14:39:13','2024-03-22 11:26:26',NULL,NULL,0),(6,'SaleManagement','销售管理','Sales Management',0,1,'/sales','LAYOUT',NULL,4,0,'ant-design:shop-outlined',0,0,0,0,0,0,0,0,NULL,NULL,'2023-09-30 14:39:29','2024-03-22 11:26:21',NULL,NULL,0),(7,'WarehouseManagement','仓库管理','Warehouse Management',0,0,'/warehouse','LAYOUT',NULL,8,0,'ant-design:bank-outlined',0,0,0,0,0,0,0,0,NULL,NULL,'2023-10-02 14:39:15','2024-03-23 00:24:32',NULL,NULL,0),(8,'FinancialManagement','财务管理','Financial Management',0,0,'/financial','LAYOUT',NULL,9,0,'ant-design:transaction-outlined',0,0,0,0,0,0,0,0,NULL,NULL,'2023-10-02 14:39:18','2024-03-23 00:24:42',NULL,NULL,0),(9,'Reports','报表查询','Reports',0,0,'/reports','LAYOUT',NULL,12,0,'ant-design:pie-chart-outlined',0,0,0,0,0,0,0,0,NULL,NULL,'2023-09-30 14:39:25','2024-03-23 00:26:34',NULL,NULL,0),(10,'ProductManagement','商品管理','Product Management',0,0,'/product','LAYOUT',NULL,6,0,'ant-design:shopping-outlined',0,0,0,0,0,0,0,0,NULL,NULL,'2023-10-04 14:39:20','2024-03-22 11:27:44',NULL,NULL,0),(11,'BasicInformation','基本资料','Basic Information',0,0,'/basic','LAYOUT',NULL,11,0,'ant-design:appstore-outlined',0,0,0,0,0,0,0,0,NULL,NULL,'2023-10-01 14:39:22','2024-03-23 00:26:26',NULL,NULL,0),(12,'RoleManagement','角色管理','Role Management',4,1,'/role','/sys/role/index',NULL,3,0,'ant-design:solution-outlined',0,0,0,0,0,0,0,0,NULL,NULL,'2023-09-20 14:36:37','2024-06-25 02:18:59',NULL,NULL,0),(13,'UserManagement','用户管理','User Management',4,1,'/user','/sys/user/index',NULL,2,0,'ant-design:user-outlined',0,0,0,0,0,0,0,0,NULL,NULL,'2023-08-25 14:36:39','2023-10-04 21:33:04',NULL,NULL,0),(14,'DepartmentManagement','部门管理','Department Management',4,1,'/department','/sys/department/index',NULL,4,0,'ic:outline-people-alt',0,0,0,0,0,0,0,0,NULL,NULL,'2023-10-04 14:36:43','2024-06-25 02:18:41',NULL,NULL,0),(15,'MenuManagement','菜单管理','Menu Management',4,1,'/menu','/sys/menu/index',NULL,5,0,'ant-design:menu-outlined',0,0,0,0,0,0,0,0,'','','2023-09-13 14:36:47','2024-06-25 02:18:33',NULL,NULL,0),(16,'ProductCategory','商品类别','Product Category',10,1,'/product/category','/product/category/index',NULL,1,0,'ant-design:share-alt-outlined',0,0,0,0,0,0,0,0,'','','2023-10-02 15:06:55','2023-10-04 17:31:45',NULL,NULL,0),(17,'ProductInfo','商品信息','Product Info',10,1,'/product/info','/product/info/index',NULL,1,0,'ant-design:rocket-outlined',0,0,0,0,0,0,0,0,'','','2023-10-16 22:49:18','2023-10-16 22:49:20',NULL,NULL,0),(18,'ProductAttribute','商品属性','Product Attribute',10,1,'/product/attributes','/product/attributes/index',NULL,2,0,'ant-design:tags-outlined',0,0,0,0,0,0,0,0,'','','2023-10-08 14:05:43','2023-10-08 14:07:38',NULL,NULL,0),(19,'ProductUnit','计量单位','Product Unit',10,1,'/product/units','/product/units/index',NULL,3,0,'ant-design:percentage-outlined',0,0,0,0,0,0,0,0,'','','2023-10-08 22:38:05',NULL,NULL,NULL,0),(20,'SupplierInformation','供应商信息','Supplier Information',11,1,'/basic/supplier','/basic/supplier/index',NULL,1,0,'ant-design:taobao-outlined',0,0,0,0,0,0,0,0,'','','2023-10-09 15:26:40',NULL,NULL,NULL,0),(21,'CustomerInformation','客户信息','Customer Information',11,1,'/basic/customer','/basic/customer/index',NULL,2,0,'ant-design:team-outlined',0,0,0,0,0,0,0,0,'','','2023-10-09 15:27:59',NULL,NULL,NULL,0),(22,'MemberInformation','会员信息','Member Information',11,1,'/basic/member','/basic/member/index',NULL,3,0,'ant-design:user-outlined',0,0,0,0,0,0,0,0,'','','2023-10-09 15:29:36',NULL,NULL,NULL,0),(23,'WarehouseInformation','仓库信息','Warehouse Information',11,1,'/basic/warehouse','/basic/warehouse/index',NULL,4,0,'ant-design:home-filled',0,0,0,0,0,0,0,0,'','','2023-10-09 15:31:20',NULL,NULL,NULL,0),(24,'SettlementAccount','结算账户','Settlement Account',11,1,'/basic/settlement-account','/basic/settlement-account/index',NULL,5,0,'ant-design:pay-circle-filled',0,0,0,0,0,0,0,0,'','','2023-10-09 15:32:56',NULL,NULL,NULL,0),(25,'WallahManagement','经手人管理','Wallah Management',11,1,'/basic/operator','/basic/operator/index',NULL,6,0,'ant-design:pushpin-twotone',0,0,0,0,0,0,0,0,'','','2023-10-09 15:34:09','2023-10-16 21:47:58',NULL,NULL,0),(26,'/gpt','GPT AI微调模型','GPT',0,0,'/gpt','LAYOUT',NULL,13,0,'ant-design:rocket-outlined',0,0,0,0,0,0,0,0,'','','2023-10-09 15:35:34','2024-03-23 00:26:50',NULL,NULL,0),(287,'RetailShipments','零售出库','Retail Shipments',3,1,'/retail/shipments','/retail/shipments/index',NULL,1,0,'ant-design:gift-twotone',0,0,0,0,0,0,0,0,'','','2023-10-25 20:28:51',NULL,NULL,NULL,0),(288,'advanceCharge','收预付款','Advance Charge',8,1,'/financial/advance-charge','/financial/advance-charge/index',NULL,7,0,'ant-design:pay-circle-outlined',0,0,0,0,0,0,0,0,'','','2023-10-28 19:15:46','2024-03-27 18:27:40',NULL,NULL,0),(289,'RetailRefund','零售退货','Retail Refund',3,1,'/retail/refund','/retail/refund/index',NULL,2,0,'ant-design:history-outlined',0,0,0,0,0,0,0,0,'','','2023-11-04 20:54:58','2023-11-29 12:32:20',NULL,NULL,0),(290,'SalesOrder','销售订单','Sales Order',6,1,'/sales/order','/sales/order/index',NULL,1,0,'ant-design:pay-circle-outlined',0,0,0,0,0,0,0,0,'','','2023-11-04 20:57:23',NULL,NULL,NULL,0),(291,'SalesShipments','销售出库','Sales Shipments',6,1,'/sales/shipments','/sales/shipments/index',NULL,2,0,'ant-design:shopping-cart-outlined',0,0,0,0,0,0,0,0,'','','2023-11-04 20:58:43',NULL,NULL,NULL,0),(292,'SalesRefund','销售退货','Sales Refund',6,1,'/sales/refund','/sales/refund/index',NULL,3,0,'ant-design:sync-outlined',0,0,0,0,0,0,0,0,'','','2023-11-04 20:59:44',NULL,NULL,NULL,0),(293,'PurchaseOrder','采购订单','Purchase Order',5,1,'/purchase/order','/purchase/order/index',NULL,1,0,'ant-design:star-twotone',0,0,0,0,0,0,0,0,'','','2023-11-04 21:01:10',NULL,NULL,NULL,0),(294,'PurchaseStorage','采购入库','Purchase Storage',5,1,'/purchase/storage','/purchase/storage/index',NULL,2,0,'ant-design:home-twotone',0,0,0,0,0,0,0,0,'','','2023-11-04 21:02:22',NULL,NULL,NULL,0),(295,'PurchaseRefund','采购退货','Purchase Refund',5,1,'/purchase/refund','/purchase/refund/index',NULL,3,0,'ant-design:send-outlined',0,0,0,0,0,0,0,0,'','','2023-11-04 21:03:14',NULL,NULL,NULL,0),(296,'ProductStock','商品库存','Product Stock',9,1,'/report/productStock','/report/productStock',NULL,1,0,'ant-design:pie-chart-outlined',0,0,0,0,0,0,0,0,'','','2023-11-15 12:49:20',NULL,NULL,NULL,0),(297,'AccountStatistics','账户统计','Account Statistics',9,1,'/report/accountStatistics','/report/accountStatistics',NULL,2,0,'ant-design:skype-filled',0,0,0,0,0,0,0,0,'','','2023-11-17 21:13:28',NULL,NULL,NULL,0),(298,'RetailStatistics','零售统计','Retail Statistics',9,1,'/report/retailStatistics','/report/retailStatistics',NULL,3,0,'ant-design:pie-chart-twotone',0,0,0,0,0,0,0,0,'','','2023-11-19 20:24:51',NULL,NULL,NULL,0),(299,'PurchaseStatistics','采购统计','Purchase Statistics',9,1,'/report/purchaseStatistics','/report/purchaseStatistics',NULL,3,0,'ant-design:signal-filled',0,0,0,0,0,0,0,0,'','','2023-11-20 13:34:31',NULL,NULL,NULL,0),(300,'SaleStatistics','销售统计','Sales Statistics',9,1,'/report/saleStatistics','/report/saleStatistics',NULL,5,0,'ant-design:schedule-outlined',0,0,0,0,0,0,0,0,'','','2023-11-20 13:35:34',NULL,NULL,NULL,0),(301,'ShipmentsDetailStatistics','出库明细','Shipments Detail Statistics',9,1,'/report/shipmentsDetail','/report/shipmentsDetail',NULL,6,0,'ant-design:line-chart-outlined',0,0,0,0,0,0,0,0,'','','2023-11-20 17:27:39',NULL,NULL,NULL,0),(302,'StorageDetailStatistics','入库明细','Storage Detail Statistics',9,1,'/report/storageDetail','/report/storageDetail',NULL,7,0,'ant-design:money-collect-outlined',0,0,0,0,0,0,0,0,'','','2023-11-20 17:28:53',NULL,NULL,NULL,0),(303,'ShipmentsSummary','出库汇总','Shipments Summary',9,1,'/report/shipmentsSummary','/report/shipmentsSummary',NULL,8,0,'ant-design:area-chart-outlined',0,0,0,0,0,0,0,0,'','','2023-11-20 21:20:09',NULL,NULL,NULL,0),(304,'StorageSummary','入库汇总','Storage Summary',9,1,'/report/storageSummary','/report/storageSummary',NULL,9,0,'ant-design:bar-chart-outlined',0,0,0,0,0,0,0,0,'','','2023-11-20 21:20:58',NULL,NULL,NULL,0),(305,'IncomeExpense','收支项目','Income Expense',11,1,'/basic/income-expense','/basic/income-expense/index',NULL,7,0,'ant-design:bank-twotone',0,0,0,0,0,0,0,0,'','','2023-11-21 10:34:47',NULL,NULL,NULL,0),(306,'IncomeReceipt','收入单','Income Receipt',8,1,'/financial/income','/financial/income/index',NULL,2,0,'ant-design:pay-circle-filled',0,0,0,0,0,0,0,0,'','','2023-11-21 16:59:36',NULL,NULL,NULL,0),(307,'ExpenseReceipt','支出单','Expense Receipt',8,1,'/financial/expense','/financial/expense/index',NULL,3,0,'ant-design:money-collect-twotone',0,0,0,0,0,0,0,0,'','','2023-11-22 14:34:18',NULL,NULL,NULL,0),(308,'TransferReceipt','转账单','Transfer Receipt',8,1,'/financial/transfer','/financial/transfer/index',NULL,4,0,'ant-design:pound-circle-outlined',0,0,0,0,0,0,0,0,'','','2023-11-22 17:36:48',NULL,NULL,NULL,0),(309,'CollectionReceipt','收款单','Collection Receipt',8,1,'/financial/collection','/financial/collection/index',NULL,5,0,'ant-design:instagram-filled',0,0,0,0,0,0,0,0,'','','2023-11-22 22:44:51',NULL,NULL,NULL,0),(310,'PaymentReceipt','付款单','Payment Receipt',8,1,'/financial/payment','/financial/payment/index',NULL,6,0,'ant-design:account-book-twotone',0,0,0,0,0,0,0,0,'','','2023-11-23 20:17:06',NULL,NULL,NULL,0),(311,'OtherStorage','其他入库','Other Storage',7,1,'/warehouse/storage','/warehouse/storage/index',NULL,1,0,'ant-design:appstore-outlined',0,0,0,0,0,0,0,0,'','','2023-11-24 21:01:38',NULL,NULL,NULL,0),(312,'OtherShipments','其他出库','Other Shipments',7,1,'/warehouse/shipments','/warehouse/shipments/index',NULL,2,0,'ant-design:shop-twotone',0,0,0,0,0,0,0,0,'','','2023-11-25 15:25:32',NULL,NULL,NULL,0),(313,'AllotShipments','调拨出库','Allot Shipments',7,1,'/warehouse/allot','/warehouse/allot/index',NULL,3,0,'ant-design:tags-outlined',0,0,0,0,0,0,0,0,'','','2023-11-25 18:17:47',NULL,NULL,NULL,0),(314,'AssembleReceipt','组装单','Assemble Receipt',7,1,'/warehouse/assemble','/warehouse/assemble/index',NULL,4,0,'ant-design:bar-chart-outlined',0,0,0,0,0,0,0,0,'','','2023-11-27 14:37:56',NULL,NULL,NULL,0),(315,'DisAssembleReceipt','拆卸单','DisAssemble Receipt',7,1,'/warehouse/disassemble','/warehouse/disassemble/index',NULL,5,0,'ant-design:bars-outlined',0,0,0,0,0,0,0,0,'','','2023-11-27 14:38:40',NULL,NULL,NULL,0),(316,'CustomerBillStatistics','客户对账','Customer Bill Statistics',9,1,'/report/customerBill','/report/customerBill',NULL,10,0,'ant-design:pay-circle-outlined',0,0,0,0,0,0,0,0,'','','2023-11-27 19:52:49',NULL,NULL,NULL,0),(317,'SupplierBillStatistics','供应商对账','Supplier Bill Statistics',9,1,'/report/supplierBill','/report/supplierBill',NULL,11,0,'ant-design:like-outlined',0,0,0,0,0,0,0,0,'','','2023-11-27 22:26:03',NULL,NULL,NULL,0),(318,'Test','测试','Test',NULL,1,'32131','',NULL,3123,0,'123',0,0,0,0,0,0,0,0,'','','2023-11-29 12:32:32',NULL,NULL,NULL,1),(319,'SystemConfig','系统配置','System Config',4,1,'/sys/config','/sys/config/index',NULL,6,0,'ant-design:tool-filled',0,0,0,0,0,0,0,0,'','','2023-12-04 18:43:34','2024-06-25 02:18:25',NULL,NULL,0),(320,'AccountSetting','个人资料','Account Setting',11,1,'/basic/account','/basic/account/index',NULL,1,0,'ant-design:skin-twotone',0,0,0,0,0,0,0,0,'','','2023-12-10 15:29:19','2023-12-10 15:31:23',NULL,NULL,0),(321,'ProductionManagement','生产管理','Production Management',0,0,'/production','LAYOUT',NULL,7,0,'ant-design:rocket-filled',0,0,0,0,0,0,0,0,'','','2024-03-23 00:23:35',NULL,NULL,NULL,0),(322,'ProductionTask','生产任务','Production Task',321,1,'/production/tasks','/production/tasks/index',NULL,1,0,'ant-design:history-outlined',0,0,0,0,0,0,0,0,'','','2024-03-23 00:27:55',NULL,NULL,NULL,0),(327,'Tenant Management','租户管理','Tenant Management',4,1,'/tenant','/sys/tenant/index',NULL,1,0,'ant-design:comment-outlined',0,0,0,0,0,0,0,0,'','','2024-06-25 02:17:47',NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_msg`
--

DROP TABLE IF EXISTS `sys_msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_msg` (
  `id` bigint NOT NULL COMMENT '主键',
  `msg_title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '消息标题',
  `msg_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '消息内容',
  `description` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '消息类型',
  `user_id` bigint DEFAULT NULL COMMENT '接收人id',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态，0未读 1已读',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `delete_Flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_msg`
--

LOCK TABLES `sys_msg` WRITE;
/*!40000 ALTER TABLE `sys_msg` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_msg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_platform_config`
--

DROP TABLE IF EXISTS `sys_platform_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_platform_config` (
  `id` bigint NOT NULL,
  `platform_key` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关键词',
  `platform_key_info` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关键词名称',
  `platform_value` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='平台参数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_platform_config`
--

LOCK TABLES `sys_platform_config` WRITE;
/*!40000 ALTER TABLE `sys_platform_config` DISABLE KEYS */;
INSERT INTO `sys_platform_config` VALUES (1,'platform_name','平台名称','eairp',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_platform_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `role_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '角色名称',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '类型',
  `price_limit` tinyint(1) DEFAULT NULL COMMENT '价格屏蔽 1-屏蔽采购价 2-屏蔽零售价 3-屏蔽销售价',
  `description` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态（0-启用，1-停用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (0,0,'平台管理员','全部数据',NULL,'全平台管理员，可管理租户下的所有权限和数据',0,'2024-06-19 12:18:20',NULL,0,NULL,0),(1,0,'租户管理员','全部数据',NULL,'普通租户',0,'2024-06-26 20:57:38',NULL,0,NULL,0);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu_rel`
--

DROP TABLE IF EXISTS `sys_role_menu_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu_rel` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `role_id` bigint DEFAULT NULL COMMENT '角色id',
  `menu_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '菜单资源id []分割',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色菜单关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu_rel`
--

LOCK TABLES `sys_role_menu_rel` WRITE;
/*!40000 ALTER TABLE `sys_role_menu_rel` DISABLE KEYS */;
INSERT INTO `sys_role_menu_rel` VALUES (0,0,0,'[1][2][3][4][5][6][7][8][9][10][11][12][13][14][16][17][18][19][20][21][22][23][24][25][287][288][289][290][291][292][293][294][295][296][297][298][299][300][301][302][303][304][305][306][307][308][309][310][311][312][313][314][315][316][317][319][320][327]','2024-06-19 12:18:20',NULL,1253081454145961984,NULL),(1805963490696818689,NULL,1,'[1][2][3][4][5][6][7][8][9][10][11][12][13][14][16][17][18][19][20][21][22][23][24][25][287][288][289][290][291][292][293][294][295][296][297][298][299][300][301][302][303][304][305][306][307][308][309][310][311][312][313][314][315][316][317][319][320]',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_role_menu_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_serial_number`
--

DROP TABLE IF EXISTS `sys_serial_number`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_serial_number` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `product_id` bigint DEFAULT NULL COMMENT '产品表id',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库id',
  `serial_number` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '序列号',
  `sell_status` tinyint(1) DEFAULT '0' COMMENT '是否卖出，0未卖出，1卖出',
  `inbound_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '入库单号',
  `outbound_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '出库单号',
  `remark` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='序列号表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_serial_number`
--

LOCK TABLES `sys_serial_number` WRITE;
/*!40000 ALTER TABLE `sys_serial_number` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_serial_number` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_tenant`
--

DROP TABLE IF EXISTS `sys_tenant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tenant` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '租户名称',
  `user_num_limit` int DEFAULT NULL COMMENT '用户数量限制',
  `type` tinyint(1) DEFAULT '0' COMMENT '租户类型，0免费租户，1付费租户',
  `status` tinyint(1) DEFAULT '1' COMMENT '启用 0-禁用  1-启用',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `expire_time` datetime DEFAULT NULL COMMENT '到期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='租户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_tenant`
--

LOCK TABLES `sys_tenant` WRITE;
/*!40000 ALTER TABLE `sys_tenant` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_tenant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_tenant_user`
--

DROP TABLE IF EXISTS `sys_tenant_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tenant_user` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `status` tinyint DEFAULT NULL COMMENT '状态（0-启用，1-停用）',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_tenant_user`
--

LOCK TABLES `sys_tenant_user` WRITE;
/*!40000 ALTER TABLE `sys_tenant_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_tenant_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户姓名--例如张三',
  `user_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '登录用户名',
  `password` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '登陆密码',
  `leader_flag` tinyint(1) DEFAULT '0' COMMENT '是否经理，0否，1是',
  `position` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '职位',
  `email` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '电子邮箱',
  `phone_number` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '手机号码',
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '头像地址',
  `system_language` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'zh_CN' COMMENT '系统语言 默认中文zh_CN，英文对en_US，香港zh_HK，菲律宾tl_PH',
  `is_manager` tinyint NOT NULL DEFAULT '1' COMMENT '是否为管理者 0==管理者 1==员工',
  `is_system` tinyint NOT NULL DEFAULT '0' COMMENT '是否系统自带数据 ',
  `status` tinyint DEFAULT '0' COMMENT '状态，0：正常，1：删除，2封禁',
  `description` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户描述信息',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `wechat_open_id` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '微信绑定',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_Flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (0,0,'平台管理员','admin','e10adc3949ba59abbe56e057f20f883e',0,NULL,NULL,NULL,NULL,'zh_CN',1,0,0,NULL,NULL,NULL,'2024-06-19 12:18:20',NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_business`
--

DROP TABLE IF EXISTS `sys_user_business`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_business` (
  `id` bigint NOT NULL COMMENT '主键',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '类别',
  `key_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '主id',
  `value` varchar(10000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '值',
  `btn_str` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '按钮权限',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户/角色/模块关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_business`
--

LOCK TABLES `sys_user_business` WRITE;
/*!40000 ALTER TABLE `sys_user_business` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_business` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_dept_rel`
--

DROP TABLE IF EXISTS `sys_user_dept_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_dept_rel` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `dept_id` bigint NOT NULL COMMENT '部门id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `sort` int DEFAULT NULL COMMENT '用户在所属部门中显示顺序',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='机构用户关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_dept_rel`
--

LOCK TABLES `sys_user_dept_rel` WRITE;
/*!40000 ALTER TABLE `sys_user_dept_rel` DISABLE KEYS */;
INSERT INTO `sys_user_dept_rel` VALUES (1253081454150156291,0,1253081454150156289,0,0,0,'2024-06-19 12:18:20',NULL,1253081454145961984,NULL);
/*!40000 ALTER TABLE `sys_user_dept_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role_rel`
--

DROP TABLE IF EXISTS `sys_user_role_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role_rel` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `role_id` bigint DEFAULT NULL COMMENT '角色id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户角色关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role_rel`
--

LOCK TABLES `sys_user_role_rel` WRITE;
/*!40000 ALTER TABLE `sys_user_role_rel` DISABLE KEYS */;
INSERT INTO `sys_user_role_rel` VALUES (1253081454150156290,0,0,0,'2024-06-19 12:18:20',NULL,1253081454145961984,NULL);
/*!40000 ALTER TABLE `sys_user_role_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_warehouse_rel`
--

DROP TABLE IF EXISTS `sys_user_warehouse_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_warehouse_rel` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `warehouse_id` bigint NOT NULL COMMENT '仓库id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_warehouse_rel`
--

LOCK TABLES `sys_user_warehouse_rel` WRITE;
/*!40000 ALTER TABLE `sys_user_warehouse_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_warehouse_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse`
--

DROP TABLE IF EXISTS `warehouse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `warehouse_manager` bigint DEFAULT NULL COMMENT '负责人',
  `warehouse_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '仓库名称',
  `address` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '仓库地址',
  `price` decimal(24,6) DEFAULT NULL COMMENT '仓储费',
  `truckage` decimal(24,6) DEFAULT NULL COMMENT '搬运费',
  `type` int DEFAULT '0' COMMENT '类型',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-启用，1-停用）',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '描述',
  `sort` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '排序',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认仓库（0-启用，1-停用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_Flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='仓库表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse`
--

LOCK TABLES `warehouse` WRITE;
/*!40000 ALTER TABLE `warehouse` DISABLE KEYS */;
INSERT INTO `warehouse` VALUES (1253081454267596801,1253081454145961984,1253081454145961984,'默认仓库',NULL,0.000000,0.000000,0,0,'默认仓库',NULL,1,'2024-06-19 12:18:20','2024-06-20 23:38:33',1253081454145961984,1253081454145961984,0);
/*!40000 ALTER TABLE `warehouse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse_head`
--

DROP TABLE IF EXISTS `warehouse_head`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse_head` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '主类型 (出库/入库)',
  `sub_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '子类型（采购订单/采购退货/销售订单/组装单/拆卸单）',
  `init_bill_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '初始票据号',
  `bill_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '票据号',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商id',
  `account_id` bigint DEFAULT NULL COMMENT '账户id',
  `change_amount` decimal(24,6) DEFAULT NULL COMMENT '变动金额(收款/付款)',
  `back_amount` decimal(24,6) DEFAULT NULL COMMENT '找零金额',
  `total_price` decimal(24,6) DEFAULT NULL COMMENT '合计金额',
  `pay_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '付款类型(现金、记账等)',
  `bill_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '单据类型',
  `remark` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `file_name` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '附件名称',
  `sales_man` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '业务员（可以多个）',
  `account_id_list` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '多账户ID列表',
  `account_money_list` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '多账户金额列表',
  `discount` decimal(24,6) DEFAULT NULL COMMENT '优惠率',
  `discount_money` decimal(24,6) DEFAULT NULL COMMENT '优惠金额',
  `discount_last_money` decimal(24,6) DEFAULT NULL COMMENT '优惠后金额',
  `other_money` decimal(24,6) DEFAULT NULL COMMENT '销售或采购费用合计',
  `deposit` decimal(24,6) DEFAULT NULL COMMENT '订金',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态，0未审核、1已审核、2完成采购|销售、3部分采购|销售、9审核中',
  `purchase_status` tinyint(1) DEFAULT NULL COMMENT '采购状态，0未采购、2完成采购、3部分采购',
  `source` tinyint(1) DEFAULT '0' COMMENT '单据来源，0-pc，1-手机',
  `correlation_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联订单号',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间（出/入库时间）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK2A80F214B610FC06` (`supplier_id`) USING BTREE,
  KEY `FK2A80F214AAE50527` (`account_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='单据主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse_head`
--

LOCK TABLES `warehouse_head` WRITE;
/*!40000 ALTER TABLE `warehouse_head` DISABLE KEYS */;
/*!40000 ALTER TABLE `warehouse_head` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse_item`
--

DROP TABLE IF EXISTS `warehouse_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse_item` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `header_id` bigint NOT NULL COMMENT '表头Id',
  `product_id` bigint NOT NULL COMMENT '商品Id',
  `product_extend_id` bigint DEFAULT NULL COMMENT '商品扩展id',
  `product_unit` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品计量单位',
  `multi_attribute` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '多属性',
  `oper_number` decimal(24,6) DEFAULT NULL COMMENT '数量',
  `basic_number` decimal(24,6) DEFAULT NULL COMMENT '基础数量，如kg、瓶',
  `unit_price` decimal(24,6) DEFAULT NULL COMMENT '单价',
  `purchase_unit_price` decimal(24,6) DEFAULT NULL COMMENT '采购单价',
  `tax_unit_price` decimal(24,6) DEFAULT NULL COMMENT '含税单价',
  `total_price` decimal(24,6) DEFAULT NULL COMMENT '金额',
  `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID',
  `another_warehouse_id` bigint DEFAULT NULL COMMENT '调拨时，对方仓库Id',
  `tax_rate` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_money` decimal(24,6) DEFAULT NULL COMMENT '税额',
  `tax_last_money` decimal(24,6) DEFAULT NULL COMMENT '价税合计',
  `product_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品类型',
  `serial_numbers_list` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '序列号列表',
  `batch_number` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '批次号',
  `effective_date` datetime DEFAULT NULL COMMENT '有效日期',
  `correlation_id` bigint DEFAULT NULL COMMENT '关联明细id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK2A819F475D61CCF7` (`product_id`) USING BTREE,
  KEY `FK2A819F474BB6190E` (`header_id`) USING BTREE,
  KEY `FK2A819F479485B3F5` (`warehouse_id`) USING BTREE,
  KEY `FK2A819F47729F5392` (`another_warehouse_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='单据子表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse_item`
--

LOCK TABLES `warehouse_item` WRITE;
/*!40000 ALTER TABLE `warehouse_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `warehouse_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse_receipt_main`
--

DROP TABLE IF EXISTS `warehouse_receipt_main`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse_receipt_main` (
  `id` bigint NOT NULL COMMENT '主键id',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `related_person_id` bigint DEFAULT NULL COMMENT '关联人id(客户/供应商)',
  `product_id` bigint DEFAULT NULL COMMENT '商品id',
  `type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '类型(入库/出库/调拨/组装/拆卸)',
  `init_receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '初始单据编号',
  `receipt_number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '单据编号',
  `receipt_date` datetime DEFAULT NULL COMMENT '单据日期',
  `total_amount` decimal(12,2) DEFAULT NULL COMMENT '合计金额',
  `total_product_number` int DEFAULT NULL COMMENT '商品总数量',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '备注',
  `file_id` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '附件id（可以多个 逗号隔开）',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态，0未审核、1已审核',
  `source` tinyint(1) DEFAULT '0' COMMENT '单据来源，0-pc，1-手机',
  `other_receipt` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联单据',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse_receipt_main`
--

LOCK TABLES `warehouse_receipt_main` WRITE;
/*!40000 ALTER TABLE `warehouse_receipt_main` DISABLE KEYS */;
/*!40000 ALTER TABLE `warehouse_receipt_main` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse_receipt_sub`
--

DROP TABLE IF EXISTS `warehouse_receipt_sub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse_receipt_sub` (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
  `warehouse_receipt_main_id` bigint DEFAULT NULL COMMENT '仓库主表id',
  `product_id` bigint NOT NULL COMMENT '商品Id',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID',
  `other_warehouse_id` bigint DEFAULT NULL COMMENT '调拨对方仓库ID',
  `product_barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品条码',
  `product_number` int DEFAULT NULL COMMENT '商品数量',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类型（组合件/普通子件）',
  `unit_price` decimal(13,2) DEFAULT NULL COMMENT '单价（这里不等于商品表的字段）因为单据会变动',
  `total_amount` decimal(13,2) DEFAULT NULL COMMENT '总金额（这里不等于商品表的字段）因为单据会变动',
  `remark` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '商品备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '修改人',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse_receipt_sub`
--

LOCK TABLES `warehouse_receipt_sub` WRITE;
/*!40000 ALTER TABLE `warehouse_receipt_sub` DISABLE KEYS */;
/*!40000 ALTER TABLE `warehouse_receipt_sub` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-05 17:00:21
