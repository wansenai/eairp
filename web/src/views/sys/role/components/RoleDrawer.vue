<template>
  <BasicDrawer
      v-bind="$attrs"
      @register="registerDrawer"
      showFooter
      :title="getTitle"
      width="500px"
      @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts" setup>
import { ref, computed, unref } from 'vue';
import { formSchema } from '@/views/sys/role/role.data';
import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
import {useForm} from "@/components/Form";
import BasicForm from "@/components/Form/src/BasicForm.vue";
import { addOrUpdateRole } from '/@/api/sys/role'
import {addOrUpdateRoleInfoReq} from "@/api/sys/model/roleModel";
import {useI18n} from "vue-i18n";
const emit = defineEmits(['success', 'register']);
const isUpdate = ref(true);
const rowId = ref('');
const { t } = useI18n();

const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
  labelWidth: 90,
  baseColProps: { span: 24 },
  schemas: formSchema,
  showActionButtonGroup: false,
});

const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
  setDrawerProps({ confirmLoading: false });
  isUpdate.value = !!data?.isUpdate;
  console.info(data.record)
  if (unref(isUpdate)) {
    rowId.value = data.record.id;
    setFieldsValue({
      ...data.record,
    });
  } else {
    resetFields();
  }
});

const getTitle = computed(() => (!unref(isUpdate) ? t('system.role.addRole') : t('system.role.editRole')));

async function handleSubmit() {
  try {
    const values = await validate();

    const saveOrUpdateRoleObject: addOrUpdateRoleInfoReq = {
      id: values.id !== null ? values.id : undefined,
      roleName: values.roleName,
      type: values.type,
      priceLimit: values.priceLimit,
      status:  values.status,
      description: values.description
    }
    const result = await addOrUpdateRole(saveOrUpdateRoleObject)
    if(result.code === 'A0004' || result.code === 'A0006') {
      closeDrawer();
      emit('success', {isUpdate: unref(isUpdate), values: {...values, id: rowId.value}});
      setDrawerProps({ confirmLoading: true });
    }
  } finally {
    setDrawerProps({ confirmLoading: false });
  }
}
</script>