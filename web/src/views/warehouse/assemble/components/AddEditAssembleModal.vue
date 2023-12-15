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
      <a-form ref="formRef" :model="assembleFormState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="8" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据日期" :rules="[{ required: true}]">
              <a-date-picker v-model:value="assembleFormState.receiptDate" show-time placeholder="选择时间" format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :lg="8" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据编号" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号">
              <a-input placeholder="请输入单据编号" v-model:value="assembleFormState.receiptNumber" :readOnly="true"/>
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
                  <a-button @click="productModal" style="margin-right: 10px">选择添加组装商品</a-button>
                  <a-button @click="addRowSubData" style="margin-right: 10px">添加一行</a-button>
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
                  <vxe-select v-model="row.barCode" placeholder="输入商品条码" @change="selectBarCode" :options="productLabelList" clearable filterable></vxe-select>
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
                  <a-textarea :rows="3" placeholder="请输入备注" v-model:value="assembleFormState.remark"
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
  assembleFormState,
  xGrid,
  tableData,
  gridOptions,
} from '/src/views/warehouse/addEditAssembleOrDisassemble.data';
import {useModal} from "@/components/Modal";
import {generateId, uploadOss} from "@/api/basic/common";
import {VXETable, VxeGrid, VxeInput, VxeButton, VxeSelect, VxeOption} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdateAssemble, getAssembleDetailById} from "@/api/warehouse/assemble"
import weekday from "dayjs/plugin/weekday";
import localeData from "dayjs/plugin/localeData";
import {WarehouseResp} from "@/api/basic/model/warehouseModel";
import {getDefaultWarehouse, getWarehouseList} from "@/api/basic/warehouse";
import {getProductSkuByBarCode, getProductStockSku} from "@/api/product/product";
import SelectProductModal from "@/views/product/info/components/SelectProductModal.vue";
import {ProductStockSkuResp} from "@/api/product/model/productModel";
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
  name: 'AddEditAssembleModal',
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
        title.value = '编辑-组装单'
        loadAssembleReceipt(id);
      } else {
        title.value = '新增-组装单'
        loadGenerateId();
        assembleFormState.receiptDate = dayjs(new Date());
        addRowData();
      }
    }

    function loadWarehouseList() {
      getWarehouseList().then(res => {
        const {columns} = gridOptions
        if (columns) {
          const warehouseColumn = columns[2]
          warehouseColumn.editRender.options = [];
          if (warehouseColumn && warehouseColumn.editRender) {
            warehouseColumn.editRender.options?.push(...res.data.map(item => ({value: item.id, label: item.warehouseName})))
          }
          warehouseList.value = res.data
        }
        const defaultWarehouse = res.data.find(item => item.isDefault === 1)
        if(defaultWarehouse) {
          assembleFormState.warehouseId = defaultWarehouse.id
        } else {
          assembleFormState.warehouseId = res.data[0].id
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
              selectRow.row.unitPrice = product.purchasePrice
              selectRow.row.amount = product.purchasePrice
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
      generateId("组装单").then(res => {
        assembleFormState.receiptNumber = res.data
      })
    }

    function addRowData() {
      // 默认给表格添加一行数据 为组合件
      const table = xGrid.value
      if(table) {
        const tableData : any = {
          type: "组合件",
          warehouseId: assembleFormState.warehouseId,
          productId: '',
          barCode: '',
          productName: '',
          productStandard: '',
          stock: '',
          productUnit: '',
          productNumber: '',
          unitPrice: '',
          amount: '',
          remark: '',
        };
        table.insert(tableData)
      }
    }

    async function loadAssembleReceipt(id) {
      clearData();
      const result = await getAssembleDetailById(id)
      if(result) {
        const data = result.data
        assembleFormState.id = data.id
        assembleFormState.receiptDate = dayjs(data.receiptDate);
        assembleFormState.receiptNumber = data.receiptNumber
        assembleFormState.remark = data.remark
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
              type: item.type,
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
      if (!assembleFormState.receiptDate) {
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
        const isBarCodeEmpty = insertRecords.some(item => !item.barCode)
        if(isBarCodeEmpty) {
          createMessage.warn("请录入条码或者选择产品")
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
          type: item.type,
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
        ...assembleFormState,
        tableData: dataArray,
        files: files,
        status: type,
      }
      const result = await addOrUpdateAssemble(params)
      if (result.code === 'A0020' || 'A0021') {
        handleCancelModal();
      }
    }

    function clearData() {
      assembleFormState.id = undefined
      assembleFormState.receiptNumber = ''
      assembleFormState.remark = ''
      assembleFormState.receiptDate = undefined
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
      getProductSkuByBarCode(barCode.value, assembleFormState.warehouseId).then(res => {
        const {columns} = gridOptions
        if (columns) {
          const {data} = res
          if (data) {
            const assembleData : any = data
            const table = xGrid.value
            if (table) {
              const tableData = table.getTableData().tableData
              const index = tableData.findIndex(item => item.barCode === assembleData.barCode)
              if (index > -1) {
                const row = tableData[index]
                row.productNumber = Number(row.productNumber) + 1
                row.amount = row.productNumber * row.unitPrice
                row.amount = Number(row.amount.toFixed(2))
                table.updateData()
              } else {
                const tableData : any = {
                  type: "普通子件",
                  warehouseId: assembleData.warehouseId,
                  productId: assembleData.productId,
                  barCode: assembleData.barCode,
                  productName: assembleData.productName,
                  productStandard: assembleData.productStandard,
                  productUnit: assembleData.productUnit,
                  stock: assembleData.stock,
                  productNumber: 1,
                  amount: assembleData.purchasePrice,
                  unitPrice: assembleData.purchasePrice,
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
        // 如果只选中了一条数据，那么就直接插入到当前行，否则就是多条数据，那么就直接插入到表格中
        if(data.length === 1) {
          // 获取第一行的数据
          const tableData = table.getTableData().tableData[0]
          tableData.warehouseId = data[0].warehouseId
          tableData.productId = data[0].productId
          tableData.barCode = data[0].barCode
          tableData.productName = data[0].productName
          tableData.productStandard = data[0].productStandard
          tableData.productUnit = data[0].productUnit
          tableData.stock = data[0].stock
          tableData.unitPrice = data[0].purchasePrice
          tableData.amount = data[0].purchasePrice
          tableData.productNumber = 1
          table.updateData()
        } else {
          data.forEach(item => {
            const tableData : any = {
              type: "普通子件",
              warehouseId: item.warehouseId,
              productId: item.productId,
              barCode: item.barCode,
              productName: item.productName,
              productStandard: item.productStandard,
              productUnit: item.productUnit,
              stock: item.stock,
              productNumber: 1,
              amount: item.purchasePrice,
              unitPrice: item.purchasePrice,
            };
            // 如果获取data的下标
            if (data.indexOf(item) === 0) {
              const tableData = table.getTableData().tableData[0]
              tableData.productId = item.productId
              tableData.warehouseId = item.warehouseId
              tableData.barCode = item.barCode
              tableData.productName = item.productName
              tableData.productStandard = item.productStandard
              tableData.productUnit = item.productUnit
              tableData.stock = item.stock
              tableData.unitPrice = item.purchasePrice
              tableData.amount = item.purchasePrice
              tableData.productNumber = 1
              table.updateData()
            } else {
              table.insertAt(tableData, -1)
            }
          })
        }
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

    function addRowSubData() {
      const table = xGrid.value
      if(table) {
        table.insertAt({type: "普通子件", warehouseId: assembleFormState.warehouseId}, -1)
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
      assembleFormState,
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
      warehouseList,
      formatWarehouseId,
      selectProductModal,
      productModal,
      handleCheckSuccess,
      productNumberChange,
      unitPriceChange,
      amountChange,
      addRowSubData,
      selectBarCode,
      productLabelList
    };
  },
});

</script>

<style scoped>

</style>