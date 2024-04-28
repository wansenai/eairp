export default {
    selectData: '请选择一条数据',
    modifyDataPrompt: '抱歉，只有未审核的单据才能编辑！',
    regularPrint: '普通打印',
    shipments:{
        title: '零售出库列表',
        addShipments: '新增-零售出库',
        editShipments: '编辑-零售出库',
        detail: '零售出库-详情',
        receipt: '单据详情',
        table:{
            member: '会员',
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
            name: '导出',
            exportData: '零售出库数据 ',
            noData: '无可用数据导出',
        },
        form: {
            member: '会员卡号',
            inputMember: '请选择会员',
            addMember: '新增会员',
            receiptDate: '单据日期',
            inputReceiptDate: '请选择单据日期',
            receiptNumber: '单据编号',
            collectionType: '收款方式',
            inputCollectionType: '请选择收款方式',
            scanCodeData: '扫码录入数据',
            collapseScanCode: '收起扫码',
            scanCodeTip: '鼠标点击此次',
            addProduct: '选择添加入库商品',
            insertRow: '添加一行',
            deleteRow: '删除选中行',
            addRowData: '请添加一行数据',
            inputBarCode: '请输入商品条码',
            noticeOne: '请选择仓库',
            noticeTwo: '请录入条码或者选择产品',
            noticeThree: '需要在商品管理添加商品',
            noticeFour: '确定要删除选中的数据?',
            noticeFive: '该文件超过2MB大小限制',
            noticeSix: '该条码查询不到商品信息',
            noticeSeven: '商品库存不足，请检查库存数量',
            advancePayment: '预付款',
            cashPayment: '现付',
            cancel: '取消',
            save: '保存',
            saveApprove: '保存并审核',
            table: {
                warehouse: '仓库',
                inputWarehouse: '请选择仓库',
                barCode: '条码',
                inputBarCode: '输入商品条码',
                name: '名称',
                standard: '规格',
                model: '型号',
                color: '颜色',
                stock: '库存',
                unit: '单位',
                quantity: '数量',
                unitPrice: '单价',
                amount: '金额',
                total: '合计',
                inputRemark: '请输入备注',
                annex: '附件',
                uploadAnnex: '上传附件',
            }
        },
        view: {
            member: '会员',
            receiptDate: '单据日期',
            collectionType: '收款方式',
            receiptAmount: '单据金额',
            collectionAmount: '收款金额',
            changeAmount: '找零金额',
            collectionAccount: '收款账户',
            inputCollectionAccount: '请选择收款账户',
            addAccount: '新增账户',
            refundReceipt: '退款单号',
            remark: '备注',
            status: '状态',
        }
    },
    refund:{
        title: '零售退货列表',
        addRefund: '新增-零售退货',
        editRefund: '编辑-零售退货',
        detail: '零售退货-详情',
        receipt: '单据详情',
        table:{
            member: '会员',
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
            name: '导出',
            exportData: '零售退货数据 ',
            noData: '无可用数据导出',
        },
        form: {
            member: '会员卡号',
            inputMember: '请选择会员',
            addMember: '新增会员',
            receiptDate: '单据日期',
            inputReceiptDate: '请选择单据日期',
            receiptNumber: '单据编号',
            relatedReceipt: '关联单据',
            scanCodeData: '扫码录入数据',
            collapseScanCode: '收起扫码',
            scanCodeTip: '鼠标点击此次',
            addProduct: '选择添加退货商品',
            insertRow: '添加一行',
            deleteRow: '删除选中行',
            addRowData: '请添加一行数据',
            noticeOne: '请选择仓库',
            noticeTwo: '请录入条码或者选择产品',
            noticeThree: '需要在商品管理添加商品',
            noticeFour: '确定要删除选中的数据?',
            noticeFive: '该文件超过2MB大小限制',
            noticeSix: '该条码查询不到商品信息',
            cancel: '取消',
            save: '保存',
            saveApprove: '保存并审核',
            table: {
                warehouse: '仓库',
                inputWarehouse: '请选择仓库',
                barCode: '条码',
                inputBarCode: '输入商品条码',
                name: '名称',
                standard: '规格',
                model: '型号',
                color: '颜色',
                stock: '库存',
                unit: '单位',
                quantity: '数量',
                unitPrice: '单价',
                amount: '金额',
                total: '合计',
                inputRemark: '请输入备注',
                annex: '附件',
                uploadAnnex: '上传附件',
            }
        },
        view: {
            member: '会员',
            receiptDate: '单据日期',
            receiptAmount: '单据金额',
            paymentAmount: '付款金额',
            changeAmount: '找零金额',
            paymentAccount: '付款账户',
            inputPaymentAccount: '请选择付款账户',
            relatedReceipt: '关联单据',
            addAccount: '新增账户',
            remark: '备注',
            status: '状态',
        }
    }
};