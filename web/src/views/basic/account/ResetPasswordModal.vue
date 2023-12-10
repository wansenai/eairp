<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import {useMessage} from "@/hooks/web/useMessage";
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from "@/components/Form";
import {resetPasswordFormSchema} from "@/views/basic/account/data";
import {userUpdatePassword} from "@/api/sys/user";

const {createMessage} = useMessage();
const title = ref('更换账户密码');

const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
  resetFields();
  setModalProps({ confirmLoading: false, destroyOnClose: true,});
  setFieldsValue({
    ...data,
  });
});

const [registerForm, { setFieldsValue, resetFields, validate, getFieldsValue }] = useForm({
  labelWidth: 100,
  schemas: resetPasswordFormSchema,
  baseColProps: { span: 24 },
  showActionButtonGroup: false,
  actionColOptions: {
    span: 23,
  },
});

const handleSubmit = async () => {
  const data = getFieldsValue();
  const valid = await validate();
  if (!valid) return;
  if (data.newPassword !== data.confirmPassword) {
    createMessage.info('两次输入的密码不一致');
    return;
  }
  const res = await userUpdatePassword(data);
  if (res.code === "A0015") {
    createMessage.success('密码修改成功');
    closeModal();
  }
}
</script>