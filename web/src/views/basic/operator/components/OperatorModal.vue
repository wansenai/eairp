<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
import { ref, computed, unref} from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from '@/views/basic/operator/operator.data';
import { AddOrUpdateOperatorReq } from '@/api/basic/model/operatorModel';
import { addOrUpdateOperator } from '@/api/basic/operator';
import {useI18n} from "vue-i18n";

const rowId = ref('');
const isUpdate = ref(true);
const getTitle = computed(() => (!unref(isUpdate) ? t('basic.operator.addOperator') : t('basic.operator.editOperator')));
const emitSuccess = defineEmits(['success', 'register']);
const { t } = useI18n();
const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
  labelWidth: 100,
  baseColProps: { span: 24 },
  schemas: formSchema,
  showActionButtonGroup: false,
  actionColOptions: {
    span: 23,
  },
});

const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
  resetFields();
  setModalProps({ confirmLoading: false, destroyOnClose: true,});
  isUpdate.value = !!data?.isUpdate;

  if (unref(isUpdate)) {
    rowId.value = data.record.id;
    setFieldsValue({
      ...data.record,
    });
  }
});

const handleSubmit = async () => {
  const values = await validate();
  setModalProps({ confirmLoading: true });

  if (unref(isUpdate)) {
    const updateOperatorReq: AddOrUpdateOperatorReq = {
      id: rowId.value,
      ...values,
    };
    const result = await addOrUpdateOperator(updateOperatorReq);
    if (result.code === 'O0002') {
      closeModal();
      emitSuccess('success');
    }
  } else {
    const addOperatorReq: AddOrUpdateOperatorReq = {
      name: values.name,
      type: values.type,
      status: values.status,
      sort: values.sort,
      remark: values.remark,
    };

    const result = await addOrUpdateOperator(addOperatorReq);
    if (result.code === 'O0001') {
      closeModal();
      emitSuccess('success');
    }
  }

  setModalProps({ confirmLoading: false });

};
</script>