<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicTable @register="registerTable">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <Tag :color="record.status === 1 ? 'green' : 'red'">
            <template v-if="localeStore === 'zh_CN'">
              {{ record.status === 1 ? '已审核' : '未审核' }}
            </template>
            <template v-if="localeStore === 'en'">
              {{ record.status === 1 ? 'Audited' : 'Unaudited' }}
            </template>
          </Tag>
        </template>
        <template v-else-if="column.key === 'receiptNumber'">
          <a @click="">{{record.receiptNumber}}</a>
        </template>
      </template>
    </BasicTable>
  </BasicModal>
  <ReceiptDetailModal @register="receiptDetailModal" @handleReceiptSuccess="handleReceiptSuccess"/>
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {BasicModal, useModal, useModalInner} from "@/components/Modal";
import {Tag} from "ant-design-vue";
import {getReceipt} from "@/api/receipt/receipt";
import ReceiptDetailModal from "@/views/receipt/ReceiptDetailModal.vue";
import {ReceiptColumn, searchSchema} from "@/views/receipt/receipt.data"
import {useI18n} from "vue-i18n";
import {useLocaleStore} from "@/store/modules/locale";
export default defineComponent({
  name: 'LinkReceiptModal',
  components: {Tag, BasicModal, BasicTable, TableAction, ReceiptDetailModal},
  emits: ['handleReceiptSuccess', 'register'],
  setup(_, context) {
    const { t } = useI18n();
    const getTitle = ref('');
    const title = ref('');
    const localeStore = useLocaleStore().getLocale;
    if(localeStore === 'zh_CN') {
      title.value = '操作'
    } else if (localeStore === 'en') {
      title.value = 'Operate'
    }
    const [receiptDetailModal, {openModal: openReceiptDetailModal}] = useModal();
    const { createMessage } = useMessage();
    const type = ref('');
    const subType = ref('');
    const [registerTable, { getSelectRows, reload}] = useTable({
      title: title.value,
      rowKey: 'id',
      columns: ReceiptColumn,
      api: getReceipt,
      rowSelection: {
        type: 'radio',
      },
      formConfig: {
        labelWidth: 110,
        schemas: searchSchema,
      },
      showIndexColumn: false,
      bordered: true,
      showTableSetting: true,
      canResize: false,
      tableSetting: { fullScreen: true },
      useSearchForm: true,
      clickToRowSelect: true,
      immediate: false,
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1200});
      type.value = data.type
      subType.value = data.subType
      getTitle.value = data.title
      searchSchema[0].defaultValue = data.type
      searchSchema[1].defaultValue = data.subType
      reload({
        searchInfo: {
          "type": type.value,
          "subType": subType.value,
        }
      });
    });

    function handleSubmit() {
      const rows = getSelectRows();
      if (rows.length === 0) {
        if(localeStore === 'zh_CN') {
          createMessage.error('请选择单据');
        } else if (localeStore === 'en') {
          createMessage.error('Please select a receipt');
        }
        return;
      }
      openReceiptDetailModal(true, {
        isUpdate: false,
        id: rows[0].id,
        receiptNumber: rows[0].receiptNumber,
        type: type.value,
        uid: rows[0].uid,
        operatorIds: rows[0].operatorIds,
        accountId: rows[0].accountId,
      });
      closeModal();
    }

    function handleReceiptSuccess(data) {
      context.emit('handleReceiptSuccess', data);
    }

    return {
      t,
      localeStore,
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
      receiptDetailModal,
      handleReceiptSuccess,
    }
  }
})
</script>