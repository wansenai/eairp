<template>
  <PageWrapper
      :title="t('system.configure.title')"
      contentBackground
      :content="t('system.configure.tip')"
      contentClass="p-4"
  >
    <BasicForm @register="register" />
  </PageWrapper>
</template>
<script lang="ts">
import { BasicForm, useForm} from '/@/components/Form';
import { defineComponent } from 'vue';
import { schemas } from './config.data';
import { PageWrapper } from '/@/components/Page';
import {addOrUpdateConfigInfo, getConfigInfo} from '@/api/sys/config'
import {useMessage} from "@/hooks/web/useMessage";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'SystemConfig',
  components: { BasicForm, PageWrapper},
  setup() {
    const { t } = useI18n();
    const {createMessage} = useMessage()
    const [register, { validate, setProps, setFieldsValue }] = useForm({
      labelCol: {
        span: 8,
      },
      wrapperCol: {
        span: 15,
      },
      schemas: schemas,
      actionColOptions: {
        offset: 8,
        span: 23,
      },
      submitButtonOptions: {
        text: '提交',
      },
      showResetButton: false,
      submitFunc: customSubmitFunc,
    });



    // 获取配置信息 然后修改表单的值
    getConfigInfo().then(res => {
        const data = res.data
        console.info(data)
        setFieldsValue(data)
    })


    async function customSubmitFunc() {
      try {
        const data = await validate();
        await addOrUpdateConfigInfo(data);
        setProps({
          submitButtonOptions: {
            loading: true,
          },
        });
        setTimeout(() => {
          setProps({
            submitButtonOptions: {
              loading: false,
            },
          });
        }, 500);
        createMessage.success(t('system.configure.updateSuccess'));
      } catch (error) {
        createMessage.error(t('system.configure.updateFailed'));
      }
    }

    return {
      t,
      register
    };
  },
});
</script>
<style lang="less" scoped>
.form-wrap {
  padding: 24px;
  background-color: @component-background;
}
</style>
