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
  <BasicModal @register="registerModal" @ok="handleSubmit">
    <BasicForm @register="registerForm">
      <template #menu="{ model, field }">
        <BasicTree
          v-model:value="model[field]"
          :treeData="treeData"
          :fieldNames="{ title: 'meta.title', key: 'id' }"
          checkable
          toolbar
          title="菜单分配"
          v-if="treeData.length > 0"
        />
      </template>
    </BasicForm>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicTree, TreeItem } from '/@/components/Tree';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { getMenuList } from '@/api/sys/menu';
  import { roleSchema } from '@/views/sys/role/role.data';
  import { rolePermission } from '@/api/sys/role';
  import {array2tree} from "@axolo/tree-array";
  import {addOrUpdateRolePermissionReq} from "@/api/sys/model/roleModel";

  const treeData = ref<TreeItem[]>([]);
  const emit = defineEmits(['success', 'register']);
  const [registerForm, {setFieldsValue, validate}] = useForm({
    labelWidth: 90,
    baseColProps: { span: 24 },
    schemas: roleSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    if (unref(treeData).length === 0) {
      const menus = await getMenuList();
      const menuTree = array2tree(menus.data.data);
      treeData.value = menuTree as any as TreeItem[];
    }
    setFieldsValue({...data.record});
  });

  async function handleSubmit() {
    setModalProps({ confirmLoading: true });
    try {
      const values = await validate();
      const object: addOrUpdateRolePermissionReq = {
        id: values.id !== null ? values.id : undefined,
        menuIds: values.menuIds
      }
      console.info(object)
      const result =  await rolePermission(object);
      if(result.code === 'A0008') {
        emit('success');
      }
    } finally {
      closeModal();
      setModalProps({ confirmLoading: false });
    }
  }
</script>
