<template>
  <div class="m-4 mr-0 overflow-hidden bg-white">
    <BasicTree
      title="部门列表"
      toolbar
      search
      treeWrapperClassName="h-[calc(100%-35px)] overflow-auto"
      :clickRowToExpand="false"
      :treeData="treeData"
      :fieldNames="{ key: 'id', title: 'deptName' }"
      @select="handleSelect"
    />
  </div>
</template>
<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';

import {BasicTree, TreeItem} from '/@/components/Tree';
import {getDeptList} from '/@/api/sys/dept';

export default defineComponent({
    name: 'DeptTree',
    components: { BasicTree },

    emits: ['select'],
    setup(_, { emit }) {
      const treeData = ref<TreeItem[]>([]);
      async function fetch() {
        const deptData = await getDeptList();
        treeData.value = (deptData.data) as unknown as TreeItem[];
      }

      function handleSelect(keys) {
        emit('select', keys[0]);
      }

      onMounted(() => {
        fetch();
      });
      return { treeData, handleSelect };
    },
  });
</script>
