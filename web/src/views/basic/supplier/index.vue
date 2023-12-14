<template>
    <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增</a-button>
        <a-button type="primary" @click="handleBatchDelete"> 批量删除</a-button>
        <a-button type="primary" @click="handleOnStatus(0)"> 批量启用</a-button>
        <a-button type="primary" @click="handleOnStatus(1)"> 批量停用</a-button>
        <a-button type="primary" @click="handleImport"> 导入</a-button>
        <a-button type="primary" @click="handleExport"> 导出</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
              {
                icon: 'clarity:note-edit-line',
                onClick: handleEdit.bind(null, record),
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除',
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

export default defineComponent({
  name: 'Supplier',
  components: {TableAction, BasicTable, SupplierModal, ImportFileModal },
  setup() {
    const currentTime = ref(null);
    const timestamp = ref(null);
    const [registerModal, {openModal}] = useModal();
    const { createMessage } = useMessage();
    const importModalRef = ref(null);
    const [registerTable, { reload, getSelectRows, getDataSource, getForm }] = useTable({
      title: '供应商列表',
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
        title: '操作',
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
        createMessage.warn('请选择一条数据');
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
        createMessage.warn('请选择一条数据');
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
      const templateName  = '供应商Excel模板[下载]'
      importModalRef.value.initModal(templateUrl, templateName);
      importModalRef.value.title = "供应商导入";
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
        createMessage.warn('当前查询条件下无数据可导出');
        return;
      }
      const data: any = getForm().getFieldsValue();
      const file: any = await exportSupplier(data)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        const timestamp = getTimestamp(new Date());
        link.download = "供应商信息数据" + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }

    return {
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