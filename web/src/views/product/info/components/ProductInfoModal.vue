<template>
  <a-modal
      :title="title"
      :width="1300"
      :confirm-loading="confirmLoading"
      v-bind:prefixNo="prefixNo"
      :id="prefixNo"
      switchHelp
      switchFullscreen
      v-model:open="open"
      @cancel="handleCancel"
      @ok="handleOk"
      style="top:10%;height: 95%;">
    <a-spin :spinning="confirmLoading">
      <a-form ref="productFormRef" :model="formState" :label-col="labelCol" :wrapper-col="wrapperCol"
              style="margin-top: 20px; margin-right: 20px; margin-left: 20px">
        <a-tabs default-active-key="1" size="small">
          <a-tab-pane key="1" tab="基本信息" id="materialHeadModal" forceRender>
            <a-row class="form-row" :gutter="24">
              <a-col :md="6" :sm="24">
                <a-input v-model:value="formState.productId" v-show="false"/>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="名称" data-step="1" data-title="名称"
                             data-intro="名称必填，可以重复" :rules="[{ required: true}]">
                  <a-input v-model:value="formState.productName" placeholder="请输入名称"/>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="规格" data-step="2" data-title="规格"
                             data-intro="规格不必填，比如：10克">
                  <a-input v-model:value="formState.productStandard" placeholder="请输入规格"/>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="型号" data-step="3" data-title="型号"
                             data-intro="型号是比规格更小的属性，比如：RX-01">
                  <a-input v-model:value="formState.productModel" placeholder="请输入型号"/>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="{ xs: { span: 24 }, sm: { span: 4 } }"
                             :wrapperCol="{ xs: { span: 24 }, sm: { span: 20 } }"
                             label="单位"
                             :data-step="4"
                             data-title="单位"
                             :data-intro="`此处支持单个单位和多单位，勾选多单位就可以切换到多单位的下拉框，多单位需要先在【计量单位】页面进行录入。
                  比如牛奶有瓶和箱两种单位，12瓶=1箱，这就构成了多单位，多单位中有个换算比例`"
                             :rules="[{ required: true}]">
                  <a-row class="form-row" :gutter="24">
                    <a-col :lg="15" :md="15" :sm="24" style="padding:0px 0px 0px 12px;">
                      <a-input v-if="!unitChecked"
                               placeholder="输入单位"
                               v-model:value="formState.productUnit"
                               :rules="[{ required: true}]"
                               @change="onlyUnitOnChange"/>
                      <a-select v-else
                                placeholder="选择多单位"
                                v-model:value="formState.productUnitId"
                                :rules="[{ required: true}]"
                                @change="manyUnitOnChange(formState.productUnitId)"
                                showSearch
                                optionFilterProp="children"
                                :dropdownMatchSelectWidth="false"
                                v-if="unitChecked"
                                :options="unitList.map(item => ({ value: item.id, label: item.computeUnit }))">
                        <template #dropdownRender="{ menuNode: menu }">
                          <v-nodes :vnodes="menu"/>
                          <a-divider style="margin: 4px 0"/>
                          <div style="padding: 4px 8px; cursor: pointer;"
                               @mousedown="e => e.preventDefault()" @click="addUnit">
                            <plus-outlined/>
                            新增计量单位
                          </div>
                        </template>
                      </a-select>
                    </a-col>
                    <a-col :lg="9" :md="9" :sm="24" style="padding:0px; text-align:center">
                      <a-checkbox v-model:checked="unitChecked" @change="unitOnChange">多单位</a-checkbox>
                    </a-col>
                  </a-row>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="颜色" data-step="5" data-title="颜色"
                             data-intro="请填写商品的颜色，如果是多属性商品可以不填（下面有多属性开关）">
                  <a-input placeholder="请输入颜色" v-model:value="formState.productColor"/>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="基础重量" data-step="6"
                             data-title="基础重量"
                             data-intro="请填写基本单位对应的重量，用于计算按重量分摊费用时单据中各行商品分摊的费用成本">
                  <a-input-number style="width: 100%" placeholder="请输入基础重量(kg)"
                                  v-model:value="formState.productWeight"/>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="保质期" data-step="7"
                             data-title="保质期"
                             data-intro="保质期指的是商品的保质期(天)，主要针对带生产日期的，此类商品一般有批号">
                  <a-input-number style="width: 100%" placeholder="请输入保质期(天)"
                                  v-model:value="formState.productExpiryNum"/>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="{xs: { span: 24 },sm: { span: 4 }}"
                             :wrapperCol="{xs: { span: 24 },sm: { span: 20 }}" label="类别"
                             data-step="8" data-title="类别"
                             data-intro="类别需要在【商品类别】页面进行录入，录入之后在此处进行调用">
                  <a-tree-select style="width:100%" :dropdownStyle="{maxHeight:'200px',overflow:'auto'}" allow-clear
                                 :treeData="categoryTree.value" v-model:value="formState.productCategoryId"
                                 placeholder="请选择类别">
                  </a-tree-select>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="序列号" data-step="9"
                             data-title="序列号"
                             data-intro="此处是商品的序列号开关，如果选择了有，则在采购入库单据需要录入该商品的序列号，在销售出库单据需要选择该商品的序列号进行出库">
                  <a-tooltip title="如果选择为有，则在采购入库单需要录入该商品的序列号">
                    <a-select placeholder="有无序列号" v-model:value="formState.enableSerialNumber">
                      <a-select-option value="1">有</a-select-option>
                      <a-select-option value="0">无</a-select-option>
                    </a-select>
                  </a-tooltip>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="批号" data-step="10" data-title="批号"
                             data-intro="此处是商品的批号开关，如果选择了有，则在采购入库单据需要录入该商品的批号和有效期，在销售出库单据需要选择该商品的批号进行出库">
                  <a-tooltip title="如果选择为有，则在采购入库单需要录入该商品的批号和有效期">
                    <a-select placeholder="有无批号" v-model:value="formState.enableBatchNumber">
                      <a-select-option value="1">有</a-select-option>
                      <a-select-option value="0">无</a-select-option>
                    </a-select>
                  </a-tooltip>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="仓位货架" data-step="11"
                             data-title="仓位货架"
                             data-intro="仓位货架指的是仓库中的仓位和货架号，主要适用于仓库较大的场景，方便查找商品的准确位置">
                  <a-input style="width: 100%" placeholder="请输入仓位货架" v-model:value="formState.warehouseShelves"/>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="{xs: { span: 24 },sm: { span: 4 }}"
                             :wrapperCol="{xs: { span: 24 },sm: { span: 20 }}" label="多属性" data-step="12"
                             data-title="多属性"
                             v-show="manySkuItem"
                             data-intro="多属性是针对的sku商品（比如服装、鞋帽行业），此处开关如果启用就可以在下方进行多sku的配置，配置具体的颜色、尺码之类的组合">
                  <a-tooltip title="多属性针对服装、鞋帽等行业，需要先录入单位才能激活此处输入框">
                    <a-tag class="tag-info" v-if="!manySkuStatus">需要先录入单位才能激活</a-tag>
                    <a-select mode="multiple" showSearch optionFilterProp="children"
                              placeholder="请选择多属性（可多选）" @change="onManySkuChange" v-show="manySkuStatus">
                      <a-select-option v-for="(item,index) in productAttributeList" :key="index" :value="item.id"
                                       :disabled="item.disabled">
                        {{ item.attributeName }}
                      </a-select-option>
                    </a-select>
                  </a-tooltip>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24" v-if="manySkuSelected>=1">
                <a-form-item :labelCol="{xs: { span: 24 },sm: { span: 4 }}"
                             :wrapperCol="{xs: { span: 24 },sm: { span: 20 }}" :label="skuOneTitle">
                  <a-select mode="multiple" v-model="skuOne" showSearch
                            placeholder="请选择（可多选）" @change="onSkuChange($event, skuOne)">
                    <a-select-option v-for="(item,index) in skuOneList" :key="index" :value="item.value">
                      {{ item.value }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="12" :sm="24" v-if="manySkuSelected>=2">
                <a-form-item :labelCol="{xs: { span: 24 },sm: { span: 4 }}"
                             :wrapperCol="{xs: { span: 24 },sm: { span: 20 }}" :label="skuTwoTitle">
                  <a-select mode="multiple" v-model="skuTwo" showSearch
                            placeholder="请选择（可多选）" @change="onSkuChange($event, skuTwo)">
                    <a-select-option v-for="(item,index) in skuTwoList" :key="index" :value="item.value">
                      {{ item.value }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="12" :sm="24" v-if="manySkuSelected>=3">
                <a-form-item :labelCol="{xs: { span: 24 },sm: { span: 4 }}"
                             :wrapperCol="{xs: { span: 24 },sm: { span: 20 }}" :label="skuThreeTitle">
                  <a-select mode="multiple" v-model="skuThree" showSearch
                            placeholder="请选择（可多选）" @change="onSkuChange($event, skuThree)">
                    <a-select-option v-for="(item,index) in skuThreeList" :key="index" :value="item.value">
                      {{ item.value }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
            <div style="margin-top:8px;" id="productDetailModal">
              <div style="margin-bottom: 16px">
                <a-button type="primary" @click="addRow">
                  + 插入一行
                </a-button>
                <a-button style="margin-left: 8px" danger @click="deleteRows" type="primary"> 删除选中行</a-button>
                <a-button style="margin-left: 8px" @click="batchSetPrice('purchase')">采购价-批量</a-button>
                <a-button style="margin-left: 8px" @click="batchSetPrice('retail')">零售价-批量</a-button>
                <a-button style="margin-left: 8px" @click="batchSetPrice('sale')">销售价-批量</a-button>
                <a-button style="margin-left: 8px" @click="batchSetPrice('low')">最低售价-批量</a-button>
                <span style="margin-left: 8px">
               </span>
              </div>
              <div>
                <a-table :loading="meTable.loading"
                         :columns="meTable.columns"
                         :dataSource="meTable.dataSource"
                         bordered
                         :row-selection="rowSelection"
                         :scroll="{ x: '100%', y: 300 }">
                  <template #bodyCell="{ column, record }">
                    <template v-if="editableData[record.key]">
                      <a-input v-model:value="editableData[record.key][column.key]"
                               :placeholder="`请输入${getColumnTitle(column)}`"
                               @change="meTableValueChange(record.key)"/>
                    </template>
                  </template>
                </a-table>
              </div>
              <batch-set-price-modal ref="priceModalForm" @ok="batchSetPriceModalFormOk"></batch-set-price-modal>
            </div>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="24" :md="24" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="{xs: { span: 24 },sm: { span: 24 }}" label="">
                  <a-input v-model:vale="formState.remark" placeholder="请输入备注" style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane key="2" tab="扩展信息" forceRender>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="6" :sm="6">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" :label="productInfo.mfrs">
                  <a-input v-model:value="formState.productManufacturer"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="6" :sm="6">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" :label="productInfo.otherField1">
                  <a-input v-model:value="formState.otherFieldOne"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="6" :sm="6">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" :label="productInfo.otherField2">
                  <a-input v-model:value="formState.otherFieldTwo"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="6" :md="6" :sm="6">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" :label="productInfo.otherField3">
                  <a-input v-model:value="formState.otherFieldThree"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane key="3" tab="库存数量" forceRender>
            <a-row class="form-row" :gutter="24">
              <a-button style="margin: 0px 0px 8px 0px" @click="batchSetStock('initStock')">期初库存-批量</a-button>
              <a-button style="margin-left: 8px" @click="batchSetStock('lowSafeStock')">最低安全库存-批量</a-button>
              <a-button style="margin-left: 8px" @click="batchSetStock('highSafeStock')">最高安全库存-批量</a-button>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-table :loading="stock.loading"
                       :columns="stock.columns"
                       :dataSource="stock.dataSource"
                       :rowNumber="true"
                       bordered
                       :scroll="{ x: '100%', y: 300 }">
                <template #bodyCell="{ column, text, record }">
                  <template v-if="column.key === 'warehouseName'">
                    <a-input v-model:value="editStockData[record.key][column.key]" readonly/>
                  </template>
                  <template v-else-if="editStockData[record.key]">
                    <a-input v-model:value="editStockData[record.key][column.key]"
                             :placeholder="`请输入${getColumnTitle(column)}`"
                             @change="stockTableValueChange(record.key)"/>
                  </template>
                </template>
              </a-table>
            </a-row>
            <batch-set-stock-modal ref="stockModalForm" @ok="batchSetStockModalFormOk"></batch-set-stock-modal>
          </a-tab-pane>
          <a-tab-pane key="4" tab="图片信息" forceRender>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="18" :md="18" :sm="24">
                <a-form-item :labelCol="{xs: { span: 24 },sm: { span: 3 }}"
                             :wrapperCol="{xs: { span: 24 },sm: { span: 20 }}" label="图片信息">
                  <a-upload
                      v-model:file-list="fileList"
                      :custom-request="uploadImage"
                      :before-upload="beforeUpload"
                      list-type="picture-card"
                      :max-count="4"
                      @preview="handlePreview"
                      @change="handleChange"
                      multiple
                  >
                    <div v-if="fileList.length < 4">
                      <plus-outlined/>
                      <div style="margin-top: 8px">上传图片</div>
                    </div>
                  </a-upload>
                  <a-modal :open="previewVisible" :title="previewTitle" :footer="null" @cancel="handleImageViewCancel">
                    <img alt="example" style="width: 100%" :src="previewImage"/>
                  </a-modal>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="6" :sm="24"></a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="18" :md="18" :sm="24">
                <a-form-item :labelCol="{xs: { span: 24 },sm: { span: 3 }}"
                             :wrapperCol="{xs: { span: 24 },sm: { span: 20 }}" label="上传提示">
                  图片最多4张，且单张大小不超过2M
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="6" :sm="24"></a-col>
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </a-form>
    </a-spin>
  </a-modal>
  <UnitModal @register="registerModal" @success="handleUnitSuccess"/>
</template>

<script lang="ts">
import {defineComponent, reactive, ref, watch, Ref} from 'vue';
import {PlusOutlined} from '@ant-design/icons-vue';
import {Textarea, UploadProps,} from 'ant-design-vue';
import {
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
  UploadChangeParam,
} from "ant-design-vue";
import {cloneDeep} from 'lodash-es';
import {getUnitList} from "/@/api/product/productUnit";
import {getCategoryList} from "/@/api/product/productCategory";
import {ProductUnitQueryReq} from "/@/api/product/model/productUnitModel";
import {DefaultOptionType} from "ant-design-vue/es/vc-tree-select/TreeSelect";
import {getProductCode, getProductInfoDetail, addOrUpdateProduct} from "@/api/product/product";
import {getAttributeById, getAttributeList} from "@/api/product/productAttribute";
import {useMessage} from "@/hooks/web/useMessage";
import BatchSetPriceModal from "@/views/product/info/components/BatchSetPriceModal.vue";
import BatchSetStockModal from "@/views/product/info/components/BatchSetStockModal.vue";
import {uploadOss} from "@/api/basic/common";
import {getWarehouse} from "@/api/basic/warehouse";
import UnitModal from "@/views/product/units/components/UnitModal.vue";
import {useModal} from "@/components/Modal";

import {AddProductImageReq, AddProductReq} from "@/api/product/model/productModel";
import {ProductCategoryResp} from '/@/api/product/model/productCategoryModel';
import {ProductAttributeListReq} from "@/api/product/model/productAttributeModel";
import {ProductAttributeModel, ProductStockModel, Unit} from "@/views/product/info/model/productInfoModel";
import {meTable, stock, productInfo, formState} from "@/views/product/info/info.data";

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
  name: 'ProductInfoModal',
  emits: ['success', 'cancel', 'error'],
  components: {
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
    'plus-outlined': PlusOutlined,
    BatchSetPriceModal,
    BatchSetStockModal,
    UnitModal,
    VNodes
  },
  setup(_, context) {
    const {createMessage} = useMessage();
    const productStandard = ref<String>('');
    const productName = ref<String>('');
    const confirmLoading = ref<boolean>(false);
    const title = ref('');
    const open = ref(false);
    const prefixNo = ref('product');
    const manySkuSelected = ref<number>(0);
    const unitChecked = ref(false);
    const manyUnitStatus = ref(false);
    const manySkuStatus = ref(false);
    const manySkuItem = ref(true);

    const unitStatus = ref<boolean>(false);
    const switchDisabled = ref<boolean>(false);
    const productCodeSwitch = ref<boolean>(false);

    const skuOneTitle = ref<string>('属性1');
    const skuTwoTitle = ref<string>('属性2');
    const skuThreeTitle = ref<string>('属性3');

    const skuOne = reactive([]);
    const skuTwo = reactive([]);
    const skuThree = reactive([]);

    const priceModalForm = ref<BatchSetPriceModal>();
    const stockModalForm = ref<BatchSetStockModal>();
    const [registerModal, {openModal}] = useModal();

    const model = ref({});


    const labelCol = reactive({
      xs: {span: 24},
      sm: {span: 8},
    })
    const wrapperCol = reactive({
      xs: {span: 24},
      sm: {span: 16},
    })

    function addUnit() {
      openModal(true, {
        isUpdate: false,
      });
    }

    function handleCancel() {
      close();
      if (formState.productId) {
        clearData()
      }
      context.emit('cancel');
    }

    function closeModal() {
      open.value = false
    }

    function openProductInfoModal(id: string | undefined) {
      open.value = true
      loadUnitListData()
      loadCategoryTreeData()
      loadAttributeTreeData()
      if (id) {
        title.value = '修改商品信息'
        loadProductInfoDetail(id)
        manySkuStatus.value = false
        manySkuItem.value = false
        manySkuSelected.value = 0
      } else {
        title.value = '新增商品'
        loadWarehouse()
        manySkuItem.value = true
      }
    }

    function unitOnChange(event) {
      const isChecked = event.target.checked;
      if (isChecked) {
        unitStatus.value = true;
        manyUnitStatus.value = false;
        unitChecked.value = true
        formState.productUnit = ''
      } else {
        unitStatus.value = false;
        manyUnitStatus.value = true;
        unitChecked.value = false
      }
    }

    function onlyUnitOnChange(e) {
      manySkuStatus.value = !!e.target.value;
      const dataSource = meTable.dataSource
      for (let i = 0; i < dataSource.length; i++) {
        dataSource[i].productUnit = e.target.value
        editableData[meTable.dataSource[i].key] = meTable.dataSource[i]
      }
    }

    const meTableValueChange = (record, key) => {
      if (!editableData[record.key]) {
        editableData[record.key] = {};
      }
      if (meTable.dataSource) {
        const filteredItems = meTable.dataSource.filter(item => record === item.key);
        if (filteredItems.length > 0) {
          const filteredItem = filteredItems[0];
          editableData[record][key] = cloneDeep(filteredItem[key]);
          Object.assign(filteredItem, editableData[record]);
          delete editableData[record][key];
        }
      }
    };

    const stockTableValueChange = (record, dataIndex, value, key) => {
      if (!editStockData[record.key]) {
        editStockData[record.key] = {};
      }
      if (stock.dataSource) {
        const filteredItems = stock.dataSource.filter(item => record === item.key);
        if (filteredItems.length > 0) {
          const filteredItem = filteredItems[0];
          editStockData[record][key] = cloneDeep(filteredItem[key]);
          Object.assign(filteredItem, editStockData[record]);
          delete editStockData[record][key];
        }
      }
    };

    async function loadUnitListData() {
      const unitObject: ProductUnitQueryReq = {
        computeUnit: undefined,
        page: 1,
        pageSize: 1000,
      }
      const unitListResult = await getUnitList(unitObject)
      if (unitListResult) {
        unitList.value = unitListResult.data.records
      }
    }

    function buildTree(flatData: ProductCategoryResp[], parentId: string | null): DefaultOptionType[] {
      const tree: DefaultOptionType[] = [];
      for (const item of flatData) {
        if (item.parentId === parentId) {
          const children = buildTree(flatData, item.id.toString());
          const defaultOption: DefaultOptionType = {
            value: item.id,
            title: item.categoryName,
            label: item.categoryName,
            key: item.id,
            children: children.length > 0 ? children : undefined,
          };
          tree.push(defaultOption);
        }
      }
      return tree;
    }

    const categoryTree: DefaultOptionType[] = reactive([]);

    async function loadCategoryTreeData() {
      const categoryTreeResult = await getCategoryList();
      if (categoryTreeResult) {
        const flatData = categoryTreeResult.data;
        categoryTree.value = buildTree(flatData, null);
      }
    }

    const productAttributeList = ref<ProductAttributeModel[]>([]);

    async function loadAttributeTreeData() {
      const attributeObject: ProductAttributeListReq = {
        attributeName: undefined,
        page: 1,
        pageSize: 1000,
      }
      const attributeListResult = await getAttributeList(attributeObject)
      if (attributeListResult) {
        productAttributeList.value = attributeListResult.data.records
      }
    }

    const unitList = ref<Unit[]>([])

    function manyUnitOnChange(value: number | string) {
      let unitArr = unitList.value;
      let basicUnit = '';
      let otherUnit = '';
      let otherUnitTwo = '';
      let otherUnitThree = '';

      for (let i = 0; i < unitArr.length; i++) {
        if (unitArr[i].id === value) {
          basicUnit = unitArr[i].basicUnit;
          otherUnit = unitArr[i].otherUnit;
          if (unitArr[i].otherUnitTwo) {
            otherUnitTwo = unitArr[i].otherUnitTwo;
          }
          if (unitArr[i].otherUnitThree) {
            otherUnitThree = unitArr[i].otherUnitThree;
          }
        }
      }

      for (let i = 0; i < meTable.dataSource.length; i++) {
        if (i === 0) {
          meTable.dataSource[i].productUnit = basicUnit
        } else if (i === 1) {
          meTable.dataSource[i].productUnit = otherUnit
        } else if (i === 2) {
          meTable.dataSource[i].productUnit = otherUnitTwo
        } else if (i === 3) {
          meTable.dataSource[i].productUnit = otherUnitThree
        }
      }
      for (let i = 0; i < meTable.dataSource.length; i++) {
        editableData[meTable.dataSource[i].key] = meTable.dataSource[i]
      }
    }

    const skuOneData: Ref<string[]> = ref([]);
    const skuTwoData: Ref<string[]> = ref([]);
    const skuThreeData: Ref<string[]> = ref([]);
    const skuArr: Ref<string[]> = ref([]);

    function onSkuChange(value, array) {
      if (array === skuOne) {
        skuOneData.value = value
      } else if (array === skuTwo) {
        skuTwoData.value = value
      } else if (array === skuThree) {
        skuThreeData.value = value
      }
      autoSkuList();
    }

    function autoSkuList() {
      let arr: string[] = [];
      if (formState.productUnit) {
        if (skuOneData.value.length > 0 && skuTwoData.value.length > 0 && skuThreeData.value.length > 0) {
          for (let i = 0; i < skuOneData.value.length; i++) {
            for (let j = 0; j < skuTwoData.value.length; j++) {
              for (let k = 0; k < skuThreeData.value.length; k++) {
                arr.push(skuOneData.value[i] + '/' + skuTwoData.value[j] + '/' + skuThreeData.value[k]);
              }
            }
          }
        } else if (skuOneData.value.length > 0 && skuTwoData.value.length > 0) {
          for (let i = 0; i < skuOneData.value.length; i++) {
            for (let j = 0; j < skuTwoData.value.length; j++) {
              arr.push(skuOneData.value[i] + '/' + skuTwoData.value[j]);
            }
          }
        } else if (skuOneData.value.length > 0 && skuThreeData.value.length > 0) {
          for (let i = 0; i < skuOneData.value.length; i++) {
            for (let k = 0; k < skuThreeData.value.length; k++) {
              arr.push(skuOneData.value[i] + '/' + skuThreeData.value[k]);
            }
          }
        } else if (skuTwoData.value.length > 0 && skuThreeData.value.length > 0) {
          for (let j = 0; j < skuTwoData.value.length; j++) {
            for (let k = 0; k < skuThreeData.value.length; k++) {
              arr.push(skuTwoData.value[j] + '/' + skuThreeData.value[k]);
            }
          }
        } else if (skuOneData.value.length > 0) {
          arr = skuOneData.value;
        } else if (skuTwoData.value.length > 0) {
          arr = skuTwoData.value;
        } else if (skuThreeData.value.length > 0) {
          arr = skuThreeData.value;
        }
        skuArr.value = arr;

        // 设置sku变化插入多属性
        if (skuArr.value.length > 0) {
          meTable.dataSource = [] // 清空meTableData数组
          for (let i = 0; i < skuArr.value.length; i++) {
            const newRowData = {key: i, productUnit: formState.productUnit, multiAttribute: skuArr.value[i]}
            meTable.dataSource.push(newRowData);
          }
          meTable.dataSource.forEach(row => {
            edit(row.key);
          });
        }

      } else {
        createMessage.warn('如果使用多属性，请填写单位（注意不要勾选多单位）');
        productCodeSwitch.value = false
      }
    }


    function loadProductInfoDetail(id) {
      getProductInfoDetail(id).then(res => {
        if (res && res.code === '00000') {
          let data = res.data
          if (data) {
            // 将data中的数据赋值给formState
            formState.productId = data.productId
            formState.productName = data.productName
            formState.productStandard = data.productStandard
            formState.productModel = data.productModel
            formState.productUnit = data.productUnit
            formState.productUnitId = data.productUnitId
            formState.productColor = data.productColor
            formState.productWeight = data.productWeight
            formState.productExpiryNum = data.productExpiryNum
            formState.productCategoryId = data.productCategoryId
            formState.enableSerialNumber = data.enableSerialNumber
            formState.enableBatchNumber = data.enableBatchNumber
            formState.warehouseShelves = data.warehouseShelves
            formState.productManufacturer = data.productManufacturer
            formState.otherFieldOne = data.otherFieldOne
            formState.otherFieldTwo = data.otherFieldTwo
            formState.otherFieldThree = data.otherFieldThree
            formState.remark = data.remark

            if (data.productUnitId) {
              // 说明是多属性 选中多属性的checkbox 赋值给下拉框3
              unitStatus.value = true;
              manyUnitStatus.value = false;
              unitChecked.value = true
              formState.productUnit = ''
              formState.productUnitId = data.productUnitId
            } else {
              unitStatus.value = false;
              manyUnitStatus.value = true;
              unitChecked.value = false
              formState.productUnit = data.productUnit
            }

            if (data.priceList) {
              meTable.dataSource = []; // 清空meTableData数组
              stock.dataSource = []; // 清空stock数组
              for (let i = 0; i < data.priceList.length; i++) {
                const newRowData = {
                  key: i,
                  productPriceId: data.priceList[i].productPriceId,
                  barCode: data.priceList[i].barCode,
                  productUnit: data.priceList[i].productUnit,
                  multiAttribute: data.priceList[i].multiAttribute,
                  purchasePrice: data.priceList[i].purchasePrice,
                  retailPrice: data.priceList[i].retailPrice,
                  salesPrice: data.priceList[i].salesPrice,
                  lowSalesPrice: data.priceList[i].lowSalesPrice
                }
                meTable.dataSource.push(newRowData);
                data.priceList[i].stockList.forEach((item, index) => {
                  const stockData = {
                    key: item.productStockId,
                    productStockId: item.productStockId,
                    warehouseId: item.warehouseId,
                    warehouseName: item.warehouseName + '(' + data.priceList[i].barCode + '/' + data.priceList[i].productUnit + ')',
                    initStockQuantity: item.initStockQuantity,
                    lowStockQuantity: item.lowStockQuantity,
                    highStockQuantity: item.highStockQuantity
                  }
                  stock.dataSource.push(stockData);
                  editStock(stockData.key);
                })
              }
              meTable.dataSource.forEach(row => {
                edit(row.key);
              });
            }
            if (data.imageList) {
              fileList.value = [];
              for (let i = 0; i < data.imageList.length; i++) {
                const newRowData = {
                  uid: i.toString(),
                  productImageId: data.imageList[i].productImageId,
                  name: data.imageList[i].imageName,
                  status: 'done',
                  url: data.imageList[i].imageUrl,
                  thumbUrl: data.imageList[i].imageUrl,
                }
                fileList.value.push(newRowData);
              }
            }
          }
        }
      })
    }

    function loadWarehouse() {
      getWarehouse().then(res => {
        if (res && res.code === '00000') {
          let warehouseList: ProductStockModel[] = res.data
          if (warehouseList.length > 0) {
            stock.dataSource = [] as ProductStockModel[];
            for (let i = 0; i < warehouseList.length; i++) {
              const rowData = {
                key: warehouseList[i].id,
                warehouseId: warehouseList[i].id,
                warehouseName: warehouseList[i].warehouseName,
              }
              stock.dataSource.push(rowData);
            }
            stock.dataSource.forEach(row => {
              editStock(row.key);
            });
          }
        }
      })
    }

    watch(manySkuSelected, (value) => {
      if (value < 3) {
        for (let i = 0; i < productAttributeList.value.length; i++) {
          productAttributeList.value[i].disabled = false
        }
      } else {
        for (let i = 0; i < productAttributeList.value.length; i++) {
          productAttributeList.value[i].disabled = true
        }
      }
    });

    const skuOneList = ref()
    const skuTwoList = ref()
    const skuThreeList = ref()

    async function onManySkuChange(value) {
      manySkuSelected.value = value.length;
      if (value.length >= 1) {
        let skuOneId = value[0];
        const data = await getAttributeById(skuOneId);
        if (data) {
          console.info(data)
          skuOneList.value = data
          skuOneTitle.value = data[0].name;
        }
      } else {
        skuOneTitle.value = "";
        skuOneList.value = [];
      }

      if (value.length >= 2) {
        let skuTwoId = value[1];
        const data = await getAttributeById(skuTwoId);
        if (data) {
          skuTwoList.value = data
          skuTwoTitle.value = data[0].name;
        }
      } else {
        skuTwoTitle.value = "";
        skuTwoList.value = [];
      }

      if (value.length >= 3) {
        let skuThreeId = value[2];
        const data = await getAttributeById(skuThreeId);
        if (data) {
          skuThreeList.value = data
          skuThreeTitle.value = data[0].name;
        }
      } else {
        skuThreeTitle.value = "";
        skuThreeList.value = [];
      }
    }

    function batchSetPrice(type: string) {
      if (manySkuSelected.value > 0) {
        priceModalForm.value.add(type);
        priceModalForm.value.openPriceModal = true;
      } else {
        createMessage.warn('抱歉，您还没有选择多属性，开启多属性后才能批量设置金额');
      }
    }

    function batchSetStock(type: string) {
      stockModalForm.value!.add(type);
      stockModalForm.value!.openStockModal = true;
    }

    function batchSetPriceModalFormOk(price: number, batchType: string) {
      if (meTable.dataSource.length === 0) {
        createMessage.warn('请先录入条码、单位等信息！');
        return;
      }
      if (batchType === 'purchase') {
        for (let i = 0; i < meTable.dataSource.length; i++) {
          meTable.dataSource[i].purchasePrice = price
        }
      } else if (batchType === 'retail') {
        for (let i = 0; i < meTable.dataSource.length; i++) {
          meTable.dataSource[i].retailPrice = price
        }
      } else if (batchType === 'sale') {
        for (let i = 0; i < meTable.dataSource.length; i++) {
          meTable.dataSource[i].salesPrice = price
        }
      } else if (batchType === 'low') {
        for (let i = 0; i < meTable.dataSource.length; i++) {
          meTable.dataSource[i].lowSalesPrice = price
        }
      }
      meTable.dataSource.forEach(row => {
        edit(row.key);
      });
    }

    function batchSetStockModalFormOk(stockNumber: number, batchType: string) {
      if (batchType === 'initStock') {
        for (let i = 0; i < stock.dataSource.length; i++) {
          stock.dataSource[i].initStockQuantity = stockNumber
        }
      } else if (batchType === 'lowSafeStock') {
        for (let i = 0; i < stock.dataSource.length; i++) {
          stock.dataSource[i].lowStockQuantity = stockNumber
        }
      } else if (batchType === 'highSafeStock') {
        for (let i = 0; i < stock.dataSource.length; i++) {
          stock.dataSource[i].highStockQuantity = stockNumber
        }
      }
      stock.dataSource.forEach(row => {
        editStock(row.key);
      });

    }

    const editableData = reactive({});

    const addRow = () => {
      // Create a new row with a unique key
      const newRow: { key: number; productUnit?: string; } = {key: Date.now()};
      editableData[newRow.key] = cloneDeep(newRow);
      if (formState.productUnit) {
        newRow.productUnit = formState.productUnit
      }
      meTable.dataSource.push(newRow);
      edit(newRow.key);
    };

    const getColumnTitle = (column) => {
      return column.title.replace(/<[^>]+>/g, '');
    };

    const deleteKey = (key) => {
      delete editableData[key];
    };

    const edit = (key) => {
      const rowData = meTable.dataSource.find(item => item.key === key);
      if (rowData) {
        editableData[key] = cloneDeep(rowData);
      }
    };

    const editStock = (key) => {
      const rowData = stock.dataSource.find(item => item.key === key);
      if (rowData) {
        editStockData[key] = cloneDeep(rowData);
      }
    };

    const rowSelection = ref({
      selectedRowKeys: [],
      onChange: (selectedRowKeys) => {
        rowSelection.value.selectedRowKeys = selectedRowKeys;
      }
    });

    function deleteRows() {
      const selectedKeys = rowSelection.value.selectedRowKeys;
      meTable.dataSource = meTable.dataSource.filter(row => !selectedKeys.includes(row.key));
      rowSelection.value.selectedRowKeys = [];
    }

    const editStockData = reactive({})

    function getBase64(file: File) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
      });
    }

    const previewVisible = ref(false);
    const previewImage = ref('');
    const previewTitle = ref('');
    const fileList = ref<UploadProps['fileList']>([])


    function beforeUpload(file) {
      const isPNG =
          file.type === "image/png" ||
          file.type === "image/jpeg" ||
          file.type === "image/jpg" ||
          file.type === "image/gif" ||
          file.type === "image/bmp";
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isPNG) {
        createMessage.error(`${file.name}，该文件不是图片类型`);
        return isPNG || Upload.LIST_IGNORE
      }
      if (!isLt2M) {
        createMessage.error(`${file.name}，该文件超过2MB大小限制`);
        return isLt2M || Upload.LIST_IGNORE
      }
    }

    const handleChange = ({file, fileList}: UploadChangeParam) => {
      if (file.status !== 'uploading') {
      }
    };

    function uploadImage(options) {
      const {file, onSuccess, onError, onProgress} = options;
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

    const handlePreview = async (file: UploadProps['fileList'][number]) => {
      if (!file.url && !file.preview) {
        file.preview = (await getBase64(file.originFileObj)) as string;
      }
      previewImage.value = file.url || file.preview;
      previewVisible.value = true;
      previewTitle.value = file.name || file.url.substring(file.url.lastIndexOf('/') + 1);
    };

    const handleImageViewCancel = () => {
      previewVisible.value = false;
      previewTitle.value = '';
    }

    function clearData() {
      formState.productId = ''
      formState.productName = ''
      formState.productStandard = ''
      formState.productModel = ''
      formState.productUnit = ''
      formState.productUnitId = ''
      formState.productColor = ''
      formState.productWeight = ''
      formState.productExpiryNum = ''
      formState.productCategoryId = ''
      formState.enableSerialNumber = ''
      formState.enableBatchNumber = ''
      formState.warehouseShelves = ''
      formState.productManufacturer = ''
      formState.otherFieldOne = ''
      formState.otherFieldTwo = ''
      formState.otherFieldThree = ''
      formState.remark = ''
      unitChecked.value = false
      manyUnitStatus.value = false
      manySkuStatus.value = false
      unitStatus.value = false
      switchDisabled.value = false
      productCodeSwitch.value = false
      // 清除所有的sku
      manySkuSelected.value = 0
      skuOneList.value = []
      skuTwoList.value = []
      skuThreeList.value = []
      skuOne.splice(0, skuOne.length);
      skuTwo.splice(0, skuTwo.length);
      skuThree.splice(0, skuThree.length);
      meTable.dataSource = []
      stock.dataSource = []
      fileList.value = []
    }

    async function handleOk() {
      if (!formState.productName) {
        createMessage.error('请输入商品名称');
        return;
      }
      if (unitChecked.value) {
        if (!formState.productUnitId) {
          createMessage.error('请选择商品单位');
          return;
        }
      } else if (!formState.productUnit) {
        createMessage.error('请输入商品单位');
        return;
      }
      if (meTable.dataSource.length === 0) {
        createMessage.error('请插入一行数据，录入商品条码价格信息');
        return;
      }
      // 检查库存信息是否为空
      if (stock.dataSource.length === 0) {
        createMessage.warn('系统检查到您没有仓库信息，请在基本资料菜单栏->添加至少1条仓库信息');
        return;
      }

      const imageList: AddProductImageReq[] = [];
      if (fileList && fileList.value) {
        for (let i = 0; i < fileList.value.length; i++) {
          if (fileList.value[i].url) {
            const image = {
              productImageId: null,
              uid: fileList.value[i].uid,
              type: null,
              status: fileList.value[i].status || null,
              imageName: fileList.value[i].name,
              imageUrl: fileList.value[i].url || null,
              imageSize: null,
            }
            imageList.push(image)
          } else {
            const image = {
              uid: fileList.value[i].uid,
              type: fileList.value[i].type,
              status: fileList.value[i].status,
              imageName: fileList.value[i].name,
              imageUrl: fileList.value[i].response.data[0] as string,
              imageSize: fileList.value[i].size,
            }
            imageList.push(image)
          }
        }
      }

      const product: AddProductReq = {
        productId: formState.productId,
        productName: formState.productName,
        productStandard: formState.productStandard,
        productModel: formState.productModel,
        productUnit: formState.productUnit,
        productUnitId: formState.productUnitId,
        productColor: formState.productColor,
        productWeight: formState.productWeight,
        productExpiryNum: formState.productExpiryNum,
        productCategoryId: formState.productCategoryId,
        enableSerialNumber: formState.enableSerialNumber,
        enableBatchNumber: formState.enableBatchNumber,
        warehouseShelves: formState.warehouseShelves,
        productManufacturer: formState.productManufacturer,
        otherFieldOne: formState.otherFieldOne,
        otherFieldTwo: formState.otherFieldTwo,
        otherFieldThree: formState.otherFieldThree,
        remark: formState.remark,
        priceList: meTable.dataSource,
        stockList: stock.dataSource,
        imageList: imageList
      }

      const addOrUpdateProductResult = await addOrUpdateProduct(product);
      if (addOrUpdateProductResult.code === 'P0010') {
        context.emit('success');
        closeModal();
        clearData();
      } else if (addOrUpdateProductResult.code === 'P0011') {
        context.emit('success');
        closeModal();
        clearData();
      }
    }

    function handleUnitSuccess() {
      // 重新渲染单位下拉框数据
      loadUnitListData()
    }

    return {
      confirmLoading,
      openProductInfoModal,
      closeModal,
      handleCancel,
      title,
      open,
      prefixNo,
      labelCol,
      wrapperCol,
      manySkuSelected,
      unitChecked,
      unitStatus,
      manyUnitStatus,
      manySkuStatus,
      manySkuItem,
      switchDisabled,
      productCodeSwitch,
      onSkuChange,
      unitList,
      skuOneList,
      skuTwoList,
      skuThreeList,
      productAttributeList,
      addUnit,
      unitOnChange,
      onlyUnitOnChange,
      manyUnitOnChange,
      skuOneTitle,
      skuTwoTitle,
      skuThreeTitle,
      categoryTree,
      onManySkuChange,
      batchSetStock,
      model,
      addRow,
      editableData,
      getColumnTitle,
      deleteKey,
      deleteRows,
      rowSelection,
      meTable,
      productInfo,
      fileList,
      skuOne,
      skuTwo,
      skuThree,
      productStandard,
      productName,
      batchSetPrice,
      priceModalForm,
      batchSetPriceModalFormOk,
      stock,
      editStockData,
      stockModalForm,
      batchSetStockModalFormOk,
      beforeUpload,
      uploadImage,
      handleChange,
      handlePreview,
      previewVisible,
      previewImage,
      previewTitle,
      handleImageViewCancel,
      handleOk,
      formState,
      meTableValueChange,
      stockTableValueChange,
      VNodes,
      registerModal,
      handleUnitSuccess
    };
  },
})
</script>

<style scoped>
.tag-info {
  font-size: 14px;
  height: 32px;
  line-height: 32px;
  width: 100%;
  padding: 0 11px;
  color: #bbb;
  background-color: #ffffff;
}

.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}
</style>