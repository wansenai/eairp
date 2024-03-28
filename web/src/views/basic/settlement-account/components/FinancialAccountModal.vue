<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
import { ref, computed, unref} from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from '@/views/basic/settlement-account/financialAccount.data';
import { AddOrUpdateAccountReq } from '@/api/financial/model/accountModel';
import { addOrUpdateAccount } from '@/api/financial/account';
import {useI18n} from "vue-i18n";

const { t } = useI18n();
const rowId = ref('');
const isUpdate = ref(true);
const getTitle = computed(() => (!unref(isUpdate) ? t('basic.settlement.addSettlementAccount') : t('basic.settlement.editSettlementAccount')));
const emitSuccess = defineEmits(['success','register']);

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
    const updateAccountReq: AddOrUpdateAccountReq = {
      id: rowId.value,
      ...values,
    };
    const result = await addOrUpdateAccount(updateAccountReq);
    if (result.code === 'F0002') {
      closeModal();
      emitSuccess('success');
    }
  } else {
    const addAccountReq: AddOrUpdateAccountReq = {
      accountNumber: values.accountNumber,
      accountName: values.accountName,
      initialAmount: values.initialAmount,
      currentAmount: values.currentAmount,
      isDefault: values.isDefault,
      sort: values.sort,
      remark: values.remark,
    };

    const result = await addOrUpdateAccount(addAccountReq);
    if (result.code === 'F0000') {
      closeModal();
      emitSuccess('success');
    }
  }

  setModalProps({ confirmLoading: false });

};
</script>