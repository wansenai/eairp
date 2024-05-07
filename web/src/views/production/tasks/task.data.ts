import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {h, reactive, ref} from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {MeTable, ProductInfo} from "@/views/product/info/model/productInfoModel";
import {getCustomerList} from "@/api/basic/customer";

const { t } = useI18n();

const columns: BasicColumn[] = [
    {
        title: '生产单号',
        dataIndex: 'productBarcode',
        width: 100,
    },
    {
        title: '产品条码',
        dataIndex: 'productModel',
        width: 100,
    },
    {
        title: '产品名称',
        dataIndex: 'productStandard',
        width: 110,
    },
    {
        title: '产品型号',
        dataIndex: 'productColor',
        width: 110,
    },
    {
        title: '产品规格',
        dataIndex: 'productCategoryName',
        width: 110,
    },
    {
        title: '外协单位',
        dataIndex: 'productUnit',
        width: 90,
    },
    {
        title: '订购数量',
        dataIndex: 'productStock',
        width: 70,
    },
    {
        title: '生产数量',
        dataIndex: 'purchasePrice',
        width: 70,
    },
    {
        title: '操作员',
        dataIndex: 'lowPrice',
        width: 80,
    },
    {
        title: '计划生产日期',
        dataIndex: 'createTime',
        width: 150,
    },
    {
        title: '单据日期',
        dataIndex: 'createTime',
        width: 150,
    },
    {
        title: '状态',
        dataIndex: 'status',
        width: 80,
    },
]


const extendPriceColumn: BasicColumn[] = [
    {
        title: '条码',
        dataIndex: 'barCode',
        width: 80,
    },
    {
        title: '名称',
        dataIndex: 'productName',
        width: 100,
    },
    {
        title: '分类',
        dataIndex: 'productCategoryName',
        width: 100,
    },
    {
        title: '所属仓库',
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: '规格',
        dataIndex: 'productStandard',
        width: 80,
    },
    {
        title: '型号',
        dataIndex: 'productModel',
        width: 100,
    },
    {
        title: '颜色',
        dataIndex: 'productColor',
        width: 60,
    },
    {
        title: '类别',
        dataIndex: 'productCategoryName',
        width: 80,
    },
    {
        title: '单位',
        dataIndex: 'productUnit',
        width: 80,
    },
    {
        title: '库存',
        dataIndex: 'stock',
        width: 60,
    },
    {
        title: '零售价',
        dataIndex: 'retailPrice',
        width: 60,
    },
    {
        title: '销售价',
        dataIndex: 'salePrice',
        width: 60,
    },
    {
        title: '采购价',
        dataIndex: 'purchasePrice',
        width: 60,
    },
    {
        title: '扩展信息',
        dataIndex: 'extendInfo',
        width: 60,
    },
]

const searchFormSchema: FormSchema[] = [
    {
        label: '生产单号',
        field: 'productCategoryId',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        }
    },
    {
        label: '产品信息',
        field: 'keywords',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
        helpMessage: ['请输入产品名称、型号、规格'],
    },
    {
        field: '[startDate, endDate]',
        label: '单据日期',
        component: 'RangePicker',
        componentProps: {
            format: 'YYYY/MM/DD',
            placeholder: ['开始日期', '结束日期'],
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: '操作人',
        field: 'customerId',
        component: 'ApiSelect',
        componentProps: {
            api: getCustomerList,
            resultField: 'data',
            labelField: 'customerName',
            valueField: 'id',
        },
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
]

const meTable: MeTable = reactive({
    loading: false,
    dataSource: ref([]),
    columns: [
        {
            title: '条码',
            key: 'barCode',
            type: 'inputNumber',
            placeholder: '请输入${title}',
        },
        {
            title: '单位',
            key: 'productUnit',
            type: 'input',
            placeholder: '请输入${title}',
            validateRules: [{ required: true, message: '单位不能为空' }],
        },
        {
            title: '多属性',
            key: 'multiAttribute',
            type: 'input',
            readonly: true,
            placeholder: '请输入${title}',
        },
        {
            title: '采购价',
            key: 'purchasePrice',
            type: 'inputNumber',
            defaultValue: '',
            placeholder: '请输入${title}',
        },
        {
            title: '零售价',
            key: 'retailPrice',
            type: 'inputNumber',
            defaultValue: '',
            placeholder: '请输入${title}',
        },
        {
            title: '销售价',
            key: 'salesPrice',
            type: 'inputNumber',
            defaultValue: '',
            placeholder: '请输入${title}',
        },
        {
            title: '最低售价',
            key: 'lowSalesPrice',
            type: 'inputNumber',
            defaultValue: '',
            placeholder: '请输入${title}',
        },
    ],
});

const stock: any = reactive({
    loading: false,
    dataSource: ref([]),
    columns: [
        {
            title: '仓库 (商品条码/商品单位)',
            key: 'warehouseName',
            type: 'input',
        },
        {
            title: '期初库存数量',
            key: 'initStockQuantity',
            type: 'inputNumber',
            placeholder: '请输入${title}',
        },
        {
            title: '最低安全库存数量',
            key: 'lowStockQuantity',
            type: 'inputNumber',
            placeholder: '请输入${title}',
        },
        {
            title: '最高安全库存数量',
            key: 'highStockQuantity',
            type: 'inputNumber',
            placeholder: '请输入${title}',
        },
    ],
});

const formState: any = reactive({
    productId: '',
    productName: '',
    productStandard: '',
    productModel: '',
    productUnit: '',
    productUnitId: null,
    productColor: '',
    productWeight: null,
    productExpiryNum: null,
    productCategoryId: null,
    enableSerialNumber: null,
    enableBatchNumber: null,
    remark: '',
    warehouseShelves: '',
    productManufacturer: '',
    otherFieldOne: '',
    otherFieldTwo: '',
    otherFieldThree: '',
});

const productInfo: ProductInfo = reactive({
    mfrs: '制造商',
    otherField1: '自定义1',
    otherField2: '自定义2',
    otherField3: '自定义3',
});


export {
    columns,
    extendPriceColumn,
    searchFormSchema,
    meTable,
    stock,
    formState,
    productInfo,
};