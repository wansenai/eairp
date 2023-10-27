import {BasicFetchResult} from "@/api/model/baseModel";

export interface DeptListItem {
    deptName: string;
}

export interface GetDeptInfoModel {
    // 机构id
    id: string | number;
    // 机构编号
    deptNumber: string;
    // 机构名称
    deptName: string;
    // 备注
    remark: string;
    // 父级部门id
    parentId: string;
    // 排序
    sort: number;
}

export interface addOrUpdateDeptReq {
    id: number | string;
    deptName: string;
    parentId: number;
    deptNumber: string;
    leader: string;
    status: number;
    remark: string;
    sort: string;
}
