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
      <a-form ref="formRef" :model="otherShipmentFormState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-input v-model:value="otherShipmentFormState.id" v-show="false"/>
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="客户" data-step="1"
                         data-title="客户" :rules="[{ required: true}]">
              <a-select v-model:value="otherShipmentFormState.customerId"
                        :dropdownMatchSelectWidth="false" showSearch optionFilterProp="children"
                        placeholder="请选择客户"
                        :options="customerList.map(item => ({ value: item.id, label: item.customerName }))">
                <template #dropdownRender="{ menuNode: menu }">
                  <v-nodes :vnodes="menu"/>
                  <a-divider style="margin: 4px 0"/>
                  <div style="padding: 4px 8px; cursor: pointer;"
                       @mousedown="e => e.preventDefault()" @click="addSupplier">
                    <plus-outlined/>
                    新增客户
                  </div>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据日期" :rules="[{ required: true}]">
              <a-date-picker v-model:value="otherShipmentFormState.receiptDate" show-time placeholder="选择时间" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :lg="7" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据编号" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号">
              <a-input placeholder="请输入单据编号" v-model:value="otherShipmentFormState.receiptNumber" :readOnly="true"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="24" :md="12" :sm="24" style="margin-bottom: 150px;">
            <div class="table-operations">
              <vxe-grid ref='xGrid' v-bind="gridOptions">
                <template #toolbar_buttons="{ row }">
                  <a-button v-if="showScanButton" type="primary"  @click="scanEnter" style="margin-right: 10px">扫条码录入数据</a-button>
                  <a-input v-if="showScanPressEnter" placeholder="鼠标点击此处扫条码" style="width: 150px; margin-right: 10px" v-model:value="barCode"
                           @pressEnter="scanPressEnter" ref="scanBarCode"/>
                  <a-button v-if="showScanPressEnter" style="margin-right: 10px" @click="stopScan">收起扫码</a-button>
                  <a-button @click="productModal" style="margin-right: 10px">选择添加入库商品</a-button>
                  <a-button @click="deleteRowData" style="margin-right: 10px">删除选中行</a-button>
                </template>
                <template #warehouseId_default="{ row }">
                  <span>{{ formatWarehouseId(row.warehouseId) }}</span>
                </template>
                <template #warehouseId_edit="{ row }">
                  <vxe-select placeholder="请选择仓库" v-model="row.warehouseId">
                    <vxe-option v-for="item in warehouseList" :key="item.id" :value="item.id" :label="item.warehouseName"></vxe-option>
                  </vxe-select>
                </template>
                <template #barCode_edit="{ row }">
                  <vxe-input type="search" clearable v-model="row.barCode"></vxe-input>
                </template>
                <template #product_number_edit="{ row }">
                  <vxe-input v-model="row.productNumber" @change="productNumberChange"></vxe-input>
                </template>
                <template #price_edit="{ row }">
                  <vxe-input v-model="row.unitPrice" @change="unitPriceChange"></vxe-input>
                </template>
                <template #amount_edit="{ row }">
                  <vxe-input v-model="row.amount"  @change="amountChange"></vxe-input>
                </template>
              </vxe-grid>
            </div>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="24" :md="24" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="{xs: { span: 24 },sm: { span: 24 }}" label="">
                  <a-textarea :rows="3" placeholder="请输入备注" v-model:value="otherShipmentFormState.remark"
                              style="margin-top:8px;"/>
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
  <CustomerModal @register="supplierModal"/>
  <SelectProductModal @register="selectProductModal" @handleCheckSuccess="handleCheckSuccess"/>
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
  Upload,
} from "ant-design-vue";
import {
  otherShipmentFormState,
  xGrid,
  tableData,
  gridOptions,
} from '/src/views/warehouse/addEditStorageShipments.data';
import {useModal} from "@/components/Modal";
import {generateId, uploadOss} from "@/api/basic/common";
import {VXETable, VxeGrid, VxeInput, VxeButton, VxeSelect, VxeOption} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdateOtherShipments, getOtherShipmentsDetailById} from "@/api/warehouse/shipments"
import {getCustomerList} from "@/api/basic/customer";
import PurchaseArrearsModal from "@/views/financial/payment/components/PurchaseArrearsModal.vue"
import weekday from "dayjs/plugin/weekday";
import localeData from "dayjs/plugin/localeData";
import {CustomerResp} from "@/api/basic/model/customerModel";
import {WarehouseResp} from "@/api/basic/model/warehouseModel";
import {getDefaultWarehouse, getWarehouseList} from "@/api/basic/warehouse";
import {getProductSkuByBarCode} from "@/api/product/product";
import SelectProductModal from "@/views/product/info/components/SelectProductModal.vue";
import CustomerModal from "@/views/basic/customer/components/CustomerModal.vue";
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
    'vxe-select': VxeSelect,
    'vxe-option': VxeOption,
    'vxe-button': VxeButton,
    'plus-outlined': PlusOutlined,
    'upload-outlined': UploadOutlined,
  },
  setup(_, context) {
    const [supplierModal, {openModal}] = useModal();
    const [selectProductModal, {openModal: openProductModal}] = useModal();
    const {createMessage} = useMessage();
    const confirmLoading = ref<boolean>(false);
    const open = ref<boolean>(false);
    const checkFlag = ref<boolean>(true);
    const showScanButton = ref(true);
    const showScanPressEnter = ref(false);
    const title = ref<string>("操作");
    const width = ref('1200px');
    const addDefaultRowNum = ref(1);
    const fileList = ref<any[]>([]);
    const minWidth = ref(1200);
    const model = ref({});
    const labelCol = ref({
      xs: {span: 24},
      sm: {span: 8},
    });
    const wrapperCol = ref({
      xs: {span: 24},
      sm: {span: 16},
    });
    const barCode = ref('');
    const customerList = ref<CustomerResp[]>([]);
    const warehouseList = ref<WarehouseResp[]>([]);

    function handleCancelModal() {
      close();
      clearData();
      open.value = false;
      context.emit('cancel');
    }

    function openAddEditModal(id: string | undefined) {
      open.value = true
      loadCustomerList();
      loadWarehouseList();
      loadDefaultWarehouse();
      if (id) {
        title.value = '编辑-其他出库'
        loadOtherShipmentsDetail(id);
      } else {
        title.value = '新增-其他出库'
        loadGenerateId();
        otherShipmentFormState.receiptDate = dayjs(new Date());
      }
    }

    function loadDefaultWarehouse() {
      getDefaultWarehouse().then(res => {
        const data = res.data
        if(data) {
          otherShipmentFormState.warehouseId = data.id
        }
      })
    }

    function loadCustomerList() {
      getCustomerList().then(res => {
        customerList.value = res.data
      })
    }

    function loadWarehouseList() {
      getWarehouseList().then(res => {
        warehouseList.value = res.data
      })
    }

    function loadGenerateId() {
      generateId("其他出库").then(res => {
        otherShipmentFormState.receiptNumber = res.data
      })
    }

    async function loadOtherShipmentsDetail(id) {
      clearData();
      const result = await getOtherShipmentsDetailById(id)
      if(result) {
        const data = result.data
        otherShipmentFormState.id = data.id
        otherShipmentFormState.customerId = data.customerId
        otherShipmentFormState.receiptDate = dayjs(data.receiptDate);
        otherShipmentFormState.receiptNumber = data.receiptNumber
        otherShipmentFormState.remark = data.remark
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
            const tableData : any = {
              warehouseId: item.warehouseId,
              productId: item.productId,
              barCode: item.barCode,
              productName: item.productName,
              productStandard: item.productStandard,
              stock: item.stock,
              productUnit: item.productUnit,
              productNumber: item.productNumber,
              unitPrice: item.unitPrice,
              amount: item.amount,
              remark: item.remark,
            };
            table.insert(tableData)
          })
        }
      }
    }

    async function handleOk(type: number) {
      if (!otherShipmentFormState.customerId) {
        createMessage.error('请选择客户');
        return;
      }
      if (!otherShipmentFormState.receiptDate) {
        createMessage.error('请选择单据日期');
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

      const files :any = [];
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

      const dataArray: any = []
      table?.getInsertRecords().forEach(item => {
        const data: any = {
          warehouseId: item.warehouseId,
          productId: item.productId,
          barCode: item.barCode,
          productName: item.productName,
          productStandard: item.productStandard,
          stock: item.stock,
          productUnit: item.productUnit,
          productNumber: item.productNumber,
          unitPrice: item.unitPrice,
          amount: item.amount,
          remark: item.remark,
        }
        dataArray.push(data)
      })
      const params: any = {
        ...otherShipmentFormState,
        tableData: dataArray,
        files: files,
        status: type,
      }
      console.info(params)
      const result = await addOrUpdateOtherShipments(params)
      if (result.code === 'P0024' || 'P0025') {
        createMessage.success('操作成功');
        handleCancelModal();
        clearData();

      } else {
        createMessage.error('操作失败');
      }
      clearData();
    }

    function clearData() {
      otherShipmentFormState.id = undefined
      otherShipmentFormState.receiptNumber = ''
      otherShipmentFormState.customerId = undefined
      otherShipmentFormState.remark = ''
      otherShipmentFormState.receiptDate = undefined
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

    function addSupplier() {
      openModal(true, {
        isUpdate: false,
      });
    }

    async function deleteRowData() {
      const type = await VXETable.modal.confirm('确定要删除选中的数据?')
      const table = xGrid.value
      const selectRow = table?.getCheckboxRecords()
      if (table) {
        if (type === 'confirm') {
          await table.remove(selectRow)
        }
      }
    }

    const formatWarehouseId = (value: string) => {
      const item = warehouseList.value.find(item => item.id === value)
      if(item) {
        return item.warehouseName
      }
    }

    function scanEnter() {
      showScanButton.value = !showScanButton.value;
      showScanPressEnter.value = !showScanPressEnter.value;
    }

    function stopScan() {
      showScanButton.value = !showScanButton.value;
      showScanPressEnter.value = !showScanPressEnter.value;
    }

    function scanPressEnter() {
      getProductSkuByBarCode(barCode.value, otherShipmentFormState.warehouseId).then(res => {
        const {columns} = gridOptions
        if (columns) {
          const {data} = res
          if (data) {
            const shipmentsData : any = data
            const table = xGrid.value
            console.info(shipmentsData)
            if (table) {
              const tableData = table.getTableData().tableData
              const index = tableData.findIndex(item => item.barCode === shipmentsData.barCode)
              if (index > -1) {
                const row = tableData[index]
                row.productNumber = Number(row.productNumber) + 1
                row.amount = row.productNumber * row.unitPrice
                row.amount = Number(row.amount.toFixed(2))
                table.updateData()
              } else {
                const tableData : any = {
                  warehouseId: shipmentsData.warehouseId,
                  productId: shipmentsData.productId,
                  barCode: shipmentsData.barCode,
                  productName: shipmentsData.productName,
                  productStandard: shipmentsData.productStandard,
                  productUnit: shipmentsData.productUnit,
                  stock: shipmentsData.stock,
                  productNumber: 1,
                  amount: shipmentsData.retailPrice,
                  unitPrice: shipmentsData.retailPrice,
                };
                table.insert(tableData)
              }
            }
          }
        }
      })
      // 清除扫码框的值
      barCode.value = ''
    }

    function handleCheckSuccess(data) {
      const table = xGrid.value
      if(table) {
        data = data.map(item => {
          item.unitPrice = item.retailPrice
          item.productNumber = 1
          item.amount = item.retailPrice * item.productNumber
          return item
        })
        table.insert(data)
      }
    }


    function productModal() {
      openProductModal(true, {
        isUpdate: false,
      });
    }

    function productNumberChange() {
      const table = xGrid.value
      if(table) {
        const data = table.getEditRecord().row;
        data.amount = data.productNumber * data.unitPrice
        data.amount = Number(data.amount.toFixed(2))
        table.updateData()
      }
    }

    function unitPriceChange() {
      const table = xGrid.value
      if(table) {
        const data = table.getEditRecord().row;
        data.amount = data.unitPrice * data.productNumber
        data.amount = Number(data.amount.toFixed(2))
        table.updateData()
      }
    }

    function amountChange() {
      const table = xGrid.value
      if(table) {
        const data = table.getEditRecord().row;
        data.unitPrice = data.amount / data.productNumber
        data.unitPrice = Number(data.unitPrice.toFixed(2))
        table.updateData()
      }
    }

    return {
      h,
      AccountBookTwoTone,
      open,
      checkFlag,
      confirmLoading,
      handleCancelModal,
      openAddEditModal,
      otherShipmentFormState,
      title,
      width,
      barCode,
      showScanButton,
      showScanPressEnter,
      scanEnter,
      stopScan,
      scanPressEnter,
      addDefaultRowNum,
      fileList,
      minWidth,
      model,
      labelCol,
      wrapperCol,
      customerList,
      handleOk,
      beforeUpload,
      uploadFiles,
      gridOptions,
      xGrid,
      tableData,
      deleteRowData,
      supplierModal,
      addSupplier,
      PurchaseArrearsModal,
      warehouseList,
      formatWarehouseId,
      selectProductModal,
      productModal,
      handleCheckSuccess,
      productNumberChange,
      unitPriceChange,
      amountChange
    };
  },
});

</script>

<style scoped>

</style>