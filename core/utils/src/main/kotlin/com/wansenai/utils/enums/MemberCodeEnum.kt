package com.wansenai.utils.enums

enum class MemberCodeEnum(val code: String, val msg: String) {

    ADD_MEMBER_SUCCESS("M0001", "会员添加成功"),
    ADD_MEMBER_ERROR("M0500", "会员添加失败"),

    UPDATE_MEMBER_INFO_SUCCESS("M0002", "会员资料修改成功"),
    UPDATE_MEMBER_INFO_ERROR("M0501", "会员资料修改失败"),

    DELETE_MEMBER_SUCCESS("M0003", "会员删除成功"),
    DELETE_MEMBER_ERROR("M0504", "会员删除失败"),

    UPDATE_MEMBER_STATUS_SUCCESS("M0004", "会员状态修改成功"),
    UPDATE_MEMBER_STATUS_ERROR("M0505", "会员状态修改失败")
}