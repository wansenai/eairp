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

import type {ValidationRule, FormInstance} from 'ant-design-vue/lib/form/Form';
import type {RuleObject, NamePath} from 'ant-design-vue/lib/form/interface';
import {ref, computed, unref, Ref} from 'vue';
import {useI18n} from '/@/hooks/web/useI18n';

export enum LoginStateEnum {
    LOGIN,
    REGISTER,
    RESET_PASSWORD,
    MOBILE,
    QR_CODE,
}

const currentState = ref(LoginStateEnum.LOGIN);

// 这里也可以优化
// import { createGlobalState } from '@vueuse/core'

export function useLoginState() {
    function setLoginState(state: LoginStateEnum) {
        currentState.value = state;
    }

    const getLoginState = computed(() => currentState.value);

    function handleBackLogin() {
        setLoginState(LoginStateEnum.LOGIN);
    }

    function handleBackMobileLogin() {
        setLoginState(LoginStateEnum.MOBILE);
    }

    return {setLoginState, getLoginState, handleBackLogin, handleBackMobileLogin};
}


export function useFormValid<T extends Object = any>(formRef: Ref<FormInstance>) {
    const validate = computed(() => {
        const form = unref(formRef);
        return form?.validate ?? ((_nameList?: NamePath) => Promise.resolve());
    });

    async function validForm() {
        const form = unref(formRef);
        if (!form) return;
        const data = await form.validate();
        return data as T;
    }

    return {validate, validForm};
}


export function useFormRules(formData?: Recordable) {
    const {t} = useI18n();

    const getAccountFormRule = computed(() => createRule(t('sys.login.accountPlaceholder')));
    const getPasswordFormRule = computed(() => createRule(t('sys.login.passwordPlaceholder')));
    const getCaptchaFormRule = computed(() => createRule(t('sys.login.captchaPlaceholder')));
    const getSmsFormRule = computed(() => createRule(t('sys.login.smsPlaceholder')));
    const getMobileFormRule = computed(() => phoneNumberRule());

    const validatePolicy = async (_: RuleObject, value: boolean) => {
        return !value ? Promise.reject(t('sys.login.policyPlaceholder')) : Promise.resolve();
    };

    const validateConfirmPassword = (password: string) => {
        return async (_: RuleObject, value: string) => {
            if (!value) {
                return Promise.reject(t('sys.login.passwordPlaceholder'));
            }
            if (value !== password) {
                return Promise.reject(t('sys.login.diffPwd'));
            }
            return Promise.resolve();
        };
    };

    const getFormRules = computed((): { [k: string]: ValidationRule | ValidationRule[] } => {
        const accountFormRule = unref(getAccountFormRule);
        const passwordFormRule = unref(getPasswordFormRule);
        const captchaFormRule = unref(getCaptchaFormRule);
        const smsFormRule = unref(getSmsFormRule);
        const mobileFormRule = unref(getMobileFormRule);

        const mobileRule = {
            sms: smsFormRule,
            mobile: mobileFormRule,
        };
        switch (unref(currentState)) {
            // register form rules
            case LoginStateEnum.REGISTER:
                return {
                    username: accountFormRule,
                    password: passwordFormRule,
                    phoneNumber: mobileFormRule,
                    captcha: captchaFormRule,
                    confirmPassword: [
                        {validator: validateConfirmPassword(formData?.password), trigger: 'change'},
                    ],
                    policy: [{validator: validatePolicy, trigger: 'change'}],
                    ...mobileRule,
                };

            // reset password form rules
            case LoginStateEnum.RESET_PASSWORD:
                return {
                    username: accountFormRule,
                    password: passwordFormRule,
                    phoneNumber: mobileFormRule,
                    sms: smsFormRule,
                };

            // mobile form rules
            case LoginStateEnum.MOBILE:
                return {
                    phoneNumber: mobileFormRule,
                    sms: smsFormRule,
                };

            // login form rules
            default:
                return {
                    account: accountFormRule,
                    password: passwordFormRule,
                    captcha: captchaFormRule,
                };
        }
    });
    return {getFormRules};
}

function createRule(message: string) {
    return [
        {
            required: true,
            message,
            trigger: 'change',
        },
    ];
}

function phoneNumberRule() {
    return [
        {
            required: true,
            pattern: /^(0|86|17951)?(13[0-9]|15[012356789]|16[6]|19[89]]|17[01345678]|18[0-9]|14[579])[0-9]{8}$/,
            message: "请输入正确的手机号",
            trigger: 'change',
        }
    ];
}
