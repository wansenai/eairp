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

<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from '@/views/sys/department/dept.data';

import { addOrUpdateDept } from '@/api/sys/dept';
import {addOrUpdateDeptReq} from "@/api/sys/model/dpetModel";

export default defineComponent({
  name: 'DeptModal',
  components: { BasicModal, BasicForm },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const isUpdate = ref(true);

    const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
      labelWidth: 100,
      baseColProps: { span: 24 },
      schemas: formSchema,
      showActionButtonGroup: false,
    });

    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      resetFields();
      setModalProps({ confirmLoading: false, destroyOnClose: true });
      isUpdate.value = !!data?.isUpdate;

      if (unref(isUpdate)) {
        setFieldsValue({
          ...data.record,
        });
      }
    });

    const getTitle = computed(() => (!unref(isUpdate) ? '新增部门' : '编辑部门'));

    async function handleSubmit() {
      try {
        const values = await validate();

        const deptObject: addOrUpdateDeptReq = {
          id: values.id,
          deptName: values.deptName,
          parentId: values.parentId,
          deptNumber: values.deptNumber,
          leader: values.leader,
          status: values.status,
          remark: values.remark,
          sort: values.sort,
        };
        console.info(deptObject);

        const result = await addOrUpdateDept(deptObject);
        if(result.code === 'A0009' || result.code === 'A0010') {
          closeModal();
          emit('success');
        }
      } finally {
        setModalProps({confirmLoading: false});
      }
    }

    return { registerModal, registerForm, getTitle, handleSubmit };
  },
});
</script >

<style scoped lang="less">

</style>