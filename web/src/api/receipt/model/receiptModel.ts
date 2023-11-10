export interface ReceiptDetailResp {
    id: string;
    productBarcode : string | number;
    warehouseId: string;
    productId: string;
    productName : string;
    productStandard : string;
    productModel : string;
    unit : string;
    productNumber : number;
    amount : number;
    taxRate : number;
    taxAmount : number;
    taxIncludedAmount : number;
    remark : string;
}

export interface ReceiptResp {
    id: string;
    name : string;
    receiptNumber : string;
    productInfo : string;
    receiptDate : string;
    operator : string;
    productNumber : number;
    totalAmount : number;
    taxRateTotalAmount : number | undefined;
}

export interface queryReceipt {
    id: string | number;
    type: string;
    subType: string;
    receiptNumber: string;
    productInfo: string;
}