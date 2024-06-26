import {defHttp} from '@/utils/http/axios';
import {
    addOrUpdateUserReq, emailLoginReq,
    GetUserInfoModel,
    LoginReq,
    LoginResp,
    mobileLoginReq, queryUserListReq,
    registerReq, resetEmailReq, resetPasswordReq, resetPhoneNumberReq, updatePasswordByEmailReq,
    updatePasswordReq,
    updateUserInfoReq,
} from './model/userModel';

import {ErrorMessageMode, SuccessMessageMode, UploadFileParams} from '#/axios';
import {BaseDataResp, BaseResp} from "@/api/model/baseModel";
import {ContentTypeEnum} from "@/enums/httpEnum";

enum Api {
    Login = '/user/login',
    MobileLogin = '/user/mobileLogin',
    EmailLogin = '/user/emailLogin',
    Logout = '/user/logout',
    Register = '/user/register',
    SMS = '/v2/common/sms',
    EmailSMS = '/v2/common/email',
    UpdatePassword = '/user/updatePassword',
    UpdatePasswordByEmail = '/user/updatePasswordByEmail',
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
    UpdateAvatar = '/user/uploadAvatar',
    UserUpdatePassword = '/user/userUpdatePassword',
    ResetPhoneNumber = '/user/resetPhoneNumber',
    ResetEmail = '/user/resetEmail',
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

export function emailLogin(params: emailLoginReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseDataResp<LoginResp>>(
        {
            url: Api.EmailLogin,
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

export function sendEmailCode(type: number, email: string, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.get<BaseResp>(
        {
            url: `${Api.EmailSMS}/${type}/${email}`
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
export function UpdatePasswordByEmail(params: updatePasswordByEmailReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {
            url: Api.UpdatePasswordByEmail,
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
        {url: `${Api.DeleteUser}?ids=${ids}`},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    )
}

export function resetPassword(id: string, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.post<BaseResp>(
        {url: `${Api.ResetPassword}?id=${id}`},
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    )
}

export function getPermCode() {
    return defHttp.get<string[]>({url: Api.GetPermCode});
}

export function doLogout(successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.get({url: Api.Logout}, {
        successMessageMode: successMode,
        errorMessageMode: errorMode,
    });
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

export function UpdateAvatar(params: UploadFileParams) {
    return defHttp.post<BaseResp>(
        {
            url: Api.UpdateAvatar,
            params,
            headers: {
                'Content-type': ContentTypeEnum.FORM_DATA,
                // @ts-ignore
                ignoreCancelToken: true,
            },
        }
    );
}

export function userUpdatePassword(params: resetPasswordReq) {
    return defHttp.put<BaseResp>(
        {
            url: Api.UserUpdatePassword,
            params,
        }
    );
}

export function resetPhoneNumber(params: resetPhoneNumberReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.put<BaseResp>(
        {
            url: Api.ResetPhoneNumber,
            params,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}

export function resetEmail(params: resetEmailReq, successMode: SuccessMessageMode = 'notice', errorMode: ErrorMessageMode = 'notice') {
    return defHttp.put<BaseResp>(
        {
            url: Api.ResetEmail,
            params,
        },
        {
            successMessageMode: successMode,
            errorMessageMode: errorMode,
        }
    );
}