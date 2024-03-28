<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm"/>
  </BasicModal>
</template>

<script lang="ts">
import {defineComponent, ref, computed, unref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {formSchema} from '@/views/basic/customer/customer.data';
import { AddOrUpdateCustomerReq } from '@/api/basic/model/customerModel';
import { addOrUpdateCustomer } from '@/api/basic/customer';
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'CustomerModal',
  components: {BasicModal, BasicForm},
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const rowId = ref('');
    const isUpdate = ref(true);
    const getTitle = computed(() => (!unref(isUpdate) ? t('basic.customer.addCustomer') : t('basic.customer.editCustomer')));
    const { t } = useI18n();
    const [registerForm, {setFieldsValue, resetFields, validate}] = useForm({
      labelWidth: 100,
      baseColProps: {span: 24},
      schemas: formSchema,
      showActionButtonGroup: false,
      actionColOptions: {
        span: 23,
      },
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      resetFields();
      setModalProps({confirmLoading: false, destroyOnClose: true, width:900});
      isUpdate.value = !!data?.isUpdate;

      if (unref(isUpdate)) {
        rowId.value = data.record.id;
        setFieldsValue({
          ...data.record,
        });
      }
    });

    async function handleSubmit() {
      const values = await validate();
      setModalProps({confirmLoading: true});

      if (unref(isUpdate)) {
        const updateCustomerObject: AddOrUpdateCustomerReq = {
          id: rowId.value,
          ...values,
        }
        const result = await addOrUpdateCustomer(updateCustomerObject)
        if (result.code === 'U0002') {
          closeModal();
          emit('success');
        }
      } else {
        const addCustomerObject: AddOrUpdateCustomerReq = {
          customerName: values.customerName,
          contact: values.contact,
          phoneNumber: values.phoneNumber,
          address: values.address,
          email: values.email,
          status: values.status,
          firstQuarterAccountReceivable: values.firstQuarterAccountReceivable,
          secondQuarterAccountReceivable: values.secondQuarterAccountReceivable,
          thirdQuarterAccountReceivable: values.thirdQuarterAccountReceivable,
          fourthQuarterAccountReceivable: values.fourthQuarterAccountReceivable,
          fax: values.fax,
          taxNumber: values.taxNumber,
          bankName: values.bankName,
          accountNumber: values.accountNumber,
          taxRate: values.taxRate,
          sort: values.sort,
          remark: values.remark,
        }

        const result = await addOrUpdateCustomer(addCustomerObject)
        if (result.code === 'U0001') {
          closeModal();
          emit('success');
        }
      }

      setModalProps({confirmLoading: false});
    }

    return {
      registerModal,
      registerForm,
      handleSubmit,
      getTitle,
    }
  },

})
</script>