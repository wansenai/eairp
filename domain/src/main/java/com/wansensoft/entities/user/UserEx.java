package com.wansensoft.entities.user;

import lombok.Data;

@Data
public class UserEx extends User{
    //机构简称
    private String orgAbr;
    //机构id
    private Long orgaId;
    //用户在部门中排序
    private String userBlngOrgaDsplSeq;
    //机构用户关联关系id
    private Long orgaUserRelId;

    private Long roleId;

    private String roleName;

    private String userType;

    private Integer userNumLimit;

    private String expireTime;

    private String leaderFlagStr;
}
