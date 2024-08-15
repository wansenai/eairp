<template>
  <div ref="container">
    <a-modal
        :title="title"
        :width="500"
        :confirm-loading="confirmLoading"
        :maskStyle="{'top':'50px','left':'70px'}"
        :maskClosable="false"
        v-model:open="open"
        :okText="t('product.info.import')"
        @ok="uploadFile"
        style="top:20%;height: 55%;">
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
              <a-upload name="file" :file-list="fileList" :before-upload="beforeUpload" @remove="handleRemove" :multiple="false" :max-count="1">
                <a-button type="primary">
                  <cloud-upload-outlined />
                  {{ t('product.info.selectFile') }}
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
import {Modal, Upload, Button, Spin, Row, Col, FormItem, UploadFile, UploadProps} from "ant-design-vue";
import { CloudUploadOutlined  } from '@ant-design/icons-vue';
import type { UploadChangeParam } from 'ant-design-vue';
import {useMessage} from "@/hooks/web/useMessage";
import {productCoverUpload, UploadCoverProductParams, UploadFileParams, uploadXlsx} from "@/api/basic/common";
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
    const fileList = ref<UploadProps['fileList']>([]);
    const uploading = ref<boolean>(false);

    const handleRemove: UploadProps['onRemove'] = file => {
      const index = fileList.value.indexOf(file);
      const newFileList = fileList.value.slice();
      newFileList.splice(index, 1);
      fileList.value = newFileList;
    };

    const beforeUpload: UploadProps['beforeUpload'] = file => {
      // 清空fileList
      fileList.value = []
      fileList.value = [...(fileList.value || []), file];
      return false;
    };

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

    const productBarcodeExistModal = (message: string, info: UploadFile) => {
      // 解析并显示错误信息
      const messageList = JSON.parse(message);
      // 生成内容消息
      const contentMessage = messageList.map((item: { productCode: string; productName: string[]; }) =>
          h('div', [
            h('div', {}, `${t('product.info.table.productName')}:`),
            item.productName.map(name => h('div', { style: 'color:red;' }, name)), // 处理换行
            h('div', {}, `${t('product.info.table.barCode')}:`),
            h('p', { style: 'color:red;' }, item.productCode),
            h('p'),
          ])
      );

      // 添加覆盖提示
      contentMessage.push(
          h('strong', { style: 'color:black;' }, t('product.info.dataCover'))
      );

      // 显示确认对话框
      Modal.confirm({
        title: t('product.info.checkBarCodeExist'),
        icon: h(ExclamationCircleOutlined),
        content: contentMessage,
        okText: t('sys.modal.cover'),
        cancelText: t('sys.modal.cancel'),
        async onOk() {
          const fileObject: UploadCoverProductParams = {
            file: info,
            type: 1,
          };
          try {
            await productCoverUpload(fileObject);
          } catch (error) {
            createMessage.info(t('sys.api.refreshBrowser'));
            handleCancel();
          } finally {
            handleCancel(); // 确保在异步操作完成后执行
          }
        },
        onCancel() {
          handleCancel();
        },
        // 添加滚动条样式
        bodyStyle: {
          maxHeight: 'calc(100vh - 200px)',
          overflowY: 'auto',
        },
      });
    };

    async function uploadFile() {
      const fileObject: UploadFileParams = {
        file: fileList.value[0]
      }
      const result = await uploadXlsx(fileObject);
      if (result.code === 'P0512') {
        close();
        productBarcodeExistModal(result.msg, fileList.value[0])
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
      handleRemove,
      beforeUpload,
      fileList,
      registerTable,
    };
  },
};
</script>