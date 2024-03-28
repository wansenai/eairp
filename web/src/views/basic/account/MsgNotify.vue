<template>
  <CollapseContainer :title="t('basic.account.notice.title')" :canExpan="false">
    <List>
        <ListItem>
          <ListItemMeta>
            <template #title>
              {{ t('basic.account.notice.systemInfo') }}
              <Switch
                class="extra"
                v-model:checked="checkedSystemInfo"
                :checked-children="t('basic.account.notice.on')"
                :un-checked-children="t('basic.account.notice.off')"
                onchange="switchChange"
              />
            </template>
            <template #description>
              <div>{{ t('basic.account.notice.systemInfoTip') }}</div>
            </template>
          </ListItemMeta>
        </ListItem>
        <ListItem>
          <ListItemMeta>
            <template #title>
              {{ t('basic.account.notice.todo') }}
              <Switch
                  class="extra"
                  v-model:checked="checkedTodoTask"
                  :checked-children="t('basic.account.notice.on')"
                  :un-checked-children="t('basic.account.notice.off')"
                  onchange="switchChange"
              />
            </template>
            <template #description>
              <div>{{ t('basic.account.notice.todoTip') }}</div>
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
  import {useI18n} from "@/hooks/web/useI18n";

  export default defineComponent({
    components: {
      CollapseContainer,
      List,
      ListItem: List.Item,
      ListItemMeta: List.Item.Meta,
      Switch,
    },
    setup() {
      const { t } = useI18n();
      const checkedSystemInfo = ref<boolean>(true);
      const checkedTodoTask = ref<boolean>(true);
      function switchChange() {
        console.info('switchChange');
      }

      return {
        t,
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
