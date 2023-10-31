<!--
  - Copyright (C) 2023-2033 WanSen AI Team
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <template v-if="getShow">
    <LoginFormTitle class="enter-x"/>
    <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="username" class="enter-x">
        <Input
            size="large"
            v-model:value="formData.username"
            :placeholder="t('sys.login.userName')"
        />
      </FormItem>

      <FormItem name="phoneNumber" class="enter-x">
        <Input size="large" v-model:value="formData.phoneNumber" :placeholder="t('sys.login.mobile')"/>
      </FormItem>
      <FormItem name="sms" class="enter-x">
        <CountdownInput
            size="large"
            v-model:value="formData.sms"
            :placeholder="t('sys.login.smsCode')"
            count=120
            :sendCodeApi="sendCodeApi"
        />
      </FormItem>
      <FormItem name="password" class="enter-x">
        <InputPassword
            size="large"
            v-model:value="formData.password"
            :placeholder="t('sys.login.newPassword')"
        />
      </FormItem>

      <FormItem class="enter-x">
        <Button type="primary" size="large" block @click="handleReset" :loading="loading">
          {{ t('sys.login.updatePassword') }}
        </Button>
        <Button size="large" block class="mt-4" @click="handleBackLogin">
          {{ t('sys.login.backSignIn') }}
        </Button>
      </FormItem>
    </Form>
  </template>
</template>
<script lang="ts" setup>
import {reactive, ref, computed, unref} from 'vue';
import LoginFormTitle from './LoginFormTitle.vue';
import {Form, Input, Button} from 'ant-design-vue';
import {CountdownInput} from '/@/components/CountDown';
import {useI18n} from '/@/hooks/web/useI18n';
import {useLoginState, useFormRules, LoginStateEnum, useFormValid} from './useLogin';
import {sendSmsRegister, updatePassword} from "@/api/sys/user";
import {PageEnum} from "@/enums/pageEnum";
import {useGo} from "@/hooks/web/usePage";

const FormItem = Form.Item;
const {t} = useI18n();
const {handleBackLogin, getLoginState} = useLoginState();
const {getFormRules} = useFormRules();
const InputPassword = Input.Password;
const formRef = ref();
const loading = ref(false);
const go = useGo();
const formData = reactive({
  username: '',
  password: '',
  phoneNumber: '',
  sms: '',
});

const {validForm} = useFormValid(formRef);
const getShow = computed(() => unref(getLoginState) === LoginStateEnum.RESET_PASSWORD);

async function handleReset() {
  const data = await validForm();
  if (!data) return;
  loading.value = true;

  await updatePassword({
    username: data.username,
    password: data.password,
    phoneNumber: data.phoneNumber,
    sms: data.sms,
  })
      .then(() => {
        loading.value = false;
        go(PageEnum.BASE_LOGIN);
      })
      .catch(() => {
        loading.value = false;
      });
}

async function sendCodeApi(): Promise<boolean> {
  const phoneNumber = await formRef.value.validateFields(['phoneNumber']);
  if (phoneNumber == false) {
    return Promise.resolve(false)
  }
  // send code
  const result = await sendSmsRegister(2, formData.phoneNumber);
  if (result.code !== "A0002") {
    return Promise.resolve(false)
  }
  return Promise.resolve(true)
}

</script>
