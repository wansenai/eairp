import {defHttp} from '/@/utils/http/axios';
import {
    addOrUpdateUserReq,
    GetUserInfoModel,
    LoginReq,
    LoginResp,
    mobileLoginReq, queryUserListReq,
    registerReq,
    updatePasswordReq,
    updateUserInfoReq,
} from './model/userModel';

import {ErrorMessageMode, SuccessMessageMode} from '/#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";

enum Api {
  Login = '/user/login',
  MobileLogin = '/user/mobileLogin',
  Logout = '/user/logout',
  Register = '/user/register',
  SMS = '/v2/common/sms',
  UpdatePassword = '/user/updatePassword',
  GetUserInfo = '/user/info',
  GetPermCode = '/user/perm',
  TestRetry = '/testRetry',
  List = '/user/list',
  ListAll = '/user/listAll',
  UpdateUser = '/user/update',
  AddOrUpdateUser = '/user/addOrUpdate',
  DeleteUser = '/user/delete',
  ResetPassword = '/user/resetPassword',
  GetUserOperatorList = '/user/operator',
}

/**
 * @description: user login api
 */
export function login(params: LoginReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
  return defHttp.post<BaseDataResp<LoginResp>>(
    {
      url: Api.Login,
      params,
    },
    {
        successMessageMode: successMode,
        errorMessageMode: errorMode,
    },
  );
}

export function mobileLogin(params: mobileLoginReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
  return defHttp.post<BaseDataResp<LoginResp>>(
    {
      url: Api.MobileLogin,
      params,
    },
    {
        successMessageMode: successMode,
        errorMessageMode: errorMode,
    },
  );
}

export function register(params: registerReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
  return defHttp.post<BaseResp>(
    {
      url: Api.Register,
      params,
    },
    {
        successMessageMode: successMode,
        errorMessageMode: errorMode,
    }
  )
}

export function sendSmsRegister(type: number, phoneNumber: string, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
  return defHttp.get<BaseResp>(
    {
      url: `${Api.SMS}/${type}/${phoneNumber}`
    },
    {
        successMessageMode: successMode,
        errorMessageMode: errorMode,
    }
  )
}

export function updatePassword(params: updatePasswordReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
  return defHttp.post<BaseResp>(
    {
      url: Api.UpdatePassword,
      params,
    },
    {
        successMessageMode: successMode,
        errorMessageMode: errorMode,
    }
  )
}

/**
 * @description: getUserInfo
 */
export function getUserInfo() {
  return defHttp.get<BaseDataResp<GetUserInfoModel>>(
    {url: Api.GetUserInfo},
    {errorMessageMode: 'none'},
  );
}

export function getUserList(params: queryUserListReq) {
  return defHttp.post<BaseDataResp<GetUserInfoModel>>(
    {url: Api.List, params},
  );
}

export function getTenantUserList(mode: ErrorMessageMode = 'notice') {
    return defHttp.get<BaseDataResp<GetUserInfoModel>>(
        {url: Api.ListAll},
        {
        errorMessageMode: mode,
        successMessageMode: mode,
        },
    );
}

export function updateUser(params: updateUserInfoReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {url: Api.UpdateUser, params},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        },
    )
}

export function updateStatus(params: { id: any; status: number }, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
  return defHttp.post<BaseResp>(
      {url: Api.UpdateUser, params},
      {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
      },
  )
}

export function addOrUpdateUser(params: addOrUpdateUserReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {url: Api.AddOrUpdateUser, params},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    )
}

export function deleteUser(ids: string[], successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        { url: `${Api.DeleteUser}?ids=${ids}`},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    )
}

export function resetPassword(id: string, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        { url: `${Api.ResetPassword}?id=${id}`},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    )
}

export function getPermCode() {
  return defHttp.get<string[]>({url: Api.GetPermCode});
}

export function doLogout() {
  return defHttp.get({url: Api.Logout});
}

export function getUserOperatorList() {
    return defHttp.get<BaseDataResp<GetUserInfoModel>>({url: Api.GetUserOperatorList});
}

export function testRetry() {
  return defHttp.get(
    {url: Api.TestRetry},
    {
      retryRequest: {
        isOpenRetry: true,
        count: 5,
        waitTime: 1000,
      },
    },
  );
}
