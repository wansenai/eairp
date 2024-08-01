<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate" v-text="t('basic.customer.add')"/>
        <a-button type="primary" @click="handleBatchDelete" v-text="t('basic.customer.batchDelete')"/>
        <a-button type="primary" @click="handleOnStatus(0)" v-text="t('basic.customer.batchEnable')"/>
        <a-button type="primary" @click="handleOnStatus(1)" v-text="t('basic.customer.batchDisable')"/>
        <a-button type="primary" @click="handleImport" v-text="t('basic.customer.Import')"/>
        <a-button type="primary" @click="handleExport" v-text="t('basic.customer.Export')"/>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
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
      </template>
    </BasicTable>
    <CustomerModal @register="registerModal" @success="handleSuccess" />
    <ImportFileModal ref="importModalRef" @cancel="handleCancel"/>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useModal} from "@/components/Modal";
import {useMessage} from "@/hooks/web/useMessage";
import {columns, searchFormSchema} from "@/views/basic/customer/customer.data";
import {getCustomerPageList, deleteBatchCustomer, updateCustomerStatus, exportCustomer} from "@/api/basic/customer";
import CustomerModal from "@/views/basic/customer/components/CustomerModal.vue";
import ImportFileModal from '@/components/Tools/ImportFileModal.vue';
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'Customer',
  components: {TableAction, BasicTable, CustomerModal, ImportFileModal },
  setup() {
    const { t } = useI18n();
    const currentTime = ref(null);
    const timestamp = ref(null);
    const [registerModal, {openModal}] = useModal();
    const { createMessage } = useMessage();
    const importModalRef = ref(null);
    const [registerTable, { reload, getSelectRows, getForm, getDataSource }] = useTable({
      title: t('basic.customer.title'),
      api: getCustomerPageList,
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
      showTableSetting: true,
      bordered: true,
      showIndexColumn: true,
      actionColumn: {
        width: 80,
        title: t('common.operating'),
        dataIndex: 'action',
        fixed: undefined,
      },
    });

    async function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }

    async function handleBatchDelete(record: Recordable) {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn(t('basic.selectData'));
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await deleteBatchCustomer(ids)
      if (result.code === 'U0003') {
        await reload();
      }
    }

    async function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      const result = await deleteBatchCustomer([record.id])
      if (result.code === 'U0003') {
        await reload();
      }
    }

    async function handleSuccess() {
      reload();
    }

    async function handleOnStatus(newStatus: number) {
      // 获取选中行的id组成一个数组
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn(t('basic.selectData'));
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await updateCustomerStatus(ids,newStatus )
      if (result.code === 'U0004') {
        await reload();
      }
    }

    async function handleCancel() {
      reload();
    }

    function handleImport() {
      const templateUrl  = 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/%E5%AE%A2%E6%88%B7%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF.xlsx'
      const templateName  = t('basic.customer.export.templateDownload')
      importModalRef.value.initModal(templateUrl, templateName);
      importModalRef.value.title = t('basic.customer.export.import');
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

  async function handleExport() {
    if (getDataSource().length === 0) {
      createMessage.warn(t('basic.customer.export.noData'));
      return;
    }
    const data: any = getForm().getFieldsValue();
    const file: any = await exportCustomer(data)
    if (file.size > 0) {
      const blob = new Blob([file]);
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      const timestamp = getTimestamp(new Date());
      link.download = t('basic.customer.export.exportData') + timestamp + ".xlsx";
      link.target = "_blank";
      link.click();
    }
  }

    return {
      t,
      registerTable,
      registerModal,
      handleCreate,
      handleDelete,
      handleBatchDelete,
      handleEdit,
      handleSuccess,
      handleOnStatus,
      handleImport,
      handleExport,
      importModalRef,
      handleCancel,
      currentTime,
      timestamp,
    }
  }
})
</script>