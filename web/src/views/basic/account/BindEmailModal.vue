<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="formData" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-input v-model:value="userId" v-show="false"/>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="原邮箱地址">
          <a-input-number v-model:value="formData.oldEmail" disabled/>
        </a-form-item>
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="新邮箱地址"
                     :rules="[{ required: true}]">
          <a-input placeholder="请输入新邮箱" v-model:value="formData.email"/>
        </a-form-item>
        <a-form-item label="邮箱验证码" :label-col="labelCol" :wrapper-col="wrapperCol" :rules="[{ required: true}]">
          <CountdownInput
              size="large"
              class="fix-auto-fill"
              count=120
              v-model:value="formData.emailCode"
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
import {sendEmailCode, resetEmail} from "@/api/sys/user";
import {useFormRules, useFormValid} from "@/views/sys/login/useLogin";
import {useMessage} from "@/hooks/web/useMessage";

export default {
  name: 'BindEmailModal',
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
    const userId = ref('');
    const title = ref('更换密保邮箱');
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
      oldEmail: '',
      email: '',
      emailCode: '',
    });
    const confirmLoading = ref(false);
    const formRef = ref();
    const { getFormRules } = useFormRules(formData);
    const { validForm } = useFormValid(formRef);

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width:600});
      formData.oldEmail = data.email
      userId.value = data.id
    });

    const handleSubmit = async () => {
      const pattern:any = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
      const valid = pattern.test(formData.email);
      if(!valid) {
        createMessage.info("请输入正确的邮箱地址");
        return;
      }
      if (!formData.emailCode) {
        createMessage.info("请输入邮箱验证码");
        return;
      }
      validForm().then(async () => {
        const data: any = {
          userId: userId.value,
          oldEmail: formData.oldEmail,
          email: formData.email,
          emailCode: formData.emailCode
        };
        const result = await resetEmail(data);
        if (result.code === "A0017") {
          handleCancel();
        }
      })
    };

    const handleCancel = () => {
      closeModal();
      formData.email = '';
      formData.emailCode = '';
    };

    async function sendCodeApi() {
      const pattern:any = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
      const valid = pattern.test(formData.email);
      if(!valid) {
        createMessage.info("请输入正确的邮箱地址");
        return Promise.resolve(false)
      }
      const result = await sendEmailCode(1, formData.email);
      if (result.code !== "A0101") {
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
      getFormRules,
      userId
    };
  }
};
</script>

<style scoped>

</style>