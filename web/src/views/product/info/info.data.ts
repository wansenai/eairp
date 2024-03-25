import {FormSchema} from "@/components/Form";
import {BasicColumn} from "@/components/Table";
import {h, reactive, ref} from 'vue';
import {Switch} from "ant-design-vue";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "@/hooks/web/useI18n";
import {updateProductStatus} from "@/api/product/product";
import {getCategoryList} from "@/api/product/productCategory";
import {MeTable, ProductInfo} from "@/views/product/info/model/productInfoModel";
import {getWarehouseList} from "@/api/basic/warehouse";

const { t } = useI18n();

const columns: BasicColumn[] = [
    {
        title: t('product.info.table.barCode'),
        dataIndex: 'productBarcode',
        width: 80,
    },
    {
        title: t('product.info.table.productName'),
        dataIndex: 'productName',
        width: 100,
    },
    {
        title: t('product.info.table.productStandard'),
        dataIndex: 'productStandard',
        width: 80,
    },
    {
        title: t('product.info.table.productModel'),
        dataIndex: 'productModel',
        width: 100,
    },
    {
        title: t('product.info.table.productColor'),
        dataIndex: 'productColor',
        width: 60,
    },
    {
        title: t('product.info.table.productCategory'),
        dataIndex: 'productCategoryName',
        width: 80,
    },
    {
        title: t('product.info.table.productUnit'),
        dataIndex: 'productUnit',
        width: 80,
    },
    {
        title: t('product.info.table.productStock'),
        dataIndex: 'productStock',
        width: 60,
    },
    {
        title: t('product.info.table.purchasePrice'),
        dataIndex: 'purchasePrice',
        width: 60,
    },
    {
        title: t('product.info.table.retailPrice'),
        dataIndex: 'retailPrice',
        width: 60,
    },
    {
        title: t('product.info.table.salesPrice'),
        dataIndex: 'salePrice',
        width: 60,
    },
    {
        title: t('product.info.table.lowestSellPrice'),
        dataIndex: 'lowPrice',
        width: 60,
    },
    {
        title: t('product.info.table.status'),
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
        title: t('product.info.table.createTime'),
        dataIndex: 'createTime',
        width: 80,
    }
]


const extendPriceColumn: BasicColumn[] = [
    {
        title: t('product.info.table.barCode'),
        dataIndex: 'barCode',
        width: 80,
    },
    {
        title: t('product.info.table.productName'),
        dataIndex: 'productName',
        width: 100,
    },
    {
        title: t('product.info.table.productCategory'),
        dataIndex: 'productCategoryName',
        width: 100,
    },
    {
        title: t('product.info.header.warehouse'),
        dataIndex: 'warehouseName',
        width: 100,
    },
    {
        title: t('product.info.table.productStandard'),
        dataIndex: 'productStandard',
        width: 80,
    },
    {
        title:  t('product.info.table.productModel'),
        dataIndex: 'productModel',
        width: 100,
    },
    {
        title:  t('product.info.table.productColor'),
        dataIndex: 'productColor',
        width: 60,
    },
    {
        title: t('product.info.table.productCategory'),
        dataIndex: 'productCategoryName',
        width: 80,
    },
    {
        title: t('product.info.table.productUnit'),
        dataIndex: 'productUnit',
        width: 80,
    },
    {
        title: t('product.info.table.productStock'),
        dataIndex: 'stock',
        width: 60,
    },
    {
        title: t('product.info.table.retailPrice'),
        dataIndex: 'retailPrice',
        width: 60,
    },
    {
        title: t('product.info.table.salesPrice'),
        dataIndex: 'salePrice',
        width: 60,
    },
    {
        title: t('product.info.table.purchasePrice'),
        dataIndex: 'purchasePrice',
        width: 60,
    },
    {
        title: t('product.info.form.extendInfo.title'),
        dataIndex: 'extendInfo',
        width: 60,
    },
]

const searchFormSchema: FormSchema[] = [
    {
        label: t('product.info.header.categoryName'),
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
        label: t('product.info.header.keyWord'),
        field: 'keywords',
        component: 'Input',
        colProps: {
            xl: 8,
            xxl: 8,
        },
    },
    {
        label: t('product.info.header.serialNumber'),
        field: 'enableSerialNumber',
        component: 'Select',
        colProps: {
            xl: 12,
            xxl: 8,
        },
        componentProps: {
            options: [
                { label: t('product.info.header.none'), value: 0, key: 0 },
                { label: t('product.info.header.have'), value: 1, key: 1 },
            ],
        },
    },
    {
        label: t('product.info.header.batchNumber'),
        field: 'enableBatchNumber',
        component: 'Select',
        colProps: {
            xl: 12,
            xxl: 8,
        },
        componentProps: {
            options: [
                { label: t('product.info.header.none'), value: 0, key: 0 },
                { label: t('product.info.header.have'), value: 1, key: 1 },
            ],
        },
    },
    {
        label: t('product.info.header.warehouse'),
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
            title: t('product.info.form.basic.table.barCode'),
            key: 'barCode',
            type: 'inputNumber',
            placeholder: t('product.info.form.basic.table.pleaseEnter')+'${title}',
        },
        {
            title: t('product.info.form.basic.table.unit'),
            key: 'productUnit',
            type: 'input',
            placeholder: t('product.info.form.basic.table.pleaseEnter')+'${title}',
            validateRules: [{ required: true, message: '单位不能为空' }],
        },
        {
            title: t('product.info.form.basic.table.multipleAttributes'),
            key: 'multiAttribute',
            type: 'input',
            readonly: true,
            placeholder: t('product.info.form.basic.table.pleaseEnter')+'${title}',
        },
        {
            title: t('product.info.form.basic.table.purchasePrice'),
            key: 'purchasePrice',
            type: 'inputNumber',
            defaultValue: '',
            placeholder: t('product.info.form.basic.table.pleaseEnter')+'${title}',
        },
        {
            title: t('product.info.form.basic.table.retailPrice'),
            key: 'retailPrice',
            type: 'inputNumber',
            defaultValue: '',
            placeholder: t('product.info.form.basic.table.pleaseEnter')+'${title}',
        },
        {
            title: t('product.info.form.basic.table.salesPrice'),
            key: 'salesPrice',
            type: 'inputNumber',
            defaultValue: '',
            placeholder: t('product.info.form.basic.table.pleaseEnter')+'${title}',
        },
        {
            title: t('product.info.form.basic.table.lowestSellPrice'),
            key: 'lowSalesPrice',
            type: 'inputNumber',
            defaultValue: '',
            placeholder: t('product.info.form.basic.table.pleaseEnter')+'${title}',
        },
    ],
});

const stock: any = reactive({
    loading: false,
    dataSource: ref([]),
    columns: [
        {
            title: t('product.info.form.inventoryQuantity.warehouse'),
            key: 'warehouseName',
            type: 'input',
        },
        {
            title: t('product.info.form.inventoryQuantity.initialQuantity'),
            key: 'initStockQuantity',
            type: 'inputNumber',
        },
        {
            title: t('product.info.form.inventoryQuantity.minSafetyQuantity'),
            key: 'lowStockQuantity',
            type: 'inputNumber',
        },
        {
            title: t('product.info.form.inventoryQuantity.maxSafetyQuantity'),
            key: 'highStockQuantity',
            type: 'inputNumber',
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