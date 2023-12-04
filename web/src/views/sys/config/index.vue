<template>
  <PageWrapper
      title="系统配置"
      contentBackground
      content="此页面功能主要对当前系统进行一些配置。"
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

export default defineComponent({
  name: 'SystemConfig',
  components: { BasicForm, PageWrapper},
  setup() {
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
        createMessage.success('修改成功');
      } catch (error) {
        createMessage.error('修改失败');
      }
    }

    return {
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
