<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          title="收款单-详情"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button key="1" type="primary">打印</a-button>
        </template>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item label="客户">{{ customerName }}</a-descriptions-item>
          <a-descriptions-item label="单据日期">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item label="财务人员">{{ financialPersonName }}</a-descriptions-item>
          <a-descriptions-item label="收款账户">{{ collectionAccountName }}</a-descriptions-item>
          <a-descriptions-item label="合计收款">{{ totalCollectionAmount }}</a-descriptions-item>
          <a-descriptions-item label="优惠金额">{{ discountAmount }}</a-descriptions-item>
          <a-descriptions-item label="实际收款">{{ actualCollectionAmount }}</a-descriptions-item>
          <a-descriptions-item label="备注">
            {{ remark }}
          </a-descriptions-item>
        </a-descriptions>
        <div class="extra">
          <div
              class="descriptions-context"
              :style="{
              display: 'flex',
              width: 'max-content',
              justifyContent: 'flex-end',
            }"
          >
            <a-statistic
                title="单据状态"
                :value="status === 1 ? '已审核' : '未审核'"
                :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                :style="{
                marginRight: '32px',
                color: 'green',
              }"
            />
            <a-statistic title="单据金额"
                         prefix="￥"
                         :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                         :value="totalCollectionAmount"/>
          </div>
        </div>
      </a-page-header>
      <BasicTable @register="registerTable">
      </BasicTable>
    </div>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicTable, useTable} from '/src/components/Table';
import {BasicModal, useModalInner} from "@/components/Modal";
import {getCollectionDetailById} from "@/api/financial/collection";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';
import {collectionReceiptTableColumns} from "@/views/financial/collection/collection.data";
export default defineComponent({
  name: 'ViewExpenseModal',
  components: {
    BasicModal,
    BasicTable,
    'a-page-header': PageHeader,
    'a-descriptions': Descriptions,
    'a-descriptions-item': DescriptionsItem,
    'a-statistic': Statistic,
  },
  setup() {
    const receiptNumber = ref('');
    const customerName = ref('');
    const receiptDate = ref('');
    const financialPersonName = ref('');
    const collectionAccountName = ref('');
    const totalCollectionAmount = ref('');
    const discountAmount = ref('');
    const actualCollectionAmount = ref('');
    const remark = ref('')
    const tableData = ref([]);
    const status = ref(-1);
    const [registerTable] = useTable({
      title: '收款单详情数据',
      columns: collectionReceiptTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
      canResize: false,
    });
    const getTitle = ref('单据详情');
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1000, showOkBtn: false});
      const res = await getCollectionDetailById(data.id);
      tableData.value = res.data.tableData;
      receiptNumber.value = res.data.receiptNumber;
      customerName.value = res.data.customerName;
      financialPersonName.value = res.data.financialPersonName;
      receiptDate.value = res.data.receiptDate;
      collectionAccountName.value = res.data.collectionAccountName;
      totalCollectionAmount.value = res.data.totalCollectionAmount;
      discountAmount.value = res.data.discountAmount;
      actualCollectionAmount.value = res.data.actualCollectionAmount;
      remark.value = res.data.remark;
      status.value = res.data.status;
    });

    function handleSubmit() {
      closeModal();
    }

    return {
      receiptNumber,
      customerName,
      receiptDate,
      financialPersonName,
      collectionAccountName,
      totalCollectionAmount,
      discountAmount,
      actualCollectionAmount,
      remark,
      status,
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
    };
  },
});
</script>
<style scoped>

</style>
