<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="t('basic.account.accountPassword')" @ok="handleSubmit">
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
import {useI18n} from "@/hooks/web/useI18n";
const { t } = useI18n();
const {createMessage} = useMessage();

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
    createMessage.info(t('basic.account.password.noticeOne'));
    return;
  }
  const res = await userUpdatePassword(data);
  if (res.code === "A0015") {
    createMessage.success(t('basic.account.password.updateSuccess'));
    closeModal();
  }
}
</script>