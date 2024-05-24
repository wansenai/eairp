<template>
  <BasicModal
      :title="title"
      :width="width"
      :confirm-loading="confirmLoading"
      v-bind:prefixNo="prefixNo"
      :id="prefixNo"
      :forceRender="true"
      :keyboard="true"
      switchHelp
      switchFullscreen
      :height="560"
      :maxHeight="560"
      @cancel="handleCancelModal"
      v-model:open="open"
      style="top: 20px; left: 20px">
    <template #footer>
      <a-button @click="handleCancelModal" v-text="t('retail.refund.form.cancel')" />
      <a-button v-if="checkFlag && isCanCheck" :loading="confirmLoading" @click="handleOk(1)" v-text="t('retail.refund.form.cancel')" />
      <a-button type="primary" :loading="confirmLoading" @click="handleOk(0)" v-text="t('retail.refund.form.save')" />
      <!--发起多级审核-->
      <a-button v-if="!checkFlag" @click="" type="primary">提交流程</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="formState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-input v-model:value="formState.id" v-show="false"/>
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('retail.refund.form.member')" data-step="1"
                         data-title="会员卡号"
                         data-intro="如果发现需要选择的会员卡号尚未录入，可以在下拉框中点击新增会员信息进行录入">
              <a-select :placeholder="t('retail.refund.form.inputMember')" v-model:value="formState.memberId" :disabled= "disabledStatus"
                        :dropdownMatchSelectWidth="false" showSearch optionFilterProp="children"
                        :options="memberList.map(item => ({ value: item.id, label: item.memberName }))">
                <template #dropdownRender="{ menuNode: menu }">
                  <v-nodes :vnodes="menu"/>
                  <a-divider style="margin: 4px 0"/>
                  <div style="padding: 4px 8px; cursor: pointer; color: #1c1e21"
                       @mousedown="e => e.preventDefault()" @click="addMember">
                    <plus-outlined/>
                    {{ t('retail.refund.form.addMember') }}
                  </div>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('retail.refund.form.receiptDate')" :rules="[{ required: true}]">
              <a-date-picker v-model:value="formState.receiptDate" show-time :placeholder="t('retail.refund.form.inputReceiptDate')" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('retail.refund.form.receiptNumber')" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号" :rules="[{ required: true}]">
              <a-input v-model:value="formState.receiptNumber"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('retail.refund.form.relatedReceipt')" data-step="3"
                         data-title="关联单据"
                         data-intro="">
              <a-input-search readonly v-model:value="formState.otherReceipt" @search="onSearch"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="18" :md="12" :sm="24" style="margin-bottom: 150px;">
            <div class="table-operations">
              <vxe-grid ref='xGrid' v-bind="gridOptions">
                <template #toolbar_buttons="{ row }">
                  <a-button v-if="showScanButton" type="primary"  @click="scanEnter" style="margin-right: 10px" v-text="t('retail.refund.form.collapseScanCode')"/>
                  <a-input v-if="showScanPressEnter" :placeholder="t('retail.refund.form.scanCodeTip')" style="width: 150px; margin-right: 10px" v-model:value="formState.scanBarCode"
                           @pressEnter="scanPressEnter" ref="scanBarCode"/>
                  <a-button v-if="showScanPressEnter" style="margin-right: 10px" @click="stopScan" v-text="t('retail.refund.form.collapseScanCode')"/>
                  <a-button @click="productModal" style="margin-right: 10px" v-text="t('retail.refund.form.addProduct')"/>
                  <a-button @click="addRowData" style="margin-right: 10px" v-text="t('retail.refund.form.insertRow')"/>
                  <a-button @click="deleteRowData" style="margin-right: 10px" v-text="t('retail.refund.form.deleteRow')"/>
                </template>
                <template #product_number_edit="{ row }">
                  <vxe-input v-model="row.productNumber"></vxe-input>
                </template>
                <template #amount_edit="{ row }">
                  <vxe-input v-model="row.amount"></vxe-input>
                </template>
                <template #barCode_edit="{ row }">
                  <vxe-select v-model="row.barCode" :placeholder="t('retail.refund.form.table.inputBarCode')" @change="selectBarCode" :options="productLabelList" clearable filterable></vxe-select>
                </template>
                <template #return_number_edit="{ row }">
                  <vxe-input v-model="row.returnNumber"></vxe-input>
                </template>
              </vxe-grid>
            </div>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="24" :md="24" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="{xs: { span: 24 },sm: { span: 24 }}" label="">
                  <a-textarea :rows="1" :placeholder="t('retail.refund.form.table.inputRemark')" v-model:value="formState.remark"
                              style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('retail.refund.form.table.annex')" data-step="9"
                             data-title="附件"
                             data-intro="可以上传与单据相关的图片、文档，支持多个文件">
                  <a-upload
                      v-model:file-list="fileList"
                      :custom-request="uploadFiles"
                      :before-upload="beforeUpload"
                      multiple>
                    <a-button>
                      <upload-outlined/>
                      {{ t('retail.refund.form.table.uploadAnnex') }}
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
                      <span style="font-size: 20px;line-height:20px">{{ t('retail.refund.view.receiptAmount') }}</span>
                    </template>
                    <a-input v-model:value="receiptAmount" :style="{color:'purple', height:'35px'}"
                             :readOnly="true"/>
                  </a-form-item>
                </a-col>
                <a-col :lg="24" :md="6" :sm="6">
                  <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" data-step="6" data-title="付款金额">
                    <template #label>
                      <span style="font-size: 20px;line-height:20px">{{ t('retail.refund.view.paymentAmount') }}</span>
                    </template>
                    <a-input v-model:value="paymentAmount" :style="{color:'red', height:'35px'}"
                             defaultValue="0"
                             @change="onChangePaymentAmount"/>
                  </a-form-item>
                </a-col>
                <a-col :lg="24" :md="6" :sm="6">
                  <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" data-step="7" data-title="找零">
                    <template #label>
                      <span style="font-size: 20px;line-height:20px">{{ t('retail.refund.view.changeAmount') }}</span>
                    </template>
                    <a-input v-model:value="backAmount" :style="{color:'green', height:'35px'}"
                             :readOnly="true"
                             defaultValue="0"/>
                  </a-form-item>
                </a-col>
                <a-col :lg="24" :md="6" :sm="6">
                  <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" data-step="8" data-title="付款账户" :rules="[{ required: true}]">
                    <template #label>
                      <span style="font-size: 20px;line-height:20px">{{ t('retail.refund.view.paymentAccount') }}</span>
                    </template>
                    <a-select :placeholder="t('retail.refund.view.inputPaymentAccount')" style="font-size:20px;"
                              v-model:value="formState.accountId" :dropdownMatchSelectWidth="false"
                              :options="accountList.map(item => ({ value: item.id, label: item.accountName }))">
                      <template #dropdownRender="{ menuNode: menu }">
                        <v-nodes :vnodes="menu"/>
                        <a-divider style="margin: 4px 0"/>
                        <div style="padding: 4px 8px; cursor: pointer; color: #1c1e21"
                             @mousedown="e => e.preventDefault()" @click="addAccount">
                          <plus-outlined/>
                          {{ t('retail.refund.view.addAccount') }}
                        </div>
                      </template>
                    </a-select>
                  </a-form-item>
                </a-col>
              </a-row>
            </div>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </BasicModal>
  <MemberModal @register="memberModal" @success="handleMemberModalSuccess"/>
  <FinancialAccountModal @register="accountModal" @success="handleAccountModalSuccess"/>
  <SelectProductModal @register="selectProductModal" @handleCheckSuccess="handleCheckSuccess"/>
  <LinkReceiptModal @register="linkReceiptModal" @handleReceiptSuccess="handleReceiptSuccess"/>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import {PlusOutlined, UploadOutlined} from '@ant-design/icons-vue';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
import weekday from "dayjs/plugin/weekday";
import localeData from "dayjs/plugin/localeData";
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
  formState,
  gridOptions,
  xGrid,
  receiptAmount,
  paymentAmount, RowVO,
} from '/@/views/retail/shipments/model/addEditModel';
import {getMemberList} from "@/api/basic/member";
import {MemberResp} from "@/api/basic/model/memberModel";
import {getAccountList} from "@/api/financial/account";
import {getWarehouseList} from "@/api/basic/warehouse";
import {AccountResp} from "@/api/financial/model/accountModel";
import MemberModal from "@/views/basic/member/components/MemberModal.vue";
import {useModal} from "@/components/Modal";
import {generateId, uploadOss} from "@/api/basic/common";
import FinancialAccountModal from "@/views/basic/settlement-account/components/FinancialAccountModal.vue";
import {WarehouseResp} from "@/api/basic/model/warehouseModel";
import {VXETable, VxeGrid, VxeInput, VxeButton, VxeSelect} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdateRefund, getRefundDetail} from "@/api/retail/refund"
import SelectProductModal from "@/views/product/info/components/SelectProductModal.vue"
import {getProductSkuByBarCode, getProductStockSku} from "@/api/product/product";
import XEUtils from "xe-utils";
import {ProductExtendPriceResp, ProductStockSkuResp} from "@/api/product/model/productModel";
import {AddOrUpdateRefundReq} from "@/api/retail/model/refundModel";
import {FileData, ShipmentsData} from "@/api/retail/model/shipmentsModel";
import LinkReceiptModal from "@/views/receipt/LinkReceiptModal.vue";
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
  name: 'AddEditModal',
  emits: ['success', 'cancel', 'error'],
  components: {
    BasicModal,
    LinkReceiptModal,
    FinancialAccountModal,
    MemberModal,
    SelectProductModal,
    'plus-outlined': PlusOutlined,
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
    'upload-outlined': UploadOutlined,
    'vxe-table': VXETable,
    'vxe-grid': VxeGrid,
    'vxe-input': VxeInput,
    'vxe-button': VxeButton,
    'vxe-select': VxeSelect,
    'a-divider': Divider,
  },
  setup(_, context) {
    const { t } = useI18n();
    const {createMessage} = useMessage();
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
    const prefixNo = ref('LSTH');
    const fileList = ref<FileData[]>([]);
    const payTypeList = ref<any>([]);
    const minWidth = ref(1100);
    const amountSymbol = ref<string>('')
    const localeStore = useLocaleStore().getLocale;
    if(localeStore === 'zh_CN') {
      amountSymbol.value = '￥'
    } else if (localeStore === 'en') {
      amountSymbol.value = '$'
    }
    const backAmount = ref(amountSymbol.value + '0.00');
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
    const refKey = ref(['productDataTable']);
    const activeKey = ref('productDataTable');
    const memberList = ref<MemberResp[]>([])
    const accountList = ref<AccountResp[]>([]);
    const warehouseList = ref<WarehouseResp[]>([]);
    const [memberModal, {openModal}] = useModal();
    const [accountModal, {openModal: openAccountModal}] = useModal();
    const [selectProductModal, {openModal: openProductModal}] = useModal();
    const [linkReceiptModal, {openModal: openLinkReceiptModal}] = useModal();
    const productList = ref<ProductStockSkuResp[]>([]);
    const productLabelList = ref<any[]>([]);

    function handleCancelModal() {
      close();
      clearData();
      open.value = false;
      context.emit('cancel');
    }

    function openAddEditModal(id: string | undefined) {
      open.value = true
      loadMemberList();
      loadAccountList();
      loadWarehouseList();
      loadProductSku();
      if (id) {
        title.value = t('retail.refund.editRefund')
        loadRefundDetail(id);
        disabledStatus.value = true
      } else {
        title.value = t('retail.refund.addRefund')
        loadGenerateId();
        disabledStatus.value = false
        formState.receiptDate = dayjs(new Date());
      }
    }

    function loadMemberList() {
      getMemberList().then(res => {
        memberList.value = res.data
      })
    }

    function loadAccountList() {
      getAccountList().then(res => {
        accountList.value = res.data
      })
    }

    function loadGenerateId() {
      generateId("零售退货").then(res => {
        formState.receiptNumber = res.data
      })
    }

    function loadWarehouseList() {
      getWarehouseList().then(res => {
        const {columns} = gridOptions
        if (columns) {
          const warehouseColumn = columns[1]
          warehouseColumn.editRender.options = [];
          if (warehouseColumn && warehouseColumn.editRender) {
            warehouseColumn.editRender.options?.push(...res.data.map(item => ({value: item.id, label: item.warehouseName})))
          }
          warehouseList.value = res.data
        }
      })
    }

    function loadProductSku() {
      getProductStockSku().then(res => {
        productList.value = res.data
        productLabelList.value.push(...res.data.map(item => ({value: item.productBarcode, label: item.productBarcode})))
        productLabelList.value = productLabelList.value.filter((item, index, arr) => {
          return arr.findIndex(item1 => item1.value === item.value) === index
        })
      })
    }

    function selectBarCode() {
      const table = xGrid.value
      const selectRow = table?.getActiveRecord()
      if(selectRow) {
        const {columns} = gridOptions
        if (columns) {
          const barCodeColumn = selectRow.row.barCode
          const warehouseColumn = selectRow.row.warehouseId
          if(barCodeColumn && warehouseColumn) {
            const product = productList.value.find(item => {
              return item.productBarcode === barCodeColumn && item.warehouseId === warehouseColumn;
            });
            if (product) {
              selectRow.row.productId = product.productId
              selectRow.row.productName = product.productName
              selectRow.row.productStandard = product.productStandard
              selectRow.row.productUnit = product.productUnit
              selectRow.row.stock = product.currentStock
              selectRow.row.retailPrice = product.retailPrice
              selectRow.row.amount = product.retailPrice
              selectRow.row.productNumber = 1
              table.updateData(selectRow.rowIndex, selectRow.row)
            } else {
              createMessage.warn(t('retail.refund.form.noticeSix'))
            }
          }
        }
      }
    }

    async function loadRefundDetail(id) {
      clearData();
      const result = await getRefundDetail(id)
      if(result) {
        const data = result.data
        formState.id = id
        formState.memberId = data.memberId
        formState.receiptDate = dayjs(data.receiptDate);
        formState.accountId = data.accountId
        formState.receiptNumber = data.receiptNumber
        formState.remark = data.remark
        formState.otherReceipt = data.otherReceipt
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
              warehouseId: item.warehouseId,
              productId: item.productId,
              barCode: item.barCode,
              productName: item.productName,
              productStandard: item.productStandard,
              productUnit: item.productUnit,
              stock: item.stock,
              productNumber: item.productNumber,
              amount: item.amount,
              retailPrice: item.unitPrice,
            };
            table.insert(tableData)
          })
        }
        formState.memberId.disabled = true
        receiptAmount.value = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(data.receiptAmount), { digits: 2 })}`
        paymentAmount.value = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(data.paymentAmount), { digits: 2 })}`
        backAmount.value = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(data.backAmount), { digits: 2 })}`
      }
    }

    function onChangePaymentAmount() {
      const sum = receiptAmount.value
      const collect = paymentAmount.value
      const sumNumber = sum.replace(/,/g, '').replace(amountSymbol.value, '');
      const collectNumber = collect.replace(/,/g, '').replace(amountSymbol.value, '');
      const numberAmount = Number(collectNumber) - Number(sumNumber)
      backAmount.value = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(numberAmount), { digits: 2 })}`
    }

    function scanPressEnter() {
      const warehouseDefaultId = warehouseList.value.find(item => item.isDefault === 1)?.id || warehouseList.value[0].id
      getProductSkuByBarCode(formState.scanBarCode, warehouseDefaultId).then(res => {
        const {columns} = gridOptions
        if (columns) {
          const {data} = res
          if (data) {
            const productExtendPrice : ProductExtendPriceResp = data
            const table = xGrid.value
            if (table) {
              //根据productExtendPrice.id判断表格中如果是同一个商品，数量加1 否则新增一行
              const tableData = table.getTableData().tableData
              const index = tableData.findIndex(item => item.barCode === productExtendPrice.barCode && item.warehouseId === productExtendPrice.warehouseId)
              if (index > -1) {
                const row = tableData[index]
                row.productNumber = row.productNumber + 1
                row.amount = row.productNumber * row.retailPrice
                table.updateData(index, row)
              } else {
                table.insert({
                  id: productExtendPrice.id,
                  warehouseId: productExtendPrice.warehouseId,
                  productId: productExtendPrice.productId,
                  barCode: productExtendPrice.barCode,
                  productName: productExtendPrice.productName,
                  retailPrice: productExtendPrice.retailPrice,
                  productStandard: productExtendPrice.productStandard,
                  productUnit: productExtendPrice.productUnit,
                  stock: productExtendPrice.stock,
                  productNumber: 1,
                })
              }
            }
          }
        }
      })
      // 清除扫码框的值
      formState.scanBarCode = ''
      onChangePaymentAmount()
    }

    function scanEnter() {
      showScanButton.value = !showScanButton.value;
      showScanPressEnter.value = !showScanPressEnter.value;
    }

    function stopScan() {
      showScanButton.value = !showScanButton.value;
      showScanPressEnter.value = !showScanPressEnter.value;
    }

    function addMember() {
      openModal(true, {
        isUpdate: false,
      });
    }

    function handleMemberModalSuccess() {
      loadMemberList();
    }

    function addAccount() {
      openAccountModal(true, {
        isUpdate: false,
      });
    }

    function handleAccountModalSuccess() {
      loadAccountList();
    }

    async function handleOk(type: number) {
      const table = xGrid.value
      if (!formState.receiptDate) {
        createMessage.warn(t('retail.refund.form.inputReceiptDate'));
        return;
      }
      if (!formState.receiptNumber) {
        createMessage.warn(t('retail.shipments.form.inputReceiptNumber'));
        return;
      }
      if (!formState.accountId) {
        createMessage.warn(t('retail.refund.view.inputPaymentAccount'));
        return;
      }
      if(table) {
        const insertRecords = table.getInsertRecords()
        if(insertRecords.length === 0) {
          createMessage.warn(t('retail.refund.form.addRowData'))
          return;
        }
        const isBarCodeEmpty = insertRecords.some(item => !item.barCode)
        if(isBarCodeEmpty) {
          createMessage.warn(t('retail.refund.form.noticeTwo'))
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
        const data: ShipmentsData = {
          warehouseId: item.warehouseId,
          barCode: item.barCode,
          productId: item.productId,
          productNumber: item.productNumber,
          unitPrice: item.retailPrice,
          amount: item.amount,
        }
        dataArray.push(data)
      })

      const sum = receiptAmount.value
      const payment = paymentAmount.value
      const sumNumber = sum.replace(/,/g, '').replace(amountSymbol.value, '');
      const paymentNumber = payment.replace(/,/g, '').replace(amountSymbol.value, '');
      const backAmount = Number(paymentNumber) - Number(sumNumber)
      formState.paymentAmount = Number(paymentNumber)
      formState.receiptAmount = Number(sumNumber)
      formState.backAmount = backAmount

      const params: AddOrUpdateRefundReq = {
        ...formState,
        tableData: dataArray,
        files: files,
        status: type,
      }
      const result = await addOrUpdateRefund(params)
      if (result.code === 'R0004' || result.code === 'R0005') {
        handleCancelModal();
      }
    }

    function clearData() {
      formState.id = undefined
      formState.receiptNumber = ''
      formState.memberId = ''
      formState.paymentType = ''
      formState.accountId = ''
      formState.remark = ''
      formState.scanBarCode = ''
      formState.otherReceipt = ''
      receiptAmount.value = ''
      paymentAmount.value = ''
      backAmount.value = amountSymbol.value + '0.00'
      fileList.value = []
      const table = xGrid.value
      if(table) {
        // 清空表格数据
        table.remove()
      }
      formState.receiptDate = undefined
    }

    function beforeUpload(file: any) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        createMessage.error(`${file.name}，` + t('retail.refund.form.noticeFive'));
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

    function productModal() {
      openProductModal(true, {
        isUpdate: false,
      });
    }

    function handleCheckSuccess(data) {
      const table = xGrid.value
      if(table) {
        data.forEach(item => {
          item.productNumber = 1
        })
        table.insert(data)
      }
    }

    function handleReceiptSuccess(data) {
      const table = xGrid.value
      if(data && table) {
        formState.otherReceipt = data.receiptNumber;
        table.remove()
        data.receiptDetailData.forEach(item => {
          const tableData : RowVO = {
            warehouseId: item.warehouseId,
            productId: item.productId,
            barCode: item.productBarcode,
            productName: item.productName,
            productStandard: item.productStandard,
            productUnit: item.unit,
            stock: item.stock,
            productNumber: item.productNumber,
            amount: item.amount,
            retailPrice: item.unitPrice,
          };
          table.insert(tableData)
        })
        formState.memberId = data.receiptDetailData[0].memberId
      }
    }

    function addRowData() {
      const table = xGrid.value
      const defaultWarehouse = warehouseList.value.find(item => item.isDefault === 1)
      const warehouseId = defaultWarehouse ? defaultWarehouse.id : warehouseList.value[0].id
      if(table) {
        table.insert({productNumber: 1, warehouseId: warehouseId})
      }
    }

    async function deleteRowData() {
      // 删除选中行
      const type = await VXETable.modal.confirm(t('retail.refund.form.noticeFour'))
      const table = xGrid.value
      // 获取VXETable选中行
      const selectRow = table.getCheckboxRecords()
      if (table) {
        if (type === 'confirm') {
          await table.remove(selectRow)
        }
      }
    }

    function onSearch() {
      openLinkReceiptModal(true, {
        type: '零售',
        subType: '零售出库',
        title: '选择零售出库订单'
      });
    }

    return {
      t,
      open,
      checkFlag,
      isCanCheck,
      isTenant,
      scanStatus,
      confirmLoading,
      handleCancelModal,
      openAddEditModal,
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
      scanPressEnter,
      scanEnter,
      stopScan,
      memberList,
      accountList,
      warehouseList,
      showScanButton,
      showScanPressEnter,
      memberModal,
      addMember,
      accountModal,
      addAccount,
      handleOk,
      beforeUpload,
      uploadFiles,
      gridOptions,
      xGrid,
      receiptAmount,
      paymentAmount,
      backAmount,
      selectProductModal,
      openProductModal,
      productModal,
      handleCheckSuccess,
      addRowData,
      deleteRowData,
      onSearch,
      handleReceiptSuccess,
      disabledStatus,
      linkReceiptModal,
      productList,
      productLabelList,
      selectBarCode,
      handleMemberModalSuccess,
      handleAccountModalSuccess
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

::v-deep(.customer-table .ant-table-cell) {
  padding-left: 3px;
  padding-right: 3px;
}

</style>