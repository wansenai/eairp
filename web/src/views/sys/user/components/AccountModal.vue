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
    <BasicForm @register="registerForm"/>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, ref, computed, unref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {accountFormSchema} from '@/views/sys/user/account.data';
import {getUserBindDept} from '/@/api/sys/dept';
import {addOrUpdateUser} from '/@/api/sys/user';
import {addOrUpdateUserReq} from '@/api/sys/model/userModel'
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'AccountModal',
  components: {BasicModal, BasicForm},
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const { t } = useI18n();
    const isUpdate = ref(true);
    const rowId = ref('');

    const [registerForm, {setFieldsValue, updateSchema, resetFields, validate}] = useForm({
      labelWidth: 100,
      baseColProps: {span: 24},
      schemas: accountFormSchema,
      showActionButtonGroup: false,
      actionColOptions: {
        span: 23,
      },
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      resetFields();
      setModalProps({confirmLoading: false});
      isUpdate.value = !!data?.isUpdate;

      if (unref(isUpdate)) {
        rowId.value = data.record.id;
        setFieldsValue({
          ...data.record,
        });

        const treeData = (await getUserBindDept()).data
        updateSchema([
          {
            field: 'deptName',
            componentProps: {treeData},
          },
        ]);
      }
    });

    const getTitle = computed(() => (!unref(isUpdate) ? t('sys.user.addAccount') : t('sys.user.editAccount')));
    const showPassword = getTitle.value;

    async function handleSubmit() {
      try {
        const values = await validate();
        setModalProps({confirmLoading: true});

        const userObject: addOrUpdateUserReq = {
          id: values.id !== null ? values.id : '',
          username: values.username,
          password: values.password,
          name: values.name,
          email: values.email,
          phoneNumber: values.phoneNumber,
          roleId: values.roleId,
          deptId: values.deptId,
          remake: values.remake,
        }
        console.info(userObject)
        const result = await addOrUpdateUser(userObject)
        if(result.code === 'A0002' || result.code === 'A0014') {
          closeModal();
          emit('success', {isUpdate: unref(isUpdate), values: {...values, id: rowId.value}});
        }
      } finally {
        setModalProps({confirmLoading: false});
      }
    }

    return {registerModal, registerForm, getTitle, handleSubmit, showPassword};
  },
});
</script>
