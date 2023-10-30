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
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="第一步：">
              <a target="_blank" :href="templateUrl"><b>{{templateName}}</b></a>
              <p>提示：模板中的第一行请勿删除</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="24" :sm="24">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="第二步：">
            <a-upload name="file" :showUploadList="false" :multiple="false" :customRequest="uploadFile">
              <a-button type="primary">
                <cloud-upload-outlined />
                导入
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
import {ref} from 'vue';
import {Modal, Upload, Button, Spin, Row, Col, FormItem} from "ant-design-vue";
import { CloudUploadOutlined  } from '@ant-design/icons-vue';
import type { UploadChangeParam } from 'ant-design-vue';
import {useMessage} from "@/hooks/web/useMessage";
import {UploadFileParams, uploadXlsx} from "@/api/basic/common";
import {useTable} from "@/components/Table";
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

    async function uploadFile(info: UploadChangeParam) {
      const fileObject: UploadFileParams = {
        file: info.file,
      }
      await uploadXlsx(fileObject);
      handleCancel()
      await reload();
    }

    return {
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
