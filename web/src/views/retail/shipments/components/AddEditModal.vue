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
      :width="width"
      :confirm-loading="confirmLoading"
      v-bind:prefixNo="prefixNo"
      :id="prefixNo"
      :keyboard="false"
      :forceRender="true"
      switchHelp
      switchFullscreen
      v-model:open="open"
      @cancel="handleCancelModal"
      @ok="handleOkModal"
      style="left: 5%; height: 95%;">
    <template #footer>
        <a-button @click="">取消</a-button>
        <a-button v-if="checkFlag && isCanCheck" :loading="confirmLoading" @click="">保存并审核</a-button>
        <a-button type="primary" :loading="confirmLoading" @click="">保存</a-button>
        <!--发起多级审核-->
        <a-button v-if="!checkFlag" @click="" type="primary">提交流程</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="formState" style="margin-top: 35px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="会员卡号" data-step="1"
                         data-title="会员卡号"
                         data-intro="如果发现需要选择的会员卡号尚未录入，可以在下拉框中点击新增会员信息进行录入">
              <a-select placeholder="选择会员卡号" v-model:value="formState.memberId"
                        :dropdownMatchSelectWidth="false" showSearch optionFilterProp="children" @change="">
                <div slot="dropdownRender" slot-scope="menu">
                  <v-nodes :vnodes="menu"/>
                  <a-divider style="margin: 4px 0;"/>
                  <div v-if="isTenant" style="padding: 4px 8px; cursor: pointer;"
                       @mousedown="e => e.preventDefault()" @click="">
                    <a-icon type="plus"/>
                    新增会员
                  </div>
                </div>
                <a-select-option v-for="(item,index) in memberList" :key="index" :value="item.id">
                  {{ item.member }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据日期">
              <a-date-picker show-time placeholder="选择时间" @change="dateChange" @ok="dateOk"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据编号" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号">
              <a-input placeholder="请输入单据编号" v-model:value="formState.receiptNumber" :readOnly="true"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="收款类型" data-step="3"
                         data-title="收款类型"
                         data-intro="收款类型可以有现付和预付款两种类型，当选择了会员之后，如果该会员有预付款，在此处会显示具体预付款的金额，而且系统会优先默认选中预付款">
              <a-select placeholder="请选择付款类型" v-model:value="formState.paymentType"
                        :dropdownMatchSelectWidth="false">
                <a-select-option v-for="(item,index) in payTypeList" :key="index" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="18" :md="12" :sm="24" style="margin-bottom: 150px;">
            <div class="table-operations">
              <a-row :gutter="24" style="float:left; margin-bottom: 10px; margin-right: 1px" data-step="4" data-title="扫码录入"
                     data-intro="此功能支持扫码枪扫描商品条码进行录入">
                <a-col :md="6" :sm="24">
                  <a-button type="primary">插入一行</a-button>
                </a-col>
              </a-row>
              <a-row :gutter="24" style="float:left; margin-bottom: 10px" v-if="showScanButton" data-step="4" data-title="扫码录入"
                     data-intro="此功能支持扫码枪扫描商品条码进行录入">
                <a-col :md="6" :sm="24">
                  <a-button @click="scanEnter">扫码录入</a-button>
                </a-col>
              </a-row>
              <a-row :gutter="24" style="float:left; margin-bottom: 10px" v-if="showScanPressEnter" data-step="4" data-title="扫码录入"
                     data-intro="此功能支持扫码枪扫描商品条码进行录入">
                <a-col :md="5" :sm="24" style="padding: 0 6px 0 12px">
                  <a-input placeholder="请扫条码并回车" style="width: 150px;" v-model:value="formState.scanBarCode"
                           @pressEnter="scanPressEnter" ref="scanBarCode"/>
                </a-col>
                <a-col :md="6" :sm="24" style="padding: 0px 600px 0 0">
                  <a-button @click="stopScan">收起扫码</a-button>
                </a-col>
              </a-row>
            </div>
            <a-table bordered
                     :columns="tableColumns"
                     :dataSource="tableData"
                     :row-selection="rowSelection"
                     :scroll="{ x: '100%', y: 300 }">
              <a-icon type="down" @click="handleBatchSetWarehouse"/>
              <a-divider v-if="isTenant" style="margin: 4px 0;"/>
              <div v-if="isTenant" style="padding: 4px 8px; cursor: pointer;" @click="addWarehouse">
                <a-icon type="plus"/>
                新增仓库
              </div>
            </a-table>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="24" :md="24" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="{xs: { span: 24 },sm: { span: 24 }}" label="">
                  <a-textarea :rows="1" placeholder="请输入备注" v-model:value="formState.remark"
                              style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="附件" data-step="9"
                             data-title="附件"
                             data-intro="可以上传与单据相关的图片、文档，支持多个文件">
                  <a-upload v-model:file-list="fileList" action="">
                    <a-button>
                      <upload-outlined/>
                      点击上传附件
                    </a-button>
                  </a-upload>
                </a-form-item>
              </a-col>
            </a-row>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <div class="sign" style="margin-top: 40px">
              <a-row class="form-row" :gutter="24">
                <a-col :lg="24" :md="6" :sm="6">
                  <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" data-step="5" data-title="单据金额"
                               data-intro="单据金额等于左侧商品的总金额">
                    <template #label>
                      <span style="font-size: 20px;line-height:20px">单据金额</span>
                    </template>
                    <a-input v-model:value="formState.receiptAmount" :style="{color:'purple', height:'35px'}" :readOnly="true"/>
                  </a-form-item>
                </a-col>
                <a-col :lg="24" :md="6" :sm="6">
                  <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" data-step="6" data-title="收款金额"
                               data-intro="收款金额为收银员收取用户的实际金额">
                    <template #label>
                      <span style="font-size: 20px;line-height:20px">收款金额</span>
                    </template>
                    <a-input v-model:value="formState.paymentAmount" :style="{color:'red', height:'35px'}" defaultValue="0"
                             @change="onChangePaymentAmount"/>
                  </a-form-item>
                </a-col>
                <a-col :lg="24" :md="6" :sm="6">
                  <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" data-step="7" data-title="找零"
                               data-intro="找零等于收款金额减去实收金额">
                    <template #label>
                      <span style="font-size: 20px;line-height:20px">找零</span>
                    </template>
                    <a-input v-model:value="formState.backAmount" :style="{color:'green', height:'35px'}" :readOnly="true"
                             defaultValue="0"/>
                  </a-form-item>
                </a-col>
                <a-col :lg="24" :md="6" :sm="6">
                  <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" data-step="8" data-title="收款账户"
                               data-intro="收款账户的信息来自基本资料菜单下的【结算账户】">
                    <template #label>
                      <span style="font-size: 20px;line-height:20px">收款账户</span>
                    </template>
                    <a-select placeholder="选择收款账户" style="font-size:20px;"
                              v-model:value="formState.accountReceivable" :dropdownMatchSelectWidth="false">
                      <div slot="dropdownRender" slot-scope="menu">
                        <v-nodes :vnodes="menu"/>
                        <a-divider style="margin: 4px 0;"/>
                        <div v-if="isTenant" style="padding: 4px 8px; cursor: pointer;"
                             @mousedown="e => e.preventDefault()" @click="addAccount">
                          <a-icon type="plus"/>
                          新增结算账户
                        </div>
                      </div>
                      <a-select-option v-for="(item,index) in accountList" :key="index" :value="item.id">
                        {{ item.name }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
              </a-row>
            </div>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import {UploadOutlined} from '@ant-design/icons-vue';
import {Dayjs} from 'dayjs';
import {
  Textarea,
  DatePicker,
  Button,
  Checkbox,
  Col,
  Form,
  FormItem,
  Input,
  InputNumber,
  Modal,
  Row,
  Select,
  SelectOption,
  Spin,
  Table,
  TabPane,
  Tabs,
  Tooltip,
  TreeSelect,
  Upload,
} from "ant-design-vue";
import type { TableProps } from 'ant-design-vue';
import {formState, tableColumns} from '/@/views/retail/shipments/model/addEditModel'

export default defineComponent({
  name: 'AddEditModal',
  emits: ['success', 'cancel', 'error'],
  components: {
    'a-modal': Modal,
    'a-upload': Upload,
    'a-button': Button,
    'a-spin': Spin,
    'a-row': Row,
    'a-col': Col,
    'a-form': Form,
    'a-form-item': FormItem,
    'a-input': Input,
    'a-input-number': InputNumber,
    'a-select': Select,
    'a-select-option': SelectOption,
    'a-tree-select': TreeSelect,
    'a-checkbox': Checkbox,
    'a-tooltip': Tooltip,
    'a-tabs': Tabs,
    'a-tab-pane': TabPane,
    'a-table': Table,
    'a-textarea': Textarea,
    'a-date-picker': DatePicker,
    'upload-outlined': UploadOutlined,
  },
  setup(_, context) {
    const confirmLoading = ref<boolean>(false);
    const open = ref<boolean>(false);
    const checkFlag = ref<boolean>(true);
    const isCanCheck = ref<boolean>(true);
    const isTenant = ref<boolean>(true);
    const scanStatus = ref<boolean>(true);
    const title = ref<string>("操作");
    const width = ref('1500px');
    const moreStatus = ref<boolean>(true);
    const addDefaultRowNum = ref(1);
    const showScanButton = ref(true);
    const showScanPressEnter = ref(false);
    const prefixNo = ref('LSCK');
    const fileList = ref([]);
    const payTypeList = ref([]);
    const minWidth = ref(1100);
    const model = ref({});
    const tableData = ref([])
    const labelCol = ref({
      xs: {span: 24},
      sm: {span: 8},
    });
    const wrapperCol = ref({
      xs: {span: 24},
      sm: {span: 16},
    });
    const refKey = ref(['productDataTable']);
    const activeKey = ref('productDataTable');
    const memberList = ref([])
    const accountList = ref([]);

    function handleCancelModal() {
      close();
      open.value = false;
      context.emit('cancel');
    }

    function openAddEditModal(id: string | undefined) {
      open.value = true
      console.info(id)
    }

    function handleOkModal() {

    }

    function onChangePaymentAmount() {

    }

    const dateChange = (value: Dayjs, dateString: string) => {
      console.log('Selected Time: ', value);
      console.log('Formatted Selected Time: ', dateString);
    };

    const dateOk = (value: Dayjs) => {
      console.log('onOk: ', value);
    };

    function scanPressEnter() {

    }

    function scanEnter() {
      showScanButton.value = !showScanButton.value;
      showScanPressEnter.value = !showScanPressEnter.value;
    }

    function stopScan() {
      showScanButton.value = !showScanButton.value;
      showScanPressEnter.value = !showScanPressEnter.value;
    }

    function addAccount() {

    }

    function addWarehouse() {

    }

    function handleBatchSetWarehouse() {

    }

    const rowSelection: TableProps['rowSelection'] = {

    };

    return {
      open,
      checkFlag,
      isCanCheck,
      isTenant,
      scanStatus,
      confirmLoading,
      handleCancelModal,
      openAddEditModal,
      handleOkModal,
      tableColumns,
      tableData,
      formState,
      title,
      width,
      moreStatus,
      addDefaultRowNum,
      prefixNo,
      fileList,
      payTypeList,
      minWidth,
      model,
      labelCol,
      wrapperCol,
      refKey,
      activeKey,
      onChangePaymentAmount,
      dateChange,
      dateOk,
      scanPressEnter,
      scanEnter,
      stopScan,
      addAccount,
      memberList,
      accountList,
      addWarehouse,
      handleBatchSetWarehouse,
      showScanButton,
      showScanPressEnter,
      rowSelection
    };
  },
});

</script>

<style scoped>
.sign .ant-input {
  font-size: 30px;
  font-weight: bolder;
  text-align: center;
  border-left-width: 0 !important;
  border-top-width: 0 !important;
  border-right-width: 0 !important;
}

.template-footer {
  margin-bottom: 8px;
}
</style>