<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> {{ t('system.department.addDepartment') }}</a-button>
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
    <DeptModal @register="registerModal" @success="handleSuccess"/>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {BasicTable, useTable, TableAction} from '/@/components/Table';
import {getDeptList} from '/@/api/sys/dept'
import {useModal} from '/@/components/Modal'
import DeptModal from '@/views/sys/department/components/DeptModal.vue';
import {columns, searchFormSchema} from '@/views/sys/department/dept.data';
import { deleteDept } from "@/api/sys/dept";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'DeptManagement',
  components: { BasicTable, DeptModal, TableAction},
  setup() {
    const { t } = useI18n();
    const [registerModal, { openModal }] = useModal();
    const [registerTable, { reload }] = useTable({
      title: t('system.department.title'),
      api: getDeptList,
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      pagination: false,
      striped: false,
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      canResize: false,
      actionColumn: {
        width: 80,
        title: t('common.operating'),
        dataIndex: 'action',
        // slots: { customRender: 'action' },
        fixed: undefined,
      },
    });

    function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }

    function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      const result = await deleteDept(record.id);
      if (result.code === 'A0011') {
        await reload();
      }
    }

    function handleSuccess() {
      reload();
    }

    return {
      t,
      registerTable,
      registerModal,
      handleCreate,
      handleSuccess,
      handleEdit,
      handleDelete,
    };
  },
});
</script>

<style scoped lang="less">

</style>
