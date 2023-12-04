<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增</a-button>
        <a-button type="primary" @click="handleBatchDelete"> 批量删除</a-button>
        <a-button type="primary" @click="handleExport"> 导出</a-button>
        <a-button type="primary" @click="handleOnStatus(1)"> 批量审核</a-button>
        <a-button type="primary" @click="handleOnStatus(0)"> 批量反审核</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
              :actions="[
              {
                icon: 'clarity:info-standard-line',
                tooltip: '查看收预付款单详情',
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
        <template v-else-if="column.key === 'status'">
          <Tag :color="record.status === 1 ? 'green' : 'red'">
            {{ record.status === 1 ? '已审核' : '未审核' }}
          </Tag>
        </template>
      </template>
    </BasicTable>
    <a-modal v-model:open="openExportData" title="确认导出" :confirm-loading="confirmLoading"
             @ok="handleExportOk" @cancel="handleExportCancel" okText="导出">
      <div style="text-align: center">
        <p>即将导出{{dataSum}}条数据，请耐心等待。</p>
        <p>如需导出明细数据（可能耗时较长），请勾选下方复选框。</p>
        <a-checkbox v-model:checked="exportDetailData">需要导出明细数据</a-checkbox>
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
export default defineComponent({
  name: 'advanceCharge',
  components: {'a-modal': Modal, 'a-checkbox': Checkbox, Tag, TableAction, BasicTable, AdvanceChargeModal, ViewAdvanceChargeModal},
  setup() {
    const [viewAdvanceChargeModalRef, {openModal: openAdvanceChargeModal}] = useModal();
    const { createMessage } = useMessage();
    const advanceChargeModalRef = ref(null);
    const exportDetailData = ref<boolean>(false);
    const openExportData = ref<boolean>(false);
    const confirmLoading = ref<boolean>(false);
    const dataSum = ref<number>(0);
    const [registerTable, { reload, getSelectRows, getForm, getDataSource }] = useTable({
      title: '收预付款列表',
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
        title: '操作',
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
        createMessage.warn('请选择一条数据');
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
        createMessage.warn('抱歉，只有未审核的单据才能编辑！');
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
        createMessage.warn('请选择一条数据');
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
        createMessage.warn('当前查询条件下无数据可导出');
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
        link.download = "收预付款单数据" + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
      confirmLoading.value = false;
      openExportData.value = false;
      exportDetailData.value = false;
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