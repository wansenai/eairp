<template>
  <a-modal
      :title="title"
      :width="500"
      v-model:open="openStockModal"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
      style="top:30%;height: 30%;">
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('product.info.form.inventoryQuantity.number')">
          <a-input-number :placeholder="t('product.info.form.inventoryQuantity.inputNumber')" v-model:value="batchNumber" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
import { ref, reactive} from 'vue';
import {Button, Form, FormItem, InputNumber, Modal, Spin} from "ant-design-vue";
import {useI18n} from "vue-i18n";

export default {
  name: 'BatchSetStockModal',
  emits: ['ok'],
  components: {
    'a-modal': Modal,
    'a-button': Button,
    'a-spin': Spin,
    'a-form': Form,
    'a-form-item': FormItem,
    'a-input-number': InputNumber
  },
  setup(_, context) {
    const { t } = useI18n();
    const title = ref('批量设置');
    const openStockModal = ref(false);
    const isReadOnly = ref(false);
    const batchNumber = ref(0);
    const batchType = ref('');
    const model = reactive({});
    const labelCol = {
      xs: { span: 24 },
      sm: { span: 5 },
    };
    const wrapperCol = {
      xs: { span: 24 },
      sm: { span: 16 },
    };
    const confirmLoading = ref(false);
    const formRef = ref();
    const add = (type) => {
      openStockModal.value = true
      batchType.value = type;
      if (type === 'initStock') {
        title.value = t('product.info.form.inventoryQuantity.batchInitialQuantity');
      } else if (type === 'lowSafeStock') {
        title.value = t('product.info.form.inventoryQuantity.batchMinSafetyQuantity');
      } else if (type === 'highSafeStock') {
        title.value = t('product.info.form.inventoryQuantity.batchMaxSafetyQuantity');
      }
    };

    const edit = (record) => {
      openStockModal.value = true
    };

    const close = () => {
      openStockModal.value = false
    };

    const handleOk = () => {
      const stockNumber = batchNumber.value
      context.emit('ok', stockNumber, batchType.value);
      batchNumber.value = 0;
      close();
    };

    const handleCancel = () => {
      batchNumber.value = 0;
      close();
    };

    return {
      t,
      title,
      isReadOnly,
      batchType,
      model,
      labelCol,
      wrapperCol,
      confirmLoading,
      formRef,
      add,
      edit,
      close,
      handleOk,
      handleCancel,
      openStockModal,
      batchNumber,
    };
  }
};
</script>

<style scoped>

</style>