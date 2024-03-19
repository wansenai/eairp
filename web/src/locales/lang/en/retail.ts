export default {
    selectData: 'Please select a piece of data',
    modifyDataPrompt: 'Sorry, only unapproved documents can be edited!',
    shipments:{
        title: 'Retail Shipments List',
        table:{
            customer: 'Customer',
            receiptNumber: 'Receipt number',
            productInformation: 'Product information',
            productQuantity: 'Product quantity',
            totalAmount: 'Total amount',
            amountCollection: 'Amount collection',
            changeAmount: 'Change amount',
            receiptDate: 'Receipt date',
            operator: 'operator',
            status: 'status',
        },
        header:{
            settlementAccount: 'Settlement account',
            startDate: 'Start date',
            endDate: 'End date',
            receiptRemark: 'receipt remark',
        },
        export: {
            exportData: 'Retail Shipments Data ',
            noData: 'No data available',
        }
    },
    refund:{
        title: 'Retail Return List',
        table:{
            customer: 'Customer',
            receiptNumber: 'Receipt number',
            productInformation: 'Product information',
            totalAmount: 'Total amount',
            paymentAmount: 'Payment amount',
            changeAmount: 'Change amount',
            receiptDate: 'Receipt date',
            operator: 'operator',
            status: 'status',
        },
        header:{
            settlementAccount: 'Settlement account',
            startDate: 'Start date',
            endDate: 'End date',
            receiptRemark: 'receipt remark',
        },
        export: {
            exportData: 'Retail Return Data ',
            noData: 'No data available',
        }
    }
};