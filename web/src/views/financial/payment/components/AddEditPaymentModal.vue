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
      <a-button @click="handleCancelModal">取消</a-button>
      <a-button v-if="checkFlag" :loading="confirmLoading" @click="handleOk(1)">保存并审核</a-button>
      <a-button type="primary" :loading="confirmLoading" @click="handleOk(0)">保存</a-button>
      <!--发起多级审核-->
      <a-button v-if="!checkFlag" @click="" type="primary">提交流程</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="paymentFormState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-input v-model:value="paymentFormState.id" v-show="false"/>
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="供应商" data-step="1"
                         data-title="供应商" :rules="[{ required: true}]">
              <a-select v-model:value="paymentFormState.supplierId" :disabled= "disabledStatus"
                        :dropdownMatchSelectWidth="false" showSearch optionFilterProp="children"
                        placeholder="请选择供应商"
                        :options="supplierList.map(item => ({ value: item.id, label: item.supplierName }))">
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据日期" :rules="[{ required: true}]">
              <a-date-picker v-model:value="paymentFormState.receiptDate" show-time placeholder="选择时间" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :lg="7" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据编号" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号">
              <a-input placeholder="请输入单据编号" v-model:value="paymentFormState.receiptNumber" :readOnly="true"/>
            </a-form-item>
          </a-col>
          <a-col :lg="5" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="财务人员" data-step="3"
                         data-title="财务人员"
                         data-intro="">
              <a-select v-model:value="paymentFormState.financialPersonId"
                        placeholder="请选择财务人员"
                        :options="financialPersonalList.map(item => ({ value: item.id, label: item.name }))">
                <template #dropdownRender="{ menuNode: menu }">
                  <v-nodes :vnodes="menu"/>
                  <a-divider style="margin: 4px 0"/>
                  <div style="padding: 4px 8px; cursor: pointer;"
                       @mousedown="e => e.preventDefault()" @click="addOperator">
                    <plus-outlined/>
                    新增财务人员
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
                  <a-button type="primary" @click="chooseReceipt" style="margin-right: 10px">选择单据</a-button>
                  <a-button @click="deleteRowData" style="margin-right: 10px">删除选中行</a-button>
                </template>
                <template #amount_edit="{ row }">
                  <vxe-input type="number" v-model="row.thisPaymentAmount"></vxe-input>
                </template>
              </vxe-grid>
            </div>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="24" :md="24" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="{xs: { span: 24 },sm: { span: 24 }}" label="">
                  <a-textarea :rows="3" placeholder="请输入备注" v-model:value="paymentFormState.remark"
                              style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="付款账户" data-step="2"
                             data-title="付款账户" :rules="[{ required: true}]">
                  <a-select v-model:value="paymentFormState.paymentAccountId"
                            placeholder="请选择付款账户"
                            :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24" >
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="合计付款" data-step="2"
                             data-title="合计付款">
                  <a-input-number placeholder="请输入付款金额" v-model:value="paymentFormState.totalPaymentAmount" readonly/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24" >
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="优惠金额" data-step="2"
                             data-title="优惠金额" :rules="[{ required: true}]">
                  <a-input-number placeholder="请输入优惠金额" @change="discountAmountChange" v-model:value="paymentFormState.discountAmount"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24" >
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="实际付款" data-step="2"
                             data-title="实际付款">
                  <a-input-number placeholder="请输入金额" v-model:value="paymentFormState.actualPaymentAmount" readonly/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="附件" data-step="9"
                             data-title="附件"
                             data-intro="可以上传与单据相关的图片、文档，支持多个文件">
                  <a-upload
                      v-model:file-list="fileList"
                      :custom-request="uploadFiles"
                      :before-upload="beforeUpload"
                      multiple>
                    <a-button>
                      <upload-outlined/>
                      点击上传附件
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
  <PurchaseArrearsModal @register="PurchaseArrearsModal" @handleReceiptSuccess="handleReceiptSuccess"/>
</template>

<script lang="ts">
import {defineComponent, ref, h, watch} from 'vue';
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
  Upload,
} from "ant-design-vue";
import {
  paymentFormState,
  RowVO,
  xGrid,
  tableData,
  gridOptions,
  getThisPaymentAmount
} from '/@/views/financial/payment/addEditPayment.data';
import {getOperatorList} from "@/api/basic/operator";
import {OperatorResp} from "@/api/basic/model/operatorModel";
import {useModal} from "@/components/Modal";
import {generateId, uploadOss} from "@/api/basic/common";
import FinancialAccountModal from "@/views/basic/settlement-account/components/FinancialAccountModal.vue";
import {VXETable, VxeGrid, VxeInput, VxeButton, VxeSelect, VxeOption} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdatePayment, getPaymentDetailById} from "@/api/financial/payment"
import {FileData} from '/@/api/retail/model/shipmentsModel';
import {getAccountList} from "@/api/financial/account";
import {getSupplierList} from "@/api/basic/supplier";
import XEUtils from "xe-utils";
import {AddOrUpdatePaymentReq} from "@/api/financial/model/paymentModel";
import OperatorModal from "@/views/basic/operator/components/OperatorModal.vue";
import PurchaseArrearsModal from "@/views/financial/payment/components/PurchaseArrearsModal.vue"
import weekday from "dayjs/plugin/weekday";
import localeData from "dayjs/plugin/localeData";
import {AccountResp} from "@/api/financial/model/accountModel";
import {SupplierResp} from "@/api/basic/model/supplierModel";
const VNodes = {
  props: {
    vnodes: {
      type: Object,
      required: true,
    },
  },
  render() {
    return this.vnodes;
  },
};
dayjs.extend(weekday);
dayjs.extend(localeData);
dayjs.locale('zh-cn');
export default defineComponent({
  name: 'AddEditPaymentModal',
  emits: ['success', 'cancel', 'error'],
  components: {
    OperatorModal,
    FinancialAccountModal,
    PurchaseArrearsModal,
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
  },
  setup(_, context) {
    const [operatorModal, {openModal}] = useModal();
    const [PurchaseArrearsModal, {openModal: openPurchaseArrearsModal}] = useModal();
    const {createMessage} = useMessage();
    const confirmLoading = ref<boolean>(false);
    const open = ref<boolean>(false);
    const checkFlag = ref<boolean>(true);
    const title = ref<string>("操作");
    const width = ref('1200px');
    const addDefaultRowNum = ref(1);
    const fileList = ref<FileData[]>([]);
    const minWidth = ref(1200);
    const model = ref({});
    const disabledStatus = ref(false);
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
    const supplierList = ref<SupplierResp[]>([]);

    function handleCancelModal() {
      close();
      clearData();
      open.value = false;
      context.emit('cancel');
    }

    function openAddEditModal(id: string | undefined) {
      open.value = true
      loadFinancialPersonalList();
      loadAccountList();
      loadsupplierList();
      if (id) {
        title.value = '编辑-付款单'
        loadPaymentDetail(id);
        disabledStatus.value = true
      } else {
        title.value = '新增-付款单'
        loadGenerateId();
        paymentFormState.receiptDate = dayjs(new Date());
        disabledStatus.value = false
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

    function loadFinancialPersonalList() {
      getOperatorList("财务员").then(res => {
        financialPersonalList.value = res.data
      })
    }

    function loadsupplierList() {
      getSupplierList().then(res => {
        supplierList.value = res.data
      })
    }

    function loadGenerateId() {
      generateId("付款单").then(res => {
        paymentFormState.receiptNumber = res.data
      })
    }

    async function loadPaymentDetail(id) {
      clearData();
      const result = await getPaymentDetailById(id)
      if(result) {
        const data = result.data
        paymentFormState.id = data.id
        paymentFormState.supplierId = data.supplierId
        paymentFormState.receiptDate = dayjs(data.receiptDate);
        paymentFormState.receiptNumber = data.receiptNumber
        paymentFormState.remark = data.remark
        paymentFormState.financialPersonId = data.financialPersonId
        paymentFormState.paymentAccountId = data.paymentAccountId
        paymentFormState.financialPersonId = data.financialPersonId
        paymentFormState.discountAmount = data.discountAmount
        paymentFormState.totalPaymentAmount = `￥${XEUtils.commafy(XEUtils.toNumber(data.totalPaymentAmount), { digits: 2 })}`
        paymentFormState.actualPaymentAmount = `￥${XEUtils.commafy(XEUtils.toNumber(data.actualPaymentAmount), { digits: 2 })}`
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
              paymentId: item.paymentId,
              purchaseReceiptNumber: item.purchaseReceiptNumber,
              paymentArrears: item.paymentArrears,
              prepaidArrears: item.prepaidArrears,
              thisPaymentAmount: item.thisPaymentAmount,
              remark: item.remark,
            };
            table.insert(tableData)
          })
        }
      }
    }

    async function handleOk(type: number) {
      if (!paymentFormState.supplierId) {
        createMessage.error('请选择供应商');
        return;
      }
      if (!paymentFormState.receiptDate) {
        createMessage.error('请选择单据日期');
        return;
      }
      if (!paymentFormState.paymentAccountId) {
        createMessage.error('请选择付款账户');
        return;
      }
      if (paymentFormState.discountAmount === undefined) {
        createMessage.error('请输入优惠金额');
        return;
      }
      const table = xGrid.value
      if(table) {
        const insertRecords = table.getInsertRecords()
        if(insertRecords.length === 0) {
          createMessage.error("请添加一行数据")
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
          paymentId: item.paymentId,
          purchaseReceiptNumber: item.purchaseReceiptNumber,
          paymentArrears: item.paymentArrears,
          prepaidArrears: item.prepaidArrears,
          thisPaymentAmount: item.thisPaymentAmount,
          remark: item.remark,
        }
        dataArray.push(data)
      })
      paymentFormState.totalPaymentAmount = paymentFormState.totalPaymentAmount.toString().replace(/,/g, '').replace(/￥/g, '')
      paymentFormState.actualPaymentAmount = paymentFormState.actualPaymentAmount.toString().replace(/,/g, '').replace(/￥/g, '')
      const params: AddOrUpdatePaymentReq = {
        ...paymentFormState,
        tableData: dataArray,
        files: files,
        status: type,
      }
      console.info(params)
      const result = await addOrUpdatePayment(params)
      if (result.code === 'P0024' || 'P0025') {
        handleCancelModal();
      }
    }

    function clearData() {
      paymentFormState.id = undefined
      paymentFormState.paymentAccountId = undefined
      paymentFormState.receiptNumber = ''
      paymentFormState.financialPersonId = undefined
      paymentFormState.discountAmount = 0
      paymentFormState.totalPaymentAmount = 0
      paymentFormState.actualPaymentAmount = 0
      paymentFormState.supplierId = undefined
      paymentFormState.remark = ''
      paymentFormState.receiptDate = undefined
      fileList.value = []
      const table = xGrid.value
      if(table) {
        table.remove()
      }
    }

    function beforeUpload(file: any) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        createMessage.error(`${file.name}，该文件超过2MB大小限制`);
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

    function chooseReceipt() {
      if (!paymentFormState.supplierId) {
        createMessage.error('请先选择供应商');
        return;
      }
      openPurchaseArrearsModal(true, {
        isUpdate: false,
        supplierId: paymentFormState.supplierId,
      });
    }

    async function deleteRowData() {
      // 删除选中行
      const type = await VXETable.modal.confirm('确定要删除选中的数据?')
      const table = xGrid.value
      // 获取VXETable选中行
      const selectRow = table.getCheckboxRecords()
      if (table) {
        if (type === 'confirm') {
          await table.remove(selectRow)
        }
      }
    }

    function handleReceiptSuccess(data) {
      const table = xGrid.value
      if(table) {
        data.forEach(item => {
          if(item.paymentArrears === 0 || item.paymentArrears === null) {
            item.paymentArrears = item.thisReceiptArrears
            item.thisPaymentAmount = item.thisReceiptArrears
          }
          if(item.paymentArrears !== 0 && item.paymentArrears !== null) {
            item.thisPaymentAmount = item.paymentArrears
          }
          const tableData : RowVO = {
            paymentId: item.id,
            purchaseReceiptNumber: item.receiptNumber,
            paymentArrears: item.paymentArrears,
            prepaidArrears: item.prepaidArrears,
            thisPaymentAmount: item.thisPaymentAmount,
            remark: item.remark,
          };
          table.insert(tableData)
        })
      }
    }

    watch(getThisPaymentAmount,(newValue, oldValue) => {
      if(oldValue !== '￥0.00') {
        paymentFormState.totalPaymentAmount = newValue
        paymentFormState.actualPaymentAmount = newValue
        discountAmountChange()
      }
    });

    function discountAmountChange() {
      const totalPaymentAmount = paymentFormState.totalPaymentAmount.toString().replace(/,/g, '').replace(/￥/g, '')
      const discountAmount = paymentFormState.discountAmount.toString().replace(/,/g, '').replace(/￥/g, '')
      const actualPaymentAmount = XEUtils.toNumber(totalPaymentAmount) - XEUtils.toNumber(discountAmount)
      paymentFormState.actualPaymentAmount = `￥${XEUtils.commafy(actualPaymentAmount, { digits: 2 })}`
    }

    return {
      h,
      AccountBookTwoTone,
      open,
      checkFlag,
      confirmLoading,
      handleCancelModal,
      openAddEditModal,
      paymentFormState,
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
      supplierList,
      handleOk,
      beforeUpload,
      uploadFiles,
      gridOptions,
      xGrid,
      tableData,
      deleteRowData,
      operatorModal,
      addOperator,
      chooseReceipt,
      PurchaseArrearsModal,
      handleReceiptSuccess,
      discountAmountChange,
      disabledStatus,
    };
  },
});

</script>

<style scoped>

</style>