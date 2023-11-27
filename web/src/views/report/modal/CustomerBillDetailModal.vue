<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle">
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click=""> 导出</a-button>
        <a-button @click=""> 打印</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'receiptNumber'">
          <a @click="openReceipt(record.receiptNumber)"> {{ record.receiptNumber }} </a>
        </template>
      </template>
    </BasicTable>
    <ViewSaleShipmentsModal @register="handleSaleShipmentsModal" />
    <ViewSaleRefundModal @register="handleSaleRefundModal" />
  </BasicModal>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {customerBillDetailColumns, searchCustomerBillDetailSchema} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getCustomerBillDetail} from "@/api/report/report";
import XEUtils from "xe-utils";
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import ViewSaleShipmentsModal from "@/views/sales/shipments/components/ViewSaleShipmentsModal.vue";
import ViewSaleRefundModal from "@/views/sales/refund/components/ViewSaleRefundModal.vue";

export default defineComponent({
  name: 'CustomerBillDetailModal',
  components: {
    BasicModal,
    ViewSaleShipmentsModal,
    ViewSaleRefundModal, Tag, TableAction, BasicTable},
  setup() {
    const getTitle = ref('客户欠款详情');
    const customerId = ref('');
    const [handleSaleShipmentsModal, {openModal: openSaleShipmentsModal}] = useModal();
    const [handleSaleRefundModal, {openModal: openSaleRefundModal}] = useModal();
    const [registerTable, { reload }] = useTable({
      api: getCustomerBillDetail,
      beforeFetch: (data) => {
        data.customerId = customerId.value;
      },
      columns: customerBillDetailColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchCustomerBillDetailSchema,
        autoSubmitOnEnter: true,
      },
      bordered: true,
      useSearchForm: true,
      showTableSetting: true,
      striped: true,
      canResize: false,
      showIndexColumn: true,
      showSummary: true,
      summaryFunc: handleSummary,
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1100});
      customerId.value = data.customerId
    });

    function handleSummary(tableData: Recordable[]) {
      const thisReceiptArrears = tableData.reduce((prev, next) => prev + next.thisReceiptArrears, 0);
      const receivableArrears = tableData.reduce((prev, next) => prev + next.receivableArrears, 0);
      const receivedArrears = tableData.reduce((prev, next) => prev + next.receivedArrears, 0);
      return [
        {
          _index: '合计',
          thisReceiptArrears:`￥${XEUtils.commafy(XEUtils.toNumber(thisReceiptArrears), { digits: 2 })}`,
          receivableArrears:`￥${XEUtils.commafy(XEUtils.toNumber(receivableArrears), { digits: 2 })}`,
          receivedArrears:`￥${XEUtils.commafy(XEUtils.toNumber(receivedArrears), { digits: 2 })}`,
        },
      ];
    }
    async function handleSuccess() {
      reload();
    }

    async function handleCancel() {
      reload();
    }

    function openReceipt(openReceiptNumber: string) {
      if (openReceiptNumber.startsWith('XSCK')) {
        openSaleShipmentsModal(true, {
          receiptNumber: openReceiptNumber,
        });
      } else if (openReceiptNumber.startsWith('XSTH')) {
        openSaleRefundModal(true, {
          receiptNumber: openReceiptNumber,
        });
      }
    }

    return {
      getTitle,
      registerModal,
      openReceipt,
      registerTable,
      handleSuccess,
      handleCancel,
      handleSaleRefundModal,
      handleSaleShipmentsModal,
    }
  }
})
</script>