export default {
    selectData: '请选择一条数据',
    modifyDataPrompt: '抱歉，只有未审核的单据才能编辑！',
    order:{
        title: '销售订单列表',
        table:{
            customer: '客户',
            receiptNumber: '单据编号',
            productInformation: '商品信息',
            productQuantity: '商品数量',
            totalAmount: '金额合计',
            totalIncludingTax: '含税合计',
            collectDeposit: '收取定金',
            receiptDate: '单据日期',
            operator: '操作员',
            status: '状态',
        },
        header:{
            startDate: '开始日期',
            endDate: '结束日期',
            receiptRemark: '单据备注',
        },
        export: {
            exportData: '销售订单数据 ',
            noData: '无可用数据导出',
        }
    },
    shipments:{
        title: '销售出库列表',
        table:{
            customer: '客户',
            receiptNumber: '单据编号',
            productInformation: '商品信息',
            productQuantity: '商品数量',
            totalAmount: '金额合计',
            totalIncludingTax: '含税合计',
            collectAmount: '待收金额',
            thisTimeCollectAmount: '本次收款',
            thisTimeArrearsAmount: '本次欠款',
            receiptDate: '单据日期',
            operator: '操作员',
            status: '状态',
        },
        header:{
            startDate: '开始日期',
            endDate: '结束日期',
            receiptRemark: '单据备注',
        },
        export: {
            exportData: '销售出库数据 ',
            noData: '无可用数据导出',
        }
    },
    refund:{
        title: '销售退货列表',
        table:{
            customer: '客户',
            receiptNumber: '单据编号',
            productInformation: '商品信息',
            productQuantity: '商品数量',
            totalAmount: '金额合计',
            totalIncludingTax: '含税合计',
            refundAmount: '待退金额',
            thisRefundAmount: '本次退款',
            thisArrearsAmount: '本次欠款',
            receiptDate: '单据日期',
            operator: '操作员',
            status: '状态',
        },
        header:{
            startDate: '开始日期',
            endDate: '结束日期',
            receiptRemark: '单据备注',
        },
        export: {
            exportData: '销售退货数据 ',
            noData: '无可用数据导出',
        }
    }
};