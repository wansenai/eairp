<template>
  <template v-if="getShow">
    <LoginFormTitle class="enter-x"/>
    <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
      <div class="pt-4 pb-4">
        <ARadioGroup v-model:value="formData.type" button-style="solid" size="large">
          <ARadioButton class="bg-transparent" value="email">
            {{ t('sys.login.email') }}
          </ARadioButton>
          <ARadioButton class="bg-transparent" value="phoneNumber">
            {{ t('sys.login.mobile') }}
          </ARadioButton>
        </ARadioGroup>
      </div>

      <FormItem name="target" class="enter-x">
        <Input
            size="large"
            v-model:value="formData.target"
            v-model:placeholder="emailOrPhonePlaceholder"
        />
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
import {Form, Input, Button, RadioGroup, RadioButton} from 'ant-design-vue';
import {CountdownInput} from '/@/components/CountDown';
import {useI18n} from '/@/hooks/web/useI18n';
import {useLoginState, useFormRules, LoginStateEnum, useFormValid} from './useLogin';
import {sendEmailCode, sendSmsRegister, updatePassword, UpdatePasswordByEmail} from "@/api/sys/user";
import {PageEnum} from "@/enums/pageEnum";
import {useGo} from "@/hooks/web/usePage";

const ARadioGroup = RadioGroup;
const ARadioButton = RadioButton;
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
  type: 'email',
  target: '',
});

const {validForm} = useFormValid(formRef);
const getShow = computed(() => unref(getLoginState) === LoginStateEnum.RESET_PASSWORD);

const emailOrPhonePlaceholder = computed(() => {
  if (formData.type === 'email') {
    return t('sys.login.emailPlaceholder');
  } else {
    return t('sys.login.mobilePlaceholder');
  }
});

async function handleReset() {
  const data = await validForm();
  if (!data) return;
  loading.value = true;
  if(formData.type == 'phoneNumber') {
    await updatePassword({
      password: data.password,
      phoneNumber: formData.target,
      sms: data.sms,
    })
        .then(() => {
          loading.value = false;
          go(PageEnum.BASE_LOGIN);
        })
        .catch(() => {
          loading.value = false;
        });
  } else {
    await UpdatePasswordByEmail({
      password: data.password,
      email: formData.target,
      emailCode: data.sms,
    })
        .then(() => {
          loading.value = false;
          go(PageEnum.BASE_LOGIN);
        })
        .catch(() => {
          loading.value = false;
        });
  }
}

async function sendCodeApi(): Promise<boolean> {
  if(formData.type == 'phoneNumber') {
    const phoneNumber = await formRef.value.validateFields(formData.target);
    if (phoneNumber == false) {
      return Promise.resolve(false)
    }
    // send code
    const result = await sendSmsRegister(2, formData.target);
    if (result.code !== "A0100") {
      return Promise.resolve(false)
    }
    return Promise.resolve(true)
  } else {
    const email = await formRef.value.validateFields(formData.target);
    if(email == false) {
      return Promise.resolve(false)
    }
    // sen code
    const result = await sendEmailCode(0, formData.target);
    if (result.code !== "A0101") {
      return Promise.resolve(false)
    }
    return Promise.resolve(true)
  }
}

</script>
