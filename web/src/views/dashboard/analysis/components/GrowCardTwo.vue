<template>
  <div class="md:flex">
    <template v-for="(item, index) in growCardTwoList" :key="item.title">
      <Card
          size="small"
          :loading="loading"
          :title="item.title"
          class="md:w-1/4 w-full !md:mt-0"
          :class="{ '!md:mr-4': index + 1 < 4, '!mt-4': index > 0 }"
      >
        <template #extra>
          <Tag :color="item.color">{{ item.action }}</Tag>
        </template>

        <div class="py-4 px-4 flex justify-between items-center">
          <CountTo :prefix="amountSymbol" :startVal=0 :endVal="item.value" :decimals="2" class="text-2xl" />
          <Icon :icon="item.icon" :size="40" />
        </div>

        <div class="p-2 px-4 flex justify-between">
          <span>{{ item.title }}</span>
          <CountTo :prefix="amountSymbol" :startVal="1" :endVal="item.total" :decimals="2"/>
        </div>
      </Card>
    </template>
  </div>
</template>
<script lang="ts" setup>
import { CountTo } from '/@/components/CountTo/index';
import Icon from '@/components/Icon/Icon.vue';
import { Tag, Card } from 'ant-design-vue';
import { growCardTwoList } from '../data';
import {ref} from "vue";
import {useLocaleStore} from "@/store/modules/locale";

defineProps({
  loading: {
    type: Boolean,
  },
});

const amountSymbol = ref<string>('')
const localeStore = useLocaleStore().getLocale;
if(localeStore === 'zh_CN') {
  amountSymbol.value = '￥'
} else if (localeStore === 'en') {
  amountSymbol.value = '$'
}
</script>
