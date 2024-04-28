<template>
    <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate" v-text="t('basic.supplier.add')"/>
        <a-button type="primary" @click="handleBatchDelete" v-text="t('basic.supplier.batchDelete')"/>
        <a-button type="primary" @click="handleOnStatus(0)" v-text="t('basic.supplier.batchEnable')"/>
        <a-button type="primary" @click="handleOnStatus(1)" v-text="t('basic.supplier.batchDisable')"/>
        <a-button type="primary" @click="handleImport" v-text="t('basic.supplier.Import')"/>
        <a-button type="primary" @click="handleExport" v-text="t('basic.supplier.Export')"/>
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
    <SupplierModal @register="registerModal" @success="handleSuccess" />
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
import {getSupplierPageList} from "@/api/basic/supplier";
import {columns, searchFormSchema} from "@/views/basic/supplier/supplier.data";
import {deleteBatchSuppliers, updateSupplierStatus, exportSupplier} from "@/api/basic/supplier";
import SupplierModal from "@/views/basic/supplier/components/SupplierModal.vue";
import ImportFileModal from '@/components/Tools/ImportFileModal.vue';
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'Supplier',
  components: {TableAction, BasicTable, SupplierModal, ImportFileModal },
  setup() {
    const { t } = useI18n();
    const currentTime = ref(null);
    const timestamp = ref(null);
    const [registerModal, {openModal}] = useModal();
    const { createMessage } = useMessage();
    const importModalRef = ref(null);
    const [registerTable, { reload, getSelectRows, getDataSource, getForm }] = useTable({
      title: t('basic.supplier.title'),
      api: getSupplierPageList,
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
      const result = await deleteBatchSuppliers(ids)
      if (result.code === 'S0003') {
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
      const result = await deleteBatchSuppliers([record.id])
      if (result.code === 'S0003') {
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
      const result = await updateSupplierStatus({ids: ids, status: newStatus} )
      if (result.code === 'S0004') {
        await reload();
      }
    }

    async function handleCancel() {
      reload();
    }

    function handleImport() {
      const templateUrl  = 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/%E4%BE%9B%E5%BA%94%E5%95%86%E6%A8%A1%E6%9D%BF.xlsx'
      const templateName  = t('basic.supplier.export.templateDownload')
      importModalRef.value.initModal(templateUrl, templateName);
      importModalRef.value.title = t('basic.supplier.export.import');
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
        createMessage.warn(t('basic.supplier.export.noData'));
        return;
      }
      const data: any = getForm().getFieldsValue();
      const file: any = await exportSupplier(data)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        const timestamp = getTimestamp(new Date());
        link.download = t('basic.supplier.export.data') + timestamp + ".xlsx";
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