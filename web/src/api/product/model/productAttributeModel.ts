/**
 * Although it seems that the parameters of the two objects are very similar here,
 * there is no guarantee that new fields will be added to the view object return in the future. For extension purposes,
 * it is still necessary to distinguish between them
 *
 * @author James
 * @since  2023-10-08 17:27
 */

export interface ProductAttributeResp {
    id: number | string;
    attributeName: string;
    attributeValue: string;
    remark: string;
    sort: number;
}

export interface AddOrUpdateProductAttributeReq {
    id: number | string;
    attributeName: string;
    attributeValue: string;
    remark: string;
    sort: number;
}

export interface ProductAttributeListReq {
    attributeName: string | undefined;
    page: number;
    pageSize: number;
}