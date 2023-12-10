<template>
  <CollapseContainer title="新消息通知" :canExpan="false">
    <List>
        <ListItem>
          <ListItemMeta>
            <template #title>
              系统消息
              <Switch
                class="extra"
                v-model:checked="checkedSystemInfo"
                checked-children="开"
                un-checked-children="关"
                onchange="switchChange"
              />
            </template>
            <template #description>
              <div>系统消息将以站内信的形式通知</div>
            </template>
          </ListItemMeta>
        </ListItem>
        <ListItem>
          <ListItemMeta>
            <template #title>
              待办任务
              <Switch
                  class="extra"
                  v-model:checked="checkedTodoTask"
                  checked-children="开"
                  un-checked-children="关"
                  onchange="switchChange"
              />
            </template>
            <template #description>
              <div>待办任务将以站内信的形式通知</div>
            </template>
          </ListItemMeta>
        </ListItem>
    </List>
  </CollapseContainer>
</template>
<script lang="ts">
  import { List, Switch } from 'ant-design-vue';
  import { defineComponent, ref } from 'vue';
  import { CollapseContainer } from '@/components/Container';

  export default defineComponent({
    components: {
      CollapseContainer,
      List,
      ListItem: List.Item,
      ListItemMeta: List.Item.Meta,
      Switch,
    },
    setup() {
      const checkedSystemInfo = ref<boolean>(true);
      const checkedTodoTask = ref<boolean>(true);
      function switchChange() {
        console.info('switchChange');
      }

      return {
        switchChange,
        checkedSystemInfo,
        checkedTodoTask
      };
    },
  });
</script>
<style lang="less" scoped>
  .extra {
    margin-top: 10px;
    margin-right: 30px;
    float: right;
  }
</style>
