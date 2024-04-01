<template>
  <div ref="container">
    <a-modal
        :title="title"
        :width="500"
        :confirm-loading="confirmLoading"
        :maskStyle="{'top':'50px','left':'154px'}"
        :maskClosable="false"
        v-model:open="open"
        @cancel="handleCancel"
        style="top:20%;height: 55%;">
      <template slot="footer">
        <a-button key="back" @click="handleCancel">取消</a-button>
      </template>
      <a-spin :spinning="confirmLoading">
        <a-row class="form-row" :gutter="24">
          <a-col :md="24" :sm="24">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" :label="t('product.info.importInfo.setup1')">
              <a target="_blank" :href="templateUrl"><b>{{templateName}}</b></a>
              <p>{{ t('product.info.importInfo.tip') }} </p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="24" :sm="24">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" :label="t('product.info.importInfo.setup2')">
            <a-upload name="file" :showUploadList="false" :multiple="false" :customRequest="uploadFile">
              <a-button type="primary">
                <cloud-upload-outlined />
                {{ t('product.info.import') }}
              </a-button>
            </a-upload>
          </a-form-item>
          </a-col>
        </a-row>
      </a-spin>
      </a-modal>
    </div>
</template>
<script lang="ts">
import {ref, h} from 'vue';
import {Modal, Upload, Button, Spin, Row, Col, FormItem} from "ant-design-vue";
import { CloudUploadOutlined  } from '@ant-design/icons-vue';
import type { UploadChangeParam } from 'ant-design-vue';
import {useMessage} from "@/hooks/web/useMessage";
import {UploadFileParams, uploadXlsx} from "@/api/basic/common";
import {useTable} from "@/components/Table";
import {useI18n} from "vue-i18n";
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
export default {
  name: 'ImportFileModal',
  emits: ['success', 'cancel'],
  components: {
    'a-modal': Modal,
    'a-upload': Upload,
    'a-a-button': Button,
    'a-spin': Spin,
    'a-row': Row,
    'a-col': Col,
    'a-form-item': FormItem,
    CloudUploadOutlined
  },
  setup(_, context) {
    const { t } = useI18n();
    const { createMessage } = useMessage();
    const title = ref('');
    const open = ref(false);
    const labelCol = ref({
      xs: { span: 24 },
      sm: { span: 5 },
    });
    const wrapperCol = ref({
      xs: { span: 24 },
      sm: { span: 18 },
    });

    const confirmLoading = ref(false);
    const disableMixinCreated = ref(true);
    const templateUrl = ref('');
    const templateName = ref('');
    const downloadUrl = ref('');
    const [registerTable, { reload }] = useTable()

    function initModal(path:string, name:string) {
      templateUrl.value = path;
      templateName.value = name;
      open.value = true
    }

    function close() {
      open.value = false
    }

    function handleCancel() {
      close();
      context.emit('cancel');
    }

    const productBarcodeExistModal = (message: string) => {
      Modal.confirm({
        title: t('product.info.checkBarCodeExist'),
        icon: h(ExclamationCircleOutlined),
        content: h('div', { style: 'color:red;' }, message),
        okText() {
          return t('sys.modal.cover');
        },
        cancelText() {
          return t('sys.modal.cancel');
        },
        onOk() {
          console.info('OK');
        },
        onCancel() {
          console.info('Cancel');
        },
      });
    };

    async function uploadFile(info: UploadChangeParam) {
      const fileObject: UploadFileParams = {
        file: info.file,
      }
      const result = await uploadXlsx(fileObject);
      if (result.code === 'P0512') {
        close();
        productBarcodeExistModal(result.msg)
      } else {
        handleCancel()
        await reload();
      }
    }

    return {
      t,
      title,
      labelCol,
      wrapperCol,
      confirmLoading,
      disableMixinCreated,
      templateUrl,
      templateName,
      downloadUrl,
      close,
      handleCancel,
      initModal,
      open,
      CloudUploadOutlined,
      uploadFile,
      registerTable,
    };
  },
};
</script>
