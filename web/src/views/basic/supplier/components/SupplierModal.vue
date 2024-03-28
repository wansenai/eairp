<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm"/>
  </BasicModal>
</template>

<script lang="ts">
import {defineComponent, ref, computed, unref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {formSchema} from '@/views/basic/supplier/supplier.data';
import { AddSupplierReq, UpdateSupplierReq } from '@/api/basic/model/supplierModel';
import { addSupplier, updateSupplier } from '@/api/basic/supplier';
import {useI18n} from "vue-i18n";

export default defineComponent({ 
  name: 'SupplierModal',
  components: {BasicModal, BasicForm},
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const rowId = ref('');
    const isUpdate = ref(true);
    const getTitle = computed(() => (!unref(isUpdate) ? t('basic.supplier.addSupplier') : t('basic.supplier.editSupplier')));
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
        const updateSupplierObject: UpdateSupplierReq = {
           id: rowId.value,
            ...values,
        }
        const result = await updateSupplier(updateSupplierObject)
        if (result.code === 'S0002') {
          closeModal();
          emit('success');
        }
      } else {
        const addSupplierObject: AddSupplierReq = {
          supplierName: values.supplierName,
          contact: values.contact,
          contactNumber: values.contactNumber,
          phoneNumber: values.phoneNumber,
          address: values.address,
          email: values.email,
          status: values.status,
          firstQuarterAccountPayment: values.firstQuarterAccountPayment,
          secondQuarterAccountPayment: values.secondQuarterAccountPayment,
          thirdQuarterAccountPayment: values.thirdQuarterAccountPayment,
          fourthQuarterAccountPayment: values.fourthQuarterAccountPayment,
          fax: values.fax,
          taxNumber: values.taxNumber,
          bankName: values.bankName,
          accountNumber: values.accountNumber,
          taxRate: values.taxRate,
          sort: values.sort,
          remark: values.remark,
        }

        const result = await addSupplier(addSupplierObject)
        if (result.code === 'S0001') {
          closeModal();
          emit('success');
        }
      }

      setModalProps({confirmLoading: false});
    }

    return {
      t,
      registerModal,
      registerForm,
      handleSubmit,
      getTitle,
    }
  },

})
</script>