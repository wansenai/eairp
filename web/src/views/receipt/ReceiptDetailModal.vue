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
    const receiptNumber = ref('');
    const [registerTable, { getSelectRows, reload}] = useTable({
      rowKey: 'id',
      columns: ReceiptDetailColumn,
      api: getReceiptDetail,
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
      immediate: false,
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200});
      receiptNumber.value = data.receiptNumber
      reload({
        searchInfo: {
          "id": data.id,
          "type": data.type,
        }
      });
    });

    function handleSubmit() {
      const rows = getSelectRows();
      if (rows.length === 0) {
        createMessage.error('请选择单据');
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