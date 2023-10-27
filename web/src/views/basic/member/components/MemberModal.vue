<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script setup lang="ts">
import { ref, computed, unref} from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from '@/views/basic/member/member.data';
import { AddOrUpdateMemberReq } from '@/api/basic/model/memberModel';
import { addOrUpdateMember } from '@/api/basic/member';

  const rowId = ref('');
  const isUpdate = ref(true);
  const getTitle = computed(() => (!unref(isUpdate) ? '新增会员信息' : '编辑会员信息'));
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
    setModalProps({ confirmLoading: false, destroyOnClose: true, width: 900 });
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
      const updateMemberObject: AddOrUpdateMemberReq = {
        id: rowId.value,
        ...values,
      };
      const result = await addOrUpdateMember(updateMemberObject);
      if (result.code === 'M0002') {
        closeModal();
        emitSuccess('success');
      }
    } else {
      const addOrUpdateMemberObject: AddOrUpdateMemberReq = {
        memberNumber: values.memberNumber,
        memberName: values.memberName,
        phoneNumber: values.phoneNumber,
        email: values.email,
        advancePayment: values.advancePayment,
        sort: values.sort,
        remark: values.remark,
      };

      const result = await addOrUpdateMember(addOrUpdateMemberObject);
      if (result.code === 'M0001') {
        closeModal();
        emitSuccess('success');
      }
    }

    setModalProps({ confirmLoading: false });

};
</script>