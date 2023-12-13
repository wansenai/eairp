<template>
  <div v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="username" class="enter-x">
        <Input
          class="fix-auto-fill"
          size="large"
          v-model:value="formData.username"
          :placeholder="t('sys.login.userName')"
        />
      </FormItem>
      <FormItem name="phoneNumber" class="enter-x">
        <Input
          size="large"
          v-model:value="formData.phoneNumber"
          :placeholder="t('sys.login.mobile')"
          class="fix-auto-fill"
        />
      </FormItem>
      <FormItem name="sms" class="enter-x">
        <CountdownInput
          size="large"
          class="fix-auto-fill"
          count=120
          v-model:value="formData.sms"
          :placeholder="t('sys.login.smsCode')"
          :sendCodeApi="sendCodeApi"
        />
      </FormItem>
      <FormItem name="password" class="enter-x">
        <StrengthMeter
          size="large"
          v-model:value="formData.password"
          :placeholder="t('sys.login.password')"
        />
      </FormItem>
      <FormItem name="confirmPassword" class="enter-x">
        <InputPassword
          size="large"
          visibilityToggle
          v-model:value="formData.confirmPassword"
          :placeholder="t('sys.login.confirmPassword')"
        />
      </FormItem>

      <FormItem class="enter-x" name="policy">
        <!-- No logic, you need to deal with it yourself -->
        <Checkbox v-model:checked="formData.policy" size="small">
          {{ t('sys.login.policy') }}
        </Checkbox>
      </FormItem>

      <Button
        type="primary"
        class="enter-x"
        size="large"
        block
        @click="handleRegister"
        :loading="loading"
      >
        {{ t('sys.login.registerButton') }}
      </Button>
      <Button size="large" block class="mt-4 enter-x" @click="handleBackLogin">
        {{ t('sys.login.backSignIn') }}
      </Button>
    </Form>
  </div>
</template>
<script lang="ts" setup>
  import { reactive, ref, unref, computed } from 'vue';
  import LoginFormTitle from './LoginFormTitle.vue';
  import { Form, Input, Button, Checkbox} from 'ant-design-vue';
  import { StrengthMeter } from '/@/components/StrengthMeter';
  import { CountdownInput } from '/@/components/CountDown';
  import { useI18n } from '/@/hooks/web/useI18n';
  import {
    useLoginState,
    useFormRules,
    useFormValid,
    LoginStateEnum, encryptByAES,
  } from './useLogin';
  import {register, sendSmsRegister} from '/@/api/sys/user'

  const FormItem = Form.Item;
  const InputPassword = Input.Password;
  const { t } = useI18n();
  const { handleBackLogin, getLoginState, handleBackMobileLogin } = useLoginState();

  const formRef = ref();
  const loading = ref(false);

  const formData = reactive({
    username: '',
    password: '',
    confirmPassword: '',
    phoneNumber: '',
    sms: '',
    policy: false,
  });

  const { getFormRules } = useFormRules(formData);
  const { validForm } = useFormValid(formRef);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.REGISTER);

  async function handleRegister() {
    const data = await validForm();
    if (!data) return;
    console.log(data);
    loading.value = true;

    const secretKey = '7Fd2u4qF/3k0z6O1c9AeC7==';
    const encryptedPassword = encryptByAES(data.password, secretKey);
    const result = await register({
      username: data.username,
      password: encryptedPassword,
      phoneNumber: data.phoneNumber,
      sms: data.sms,
    });

    if (result.code === "A0001") {
      setTimeout(() => {
        handleBackLogin();
      }, 2000);
      loading.value = false;
    } else if(result.code === "A0112"){
      handleBackMobileLogin()
      loading.value = false;
    } else {
      loading.value = false;
    }
  }

  async function sendCodeApi():Promise<boolean> {
    const phoneNumber = await formRef.value.validateFields(['phoneNumber']);
    if(phoneNumber == false) {
      return Promise.resolve(false)
    }
    // sen code
    const result = await sendSmsRegister(0, formData.phoneNumber);
    if (result.code !== "A0100") {
      return Promise.resolve(false)
    }
    return Promise.resolve(true)
  }

</script>
