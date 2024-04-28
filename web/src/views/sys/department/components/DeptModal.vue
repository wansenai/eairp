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
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'DeptModal',
  components: { BasicModal, BasicForm },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const isUpdate = ref(true);
    const { t } = useI18n();
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

    const getTitle = computed(() => (!unref(isUpdate) ? t('system.department.addDepartment') : t('system.department.editDepartment')));

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

    return { t, registerModal, registerForm, getTitle, handleSubmit };
  },
});
</script >

<style scoped lang="less">

</style>