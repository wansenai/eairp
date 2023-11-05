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
</template>

<script lang="ts">
import {getShipmentsList} from "@/api/retail/shipments";

const store = defineStore('modalStore', {
  state: () => ({
    data: null,
  }),
});

import {defineComponent, ref} from "vue";
import { defineStore } from 'pinia';
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {searchRetailShipmentsSchema, RetailShipmentsColumn} from "@/views/retail/refund/refund.data";
import {BasicModal, useModalInner} from "@/components/Modal";
import {Tag} from "ant-design-vue";
export default defineComponent({
  name: 'productModal',
  components: {Tag, BasicModal, BasicTable, TableAction},
  emits: ['selectedRows'],
  setup(_, context) {
    const getTitle = ref('选择需要退货的出库单');
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

    function handleSubmit() {
      const rows = getSelectRows();
      if (rows.length === 0) {
        createMessage.error('请选择需要退货的出库单据');
        return;
      }
      const receiptNumber = rows[0].receiptNumber;
      context.emit('handleRadioSuccess', receiptNumber);
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