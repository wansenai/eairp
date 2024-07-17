<template>
  <CollapseContainer :title="t('basic.account.basicSetting')" :canExpan="false">
    <a-row :gutter="24">
      <a-col :span="14">
        <BasicForm @register="register" />
      </a-col>
      <a-col :span="10">
        <div class="change-avatar">
          <div class="mb-2">{{ t('basic.account.avatarTip') }}</div>
          <CropperAvatar
            :uploadApi="uploadApi"
            :value="avatar"
            :btnText="t('basic.account.avatar')"
            :btnProps="{ preIcon: 'ant-design:cloud-upload-outlined' }"
            @change="updateAvatar"
            width="150"
          />
        </div>
      </a-col>
    </a-row>
    <Button type="primary" @click="handleSubmit"> {{ t('basic.account.updateInfo') }} </Button>
  </CollapseContainer>
</template>
<script lang="ts">
  import { Button, Row, Col } from 'ant-design-vue';
  import { computed, defineComponent, onMounted } from 'vue';
  import { BasicForm, useForm } from '@/components/Form/index';
  import { CollapseContainer } from '@/components/Container';
  import { CropperAvatar } from '@/components/Cropper';
  import { useMessage } from '@/hooks/web/useMessage';
  import headerImg from '/@/assets/images/header.jpg';
  import {baseSetSchemas} from './data';
  import { useUserStore } from '@/store/modules/user';
  import {getUserInfo, UpdateAvatar, updateUser} from '@/api/sys/user'
  import {useI18n} from "vue-i18n";
  export default defineComponent({
    components: {
      BasicForm,
      CollapseContainer,
      Button,
      ARow: Row,
      ACol: Col,
      CropperAvatar,
    },
    setup() {
      const { t } = useI18n();
      const { createMessage } = useMessage();
      const userStore = useUserStore();

      const [register, { setFieldsValue, validate }] = useForm({
        labelWidth: 120,
        schemas: baseSetSchemas,
        showActionButtonGroup: false,
      });

      onMounted(async () => {
        // 设置表单数据
        const res = await getUserInfo();
        setFieldsValue(res.data);
        // 更新secureSettingList数组里的description字段值
      });

      const avatar = computed(() => {
        const { avatar } = userStore.getUserInfo;
        return avatar || headerImg;
      });

      function updateAvatar({ src, data }) {
        const userinfo = userStore.getUserInfo;
        userinfo.avatar = src;
        userStore.setUserInfo(userinfo);
      }

      async function handleSubmit() {
        const values = await validate();
        const result = await updateUser(values)
        if(result.code !== 'A0014') {
            createMessage.warn(t('basic.account.noticeOne'))
        }
      }

      return {
        t,
        avatar,
        register,
        uploadApi: UpdateAvatar as any,
        updateAvatar,
        handleSubmit,
      };
    },
  });
</script>

<style lang="less" scoped>
  .change-avatar {
    img {
      display: block;
      margin-bottom: 15px;
      border-radius: 50%;
    }
  }
</style>
