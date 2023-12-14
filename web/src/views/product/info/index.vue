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
        <a-button type="primary" @click="handleBatchProductInfo"> 批量编辑</a-button>
        <a-button type="primary" @click=""> 修正库存</a-button>
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
    <ProductInfoModal ref="productModalRef" @cancel="handleCancel" @success="handleOk"/>
    <BatchEditModal ref="batchProductInfoModalRef" @cancel="handleCancel" @success="handleOk"/>
    <ImportFileModal ref="importModalRef" @cancel="handleCancel"/>
  </div>
</template>
<div>
</div>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {columns, searchFormSchema} from "@/views/product/info/info.data";
import ProductInfoModal from "@/views/product/info/components/ProductInfoModal.vue";
import BatchEditModal from "@/views/product/info/components/BatchEditModal.vue";
import {getProductInfo, deleteProduct, updateProductStatus} from "@/api/product/product";
import ImportFileModal from "@/components/Tools/ImportFileModal.vue";
import {exportXlsx} from "@/api/basic/common";

export default defineComponent({
  name: 'ProductInfo',
  components: {TableAction, BasicTable, ProductInfoModal, BatchEditModal, ImportFileModal},
  setup() {
    const { createMessage } = useMessage();
    const importModalRef = ref(null);
    const productModalRef = ref(null);
    const batchProductInfoModalRef = ref(null);
    const [registerTable, { reload, getSelectRows }] = useTable({
      title: '商品信息列表',
      rowKey: 'id',
      columns: columns,
      api: getProductInfo,
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
        title: '操作',
        dataIndex: 'action',
        fixed: undefined,
      },
    });

    function handleCreate() {
      productModalRef.value.openProductInfoModal()
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
      productModalRef.value.openProductInfoModal(record.id)
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
      batchProductInfoModalRef.value.open(ids)
    }

    function handleImport() {
      const templateUrl  = 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/%E4%BC%9A%E5%91%98%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF.xlsx'
      const templateName  = '商品信息Excel模板[下载]'
      importModalRef.value.initModal(templateUrl, templateName);
      importModalRef.value.title = "商品信息数据导入";
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
      handleSuccess,
      handleOnStatus,
      handleCancel,
      handleOk,
      productModalRef,
      batchProductInfoModalRef,
      handleBatchProductInfo,
      handleImport,
      handleExport,
      importModalRef
    }
  }
})
</script>