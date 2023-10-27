<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm"/>
  </BasicModal>
</template>

<script lang="ts">

import {defineComponent, ref, computed, unref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {CategorySchema} from "@/views/product/category/category.data";
import {AddOrUpdateProductCategoryReq} from "@/api/product/model/productCategoryModel";
import {addOrUpdateCategory} from "@/api/product/productCategory";

export default defineComponent({
  name: 'CategoryModal',
  components: {BasicModal, BasicForm},
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const rowId = ref('');
    const isUpdate = ref(true);
    const getTitle = computed(() => (!unref(isUpdate) ? '新增产品分类' : '编辑产品分类'));

    const [registerForm, {setFieldsValue, resetFields, validate}] = useForm({
      labelWidth: 100,
      baseColProps: {span: 24},
      schemas: CategorySchema,
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
      const productCategoryObject: AddOrUpdateProductCategoryReq = {
        id: values.id !== null ? values.id : null,
        parentId: values.parentId !== null ? values.parentId : null,
        categoryName: values.categoryName,
        categoryNumber: values.categoryNumber,
        remark: values.remark,
        sort: values.sort,
      }
      const result = await addOrUpdateCategory(productCategoryObject)
      if (result.code === 'P0000' || result.code === 'P0001') {
        closeModal();
        emit('success');
      }
      setModalProps({confirmLoading: false});
    }

    return {registerModal, registerForm, getTitle, handleSubmit};
  },
})
</script>

<style lang="less">

</style>