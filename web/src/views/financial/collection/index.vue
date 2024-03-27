<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate" v-text="t('financial.collection.add')" />
        <a-button type="primary" @click="handleBatchDelete" v-text="t('financial.collection.batchDelete')" />
        <a-button type="primary" @click="handleExport" v-text="t('financial.collection.export.name')" />
        <a-button type="primary" @click="handleOnStatus(1)" v-text="t('sys.table.approve')" />
        <a-button type="primary" @click="handleOnStatus(0)" v-text="t('sys.table.reject')" />
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
              {
                icon: 'clarity:info-standard-line',
                tooltip: t('financial.collection.viewCollectionReceiptDetail'),
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
    <AddEditCollectionModal ref="addEditModalRef" @cancel="handleCancel"/>
    <ViewCollectionModal @register="receiptViewModal" />
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {columns, searchFormSchema} from "@/views/financial/collection/collection.data";
import {useI18n} from "vue-i18n";
import {getCollectionPageList, deleteBatchCollection, updateCollectionStatus, exportCollection} from "@/api/financial/collection";
import {Checkbox, Modal, Tag} from "ant-design-vue";
import {useModal} from "@/components/Modal";
import ViewCollectionModal from "@/views/financial/collection/components/ViewCollectionModal.vue";
import AddEditCollectionModal from "@/views/financial/collection/components/AddEditCollectionModal.vue";
import {getTimestamp} from "@/utils/dateUtil";
export default defineComponent({
  name: 'CollectionReceipt',
  components: {'a-modal': Modal, 'a-checkbox': Checkbox, ViewCollectionModal, Tag, TableAction, BasicTable, AddEditCollectionModal},
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
      title: t('financial.collection.title'),
      rowKey: 'id',
      api: getCollectionPageList,
      columns: columns,
      rowSelection: {
        type: 'checkbox',
      },
      formConfig: {
        labelWidth: 110,
        schemas: searchFormSchema,
      },
      bordered: true,
      tableSetting: { fullScreen: true },
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
        createMessage.warn(t('financial.selectData'));
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await deleteBatchCollection(ids);
      if (result.code === 'C0004') {
        await reload();
      }
    }

    function handleEdit(record: Recordable) {
      if (record.status === 1) {
        createMessage.warn(t('financial.modifyDataPrompt'));
        return;
      }
      addEditModalRef.value.openAddEditModal(record.id);
    }

    async function handleDelete(record: Recordable) {
      const result = await deleteBatchCollection([record.id]);
      if (result.code === 'C0004') {
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
        createMessage.warn(t('financial.selectData'));
        return;
      }

      const ids = data.map((item) => item.id);
      const {code} = await updateCollectionStatus(ids, newStatus);
      if (code === "C0003") {
        await reload();
      }
    }

    async function handleExport() {
      dataSum.value = getDataSource().length;
      if (dataSum.value === 0) {
        createMessage.warn(t('financial.collection.export.noData'));
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
      const file: any = await exportCollection(data)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        const timestamp = getTimestamp(new Date());
        link.download = t('financial.collection.export.exportData') + timestamp + ".xlsx";
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