<template>
  <CollapseContainer title="安全设置" :canExpan="false">
    <List>
      <template v-for="item in list" :key="item.key">
        <ListItem>
          <ListItemMeta>
            <template #title>
              {{ item.title }}
              <div class="extra" v-if="item.extra" @click="bindSetting(item.title)">
                {{ item.extra }}
              </div>
            </template>
            <template #description>
              <div>{{ item.description }}</div>
            </template>
          </ListItemMeta>
        </ListItem>
      </template>
    </List>
  </CollapseContainer>
  <BindPhoneModal @register="bindPhoneModal" @handleBindPhoneSuccess="handleBindPhoneSuccess"/>
  <ResetPasswordModal @register="resetPasswordModal"/>
</template>
<script lang="ts">
  import { List } from 'ant-design-vue';
  import { defineComponent } from 'vue';
  import { CollapseContainer } from '@/components/Container';

  import { secureSettingList } from './data';
  import {useUserStore} from "@/store/modules/user";
  import BindPhoneModal from "@/views/basic/account/BindPhoneModal.vue";
  import ResetPasswordModal from "@/views/basic/account/ResetPasswordModal.vue";
  import {useModal} from "@/components/Modal";

  export default defineComponent({
    components: {BindPhoneModal, ResetPasswordModal, CollapseContainer, List, ListItem: List.Item, ListItemMeta: List.Item.Meta },
    setup() {
      const userStore = useUserStore();
      const userInfo: any = userStore.getUserInfo;
      const [bindPhoneModal, {openModal: openBindPhoneModal}] = useModal();
      const [resetPasswordModal, {openModal: openResetPasswordModal}] = useModal();
      userInfo.phoneNumber ? secureSettingList[1].description = '已绑定手机：' + userInfo.phoneNumber : secureSettingList[0].description = '未绑定';
      userInfo.email ? secureSettingList[2].description = '已绑定邮箱：' + userInfo.email : secureSettingList[1].description = '未绑定';

      function bindSetting(type: string) {
        if (type === '密保手机') {
          openBindPhoneModal(true, {
            phoneNumber: userInfo.phoneNumber
          })
        } else if (type === '账户密码') {
          openResetPasswordModal(true, {
            id: userInfo.id,
            userName: userInfo.userName
          })
        }
      }

      function handleBindPhoneSuccess() {

      }

      return {
        list: secureSettingList,
        bindSetting,
        bindPhoneModal,
        handleBindPhoneSuccess,
        resetPasswordModal
      };
    },
  });
</script>
<style lang="less" scoped>
  .extra {
    margin-top: 10px;
    margin-right: 30px;
    float: right;
    color: #1890ff;
    font-weight: normal;
    cursor: pointer;
  }
</style>
