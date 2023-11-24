import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {h, reactive, ref} from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {updateProductStatus} from "@/api/product/product";
import {getCategoryList} from "@/api/product/productCategory";
import {MeTable, ProductInfo, Stock} from "@/views/product/info/model/productInfoModel";
import {AddProductReq} from "@/api/product/model/productModel";
import {getWarehouseList} from "@/api/basic/warehouse";

const { t } = useI18n();

const columns: BasicColumn[] = [
    {
        title: '条码',
        dataIndex: 'productBarcode',
        width: 80,
    },
    {
        title: '名称',
        dataIndex: 'productName',
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
        dataIndex: 'productStock',
        width: 60,
    },
    {
        title: '采购价',
        dataIndex: 'purchasePrice',
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
        title: '最低售价',
        dataIndex: 'lowPrice',
        width: 60,
    },
    {
        title: '状态',
        dataIndex: 'status',
        width: 80,
        customRender: ({ record }) => {
            if (!Reflect.has(record, 'pendingStatus')) {
                record.pendingStatus = false;
            }
            return h(Switch, {
                checked: record.status === 0,
                checkedChildren: t('common.on'),
                unCheckedChildren: t('common.off'),
                loading: record.pendingStatus,
                onChange(checked, _) {
                    const {createMessage} = useMessage();
                    if (record.id == 1) {
                        createMessage.warn(t('common.notice'));
                        return;
                    }
                    record.pendingStatus = true;
                    const newStatus = checked ? 0 : 1;
                    updateProductStatus([record.id], newStatus )
                        .then(() => {
                            record.status = newStatus;
                        })
                        .finally(() => {
                            record.pendingStatus = false;
                        });
                },
            });
        }
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
        width: 80,
    }
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
        title: '扩展信息',
        dataIndex: 'extendInfo',
        width: 60,
    },
]

const searchFormSchema: FormSchema[] = [
    {
        label: '商品类别',
        field: 'productCategoryId',
        component: 'ApiTreeSelect',
        colProps: {
            xl: 8,
            xxl: 8,
        },
        componentProps: {
            api: getCategoryList,
            resultField: 'data',
            labelField: 'categoryName',
            valueField: 'id',
        },
    },
    {
        label: '关键词',
        field: 'keywords',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: '序列号',
        field: 'enableSerialNumber',
        component: 'Select',
        colProps: {
            xl: 12,
            xxl: 8,
        },
        componentProps: {
            options: [
                { label: '无', value: 0, key: 0 },
                { label: '有', value: 1, key: 1 },
            ],
        },
    },
    {
        label: '批次号',
        field: 'enableBatchNumber',
        component: 'Select',
        colProps: {
            xl: 12,
            xxl: 8,
        },
        componentProps: {
            options: [
                { label: '无', value: 0, key: 0 },
                { label: '有', value: 1, key: 1 },
            ],
        },
    },
    {
        label: '仓库',
        field: 'warehouseId',
        component: 'ApiTreeSelect',
        colProps: {
            xl: 12,
            xxl: 8,
        },
        componentProps: {
            api: getWarehouseList,
            resultField: 'data',
            labelField: 'warehouseName',
            valueField: 'id',
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

const stock: Stock = reactive({
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

const formState: AddProductReq = reactive({
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