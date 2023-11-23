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
      <a-form ref="formRef" :model="collectionFormState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-input v-model:value="collectionFormState.id" v-show="false"/>
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="客户" data-step="1"
                         data-title="客户" :rules="[{ required: true}]">
              <a-select v-model:value="collectionFormState.customerId" :disabled= "disabledStatus"
                        :dropdownMatchSelectWidth="false" showSearch optionFilterProp="children"
                        placeholder="请选择客户"
                        :options="customerList.map(item => ({ value: item.id, label: item.customerName }))">
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据日期" :rules="[{ required: true}]">
              <a-date-picker v-model:value="collectionFormState.receiptDate" show-time placeholder="选择时间" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :lg="7" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据编号" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号">
              <a-input placeholder="请输入单据编号" v-model:value="collectionFormState.receiptNumber" :readOnly="true"/>
            </a-form-item>
          </a-col>
          <a-col :lg="5" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="财务人员" data-step="3"
                         data-title="财务人员"
                         data-intro="">
              <a-select v-model:value="collectionFormState.financialPersonId"
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
                  <vxe-input type="number" v-model="row.thisCollectionAmount"></vxe-input>
                </template>
              </vxe-grid>
            </div>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="24" :md="24" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="{xs: { span: 24 },sm: { span: 24 }}" label="">
                  <a-textarea :rows="3" placeholder="请输入备注" v-model:value="collectionFormState.remark"
                              style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="收款账户" data-step="2"
                             data-title="支出账户" :rules="[{ required: true}]">
                  <a-select v-model:value="collectionFormState.collectionAccountId"
                            placeholder="请选择支出账户"
                            :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24" >
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="合计金额" data-step="2"
                             data-title="合计金额">
                  <a-input-number placeholder="请输入金额" v-model:value="collectionFormState.totalCollectionAmount" readonly/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24" >
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="优惠金额" data-step="2"
                             data-title="优惠金额" :rules="[{ required: true}]">
                  <a-input-number placeholder="请输入优惠金额" @change="discountAmountChange" v-model:value="collectionFormState.discountAmount"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24" >
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="实际收款" data-step="2"
                             data-title="实际收款">
                  <a-input-number placeholder="请输入金额" v-model:value="collectionFormState.actualCollectionAmount" readonly/>
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
  <SaleArrearsModal @register="saleArrearsModal" @handleReceiptSuccess="handleReceiptSuccess"/>
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
  collectionFormState,
  RowVO,
  xGrid,
  tableData,
  gridOptions,
  getThisCollectionAmount
} from '/@/views/financial/collection/addEditCollection.data';
import {getOperatorList} from "@/api/basic/operator";
import {OperatorResp} from "@/api/basic/model/operatorModel";
import {useModal} from "@/components/Modal";
import {generateId, uploadOss} from "@/api/basic/common";
import FinancialAccountModal from "@/views/basic/settlement-account/components/FinancialAccountModal.vue";
import {VXETable, VxeGrid, VxeInput, VxeButton, VxeSelect, VxeOption} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdateCollection, getCollectionDetailById} from "@/api/financial/collection"
import {FileData} from '/@/api/retail/model/shipmentsModel';
import {getAccountList} from "@/api/financial/account";
import {getCustomerList} from "@/api/basic/customer";
import XEUtils from "xe-utils";
import {AddOrUpdateCollectionReq} from "@/api/financial/model/collectionModel";
import OperatorModal from "@/views/basic/operator/components/OperatorModal.vue";
import SaleArrearsModal from "@/views/financial/collection/components/SaleArrearsModal.vue"
import weekday from "dayjs/plugin/weekday";
import localeData from "dayjs/plugin/localeData";
import {AccountResp} from "@/api/financial/model/accountModel";
import {CustomerResp} from "@/api/basic/model/customerModel";
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
  name: 'AddEditCollectionModal',
  emits: ['success', 'cancel', 'error'],
  components: {
    OperatorModal,
    FinancialAccountModal,
    SaleArrearsModal,
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
    const [saleArrearsModal, {openModal: openSaleArrearsModal}] = useModal();
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
    const customerList = ref<CustomerResp[]>([]);

    function handleCancelModal() {
      close();
      clearData();
      open.value = false;
      context.emit('cancel');
    }

    function openAddEditModal(id: string | undefined) {
      open.value = true
      loadfinancialPersonalList();
      loadAccountList();
      loadCustomerList();
      if (id) {
        title.value = '编辑-收款单'
        loadCollectionDetail(id);
        disabledStatus.value = true
      } else {
        title.value = '新增-收款单'
        loadGenerateId();
        collectionFormState.receiptDate = dayjs(new Date());
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

    function loadfinancialPersonalList() {
      getOperatorList("财务员").then(res => {
        financialPersonalList.value = res.data
      })
    }

    function loadCustomerList() {
      getCustomerList().then(res => {
        customerList.value = res.data
      })
    }

    function loadGenerateId() {
      generateId("收款单").then(res => {
        collectionFormState.receiptNumber = res.data
      })
    }

    async function loadCollectionDetail(id) {
      clearData();
      const result = await getCollectionDetailById(id)
      if(result) {
        const data = result.data
        collectionFormState.id = data.id
        collectionFormState.customerId = data.customerId
        collectionFormState.receiptDate = dayjs(data.receiptDate);
        collectionFormState.receiptNumber = data.receiptNumber
        collectionFormState.remark = data.remark
        collectionFormState.financialPersonId = data.financialPersonId
        collectionFormState.collectionAccountId = data.collectionAccountId
        collectionFormState.financialPersonId = data.financialPersonId
        collectionFormState.discountAmount = data.discountAmount
        collectionFormState.totalCollectionAmount = `￥${XEUtils.commafy(XEUtils.toNumber(data.totalCollectionAmount), { digits: 2 })}`
        collectionFormState.actualCollectionAmount = `￥${XEUtils.commafy(XEUtils.toNumber(data.actualCollectionAmount), { digits: 2 })}`
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
              collectionId: item.collectionId,
              saleReceiptNumber: item.saleReceiptNumber,
              receivableArrears: item.receivableArrears,
              receivedArrears: item.receivedArrears,
              thisCollectionAmount: item.thisCollectionAmount,
              remark: item.remark,
            };
            table.insert(tableData)
          })
        }
      }
    }

    async function handleOk(type: number) {
      if (!collectionFormState.customerId) {
        createMessage.error('请选择客户');
        return;
      }
      if (!collectionFormState.receiptDate) {
        createMessage.error('请选择单据日期');
        return;
      }
      if (!collectionFormState.collectionAccountId) {
        createMessage.error('请选择收款账户');
        return;
      }
      if (collectionFormState.discountAmount === undefined) {
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
          collectionId: item.collectionId,
          saleReceiptNumber: item.saleReceiptNumber,
          receivableArrears: item.receivableArrears,
          receivedArrears: item.receivedArrears,
          thisCollectionAmount: item.thisCollectionAmount,
          remark: item.remark,
        }
        dataArray.push(data)
      })
      collectionFormState.totalCollectionAmount = collectionFormState.totalCollectionAmount.toString().replace(/,/g, '').replace(/￥/g, '')
      collectionFormState.actualCollectionAmount = collectionFormState.actualCollectionAmount.toString().replace(/,/g, '').replace(/￥/g, '')
      const params: AddOrUpdateCollectionReq = {
        ...collectionFormState,
        tableData: dataArray,
        files: files,
        status: type,
      }
      console.info(params)
      const result = await addOrUpdateCollection(params)
      if (result.code === 'C0001' || 'C0003') {
        createMessage.success('操作成功');
        handleCancelModal();
        clearData();

      } else {
        createMessage.error('操作失败');
      }
      clearData();
    }

    function clearData() {
      collectionFormState.id = undefined
      collectionFormState.collectionAccountId = undefined
      collectionFormState.receiptNumber = ''
      collectionFormState.financialPersonId = undefined
      collectionFormState.discountAmount = 0
      collectionFormState.totalCollectionAmount = 0
      collectionFormState.actualCollectionAmount = 0
      collectionFormState.customerId = undefined
      collectionFormState.remark = ''
      collectionFormState.receiptDate = undefined
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
      if (!collectionFormState.customerId) {
        createMessage.error('请先选择客户');
        return;
      }
      openSaleArrearsModal(true, {
        isUpdate: false,
        customerId: collectionFormState.customerId,
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
          // 如果receivableArrears和receivedArrears都为0或者null的话 就设置receivableArrears和thisCollectionAmount为thisReceiptArrears的值
          if(item.receivableArrears === 0 || item.receivableArrears === null) {
            item.receivableArrears = item.thisReceiptArrears
            item.thisCollectionAmount = item.thisReceiptArrears
          }
          if(item.receivableArrears !== 0 && item.receivableArrears !== null) {
            item.thisCollectionAmount = item.receivableArrears
          }
          const tableData : RowVO = {
            collectionId: item.id,
            saleReceiptNumber: item.receiptNumber,
            receivableArrears: item.receivableArrears,
            receivedArrears: item.receivedArrears,
            thisCollectionAmount: item.thisCollectionAmount,
            remark: item.remark,
          };
          table.insert(tableData)
        })
      }
    }

    watch(getThisCollectionAmount,(newValue, oldValue) => {
      if(oldValue !== '￥0.00') {
        collectionFormState.totalCollectionAmount = newValue
        collectionFormState.actualCollectionAmount = newValue
        discountAmountChange()
      }
    });

    function discountAmountChange() {
      // 获取合计收款金额 和优惠金额 计算实际收款金额
      // 实际收款金额 = 合计收款金额 - 优惠金额
      const totalCollectionAmount = collectionFormState.totalCollectionAmount.toString().replace(/,/g, '').replace(/￥/g, '')
      const discountAmount = collectionFormState.discountAmount.toString().replace(/,/g, '').replace(/￥/g, '')
      const actualCollectionAmount = XEUtils.toNumber(totalCollectionAmount) - XEUtils.toNumber(discountAmount)
      collectionFormState.actualCollectionAmount = `￥${XEUtils.commafy(actualCollectionAmount, { digits: 2 })}`
    }

    return {
      h,
      AccountBookTwoTone,
      open,
      checkFlag,
      confirmLoading,
      handleCancelModal,
      openAddEditModal,
      collectionFormState,
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
      customerList,
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
      saleArrearsModal,
      handleReceiptSuccess,
      discountAmountChange,
      disabledStatus,
    };
  },
});

</script>

<style scoped>

</style>