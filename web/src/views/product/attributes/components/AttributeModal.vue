<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm"/>
  </BasicModal>
</template>
<script lang="ts">

import {defineComponent, ref, computed, unref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {formSchema} from "@/views/product/attributes/attributes.data";
import {AddOrUpdateProductAttributeReq} from "@/api/product/model/productAttributeModel";
import {addOrUpdateAttribute} from "@/api/product/productAttribute";

export default defineComponent({
  name: 'AttributeModal',
  components: {BasicModal, BasicForm},
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const rowId = ref('');
    const isUpdate = ref(true);
    const getTitle = computed(() => (!unref(isUpdate) ? '新增产品属性' : '编辑产品属性'));

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
      setModalProps({confirmLoading: false, destroyOnClose: true});
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
      const productAttributeObject: AddOrUpdateProductAttributeReq = {
        id : values.id == null ? null : values.id,
        attributeName: values.attributeName,
        attributeValue: values.attributeValue,
        remark: values.remark,
        sort: values.sort,
      }
      const result = await addOrUpdateAttribute(productAttributeObject)
      if (result.code === 'P0003' || result.code === 'P0004') {
        closeModal();
        emit('success');
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