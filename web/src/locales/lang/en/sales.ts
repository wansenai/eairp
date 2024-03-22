export default {
    selectData: 'Please select a piece of data',
    modifyDataPrompt: 'Sorry, only unapproved documents can be edited!',
    partialSales: 'Partial Sales',
    completeSales: 'Complete Sales',
    order:{
        title: 'Sales Order List',
        table:{
            customer: 'Customer',
            receiptNumber: 'Receipt number',
            productInformation: 'Product information',
            productQuantity: 'Product quantity',
            totalAmount: 'Total amount',
            totalIncludingTax: 'Total including tax',
            collectDeposit: 'Collect Deposit',
            receiptDate: 'Receipt date',
            operator: 'operator',
            status: 'status',
        },
        header:{
            startDate: 'Start date',
            endDate: 'End date',
            receiptRemark: 'receipt remark',
        },
        export: {
            exportData: 'Sales Order Data ',
            noData: 'No data available',
        }
    },
    shipments:{
        title: 'Sales Shipments List',
        table:{
            customer: 'Customer',
            receiptNumber: 'Receipt number',
            productInformation: 'Product information',
            productQuantity: 'Product quantity',
            totalAmount: 'Total amount',
            totalIncludingTax: 'Total including tax',
            collectAmount: 'Collect amount',
            thisTimeCollectAmount: 'This time collect amount',
            thisTimeArrearsAmount: 'This time arrears amount',
            receiptDate: 'Receipt date',
            operator: 'operator',
            status: 'status',
        },
        header:{
            startDate: 'Start date',
            endDate: 'End date',
            receiptRemark: 'receipt remark',
        },
        export: {
            exportData: 'Sales Shipments Data ',
            noData: 'No data available',
        }
    },
    refund:{
        title: '销售退货列表',
        table:{
            customer: 'Customer',
            receiptNumber: 'Receipt number',
            productInformation: 'Product information',
            productQuantity: 'Product quantity',
            totalAmount: 'Total amount',
            totalIncludingTax: 'Total including tax',
            refundAmount: 'Refund Amount',
            thisTimeRefundAmount: 'This time refund amount',
            thisTimeArrearsAmount: 'This time arrears amount',
            receiptDate: 'Receipt date',
            operator: 'operator',
            status: 'status',
        },
        header:{
            startDate: 'Start date',
            endDate: 'End date',
            receiptRemark: 'receipt remark',
        },
        export: {
            exportData: 'Sales Return Data',
            noData: 'No data available',
        }
    }
};