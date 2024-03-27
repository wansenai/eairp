<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate" v-text="t('financial.advance.add')" />
        <a-button type="primary" @click="handleBatchDelete" v-text="t('financial.advance.batchDelete')" />
        <a-button type="primary" @click="handleExport" v-text="t('financial.advance.export.name')" />
        <a-button type="primary" @click="handleOnStatus(1)" v-text="t('sys.table.approve')" />
        <a-button type="primary" @click="handleOnStatus(0)" v-text="t('sys.table.reject')" />
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
              {
                icon: 'clarity:info-standard-line',
                tooltip: t('financial.advance.viewAdvanceReceiptDetail'),
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
    <AdvanceChargeModal ref="advanceChargeModalRef" @cancel="handleCancel"></AdvanceChargeModal>
    <ViewAdvanceChargeModal @register="viewAdvanceChargeModalRef"/>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useModal} from "@/components/Modal";
import {useMessage} from "@/hooks/web/useMessage";
import {columns, searchFormSchema} from "@/views/financial/advance-charge/advance.data";
import {getAdvancePageList, deleteBatchAdvance, updateAdvanceStatus, exportAdvance} from "@/api/financial/advance";
import AdvanceChargeModal from "@/views/financial/advance-charge/components/AdvanceChargeModal.vue";
import ViewAdvanceChargeModal from "@/views/financial/advance-charge/components/ViewAdvanceChargeModal.vue";
import {Checkbox, Modal, Tag} from "ant-design-vue";
import {getTimestamp} from "@/utils/dateUtil";
import {useI18n} from "vue-i18n";
export default defineComponent({
  name: 'advanceCharge',
  components: {'a-modal': Modal, 'a-checkbox': Checkbox, Tag, TableAction, BasicTable, AdvanceChargeModal, ViewAdvanceChargeModal},
  setup() {
    const { t } = useI18n();
    const [viewAdvanceChargeModalRef, {openModal: openAdvanceChargeModal}] = useModal();
    const { createMessage } = useMessage();
    const advanceChargeModalRef = ref(null);
    const exportDetailData = ref<boolean>(false);
    const openExportData = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);
    const dataSum = ref<number>(0);
    const [registerTable, { reload, getSelectRows, getForm, getDataSource }] = useTable({
      title: t('financial.advance.title'),
      api: getAdvancePageList,
      rowKey: 'id',
      columns: columns,
      rowSelection: {
        type: 'checkbox',
      },
      formConfig: {
        labelWidth: 110,
        schemas: searchFormSchema,
        autoSubmitOnEnter: true,
      },
      useSearchForm: true,
      clickToRowSelect: false,
      bordered: true,
      showTableSetting: true,
      striped: true,
      showIndexColumn: true,
      actionColumn: {
        width: 80,
        title: t('common.operating'),
        dataIndex: 'action',
        fixed: undefined,
      },
    });

    async function handleCreate() {
      advanceChargeModalRef.value.openAdvanceChargeModal();
    }

    async function handleBatchDelete(record: Recordable) {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn(t('financial.selectData'));
        return;
      }
      const ids = data.map((item) => item.id);
      const {code} = await deleteBatchAdvance(ids);
      if (code === "F0007") {
        await reload();
      }
    }

    async function handleEdit(record: Recordable) {
      if (record.status === 1) {
        createMessage.warn(t('financial.modifyDataPrompt'));
        return;
      }
      advanceChargeModalRef.value.openAdvanceChargeModal(record.id);
    }

    async function handleDelete(record: Recordable) {
      const {code} = await deleteBatchAdvance([record.id]);
      if (code === "F0007") {
        await reload();
      }
    }

    async function handleSuccess() {
      reload();
    }

    async function handleOnStatus(newStatus: number) {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn(t('financial.selectData'));
        return;
      }

      const ids = data.map((item) => item.id);
      const {code} = await updateAdvanceStatus(ids, newStatus);
      if (code === "F0006") {
        await reload();
      }
    }

    async function handleCancel() {
      reload();
    }

    function handleView(record: Recordable){
      openAdvanceChargeModal(true, {
        id: record.id,
      });
    }

    async function handleExport() {
      dataSum.value = getDataSource().length;
      if (dataSum.value === 0) {
        createMessage.warn(t('financial.advance.export.noData'));
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
      const file: any = await exportAdvance(data)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        const timestamp = getTimestamp(new Date());
        link.download = t('financial.advance.export.exportData') + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
      confirmLoading.value = false;
      openExportData.value = false;
      exportDetailData.value = false;
    }

    return {
      t,
      registerTable,
      handleCreate,
      handleDelete,
      handleBatchDelete,
      handleEdit,
      handleView,
      handleSuccess,
      handleOnStatus,
      handleCancel,
      advanceChargeModalRef,
      viewAdvanceChargeModalRef,
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