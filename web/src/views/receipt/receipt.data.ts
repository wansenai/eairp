import {BasicColumn} from "@/components/Table";

export const ReceiptDetailColumn: BasicColumn[] = [
    {
        title: 'id',
        dataIndex: 'id',
        width: 60,
        ifShow: false,
    },
    {
        title: '条码',
        dataIndex: 'productBarcode',
        width: 130,
    },
    {
        title: '商品名称',
        dataIndex: 'productName',
        width: 130,
    },
    {
        title: '商品规格',
        dataIndex: 'productStandard',
        width: 100,
    },
    {
        title: '商品型号',
        dataIndex: 'productModel',
        width: 100,
    },
    {
        title: '单位',
        dataIndex: 'unit',
        width: 80,
    },
    {
        title: '数量',
        dataIndex: 'productNumber',
        width: 60,
    },
    {
        title: '单价',
        dataIndex: 'productPrice',
        width: 60,
    },
    {
        title: '金额',
        dataIndex: 'productTotalPrice',
        width: 60,
    },
    {
        title: '税率(%)',
        dataIndex: 'discountRate',
        width: 80,
    },
    {
        title: '税额',
        dataIndex: 'discountAmount',
        width: 80,
    },
    {
        title: '优惠合计',
        dataIndex: 'discountLastAmount',
        width: 80,
    },
    {
        title: '备注',
        dataIndex: 'remark',
        width: 100,
    },
]