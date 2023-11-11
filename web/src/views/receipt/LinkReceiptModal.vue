<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicTable @register="registerTable">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <Tag :color="record.status === 1 ? 'green' : 'red'">
            {{ record.status === 1 ? '已审核' : '未审核' }}
          </Tag>
        </template>
        <template v-else-if="column.key === 'receiptNumber'">
          <a @click="">{{record.receiptNumber}}</a>
        </template>
      </template>
    </BasicTable>
  </BasicModal>
  <ReceiptDetailModal @register="receiptDetailModal" @handleReceiptSuccess="handleReceiptSuccess"/>
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import {Tag} from "ant-design-vue";
import {getReceipt} from "@/api/receipt/receipt";
import ReceiptDetailModal from "@/views/receipt/ReceiptDetailModal.vue";
import {ReceiptColumn, searchSchema} from "@/views/receipt/receipt.data"
export default defineComponent({
  name: 'LinkReceiptModal',
  components: {Tag, BasicModal, BasicTable, TableAction, ReceiptDetailModal},
  emits: ['selectedRows'],
  setup(_, context) {
    const getTitle = ref('操作');
    const [receiptDetailModal, {openModal: openReceiptDetailModal}] = useModal();
    const { createMessage } = useMessage();
    const type = ref('');
    const subType = ref('');
    const [registerTable, { getSelectRows, reload}] = useTable({
      title: '操作',
      rowKey: 'id',
      columns: ReceiptColumn,
      api: getReceipt,
      rowSelection: {
        type: 'radio',
      },
      formConfig: {
        labelWidth: 110,
        schemas: searchSchema,
      },
      showIndexColumn: false,
      bordered: true,
      showTableSetting: true,
      canResize: true,
      tableSetting: { fullScreen: true },
      useSearchForm: true,
      clickToRowSelect: true,
      immediate: false,
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200});
      type.value = data.type
      subType.value = data.subType
      getTitle.value = data.title
      searchSchema[0].defaultValue = data.type
      searchSchema[1].defaultValue = data.subType
      reload({
        searchInfo: {
          "type": type.value,
          "subType": subType.value,
        }
      });
    });

    function handleSubmit() {
      const rows = getSelectRows();
      if (rows.length === 0) {
        createMessage.error('请选择单据');
        return;
      }
      openReceiptDetailModal(true, {
        isUpdate: false,
        id: rows[0].id,
        receiptNumber: rows[0].receiptNumber,
        type: type.value,
      });
      closeModal();
    }

    function handleReceiptSuccess(data) {
      context.emit('handleReceiptSuccess', data);
    }

    return {
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
      receiptDetailModal,
      handleReceiptSuccess,
    }
  }
})
</script>