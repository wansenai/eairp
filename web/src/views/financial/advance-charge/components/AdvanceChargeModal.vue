<template>
  <BasicModal
      :title="title"
      :width="width"
      :confirm-loading="confirmLoading"
      v-bind:prefixNo="prefixNo"
      :id="prefixNo"
      :keyboard="true"
      :forceRender="true"
      switchHelp
      switchFullscreen
      :height="600"
      :maxHeight="660"
      v-model:open="open"
      @cancel="handleCancelModal"
      style="top: 30px; left: 20px">
    <template #footer >
      <a-button @click="handleCancelModal" v-text="t('financial.advance.form.cancel')"/>
      <a-button :loading="confirmLoading" @click="handleOk(1)" v-text="t('financial.advance.form.saveApprove')"/>
      <a-button type="primary" :loading="confirmLoading" @click="handleOk(0)"  v-text="t('financial.advance.form.save')"/>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="formState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-input v-model:value="formState.id" v-show="false"/>
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.advance.form.paymentMember')" data-step="1"
                         data-title="付款会员" :rules="[{ required: true}]">
              <a-select v-model:value="formState.memberId" :placeholder="t('financial.advance.form.inputCustomer')">
                <a-select-option v-for="(item,index) in memberList" :key="index" :value="item.id">
                  {{ item.memberName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.advance.form.receiptDate')" :rules="[{ required: true}]">
              <a-date-picker v-model:value="formState.receiptDate" show-time :placeholder="t('financial.advance.form.inputReceiptDate')" @change="dateChange"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.advance.form.receiptNumber')" data-step="2"
                         data-title="单据编号">
              <a-input :placeholder="t('financial.advance.form.inputReceiptNumber')" v-model:value="formState.receiptNumber" readOnly/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.advance.form.financialPerson')" data-step="3"
                         data-title="财务人员">
              <a-select :placeholder="t('financial.advance.form.inputFinancialPerson')" v-model:value="formState.financialPersonnelId"
                        :dropdownMatchSelectWidth="false">
                <a-select-option v-for="(item,index) in operatorList" :key="index" :value="item.id">
                  {{ item.name }}
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
                  <a-button type="primary" @click="addRow" v-text="t('financial.advance.form.insertRow')" />
                </a-col>
                <a-col :md="6" :sm="24" style="margin-left: 30px">
                  <a-button danger @click="deleteRows" type="primary" :disabled="!hasSelected" v-text="t('financial.advance.form.deleteRow')" />
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
                <a-select
                    v-model:value="editableData[record.key][column.key]"
                    style="width: 100%"
                    placeholder="请选择账户名称"
                    @change="handleAccountChange(record.key, column.key, $event)" >
                  <a-select-option v-for="(item,index) in accountList" :key="index" :value="item.id">
                    {{ item.accountName }}
                  </a-select-option>
                </a-select>
              </template>
              <template v-else>
                <template v-if="column.key !== 'accountId'">
                  <a-input v-model:value="editableData[record.key][column.key]"
                           :placeholder="`请输入${getColumnTitle(column)}`" @change="valueChange" :rules="[{ required: true}]"/>
                </template>
                <template v-else>
                  {{ record[column.key] }}
                </template>
              </template>
            </template>
            <template #footer >{{ t('financial.advance.form.total') }}: {{totalPrice}}</template>
          </a-table>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="24" :md="24" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="{xs: { span: 24 },sm: { span: 24 }}" label="">
              <a-textarea :rows="3" :placeholder="t('financial.advance.form.inputRemark')" v-model:value="formState.remark"
                          style="margin-top:8px;"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" :label="t('financial.advance.form.totalAmount')">
              <a-input placeholder="请输入合计收款" v-model:value="formState.totalAmount" :readOnly="true"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" :label="t('financial.advance.form.amountCollected')">
              <a-input placeholder="请输入收款金额" v-model:value="formState.collectedAmount" :readOnly="true"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.advance.form.annex')" data-step="9"
                         data-title="附件"
                         data-intro="可以上传与单据相关的图片、文档，支持多个文件">
              <a-upload
                  v-model:file-list="fileList"
                  :custom-request="uploadFiles"
                  :before-upload="beforeUpload"
                  multiple>
                <a-button>
                  <upload-outlined/>
                  {{ t('financial.advance.form.uploadAnnex') }}
                </a-button>
              </a-upload>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </BasicModal>
</template>

<script lang="ts">
import {computed, defineComponent, reactive, ref} from 'vue';
import {UploadOutlined} from '@ant-design/icons-vue';
import dayjs from 'dayjs';
import {
  Button,
  Checkbox,
  Col,
  DatePicker,
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
  Textarea,
  Tooltip,
  TreeSelect,
  Upload,
} from "ant-design-vue";
import {formState, tableColumns} from '@/views/financial/advance-charge/advance.data'
import {cloneDeep} from "lodash-es";
import {getMemberList} from "@/api/basic/member"
import {generateId, uploadOss} from "@/api/basic/common"
import {getOperatorList} from "@/api/basic/operator";
import {getAccountList} from "@/api/financial/account";
import {MemberResp} from "@/api/basic/model/memberModel";
import {OperatorResp} from "@/api/basic/model/operatorModel";
import {AccountResp} from "@/api/financial/model/accountModel";
import {useMessage} from "@/hooks/web/useMessage";
import {addOrUpdateAdvance, getAdvanceDetail} from "@/api/financial/advance";
import {AddOrUpdateAdvanceReq, AdvanceChargeData} from "@/api/financial/model/advanceModel";
import weekday from "dayjs/plugin/weekday";
import localeData from "dayjs/plugin/localeData";
import {useI18n} from "vue-i18n";
import BasicModal from "@/components/Modal/src/BasicModal.vue";
dayjs.extend(weekday);
dayjs.extend(localeData);
dayjs.locale('zh-cn');
export default defineComponent({
  name: 'AdvanceChargeModal',
  emits: ['success', 'cancel', 'error'],
  components: {
    BasicModal,
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
    const { t } = useI18n();
    const {createMessage} = useMessage();
    const formRef = ref();
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
    const memberList = ref<MemberResp[]>([])
    const operatorList = ref<OperatorResp[]>([]);
    const accountList = ref<AccountResp[]>([]);
    const totalPrice = ref<number>(0.00);

    function handleCancelModal() {
      open.value = false;
      context.emit('cancel');
      clearData();
    }

    function openAdvanceChargeModal(id: string | undefined) {
      open.value = true
      loadMemberList();
      loadOperatorList();
      loadAccountList();
      if (id) {
        title.value = t('financial.advance.editAdvanceReceipt')
        loadAdvanceChargeData(id)
        formState.id = id;
      } else {
        title.value = t('financial.advance.addAdvanceReceipt')
        loadGenerateId();
      }
    }

    function loadMemberList() {
      getMemberList().then(res => {
        memberList.value = res.data
      })
    }

    function loadOperatorList() {
      getOperatorList("财务员").then(res => {
        operatorList.value = res.data
      })
    }

    function loadAccountList() {
      getAccountList().then(res => {
        accountList.value = res.data
      })
    }

    function loadGenerateId() {
      generateId("收预付款").then(res => {
        formState.receiptNumber = res.data
      })
    }

    async function loadAdvanceChargeData(id: string) {
      const res = await getAdvanceDetail(id);
      // 赋值给formState的数据
      if (res.data) {
        const data = res.data
        const formattedDate = dayjs(data.receiptDate);
        formState.receiptNumber = data.receiptNumber;
        formState.receiptDate = formattedDate;
        formState.memberId = data.memberId;
        formState.financialPersonnelId = data.financialPersonnelId;
        formState.remark = data.remark;
        formState.totalAmount = data.totalAmount;
        formState.collectedAmount = data.collectedAmount;
        totalPrice.value = data.totalAmount;

        if(data.tableData) {
          tableData.value = [];
          for (const key in editableData) {
            delete editableData[key];
          }
          const tableDataArray = data.tableData;
          for (let i = 0; i < tableDataArray.length; i++) {
            const tableDataItem = tableDataArray[i];
            const tableDataItemObj = {
              key: i,
              accountId: tableDataItem.accountId,
              amount: tableDataItem.amount,
              remark: tableDataItem.remark,
            }
            tableData.value.push(tableDataItemObj);
            editableData[tableDataItemObj.key] = cloneDeep(tableDataItemObj);
            editableData[tableDataItemObj.key]['accountId'] = tableDataItem.accountId;
          }
        }
        if(data.files) {
          fileList.value = [];
          for (let i = 0; i < data.files.length; i++) {
            const file = data.files[i];
            const fileObj = {
              uid: file.uid,
              name: file.fileName,
              status: 'done',
              url: file.fileUrl,
              type: file.fileType,
              size: file.fileSize,
            }
            fileList.value.push(fileObj);
          }
        }
      }
    }

    async function handleOk(review: number) {
      if (!formState.memberId) {
        createMessage.warn(t('financial.advance.form.inputCustomer'));
        return;
      }
      if (!formState.receiptDate) {
        createMessage.warn(t('financial.advance.form.inputReceiptDate'));
        return;
      }
      if (tableData.value.length === 0) {
        createMessage.warn(t('financial.advance.form.noticeFour'));
        return;
      }
      console.info(editableData)
      for (const key in editableData) {
        console.info(editableData[key])
        if (!editableData[key].accountId) {
          createMessage.warn(t('financial.advance.form.noticeFive'));
          return;
        }
        if (!editableData[key].amount) {
          createMessage.warn(t('financial.advance.form.noticeSix'));
          return;
        }
      }


      const files = [];
      if (fileList && fileList.value) {
        for (let i = 0; i < fileList.value.length; i++) {
          if (fileList.value[i].url) {
            const file = {
              uid: fileList.value[i].uid,
              fileType: fileList.value[i].type,
              fileName: fileList.value[i].name,
              fileUrl: fileList.value[i].url || null,
              fileSize: fileList.value[i].size,
            }
            files.push(file)
          } else {
            const file = {
              uid: fileList.value[i].uid,
              fileType: fileList.value[i].type,
              fileName: fileList.value[i].name,
              fileUrl: fileList.value[i].response.data[0] as string,
              fileSize: fileList.value[i].size,
            }
            files.push(file)
          }
        }
      }
      const dataArray: AdvanceChargeData[] = Object.values(editableData).map((item: any) => ({
        accountId: item.accountId,
        accountName: '',
        amount: Number(item.amount),
        remark: item.remark
      }));

      const params: AddOrUpdateAdvanceReq = {
        ...formState,
        tableData: dataArray,
        files: files,
        review: review,
      }
      const result = await addOrUpdateAdvance(params);
      if (result.code === 'F0005') {
        handleCancelModal();
      }
    }

    const clearData = () => {
      formRef.value.resetFields();
      formState.id = undefined;
      formState.receiptNumber = '';
      formState.receiptDate = undefined;
      formState.memberId = '';
      formState.financialPersonnelId = '';
      formState.remark = '';
      formState.totalAmount = 0.00;
      formState.collectedAmount = 0.00;
      tableData.value = [];
      fileList.value = [];
      totalPrice.value = 0.00;
      for (const key in editableData) {
        delete editableData[key];
      }
    };

    const editableData = reactive({});
    function addRow() {
      const newRow: { key: number, accountId: string, amount: number | undefined, remark: string } =
          {
            key: Date.now(),
            accountId: '',
            amount: 0.00,
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
      // 获取row.key的数据进行对应editableData的删除
      selectedKeys.forEach((key) => {
        delete editableData[key];
      });

    }

    const dateChange = (dateString: string) => {
      formState.receiptDate = dateString;
    };

    const valueChange = () => {
      let total = 0.00;
      for (const key in editableData) {
        if (editableData[key].amount) {
          total += parseFloat(editableData[key].amount);
        }
      }
      totalPrice.value = parseFloat(total.toFixed(2));
      formState.collectedAmount = parseFloat(total.toFixed(2));
      formState.totalAmount = parseFloat(total.toFixed(2));
    };

    const handleAccountChange = (recordKey: string, columnKey: string, selectedAccountId: string) => {
      editableData[recordKey][columnKey] = selectedAccountId;
    }

    const getColumnTitle = (column) => {
      return column.title.replace(/<[^>]+>/g, '');
    };

    const hasSelected = computed(() =>  rowSelection.value.selectedRowKeys.length > 0);

    function beforeUpload(file: any) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        createMessage.warn(`${file.name}，` + t('financial.advance.form.noticeThree'));
        return isLt2M || Upload.LIST_IGNORE
      }
    }

    const uploadFiles = (options) => {
      const { file, onSuccess, onError, onProgress } = options;
      const formData = new FormData();
      formData.append('files', file);
      // 调用 uploadOss 方法进行上传
      uploadOss(formData, {
        onUploadProgress: ({total, loaded}) => {
          onProgress(
              {percent: Math.round((loaded / total) * 100).toFixed(2)},
              file
          );
        },
      }).then((res) => {
        onSuccess(res, file);
      }).catch((error) => {
        onError(error);
      });
    }

    return {
      t,
      open,
      formRef,
      checkFlag,
      tableColumns,
      editableData,
      formState,
      dateChange,
      isCanCheck,
      isTenant,
      scanStatus,
      confirmLoading,
      handleCancelModal,
      openAdvanceChargeModal,
      handleOk,
      tableData,
      title,
      width,
      moreStatus,
      addDefaultRowNum,
      prefixNo,
      fileList,
      operatorList,
      minWidth,
      model,
      labelCol,
      wrapperCol,
      refKey,
      activeKey,
      memberList,
      accountList,
      showScanButton,
      showScanPressEnter,
      addRow,
      getColumnTitle,
      rowSelection,
      deleteRows,
      hasSelected,
      valueChange,
      totalPrice,
      handleAccountChange,
      uploadFiles,
      beforeUpload
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