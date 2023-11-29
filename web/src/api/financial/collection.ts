import {defHttp} from '/@/utils/http/axios';
import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {
    AddOrUpdateCollectionReq,
    QueryCollectionReq,
    CollectionResp,
    CollectionDetailResp, QuerySaleArrearsReq, SaleArrearsResp,
} from "@/api/financial/model/collectionModel";

enum API {
    PageList = '/financial/collection/pageList',
    AddOrUpdateAccount = '/financial/collection/addOrUpdate',
    DeleteBatch = '/financial/collection/deleteByIds',
    UpdateStatus = '/financial/collection/updateStatusByIds',
    GetDetail = '/financial/collection/getDetailById',
    GetArrearsPage = '/sale/arrears/pageList',
}

export function getCollectionPageList(params: QueryCollectionReq) {
    return defHttp.post<BaseDataResp<CollectionResp>>(
        {
            url: API.PageList,
            params,
        }
    );
}

export function addOrUpdateCollection(params: AddOrUpdateCollectionReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.post<BaseResp>(
        {
            url: API.AddOrUpdateAccount,
            params,
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function updateCollectionStatus(ids: number[], status: number, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.UpdateStatus}?ids=${ids}&status=${status}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function deleteBatchCollection(ids: number[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'message') {
    return defHttp.put<BaseResp>(
        {
            url: `${API.DeleteBatch}?ids=${ids}`
        },{
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function getCollectionDetailById(id: number) {
    return defHttp.get<BaseDataResp<CollectionDetailResp>>(
        {
            url: `${API.GetDetail}/${id}`
        },
    );
}

export function getArrearsPageList(params: QuerySaleArrearsReq) {
    return defHttp.post<BaseDataResp<SaleArrearsResp>>(
        {
            url: API.GetArrearsPage,
            params,
        }
    );
}