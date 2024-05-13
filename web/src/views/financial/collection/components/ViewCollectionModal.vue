<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div class="components-page-header-demo-responsive" style="border: 1px solid rgb(235, 237, 240)">
      <a-page-header
          :title="t('financial.collection.collectionReceiptDetail')"
          :sub-title= "receiptNumber">
        <template #extra>
          <a-button @click="exportTable" v-text="t('financial.collection.export.name')"/>
          <a-button @click="primaryPrint" type="primary" v-text="t('financial.regularPrint')"/>
          <!--          <a-button key="triplePrint">三联打印</a-button>-->
          <!--          <a-button key="2" type="primary">发起流程审批</a-button>-->
        </template>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item :label="t('financial.collection.header.customer')">{{ customerName }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.collection.header.receiptDate')">{{ receiptDate }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.collection.header.financialPerson')">{{ financialPersonName }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.collection.header.collectionAccount')">{{ collectionAccountName }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.collection.table.totalCollection')">{{ totalCollectionAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.collection.table.discountAmount')">{{ discountAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.collection.table.actualCollection')">{{ actualCollectionAmount }}</a-descriptions-item>
          <a-descriptions-item :label="t('financial.collection.table.remark')">
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
                :title="t('financial.collection.table.status')"
                :value="status === 1 ? t('sys.table.audited') : t('sys.table.unaudited')"
                :value-style="status === 1 ? { color: '#3f8600' } : { color: '#cf1322' }"
                :style="{
                marginRight: '32px',
                color: 'green',
              }"
            />
            <a-statistic :title="t('financial.collection.table.totalCollection')"
                         :prefix="amountSymbol"
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
import {getCollectionDetailById, exportCollectionDetail} from "@/api/financial/collection";
import {
  Descriptions,
  DescriptionsItem,
  PageHeader,
  Statistic,
} from 'ant-design-vue';
import {collectionReceiptTableColumns} from "@/views/financial/collection/collection.data";
import printJS from "print-js";
import {getTimestamp} from "@/utils/dateUtil";
import {useI18n} from "vue-i18n";
import {useLocaleStore} from "@/store/modules/locale";
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
    const { t } = useI18n();
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
    const amountSymbol = ref<string>('')
    const localeStore = useLocaleStore().getLocale;
    if(localeStore === 'zh_CN') {
      amountSymbol.value = '￥'
    } else if (localeStore === 'en') {
      amountSymbol.value = '$'
    }
    const [registerTable] = useTable({
      title: t('financial.collection.receiptDetail'),
      columns: collectionReceiptTableColumns,
      dataSource: tableData,
      pagination: false,
      showIndexColumn: false,
      bordered: true,
      canResize: false,
    });
    const getTitle = ref(t('financial.collection.receiptDetail'));
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

    async function exportTable() {
      const file: any = await exportCollectionDetail(receiptNumber.value)
      if (file.size > 0) {
        const blob = new Blob([file]);
        const link = document.createElement("a");
        const timestamp = getTimestamp(new Date());
        link.href = URL.createObjectURL(blob);
        link.download = t('financial.collection.receiptDetail') + timestamp + ".xlsx";
        link.target = "_blank";
        link.click();
      }
    }

    const flexContainer = 'display: flex; justify-content: space-between; border-bottom: 1px solid #ddd; padding: 8px;';
    const flexItem = 'display: flex; flex-direction: column; justify-content: space-between; font-size: 12px;';
    function primaryPrint() {
      const header = `
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('financial.collection.header.receiptNumber')}：${receiptNumber.value}</div>
    <div style="${flexItem}">${t('financial.collection.header.receiptDate')}：${receiptDate.value}</div>
    <div style="${flexItem}">${t('financial.collection.table.totalCollection')}：${totalCollectionAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('financial.collection.table.collectionAccount')}：${collectionAccountName.value}</div>
    <div style="${flexItem}">${t('financial.collection.table.discountAmount')}：${discountAmount.value}</div>
    <div style="${flexItem}">${t('financial.collection.table.actualCollection')}：${actualCollectionAmount.value}</div>
  </div>
  <div style="${flexContainer}">
    <div style="${flexItem}">${t('financial.collection.table.customer')}：${customerName.value}</div>
    <div style="${flexItem}">${t('financial.collection.table.financialPerson')}：${financialPersonName.value}</div>
    <div style="${flexItem}">${t('financial.collection.table.remark')}：${remark.value}</div>
  </div>
`;
      printJS({
        documentTitle: "EAIRP " + t('financial.collection.receiptDetail'),
        header: header,
        properties: collectionReceiptTableColumns.map((item) => {
          return {
            field: item.dataIndex,
            displayName: item.title,
          };
        }),
        printable: tableData.value,
        gridHeaderStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        gridStyle: 'border: 1px solid #ddd; font-size: 12px; text-align: center; padding: 8px;',
        type: 'json',
      });
    }

    return {
      t,
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
      exportTable,
      primaryPrint,
      amountSymbol
    };
  },
});
</script>
<style scoped>

</style>
