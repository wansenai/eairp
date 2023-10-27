<template>
    <a-modal
        :title="title"
        :width="500"
        v-model:open="openPriceModal"
        :confirm-loading="confirmLoading"
        :destroyOnClose="true"
        @ok="handleOk"
        @cancel="handleCancel"
        style="top:30%;height: 30%;">
      <a-spin :spinning="confirmLoading">
        <a-form ref="formRef" :rules="rules" :label-col="labelCol" :wrapper-col="wrapperCol">
          <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="请输入价格">
            <a-input-number placeholder="请输入价格" v-model:value="batchPrice" />
          </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
</template>

<script lang="ts">
import { ref, reactive } from 'vue';
import {Button, Form, FormItem, InputNumber, Modal, Spin} from "ant-design-vue";
import {Rule} from 'ant-design-vue/es/form';

export default {
  name: 'BatchSetPriceModal',
  components: {
    'a-modal': Modal,
    'a-button': Button,
    'a-spin': Spin,
    'a-form': Form,
    'a-form-item': FormItem,
    'a-input-number': InputNumber
  },
  setup(_, context) {
    const title = ref('批量设置');
    const openPriceModal = ref(false);
    const isReadOnly = ref(false);
    const batchPrice = ref('');
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

    const rules: Record<string, Rule[]> = {
      batchPrice: [
        {required: true, message: '请输入价格', trigger: 'change'},
      ]
    }

    const add = (type) => {
      openPriceModal.value = true
      batchType.value = type;
      if (type === 'purchase') {
        title.value = '采购价-批量设置';
      } else if (type === 'retail') {
        title.value = '零售价-批量设置';
      } else if (type === 'sale') {
        title.value = '销售价-批量设置';
      } else if (type === 'low') {
        title.value = '最低售价-批量设置';
      }
    };

    const edit = (record) => {
      openPriceModal.value = true
    };

    const close = () => {
      openPriceModal.value = false
    };

    const handleOk = () => {
      const price = batchPrice.value
      context.emit('ok', price, batchType.value);
      batchPrice.value = null;
      close();
    };

    const handleCancel = () => {
      batchPrice.value = null;
      close();
    };

    return {
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
      openPriceModal,
      batchPrice,
      rules
    };
  }
};
</script>

<style scoped>

</style>