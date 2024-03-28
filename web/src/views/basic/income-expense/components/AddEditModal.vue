<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
import { ref, computed, unref} from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from '@/views/basic/income-expense/incomeExpense.data';
import { AddOrUpdateIncomeExpenseReq } from '@/api/basic/model/incomeExpenseModel';
import { addOrUpdateIncomeExpense } from '@/api/basic/incomeExpense';
import {useI18n} from "vue-i18n";

const rowId = ref('');
const isUpdate = ref(true);
const getTitle = computed(() => (!unref(isUpdate) ? t('basic.incomeExpense.addIncomeExpense') : t('basic.incomeExpense.editIncomeExpense')));
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
    const updateIncomeExpenseReq: AddOrUpdateIncomeExpenseReq = {
      id: rowId.value,
      ...values,
    };
    const result = await addOrUpdateIncomeExpense(updateIncomeExpenseReq);
    if (result.code === 'I0002') {
      closeModal();
      emitSuccess('success');
    }
  } else {
    const addIncomeExpenseReq: AddOrUpdateIncomeExpenseReq = {
      name: values.name,
      type: values.type,
      status: values.status,
      sort: values.sort,
      remark: values.remark,
    };

    const result = await addOrUpdateIncomeExpense(addIncomeExpenseReq);
    if (result.code === 'I0001') {
      closeModal();
      emitSuccess('success');
    }
  }

  setModalProps({ confirmLoading: false });

};
</script>