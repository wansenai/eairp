<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增任务</a-button>
        <a-button type="primary" @click="handleBatchDelete"> 删除</a-button>
        <a-button type="primary" @click="handleBatchProductInfo"> 重新加工</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
              {
                icon: 'clarity:info-standard-line',
                tooltip: '查看单据详情',
                onClick: handleView.bind(null, record),
              },
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
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {columns, searchFormSchema} from "@/views/production/tasks/task.data";
import {getProductInfo, deleteProduct, updateProductStatus} from "@/api/product/product";
import {exportXlsx} from "@/api/basic/common";

export default defineComponent({
  name: 'ProductInfo',
  components: {TableAction, BasicTable},
  setup() {
    const { createMessage } = useMessage();
    const [registerTable, { reload, getSelectRows }] = useTable({
      title: '生产任务',
      rowKey: 'id',
      columns: columns,
      api: getProductInfo,
      rowSelection: {
        type: 'checkbox',
      },
      showIndexColumn: false,
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
        title: '操作',
        dataIndex: 'action',
        fixed: undefined,
      },
    });

    function handleCreate() {
    }

    async function handleBatchDelete() {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn('请选择一条数据');
        return;
      }
      const ids = data.map((item) => item.id);
      const { code } = await deleteProduct(ids);
      if (code === "P0012") {
        await reload();
      }
    }

    function handleEdit(record: Recordable) {
    }

    function handleView(record: Recordable) {
    }

    async function handleDelete(record: Recordable) {
      // 调用deleteProduct接口 注意是批量删除 这里传递的是数组
      const { code } = await deleteProduct([record.id]);
      if (code === "P0012") {
        await reload();
      }
    }

    async function handleSuccess() {
      reload();
    }

    async function handleOnStatus(newStatus: number) {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn('请选择一条数据');
        return;
      }
      const ids = data.map((item) => item.id);
      const result = await updateProductStatus(ids, newStatus);
      if (result.code === "P0013") {
        await reload();
      }
    }

    async function handleCancel() {
      await reload();
    }

    async function handleOk() {
      await reload();
    }

    function handleBatchProductInfo() {
      const data = getSelectRows();
      if (data.length === 0) {
        createMessage.warn('请选择一条数据');
        return;
      }
      const ids = data.map((item) => item.id);
    }

    function handleImport() {
      const templateUrl  = 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/%E4%BC%9A%E5%91%98%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF.xlsx'
      const templateName  = '商品信息Excel模板[下载]'
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
      const file = await exportXlsx("商品信息列表")
      const blob = new Blob([file]);
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      const timestamp = getTimestamp(new Date());
      link.download = "商品信息数据" + timestamp + ".xlsx";
      link.target = "_blank";
      link.click();
    }


    return {
      registerTable,
      handleCreate,
      handleDelete,
      handleBatchDelete,
      handleEdit,
      handleView,
      handleSuccess,
      handleOnStatus,
      handleCancel,
      handleOk,
      handleBatchProductInfo,
      handleImport,
      handleExport
    }
  }
})
</script>