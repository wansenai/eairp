<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit">
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="formData" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="原手机号码">
          <a-input-number v-model:value="formData.oldPhoneNumber" disabled/>
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="新手机号码"
                     :rules="[{ required: true, message: '请输入正确的手机号',
                      pattern: /^(0|86|17951)?(13[0-9]|15[012356789]|16[6]|19[89]]|17[01345678]|18[0-9]|14[579])[0-9]{8}$/,
                      trigger: 'change'}]">
          <a-input placeholder="请输入新手机号码" v-model:value="formData.phoneNumber"/>
        </a-form-item>
        <a-form-item label="验证码" :label-col="labelCol" :wrapper-col="wrapperCol">
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
import {Button, Form, FormItem, Input, InputNumber, Modal, Spin} from "ant-design-vue";
import {BasicModal, useModalInner} from '/@/components/Modal';
import {CountdownInput} from "@/components/CountDown";
import {sendSmsRegister} from "@/api/sys/user";
import {useFormRules, useFormValid} from "@/views/sys/login/useLogin";

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
    const title = ref('更换密保手机');
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
      console.info(data)
      formData.oldPhoneNumber = data.phoneNumber
    });

    const handleSubmit = () => {
      closeModal();
    };

    const handleCancel = () => {
      closeModal();
    };

    async function sendCodeApi() {
      const phoneNumber = await formRef.value.validateFields(['phoneNumber']);
      if(phoneNumber == false) {
        return Promise.resolve(false)
      }
      // sen code
      const result = await sendSmsRegister(0, formData.phoneNumber);
      if (result.code !== "A0002") {
        return Promise.resolve(false)
      }
      return Promise.resolve(true)
    }

    return {
      registerModal,
      title,
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
      getFormRules
    };
  }
};
</script>

<style scoped>

</style>