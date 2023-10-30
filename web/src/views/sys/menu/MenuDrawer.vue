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
  <BasicDrawer
      v-bind="$attrs"
      @register="registerDrawer"
      showFooter
      :title="getTitle"
      width="50%"
      @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from './menu.data';
import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
import {AddOrUpdateMenuReq} from "@/api/sys/model/menuModel";
import {addOrUpdateMenu} from "@/api/sys/menu";

export default defineComponent({
  name: 'MenuDrawer',
  components: { BasicDrawer, BasicForm },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const isUpdate = ref(true);
    const menuId = ref<number>(0);
    const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
      baseColProps: { lg: 12, md: 24 },
    });

    const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
      resetFields();
      setDrawerProps({ confirmLoading: false });
      isUpdate.value = !!data?.isUpdate;

      if (unref(isUpdate)) {
        setFieldsValue({
          ...data.record,
        });
      }
      if ('record' in data) {
        menuId.value = data.record.id;
      }
    });

    const getTitle = computed(() => (!unref(isUpdate) ? '新增菜单' : '编辑菜单'));

    async function handleSubmit() {
      try {
        const values = await validate();
        setDrawerProps({ confirmLoading: true });

        console.info(values)
        console.info(values.ignoreKeepAlive)
        console.info(values.blank)

        const saveOrUpdateObject: AddOrUpdateMenuReq = {
          id: values.id,
          parentId: values.parentId,
          name: values.name,
          title: values.title,
          menuType: values.menuType,
          icon: values.icon,
          path: values.path,
          component: values.component,
          sort: values.sort,
          blank: values.blank,
          status: values.status,
          ignoreKeepAlive: values.ignoreKeepAlive,
          hideMenu: values.hideMenu,
        };

        console.info(saveOrUpdateObject)

        const result = await addOrUpdateMenu(saveOrUpdateObject);
        if(result.code === 'A0012' || result.code === 'A0013') {
          closeDrawer();
          emit('success');
        }
      } finally {
        setDrawerProps({confirmLoading: false});
      }
    }

    return { registerDrawer, registerForm, getTitle, handleSubmit };
  },
});
</script>
<style scoped lang="less">

</style>