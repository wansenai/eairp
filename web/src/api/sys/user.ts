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

import {ErrorMessageMode} from '/#/axios';
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
}

/**
 * @description: user login api
 */
export function login(params: LoginReq, mode: ErrorMessageMode = 'notice') {
  return defHttp.post<BaseDataResp<LoginResp>>(
    {
      url: Api.Login,
      params,
    },
    {
      errorMessageMode: mode,
      successMessageMode: mode,
    },
  );
}

export function mobileLogin(params: mobileLoginReq, mode: ErrorMessageMode = 'notice') {
  return defHttp.post<BaseDataResp<LoginResp>>(
    {
      url: Api.MobileLogin,
      params,
    },
    {
      errorMessageMode: mode,
      successMessageMode: mode,
    },
  );
}

export function register(params: registerReq, mode: ErrorMessageMode = 'notice') {
  return defHttp.post<BaseResp>(
    {
      url: Api.Register,
      params,
    },
    {
      errorMessageMode: mode,
      successMessageMode: mode,
    }
  )
}

export function sendSmsRegister(type: number, phoneNumber: string, mode: ErrorMessageMode = 'notice') {
  return defHttp.get<BaseResp>(
    {
      url: `${Api.SMS}/${type}/${phoneNumber}`
    },
    {
      errorMessageMode: mode,
      successMessageMode: mode,
    }
  )
}

export function updatePassword(params: updatePasswordReq, mode: ErrorMessageMode = 'notice') {
  return defHttp.post<BaseResp>(
    {
      url: Api.UpdatePassword,
      params,
    },
    {
      errorMessageMode: mode,
      successMessageMode: mode,
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

export function getUserList(params: queryUserListReq, mode: ErrorMessageMode = 'notice') {
  return defHttp.post<BaseDataResp<GetUserInfoModel>>(
    {url: Api.List, params},
    {
      errorMessageMode: mode,
      successMessageMode: mode,
    },
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

export function updateUser(params: updateUserInfoReq, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {url: Api.UpdateUser, params},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        },
    )
}

export function updateStatus(params: { id: any; status: number }, mode: ErrorMessageMode = 'notice') {
  return defHttp.post<BaseResp>(
      {url: Api.UpdateUser, params},
      {
        errorMessageMode: mode,
        successMessageMode: mode,
      },
  )
}

export function addOrUpdateUser(params: addOrUpdateUserReq) {
    return defHttp.post<BaseResp>(
        {url: Api.AddOrUpdateUser, params},
    )
}

export function deleteUser(ids: string[] , mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        { url: `${Api.DeleteUser}?ids=${ids}`},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        }
    )
}

export function resetPassword(id: string, mode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        { url: `${Api.ResetPassword}?id=${id}`},
        {
            errorMessageMode: mode,
            successMessageMode: mode,
        }
    )
}

export function getPermCode() {
  return defHttp.get<string[]>({url: Api.GetPermCode});
}

export function doLogout() {
  return defHttp.get({url: Api.Logout});
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
