<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm"/>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, ref, computed, unref} from 'vue';
import {BasicModal, useModalInner} from '@/components/Modal';
import {BasicForm, useForm} from '@/components/Form/index';
import {getUserBindDept} from '@/api/sys/dept';
import {addOrUpdateTenant} from '@/api/sys/tenant';
import {tenantFormSchema} from "@/views/sys/tenant/tenant.data";
import {useI18n} from "vue-i18n";
import {addOrUpdateTenantReq} from "@/api/sys/model/tenantModel";

export default defineComponent({
  name: 'TenantModal',
  components: {BasicModal, BasicForm},
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const { t } = useI18n();
    const isUpdate = ref(true);
    const rowId = ref('');

    const [registerForm, {setFieldsValue, updateSchema, resetFields, validate}] = useForm({
      labelWidth: 100,
      baseColProps: {span: 24},
      schemas: tenantFormSchema,
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

    const getTitle = computed(() => (!unref(isUpdate) ? t('sys.tenant.addTenant') : t('sys.tenant.editTenant')));
    const showPassword = getTitle.value;

    async function handleSubmit() {
      try {
        const values = await validate();
        setModalProps({confirmLoading: true});

        const userObject: addOrUpdateTenantReq = {
          id: values.id !== null ? values.id : '',
          username: values.username,
          password: values.password,
          tenantName: values.tenantName,
          type: values.type,
          status: values.status,
          userNumLimit: values.userNumLimit,
          expireTime: values.expireTime,
          email: values.email,
          phoneNumber: values.phoneNumber,
          roleId: values.roleId,
          deptId: values.deptId,
          remark: values.remark,
        }
        const result = await addOrUpdateTenant(userObject)
        if(result.code === 'A0300' || result.code === 'A0301') {
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
