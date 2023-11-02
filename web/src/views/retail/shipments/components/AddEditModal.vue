<template>
  <a-modal
      :title="title"
      :width="width"
      :confirm-loading="confirmLoading"
      v-bind:prefixNo="prefixNo"
      :id="prefixNo"
      :keyboard="false"
      :forceRender="true"
      switchHelp
      switchFullscreen
      v-model:open="open"
      @cancel="handleCancelModal"
      @ok="handleOkModal"
      style="left: 5%; height: 95%;">
    <template #footer>
      <a-button @click="">取消</a-button>
      <a-button v-if="checkFlag && isCanCheck" :loading="confirmLoading" @click="">保存并审核</a-button>
      <a-button type="primary" :loading="confirmLoading" @click="handleOk(0)">保存</a-button>
      <!--发起多级审核-->
      <a-button v-if="!checkFlag" @click="" type="primary">提交流程</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form ref="formRef" :model="formState" style="margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: -150px">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="会员卡号" data-step="1"
                         data-title="会员卡号"
                         data-intro="如果发现需要选择的会员卡号尚未录入，可以在下拉框中点击新增会员信息进行录入">
              <a-select placeholder="选择会员卡号" v-model:value="formState.memberId"
                        :dropdownMatchSelectWidth="false" showSearch optionFilterProp="children"
                        @change="onMemberChange"
                        :options="memberList.map(item => ({ value: item.id, label: item.memberName }))">
                <template #dropdownRender="{ menuNode: menu }">
                  <v-nodes :vnodes="menu"/>
                  <a-divider style="margin: 4px 0"/>
                  <div style="padding: 4px 8px; cursor: pointer;"
                       @mousedown="e => e.preventDefault()" @click="addMember">
                    <plus-outlined/>
                    新增会员
                  </div>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据日期" :rules="[{ required: true}]">
              <a-date-picker v-model:value="formState.receiptDate" show-time placeholder="选择时间" @change="dateChange" @ok="dateOk"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="单据编号" data-step="2"
                         data-title="单据编号"
                         data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号">
              <a-input placeholder="请输入单据编号" v-model:value="formState.receiptNumber" :readOnly="true"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="收款类型" data-step="3"
                         data-title="收款类型"
                         data-intro="收款类型可以有现付和预付款两种类型，当选择了会员之后，如果该会员有预付款，在此处会显示具体预付款的金额，而且系统会优先默认选中预付款">
              <a-select placeholder="请选择付款类型" v-model:value="formState.paymentType"
                        :dropdownMatchSelectWidth="false">
                <a-select-option v-for="(item,index) in payTypeList" :key="index" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :lg="18" :md="12" :sm="24" style="margin-bottom: 150px;">
                <a-button v-if="showScanButton" type="primary"  @click="scanEnter" style="margin-right: 10px">扫条码录入数据</a-button>
                <a-input v-if="showScanPressEnter" placeholder="请扫条码并回车" style="width: 150px; margin-right: 10px" v-model:value="formState.scanBarCode"
                         @pressEnter="scanPressEnter" ref="scanBarCode"/>
                <a-button v-if="showScanPressEnter" @click="stopScan">收起扫码</a-button>
            <div class="table-operations">
              <vxe-grid ref='xGrid' v-bind="gridOptions" v-on="gridEvent">
                <template #product_number_edit="{ row, column }">
                  <vxe-input v-model="row.productNumber"></vxe-input>
                </template>
                <template #amount_edit="{ row, column }">
                  <vxe-input v-model="row.amount"></vxe-input>
                </template>
                <template #barcode_edit="{ row, column }">
                  <vxe-input type="search" clearable v-model="row.barcode" @search-click="productModal"></vxe-input>
                </template>
              </vxe-grid>
            </div>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="24" :md="24" :sm="24">
                <a-form-item :label-col="labelCol" :wrapper-col="{xs: { span: 24 },sm: { span: 24 }}" label="">
                  <a-textarea :rows="1" placeholder="请输入备注" v-model:value="formState.remark"
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
          <a-col :lg="6" :md="12" :sm="24">
            <div class="sign" style="margin-top: 40px">
              <a-row class="form-row" :gutter="24">
                <a-col :lg="24" :md="6" :sm="6">
                  <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" data-step="5" data-title="单据金额"
                               data-intro="单据金额等于左侧商品的总金额">
                    <template #label>
                      <span style="font-size: 20px;line-height:20px">单据金额</span>
                    </template>
                    <a-input v-model:value="sumValue" :style="{color:'purple', height:'35px'}"
                             :readOnly="true"/>
                  </a-form-item>
                </a-col>
                <a-col :lg="24" :md="6" :sm="6">
                  <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" data-step="6" data-title="收款金额"
                               data-intro="收款金额为收银员收取用户的实际金额">
                    <template #label>
                      <span style="font-size: 20px;line-height:20px">收款金额</span>
                    </template>
                    <a-input v-model:value="sumValue" :style="{color:'red', height:'35px'}"
                             defaultValue="0"
                             @change="onChangePaymentAmount"/>
                  </a-form-item>
                </a-col>
                <a-col :lg="24" :md="6" :sm="6">
                  <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" data-step="7" data-title="找零"
                               data-intro="找零等于收款金额减去实收金额">
                    <template #label>
                      <span style="font-size: 20px;line-height:20px">找零</span>
                    </template>
                    <a-input v-model:value="formState.backAmount" :style="{color:'green', height:'35px'}"
                             :readOnly="true"
                             defaultValue="0"/>
                  </a-form-item>
                </a-col>
                <a-col :lg="24" :md="6" :sm="6">
                  <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" data-step="8" data-title="收款账户"
                               data-intro="收款账户的信息来自基本资料菜单下的【结算账户】">
                    <template #label>
                      <span style="font-size: 20px;line-height:20px">收款账户</span>
                    </template>
                    <a-select placeholder="选择收款账户" style="font-size:20px;"
                              v-model:value="formState.accountReceivable" :dropdownMatchSelectWidth="false"
                              :options="accountList.map(item => ({ value: item.id, label: item.accountName }))">
                      <template #dropdownRender="{ menuNode: menu }">
                        <v-nodes :vnodes="menu"/>
                        <a-divider style="margin: 4px 0"/>
                        <div style="padding: 4px 8px; cursor: pointer;"
                             @mousedown="e => e.preventDefault()" @click="addAccount">
                          <plus-outlined/>
                          新增结算账户
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
  </a-modal>
  <MemberModal @register="memberModal" @success="handleMemberModalSuccess"/>
  <FinancialAccountModal @register="accountModal" @success="handleAccountModalSuccess"/>
  <SelectProductModal @register="selectProductModal" @handleCheckSuccess="handleCheckSuccess"/>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import {PlusOutlined, UploadOutlined} from '@ant-design/icons-vue';
import {Dayjs} from 'dayjs';
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
import {formState, gridOptions, xGrid, sumValue, tableData} from '/@/views/retail/shipments/model/addEditModel';
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
import {VXETable, VxeGrid, VxeInput, VxeButton, VxeTableEvents} from 'vxe-table'
import {useMessage} from "@/hooks/web/useMessage";
import { addOrUpdateShipments } from "@/api/retail/shipments"
import SelectProductModal from "@/views/product/info/components/SelectProductModal.vue"
import {defineStore} from "pinia";
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

export default defineComponent({
  name: 'AddEditModal',
  emits: ['success', 'cancel', 'error'],
  components: {
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
    'vxe-button': VxeButton
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
    const prefixNo = ref('LSCK');
    const fileList = ref([]);
    const payTypeList = ref<any>([]);
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
    const refKey = ref(['productDataTable']);
    const activeKey = ref('productDataTable');
    const memberList = ref<MemberResp[]>([])
    const accountList = ref<AccountResp[]>([]);
    const warehouseList = ref<WarehouseResp[]>([]);
    const [memberModal, {openModal}] = useModal();
    const [accountModal, {openModal: openAccountModal}] = useModal();
    const [selectProductModal, {openModal: openProductModal}] = useModal();

    function handleCancelModal() {
      close();
      open.value = false;
      context.emit('cancel');
    }

    function openAddEditModal(id: string | undefined) {
      open.value = true
      loadMemberList();
      loadAccountList();
      loadWarehouseList();
      if (id) {
        title.value = '编辑-零售出库'
      } else {
        title.value = '新增-零售出库'
        loadGenerateId();
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
      generateId("零售出库").then(res => {
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
        }
      })
    }

    function handleOkModal() {

    }

    function onChangePaymentAmount() {

    }

    const dateChange = (value: Dayjs, dateString: string) => {

    };

    const dateOk = (value: Dayjs) => {
    };

    function scanPressEnter() {

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

    function onMemberChange(value) {
      payTypeList.value = []
      const member = memberList.value.find(item => item.id === value)
      if (member.advancePayment > 0) {
        payTypeList.value = [
          {value: '预付款', text: '预付款（' + member.advancePayment + '）'},
          {value: '现付', text: '现付'},
        ]
      } else {
        payTypeList.value = [
          {value: '现付', text: '现付'},
        ]
      }
    }

    function addAccount() {
      openAccountModal(true, {
        isUpdate: false,
      });
    }

    async function handleOk() {
      const form = formState;
      console.info(form)
      const table = xGrid.value
      if (!formState.receiptDate) {
        createMessage.error('请选择单据日期');
        return;
      }
      if(table) {
        const insertRecords = table.getInsertRecords()
        console.info(insertRecords)
        if(insertRecords.length === 0) {
          createMessage.error("请添加一行数据")
        }
        insertRecords.forEach(item => {
          if(!item.warehouseId || !item.barcode) {
            createMessage.error("请填写红色*的必填参数")
          }
        })

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
        insertRecords.forEach(item => {
          const data: ShipmentsData = {
            warehouseId: item.warehouseId,
            barcode: item.barcode,
            productNumber: item.productNumber,
            unitPrice: item.unitPrice,
            amount: item.amount,
            remark: item.remark,
          }
          dataArray.push(data)
        })

        const params: AddOrUpdateShipmentsReq = {
          ...formState,
          tableData: dataArray,
          files: files,
        }

        const result = await addOrUpdateShipments(params)
        if (result.code === 'R0001' || 'R0002') {
          createMessage.success('操作成功');
          handleCancelModal();
          // clearData();
        } else {
          createMessage.error('操作失败');
        }
      }
    }

    function handleMemberModalSuccess() {
      loadMemberList()
    }

    function handleAccountModalSuccess() {
      loadAccountList()
    }

    function addWarehouse() {
    }

    function handleBatchSetWarehouse() {
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
      // 将data数据数组添加到表格中
      const table = xGrid.value
      if(table) {
        table.insert(data)
      }
    }

    return {
      open,
      checkFlag,
      isCanCheck,
      isTenant,
      scanStatus,
      confirmLoading,
      handleCancelModal,
      openAddEditModal,
      handleOkModal,
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
      dateChange,
      dateOk,
      scanPressEnter,
      scanEnter,
      stopScan,
      memberList,
      accountList,
      warehouseList,
      addWarehouse,
      handleBatchSetWarehouse,
      showScanButton,
      showScanPressEnter,
      memberModal,
      addMember,
      handleMemberModalSuccess,
      onMemberChange,
      accountModal,
      addAccount,
      handleAccountModalSuccess,
      handleOk,
      beforeUpload,
      uploadFiles,
      gridOptions,
      xGrid,
      sumValue,
      SelectProductModal,
      selectProductModal,
      openProductModal,
      productModal,
      handleCheckSuccess
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