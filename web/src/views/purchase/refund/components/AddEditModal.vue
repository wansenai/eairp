<template>
  <BasicModal
      :title="title"
      :width="width"
      :confirm-loading="confirmLoading"
      :forceRender="true"
      :keyboard="true"
      switchHelp
      switchFullscreen
      :height="650"
      :maxHeight="750"
      @cancel="handleCancelModal"
      v-model:open="open"
      style="top: 20px;">
    <template #footer>
      <a-button @click="handleCancelModal" v-text="t('purchase.refund.form.cancel')" />
      <a-button v-if="checkFlag && isCanCheck" :loading="confirmLoading" @click="handleOk(1)" v-text="t('purchase.refund.form.saveApprove')" />
      <a-button type="primary" :loading="confirmLoading" @click="handleOk(0)" v-text="t('purchase.refund.form.save')" />
      <!--发起多级审核-->
      <a-button v-if="!checkFlag" @click="" type="primary">提交流程</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="purchaseRefundFormState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-input v-model:value="purchaseRefundFormState.id" v-show="false"/>
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('purchase.refund.form.supplier')" data-step="1"
                         data-title="供应商" :rules="[{ required: true}]">
              <a-select v-model:value="purchaseRefundFormState.supplierId"
                        :dropdownMatchSelectWidth="false" showSearch optionFilterProp="children"
                        :placeholder="t('purchase.refund.form.inputSupplier')"
                        :options="supplierList.map(item => ({ value: item.id, label: item.supplierName }))">
                <template #dropdownRender="{ menuNode: menu }">
                  <v-nodes :vnodes="menu"/>
                  <a-divider style="margin: 4px 0"/>
                  <div style="padding: 4px 8px; cursor: pointer; color: #1c1e21"
                       @mousedown="e => e.preventDefault()" @click="addSupplier">
                    <plus-outlined/>
                    {{ t('purchase.refund.form.addSupplier') }}
                  </div>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('purchase.refund.form.receiptDate')" :rules="[{ required: true}]">
              <a-date-picker v-model:value="purchaseRefundFormState.receiptDate" show-time :placeholder="t('purchase.refund.form.inputReceiptDate')" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('purchase.refund.form.receiptNumber')" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号" :rules="[{ required: true}]">
              <a-input v-model:value="purchaseRefundFormState.receiptNumber"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('purchase.refund.form.relatedStorage')" data-step="3"
                         data-title="关联单据"
                         data-intro="">
              <a-input-search :readOnly="true" v-model:value="purchaseRefundFormState.otherReceipt" @search="onSearch"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="24" :md="12" :sm="24" style="margin-bottom: 150px;">
            <div class="table-operations">
              <vxe-grid ref='xGrid' v-bind="gridOptions">
                <template #toolbar_buttons="{ row }">
                  <a-button v-if="showScanButton" type="primary"  @click="scanEnter" style="margin-right: 10px" v-text="t('purchase.refund.form.scanCodeData')"/>
                  <a-input v-if="showScanPressEnter" :placeholder="t('purchase.refund.form.scanCodeTip')" style="width: 150px; margin-right: 10px" v-model:value="barCode"
                           @pressEnter="scanPressEnter" ref="scanBarCode"/>
                  <a-button v-if="showScanPressEnter" style="margin-right: 10px" @click="stopScan" v-text="t('purchase.refund.form.collapseScanCode')"/>
                  <a-button @click="productModal" style="margin-right: 10px" v-text="t('purchase.refund.form.addProduct')"/>
                  <a-button @click="addRowData" style="margin-right: 10px" v-text="t('purchase.refund.form.insertRow')"/>
                  <a-button @click="deleteRowData" style="margin-right: 10px" v-text="t('purchase.refund.form.deleteRow')"/>
                </template>
                <template #warehouse_default="{ row }">
                  <span>{{ formatWarehouseId(row.warehouseId) }}</span>
                </template>
                <template #warehouse_edit="{ row }">
                  <vxe-select v-model="row.warehouseId" :placeholder="t('purchase.refund.form.table.inputWarehouse')" @change="selectBarCode" :options="warehouseLabelList" clearable filterable></vxe-select>
                </template>
                <template #barCode_edit="{ row }">
                  <vxe-select v-model="row.barCode" :placeholder="t('purchase.refund.form.table.inputBarCode')" @change="selectBarCode" :options="productLabelList" clearable filterable></vxe-select>
                </template>
                <template #product_number_edit="{ row }">
                  <vxe-input v-model="row.productNumber" @change="productNumberChange"></vxe-input>
                </template>
                <template #price_edit="{ row }">
                  <vxe-input v-model="row.unitPrice" @change="unitPriceChange"></vxe-input>
                </template>
                <template #amount_edit="{ row }">
                  <vxe-input v-model="row.amount" @change="amountChange"></vxe-input>
                </template>
                <template #tax_rate_edit="{ row }">
                  <vxe-input v-model="row.taxRate" @change="taxRateChange"></vxe-input>
                </template>
                <template #tax_amount_edit="{ row }">
                  <vxe-input v-model="row.taxAmount" disabled></vxe-input>
                </template>
                <template #tax_total_price_edit="{ row }">
                  <vxe-input v-model="row.taxTotalPrice" @change="taxTotalPriceChange"></vxe-input>
                </template>
              </vxe-grid>
            </div>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="24" :md="24" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="{xs: { span: 24 },sm: { span: 24 }}" label="">
                  <a-textarea :rows="1" :placeholder="t('purchase.refund.form.table.inputRemark')" v-model:value="purchaseRefundFormState.remark"
                              style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('purchase.refund.form.discountRate')" data-step="2"
                             data-title="优惠率">
                  <a-input-number @change="discountRateChange" addon-after="%" v-model:value="purchaseRefundFormState.refundOfferRate"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('purchase.refund.form.refundDiscount')" data-step="2"
                             data-title="退款优惠">
                  <a-input-number @change="discountAmountChange" v-model:value="purchaseRefundFormState.refundOfferAmount"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('purchase.refund.form.discountAmount')" data-step="2"
                             data-title="优惠后金额">
                  <a-input v-model:value="purchaseRefundFormState.refundLastAmount" :readOnly="true"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('purchase.refund.form.otherFees')" data-step="2"
                             data-title="其他费用">
                  <a-input-number @change="otherAmountChange" v-model:value="purchaseRefundFormState.otherAmount" :readOnly="true"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('purchase.refund.form.settlementAccount')" data-step="2"
                             data-title="结算账户"
                             :rules="[{ required: true}]">
                  <a-select v-model:value="purchaseRefundFormState.accountId"
                            :placeholder="t('purchase.refund.view.inputSettlementAccount')"
                            :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"
                            @change="selectAccountChange">
                    <template #dropdownRender="{ menuNode: menu }">
                      <v-nodes :vnodes="menu"/>
                      <a-divider style="margin: 4px 0"/>
                      <div style="padding: 4px 8px; cursor: pointer; color: #1c1e21"
                           @mousedown="e => e.preventDefault()" @click="addAccount">
                        <plus-outlined/>
                        {{ t('purchase.refund.view.addSettlementAccount') }}
                      </div>
                    </template>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col style="margin-left: -25px">
                <a-tooltip title="多账户明细">
                  <a-button type="default" :icon="h(AccountBookTwoTone)" style="font-size: small; margin-left: 5px" v-show="manyAccountBtnStatus" @click="handleManyAccount"/>
                </a-tooltip>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('purchase.refund.view.thisRefundAmount')" data-step="2"
                             data-title="本次退款"
                             :rules="[{ required: true}]">
                  <a-input-number @change="thisPaymentAmountChange" v-model:value="purchaseRefundFormState.thisRefundAmount"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24" >
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('purchase.refund.view.thisTimeArrearsAmount')" data-step="2"
                             data-title="本次欠款">
                  <a-input :readOnly="true" v-model:value="purchaseRefundFormState.thisArrearsAmount"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('purchase.refund.form.table.annex')" data-step="9"
                             data-title="附件"
                             data-intro="可以上传与单据相关的图片、文档，支持多个文件">
                  <a-upload
                      v-model:file-list="fileList"
                      :custom-request="uploadFiles"
                      :before-upload="beforeUpload"
                      multiple>
                    <a-button>
                      <upload-outlined/>
                      {{ t('purchase.refund.form.table.uploadAnnex') }}
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
  <SupplierModal @register="supplierModal" @success="handleSupplierModalSuccess"/>
  <FinancialAccountModal @register="accountModal" @success="handleAccountModalSuccess"/>
  <SelectProductModal @register="selectProductModal" @handleCheckSuccess="handleCheckSuccess"/>
  <MultipleAccountsModal @register="multipleAccountModal" @handleAccountSuccess="handleAccountSuccess" />
  <LinkReceiptModal @register="linkReceiptModal" @handleReceiptSuccess="handleReceiptSuccess"/>
</template>

<script lang="ts">
import {defineComponent, ref, h, watch} from 'vue';
import {AccountBookTwoTone, PlusOutlined, UploadOutlined} from '@ant-design/icons-vue';
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
  purchaseRefundFormState,
  RowVO,
  xGrid,
  tableData,
  gridOptions,
  getTaxTotalPrice,
} from '/src/views/purchase/model/addEditModel';
import {useModal} from "@/components/Modal";
import {generateId, uploadOss} from "@/api/basic/common";
import FinancialAccountModal from "@/views/basic/settlement-account/components/FinancialAccountModal.vue";
import {VXETable, VxeGrid, VxeInput, VxeButton} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdatePurchaseRefund, getPurchaseRefundDetail} from "@/api/purchase/refund"
import SupplierModal from "@/views/basic/supplier/components/SupplierModal.vue"
import SelectProductModal from "@/views/product/info/components/SelectProductModal.vue"
import {getProductSkuByBarCode, getProductStockSku} from "@/api/product/product";
import {getWarehouseList} from "@/api/basic/warehouse";
import {AddOrUpdatePurchaseRefundReq, PurchaseRefundData} from "@/api/purchase/model/refundModel";
import {FileData} from '/@/api/retail/model/shipmentsModel';
import {getAccountList} from "@/api/financial/account";
import {AccountResp} from "@/api/financial/model/accountModel";
import XEUtils from "xe-utils";
import MultipleAccountsModal from "@/views/basic/settlement-account/components/MultipleAccountsModal.vue";
import {SupplierResp} from "@/api/basic/model/supplierModel";
import {addSupplier, getSupplierList} from "@/api/basic/supplier";
import LinkReceiptModal from "@/views/receipt/LinkReceiptModal.vue";
import {ProductStockSkuResp} from "@/api/product/model/productModel";
import {WarehouseResp} from "@/api/basic/model/warehouseModel";
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
  name: 'AddEditPurchaseRefundModal',
  methods: {addSupplier},
  emits: ['success', 'cancel', 'error'],
  components: {
    BasicModal,
    LinkReceiptModal,
    MultipleAccountsModal,
    FinancialAccountModal,
    SupplierModal,
    SelectProductModal,
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
    'vxe-button': VxeButton,
    'plus-outlined': PlusOutlined,
    'upload-outlined': UploadOutlined,
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
    const manyAccountBtnStatus = ref(false);
    const fileList = ref<FileData[]>([]);
    const payTypeList = ref<any>([]);
    const minWidth = ref(1100);
    const model = ref({});
    const barCode = ref('');
    const formRef = ref('');
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
    const supplierList = ref<SupplierResp[]>([])
    const accountList = ref<AccountResp[]>([]);
    const warehouseList = ref<WarehouseResp[]>([]);
    const multipleAccounts = ref();
    const [supplierModal, {openModal}] = useModal();
    const [accountModal, {openModal: openAccountModal}] = useModal();
    const [selectProductModal, {openModal: openProductModal}] = useModal();
    const [multipleAccountModal, {openModal: openManyAccountModal}] = useModal();
    const [linkReceiptModal, {openModal: openLinkReceiptModal}] = useModal();
    const productList = ref<ProductStockSkuResp[]>([]);
    const productLabelList = ref<any[]>([]);
    const warehouseLabelList =  ref<any[]>([]);
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
      Modal.destroyAll();
    }

    function openAddEditModal(id: string | undefined) {
      open.value = true
      loadSupplierList();
      loadWarehouseList();
      loadAccountList();
      loadProductSku();
      if (id) {
        title.value = t('purchase.refund.editRefund')
        loadRefundDetail(id);
      } else {
        title.value = t('purchase.refund.addRefund')
        loadGenerateId();
        purchaseRefundFormState.receiptDate = dayjs(new Date());
        const table = xGrid.value
        if (table) {
          table.insert({productNumber: 0})
        }
      }
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
          warehouseLabelList.value.push(...res.data.map(item => ({value: item.id, label: item.warehouseName})))
        }
        const defaultWarehouse = res.data.find(item => item.isDefault === 1)
        if(defaultWarehouse) {
          purchaseRefundFormState.warehouseId = defaultWarehouse.id
        } else {
          purchaseRefundFormState.warehouseId = res.data[0].id
        }
      })
    }

    function loadAccountList() {
      // 每次赋值前先清空
      accountList.value = []
      const account : AccountResp = {
        id: 0,
        accountName: '多账户',
      }
      accountList.value.push(account)
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

    function handleAccountModalSuccess() {
      loadAccountList();
    }

    function loadSupplierList() {
      getSupplierList().then(res => {
        supplierList.value = res.data
      })
    }

    function handleSupplierModalSuccess() {
      loadSupplierList();
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
              selectRow.row.unitPrice = product.purchasePrice
              selectRow.row.taxTotalPrice = product.purchasePrice
              selectRow.row.amount = product.purchasePrice
              selectRow.row.productNumber = 1
              selectRow.row.taxRate = 0
            } else {
              createMessage.warn(t('purchase.refund.form.noticeFour'))
              selectRow.row.barCode = '';
              selectRow.row.productId = undefined
              selectRow.row.productName = ''
              selectRow.row.productStandard = ''
              selectRow.row.productUnit = ''
              selectRow.row.stock = 0
              selectRow.row.unitPrice = 0
              selectRow.row.taxTotalPrice = 0
              selectRow.row.amount = 0
              selectRow.row.productNumber = 0
              selectRow.row.taxRate = 0
            }
          }
        }
      }
    }

    function loadGenerateId() {
      generateId("采购退货").then(res => {
        purchaseRefundFormState.receiptNumber = res.data
      })
    }

    async function loadRefundDetail(id) {
      const result = await getPurchaseRefundDetail(id)
      if(result) {
        const data = result.data
        purchaseRefundFormState.id = id
        purchaseRefundFormState.supplierId = data.supplierId
        purchaseRefundFormState.receiptDate = dayjs(data.receiptDate);
        purchaseRefundFormState.receiptNumber = data.receiptNumber
        purchaseRefundFormState.remark = data.remark
        purchaseRefundFormState.refundOfferRate = data.refundOfferRate
        purchaseRefundFormState.refundOfferAmount = data.refundOfferAmount
        purchaseRefundFormState.refundLastAmount = data.refundLastAmount
        purchaseRefundFormState.otherReceipt = data.otherReceipt
        purchaseRefundFormState.otherAmount = data.otherAmount
        purchaseRefundFormState.thisRefundAmount = data.thisRefundAmount
        purchaseRefundFormState.thisArrearsAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(data.thisArrearsAmount), { digits: 2 })}`
        // 判断多账户渲染
        if(data.multipleAccountAmounts.length > 0 && data.multipleAccountIds.length > 0) {
          manyAccountBtnStatus.value = true
          purchaseRefundFormState.accountId = 0
          multipleAccounts.value = {
            accountOne: data.multipleAccountIds[0],
            accountTwo: data.multipleAccountIds[1],
            accountThree: data.multipleAccountIds[2],
            accountPriceOne: data.multipleAccountAmounts[0],
            accountPriceTwo: data.multipleAccountAmounts[1],
            accountPriceThree: data.multipleAccountAmounts[2],
          }
        } else {
          manyAccountBtnStatus.value = false
          purchaseRefundFormState.accountId = data.accountId
        }

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
              unitPrice: item.unitPrice,
              taxRate: item.taxRate,
              taxAmount: item.taxAmount,
              taxTotalPrice: item.taxTotalPrice,
              remark: item.remark,
            };
            table.insert(tableData)
          })
        }
      }
    }

    function scanPressEnter() {
      getProductSkuByBarCode(barCode.value, purchaseRefundFormState.warehouseId).then(res => {
        const {columns} = gridOptions
        if (columns) {
          const {data} = res
          if (data) {
            const purchase : PurchaseRefundData = data
            const table = xGrid.value
            if (table) {
              //根据productExtendPrice.id判断表格中如果是同一个商品，数量加1 否则新增一行
              const tableData = table.getTableData().tableData
              const index = tableData.findIndex(item => item.barCode === purchase.barCode)
              if (index > -1) {
                const row = tableData[index]
                row.productNumber = Number(row.productNumber) + 1
                row.amount = row.productNumber * row.unitPrice
                row.amount = row.unitPrice * row.productNumber
                row.taxAmount = row.amount * row.taxRate / 100
                row.taxTotalPrice = row.amount + row.taxAmount
                row.amount = Number(row.amount.toFixed(2))
                row.taxAmount = Number(row.taxAmount.toFixed(2))
                row.taxTotalPrice = Number(row.taxTotalPrice.toFixed(2))

                table.updateData()
              } else {
                const tableData : RowVO = {
                  warehouseId: purchase.warehouseId,
                  productId: purchase.productId,
                  barCode: purchase.barCode,
                  productName: purchase.productName,
                  productStandard: purchase.productStandard,
                  productUnit: purchase.productUnit,
                  stock: purchase.stock,
                  productNumber: 1,
                  amount: purchase.purchasePrice,
                  unitPrice: purchase.purchasePrice,
                  taxTotalPrice: purchase.purchasePrice,
                  taxRate: 0,
                  taxAmount: 0,
                };
                table.insert(tableData)
              }
              purchaseRefundFormState.refundLastAmount = getTaxTotalPrice.value
            }
          }
        }
      })
      // 清除扫码框的值
      barCode.value = ''
    }

    function scanEnter() {
      showScanButton.value = !showScanButton.value;
      showScanPressEnter.value = !showScanPressEnter.value;
    }

    function stopScan() {
      showScanButton.value = !showScanButton.value;
      showScanPressEnter.value = !showScanPressEnter.value;
    }

    function addSupplier() {
      openModal(true, {
        isUpdate: false,
      });
    }

    function addAccount() {
      openAccountModal(true, {
        isUpdate: false,
      });
    }

    async function handleOk(type: number) {
      const table = xGrid.value
      if (!purchaseRefundFormState.supplierId) {
        createMessage.warn(t('purchase.refund.form.inputSupplier'));
        return;
      }
      if (!purchaseRefundFormState.receiptNumber) {
        createMessage.warn(t('sales.order.form.inputReceiptNumber'));
        return;
      }
      if (purchaseRefundFormState.accountId === 0) {
        if(!multipleAccounts.value.accountOne && !multipleAccounts.value.accountTwo) {
          createMessage.warn(t('purchase.refund.form.noticeSeven'));
          return;
        }
        if(!multipleAccounts.value.accountPriceOne && !multipleAccounts.value.accountPriceTwo) {
          createMessage.warn(t('purchase.refund.form.noticeEight'));
          return;
        }
      } else if (!purchaseRefundFormState.accountId) {
        createMessage.warn(t('purchase.refund.view.inputSettlementAccount'));
        return;
      }
      if(table) {
        const insertRecords = table.getInsertRecords()
        if(insertRecords.length === 0) {
          createMessage.warn(t('purchase.refund.form.addRowData'))
          return;
        }
        const isBarCodeEmpty = insertRecords.some(item => !item.barCode)
        if(isBarCodeEmpty) {
          createMessage.warn(t('purchase.refund.form.noticeOne'))
          return;
        }
        const isWarehouseEmpty = insertRecords.some(item => !item.warehouseId)
        if(isWarehouseEmpty) {
          createMessage.warn(t('purchase.refund.form.table.inputWarehouse'))
          return;
        }
      }
      // 库存校验
      const tableData = table.getTableData().tableData
      const isStockNotEnough = tableData.some(item => item.productNumber > item.stock)
      if(isStockNotEnough) {
        const tableDataNotEnough = tableData.filter(item => item.productNumber > item.stock)
        const tableDataNotEnoughBarCode = tableDataNotEnough.map(item => item.barCode)
        const tableDataNotEnoughBarCodeStr = tableDataNotEnoughBarCode.join(",")
        createMessage.info(t('purchase.refund.form.table.barCode') +": "+tableDataNotEnoughBarCodeStr +" " + t('purchase.refund.form.noticeNine'))
        return;
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
        const data: PurchaseRefundData = {
          warehouseId: item.warehouseId,
          barCode: item.barCode,
          productId: item.productId,
          productNumber: item.productNumber,
          unitPrice: item.unitPrice,
          amount: item.amount,
          taxRate: item.taxRate,
          taxAmount: item.taxAmount,
          taxTotalPrice: item.taxTotalPrice,
          remark: item.remark,
        }
        dataArray.push(data)
      })

      // 判断是否是多账户 0是多账户 如果是多账户需要把accountIds置空 如果不是需要把multipleAccountIds 和 multipleAccountAmounts 置空
      if (purchaseRefundFormState.accountId === 0) {
        purchaseRefundFormState.accountId = undefined
        purchaseRefundFormState.multipleAccountIds = []
        purchaseRefundFormState.multipleAccountAmounts = []
        if(multipleAccounts.value.accountOne) {
          purchaseRefundFormState.multipleAccountIds.push(multipleAccounts.value.accountOne)
        }
        if(multipleAccounts.value.accountTwo){
          purchaseRefundFormState.multipleAccountIds.push(multipleAccounts.value.accountTwo)
        }
        if(multipleAccounts.value.accountThree) {
          purchaseRefundFormState.multipleAccountIds.push(multipleAccounts.value.accountThree)
        }
        if(multipleAccounts.value.accountOne) {
          purchaseRefundFormState.multipleAccountAmounts.push(multipleAccounts.value.accountPriceOne)
        }
        if(multipleAccounts.value.accountTwo){
          purchaseRefundFormState.multipleAccountAmounts.push(multipleAccounts.value.accountPriceTwo)
        }
        if(multipleAccounts.value.accountThree) {
          purchaseRefundFormState.multipleAccountAmounts.push(multipleAccounts.value.accountPriceThree)
        }
      } else {
        purchaseRefundFormState.multipleAccountIds = []
        purchaseRefundFormState.multipleAccountAmounts = []
      }

      purchaseRefundFormState.thisRefundAmount = purchaseRefundFormState.thisRefundAmount.toString().replace(/,/g, '').replace(amountSymbol.value, '')
      purchaseRefundFormState.refundLastAmount = purchaseRefundFormState.refundLastAmount.toString().replace(/,/g, '').replace(amountSymbol.value, '')
      purchaseRefundFormState.thisArrearsAmount = purchaseRefundFormState.thisArrearsAmount.toString().replace(/,/g, '').replace(amountSymbol.value, '')
      const params: AddOrUpdatePurchaseRefundReq = {
        ...purchaseRefundFormState,
        tableData: dataArray,
        files: files,
        status: type,
      }

      const result = await addOrUpdatePurchaseRefund(params)
      if (result.code === 'P0021' || 'P0022') {
        createMessage.success(t('sys.api.operationSuccess'));
        handleCancelModal();
        clearData();

      } else {
        createMessage.error(t('sys.api.operationFailed'));
      }
      clearData();
    }

    function clearTable() {
      const table = xGrid.value
      if(table) {
        table.remove()
      }
    }

    function clearData() {
      purchaseRefundFormState.id = undefined
      purchaseRefundFormState.receiptNumber = ''
      purchaseRefundFormState.supplierId = ''
      purchaseRefundFormState.refundOfferRate = 0
      purchaseRefundFormState.refundOfferAmount = 0
      purchaseRefundFormState.refundLastAmount = 0
      purchaseRefundFormState.otherAmount = 0
      purchaseRefundFormState.thisRefundAmount = 0
      purchaseRefundFormState.thisArrearsAmount = 0
      purchaseRefundFormState.otherReceipt = ''
      purchaseRefundFormState.accountId = undefined
      barCode.value = ''
      purchaseRefundFormState.remark = ''
      fileList.value = []
      warehouseList.value = []
      warehouseLabelList.value = []
      multipleAccounts.value = {}
      purchaseRefundFormState.receiptDate = undefined
      clearTable()
    }

    function beforeUpload(file: any) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        createMessage.error(`${file.name}，` + t('purchase.refund.form.noticeThree'));
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
        data = data.map(item => {
          item.unitPrice = item.purchasePrice
          item.productNumber = 1
          item.amount = item.purchasePrice * item.productNumber
          item.taxRate = 0
          item.taxAmount = 0
          item.taxTotalPrice = item.amount + item.taxRate
          return item
        })
        table.insert(data)
        const tableData = table.getTableData().tableData
        let total = 0
        tableData.forEach(item => {
          total += item.taxTotalPrice
        })
        purchaseRefundFormState.refundLastAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(total), { digits: 2 })}`
      }
    }

    function addRowData() {
      const table = xGrid.value
      const defaultWarehouse = warehouseList.value.find(item => item.isDefault === 1)
      const warehouseId = defaultWarehouse ? defaultWarehouse.id : warehouseList.value[0].id
      if(table) {
        table.insert({warehouseId: warehouseId, productNumber: 0})
      }
    }

    async function deleteRowData() {
      // 删除选中行
      const type = await VXETable.modal.confirm(t('purchase.refund.form.noticeTwo'))
      const table = xGrid.value
      // 获取VXETable选中行
      const selectRow = table.getCheckboxRecords()
      if (table) {
        if (type === 'confirm') {
          await table.remove(selectRow)
        }
      }
    }

    function productNumberChange() {
      const table = xGrid.value
      if(table) {
        const data = table.getEditRecord().row;
        data.amount = data.productNumber * data.unitPrice
        data.taxAmount = data.amount * data.taxRate / 100
        data.taxTotalPrice = data.amount + data.taxAmount

        data.amount = Number(data.amount.toFixed(2))
        data.taxAmount = Number(data.taxAmount.toFixed(2))
        data.taxTotalPrice = Number(data.taxTotalPrice.toFixed(2))
        table.updateData()
      }
    }

    function unitPriceChange() {
      const table = xGrid.value
      if(table) {
        // 通过单价 计算金额 税额 价税合计
        // 已知 数量 和 税率
        // 计算金额 = 单价 * 数量
        // 计算税额 = 金额 * 税率 / 100
        // 计算价税合计 = 金额 + 税额
        const data = table.getEditRecord().row;
        data.amount = data.unitPrice * data.productNumber
        data.taxAmount = data.amount * data.taxRate / 100
        data.taxTotalPrice = data.amount + data.taxAmount
        data.amount = Number(data.amount.toFixed(2))
        data.taxAmount = Number(data.taxAmount.toFixed(2))
        data.taxTotalPrice = Number(data.taxTotalPrice.toFixed(2))
        table.updateData()
      }
    }

    function amountChange() {
      const table = xGrid.value
      if(table) {
        const data = table.getEditRecord().row;
        data.unitPrice = data.amount / data.productNumber
        data.taxAmount = data.amount * data.taxRate / 100
        data.taxTotalPrice = Number(data.amount) + Number(data.taxAmount)
        data.unitPrice = Number(data.unitPrice.toFixed(2))
        data.taxAmount = Number(data.taxAmount.toFixed(2))
        data.taxTotalPrice= Number(data.taxTotalPrice.toFixed(2))
        table.updateData()
      }
    }

    function taxRateChange() {
      const table = xGrid.value
      if(table) {
        const data = table.getEditRecord().row;
        // 计算税额 = 金额 * 税率 / 100 保留两位小数
        data.taxAmount = data.amount * data.taxRate / 100
        data.taxTotalPrice = Number(data.amount) + Number(data.taxAmount)
        data.taxAmount = Number(data.taxAmount.toFixed(2))
        data.taxTotalPrice = Number(data.taxTotalPrice.toFixed(2))
        table.updateData()
      }
    }

    function taxTotalPriceChange() {

      const table = xGrid.value
      if(table) {
        const data = table.getEditRecord().row;
        // 通过价税合计 分别进行税额 和 金额 单价的计算
        // 已知税率 和 数量
        // 计算金额 = 价税合计 / (1 + 税率 / 100)
        // 计算单价 = 金额 / 数量
        data.amount = data.taxTotalPrice / (1 + data.taxRate / 100)
        data.unitPrice = data.amount / data.productNumber
        data.taxAmount = data.amount * data.taxRate / 100
        data.amount = Number(data.amount.toFixed(2))
        data.unitPrice = Number(data.unitPrice.toFixed(2))
        data.taxAmount = Number(data.taxAmount.toFixed(2))
        table.updateData()
      }
    }

    watch(getTaxTotalPrice, (newValue) => {
        purchaseRefundFormState.refundLastAmount = newValue
        purchaseRefundFormState.thisRefundAmount = newValue
      discountAmountChange()
    });

    function onSearch() {
      openLinkReceiptModal(true, {
        type: '采购',
        subType: '采购入库',
        title: '选择采购入库单'
      });
    }


    function discountRateChange() {
      const price = getTaxTotalPrice.value;
      const discountLastAmount = Number(price.replace(/,/g, '').replace(amountSymbol.value, ''))
      const discountRate = purchaseRefundFormState.refundOfferRate
      const discountAmount = discountLastAmount * discountRate / 100
      const otherAmount = purchaseRefundFormState.otherAmount

      const lastAmount = Number((discountLastAmount - discountAmount + otherAmount));

      purchaseRefundFormState.thisArrearsAmount = 0
      purchaseRefundFormState.refundOfferAmount = Number(discountAmount.toFixed(2))
      purchaseRefundFormState.refundLastAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(lastAmount), { digits: 2 })}`
      purchaseRefundFormState.thisRefundAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(Number((lastAmount + otherAmount))), { digits: 2 })}`

    }

    function discountAmountChange() {
      const price = getTaxTotalPrice.value;
      const discountLastAmount = Number(price.replace(/,/g, '').replace(amountSymbol.value, ''))
      const discountAmount = purchaseRefundFormState.refundOfferAmount
      const otherAmount = purchaseRefundFormState.otherAmount
      const lastAmount = Number((discountLastAmount - discountAmount));
      purchaseRefundFormState.thisArrearsAmount = 0
      purchaseRefundFormState.refundOfferAmount = Number(discountAmount.toFixed(2))
      purchaseRefundFormState.refundLastAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(lastAmount), { digits: 2 })}`
      purchaseRefundFormState.thisRefundAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(Number((lastAmount + otherAmount))), { digits: 2 })}`
      if (discountLastAmount) {
        const discountRate = discountAmount / discountLastAmount * 100
        purchaseRefundFormState.refundOfferRate = Number(discountRate.toFixed(2))
      } else {
        purchaseRefundFormState.refundOfferRate = 0
      }
    }

    function otherAmountChange() {
      const price = purchaseRefundFormState.refundLastAmount;
      const discountLastAmount = Number(price.replace(/,/g, '').replace(amountSymbol.value, ''))
      const otherAmount = purchaseRefundFormState.otherAmount
      const lastAmount =  Number((discountLastAmount + otherAmount));

      purchaseRefundFormState.thisArrearsAmount = 0
      purchaseRefundFormState.thisRefundAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(lastAmount), { digits: 2 })}`
    }

    function thisPaymentAmountChange() {
      const price = purchaseRefundFormState.refundLastAmount;
      const discountLastAmount = Number(price.replace(/,/g, '').replace(amountSymbol.value, ''))
      const otherAmount = purchaseRefundFormState.otherAmount
      const thisCollectAmount = purchaseRefundFormState.thisRefundAmount
      const lastAmount = Number((discountLastAmount + otherAmount - thisCollectAmount));

      purchaseRefundFormState.thisArrearsAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(lastAmount), { digits: 2 })}`

    }

    const selectAccountChange = (value: number) => {
      if(value == 0) {
        manyAccountBtnStatus.value = true
        openManyAccountModal(true, {
          isUpdate: false,
        });
      } else {
        manyAccountBtnStatus.value = false
      }
    };

    function handleManyAccount() {
      openManyAccountModal(true, {
        isUpdate: false,
        multipleAccounts: multipleAccounts.value
      });
    }

    function handleAccountSuccess(data) {
      multipleAccounts.value = data;
    }

    function handleReceiptSuccess(data) {
      const table = xGrid.value
      if(data && table) {
        purchaseRefundFormState.otherReceipt = data.receiptNumber;
        purchaseRefundFormState.supplierId = data.uid;
        table.remove()
        data.receiptDetailData.forEach(item => {
          const tableData : RowVO = {
            id: item.id,
            warehouseId: item.warehouseId,
            productId: item.productId,
            barCode: item.productBarcode,
            productName: item.productName,
            productModel: item.productModel,
            productStandard: item.productStandard,
            productUnit: item.unit,
            stock: item.stock,
            productNumber: item.productNumber,
            unitPrice: item.unitPrice,
            amount: item.amount,
            taxRate: item.taxRate,
            taxAmount: item.taxAmount,
            taxTotalPrice: item.taxIncludedAmount,
          };
          table.insert(tableData)
        })
      }
    }

    const formatWarehouseId = (value: string) => {
      const item = warehouseList.value.find(item => item.id === value)
      if(item) {
        return item.warehouseName
      }
    }

    return {
      t,
      h,
      formRef,
      AccountBookTwoTone,
      open,
      checkFlag,
      isCanCheck,
      isTenant,
      scanStatus,
      confirmLoading,
      handleCancelModal,
      openAddEditModal,
      purchaseRefundFormState,
      title,
      width,
      moreStatus,
      addDefaultRowNum,
      fileList,
      payTypeList,
      minWidth,
      model,
      labelCol,
      wrapperCol,
      refKey,
      activeKey,
      scanPressEnter,
      scanEnter,
      stopScan,
      supplierList,
      showScanButton,
      showScanPressEnter,
      supplierModal,
      addSupplier,
      accountModal,
      addAccount,
      accountList,
      handleOk,
      beforeUpload,
      uploadFiles,
      gridOptions,
      xGrid,
      tableData,
      getTaxTotalPrice,
      selectProductModal,
      openProductModal,
      productModal,
      handleCheckSuccess,
      addRowData,
      deleteRowData,
      barCode,
      productNumberChange,
      unitPriceChange,
      amountChange,
      taxRateChange,
      taxTotalPriceChange,
      discountRateChange,
      discountAmountChange,
      selectAccountChange,
      manyAccountBtnStatus,
      handleManyAccount,
      multipleAccountModal,
      handleAccountSuccess,
      otherAmountChange,
      thisPaymentAmountChange,
      onSearch,
      linkReceiptModal,
      openLinkReceiptModal,
      handleReceiptSuccess,
      productList,
      productLabelList,
      warehouseLabelList,
      selectBarCode,
      handleAccountModalSuccess,
      handleSupplierModalSuccess,
      formatWarehouseId,
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