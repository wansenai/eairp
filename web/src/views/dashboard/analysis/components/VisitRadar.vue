<template>
  <Card title="销售统计" :loading="loading">
    <div ref="chartRef" :style="{ width, height }"></div>
  </Card>
</template>
<script lang="ts" setup>
  import { Ref, ref, watch } from 'vue';
  import { Card } from 'ant-design-vue';
  import { useECharts } from '/@/hooks/web/useECharts';
  import { saleAxisStatisticalData } from '../data';

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
              color: '#b6a2de',
            },
          },
        },
        grid: { left: '1%', right: '1%', top: '2  %', bottom: 0, containLabel: true },
        xAxis: {
          type: 'category',
          data: saleAxisStatisticalData.value.map((item) => item.xaxisData),
        },
        yAxis: {
          type: 'value',
          splitNumber: 4,
        },
        series: [
          {
            data: saleAxisStatisticalData.value.map((item) => item.yAxisData),
            type: 'bar',
            barMaxWidth: 80,
          },
        ],
      });
    },
    { immediate: true },
  );
</script>
