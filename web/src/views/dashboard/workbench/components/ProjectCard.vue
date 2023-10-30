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
  <Card title="项目" v-bind="$attrs">
    <template #extra>
      <a-button type="link" size="small">更多</a-button>
    </template>

    <CardGrid v-for="item in items" :key="item.title" class="!md:w-1/3 !w-full">
      <span class="flex">
        <Icon :icon="item.icon" :color="item.color" size="30" />
        <span class="text-lg ml-4">{{ item.title }}</span>
      </span>
      <div class="flex mt-2 h-10 text-secondary">{{ item.desc }}</div>
      <div class="flex justify-between text-secondary">
        <span>{{ item.group }}</span>
        <span>{{ item.date }}</span>
      </div>
    </CardGrid>
  </Card>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { Card, CardGrid } from 'ant-design-vue';
  import Icon from '@/components/Icon/Icon.vue';
  import { groupItems } from './data';

  export default defineComponent({
    components: { Card, CardGrid, Icon },
    setup() {
      return { items: groupItems };
    },
  });
</script>
