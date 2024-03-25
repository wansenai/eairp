<template>
  <div>
    <BasicTable @register="registerTable" @fetch-success="onFetchSuccess">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate" v-text="t('product.category.add')" />
        <a-button type="primary" @click="handleBatchDelete" v-text="t('product.category.batchDelete')" />
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
    <CategoryModal @register="registerModal" @success="handleSuccess"/>
  </div>
</template>

<script lang="ts">
import {defineComponent, nextTick} from 'vue';
import {useModal} from "@/components/Modal";
import CategoryModal from "@/views/product/category/components/CategoryModal.vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {getCategoryList, deleteCategory} from "@/api/product/productCategory";
import {columns} from "@/views/product/category/category.data";
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'ProductCategory',
  components: {
    BasicTable, TableAction, CategoryModal,
  },
  setup() {
    const { t } = useI18n();
    const { createMessage } = useMessage();
    const [registerModal, {openModal}] = useModal();
    const [registerTable, {reload, expandAll, getSelectRows}] = useTable({
      title: t('product.category.title'),
      api: getCategoryList,
      columns,
      formConfig: {
        labelWidth: 120,
      },
      isTreeTable: true,
      rowSelection: {
        type: 'checkbox',
      },
      titleHelpMessage: t('product.category.tip'),
      // 产品分类不开启分页和行选中
      pagination: false,
      clickToRowSelect: false,
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

    function handleSuccess() {
      reload();
    }

    function onFetchSuccess() {
      nextTick(expandAll);
    }

    async function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      const result = await deleteCategory([record.id]);
      if (result.code === 'P0002') {
        await reload();
      }
    }

    async function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }

    async function handleBatchDelete() {
      // 通过getSelectRowKeys获取勾选的复选框，如果没有勾选复选框，就提示选择一条数据
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn(t('product.selectData'));
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await deleteCategory(ids);
      if (result.code === 'P0002') {
        await reload();
      }
    }

    return {
      t,
      registerModal,
      registerTable,
      handleEdit,
      handleDelete,
      onFetchSuccess,
      handleSuccess,
      handleCreate,
      handleBatchDelete,
    };
  }
})
</script>