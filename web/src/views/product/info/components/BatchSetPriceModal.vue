<!--
  - Copyright (C) 2023-2033 WanSen AI Team
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

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