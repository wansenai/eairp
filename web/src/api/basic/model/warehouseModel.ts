export interface WarehouseResp {
    id: number | string;
    warehouseManager: string;
    warehouseName: string;
    address: string;
    price: number;
    truckage: number;
    type: number;
    status: number;
    remark: string;
    sort: number;
    createTime: string;
}

export interface AddOrUpdateWarehouseReq {
    id: number | string | undefined;
    warehouseName: string;
    warehouseManager: number;
    address: string;
    price: number;
    truckage: number;
    type: number;
    isDefault: number;
    status: number;
    remark: string;
    sort: number;
}

export interface QueryWarehouseReq {
    warehouseName: string;
    remark: string;
}