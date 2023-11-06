export interface ReceiptDetailResp {
    id: string | number;
    productBarcode : string | number;
    warehouseId: string | number;
    memberId: string | number;
    productId: string | number;
    productName : string;
    productStandard : string;
    productModel : string;
    unit : string;
    productNumber : number;
    productPrice : number;
    productTotalPrice : number;
    discountRate : number;
    discountAmount : number;
    discountLastAmount : number;
}