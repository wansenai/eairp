<template>
  <Card :title="t('home.retailStatistics')" :loading="loading">
    <div ref="chartRef" :style="{ width, height }"></div>
  </Card>
</template>
<script lang="ts" setup>
  import { Ref, ref, watch } from 'vue';
  import { Card } from 'ant-design-vue';
  import { useECharts } from '/@/hooks/web/useECharts';
  import { retailAxisStatisticalData } from '../data';
  import {useI18n} from "@/hooks/web/useI18n";

  const props = defineProps({
    loading: Boolean,
    width: {
      type: String as PropType<string>,
      default: '100%',
    },
    height: {
      type: String as PropType<string>,
      default: '300px',
    },
  });
  const chartRef = ref<HTMLDivElement | null>(null);
  const { setOptions } = useECharts(chartRef as Ref<HTMLDivElement>);
  const { t } = useI18n();

  watch(
    () => props.loading,
    () => {
      if (props.loading) {
        return;
      }
      setOptions({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            lineStyle: {
              width: 1,
              color: '#5ab1ef',
            },
          },
        },
        grid: { left: '1%', right: '1%', top: '2%', bottom: 0, containLabel: true },
        xAxis: {
          type: 'category',
          // 这里将返回的月份数据进行排序从1到12 月数据格式是
          data: retailAxisStatisticalData.value.map((item) => item.xaxisData),
        },
        yAxis: {
          type: 'value',
          splitNumber: 4,
        },
        series: [
          {
            data: retailAxisStatisticalData.value.map((item) => item.yAxisData),
            type: 'bar',
            barMaxWidth: 80,
          },
        ],
      });
    },
    { immediate: true },
  );
</script>
