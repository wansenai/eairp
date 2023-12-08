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
      <a-button v-if="checkFlag && isCanCheck" :loading="confirmLoading" @click="handleOk(1)">保存并审核</a-button>
      <a-button type="primary" :loading="confirmLoading" @click="handleOk(0)">保存</a-button>
      <!--发起多级审核-->
      <a-button v-if="!checkFlag" @click="" type="primary">提交流程</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="purchaseOrderFormState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-input v-model:value="purchaseOrderFormState.id" v-show="false"/>
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="供应商" data-step="1"
                         data-title="供应商" :rules="[{ required: true}]">
              <a-select v-model:value="purchaseOrderFormState.supplierId"
                        :dropdownMatchSelectWidth="false" showSearch optionFilterProp="children"
                        placeholder="请选择供应商"
                        :options="Array.isArray(supplierList) ? supplierList.map(item => ({ value: item.id, label: item.supplierName })) : []">
                <template #dropdownRender="{ menuNode: menu }">
                  <v-nodes :vnodes="menu"/>
                  <a-divider style="margin: 4px 0"/>
                  <div style="padding: 4px 8px; cursor: pointer; color: #1c1e21"
                       @mousedown="e => e.preventDefault()" @click="addSupplier">
                    <plus-outlined/>
                    新增供应商
                  </div>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据日期" :rules="[{ required: true}]">
              <a-date-picker v-model:value="purchaseOrderFormState.receiptDate" show-time placeholder="选择时间" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据编号" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号">
              <a-input placeholder="请输入单据编号" v-model:value="purchaseOrderFormState.receiptNumber" :readOnly="true"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="24" :md="12" :sm="24" style="margin-bottom: 150px;">
            <div class="table-operations">
              <vxe-grid ref='xGrid' v-bind="orderGridOptions">
                <template #toolbar_buttons="{ row }">
                  <a-button v-if="showScanButton" type="primary"  @click="scanEnter" style="margin-right: 10px">扫条码录入数据</a-button>
                  <a-input v-if="showScanPressEnter" placeholder="鼠标点击此处扫条码" style="width: 150px; margin-right: 10px" v-model:value="barCode"
                           @pressEnter="scanPressEnter" ref="scanBarCode"/>
                  <a-button v-if="showScanPressEnter" style="margin-right: 10px" @click="stopScan">收起扫码</a-button>
                  <a-button @click="productModal" style="margin-right: 10px">选择添加采购商品</a-button>
                  <a-button @click="" style="margin-right: 10px">历史单据</a-button>
                  <a-button @click="addRowData" style="margin-right: 10px">添加一行</a-button>
                  <a-button @click="deleteRowData" style="margin-right: 10px">删除选中行</a-button>
                </template>
                <template #barCode_edit="{ row }">
                  <vxe-select v-model="row.barCode" placeholder="输入商品条码" @change="selectBarCode" :options="productLabelList" clearable filterable></vxe-select>
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
                  <vxe-input v-model="row.taxRate"  @change="taxRateChange"></vxe-input>
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
                  <a-textarea :rows="1" placeholder="请输入备注" v-model:value="purchaseOrderFormState.remark"
                              style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="优惠率" data-step="2"
                             data-title="优惠率">
                  <a-input-number placeholder="请输入优惠率" @change="discountRateChange" v-model:value="purchaseOrderFormState.discountRate"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="付款优惠" data-step="2"
                             data-title="付款优惠">
                  <a-input-number placeholder="请输入收款优惠" @change="discountAmountChange" v-model:value="purchaseOrderFormState.discountAmount"/>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="优惠后金额" data-step="2"
                             data-title="优惠后金额">
                  <a-input placeholder="请输入优惠后金额" v-model:value="purchaseOrderFormState.discountLastAmount" :readOnly="true"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="结算账户" data-step="2"
                             data-title="结算账户">
                  <a-select v-model:value="purchaseOrderFormState.accountId"
                            placeholder="请选择结算账户"
                            :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"
                            @change="selectAccountChange"/>
                </a-form-item>
              </a-col>
              <a-col style="margin-left: -25px">
                <a-tooltip title="多账户明细">
                  <a-button type="default" :icon="h(AccountBookTwoTone)" style="font-size: small; margin-left: 5px" v-show="manyAccountBtnStatus" @click="handleManyAccount"/>
                </a-tooltip>
              </a-col>
              <a-col :lg="6" :md="12" :sm="24" >
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="支付定金" data-step="2"
                             data-title="支付定金">
                  <a-input-number placeholder="请输入支付定金" v-model:value="purchaseOrderFormState.deposit"/>
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
  <SupplierModal @register="supplierModal" @success="handleSupplierModalSuccess"/>
  <FinancialAccountModal @register="accountModal"/>
  <SelectProductModal @register="selectProductModal" @handleCheckSuccess="handleCheckSuccess"/>
  <MultipleAccountsModal @register="multipleAccountModal" @handleAccountSuccess="handleAccountSuccess" />
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
  purchaseOrderFormState,
  RowVO,
  xGrid,
  tableData,
  orderGridOptions,
  getTaxTotalPrice,
} from '/src/views/purchase/model/addEditModel';
import {useModal} from "@/components/Modal";
import {generateId, uploadOss} from "@/api/basic/common";
import FinancialAccountModal from "@/views/basic/settlement-account/components/FinancialAccountModal.vue";
import {VXETable, VxeGrid, VxeInput, VxeButton} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdatePurchaseOrder, getPurchaseOrderDetail} from "@/api/purchase/order"
import SupplierModal from "@/views/basic/supplier/components/SupplierModal.vue"
import SelectProductModal from "@/views/product/info/components/SelectProductModal.vue"
import {getProductSkuByBarCode, getProductStockSku} from "@/api/product/product";
import {getWarehouseList} from "@/api/basic/warehouse";
import {AddOrUpdateReceiptReq, PurchaseData} from "@/api/purchase/model/orderModel";
import {FileData} from '/@/api/retail/model/shipmentsModel';
import {getAccountList} from "@/api/financial/account";
import {AccountResp} from "@/api/financial/model/accountModel";
import XEUtils from "xe-utils";
import MultipleAccountsModal from "@/views/basic/settlement-account/components/MultipleAccountsModal.vue";
import {SupplierResp} from "@/api/basic/model/supplierModel";
import {addSupplier, getSupplierList} from "@/api/basic/supplier";
import {ProductStockSkuResp} from "@/api/product/model/productModel";
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
  name: 'AddEditPurchaseOrderModal',
  methods: {addSupplier},
  emits: ['success', 'cancel', 'error'],
  components: {
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
    const multipleAccounts = ref();
    const [supplierModal, {openModal}] = useModal();
    const [accountModal, {openModal: openAccountModal}] = useModal();
    const [selectProductModal, {openModal: openProductModal}] = useModal();
    const [multipleAccountModal, {openModal: openManyAccountModal}] = useModal();
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
      loadSupplierList();
      loadWarehouseList();
      loadAccountList();
      loadProductSku();
      if (id) {
        title.value = '编辑-采购订单'
        loadRefundDetail(id);
      } else {
        title.value = '新增-采购订单'
        loadGenerateId();
        purchaseOrderFormState.receiptDate = dayjs(new Date());
      }
    }

    function loadWarehouseList() {
      getWarehouseList().then(res => {
        const data = res.data
        if(data) {
          const defaultWarehouse = res.data.find(item => item.isDefault === 1)
          if(defaultWarehouse) {
            purchaseOrderFormState.warehouseId = defaultWarehouse.id
          } else {
            purchaseOrderFormState.warehouseId = res.data[0].id
          }
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

    function loadSupplierList() {
      getSupplierList().then(res => {
        supplierList.value = res.data
      })
    }

    function handleSupplierModalSuccess() {
      loadSupplierList();
    }

    function loadGenerateId() {
      generateId("采购单").then(res => {
        purchaseOrderFormState.receiptNumber = res.data
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
        const {columns} = orderGridOptions
        if (columns) {
          const barCodeColumn = selectRow.row.barCode
          if(barCodeColumn) {
            const product = productList.value.find(item => {
              return item.productBarcode === barCodeColumn;
            });
            if (product) {
              selectRow.row.productId = product.productId
              // 这里默认加载默认仓库
              selectRow.row.warehouseId = purchaseOrderFormState.warehouseId
              selectRow.row.productName = product.productName
              selectRow.row.productStandard = product.productStandard
              selectRow.row.productUnit = product.productUnit
              selectRow.row.stock = product.currentStock
              selectRow.row.unitPrice = product.purchasePrice
              selectRow.row.taxTotalPrice = product.purchasePrice
              selectRow.row.amount = product.purchasePrice
              selectRow.row.taxRate = 0
              selectRow.row.productNumber = 1
              table.updateData(selectRow.rowIndex, selectRow.row)
            } else {
              createMessage.warn("该条码查询不到商品信息")
            }
          }
        }
      }
    }

    async function loadRefundDetail(id) {
      clearData();
      const result = await getPurchaseOrderDetail(id)
      if(result) {
        const data = result.data
        purchaseOrderFormState.id = id
        purchaseOrderFormState.supplierId = data.supplierId
        purchaseOrderFormState.receiptDate = dayjs(data.receiptDate);
        purchaseOrderFormState.receiptNumber = data.receiptNumber
        purchaseOrderFormState.remark = data.remark
        purchaseOrderFormState.discountRate = data.discountRate
        purchaseOrderFormState.discountAmount = data.discountAmount
        purchaseOrderFormState.discountLastAmount = `￥${XEUtils.commafy(XEUtils.toNumber(data.discountLastAmount), { digits: 2 })}`
        purchaseOrderFormState.deposit = data.deposit
        // 判断多账户渲染
        if(data.multipleAccountAmounts.length > 0 && data.multipleAccountIds.length > 0) {
          manyAccountBtnStatus.value = true
          purchaseOrderFormState.accountId = 0
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
          purchaseOrderFormState.accountId = data.accountId
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
      getProductSkuByBarCode(barCode.value, purchaseOrderFormState.warehouseId).then(res => {
        const {columns} = orderGridOptions
        if (columns) {
          const {data} = res
          if (data) {
            const purchase : PurchaseData = data
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
              purchaseOrderFormState.discountLastAmount = getTaxTotalPrice.value
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
      if (!purchaseOrderFormState.supplierId) {
        createMessage.warn('请选择供应商');
        return;
      }
      if(table) {
        const insertRecords = table.getInsertRecords()
        if(insertRecords.length === 0) {
          createMessage.warn("请添加一行数据")
          return;
        }
        const isBarCodeEmpty = insertRecords.some(item => !item.barCode)
        if(isBarCodeEmpty) {
          createMessage.warn("请录入条码或者选择产品")
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
        const data: PurchaseData = {
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
      if (purchaseOrderFormState.accountId === 0) {
        purchaseOrderFormState.accountId = undefined
        purchaseOrderFormState.multipleAccountIds = []
        purchaseOrderFormState.multipleAccountAmounts = []
        if(multipleAccounts.value.accountOne) {
          purchaseOrderFormState.multipleAccountIds.push(multipleAccounts.value.accountOne)
        }
        if(multipleAccounts.value.accountTwo){
          purchaseOrderFormState.multipleAccountIds.push(multipleAccounts.value.accountTwo)
        }
        if(multipleAccounts.value.accountThree) {
          purchaseOrderFormState.multipleAccountIds.push(multipleAccounts.value.accountThree)
        }
        if(multipleAccounts.value.accountOne) {
          purchaseOrderFormState.multipleAccountAmounts.push(multipleAccounts.value.accountPriceOne)
        }
        if(multipleAccounts.value.accountTwo){
          purchaseOrderFormState.multipleAccountAmounts.push(multipleAccounts.value.accountPriceTwo)
        }
        if(multipleAccounts.value.accountThree) {
          purchaseOrderFormState.multipleAccountAmounts.push(multipleAccounts.value.accountPriceThree)
        }
      } else {
        purchaseOrderFormState.multipleAccountIds = []
        purchaseOrderFormState.multipleAccountAmounts = []
      }

      purchaseOrderFormState.discountLastAmount = purchaseOrderFormState.discountLastAmount.replace(/,/g, '').replace(/￥/g, '')
      const params: AddOrUpdateReceiptReq = {
        ...purchaseOrderFormState,
        tableData: dataArray,
        files: files,
        status: type,
      }

      console.info(params)
      const result = await addOrUpdatePurchaseOrder(params)
      if (result.code === 'P0015' || result.code === 'P0016') {
        handleCancelModal();
      }
      clearData();
    }

    function clearData() {
      purchaseOrderFormState.id = undefined
      purchaseOrderFormState.receiptNumber = ''
      purchaseOrderFormState.supplierId = ''
      purchaseOrderFormState.discountRate = 0
      purchaseOrderFormState.discountAmount = 0
      purchaseOrderFormState.discountLastAmount = 0
      purchaseOrderFormState.deposit = 0
      purchaseOrderFormState.accountId = undefined
      barCode.value = ''
      purchaseOrderFormState.remark = ''
      fileList.value = []
      multipleAccounts.value = {}
      const table = xGrid.value
      if(table) {
        table.remove()
      }
      purchaseOrderFormState.receiptDate = undefined
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
        purchaseOrderFormState.discountLastAmount = `￥${XEUtils.commafy(XEUtils.toNumber(total), { digits: 2 })}`
      }
    }

    function addRowData() {
      const table = xGrid.value
      if(table) {
        table.insert({productNumber: 0})
      }
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
      purchaseOrderFormState.discountLastAmount = newValue
      // 重新调用本次收款
      discountAmountChange()
    });

    function discountRateChange() {
      const price = getTaxTotalPrice.value;
      const discountLastAmount = Number(price.replace(/,/g, '').replace(/￥/g, ''))
      const discountRate = purchaseOrderFormState.discountRate
      const discountAmount = discountLastAmount * discountRate / 100
      purchaseOrderFormState.discountAmount = Number(discountAmount.toFixed(2))
      purchaseOrderFormState.discountLastAmount = `￥${XEUtils.commafy(XEUtils.toNumber(Number((discountLastAmount - discountAmount))), { digits: 2 })}`
    }

    function discountAmountChange() {
      const price = getTaxTotalPrice.value;
      const discountLastAmount = Number(price.replace(/,/g, '').replace(/￥/g, ''))
      const discountAmount = purchaseOrderFormState.discountAmount
      const discountRate = discountAmount / discountLastAmount * 100
      purchaseOrderFormState.discountRate = Number(discountRate.toFixed(2))
      purchaseOrderFormState.discountLastAmount = `￥${XEUtils.commafy(XEUtils.toNumber(Number((discountLastAmount - discountAmount))), { digits: 2 })}`
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

    return {
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
      purchaseOrderFormState,
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
      orderGridOptions,
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
      productList,
      productLabelList,
      selectBarCode,
      handleSupplierModalSuccess
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