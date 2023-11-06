<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicTable @register="registerTable">
    </BasicTable>
  </BasicModal>
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {ReceiptDetailColumn} from "@/views/receipt/receipt.data";
import {BasicModal, useModalInner} from "@/components/Modal";
import {getReceiptDetail} from "@/api/receipt/receipt";
export default defineComponent({
  name: 'ReceiptDetailModal',
  components: {BasicModal, BasicTable, TableAction},
  emits: ['selectedRows'],
  setup(_, context) {
    const getTitle = ref('选择单据明细');
    const { createMessage } = useMessage();
    const id = ref('');
    const receiptNumber = ref('');
    const [registerTable, { getSelectRows }] = useTable({
      rowKey: 'id',
      columns: ReceiptDetailColumn,
      api: getReceiptDetail,
      beforeFetch: () => {
        return id.value;
      },
      rowSelection: {
        type: 'checkbox',
      },
      bordered: true,
      showTableSetting: false,
      canResize: true,
      tableSetting: { fullScreen: true },
      useSearchForm: false,
      clickToRowSelect: true,
      showIndexColumn: false,
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200});
      id.value = data.id
      receiptNumber.value = data.receiptNumber
    });

    function handleSubmit() {
      const rows = getSelectRows();
      if (rows.length === 0) {
        createMessage.error('请选择需要退货的出库单据');
        return;
      }
      const dataObject = {
        receiptNumber: receiptNumber.value,
        receiptDetailData: rows,
      }
      context.emit('handleReceiptSuccess', dataObject);
      closeModal();
    }

    return {
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
    }
  }
})
</script>