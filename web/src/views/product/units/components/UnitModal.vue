<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm"/>
  </BasicModal>
</template>
<script lang="ts">

import {defineComponent, ref, computed, unref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {formSchema} from "@/views/product/units/units.data";
import {AddOrUpdateProductUnitReq} from "@/api/product/model/productUnitModel";
import {addOrUpdateUnit} from "@/api/product/productUnit";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'UnitModal',
  components: {BasicModal, BasicForm},
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const { t } = useI18n();
    const rowId = ref('');
    const isUpdate = ref(true);
    const getTitle = computed(() => (!unref(isUpdate) ? t('product.unit.addProductUnit') : t('product.unit.editProductUnit')));
    const { createMessage } = useMessage();
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
      setModalProps({
        confirmLoading: false,
        // 解决重新打开modal数据不渲染
        destroyOnClose: true,
        width: 600,
      });
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

      if (!values.ratio) {
        setModalProps({confirmLoading: false,});
        createMessage.warn(t('product.unit.noticeOne'));
        return;
      }

      const productUnit: AddOrUpdateProductUnitReq = {
        id : values.id == null ? null : values.id,
        basicUnit: values.basicUnit,
        otherUnit: values.otherUnit,
        otherUnitTwo: values.otherUnitTwo,
        otherUnitThree: values.otherUnitThree,
        ratio: values.ratio,
        ratioTwo: values.ratioTwo,
        ratioThree: values.ratioThree,
        status: values.status,
      }
      const result = await addOrUpdateUnit(productUnit)
      if (result.code === 'P0006' || result.code === 'P0007') {
        closeModal();
        emit('success');
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