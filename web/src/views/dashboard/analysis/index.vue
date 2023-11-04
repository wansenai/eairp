<template>
  <div class="p-4">
    <GrowCard  :loading="loading" class="md:w-4/3 w-full" />
    <GrowCardTwo :loading="loading" class="md:w-4/3 w-full" />
    <GrowCardThree :loading="loading" class="md:w-4/3 w-full" />
    <GrowCardFour :loading="loading" class="md:w-4/3 w-full" />
    <SiteAnalysis class="!my-4 enter-y" :loading="loading" />
    <div class="md:flex enter-y">
      <VisitRadar class="md:w-1/3 w-full" :loading="loading" />
      <VisitSource class="md:w-1/3 !md:mx-4 !md:my-0 !my-4 w-full" :loading="loading" />
      <SalesProductPie class="md:w-1/3 w-full" :loading="loading" />
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { ref, onMounted } from 'vue';
  import GrowCard from './components/GrowCard.vue';
  import GrowCardTwo from './components/GrowCardTwo.vue';
  import GrowCardThree from './components/GrowCardThree.vue';
  import GrowCardFour from './components/GrowCardFour.vue';
  import SiteAnalysis from './components/SiteAnalysis.vue';
  import VisitSource from './components/VisitSource.vue';
  import VisitRadar from './components/VisitRadar.vue';
  import SalesProductPie from './components/SalesProductPie.vue';
  import {getStatistical} from '/@/api/report/report';
  import {RetailStatisticalResp} from '/@/api/report/reportModel';
  import {growCardList, growCardThreeList, growCardTwoList, growCardFourList} from "@/views/dashboard/analysis/data";
  const loading = ref(true);

  onMounted(async () => {
    const timer = setTimeout(() => {
      loading.value = false;
    }, 1200);

    try {
      const res = await getStatistical();
      const data: RetailStatisticalResp = res.data;
      growCardList.forEach((item, index) => {
        index === 0 && (item.value = data.todayRetailSales) && (item.total = data.todayRetailSales);
        index === 1 && (item.value = data.todaySales) && (item.total = data.todaySales);
        index === 2 && (item.value = data.todayPurchase)  && (item.total = data.todayPurchase);
      });

      growCardTwoList.forEach((item, index) => {
        index === 0 && (item.value = data.yesterdayRetailSales) && (item.total = data.yesterdayRetailSales);
        index === 1 && (item.value = data.yesterdaySales) && (item.total = data.yesterdaySales);
        index === 2 && (item.value = data.yesterdayPurchase) && (item.total = data.yesterdayPurchase);
      });

      growCardThreeList.forEach((item, index) => {
        index === 0 && (item.value = data.monthRetailSales) && (item.total = data.monthRetailSales);
        index === 1 && (item.value = data.monthSales) && (item.total = data.monthSales);
        index === 2 && (item.value = data.monthPurchase) && (item.total = data.monthPurchase);
      });

      growCardFourList.forEach((item, index) => {
        index === 0 && (item.value = data.yearRetailSales) && (item.total = data.yearRetailSales);
        index === 1 && (item.value = data.yearSales) && (item.total = data.yearSales);
        index === 2 && (item.value = data.yearPurchase) && (item.total = data.yearPurchase);
      });

      clearTimeout(timer);
      loading.value = false;
    } catch (error) {
      console.error('Failed to fetch data:', error);
      clearTimeout(timer);
      loading.value = false;
    }
  });
</script>
