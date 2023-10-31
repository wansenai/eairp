<!--
  - Copyright (C) 2023-2033 WanSen AI Team
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
import { ref, computed, unref} from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from '@/views/basic/warehouse/warehouse.data';
import { AddOrUpdateWarehouseReq } from '@/api/basic/model/warehouseModel';
import { addOrUpdateWarehouse } from '@/api/basic/warehouse';

const rowId = ref('');
const isUpdate = ref(true);
const getTitle = computed(() => (!unref(isUpdate) ? '新增仓库' : '编辑仓库'));
const emitSuccess = defineEmits(['success']);

const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
  labelWidth: 100,
  baseColProps: { span: 24 },
  schemas: formSchema,
  showActionButtonGroup: false,
  actionColOptions: {
    span: 23,
  },
});

const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
  resetFields();
  setModalProps({ confirmLoading: false, destroyOnClose: true,});
  isUpdate.value = !!data?.isUpdate;

  if (unref(isUpdate)) {
    rowId.value = data.record.id;
    setFieldsValue({
      ...data.record,
    });
  }
});

const handleSubmit = async () => {
  const values = await validate();
  setModalProps({ confirmLoading: true });

  if (unref(isUpdate)) {
    const updateWarehouseReq: AddOrUpdateWarehouseReq = {
      id: rowId.value,
      ...values,
    };
    const result = await addOrUpdateWarehouse(updateWarehouseReq);
    if (result.code === 'W0002') {
      closeModal();
      emitSuccess('success');
    }
  } else {
    const addOrUpdateWarehouseReq: AddOrUpdateWarehouseReq = {
      warehouseName: values.warehouseName,
      warehouseManager: values.warehouseManager,
      address: values.address,
      price: values.price,
      truckage: values.truckage,
      type: values.type,
      isDefault: values.isDefault,
      sort: values.sort,
      remark: values.remark,
    };

    const result = await addOrUpdateWarehouse(addOrUpdateWarehouseReq);
    if (result.code === 'W0001') {
      closeModal();
      emitSuccess('success');
    }
  }

  setModalProps({ confirmLoading: false });

};
</script>