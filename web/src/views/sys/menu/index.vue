<template>
  <div>
    <BasicTable @register="registerTable" @fetch-success="onFetchSuccess">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> {{ t('system.menu.addMenu') }} </a-button>
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
                color: 'error',
                tooltip: t('sys.table.delete'),
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
    <MenuDrawer @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>

<script lang="ts">
import { defineComponent, nextTick } from 'vue';

import { BasicTable, useTable, TableAction } from '/@/components/Table';
import {deleteMenu, getMenuList} from '/@/api/sys/menu';
import { useDrawer } from '/@/components/Drawer';
import MenuDrawer from './MenuDrawer.vue';
import { columns, searchFormSchema } from './menu.data';
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'MenuManagement',
  components: { BasicTable, MenuDrawer, TableAction },
  setup() {
    const { t } = useI18n();
    const [registerDrawer, { openDrawer }] = useDrawer();
    const [registerTable, { reload, expandAll }] = useTable({
      title: t('system.menu.title'),
      api: getMenuList,
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      isTreeTable: true,
      pagination: false,
      striped: false,
      useSearchForm: false,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      canResize: false,
      actionColumn: {
        width: 80,
        title: t('common.operating'),
        dataIndex: 'action',
        fixed: undefined,
      },
    });

    function handleCreate() {
      openDrawer(true, {
        isUpdate: false,
      });
    }

    function handleEdit(record: Recordable) {
      openDrawer(true, {
        record,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      const result = await deleteMenu(record.id);
      if (result.code === 'A0014') {
        await reload();
      }
    }

    function handleSuccess() {
      reload();
    }

    function onFetchSuccess() {
      // 演示默认展开所有表项
      nextTick(expandAll);
    }

    return {
      t,
      registerTable,
      registerDrawer,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
      onFetchSuccess,
    };
  },
});
</script>

<style scoped lang="less">

</style>