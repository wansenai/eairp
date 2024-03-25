<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate" v-text="t('warehouse.otherShipments.add')" />
        <a-button type="primary" @click="handleBatchDelete" v-text="t('warehouse.otherShipments.batchDelete')" />
        <a-button type="primary" @click="handleExport" v-text="t('warehouse.otherShipments.export.name')" />
        <a-button type="primary" @click="handleOnStatus(1)" v-text="t('sys.table.approve')" />
        <a-button type="primary" @click="handleOnStatus(0)" v-text="t('sys.table.reject')" />
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
              {
                icon: 'clarity:info-standard-line',
                tooltip: t('sys.table.viewReceiptDetails'),
                onClick: handleView.bind(null, record),
              },
              {
                icon: 'clarity:note-edit-line',
                tooltip: t('sys.table.edit'),
                onClick: handleEdit.bind(null, record),
              },
              {
                icon: 'ant-design:delete-outlined',
                tooltip: t('sys.table.delete'),
                color: 'error',
                popConfirm: {
                  title: t('sys.table.confirmDelete'),
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
        <template v-else-if="column.key === 'status'">
          <Tag :color="record.status === 1 ? 'green' : 'red'">
            {{ record.status === 1 ? t('sys.table.audited') : t('sys.table.unaudited') }}
          </Tag>
        </template>
      </template>
    </BasicTable>
    <a-modal v-model:open="openExportData" :title="t('sys.table.confirmExport')" :confirm-loading="confirmLoading"
             @ok="handleExportOk" @cancel="handleExportCancel" :ok-text="t('sys.table.confirmExportTextOne')">
      <div style="text-align: center">
        <p>{{ t('sys.table.confirmExportTextOne') }} {{ dataSum }} {{ t('sys.table.confirmExportTextTwo') }}</p>
        <p>{{ t('sys.table.confirmExportTextThree') }}</p>
        <a-checkbox v-model:checked="exportDetailData">{{ t('sys.table.confirmExportTextFour') }}</a-checkbox>
      </div>
    </a-modal>
    <AddEditOtherShipmentsModal ref="addEditModalRef" @cancel="handleCancel" />
    <ViewOtherShipmentsModal @register="receiptViewModal" @ok="handleOk"/>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {columns, searchFormSchema} from "@/views//warehouse/shipments/otherShipments.data";
import {useI18n} from "vue-i18n";
import {getOtherShipmentsPageList, deleteBatchOtherShipments, updateOtherShipmentsStatus, exportOtherShipments} from "@/api/warehouse/shipments";
import AddEditOtherShipmentsModal from "@/views/warehouse/shipments/components/AddEditOtherShipmentsModal.vue"
import ViewOtherShipmentsModal from "@/views/warehouse/shipments/components/ViewOtherShipmentsModal.vue";
import {Checkbox, Modal, Tag} from "ant-design-vue";
import {useModal} from "@/components/Modal";
export default defineComponent({
  name: 'OtherShipments',
  components: {'a-modal': Modal, 'a-checkbox': Checkbox, Tag, TableAction, BasicTable, AddEditOtherShipmentsModal, ViewOtherShipmentsModal},
  setup() {
    const { t } = useI18n();
    const { createMessage } = useMessage();
    const addEditModalRef = ref(null);
    const exportDetailData = ref<boolean>(false);
    const openExportData = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);
    const dataSum = ref<number>(0);
    const [receiptViewModal, {openModal: openReceiptViewModal}] = useModal();
    const [registerTable, { reload, getSelectRows, getForm, getDataSource }] = useTable({
      title: t('warehouse.otherShipments.title'),
      rowKey: 'id',
      api: getOtherShipmentsPageList,
      columns: columns,
      rowSelection: {
        type: 'checkbox',
      },
      formConfig: {
        labelWidth: 110,
        schemas: searchFormSchema,
        autoSubmitOnEnter: true
      },
      bordered: true,
      useSearchForm: true,
      clickToRowSelect: false,
      showTableSetting: true,
      actionColumn: {
        width: 80,
        title: t('common.operating'),
        dataIndex: 'action',
        fixed: undefined,
      },
    });

    function handleCreate() {
      addEditModalRef.value.openAddEditModal();
    }

    async function handleBatchDelete() {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn(t('warehouse.selectData'));
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await deleteBatchOtherShipments(ids);
      if (result.code === 'S0015') {
        await reload();
      }
    }

    function handleEdit(record: Recordable) {
      if (record.status === 1) {
        createMessage.warn(t('warehouse.modifyDataPrompt'));
        return;
      }
      addEditModalRef.value.openAddEditModal(record.id);
    }

    async function handleDelete(record: Recordable) {
      const result = await deleteBatchOtherShipments([record.id]);
      if (result.code === 'S0015') {
        await reload();
      }
    }

    async function handleSuccess() {
      await reload();
    }

    async function handleCancel() {
      await reload();
    }

    async function handleOk() {
      await reload();
    }

    function handleView(record: Recordable){
      openReceiptViewModal(true, {
        isUpdate: false,
        id: record.id,
      });
    }

    const getTimestamp = (date) => {
      return (
          date.getFullYear() * 10000000000 +
          (date.getMonth() + 1) * 100000000 +
          date.getDate() * 1000000 +
          date.getHours() * 10000 +
          date.getMinutes() * 100 +
          date.getSeconds()
      ).toString();
    };

    async function handleOnStatus(newStatus: number) {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn(t('warehouse.selectData'));
        return;
      }

      const ids = data.map((item) => item.id);
      const {code} = await updateOtherShipmentsStatus(ids, newStatus);
      if (code === "S0014") {
        await reload();
      }
    }

    async function handleExport() {
      dataSum.value = getDataSource().length;
      if (dataSum.value === 0) {
        createMessage.warn(t('warehouse.otherShipments.export.noData'));
        return;
      }
      openExportData.value = true;
    }

    const handleExportCancel = () => {
      confirmLoading.value = false;
      openExportData.value = false;
      exportDetailData.value = false;
    };

    const handleExportOk = async () => {
      confirmLoading.value = true;
      const data: any = getForm().getFieldsValue();
      data.isExportDetail = exportDetailData.value;
      const file: any = await exportOtherShipments(data)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        const timestamp = getTimestamp(new Date());
        link.download = t('warehouse.otherShipments.export.exportData') + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
      confirmLoading.value = false;
      openExportData.value = false;
      exportDetailData.value = false;
    }

    return {
      t,
      receiptViewModal,
      addEditModalRef,
      registerTable,
      handleCreate,
      handleDelete,
      handleBatchDelete,
      handleEdit,
      handleSuccess,
      handleOnStatus,
      handleCancel,
      handleView,
      handleOk,
      handleExport,
      openExportData,
      confirmLoading,
      exportDetailData,
      dataSum,
      handleExportOk,
      handleExportCancel
    }
  }
})
</script>