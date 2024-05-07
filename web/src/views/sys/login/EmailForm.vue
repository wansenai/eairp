<template>
  <div v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="email" class="enter-x">
        <Input
            size="large"
            v-model:value="formData.email"
            :placeholder="t('sys.login.email')"
            class="fix-auto-fill"
        />
      </FormItem>
      <FormItem name="emailCode" class="enter-x">
        <CountdownInput
            size="large"
            class="fix-auto-fill"
            v-model:value="formData.emailCode"
            :placeholder="t('sys.login.emailCode')"
            count=120
            :sendCodeApi="sendEmailCodeApi"
        />
      </FormItem>

      <FormItem class="enter-x">
        <Button type="primary" size="large" block @click="handleLogin" :loading="loading">
          {{ t('sys.login.loginButton') }}
        </Button>
        <Button size="large" block class="mt-4" @click="handleBackLogin">
          {{ t('sys.login.backSignIn') }}
        </Button>
      </FormItem>
    </Form>
  </div>
</template>
<script lang="ts" setup>
import { reactive, ref, computed, unref } from 'vue';
import { Form, Input, Button } from 'ant-design-vue';
import { CountdownInput } from '/@/components/CountDown';
import LoginFormTitle from './LoginFormTitle.vue';
import { useI18n } from '/@/hooks/web/useI18n';
import { useLoginState, useFormRules, useFormValid, LoginStateEnum } from './useLogin';
import {sendEmailCode, sendSmsRegister} from "@/api/sys/user";
import {PageEnum} from "@/enums/pageEnum";
import {useUserStore} from "@/store/modules/user";
import {useGo} from "@/hooks/web/usePage";

const FormItem = Form.Item;
const { t } = useI18n();
const { handleBackLogin, getLoginState } = useLoginState();
const { getFormRules } = useFormRules();
const userStore = useUserStore();
const go = useGo();
const formRef = ref();
const loading = ref(false);

const formData = reactive({
  email: '',
  emailCode: '',
  type: 1
});

const { validForm } = useFormValid(formRef);

const getShow = computed(() => unref(getLoginState) === LoginStateEnum.EMAIL);

async function handleLogin() {
  const data = await validForm();
  if (!data) return;
  loading.value = true;
  userStore
      .emailLogin({
        email: data.email,
        emailCode: data.emailCode,
        type: data.type
      })
      .then(() => {
        loading.value = false;
        go(PageEnum.BASE_HOME);
      })
      .catch(() => {
        loading.value = false;
      });
}

async function sendEmailCodeApi():Promise<boolean> {
  const email = await formRef.value.validateFields(['email']);
  if(email == false) {
    return Promise.resolve(false)
  }
  // sen code
  const result = await sendEmailCode(2, formData.email);
  if (result.code !== "A0101") {
    return Promise.resolve(false)
  }
  return Promise.resolve(true)
}

</script>
