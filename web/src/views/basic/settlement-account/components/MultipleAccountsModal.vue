<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <a-form ref="formRef">
      <a-row class="form-row" :gutter="24">
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-item :label="t('basic.settlement.accountOne')" :label-col="labelCol" :wrapper-col="wrapperCol"
                       :rules="[{ required: true, message: t('basic.settlement.inputAccount') }]">
            <a-select v-model:value="multipleAccounts.accountOne"
                      :placeholder="t('basic.settlement.inputAccount')"
                      :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"
                      @change=""/>
          </a-form-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-item :label="t('basic.settlement.amountOne')" :label-col="labelCol" :wrapper-col="wrapperCol"
                       :rules="[{ required: true, message: t('basic.settlement.inputAmount') }]">
            <a-input-number v-model:value="multipleAccounts.accountPriceOne" :placeholder="t('basic.settlement.inputAmount')" min="0" max="9999999999.99"
                            step="0.01"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row class="form-row" :gutter="24">
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-item :label="t('basic.settlement.accountTwo')" :label-col="labelCol" :wrapper-col="wrapperCol"
                       :rules="[{ required: true, message: t('basic.settlement.inputAccount') }]">
            <a-select v-model:value="multipleAccounts.accountTwo"
                      :placeholder="t('basic.settlement.inputAccount')"
                      :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"
                      @change=""/>
          </a-form-item>
        </a-col>
        <a-col :lg="12" :md="12" :sm="24">
          <a-form-item :label="t('basic.settlement.amountTwo')" :label-col="labelCol" :wrapper-col="wrapperCol"
                       :rules="[{ required: true, message: t('basic.settlement.inputAmount') }]">
            <a-input-number v-model:value="multipleAccounts.accountPriceTwo" :placeholder="t('basic.settlement.inputAmount')" min="0" max="9999999999.99"
                            step="0.01"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row class="form-row" :gutter="24">
        <a-col :lg="12" :md="24" :sm="24">
          <a-form-item :label="t('basic.settlement.accountThree')" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-select v-model:value="multipleAccounts.accountThree"
                      :placeholder="t('basic.settlement.inputAccount')"
                      :options="accountList.map(item => ({ value: item.id, label: item.accountName }))"
                      @change=""/>
          </a-form-item>
        </a-col>
        <a-col :lg="12" :md="24" :sm="24">
          <a-form-item :label="t('basic.settlement.amountThree')" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-input-number v-model:value="multipleAccounts.accountPriceThree" :placeholder="t('basic.settlement.inputAmount')" min="0" max="9999999999.99"
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
import {useI18n} from "vue-i18n";

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
    const { t } = useI18n();
    const getTitle = ref(t('basic.settlement.title'));
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
        createMessage.warn(t('basic.settlement.notice'))
        return;
      }
      setModalProps({confirmLoading: false,});
      emit('handleAccountSuccess', multipleAccounts.value);
      closeModal();
    }

    return {
      t,
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