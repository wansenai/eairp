export default {
    selectData: '请选择一条数据',
    modifyDataPrompt: '抱歉，只有未审核的单据才能编辑！',
    shipments:{
        title: '零售出库列表',
        table:{
            customer: '会员',
            receiptNumber: '单据编号',
            productInformation: '商品信息',
            productQuantity: '商品数量',
            totalAmount: '金额合计',
            amountCollection: '收款金额',
            changeAmount: '找零金额',
            receiptDate: '单据日期',
            operator: '操作员',
            status: '状态',
        },
        header:{
            settlementAccount: '结算账户',
            startDate: '开始日期',
            endDate: '结束日期',
            receiptRemark: '单据备注',
        },
        export: {
            exportData: '零售出库数据 ',
            noData: '无可用数据导出',
        }
    },
    refund:{
        title: '零售退货列表',
        table:{
            customer: '会员',
            receiptNumber: '单据编号',
            productInformation: '商品信息',
            totalAmount: '金额合计',
            paymentAmount: '付款金额',
            changeAmount: '找零金额',
            receiptDate: '单据日期',
            operator: '操作员',
            status: '状态',
        },
        header:{
            settlementAccount: '结算账户',
            startDate: '开始日期',
            endDate: '结束日期',
            receiptRemark: '单据备注',
        },
        export: {
            exportData: '零售退货数据 ',
            noData: '无可用数据导出',
        }
    }
};