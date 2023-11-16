<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click=""> 导出</a-button>
      </template>
    </BasicTable>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useModal} from "@/components/Modal";
import {searchStockFlowSchema, stockFlowColumns} from "@/views/report/report.data";
import {Tag} from "ant-design-vue";
import {getProductStock} from "@/api/report/report";

export default defineComponent({
  name: 'productStock',
  components: {Tag, TableAction, BasicTable },
  setup() {
    const [registerModal, {openModal}] = useModal();
    const [registerTable, { reload }] = useTable({
      title: '查看商品库存流水',
      api: getProductStock,
      rowKey: 'id',
      columns: stockFlowColumns,
      formConfig: {
        labelWidth: 110,
        schemas: searchStockFlowSchema,
        autoSubmitOnEnter: true,
      },
      useSearchForm: true,
      bordered: true,
      showTableSetting: true,
      striped: true,
      showIndexColumn: false,
    });


    async function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }

    async function handleSuccess() {
      reload();
    }

    async function handleCancel() {
      reload();
    }

    return {
      registerTable,
      registerModal,
      handleCreate,
      handleSuccess,
      handleCancel,
    }
  }
})
</script>