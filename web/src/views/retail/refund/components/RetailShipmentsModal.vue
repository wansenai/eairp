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
import {searchRetailShipmentsSchema, RetailShipmentsColumn} from "@/views/retail/refund/refund.data";
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import {Tag} from "ant-design-vue";
import {getShipmentsList} from "@/api/retail/shipments";
import ReceiptDetailModal from "@/views/receipt/ReceiptDetailModal.vue";
import {formState} from "@/views/retail/shipments/model/addEditModel";
export default defineComponent({
  name: 'RetailShipmentsModal',
  components: {Tag, BasicModal, BasicTable, TableAction, ReceiptDetailModal},
  emits: ['selectedRows'],
  setup(_, context) {
    const getTitle = ref('选择需要退货的出库单');
    const [receiptDetailModal, {openModal: openReceiptDetailModal}] = useModal();
    const { createMessage } = useMessage();
    const [registerTable, { getSelectRows }] = useTable({
      title: '选择需要退货的出库单',
      rowKey: 'id',
      columns: RetailShipmentsColumn,
      api: getShipmentsList,
      rowSelection: {
        type: 'radio',
      },
      formConfig: {
        labelWidth: 110,
        schemas: searchRetailShipmentsSchema,
      },
      bordered: true,
      showTableSetting: true,
      canResize: true,
      tableSetting: { fullScreen: true },
      useSearchForm: true,
      clickToRowSelect: true,
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200});
    });

    function handleReceiptSuccess(data) {
      if(data){
        context.emit('handleReceiptSuccess', data);
      }
    }

    function handleSubmit() {
      const rows = getSelectRows();
      if (rows.length === 0) {
        createMessage.error('请选择需要退货的出库单据');
        return;
      }
      openReceiptDetailModal(true, {
        isUpdate: false,
        id: rows[0].id,
        receiptNumber: rows[0].receiptNumber,
      });
      closeModal();
    }

    return {
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
      receiptDetailModal,
      handleReceiptSuccess
    }
  }
})
</script>