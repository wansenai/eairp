<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm"/>
  </BasicModal>
</template>
<script lang="ts">

import {defineComponent, ref, unref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, FormSchema, useForm} from '/@/components/Form/index';
import {useMessage} from "@/hooks/web/useMessage";
import {multipleAccountForm} from "@/views/basic/settlement-account/financialAccount.data";
export default defineComponent({
  name: 'MultipleAccountsModal',
  components: {BasicModal, BasicForm},
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const rowId = ref('');
    const isUpdate = ref(true);
    const getTitle = ref('多账户结算');
    const { createMessage } = useMessage();
    const [registerForm, {setFieldsValue, resetFields, validate}] = useForm({
      labelWidth: 100,
      baseColProps: {span: 24},
      schemas: multipleAccountForm,
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
      if (values) {
        setModalProps({confirmLoading: false,});
        emit('handleAccountSuccess', values);
        // 关闭弹窗
        closeModal();
      }
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