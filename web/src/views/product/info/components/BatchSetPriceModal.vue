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
          <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('product.info.form.basic.price')">
            <a-input-number :placeholder="t('product.info.form.basic.inputPrice')" v-model:value="batchPrice" />
          </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
</template>

<script lang="ts">
import { ref, reactive } from 'vue';
import {Button, Form, FormItem, InputNumber, Modal, Spin} from "ant-design-vue";
import {Rule} from 'ant-design-vue/es/form';
import {useI18n} from "vue-i18n";

export default {
  name: 'BatchSetPriceModal',
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
    const openPriceModal = ref(false);
    const isReadOnly = ref(false);
    const batchPrice = ref(0);
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
        {required: true, message: t('product.info.form.basic.inputPrice'), trigger: 'change'},
      ]
    }

    const add = (type) => {
      openPriceModal.value = true
      batchType.value = type;
      if (type === 'purchase') {
        title.value = t('product.info.form.basic.purchasePriceBatch')
      } else if (type === 'retail') {
        title.value = t('product.info.form.basic.retailPriceBatch')
      } else if (type === 'sale') {
        title.value = t('product.info.form.basic.salesPriceBatch')
      } else if (type === 'low') {
        title.value = t('product.info.form.basic.lowestSellPriceBatch')
      } else {
        title.value = t('product.info.form.basic.batchSet')
      }
    };

    const edit = () => {
      openPriceModal.value = true
    };

    const close = () => {
      openPriceModal.value = false
    };

    const handleOk = () => {
      const price = batchPrice.value
      context.emit('ok', price, batchType.value);
      batchPrice.value = 0;
      close();
    };

    const handleCancel = () => {
      batchPrice.value = 0;
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
      openPriceModal,
      batchPrice,
      rules
    };
  }
};
</script>

<style scoped>

</style>