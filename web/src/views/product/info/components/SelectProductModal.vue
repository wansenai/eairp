<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
  <BasicTable @register="registerTable"/>
  </BasicModal>
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {extendPriceColumn, searchFormSchema} from "@/views/product/info/info.data";
import {getProductSkuPage} from "@/api/product/product";
import {BasicModal, useModalInner} from "@/components/Modal";
import {useI18n} from "vue-i18n";
export default defineComponent({
  name: 'productModal',
  components: {BasicModal, BasicTable, TableAction},
  emits: ['register', 'handleCheckSuccess'],
  setup(_, context) {
    const { t } = useI18n();
    const getTitle = ref(t('product.selectProduct'));
    const { createMessage } = useMessage();
    const [registerTable, { getSelectRows }] = useTable({
      title: t('product.productList'),
      rowKey: 'id',
      columns: extendPriceColumn,
      api: getProductSkuPage,
      rowSelection: {
        type: 'checkbox',
      },
      formConfig: {
        labelWidth: 110,
        schemas: searchFormSchema,
      },
      bordered: true,
      showTableSetting: true,
      canResize: true,
      tableSetting: { fullScreen: true },
      useSearchForm: true,
      clickToRowSelect: true,
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({confirmLoading: false, destroyOnClose: true, width: 1300});
    });

    function handleSubmit() {
      const rows = getSelectRows();
      if (rows.length === 0) {
        createMessage.warn(t('product.inputSelectProduct'));
        return;
      }
      context.emit('handleCheckSuccess', rows);
      closeModal();
    }

    return {
      t,
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
    }
  }
})
</script>