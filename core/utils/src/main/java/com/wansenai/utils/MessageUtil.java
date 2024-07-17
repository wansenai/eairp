/*
 * Copyright 2024-2033 WanSen AI Team, Inc. All Rights Reserved.
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
package com.wansenai.utils;

public class MessageUtil {

    /** Retail Outbound**/
    public static String RetailShipmentsZhCnSubject() {
        return "新的零售出库单据已创建";
    }

    public static String RetailShipmentsZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您已成功创建了一张新的零售出库单据，单据编号为：{" + receiptNumber + "}。当前订单状态为“未审核”。请及时处理相关事宜。感谢您使用ERP系统！";
    }

    public static String RetailShipmentsZhCnDescription(String receiptNumber) {
        return "零售出库单据编号：" + receiptNumber;
    }

    public static String RetailShipmentsEnUsSubject() {
        return "New Retail Outbound Document Created";
    }

    public static String RetailShipmentsEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! You have successfully created a new retail outbound document with document number:{" + receiptNumber + "}. The current order status is Unreviewed. Please handle related matters promptly. Thank you for using the ERP system!";
    }

    public static String RetailShipmentsEnUsDescription(String receiptNumber) {
        return "Retail Outbound Document Number:" + receiptNumber;
    }

    public static String RetailShipmentsAuditedZhCnSubject() {
        return "零售出库单据已审核";
    }

    public static String RetailShipmentsAuditedEnUsSubject() {
        return "Retail Outbound Document Approved";
    }

    public static String RetailShipmentsAuditedZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您的零售出库单据已审核通过，单据编号为：{" + receiptNumber + "}。请继续跟进后续事宜。感谢您使用ERP系统！";
    }

    public static String RetailShipmentsAuditedEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! Your retail outbound document has been approved, with document number:{" + receiptNumber + "}. Please continue to follow up on subsequent matters. Thank you for using the ERP system!";
    }

    /** Retail Return**/
    public static String RetailRefundZhCnSubject() {
        return "新的零售退货单据已创建";
    }

    public static String RetailRefundZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您已成功创建了一张新的零售退货单据，退货单据编号为：{" + receiptNumber + "}。当前订单状态为“未审核”。请及时处理相关事宜。感谢您使用ERP系统！";
    }

    public static String RetailRefundEnUsSubject() {
        return "New Retail Return Document Created";
    }

    public static String RetailRefundEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! You have successfully created a new retail return document with return document number:{" + receiptNumber + "}. The current order status is Unreviewed. Please handle related matters promptly. Thank you for using the ERP system!";
    }

    public static String RetailRefundZhCnDescription(String receiptNumber) {
        return "零售退货单据编号：" + receiptNumber;
    }

    public static String RetailRefundEnUsDescription(String receiptNumber) {
        return "Retail Return Document Number:" + receiptNumber;
    }

    public static String RetailRefundAuditedZhCnSubject() {
        return "零售退货单据已审核";
    }

    public static String RetailRefundAuditedEnUsSubject() {
        return "Retail Return Document Approved";
    }

    public static String RetailRefundAuditedZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您的零售退货单据已审核通过，退货单据编号为：{" + receiptNumber + "}。请继续跟进后续事宜。感谢您使用ERP系统！";
    }

    public static String RetailRefundAuditedEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! Your retail return document has been approved, with return document number:{" + receiptNumber + "}. Please continue to follow up on subsequent matters. Thank you for using the ERP system!";
    }

    /** Sale Order**/
    public static String SaleOrderZhCnSubject() {
        return "新的销售订单已创建";
    }

    public static String SaleOrderEnUsSubject() {
        return "New Sales Order Created";
    }

    public static String SaleOrderZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您已成功创建了一张新的销售订单，订单编号为：{" + receiptNumber + "}。当前订单状态为“未审核”。请及时处理相关事宜。感谢您使用ERP系统！" ;
    }

    public static String SaleOrderEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! You have successfully created a new sales order with order number:{" + receiptNumber + "}. The current order status is Unreviewed. Please handle related matters promptly. Thank you for using the ERP system!";
    }

    public static String SaleOrderZhCnDescription(String receiptNumber) {
        return "销售订单单据编号：" + receiptNumber;
    }

    public static String SaleOrderEnUsDescription(String receiptNumber) {
        return "Sales Order Document Number:" + receiptNumber;
    }

    public static String SaleOrderAuditedZhCnSubject() {
        return "销售订单已审核";
    }

    public static String SaleOrderAuditedEnUsSubject() {
        return "Sales Order Approved";
    }

    public static String SaleOrderAuditedZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您的销售订单已审核通过，订单编号为：{" + receiptNumber + "}。请继续跟进后续事宜。感谢您使用ERP系统！";
    }

    public static String SaleOrderAuditedEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! Your sales order has been approved, with order number:{" + receiptNumber + "}. Please continue to follow up on subsequent matters. Thank you for using the ERP system!";
    }

    /** Sale Outbound**/
    public static String SaleShipmentsZhCnSubject() {
        return "新的销售出库单据已创建";
    }

    public static String SaleShipmentsZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您已成功创建了一张新的销售出库单据，单据编号为：{" + receiptNumber + "}。当前订单状态为“未审核”。请及时处理相关事宜。感谢您使用ERP系统！";
    }

    public static String SaleShipmentsZhCnDescription(String receiptNumber) {
        return "销售出库单据编号：" + receiptNumber;
    }

    public static String SaleShipmentsEnUsSubject() {
        return "New Sales Outbound Document Created";
    }

    public static String SaleShipmentsEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! You have successfully created a new sales outbound document with document number:{" + receiptNumber + "}. The current order status is Unreviewed. Please handle related matters promptly. Thank you for using the ERP system!";
    }

    public static String SaleShipmentsEnUsDescription(String receiptNumber) {
        return "Sales Outbound Document Number:" + receiptNumber;
    }

    public static String SaleShipmentsAuditedZhCnSubject() {
        return "销售出库单据已审核";
    }

    public static String SaleShipmentsAuditedEnUsSubject() {
        return "Sales Outbound Document Approved";
    }

    public static String SaleShipmentsAuditedZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您的销售出库单据已审核通过，单据编号为：{" + receiptNumber + "}。请继续跟进后续事宜。感谢您使用ERP系统！";
    }

    public static String SaleShipmentsAuditedEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! Your sales outbound document has been approved, with document number:{" + receiptNumber + "}. Please continue to follow up on subsequent matters. Thank you for using the ERP system!";
    }

    /** Sale Return**/
    public static String SaleRefundZhCnSubject() {
        return "新的销售退货单据已创建";
    }

    public static String SaleRefundZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您已成功创建了一张新的销售退货单据，退货单据编号为：{" + receiptNumber + "}。当前订单状态为“未审核”。请及时处理相关事宜。感谢您使用ERP系统！";
    }

    public static String SaleRefundZhCnDescription(String receiptNumber) {
        return "销售退货单据编号：" + receiptNumber;
    }

    public static String SaleRefundEnUsSubject() {
        return "New Sales Return Document Created";
    }

    public static String SaleRefundEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! You have successfully created a new sales return document with return document number:{" + receiptNumber + "}. The current order status is Unreviewed. Please handle related matters promptly. Thank you for using the ERP system!";
    }

    public static String SaleRefundEnUsDescription(String receiptNumber) {
        return "Sales Return Document Number:" + receiptNumber;
    }

    public static String SaleRefundAuditedZhCnSubject() {
        return "销售退货单据已审核";
    }

    public static String SaleRefundAuditedEnUsSubject() {
        return "Sales Returns Document Approved";
    }

    public static String SaleRefundAuditedZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您的销售退货单据已审核通过，退货单据编号为：{" + receiptNumber + "}。请继续跟进后续事宜。感谢您使用ERP系统！";
    }

    public static String SaleRefundAuditedEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! Your sales return document has been approved, with return document number:{" + receiptNumber + "}. Please continue to follow up on subsequent matters. Thank you for using the ERP system!";
    }

    /** Purchase Order**/
    public static String PurchaseOrderZhCnSubject() {
        return "新的采购订单已创建";
    }

    public static String PurchaseOrderZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您已成功创建了一张新的采购订单，订单编号为：{" + receiptNumber + "}。当前订单状态为“未审核”。请及时处理相关事宜。感谢您使用ERP系统！";
    }

    public static String PurchaseOrderZhCnDescription(String receiptNumber) {
        return "采购订单单据编号：" + receiptNumber;
    }

    public static String PurchaseOrderEnUsSubject() {
        return "New Purchase Order Created";
    }

    public static String PurchaseOrderEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! You have successfully created a new purchase order with order number:{" + receiptNumber + "}. The current order status is Unreviewed. Please handle related matters promptly. Thank you for using the ERP system!";
    }

    public static String PurchaseOrderEnUsDescription(String receiptNumber) {
        return "Purchase Order Document Number:" + receiptNumber;
    }

    public static String PurchaseOrderAuditedZhCnSubject() {
        return "采购订单已审核";
    }

    public static String PurchaseOrderAuditedEnUsSubject() {
        return "Purchase Order Approved";
    }

    public static String PurchaseOrderAuditedZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您的采购订单已审核通过，订单编号为：{" + receiptNumber + "}。请继续跟进后续事宜。感谢您使用ERP系统！";
    }

    public static String PurchaseOrderAuditedEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! Your purchase order has been approved, with order number:{" + receiptNumber + "}. Please continue to follow up on subsequent matters. Thank you for using the ERP system!";
    }

    /** Purchase Inbound**/
    public static String PurchaseReceiptZhCnSubject() {
        return "新的采购入库单据已创建";
    }

    public static String PurchaseReceiptZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您已成功创建了一张新的采购入库单据，单据编号为：{" + receiptNumber + "}。当前订单状态为“未审核”。请及时处理相关事宜。感谢您使用ERP系统！";
    }

    public static String PurchaseReceiptZhCnDescription(String receiptNumber) {
        return "采购入库单据编号：" + receiptNumber;
    }

    public static String PurchaseReceiptEnUsSubject() {
        return "New Purchase Inbound Document Created";
    }

    public static String PurchaseReceiptEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! You have successfully created a new purchase inbound document with document number:{" + receiptNumber + "}. The current order status is Unreviewed. Please handle related matters promptly. Thank you for using the ERP system!";
    }

    public static String PurchaseReceiptEnUsDescription(String receiptNumber) {
        return "Purchase Inbound Document Number:" + receiptNumber;
    }

    public static String PurchaseInboundAuditedZhCnSubject() {
        return "采购入库单据已审核";
    }

    public static String PurchaseInboundAuditedEnUsSubject() {
        return "Purchase Inbound Document Approved";
    }

    public static String PurchaseInboundAuditedZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您的采购入库单据已审核通过，单据编号为：{" + receiptNumber + "}。请继续跟进后续事宜。感谢您使用ERP系统！";
    }

    public static String PurchaseInboundAuditedEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! Your purchase inbound document has been approved, with document number:{" + receiptNumber + "}. Please continue to follow up on subsequent matters. Thank you for using the ERP system!";
    }

    /** Purchase Return**/

    public static String PurchaseRefundZhCnSubject() {
        return "新的采购退货单据已创建";
    }

    public static String PurchaseRefundZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您已成功创建了一张新的采购退货单据，退货单据编号为：{" + receiptNumber + "}。当前订单状态为“未审核”。请及时处理相关事宜。感谢您使用ERP系统！";
    }

    public static String PurchaseRefundZhCnDescription(String receiptNumber) {
        return "采购退货单据编号：" + receiptNumber;
    }

    public static String PurchaseRefundEnUsSubject() {
        return "New Purchase Return Document Created";
    }

    public static String PurchaseRefundEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! You have successfully created a new purchase return document with return document number:{" + receiptNumber + "}. The current order status is Unreviewed. Please handle related matters promptly. Thank you for using the ERP system!";
    }

    public static String PurchaseRefundEnUsDescription(String receiptNumber) {
        return "Purchase Return Document Number:" + receiptNumber;
    }

    public static String PurchaseRefundAuditedZhCnSubject() {
        return "采购退货单据已审核";
    }

    public static String PurchaseRefundAuditedEnUsSubject() {
        return "Purchase Returns Document Approved";
    }

    public static String PurchaseRefundAuditedZhCnTemplate(String receiptNumber) {
        return "尊敬的用户，您好！ 您的采购退货单据已审核通过，退货单据编号为：{" + receiptNumber + "}。请继续跟进后续事宜。感谢您使用ERP系统！";
    }

    public static String PurchaseRefundAuditedEnUsTemplate(String receiptNumber) {
        return "Dear User, Greetings! Your purchase return document has been approved, with return document number:{" + receiptNumber + "}. Please continue to follow up on subsequent matters. Thank you for using the ERP system!";
    }
}
