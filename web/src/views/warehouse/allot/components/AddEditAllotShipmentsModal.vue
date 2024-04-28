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
      <a-button @click="handleCancelModal">{{ t('warehouse.allotShipments.form.cancel') }}</a-button>
      <a-button v-if="checkFlag" :loading="confirmLoading" @click="handleOk(1)">{{ t('warehouse.allotShipments.form.saveApprove') }}</a-button>
      <a-button type="primary" :loading="confirmLoading" @click="handleOk(0)">{{ t('warehouse.allotShipments.form.save') }}</a-button>
      <!--发起多级审核-->
      <a-button v-if="!checkFlag" @click="" type="primary">提交流程</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="allotShipmentsFormState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="8" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('warehouse.allotShipments.form.receiptDate')" :rules="[{ required: true}]">
              <a-date-picker v-model:value="allotShipmentsFormState.receiptDate" show-time :placeholder="t('warehouse.allotShipments.form.inputReceiptDate')" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :lg="8" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('warehouse.allotShipments.form.receiptNumber')" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号">
              <a-input :placeholder="t('warehouse.allotShipments.form.inputReceiptNumber')" v-model:value="allotShipmentsFormState.receiptNumber" :readOnly="true"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="24" :md="12" :sm="24" style="margin-bottom: 150px;">
            <div class="table-operations">
              <vxe-grid ref='xGrid' v-bind="gridOptions">
                <template #toolbar_buttons="{ row }">
                  <a-button v-if="showScanButton" type="primary"  @click="scanEnter" style="margin-right: 10px">
                    {{ t('warehouse.allotShipments.form.scanCodeData') }}
                  </a-button>
                  <a-input v-if="showScanPressEnter" :placeholder="t('warehouse.allotShipments.form.scanCodeTip')" style="width: 150px; margin-right: 10px" v-model:value="barCode"
                           @pressEnter="scanPressEnter" ref="scanBarCode"/>
                  <a-button v-if="showScanPressEnter" style="margin-right: 10px" @click="stopScan">{{ t('warehouse.allotShipments.form.collapseScanCode') }}</a-button>
                  <a-button @click="productModal" style="margin-right: 10px">{{ t('warehouse.allotShipments.form.addProduct') }}</a-button>
                  <a-button @click="addRowData" style="margin-right: 10px">{{ t('warehouse.allotShipments.form.insertRow') }}</a-button>
                  <a-button @click="deleteRowData" style="margin-right: 10px">{{ t('warehouse.allotShipments.form.deleteRow') }}</a-button>
                </template>
                <template #warehouse_default="{ row }">
                  <span>{{ formatWarehouseId(row.warehouseId) }}</span>
                </template>
                <template #warehouse_edit="{ row }">
                  <vxe-select :placeholder="t('warehouse.allotShipments.form.inputOutWarehouse')" v-model="row.warehouseId" @change="selectBarCode" clearable filterable>
                    <vxe-option v-for="item in warehouseList" :key="item.id" :value="item.id" :label="item.warehouseName"></vxe-option>
                  </vxe-select>
                </template>
                <template #otherWarehouse_default="{ row }">
                  <span>{{ formatWarehouseId(row.otherWarehouseId) }}</span>
                </template>
                <template #otherWarehouse_edit="{ row }">
                  <vxe-select :placeholder="t('warehouse.allotShipments.form.inputInWarehouse')" v-model="row.otherWarehouseId" clearable filterable>
                    <vxe-option v-for="item in warehouseList" :key="item.id" :value="item.id" :label="item.warehouseName"></vxe-option>
                  </vxe-select>
                </template>
                <template #barCode_edit="{ row }">
                  <vxe-select v-model="row.barCode" :placeholder="t('warehouse.allotShipments.form.table.inputBarCode')" @change="selectBarCode" :options="productLabelList" clearable filterable></vxe-select>
                </template>
                <template #product_number_edit="{ row }">
                  <vxe-input v-model="row.productNumber" ></vxe-input>
                </template>
              </vxe-grid>
            </div>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="24" :md="24" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="{xs: { span: 24 },sm: { span: 24 }}" label="">
                  <a-textarea :rows="3" :placeholder="t('warehouse.allotShipments.form.inputRemark')" v-model:value="allotShipmentsFormState.remark"
                              style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="12" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" :label="t('warehouse.allotShipments.form.annex')" data-step="9"
                             data-title="附件"
                             data-intro="可以上传与单据相关的图片、文档，支持多个文件">
                  <a-upload
                      v-model:file-list="fileList"
                      :custom-request="uploadFiles"
                      :before-upload="beforeUpload"
                      multiple>
                    <a-button>
                      <upload-outlined/>
                      {{ t('warehouse.allotShipments.form.uploadAnnex') }}
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
  allotShipmentsFormState,
  xGrid,
  tableData,
  gridOptions,
} from '/src/views/warehouse/allot/components/addEditAllotShipments.data';
import {useModal} from "@/components/Modal";
import {generateId, uploadOss} from "@/api/basic/common";
import {VXETable, VxeGrid, VxeInput, VxeButton, VxeSelect, VxeOption} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdateAllotShipments, getAllotShipmentsDetailById} from "@/api/warehouse/allotShipments"
import PurchaseArrearsModal from "@/views/financial/payment/components/PurchaseArrearsModal.vue"
import weekday from "dayjs/plugin/weekday";
import localeData from "dayjs/plugin/localeData";
import {WarehouseResp} from "@/api/basic/model/warehouseModel";
import {getWarehouseList} from "@/api/basic/warehouse";
import {getProductSkuByBarCode, getProductStockSku} from "@/api/product/product";
import SelectProductModal from "@/views/product/info/components/SelectProductModal.vue";
import {ProductStockSkuResp} from "@/api/product/model/productModel";
import {useI18n} from "vue-i18n";
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
  name: 'AddEditAllotShipmentsModal',
  emits: ['success', 'cancel', 'error'],
  components: {
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
    const { t } = useI18n();
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
    const warehouseList = ref<WarehouseResp[]>([]);
    const productList = ref<ProductStockSkuResp[]>([]);
    const productLabelList = ref<any[]>([]);

    function handleCancelModal() {
      clearData();
      open.value = false;
      context.emit('cancel');
    }

    function openAddEditModal(id: string | undefined) {
      open.value = true
      loadWarehouseList();
      loadProductSku();
      if (id) {
        title.value = t('warehouse.allotShipments.editAllotShipments')
        loadAllotShipmentsDetail(id);
      } else {
        title.value = t('warehouse.allotShipments.addAllotShipments')
        loadGenerateId();
        allotShipmentsFormState.receiptDate = dayjs(new Date());
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
        }
        const defaultWarehouse = res.data.find(item => item.isDefault === 1)
        if(defaultWarehouse) {
          allotShipmentsFormState.warehouseId = defaultWarehouse.id
        } else {
          allotShipmentsFormState.warehouseId = res.data[0].id
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
              selectRow.row.productNumber = 1
              table.updateData(selectRow.rowIndex, selectRow.row)
            } else {
              createMessage.warn("该条码查询不到商品信息")
            }
          }
        }
      }
    }

    function loadGenerateId() {
      generateId("调拨出库").then(res => {
        allotShipmentsFormState.receiptNumber = res.data
      })
    }

    async function loadAllotShipmentsDetail(id) {
      clearData();
      const result = await getAllotShipmentsDetailById(id)
      if(result) {
        const data = result.data
        allotShipmentsFormState.id = data.id
        allotShipmentsFormState.receiptDate = dayjs(data.receiptDate);
        allotShipmentsFormState.receiptNumber = data.receiptNumber
        allotShipmentsFormState.remark = data.remark
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
              otherWarehouseId: item.otherWarehouseId,
              productId: item.productId,
              barCode: item.barCode,
              productName: item.productName,
              productStandard: item.productStandard,
              stock: item.stock,
              productUnit: item.productUnit,
              productNumber: item.productNumber,
              remark: item.remark,
            };
            table.insert(tableData)
          })
        }
      }
    }

    async function handleOk(type: number) {
      if (!allotShipmentsFormState.receiptDate) {
        createMessage.warn(t('warehouse.allotShipments.form.inputReceiptDate'))
        return;
      }
      const table = xGrid.value
      if(table) {
        const insertRecords = table.getInsertRecords()
        if(insertRecords.length === 0) {
          createMessage.warn(t('warehouse.allotShipments.form.addRowData'))
          return;
        }
        const isBarCodeEmpty = insertRecords.some(item => !item.barCode)
        if(isBarCodeEmpty) {
          createMessage.warn(t('warehouse.allotShipments.form.noticeOne'))
          return;
        }
        const isOtherWarehouseEmpty = insertRecords.some(item => !item.otherWarehouseId)
        if(isOtherWarehouseEmpty) {
          createMessage.warn(t('warehouse.allotShipments.form.inputInWarehouse'))
          return;
        }
        const isWarehouseEmpty = insertRecords.some(item => !item.warehouseId)
        if(isWarehouseEmpty) {
          createMessage.warn(t('warehouse.allotShipments.form.inputOutWarehouse'))
          return;
        }
      }

      const tableData = table.getTableData().tableData
      const isStockNotEnough = tableData.some(item => item.productNumber > item.stock)
      if(isStockNotEnough) {
        const tableDataNotEnough = tableData.filter(item => item.productNumber > item.stock)
        const tableDataNotEnoughBarCode = tableDataNotEnough.map(item => item.barCode)
        const tableDataNotEnoughBarCodeStr = tableDataNotEnoughBarCode.join(",")
        createMessage.info("条码: "+tableDataNotEnoughBarCodeStr +"商品库存不足，请检查库存数量")
        return;
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
          otherWarehouseId: item.otherWarehouseId,
          productId: item.productId,
          barCode: item.barCode,
          productName: item.productName,
          productStandard: item.productStandard,
          stock: item.stock,
          productUnit: item.productUnit,
          productNumber: item.productNumber,
          remark: item.remark,
        }
        dataArray.push(data)
      })
      const params: any = {
        ...allotShipmentsFormState,
        tableData: dataArray,
        files: files,
        status: type,
      }
      const result = await addOrUpdateAllotShipments(params)
      if (result.code === 'S0010' || result.code === 'S0011') {
        handleCancelModal();
      }
    }

    function clearData() {
      allotShipmentsFormState.id = undefined
      allotShipmentsFormState.receiptNumber = ''
      allotShipmentsFormState.remark = ''
      allotShipmentsFormState.receiptDate = undefined
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
      getProductSkuByBarCode(barCode.value, allotShipmentsFormState.warehouseId).then(res => {
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
                table.updateData()
              } else {
                const tableData : any = {
                  warehouseId: shipmentsData.warehouseId,
                  otherWarehouseId: shipmentsData.otherWarehouseId,
                  productId: shipmentsData.productId,
                  barCode: shipmentsData.barCode,
                  productName: shipmentsData.productName,
                  productStandard: shipmentsData.productStandard,
                  productUnit: shipmentsData.productUnit,
                  stock: shipmentsData.stock,
                  productNumber: 1,
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
          item.warehouseId = allotShipmentsFormState.warehouseId
          item.productNumber = 1
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

    function addRowData() {
      const table = xGrid.value
      const defaultWarehouse = warehouseList.value.find(item => item.isDefault === 1)
      const warehouseId = defaultWarehouse ? defaultWarehouse.id : warehouseList.value[0].id
      if(table) {
        table.insert({warehouseId: warehouseId})
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
      allotShipmentsFormState,
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
      addRowData,
      productList,
      productLabelList,
      selectBarCode
    };
  },
});

</script>

<style scoped>

</style>