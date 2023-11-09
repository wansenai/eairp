<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <a-form ref="formRef">
      <a-row class="form-row" :gutter="24">
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-item label="结算账户一" :label-col="labelCol" :wrapper-col="wrapperCol"
                       :rules="[{ required: true, message: '请选择结算账户' }]">
            <a-select v-model:value="multipleAccounts.accountOne"
                      placeholder="请选择结算账户"
                      :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"
                      @change=""/>
          </a-form-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-item label="结算金额一" :label-col="labelCol" :wrapper-col="wrapperCol"
                       :rules="[{ required: true, message: '请选择金额' }]">
            <a-input-number v-model:value="multipleAccounts.accountPriceOne" placeholder="请输入结算金额" min="0" max="9999999999.99"
                            step="0.01"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row class="form-row" :gutter="24">
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-item label="结算账户二" :label-col="labelCol" :wrapper-col="wrapperCol"
                       :rules="[{ required: true, message: '请选择结算账户' }]">
            <a-select v-model:value="multipleAccounts.accountTwo"
                      placeholder="请选择结算账户"
                      :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"
                      @change=""/>
          </a-form-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-item label="结算金额二" :label-col="labelCol" :wrapper-col="wrapperCol"
                       :rules="[{ required: true, message: '请选择金额' }]">
            <a-input-number v-model:value="multipleAccounts.accountPriceTwo" placeholder="请输入结算金额" min="0" max="9999999999.99"
                            step="0.01"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row class="form-row" :gutter="24">
        <a-col :lg="12" :md="24" :sm="24">
          <a-form-item label="结算账户三" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-select v-model:value="multipleAccounts.accountThree"
                      placeholder="请选择结算账户"
                      :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"
                      @change=""/>
          </a-form-item>
        </a-col>
        <a-col :lg="12" :md="24" :sm="24">
          <a-form-item label="结算金额三" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-input-number v-model:value="multipleAccounts.accountPriceThree" placeholder="请输入结算金额" min="0" max="9999999999.99"
                            step="0.01"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {getAccountList} from "@/api/financial/account";
import {Spin, Row, Col, Form, FormItem, InputNumber, Select, SelectOption} from "ant-design-vue";
import {AccountResp} from "@/api/financial/model/accountModel";
import {useMessage} from "@/hooks/web/useMessage";

export default defineComponent({
  name: 'MultipleAccountsModal',
  components: {
    BasicModal,
    BasicForm,
    'a-select': Select,
    'a-select-option': SelectOption,
    'a-form': Form,
    'a-form-item': FormItem,
    'a-input-number': InputNumber,
    'a-col': Col,
    'a-row': Row,
    'a-spin': Spin,
  },
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const getTitle = ref('多账户结算');
    const { createMessage } = useMessage();
    const accountList = ref<AccountResp[]>([]);
    const labelCol = ref({
      xs: {span: 12},
      sm: {span: 8},
    });
    const wrapperCol = ref({
      xs: {span: 24},
      sm: {span: 16},
    });
    const multipleAccounts = ref({
      accountOne: '',
      accountPriceOne: '',
      accountTwo: '',
      accountPriceTwo: '',
      accountThree: '',
      accountPriceThree: '',
    });

    const [registerForm,] = useForm({
      labelWidth: 100,
      baseColProps: {span: 24},
      showActionButtonGroup: false,
      actionColOptions: {
        span: 23,
      },
    });

    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({
        confirmLoading: false,
        destroyOnClose: true,
        width: 600,
      });

      getAccountList().then((res) => {
        accountList.value = res.data;
      });
      if (data.multipleAccounts) {
        multipleAccounts.value = data.multipleAccounts
      }
    });

    async function handleSubmit() {
      // 判断是否选择了结算账户
      if (!multipleAccounts.value.accountOne || !multipleAccounts.value.accountTwo
          || !multipleAccounts.value.accountPriceOne || !multipleAccounts.value.accountPriceTwo) {
        createMessage.warn('请选择结算账户和金额，至少两个账户')
        return;
      }
      setModalProps({confirmLoading: false,});
      emit('handleAccountSuccess', multipleAccounts.value);
      closeModal();
    }

    return {
      registerModal,
      registerForm,
      handleSubmit,
      getTitle,
      accountList,
      labelCol,
      wrapperCol,
      multipleAccounts,
    }
  },
})
</script>