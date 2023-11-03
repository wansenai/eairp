<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
  <BasicTable @register="registerTable"/>
  </BasicModal>
</template>

<script lang="ts">

// 创建一个pinia store
const store = defineStore('modalStore', {
  state: () => ({
    data: null,
  }),
});


import {defineComponent, ref} from "vue";
import { defineStore } from 'pinia';
import {BasicTable, TableAction, useTable} from "@/components/Table";
import {useMessage} from "@/hooks/web/useMessage";
import {extendPriceColumn, searchFormSchema} from "@/views/product/info/info.data";
import {getProductSkuPage} from "@/api/product/product";
import {BasicModal, useModalInner} from "@/components/Modal";
export default defineComponent({
  name: 'productModal',
  components: {BasicModal, BasicTable, TableAction},
  emits: ['selectedRows'],
  setup(_, context) {
    const getTitle = ref('选择商品');
    const { createMessage } = useMessage();
    const [registerTable, { getSelectRows }] = useTable({
      title: '商品列表',
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
        createMessage.error('请选择商品');
        return;
      }
      context.emit('handleCheckSuccess', rows);
      closeModal();
    }

    return {
      registerTable,
      registerModal,
      getTitle,
      handleSubmit,
    }
  }
})
</script>