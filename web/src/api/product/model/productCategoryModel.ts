export interface ProductCategoryResp {
    id: number | string;
    parentId: number | string;
    parentName: string;
    categoryName: string;
    categoryNumber: string;
    remark: string;
    sort: number;
    createTime: string;
    children?: ProductCategoryResp[];
}

export interface AddOrUpdateProductCategoryReq {
    id: number | string;
    parentId: number | string;
    categoryName: string;
    categoryNumber: number;
    remark: string;
    sort: number;
}

export interface ProductCategoryListReq {
    categoryName: string;
}