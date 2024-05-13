<template>
  <a-modal
      :title="title"
      :width="width"
      :confirm-loading="confirmLoading"
      v-bind:prefixNo="prefixNo"
      :id="prefixNo"
      :forceRender="true"
      :keyboard="true"
      switchHelp
      switchFullscreen
      @cancel="handleCancelModal"
      v-model:open="open"
      style="left: 5%; height: 95%;">
    <template #footer>
      <a-button @click="handleCancelModal" v-text="t('sales.shipments.form.cancel')" />
      <a-button v-if="checkFlag && isCanCheck" :loading="confirmLoading" @click="handleOk(1)" v-text="t('sales.shipments.form.saveApprove')" />
      <a-button type="primary" :loading="confirmLoading" @click="handleOk(0)" v-text="t('sales.shipments.form.save')" />
      <!--发起多级审核-->
      <a-button v-if="!checkFlag" @click="" type="primary">提交流程</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="saleShipmentsFormState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-input v-model:value="saleShipmentsFormState.id" v-show="false"/>
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.form.customer')" data-step="1"
                         data-title="客户卡号"
                         :rules="[{ required: true}]">
              <a-select v-model:value="saleShipmentsFormState.customerId"
                        :dropdownMatchSelectWidth="false" showSearch optionFilterProp="children"
                        :placeholder="t('sales.shipments.form.inputCustomer')"
                        :options="customerList.map(item => ({ value: item.id, label: item.customerName }))">
                <template #dropdownRender="{ menuNode: menu }">
                  <v-nodes :vnodes="menu"/>
                  <a-divider style="margin: 4px 0"/>
                  <div style="padding: 4px 8px; cursor: pointer; color: #1c1e21"
                       @mousedown="e => e.preventDefault()" @click="addCustomer">
                    <plus-outlined/>
                    {{ t('sales.shipments.form.inputCustomer') }}
                  </div>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.form.receiptDate')" :rules="[{ required: true}]">
              <a-date-picker v-model:value="saleShipmentsFormState.receiptDate" show-time :placeholder="t('sales.shipments.form.inputReceiptDate')" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.form.receiptNumber')" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号">
              <a-input v-model:value="saleShipmentsFormState.receiptNumber" :readOnly="true"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.form.relatedOrder')" data-step="3"
                         data-title="关联订单"
                         data-intro="">
              <a-input-search :readonly="true" :placeholder="t('sales.shipments.form.inputRelatedOrder')" v-model:value="saleShipmentsFormState.otherReceipt" @search="onSearch"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="24" :md="12" :sm="24" style="margin-bottom: 150px;">
            <div class="table-operations">
              <vxe-grid ref='xGrid' v-bind="gridOptions">
                <template #toolbar_buttons="{ row }">
                  <a-button v-if="showScanButton" type="primary"  @click="scanEnter" style="margin-right: 10px" v-text="t('sales.shipments.form.scanCodeData')"/>
                  <a-input v-if="showScanPressEnter" :placeholder="t('sales.shipments.form.scanCodeTip')" style="width: 150px; margin-right: 10px" v-model:value="barCode"
                           @pressEnter="scanPressEnter" ref="scanBarCode"/>
                  <a-button v-if="showScanPressEnter" style="margin-right: 10px" @click="stopScan" v-text="t('sales.shipments.form.collapseScanCode')"/>
                  <a-button @click="productModal" style="margin-right: 10px" v-text="t('sales.shipments.form.addProduct')"/>
                  <a-button @click="addRowData" style="margin-right: 10px" v-text="t('sales.shipments.form.insertRow')"/>
                  <a-button @click="deleteRowData" style="margin-right: 10px" v-text="t('sales.shipments.form.deleteRow')"/>
                </template>
                <template #warehouse_default="{ row }">
                  <span>{{ formatWarehouseId(row.warehouseId) }}</span>
                </template>
                <template #warehouse_edit="{ row }">
                  <vxe-select v-model="row.warehouseId" :placeholder="t('sales.shipments.form.noticeEight')" @change="selectBarCode" :options="warehouseLabelList" clearable filterable></vxe-select>
                </template>
                <template #barCode_edit="{ row }">
                  <vxe-select v-model="row.barCode" :placeholder="t('sales.shipments.form.table.inputBarCode')" @change="selectBarCode" :options="productLabelList" clearable filterable></vxe-select>
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
                  <a-textarea :rows="1" :placeholder="t('sales.shipments.form.table.inputRemark')" v-model:value="saleShipmentsFormState.remark"
                              style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.form.table.discount')" data-step="2"
                             data-title="优惠率">
                  <a-input-number @change="discountRateChange" addon-after="%" v-model:value="saleShipmentsFormState.collectOfferRate"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.form.table.collectionDiscount')" data-step="2"
                             data-title="收款优惠">
                  <a-input-number @change="discountAmountChange" v-model:value="saleShipmentsFormState.collectOfferAmount"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.form.table.discountAmount')" data-step="2"
                             data-title="优惠后金额">
                  <a-input v-model:value="saleShipmentsFormState.collectOfferLastAmount" :readOnly="true"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.view.otherFees')" data-step="2"
                             data-title="其他费用">
                  <a-input-number @change="otherAmountChange" v-model:value="saleShipmentsFormState.otherAmount" :readOnly="true"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.form.table.account')" data-step="2"
                             data-title="结算账户"
                             :rules="[{ required: true}]">
                  <a-select v-model:value="saleShipmentsFormState.accountId"
                            :placeholder="t('sales.shipments.form.table.inputAccount')"
                            :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"
                            @change="selectAccountChange">
                    <template #dropdownRender="{ menuNode: menu }">
                      <v-nodes :vnodes="menu"/>
                      <a-divider style="margin: 4px 0"/>
                      <div style="padding: 4px 8px; cursor: pointer; color: #1c1e21"
                           @mousedown="e => e.preventDefault()" @click="addAccount">
                        <plus-outlined/>
                        {{ t('sales.shipments.form.addSettlementAccount') }}
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
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.view.thisTimeCollectAmount')" data-step="2"
                             data-title="本次收款"
                             :rules="[{ required: true}]">
                  <a-input-number placeholder="t('sales.shipments.form.noticeSeven')" @change="thisCollectAmountChange" v-model:value="saleShipmentsFormState.thisCollectAmount"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24" >
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.view.thisTimeArrearsAmount')" data-step="2"
                             data-title="本次欠款">
                  <a-input  :readOnly="true" v-model:value="saleShipmentsFormState.thisArrearsAmount"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.view.salesPerson')" data-step="3"
                             data-title="销售人员"
                             data-intro="">
                  <a-select v-model:value="saleShipmentsFormState.operatorIds"
                            :placeholder="t('sales.shipments.form.inputSalesPerson')"
                            mode="multiple"
                            :options="salePersonalList.map(item => ({ value: item.id, label: item.name }))"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('sales.shipments.form.table.annex')" data-step="9"
                             data-title="附件"
                             data-intro="可以上传与单据相关的图片、文档，支持多个文件">
                  <a-upload
                      v-model:file-list="fileList"
                      :custom-request="uploadFiles"
                      :before-upload="beforeUpload"
                      multiple>
                    <a-button>
                      <upload-outlined/>
                      {{ t('sales.shipments.form.table.uploadAnnex') }}
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
  <CustomerModal @register="customerModal" @success="handleCustomerModalSuccess"/>
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
  saleShipmentsFormState,
  RowVO,
  xGrid,
  tableData,
  gridOptions, getTaxTotalPrice, formState,
} from '/src/views/sales/model/addEditModel';
import {getCustomerList} from "@/api/basic/customer";
import {CustomerResp} from "@/api/basic/model/customerModel";
import {getOperatorList} from "@/api/basic/operator";
import {OperatorResp} from "@/api/basic/model/operatorModel";
import CustomerModal from "@/views/basic/customer/components/CustomerModal.vue";
import {useModal} from "@/components/Modal";
import {generateId, uploadOss} from "@/api/basic/common";
import FinancialAccountModal from "@/views/basic/settlement-account/components/FinancialAccountModal.vue";
import {VXETable, VxeGrid, VxeInput, VxeButton} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdateSaleShipments, getSaleShipmentsDetail} from "@/api/sale/shipments"
import SelectProductModal from "@/views/product/info/components/SelectProductModal.vue"
import {getProductSkuByBarCode, getProductStockSku} from "@/api/product/product";
import {getWarehouseList} from "@/api/basic/warehouse";
import {AddOrUpdateReceiptReq, SalesData} from "@/api/sale/model/orderModel";
import {FileData} from '/@/api/retail/model/shipmentsModel';
import {getAccountList} from "@/api/financial/account";
import {AccountResp} from "@/api/financial/model/accountModel";
import XEUtils from "xe-utils";
import MultipleAccountsModal from "@/views/basic/settlement-account/components/MultipleAccountsModal.vue";
import LinkReceiptModal from "@/views/receipt/LinkReceiptModal.vue";
import {ProductStockSkuResp} from "@/api/product/model/productModel";
import {WarehouseResp} from "@/api/basic/model/warehouseModel";
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
  name: 'SaleShipmentsAddEditModal',
  emits: ['success', 'cancel', 'error'],
  components: {
    MultipleAccountsModal,
    FinancialAccountModal,
    LinkReceiptModal,
    CustomerModal,
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
    'upload-outlined': UploadOutlined,
    'plus-outlined': PlusOutlined,
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
    const prefixNo = ref('LSTH');
    const fileList = ref<FileData[]>([]);
    const payTypeList = ref<any>([]);
    const minWidth = ref(1100);
    const model = ref({});
    const barCode = ref('');
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
    const customerList = ref<CustomerResp[]>([])
    const salePersonalList = ref<OperatorResp[]>([]);
    const accountList = ref<AccountResp[]>([]);
    const warehouseList = ref<WarehouseResp[]>([]);
    const multipleAccounts = ref();
    const [customerModal, {openModal}] = useModal();
    const [accountModal, {openModal: openAccountModal}] = useModal();
    const [selectProductModal, {openModal: openProductModal}] = useModal();
    const [multipleAccountModal, {openModal: openManyAccountModal}] = useModal();
    const [linkReceiptModal, {openModal: openLinkReceiptModal}] = useModal();
    const productList = ref<ProductStockSkuResp[]>([]);
    const productLabelList = ref<any[]>([]);
    const warehouseLabelList =  ref<any[]>([]);
    function handleCancelModal() {
      clearData();
      open.value = false;
      context.emit('cancel');
    }
    const amountSymbol = ref<string>('')
    const localeStore = useLocaleStore().getLocale;
    if(localeStore === 'zh_CN') {
      amountSymbol.value = '￥'
    } else if (localeStore === 'en') {
      amountSymbol.value = '$'
    }

    function openAddEditModal(id: string | undefined) {
      open.value = true
      loadCustomerList();
      loadSalePersonalList();
      loadWarehouseList();
      loadAccountList();
      loadProductSku();
      if (id) {
        title.value = t('sales.shipments.editShipments')
        loadSaleShipmentDetail(id);
      } else {
        title.value = t('sales.shipments.addShipments')
        loadGenerateId();
        saleShipmentsFormState.receiptDate = dayjs(new Date());
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
          saleShipmentsFormState.warehouseId = defaultWarehouse.id
        } else {
          saleShipmentsFormState.warehouseId = res.data[0].id
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
          if(barCodeColumn) {
            const product = productList.value.find(item => {
              return item.productBarcode === barCodeColumn;
            });
            if (product) {
              selectRow.row.productId = product.productId
              selectRow.row.productName = product.productName
              selectRow.row.productStandard = product.productStandard
              selectRow.row.productUnit = product.productUnit
              selectRow.row.stock = product.currentStock
              selectRow.row.unitPrice = product.salePrice
              selectRow.row.taxTotalPrice = product.salePrice
              selectRow.row.amount = product.salePrice
              selectRow.row.taxRate = 0
              selectRow.row.productNumber = 1
              table.updateData(selectRow.rowIndex, selectRow.row)
            } else {
              createMessage.warn(t('sales.shipments.form.noticeFour'))
            }
          }
        }
      }
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

    function loadCustomerList() {
      getCustomerList().then(res => {
        customerList.value = res.data
      })
    }

    function handleCustomerModalSuccess() {
      loadCustomerList();
    }

    function loadSalePersonalList() {
      getOperatorList("销售员").then(res => {
        salePersonalList.value = res.data
      })
    }

    function loadGenerateId() {
      generateId("销售出库").then(res => {
        saleShipmentsFormState.receiptNumber = res.data
      })
    }

    async function loadSaleShipmentDetail(id) {
      clearData();
      const result = await getSaleShipmentsDetail(id)
      if(result) {
        const data = result.data
        saleShipmentsFormState.id = id
        saleShipmentsFormState.customerId = data.customerId
        saleShipmentsFormState.receiptDate = dayjs(data.receiptDate);
        saleShipmentsFormState.receiptNumber = data.receiptNumber
        saleShipmentsFormState.remark = data.remark
        saleShipmentsFormState.operatorIds = data.operatorIds
        saleShipmentsFormState.collectOfferRate = data.collectOfferRate
        saleShipmentsFormState.collectOfferAmount = data.collectOfferAmount
        saleShipmentsFormState.collectOfferLastAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(data.collectOfferLastAmount), { digits: 2 })}`
        saleShipmentsFormState.otherReceipt = data.otherReceipt
        saleShipmentsFormState.otherAmount = data.otherAmount
        saleShipmentsFormState.thisCollectAmount = data.thisCollectAmount
        saleShipmentsFormState.thisArrearsAmount = data.thisArrearsAmount
        // 判断多账户渲染
        if(data.multipleAccountAmounts.length > 0 && data.multipleAccountIds.length > 0) {
          manyAccountBtnStatus.value = true
          saleShipmentsFormState.accountId = 0
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
          saleShipmentsFormState.accountId = data.accountId
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
      getProductSkuByBarCode(barCode.value, saleShipmentsFormState.warehouseId).then(res => {
        const {columns} = gridOptions
        if (columns) {
          const {data} = res
          if (data) {
            const sale: SalesData = data
            const table = xGrid.value
            if (table) {
              //根据productExtendPrice.id判断表格中如果是同一个商品，数量加1 否则新增一行
              const tableData = table.getTableData().tableData
              const index = tableData.findIndex(item => item.barCode === sale.barCode)
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
                  warehouseId: sale.warehouseId,
                  productId: sale.productId,
                  barCode: sale.barCode,
                  productName: sale.productName,
                  productStandard: sale.productStandard,
                  productUnit: sale.productUnit,
                  stock: sale.stock,
                  productNumber: 1,
                  amount: sale.salePrice,
                  unitPrice: sale.salePrice,
                  taxTotalPrice: sale.salePrice,
                  taxRate: 0,
                  taxAmount: 0,
                };
                table.insert(tableData)
              }
              saleShipmentsFormState.discountLastAmount = tableData.taxTotalPrice.value
              saleShipmentsFormState.thisCollectAmount = tableData.taxTotalPrice.value
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

    function addCustomer() {
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
      if (!saleShipmentsFormState.customerId) {
        createMessage.warn(t('sales.shipments.form.inputCustomer'));
        return;
      }
      if (saleShipmentsFormState.accountId === 0) {
        if(!multipleAccounts.value.accountOne && !multipleAccounts.value.accountTwo) {
          createMessage.warn(t('sales.shipments.form.noticeFive'));
          return;
        }
        if(!multipleAccounts.value.accountPriceOne && !multipleAccounts.value.accountPriceTwo) {
          createMessage.warn(t('sales.shipments.form.noticeSix'));
          return;
        }
      } else if (!saleShipmentsFormState.accountId) {
        createMessage.warn(t('sales.shipments.form.table.inputAccount'));
        return;
      }
      if (!saleShipmentsFormState.thisCollectAmount) {
        createMessage.warn(t('sales.shipments.form.noticeSeven'));
        return;
      }
      if(table) {
        const insertRecords = table.getInsertRecords()
        if(insertRecords.length === 0) {
          createMessage.warn(t('sales.shipments.form.addRowData'))
          return;
        }
        const isBarCodeEmpty = insertRecords.some(item => !item.barCode)
        if(isBarCodeEmpty) {
          createMessage.warn(t('sales.shipments.form.noticeOne'))
          return;
        }
        const isWarehouseEmpty = insertRecords.some(item => !item.warehouseId)
        if(isWarehouseEmpty) {
          createMessage.warn(t('sales.shipments.form.noticeEight'))
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
        createMessage.info(t('sales.shipments.form.table.barCode') +tableDataNotEnoughBarCodeStr + t('sales.shipments.form.noticeNine'))
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
        const data: SalesData = {
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
      if (saleShipmentsFormState.accountId === 0) {
        saleShipmentsFormState.accountId = undefined
        saleShipmentsFormState.multipleAccountIds = []
        saleShipmentsFormState.multipleAccountAmounts = []
        if(multipleAccounts.value.accountOne) {
          saleShipmentsFormState.multipleAccountIds.push(multipleAccounts.value.accountOne)
        }
        if(multipleAccounts.value.accountTwo){
          saleShipmentsFormState.multipleAccountIds.push(multipleAccounts.value.accountTwo)
        }
        if(multipleAccounts.value.accountThree) {
          saleShipmentsFormState.multipleAccountIds.push(multipleAccounts.value.accountThree)
        }
        if(multipleAccounts.value.accountOne) {
          saleShipmentsFormState.multipleAccountAmounts.push(multipleAccounts.value.accountPriceOne)
        }
        if(multipleAccounts.value.accountTwo){
          saleShipmentsFormState.multipleAccountAmounts.push(multipleAccounts.value.accountPriceTwo)
        }
        if(multipleAccounts.value.accountThree) {
          saleShipmentsFormState.multipleAccountAmounts.push(multipleAccounts.value.accountPriceThree)
        }
      } else {
        saleShipmentsFormState.multipleAccountIds = []
        saleShipmentsFormState.multipleAccountAmounts = []
      }

      saleShipmentsFormState.thisCollectAmount = saleShipmentsFormState.thisCollectAmount.toString().replace(/,/g, '').replace(amountSymbol.value, '');
      saleShipmentsFormState.collectOfferLastAmount = saleShipmentsFormState.collectOfferLastAmount.replace(/,/g, '').replace(amountSymbol.value, '')
      saleShipmentsFormState.thisArrearsAmount = saleShipmentsFormState.thisArrearsAmount.toString().replace(/,/g, '').replace(amountSymbol.value, '')
      const params: AddOrUpdateReceiptReq = {
        ...saleShipmentsFormState,
        tableData: dataArray,
        files: files,
        status: type,
      }

      const result = await addOrUpdateSaleShipments(params)
      if (result.code === 'S0004' || result.code === 'S0005') {
        handleCancelModal();
      }
    }

    function clearData() {
      saleShipmentsFormState.id = undefined
      saleShipmentsFormState.receiptNumber = ''
      saleShipmentsFormState.customerId = ''
      saleShipmentsFormState.collectOfferRate = 0
      saleShipmentsFormState.collectOfferAmount = 0
      saleShipmentsFormState.collectOfferLastAmount = 0
      saleShipmentsFormState.otherAmount = 0
      saleShipmentsFormState.thisCollectAmount = 0
      saleShipmentsFormState.thisArrearsAmount = 0
      saleShipmentsFormState.accountId = undefined
      saleShipmentsFormState.operatorIds = []
      saleShipmentsFormState.otherReceipt = ''
      barCode.value = ''
      saleShipmentsFormState.remark = ''
      fileList.value = []
      multipleAccounts.value = {}
      const table = xGrid.value
      if(table) {
        table.remove()
      }
      saleShipmentsFormState.receiptDate = undefined
    }

    function beforeUpload(file: any) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        createMessage.error(`${file.name}，` + t('sales.shipments.form.noticeThree'));
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
          item.unitPrice = item.salePrice
          item.productNumber = 1
          item.amount = item.salePrice * item.productNumber
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
        saleShipmentsFormState.collectOfferLastAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(total), { digits: 2 })}`
        saleShipmentsFormState.thisCollectAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(total), { digits: 2 })}`
      }
    }

    function addRowData() {
      const table = xGrid.value
      const defaultWarehouse = warehouseList.value.find(item => item.isDefault === 1)
      const warehouseId = defaultWarehouse ? defaultWarehouse.id : warehouseList.value[0].id
      if(table) {
        table.insert({warehouseId: warehouseId})
      }
    }

    async function deleteRowData() {
      const type = await VXETable.modal.confirm(t('sales.shipments.form.noticeTwo'))
      const table = xGrid.value
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

    watch(getTaxTotalPrice, (newValue, oldValue) => {
        saleShipmentsFormState.collectOfferLastAmount = newValue
        saleShipmentsFormState.thisCollectAmount = newValue
        discountAmountChange()
    });

    function discountRateChange() {
      const price = getTaxTotalPrice.value;
      const discountLastAmount = Number(price.replace(/,/g, '').replace(amountSymbol.value, ''))
      const discountRate = saleShipmentsFormState.collectOfferRate
      const discountAmount = discountLastAmount * discountRate / 100
      const otherAmount = saleShipmentsFormState.otherAmount

      const lastAmount = Number((discountLastAmount - discountAmount + otherAmount));

      saleShipmentsFormState.thisArrearsAmount = 0
      saleShipmentsFormState.collectOfferAmount = Number(discountAmount.toFixed(2))
      saleShipmentsFormState.collectOfferLastAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(lastAmount), { digits: 2 })}`
      saleShipmentsFormState.thisCollectAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(Number((lastAmount + otherAmount))), { digits: 2 })}`

    }

    function discountAmountChange() {
      const price = getTaxTotalPrice.value;
      const discountLastAmount = Number(price.replace(/,/g, '').replace(amountSymbol.value, ''))
      const discountAmount = saleShipmentsFormState.collectOfferAmount
      const otherAmount = saleShipmentsFormState.otherAmount
      const lastAmount = Number((discountLastAmount - discountAmount));
      saleShipmentsFormState.thisArrearsAmount = 0
      saleShipmentsFormState.collectOfferAmount = Number(discountAmount.toFixed(2))
      saleShipmentsFormState.collectOfferLastAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(lastAmount), { digits: 2 })}`
      saleShipmentsFormState.thisCollectAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(Number((lastAmount + otherAmount))), { digits: 2 })}`
      if (discountLastAmount) {
        const discountRate = discountAmount / discountLastAmount * 100
        saleShipmentsFormState.collectOfferRate = Number(discountRate.toFixed(2))
      } else {
        saleShipmentsFormState.collectOfferRate = 0
      }
    }

    function otherAmountChange() {
      // 计算本次收款(thisCollectAmount) = 优惠金额 + 其他费用
      const price = saleShipmentsFormState.collectOfferLastAmount;
      const discountLastAmount = Number(price.replace(/,/g, '').replace(amountSymbol.value, ''))
      const otherAmount = saleShipmentsFormState.otherAmount
      const lastAmount =  Number((discountLastAmount + otherAmount));
      saleShipmentsFormState.thisArrearsAmount = 0
      saleShipmentsFormState.thisCollectAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(lastAmount), { digits: 2 })}`
    }

    function thisCollectAmountChange() {
      const price = saleShipmentsFormState.collectOfferLastAmount;
      const discountLastAmount = Number(price.replace(/,/g, '').replace(amountSymbol.value, ''))
      const otherAmount = saleShipmentsFormState.otherAmount
      const thisCollectAmount = saleShipmentsFormState.thisCollectAmount
      const lastAmount = Number((discountLastAmount + otherAmount - thisCollectAmount));

      saleShipmentsFormState.thisArrearsAmount = amountSymbol.value + `${XEUtils.commafy(XEUtils.toNumber(lastAmount), { digits: 2 })}`
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

    function onSearch() {
      openLinkReceiptModal(true, {
        type: '销售',
        subType: '销售订单',
        title: '选择销售订单'
      });
    }

    function handleReceiptSuccess(data) {
      console.info(data)
      const table = xGrid.value
      if(data && table) {
        saleShipmentsFormState.otherReceipt = data.receiptNumber;
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
        saleShipmentsFormState.customerId = data.receiptDetailData[0].memberId
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
      AccountBookTwoTone,
      open,
      checkFlag,
      isCanCheck,
      isTenant,
      scanStatus,
      confirmLoading,
      handleCancelModal,
      openAddEditModal,
      saleShipmentsFormState,
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
      scanPressEnter,
      scanEnter,
      stopScan,
      customerList,
      salePersonalList,
      showScanButton,
      showScanPressEnter,
      customerModal,
      addCustomer,
      accountModal,
      addAccount,
      accountList,
      handleOk,
      beforeUpload,
      uploadFiles,
      gridOptions,
      xGrid,
      tableData,
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
      otherAmountChange,
      thisCollectAmountChange,
      manyAccountBtnStatus,
      handleManyAccount,
      multipleAccountModal,
      handleAccountSuccess,
      onSearch,
      linkReceiptModal,
      openLinkReceiptModal,
      handleReceiptSuccess,
      productList,
      productLabelList,
      selectBarCode,
      handleCustomerModalSuccess,
      handleAccountModalSuccess,
      formatWarehouseId,
      warehouseLabelList,
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