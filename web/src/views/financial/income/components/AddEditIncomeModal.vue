<template>
  <BasicModal
      :title="title"
      :width="width"
      :confirm-loading="confirmLoading"
      :forceRender="true"
      :keyboard="true"
      switchHelp
      switchFullscreen
      :height="600"
      :maxHeight="660"
      @cancel="handleCancelModal"
      v-model:open="open"
      style="top: 30px; left: 20px">
    <template #footer>
      <a-button @click="handleCancelModal" v-text="t('financial.income.form.cancel')"/>
      <a-button v-if="checkFlag" :loading="confirmLoading" @click="handleOk(1)" v-text="t('financial.income.form.saveApprove')"/>
      <a-button type="primary" :loading="confirmLoading" @click="handleOk(0)" v-text="t('financial.income.form.save')"/>
      <!--发起多级审核-->
      <a-button v-if="!checkFlag" @click="" type="primary">提交流程</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="incomeFormState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-input v-model:value="incomeFormState.id" v-show="false"/>
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.income.form.correspondenceUnit')" data-step="1"
                         data-title="往来单位" :rules="[{ required: true}]">
              <a-select v-model:value="incomeFormState.relatedPersonId"
                        :dropdownMatchSelectWidth="false" showSearch optionFilterProp="children"
                        :placeholder="t('financial.income.form.inputCorrespondenceUnit')"
                        :options="relatedPersonList.map(item => ({ value: item.id, label: item.name }))">
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.income.form.receiptDate')" :rules="[{ required: true}]">
              <a-date-picker v-model:value="incomeFormState.receiptDate" show-time :placeholder="t('financial.income.form.inputReceiptDate')" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.income.form.receiptNumber')" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号" :rules="[{ required: true}]">
              <a-input :placeholder="t('financial.income.form.inputReceiptNumber')" v-model:value="incomeFormState.receiptNumber"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.income.form.financialPerson')" data-step="3"
                         data-title="财务人员"
                         data-intro="">
              <a-select v-model:value="incomeFormState.financialPersonId"
                        :placeholder="t('financial.income.form.inputFinancialPerson')"
                        :options="financialPersonalList.map(item => ({ value: item.id, label: item.name }))">
                <template #dropdownRender="{ menuNode: menu }">
                  <v-nodes :vnodes="menu"/>
                  <a-divider style="margin: 4px 0"/>
                  <div style="padding: 4px 8px; cursor: pointer;"
                       @mousedown="e => e.preventDefault()" @click="addOperator">
                    <plus-outlined/>
                    {{ t('financial.income.form.addFinancialPerson') }}
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
                  <a-button type="primary" @click="addRowData" style="margin-right: 10px" v-text="t('financial.income.form.insertRow')" />
                  <a-button @click="deleteRowData" style="margin-right: 10px" v-text="t('financial.income.form.deleteRow')" />
                </template>
                <template #id_default="{ row }">
                  <span>{{ formatIncomeExpenseId(row.incomeExpenseId) }}</span>
                </template>
                <template #id_edit="{ row }">
                  <vxe-select :placeholder="t('financial.income.form.noticeOne')" v-model="row.incomeExpenseId">
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
                  <a-textarea :rows="1" :placeholder="t('financial.income.form.inputRemark')" v-model:value="incomeFormState.remark"
                              style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.income.form.incomeAccount')" data-step="2"
                             data-title="收入账户" :rules="[{ required: true}]">
                  <a-select v-model:value="incomeFormState.incomeAccountId"
                            :placeholder="t('financial.income.form.inputIncomeAccount')"
                            :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24" >
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.income.form.incomeAmount')" data-step="2"
                             data-title="收入金额" :rules="[{ required: true}]">
                  <a-input-number :placeholder="t('financial.income.form.inputIncomeAmount')" v-model:value="incomeFormState.incomeAmount" readonly/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.income.form.annex')" data-step="9"
                             data-title="附件"
                             data-intro="可以上传与单据相关的图片、文档，支持多个文件">
                  <a-upload
                      v-model:file-list="fileList"
                      :custom-request="uploadFiles"
                      :before-upload="beforeUpload"
                      multiple>
                    <a-button>
                      <upload-outlined/>
                      {{ t('financial.income.form.uploadAnnex') }}
                    </a-button>
                  </a-upload>
                </a-form-item>
              </a-col>
            </a-row>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </BasicModal>
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
  incomeFormState,
  RowVO,
  xGrid,
  tableData,
  gridOptions,
} from '/@/views/financial/income/addEditIncome.data';
import {getOperatorList} from "@/api/basic/operator";
import {OperatorResp} from "@/api/basic/model/operatorModel";
import {useModal} from "@/components/Modal";
import {generateId, uploadOss} from "@/api/basic/common";
import FinancialAccountModal from "@/views/basic/settlement-account/components/FinancialAccountModal.vue";
import {VXETable, VxeGrid, VxeInput, VxeButton, VxeSelect, VxeOption} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdateIncome, getIncomeDetailById} from "@/api/financial/income"
import {FileData} from '/@/api/retail/model/shipmentsModel';
import {getAccountList} from "@/api/financial/account";
import {getRelatedPerson} from "@/api/report/report";
import {getIncomeExpenseList} from "@/api/basic/incomeExpense";
import {AccountResp} from "@/api/financial/model/accountModel";
import XEUtils from "xe-utils";
import {AddOrUpdateIncomeReq} from "@/api/financial/model/incomeModel";
import OperatorModal from "@/views/basic/operator/components/OperatorModal.vue";
import weekday from "dayjs/plugin/weekday";
import localeData from "dayjs/plugin/localeData";
import incomeExpense from "@/views/basic/income-expense/index.vue";
import {useI18n} from "vue-i18n";
import {useLocaleStore} from "@/store/modules/locale";
import BasicModal from "@/components/Modal/src/BasicModal.vue";
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
  name: 'AddEditIncomeModal',
  computed: {
    incomeExpense() {
      return incomeExpense
    }
  },
  emits: ['success', 'cancel', 'error'],
  components: {
    BasicModal,
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
        title.value = t('financial.income.editIncomeReceipt')
        loadIncomeDetail(id);
      } else {
        title.value = t('financial.income.addIncomeReceipt')
        loadGenerateId();
        incomeFormState.receiptDate = dayjs(new Date());
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
      generateId("收入单").then(res => {
        incomeFormState.receiptNumber = res.data
      })
    }

    function loadIncomeExpenseList() {
      getIncomeExpenseList("收入").then(res => {
        incomeExpenseList.value = res.data
      })
    }

    async function loadIncomeDetail(id) {
      clearData();
      const result = await getIncomeDetailById(id)
      if(result) {
        const data = result.data
        incomeFormState.id = id
        incomeFormState.receiptDate = dayjs(data.receiptDate);
        incomeFormState.receiptNumber = data.receiptNumber
        incomeFormState.remark = data.remark
        incomeFormState.relatedPersonId = data.relatedPersonId
        incomeFormState.incomeAccountId = data.incomeAccountId
        incomeFormState.financialPersonId = data.financialPersonId
        incomeFormState.incomeAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(data.incomeAmount), { digits: 2 })}`
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
      if (!incomeFormState.relatedPersonId) {
        createMessage.warn(t('financial.income.form.inputCorrespondenceUnit'));
        return;
      }
      if (!incomeFormState.receiptDate) {
        createMessage.warn(t('financial.income.form.inputReceiptDate'));
        return;
      }
      if (!incomeFormState.receiptNumber) {
        createMessage.warn(t('sales.order.form.inputReceiptNumber'));
        return;
      }
      if (!incomeFormState.incomeAccountId) {
        createMessage.warn(t('financial.income.form.inputIncomeAccount'));
        return;
      }
      if (!incomeFormState.incomeAmount) {
        createMessage.warn(t('financial.income.form.inputIncomeAmount'));
        return;
      }
      const table = xGrid.value
      if(table) {
        const insertRecords = table.getInsertRecords()
        if(insertRecords.length === 0) {
          createMessage.warn(t('financial.income.form.addRowData'));
          return;
        }

        const isIncomeExpenseIdEmpty = insertRecords.some(item => !item.incomeExpenseId)
        if(isIncomeExpenseIdEmpty) {
          createMessage.warn(t('financial.income.form.noticeOne'))
          return;
        }

        const isIncomeExpenseAmount = insertRecords.some(item => !item.incomeExpenseAmount)
        if(isIncomeExpenseAmount) {
          createMessage.warn(t('financial.income.form.noticeTwo'))
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
      incomeFormState.incomeAmount = incomeFormState.incomeAmount.toString().replace(/,/g, '').replace(amountSymbol.value, '')
      const params: AddOrUpdateIncomeReq = {
        ...incomeFormState,
        tableData: dataArray,
        files: files,
        status: type,
      }

      const result = await addOrUpdateIncome(params)
      if (result.code === 'I0004' || 'I0005') {
        handleCancelModal();
      }
    }

    function clearData() {
      incomeFormState.id = undefined
      incomeFormState.relatedPersonId = undefined
      incomeFormState.receiptNumber = ''
      incomeFormState.financialPersonId = undefined
      incomeFormState.incomeAmount = 0
      incomeFormState.incomeAccountId = undefined
      incomeFormState.remark = ''
      incomeFormState.receiptDate = undefined
      fileList.value = []
      const table = xGrid.value
      if(table) {
        table.remove()
      }
    }

    function beforeUpload(file: any) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        createMessage.error(`${file.name}，` + t('financial.income.form.noticeThree'));
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
      const type = await VXETable.modal.confirm(t('financial.income.form.noticeFour'))
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
      incomeFormState,
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