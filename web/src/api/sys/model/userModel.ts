/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @description: Login interface parameters
 */
export interface LoginReq {
  username: string;
  password: string;
  captcha: string;
  captchaId: string;
}

export interface mobileLoginReq {
  phoneNumber: string;
  sms: string;
  type: number;
}

export interface registerReq {
  username: string;
  password: string;
  phoneNumber: string;
  sms: number;
}

export interface queryUserListReq {
  username: string;
  name: string;
}

export interface updatePasswordReq {
  username: string;
  password: string;
  phoneNumber: string;
  sms: number;
}

export interface updateUserInfoReq {
  id: number | string;
  name: string;
  status: number;
  email: string;
  phoneNumber: string;
  position: string;
  leaderFlag: number;
}

export interface addOrUpdateUserReq {
  id: number | string;
  username: string;
  password: string;
  name: string;
  email: string;
  phoneNumber: string;
  roleId: number;
  deptId: number;
  remake: string;
}

export interface RoleInfo {
  roleName: string;
  value: string;
}

export interface LoginResp {
  userId: string | number;
  token: string;
  expire?: number;
}

/**
 * @description: Login interface return value
 */
export interface LoginResultModel {
  userId: string | number;
  token: string;
  roles: RoleInfo[];
}

/**
 * @description: Get user information return value
 */
export interface GetUserInfoModel {
  // 用户id
  id: string | number;
  // 用户名
  username: string;
  // 昵称
  name: string;
  // 邮箱
  email: string;
  // 电话
  phoneNumber: string;
  // 状态
  status: number;
  // 用户角色名称
  roleName: string;
}
