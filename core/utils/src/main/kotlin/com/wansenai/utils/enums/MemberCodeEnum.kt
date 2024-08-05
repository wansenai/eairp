package com.wansenai.utils.enums

enum class MemberCodeEnum(val code: String, val msg: String) {

    ADD_MEMBER_SUCCESS("M0001", "成功添加会员"),

    ADD_MEMBER_SUCCESS_EN("M0001", "Successfully add member"),

    ADD_MEMBER_ERROR("M0500", "系统异常，会员添加失败"),

    ADD_MEMBER_ERROR_EN("M0500", "System exception, failed to add member"),

    UPDATE_MEMBER_INFO_SUCCESS("M0002", "会员信息修改成功"),

    UPDATE_MEMBER_INFO_SUCCESS_EN("M0002", "Successfully modify member info"),

    UPDATE_MEMBER_INFO_ERROR("M0501", "系统异常，会员信息修改失败"),

    UPDATE_MEMBER_INFO_ERROR_EN("M0501", "System exception, failed to modify member info"),

    DELETE_MEMBER_SUCCESS("M0003", "会员删除成功"),

    DELETE_MEMBER_SUCCESS_EN("M0003", "Successfully delete member"),

    DELETE_MEMBER_ERROR("M0504", "系统异常，会员删除失败"),

    DELETE_MEMBER_ERROR_EN("M0504", "System exception, failed to delete member"),

    UPDATE_MEMBER_STATUS_SUCCESS("M0004", "会员状态修改成功"),

    UPDATE_MEMBER_STATUS_SUCCESS_EN("M0004", "Successfully modify member status"),

    UPDATE_MEMBER_STATUS_ERROR("M0505", "系统异常，会员状态修改失败"),

    UPDATE_MEMBER_STATUS_ERROR_EN("M0505", "System exception, failed to modify member status"),
}