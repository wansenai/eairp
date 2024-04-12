import type {ValidationRule, FormInstance} from 'ant-design-vue/lib/form/Form';
import type {RuleObject, NamePath} from 'ant-design-vue/lib/form/interface';
import {ref, computed, unref, Ref} from 'vue';
import {useI18n} from '/@/hooks/web/useI18n';
import CryptoJS from 'crypto-js';

export enum LoginStateEnum {
    LOGIN,
    REGISTER,
    RESET_PASSWORD,
    MOBILE,
    QR_CODE,
    EMAIL
}

/**
 * AES加密
 * @param plainText 明文
 * @param keyInBase64Str base64编码后的key
 * @returns {string} base64编码后的密文
 */
export function encryptByAES(plainText, keyInBase64Str) {
    let key = CryptoJS.enc.Base64.parse(keyInBase64Str);
    let encrypted = CryptoJS.AES.encrypt(plainText, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7,
    });
    return encrypted.ciphertext.toString(CryptoJS.enc.Base64);
}

/**
 * AES解密
 * @param cipherText 密文
 * @param keyInBase64Str base64编码后的key
 * @return 明文
 */
export function decryptByAES(cipherText, keyInBase64Str) {
    let key = CryptoJS.enc.Base64.parse(keyInBase64Str);
    let decrypted = CryptoJS.AES.decrypt(cipherText, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7,
    });

    return decrypted.toString(CryptoJS.enc.Utf8);
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
    const getEmailFormRule = computed( () => emailRule());

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
        const emailFormRule = unref(getEmailFormRule);

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
            // email form rules
            case LoginStateEnum.EMAIL:
                return {
                    email: emailFormRule,
                    emailCode: smsFormRule,
                }

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
    const {t} = useI18n();
    return [
        {
            required: true,
            pattern: /^(0|86|17951)?(13[0-9]|15[012356789]|16[6]|19[89]]|17[01345678]|18[0-9]|14[579])[0-9]{8}$/,
            message: t('sys.login.correctMobilePlaceholder'),
            trigger: 'change',
        }
    ];
}

function emailRule() {
    const {t} = useI18n();
    return [
        {
            required: true,
            pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
            message: t('sys.login.correctEmailPlaceholder'),
            trigger: 'change',
        }
    ];
}
