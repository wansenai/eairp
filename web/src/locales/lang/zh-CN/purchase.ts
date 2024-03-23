export default {
    selectData: '请选择一条数据',
    modifyDataPrompt: '抱歉，只有未审核的单据才能编辑！',
    partialPurchase: '部分采购',
    completePurchase: '完成采购',
    order:{
        title: '采购订单列表',
        table:{
            supplier: '供应商',
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
            exportData: '采购订单数据 ',
            noData: '无可用数据导出',
        }
    },
    storage:{
        title: '采购入库列表',
        table:{
            supplier: '供应商',
            receiptNumber: '单据编号',
            productInformation: '商品信息',
            productQuantity: '商品数量',
            totalAmount: '金额合计',
            totalIncludingTax: '含税合计',
            paymentAmount: '待付金额',
            thisTimePaymentAmount: '本次付款',
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
            exportData: '采购入库数据 ',
            noData: '无可用数据导出',
        }
    },
    refund:{
        title: '采购退货列表',
        table:{
            supplier: '供应商',
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
            exportData: '采购退货数据 ',
            noData: '无可用数据导出',
        }
    }
};