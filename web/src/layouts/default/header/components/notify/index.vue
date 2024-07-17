<template>
  <div :class="prefixCls">
    <Popover title="" trigger="click" :overlayClassName="`${prefixCls}__overlay`">
      <Badge :count="count" dot :numberStyle="numberStyle">
        <BellOutlined />
      </Badge>
      <template #content>
        <Tabs>
          <template v-for="item in listData" :key="item.key">
            <Tabs.TabPane>
              <template #tab>
                {{ item.name }}
                <span v-if="item.list.length !== 0">({{ item.list.length }})</span>
              </template>
              <!-- 绑定title-click事件的通知列表中标题是“可点击”的-->
              <NoticeList :list="item.list" v-if="item.key === '1'" @title-click="onNoticeClick" />
              <NoticeList :list="item.list" v-else />
            </Tabs.TabPane>
          </template>
        </Tabs>
      </template>
    </Popover>
  </div>
</template>
<script lang="ts" setup>
import {computed, ref, watch} from 'vue';
import { Popover, Tabs, Badge } from 'ant-design-vue';
import { BellOutlined } from '@ant-design/icons-vue';
import { ListItem } from './data';
import NoticeList from './NoticeList.vue';
import { useDesign } from '@/hooks/web/useDesign';
import { useMessage } from '@/hooks/web/useMessage';
import { getMessageList, readMessage } from "@/api/sys/message";
import {ReadMessageReq} from "@/api/sys/model/MessageModel";
import {useUserStore} from "@/store/modules/user";

const { prefixCls } = useDesign('header-notify');
const { createMessage } = useMessage();
const listData = ref([]);
const messageCount = ref(0);
const numberStyle = {};

const fetchMessages = () => {
  getMessageList().then((res) => {
    listData.value = res.data;
    console.info(listData.value);
  });
};

// Watch for changes in listData and update messageCount
watch(listData, (newList) => {
  messageCount.value = newList.length;
});

// Fetch messages initially
fetchMessages();

// Computed property for the message count
const count = computed(() => {
  return messageCount.value;
});


function onNoticeClick(record: ListItem) {
  record.titleDelete = !record.titleDelete;
  const userStore = useUserStore();
  const readMessageReq: ReadMessageReq = {
    id: record.id,
    userId: userStore.getUserInfo?.id,
    status: 1
  }
  readMessage(readMessageReq);
}
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-header-notify';

.@{prefix-cls} {
  padding-bottom: 1px;

  &__overlay {
    max-width: 720px;
  }

  .ant-tabs-content {
    width: 600px;
  }

  .ant-badge {
    display: flex;
    align-items: center;
    font-size: 18px;

    .ant-badge-multiple-words {
      padding: 0 4px;
    }

    svg {
      width: 0.9em;
    }
  }
}
</style>