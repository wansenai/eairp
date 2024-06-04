<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate" v-text="t('product.info.add')" />
        <a-button type="primary" @click="handleBatchDelete" v-text="t('product.info.batchDelete')" />
        <a-button type="primary" @click="handleOnStatus(0)" v-text="t('product.info.batchEnable')" />
        <a-button type="primary" @click="handleOnStatus(1)" v-text="t('product.info.batchDisable')" />
        <a-button type="primary" @click="handleImport"  v-text="t('product.info.import')" />
        <a-button type="primary" @click="handleExport" v-text="t('product.info.export')" />
        <a-button type="primary" @click="handleBatchProductInfo" v-text="t('product.info.batchEdit')" />
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
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'ProductInfo',
  components: {TableAction, BasicTable, ProductInfoModal, BatchEditModal, ImportFileModal},
  setup() {
    const { t } = useI18n();
    const { createMessage } = useMessage();
    const importModalRef = ref(null);
    const productModalRef = ref(null);
    const batchProductInfoModalRef = ref(null);
    const [registerTable, { reload, getSelectRows }] = useTable({
      title: t('product.info.title'),
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
      showIndexColumn: false,
      actionColumn: {
        width: 80,
        title: t('common.operating'),
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
        createMessage.warn(t('product.selectData'));
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
        createMessage.warn(t('product.selectData'));
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
        createMessage.warn(t('product.selectData'));
        return;
      }
      const ids = data.map((item) => item.id);
      batchProductInfoModalRef.value.open(ids)
    }

    function handleImport() {
      const templateUrl  = 'https://wansen-1317413588.cos.ap-shanghai.myqcloud.com/Commodity%20information%20template%28%E5%95%86%E5%93%81%E4%BF%A1%E6%81%AF%E6%A8%A1%E6%9D%BF%29.xlsx'
      const templateName  = t('product.info.importInfo.templateName')
      importModalRef.value.initModal(templateUrl, templateName);
      importModalRef.value.title = t('product.info.importInfo.title')
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
      link.download = t('product.importInfo.infoData') + timestamp + ".xlsx";
      link.target = "_blank";
      link.click();
    }


    return {
      t,
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