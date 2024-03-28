<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="t('basic.account.accountPhone')" @ok="handleSubmit" @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="formData" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-input v-model:value="userId" v-show="false"/>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('basic.account.phone.oldPhone')">
          <a-input-number v-model:value="formData.oldPhoneNumber" disabled/>
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('basic.account.phone.newPhone')"
                     :rules="[{ required: true}]">
          <a-input :placeholder="t('basic.account.phone.inputNewPhone')" v-model:value="formData.phoneNumber"/>
        </a-form-item>
        <a-form-item :label="t('basic.account.phone.code')" :label-col="labelCol" :wrapper-col="wrapperCol" :rules="[{ required: true}]">
          <CountdownInput
              size="large"
              class="fix-auto-fill"
              count=120
              v-model:value="formData.sms"
              :sendCodeApi="sendCodeApi"
          />
        </a-form-item>
      </a-form>
    </a-spin>
  </BasicModal>
</template>

<script lang="ts">
import {reactive, ref} from 'vue';
import { useI18n } from '/@/hooks/web/useI18n';
import {Button, Form, FormItem, Input, InputNumber, Modal, Spin} from "ant-design-vue";
import {BasicModal, useModalInner} from '/@/components/Modal';
import {CountdownInput} from "@/components/CountDown";
import {sendSmsRegister, resetPhoneNumber} from "@/api/sys/user";
import {useFormRules, useFormValid} from "@/views/sys/login/useLogin";
import {useMessage} from "@/hooks/web/useMessage";

export default {
  name: 'BindPhoneModal',
  components: {
    CountdownInput,
    Input,
    BasicModal,
    'a-modal': Modal,
    'a-button': Button,
    'a-spin': Spin,
    'a-form': Form,
    'a-form-item': FormItem,
    'a-input-number': InputNumber
  },
  setup(_, context) {
    const { createMessage } = useMessage();
    const { t } = useI18n();
    const userId = ref('');
    const openBindPhoneModal = ref(false);
    const labelCol = {
      xs: {span: 24},
      sm: {span: 5},
    };
    const wrapperCol = {
      xs: {span: 24},
      sm: {span: 16},
    };
    const formData = reactive({
      phoneNumber: '',
      oldPhoneNumber: '',
      sms: '',
    });
    const confirmLoading = ref(false);
    const formRef = ref();
    const { getFormRules } = useFormRules(formData);
    const { validForm } = useFormValid(formRef);

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width:600});
      formData.oldPhoneNumber = data.phoneNumber
      userId.value = data.id
    });

    const handleSubmit = async () => {
      const pattern:any = /^(0|86|17951)?(13[0-9]|15[012356789]|16[6]|19[89]]|17[01345678]|18[0-9]|14[579])[0-9]{8}$/;
      const valid = pattern.test(formData.phoneNumber);
      if(!valid) {
        createMessage.info(t('basic.account.phone.notice'));
        return;
      }
      if (!formData.sms) {
        createMessage.info(t('basic.account.phone.inputCode'));
        return;
      }
      validForm().then(async () => {
        const data: any = {
          userId: userId.value,
          oldPhoneNumber: formData.oldPhoneNumber,
          phoneNumber: formData.phoneNumber,
          sms: formData.sms
        };
        const result = await resetPhoneNumber(data);
        if (result.code === "A0016") {
          handleCancel();
        }
      })
    };

    const handleCancel = () => {
      closeModal();
      // 清空formData里的数据
      formData.phoneNumber = '';
      formData.sms = '';
    };

    async function sendCodeApi() {
      const pattern:any = /^(0|86|17951)?(13[0-9]|15[012356789]|16[6]|19[89]]|17[01345678]|18[0-9]|14[579])[0-9]{8}$/;
      const valid = pattern.test(formData.phoneNumber);
      if(!valid) {
        return Promise.resolve(false)
      }
      // sen code
      const result = await sendSmsRegister(3, formData.phoneNumber);
      if (result.code !== "A0100") {
        return Promise.resolve(false)
      }
      return Promise.resolve(true)
    }

    return {
      t,
      registerModal,
      formData,
      labelCol,
      wrapperCol,
      confirmLoading,
      formRef,
      close,
      handleSubmit,
      handleCancel,
      openBindPhoneModal,
      sendCodeApi,
      getFormRules,
      userId
    };
  }
};
</script>

<style scoped>

</style>