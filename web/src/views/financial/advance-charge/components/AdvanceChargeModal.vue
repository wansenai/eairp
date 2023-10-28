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
      style="left: 5%; height: 75%;">
    <template #footer >
      <a-button @click="">取消</a-button>
      <a-button v-if="checkFlag && isCanCheck" :loading="confirmLoading" @click="">保存并审核</a-button>
      <a-button type="primary" :loading="confirmLoading" @click="">保存</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="formState" style="margin-top: 35px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="会员卡号" data-step="1"
                         data-title="付款会员" :rules="[{ required: true}]">
              <a-select placeholder="选择会员卡号" v-model:value="formState.memberId" showSearch  @change="">
                <a-select-option v-for="(item,index) in memberList" :key="index" :value="item.id">
                  {{ item.member }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据日期" :rules="[{ required: true}]">
              <a-date-picker show-time placeholder="选择时间" @change="dateChange" @ok="dateOk"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据编号" data-step="2"
                         data-title="单据编号">
              <a-input placeholder="请输入单据编号" v-model:value="formState.receiptNumber" :readOnly="true"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="财务人员" data-step="3"
                         data-title="财务人员">
              <a-select placeholder="请财务人员" v-model:value="formState.financialPersonnelId"
                        :dropdownMatchSelectWidth="false">
                <a-select-option v-for="(item,index) in payTypeList" :key="index" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
            <div class="table-operations">
              <a-row :gutter="24" style="float:left; margin-bottom: 10px; margin-left: 1px" data-step="4"
                     data-title="插入一行">
                <a-col :md="6" :sm="24">
                  <a-button type="primary" @click="addRow">插入一行</a-button>
                </a-col>
                <a-col :md="6" :sm="24" style="margin-left: 30px">
                  <a-button danger @click="deleteRows" type="primary" :disabled="!hasSelected"> 删除选中行</a-button>
                </a-col>
              </a-row>
            </div>
        </a-row>
          <a-table bordered
                   :columns="tableColumns"
                   :dataSource="tableData"
                   :row-selection="rowSelection"
                   :scroll="{y: 240 }">
            <template #bodyCell="{record, column }">
              <template v-if="column.key === 'accountId'">
                <a-select v-model="editableData[record.key][column.key]" style="width: 100%" placeholder="请选择账户名称">
                  <a-select-option value="option1">Option 1</a-select-option>
                  <a-select-option value="option2">Option 2</a-select-option>
                  <a-select-option value="option3">Option 3</a-select-option>
                </a-select>
              </template>
              <template v-else>
                <template v-if="column.key !== 'accountId'">
                  <a-input v-model:value="editableData[record.key][column.key]"
                           :placeholder="`请输入${getColumnTitle(column)}`" />
                </template>
                <template v-else>
                  {{ record[column.key] }}
                </template>
              </template>
            </template>
            <template #footer >总计: $1438,130,00</template>
          </a-table>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="24" :md="24" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="{xs: { span: 24 },sm: { span: 24 }}" label="">
              <a-textarea :rows="3" placeholder="请输入备注" v-model:value="formState.remark"
                          style="margin-top:8px;"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="合计金额">
              <a-input placeholder="请输入合计收款" v-model:value="formState.totalPrice" :readOnly="true"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="收款金额">
              <a-input placeholder="请输入收款金额" v-model:value="formState.changeAmount" :readOnly="true"/>
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
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
import {computed, defineComponent, reactive, ref} from 'vue';
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
import {formState, tableColumns} from '@/views/financial/advance-charge/advance.data'
import {cloneDeep} from "lodash-es";

export default defineComponent({
  name: 'AdvanceChargeModal',
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
    const width = ref('1250px');
    const moreStatus = ref<boolean>(true);
    const addDefaultRowNum = ref(1);
    const showScanButton = ref(true);
    const showScanPressEnter = ref(false);
    const prefixNo = ref('LSCK');
    const fileList = ref([]);
    const payTypeList = ref([]);
    const minWidth = ref(1100);
    const model = ref({});
    const tableData = ref([
    ])
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

    function openAdvanceChargeModal(id: string | undefined) {
      open.value = true
      console.info(id)
    }

    function handleOkModal() {

    }

    const editableData = reactive({});
    function addRow() {
      const newRow: { key: number, accountId: string, amount: number | undefined, remark: string } =
          {
            key: Date.now(),
            accountId: '',
            amount: undefined,
            remark: '',
          };
      editableData[newRow.key] = cloneDeep(newRow);
      tableData.value.push(newRow);
      edit(newRow.key);
    }

    const edit = (key) => {
      const rowData = tableData.value.find(item => item.key === key);
      if (rowData) {
        editableData[key] = cloneDeep(rowData);
      }
    };

    const rowSelection = ref({
      selectedRowKeys: [],
      onChange: (selectedRowKeys) => {
        rowSelection.value.selectedRowKeys = selectedRowKeys;
      }
    });

    function deleteRows() {
      const selectedKeys = rowSelection.value.selectedRowKeys;
      tableData.value = tableData.value.filter(row => !selectedKeys.includes(row.key));
      rowSelection.value.selectedRowKeys = [];
    }

    const dateChange = (value: Dayjs, dateString: string) => {
      console.log('Selected Time: ', value);
      console.log('Formatted Selected Time: ', dateString);
    };

    const dateOk = (value: Dayjs) => {
      console.log('onOk: ', value);
    };

    const getColumnTitle = (column) => {
      return column.title.replace(/<[^>]+>/g, '');
    };

    const hasSelected = computed(() =>  rowSelection.value.selectedRowKeys.length > 0);

    return {
      open,
      checkFlag,
      tableColumns,
      editableData,
      formState,
      isCanCheck,
      isTenant,
      scanStatus,
      confirmLoading,
      handleCancelModal,
      openAdvanceChargeModal,
      handleOkModal,
      tableData,
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
      dateChange,
      dateOk,
      memberList,
      accountList,
      showScanButton,
      showScanPressEnter,
      addRow,
      getColumnTitle,
      rowSelection,
      deleteRows,
      hasSelected
    };
  },
});

</script>

<style scoped>
.custom-selection {
  width: 80px;
}

.ant-table-footer {
  text-align: center;
  font-size: 80px;
  font-weight: bold;
  color: #000;
}
</style>