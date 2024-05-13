<template>
  <a-modal
      :title="title"
      :width="width"
      :confirm-loading="confirmLoading"
      :forceRender="true"
      :keyboard="true"
      switchHelp
      switchFullscreen
      @cancel="handleCancelModal"
      v-model:open="open"
      style="left: 5%; height: 95%;">
    <template #footer>
      <a-button @click="handleCancelModal" v-text="t('financial.expense.form.cancel')"/>
      <a-button v-if="checkFlag" :loading="confirmLoading" @click="handleOk(1)" v-text="t('financial.expense.form.saveApprove')"/>
      <a-button type="primary" :loading="confirmLoading" @click="handleOk(0)" v-text="t('financial.expense.form.save')"/>
      <!--发起多级审核-->
      <a-button v-if="!checkFlag" @click="" type="primary">提交流程</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="expenseFormState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-input v-model:value="expenseFormState.id" v-show="false"/>
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.expense.form.correspondenceUnit')" data-step="1"
                         data-title="往来单位" :rules="[{ required: true}]">
              <a-select v-model:value="expenseFormState.relatedPersonId"
                        :dropdownMatchSelectWidth="false" showSearch optionFilterProp="children"
                        :placeholder="t('financial.expense.form.inputCorrespondenceUnit')"
                        :options="relatedPersonList.map(item => ({ value: item.id, label: item.name }))">
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.expense.form.receiptDate')" :rules="[{ required: true}]">
              <a-date-picker v-model:value="expenseFormState.receiptDate" show-time :placeholder="t('financial.expense.form.inputReceiptDate')" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.expense.form.receiptNumber')" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号">
              <a-input :placeholder="t('financial.expense.form.inputReceiptNumber')" v-model:value="expenseFormState.receiptNumber" :readOnly="true"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.expense.form.financialPerson')" data-step="3"
                         data-title="财务人员"
                         data-intro="">
              <a-select v-model:value="expenseFormState.financialPersonId"
                        :placeholder="t('financial.expense.form.inputFinancialPerson')"
                        :options="financialPersonalList.map(item => ({ value: item.id, label: item.name }))">
                <template #dropdownRender="{ menuNode: menu }">
                  <v-nodes :vnodes="menu"/>
                  <a-divider style="margin: 4px 0"/>
                  <div style="padding: 4px 8px; cursor: pointer;"
                       @mousedown="e => e.preventDefault()" @click="addOperator">
                    <plus-outlined/>
                    {{ t('financial.expense.form.addFinancialPerson') }}
                  </div>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="24" :md="12" :sm="24" style="margin-bottom: 150px;">
            <div class="table-operations">
              <vxe-grid ref='xGrid' v-bind="gridOptions">
                <template #toolbar_buttons="{ row }">
                  <a-button type="primary" @click="addRowData" style="margin-right: 10px" v-text="t('financial.expense.form.insertRow')" />
                  <a-button @click="deleteRowData" style="margin-right: 10px" v-text="t('financial.expense.form.deleteRow')" />
                </template>
                <template #id_default="{ row }">
                  <span>{{ formatIncomeExpenseId(row.incomeExpenseId) }}</span>
                </template>
                <template #id_edit="{ row }">
                  <vxe-select :placeholder="t('financial.expense.form.noticeOne')" v-model="row.incomeExpenseId">
                    <vxe-option v-for="item in incomeExpenseList" :key="item.id" :value="item.id" :label="item.name"></vxe-option>
                  </vxe-select>
                </template>
                <template #amount_edit="{ row }">
                  <vxe-input type="number" v-model="row.incomeExpenseAmount"></vxe-input>
                </template>
              </vxe-grid>
            </div>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="24" :md="24" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="{xs: { span: 24 },sm: { span: 24 }}" label="">
                  <a-textarea :rows="1" :placeholder="t('financial.expense.form.inputRemark')" v-model:value="expenseFormState.remark"
                              style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.expense.form.expenseAccount')" data-step="2"
                             data-title="支出账户" :rules="[{ required: true}]">
                  <a-select v-model:value="expenseFormState.expenseAccountId"
                            :placeholder="t('financial.expense.form.inputExpenseAccount')"
                            :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24" >
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.expense.form.expenseAmount')" data-step="2"
                             data-title="支出金额" :rules="[{ required: true}]">
                  <a-input-number :placeholder="t('financial.expense.form.inputExpenseAmount')" v-model:value="expenseFormState.expenseAmount" readonly/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.expense.form.annex')" data-step="9"
                             data-title="附件"
                             data-intro="可以上传与单据相关的图片、文档，支持多个文件">
                  <a-upload
                      v-model:file-list="fileList"
                      :custom-request="uploadFiles"
                      :before-upload="beforeUpload"
                      multiple>
                    <a-button>
                      <upload-outlined/>
                      {{ t('financial.expense.form.uploadAnnex') }}
                    </a-button>
                  </a-upload>
                </a-form-item>
              </a-col>
            </a-row>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
  <OperatorModal @register="operatorModal"/>
</template>

<script lang="ts">
import {defineComponent, ref, h} from 'vue';
import {AccountBookTwoTone, PlusOutlined, UploadOutlined} from '@ant-design/icons-vue';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
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
  Upload, Divider,
} from "ant-design-vue";
import {
  expenseFormState,
  RowVO,
  xGrid,
  tableData,
  gridOptions,
} from '/@/views/financial/expense/addEditExpense.data';
import {getOperatorList} from "@/api/basic/operator";
import {OperatorResp} from "@/api/basic/model/operatorModel";
import {useModal} from "@/components/Modal";
import {generateId, uploadOss} from "@/api/basic/common";
import FinancialAccountModal from "@/views/basic/settlement-account/components/FinancialAccountModal.vue";
import {VXETable, VxeGrid, VxeInput, VxeButton, VxeSelect, VxeOption} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdateExpense, getExpenseDetailById} from "@/api/financial/expense"
import {FileData} from '/@/api/retail/model/shipmentsModel';
import {getAccountList} from "@/api/financial/account";
import {getRelatedPerson} from "@/api/report/report";
import {getIncomeExpenseList} from "@/api/basic/incomeExpense";
import {AccountResp} from "@/api/financial/model/accountModel";
import XEUtils from "xe-utils";
import {AddOrUpdateExpenseReq} from "@/api/financial/model/expenseModel";
import OperatorModal from "@/views/basic/operator/components/OperatorModal.vue";
import weekday from "dayjs/plugin/weekday";
import localeData from "dayjs/plugin/localeData";
import {useI18n} from "vue-i18n";
import {useLocaleStore} from "@/store/modules/locale";
const VNodes = defineComponent({
  props: {
    vnodes: {
      type: Object,
      required: true,
    },
  },
  render() {
    return this.vnodes;
  },
});
dayjs.extend(weekday);
dayjs.extend(localeData);
dayjs.locale('zh-cn');
export default defineComponent({
  name: 'AddEditExpenseModal',
  emits: ['success', 'cancel', 'error'],
  components: {
    OperatorModal,
    FinancialAccountModal,
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
    'v-nodes': VNodes,
    'a-textarea': Textarea,
    'a-date-picker': DatePicker,
    'vxe-table': VXETable,
    'vxe-grid': VxeGrid,
    'vxe-input': VxeInput,
    'vxe-select': VxeSelect,
    'vxe-option': VxeOption,
    'vxe-button': VxeButton,
    'plus-outlined': PlusOutlined,
    'upload-outlined': UploadOutlined,
    'a-divider': Divider,
  },
  setup(_, context) {
    const { t } = useI18n();
    const [operatorModal, {openModal}] = useModal();
    const {createMessage} = useMessage();
    const relatedPersonList = ref([]);
    const incomeExpenseList = ref([]);
    const confirmLoading = ref<boolean>(false);
    const open = ref<boolean>(false);
    const checkFlag = ref<boolean>(true);
    const title = ref<string>("操作");
    const width = ref('1100px');
    const addDefaultRowNum = ref(1);
    const fileList = ref<FileData[]>([]);
    const minWidth = ref(1100);
    const model = ref({});
    const labelCol = ref({
      xs: {span: 24},
      sm: {span: 8},
    });
    const wrapperCol = ref({
      xs: {span: 24},
      sm: {span: 16},
    });
    const financialPersonalList = ref<OperatorResp[]>([]);
    const accountList = ref<AccountResp[]>([]);
    const amountSymbol = ref<string>('')
    const localeStore = useLocaleStore().getLocale;
    if(localeStore === 'zh_CN') {
      amountSymbol.value = '￥'
    } else if (localeStore === 'en') {
      amountSymbol.value = '$'
    }
    function handleCancelModal() {
      clearData();
      open.value = false;
      context.emit('cancel');
    }

    function openAddEditModal(id: string | undefined) {
      open.value = true
      loadfinancialPersonalList();
      loadRelatedPersonList();
      loadAccountList();
      loadIncomeExpenseList();
      if (id) {
        title.value = t('financial.expense.editExpenseReceipt')
        loadExpenseDetail(id);
      } else {
        title.value = t('financial.expense.addExpenseReceipt')
        loadGenerateId();
        expenseFormState.receiptDate = dayjs(new Date());
        addRowData()
      }
    }

    function loadAccountList() {
      accountList.value = []
      getAccountList().then(res => {
        const data: AccountResp[] = res.data
        if(data) {
          data.forEach(item => {
            const account : AccountResp = {
              id: item.id,
              accountName: item.accountName,
            }
            accountList.value.push(account)
          })
        }
      })
    }

    function loadfinancialPersonalList() {
      getOperatorList("财务员").then(res => {
        financialPersonalList.value = res.data
      })
    }

    function loadRelatedPersonList() {
      getRelatedPerson().then(res => {
        relatedPersonList.value = res.data
      })
    }

    function loadGenerateId() {
      generateId("支出单").then(res => {
        expenseFormState.receiptNumber = res.data
      })
    }

    function loadIncomeExpenseList() {
      getIncomeExpenseList("支出").then(res => {
        incomeExpenseList.value = res.data
      })
    }

    async function loadExpenseDetail(id) {
      clearData();
      const result = await getExpenseDetailById(id)
      if(result) {
        const data = result.data
        expenseFormState.id = id
        expenseFormState.receiptDate = dayjs(data.receiptDate);
        expenseFormState.receiptNumber = data.receiptNumber
        expenseFormState.remark = data.remark
        expenseFormState.relatedPersonId = data.relatedPersonId
        expenseFormState.expenseAccountId = data.expenseAccountId
        expenseFormState.financialPersonId = data.financialPersonId
        expenseFormState.expenseAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(data.expenseAmount), { digits: 2 })}`
        // file
        fileList.value = data.files.map(item => ({
          id: item.id,
          uid: item.uid,
          name: item.fileName,
          status: 'done',
          url: item.fileUrl,
          type: item.fileType,
          size: item.fileSize,
        }))

        // table
        const table = xGrid.value
        if(table) {
          data.tableData.forEach(item => {
            const tableData : RowVO = {
              incomeExpenseId: item.incomeExpenseId,
              incomeExpenseAmount: item.incomeExpenseAmount,
              remark: item.remark,
            };
            table.insert(tableData)
          })
        }
      }
    }

    async function handleOk(type: number) {
      if (!expenseFormState.relatedPersonId) {
        createMessage.warn(t('financial.expense.form.inputCorrespondenceUnit'));
        return;
      }
      if (!expenseFormState.receiptDate) {
        createMessage.warn(t('financial.expense.form.inputReceiptDate'));
        return;
      }
      if (!expenseFormState.expenseAccountId) {
        createMessage.warn(t('financial.expense.form.inputExpenseAccount'));
        return;
      }
      if (!expenseFormState.expenseAmount) {
        createMessage.warn(t('financial.expense.form.inputExpenseAmount'));
        return;
      }
      const table = xGrid.value
      if(table) {
        const insertRecords = table.getInsertRecords()
        if(insertRecords.length === 0) {
          createMessage.warn(t('financial.expense.form.addRowData'))
          return;
        }

        const isIncomeExpenseIdEmpty = insertRecords.some(item => !item.incomeExpenseId)
        if(isIncomeExpenseIdEmpty) {
          createMessage.warn(t('financial.expense.form.noticeOne'))
          return;
        }

        const isIncomeExpenseAmount = insertRecords.some(item => !item.incomeExpenseAmount)
        if(isIncomeExpenseAmount) {
          createMessage.warn(t('financial.expense.form.noticeTwo'))
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

      const dataArray = []
      table.getInsertRecords().forEach(item => {
        const data: any = {
          incomeExpenseId: item.incomeExpenseId,
          incomeExpenseAmount: item.incomeExpenseAmount,
          remark: item.remark,
        }
        dataArray.push(data)
      })
      expenseFormState.expenseAmount = expenseFormState.expenseAmount.toString().replace(/,/g, '').replace(amountSymbol.value, '')
      const params: AddOrUpdateExpenseReq = {
        ...expenseFormState,
        tableData: dataArray,
        files: files,
        status: type,
      }

      const result = await addOrUpdateExpense(params)
      if (result.code === 'E0004' || 'E0005') {
        handleCancelModal();
      }
    }

    function clearData() {
      expenseFormState.id = undefined
      expenseFormState.relatedPersonId = undefined
      expenseFormState.receiptNumber = ''
      expenseFormState.financialPersonId = undefined
      expenseFormState.expenseAmount = 0
      expenseFormState.expenseAccountId = undefined
      expenseFormState.remark = ''
      expenseFormState.receiptDate = undefined
      fileList.value = []
      const table = xGrid.value
      if(table) {
        table.remove()
      }
    }

    function beforeUpload(file: any) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        createMessage.error(`${file.name}，` + t('financial.expense.form.noticeThree'));
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

    function addOperator() {
      openModal(true, {
        isUpdate: false,
      });
    }

    function addRowData() {
      const table = xGrid.value
      if(table) {
        table.insert({productNumber: 1})
      }
    }

    async function deleteRowData() {
      // 删除选中行
      const type = await VXETable.modal.confirm(t('financial.expense.form.noticeFour'))
      const table = xGrid.value
      // 获取VXETable选中行
      const selectRow = table.getCheckboxRecords()
      if (table) {
        if (type === 'confirm') {
          await table.remove(selectRow)
        }
      }
    }

    const formatIncomeExpenseId = (value: string) => {
      const item = incomeExpenseList.value.find(item => item.id === value)
      if(item) {
        return item.name
      }
    }

    return {
      t,
      h,
      AccountBookTwoTone,
      open,
      checkFlag,
      confirmLoading,
      handleCancelModal,
      openAddEditModal,
      expenseFormState,
      title,
      width,
      addDefaultRowNum,
      fileList,
      minWidth,
      model,
      labelCol,
      wrapperCol,
      financialPersonalList,
      accountList,
      handleOk,
      beforeUpload,
      uploadFiles,
      gridOptions,
      xGrid,
      tableData,
      addRowData,
      deleteRowData,
      relatedPersonList,
      operatorModal,
      addOperator,
      incomeExpenseList,
      formatIncomeExpenseId
    };
  },
});

</script>

<style scoped>

</style>