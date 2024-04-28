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
      <a-button @click="handleCancelModal" v-text="t('financial.transfer.form.cancel')"/>
      <a-button v-if="checkFlag" :loading="confirmLoading" @click="handleOk(1)" v-text="t('financial.transfer.form.saveApprove')"/>
      <a-button type="primary" :loading="confirmLoading" @click="handleOk(0)" v-text="t('financial.transfer.form.save')"/>
      <!--发起多级审核-->
      <a-button v-if="!checkFlag" @click="" type="primary">提交流程</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="transferFormState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="8" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.transfer.form.receiptDate')" :rules="[{ required: true}]">
              <a-date-picker v-model:value="transferFormState.receiptDate" show-time :placeholder="t('financial.transfer.form.inputReceiptDate')" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :lg="8" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.transfer.form.receiptNumber')" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号">
              <a-input :placeholder="t('financial.transfer.form.inputReceiptNumber')" v-model:value="transferFormState.receiptNumber" :readOnly="true"/>
            </a-form-item>
          </a-col>
          <a-col :lg="8" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.transfer.form.financialPerson')" data-step="3"
                         data-title="财务人员"
                         data-intro="">
              <a-select v-model:value="transferFormState.financialPersonId"
                        :placeholder="t('financial.transfer.form.inputFinancialPerson')"
                        :options="financialPersonalList.map(item => ({ value: item.id, label: item.name }))">
                <template #dropdownRender="{ menuNode: menu }">
                  <v-nodes :vnodes="menu"/>
                  <a-divider style="margin: 4px 0"/>
                  <div style="padding: 4px 8px; cursor: pointer;"
                       @mousedown="e => e.preventDefault()" @click="addOperator">
                    <plus-outlined/>
                    {{ t('financial.transfer.form.addFinancialPerson') }}
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
                  <a-button type="primary" @click="addRowData" style="margin-right: 10px" v-text="t('financial.transfer.form.insertRow')" />
                  <a-button @click="deleteRowData" style="margin-right: 10px" v-text="t('financial.transfer.form.deleteRow')" />
                </template>
                <template #id_default="{ row }">
                  <span>{{ formatAccountExpenseId(row.accountId) }}</span>
                </template>
                <template #id_edit="{ row }">
                  <vxe-select :placeholder="t('financial.transfer.form.noticeOne')" v-model="row.accountId">
                    <vxe-option v-for="item in accountList" :key="item.id" :value="item.id" :label="item.accountName"></vxe-option>
                  </vxe-select>
                </template>
                <template #amount_edit="{ row }">
                  <vxe-input type="number" v-model="row.transferAmount"></vxe-input>
                </template>
              </vxe-grid>
            </div>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="24" :md="24" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="{xs: { span: 24 },sm: { span: 24 }}" label="">
                  <a-textarea :rows="2" :placeholder="t('financial.transfer.form.inputRemark')" v-model:value="transferFormState.remark"
                              style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="8" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.transfer.form.paymentAccount')" data-step="2"
                             data-title="付款账户" :rules="[{ required: true}]">
                  <a-select v-model:value="transferFormState.paymentAccountId"
                            :placeholder="t('financial.transfer.form.inputPaymentAccount')"
                            :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"/>
                </a-form-item>
              </a-col>
              <a-col :lg="8" :md="12" :sm="24" >
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.transfer.form.paymentAmount')" data-step="2"
                             data-title="实付金额" :rules="[{ required: true}]">
                  <a-input-number :placeholder="t('financial.transfer.form.inputPaymentAmount')" v-model:value="transferFormState.paymentAmount" readonly/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('financial.transfer.form.annex')" data-step="9"
                             data-title="附件"
                             data-intro="可以上传与单据相关的图片、文档，支持多个文件">
                  <a-upload
                      v-model:file-list="fileList"
                      :custom-request="uploadFiles"
                      :before-upload="beforeUpload"
                      multiple>
                    <a-button>
                      <upload-outlined/>
                      {{ t('financial.transfer.form.uploadAnnex') }}
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
  transferFormState,
  RowVO,
  xGrid,
  tableData,
  gridOptions,
} from '/@/views/financial/transfer/addEditTransfer.data';
import {getOperatorList} from "@/api/basic/operator";
import {OperatorResp} from "@/api/basic/model/operatorModel";
import {useModal} from "@/components/Modal";
import {generateId, uploadOss} from "@/api/basic/common";
import FinancialAccountModal from "@/views/basic/settlement-account/components/FinancialAccountModal.vue";
import {VXETable, VxeGrid, VxeInput, VxeButton, VxeSelect, VxeOption} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdateTransfer, getTransferDetailById} from "@/api/financial/transfer"
import {FileData} from '/@/api/retail/model/shipmentsModel';
import {getAccountList} from "@/api/financial/account";
import {AccountResp} from "@/api/financial/model/accountModel";
import XEUtils from "xe-utils";
import {AddOrUpdateTransferReq} from "@/api/financial/model/transferModel";
import OperatorModal from "@/views/basic/operator/components/OperatorModal.vue";
import weekday from "dayjs/plugin/weekday";
import localeData from "dayjs/plugin/localeData";
import {useI18n} from "vue-i18n";
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
  name: 'AddEditTransferModal',
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

    function handleCancelModal() {
      clearData();
      open.value = false;
      context.emit('cancel');
    }

    function openAddEditModal(id: string | undefined) {
      open.value = true
      loadfinancialPersonalList();
      loadAccountList();
      if (id) {
        title.value = t('financial.transfer.editTransferReceipt')
        loadTransferDetail(id);
      } else {
        title.value = t('financial.transfer.addTransferReceipt')
        loadGenerateId();
        transferFormState.receiptDate = dayjs(new Date());
        addRowData();
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

    function loadGenerateId() {
      generateId("转账单").then(res => {
        transferFormState.receiptNumber = res.data
      })
    }

    async function loadTransferDetail(id) {
      clearData();
      const result = await getTransferDetailById(id)
      if(result) {
        const data = result.data
        transferFormState.id = id
        transferFormState.receiptDate = dayjs(data.receiptDate);
        transferFormState.receiptNumber = data.receiptNumber
        transferFormState.remark = data.remark
        transferFormState.paymentAccountId = data.paymentAccountId
        transferFormState.financialPersonId = data.financialPersonId
        transferFormState.paymentAmount = `￥${XEUtils.commafy(XEUtils.toNumber(data.paymentAmount), { digits: 2 })}`
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
              accountId: item.accountId,
              transferAmount: item.transferAmount,
              remark: item.remark,
            };
            table.insert(tableData)
          })
        }
      }
    }

    async function handleOk(type: number) {
      if (!transferFormState.receiptDate) {
        createMessage.warn(t('financial.transfer.form.inputReceiptDate'));
        return;
      }
      if (!transferFormState.paymentAccountId) {
        createMessage.warn(t('financial.transfer.form.inputPaymentAccount'));
        return;
      }
      if (!transferFormState.paymentAmount) {
        createMessage.warn(t('financial.transfer.form.inputPaymentAmount'));
        return;
      }
      const table = xGrid.value
      if(table) {
        const insertRecords = table.getInsertRecords()
        if(insertRecords.length === 0) {
          createMessage.warn(t('financial.transfer.form.addRowData'))
          return;
        }
        const isAccount = insertRecords.some(item => !item.accountId)
        if(isAccount) {
          createMessage.warn(t('financial.transfer.form.inputPaymentAccount'))
          return;
        }
        const isAmount = insertRecords.some(item => !item.transferAmount)
        if(isAmount) {
          createMessage.warn(t('financial.transfer.form.inputPaymentAmount'))
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
          accountId: item.accountId,
          transferAmount: item.transferAmount,
          remark: item.remark,
        }
        dataArray.push(data)
      })
      transferFormState.paymentAmount = transferFormState.paymentAmount.toString().replace(/,/g, '').replace(/￥/g, '')
      const params: AddOrUpdateTransferReq = {
        ...transferFormState,
        tableData: dataArray,
        files: files,
        status: type,
      }

      const result = await addOrUpdateTransfer(params)
      if (result.code === 'T0001' || 'T0002') {
        handleCancelModal();
      }
    }

    function clearData() {
      transferFormState.id = undefined
      transferFormState.receiptNumber = ''
      transferFormState.financialPersonId = undefined
      transferFormState.paymentAmount = 0
      transferFormState.paymentAccountId = undefined
      transferFormState.remark = ''
      transferFormState.receiptDate = undefined
      fileList.value = []
      const table = xGrid.value
      if(table) {
        table.remove()
      }
    }

    function beforeUpload(file: any) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        createMessage.error(`${file.name}，` + t('financial.transfer.form.noticeThree'));
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
      const type = await VXETable.modal.confirm(t('financial.transfer.form.noticeFour'))
      const table = xGrid.value
      // 获取VXETable选中行
      const selectRow = table.getCheckboxRecords()
      if (table) {
        if (type === 'confirm') {
          await table.remove(selectRow)
        }
      }
    }

    const formatAccountExpenseId = (value: string) => {
      const item = accountList.value.find(item => item.id === value)
      if(item) {
        return item.accountName
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
      transferFormState,
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
      operatorModal,
      addOperator,
      formatAccountExpenseId
    };
  },
});

</script>

<style scoped>

</style>