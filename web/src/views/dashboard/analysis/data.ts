import {ref} from "vue";

export interface GrowCardItem {
  icon: string;
  dataIndex: string;
  title: string;
  value: number;
  total: number;
  color: string;
  action: string;
}

export const growCardList: GrowCardItem[] = [
  {
    title: '今日零售',
    dataIndex: 'todayRetailSales',
    icon: 'visit-count|svg',
    value: 0,
    total: 0,
    color: 'green',
    action: '今日',
  },
  {
    title: '今日销售',
    dataIndex: 'todaySales',
    icon: 'visit-count|svg',
    value: 0,
    total: 0,
    color: 'green',
    action: '今日',
  },
  {
    title: '今日采购',
    dataIndex: 'todayPurchase',
    icon: 'visit-count|svg',
    value: 0,
    total: 0,
    color: 'green',
    action: '今日',
  },
];

export const growCardTwoList: GrowCardItem[] = [
  {
    title: '昨日零售',
    dataIndex: 'yesterdayRetailSales',
    icon: 'visit-count|svg',
    value: 0,
    total: 0,
    color: 'blue',
    action: '昨日',
  },
  {
    title: '昨日销售',
    dataIndex: 'yesterdaySales',
    icon: 'total-sales|svg',
    value: 0,
    total: 0,
    color: 'blue',
    action: '昨日',
  },
  {
    title: '昨日采购',
    dataIndex: 'yesterdayPurchase',
    icon: 'download-count|svg',
    value: 0,
    total: 0,
    color: 'blue',
    action: '昨日',
  },
];

export const growCardThreeList: GrowCardItem[] = [
  {
    title: '本月累计零售',
    dataIndex: 'monthRetailSales',
    icon: 'total-sales|svg',
    value: 0,
    total: 0,
    color: 'orange',
    action: '本月',
  },
  {
    title: '本月累计销售',
    dataIndex: 'monthSales',
    icon: 'total-sales|svg',
    value: 0,
    total: 0,
    color: 'orange',
    action: '本月',
  },
  {
    title: '本月累计采购',
    dataIndex: 'monthPurchase',
    icon: 'total-sales|svg',
    value: 0,
    total: 0,
    color: 'orange',
    action: '本月',
  },
];

export const growCardFourList: GrowCardItem[] = [
  {
    title: '今年累计零售',
    dataIndex: 'yearRetailSales',
    icon: 'transaction|svg',
    value: 0,
    total: 0,
    color: 'purple',
    action: '今年',
  },
  {
    title: '今年累计销售',
    dataIndex: 'yearSales',
    icon: 'transaction|svg',
    value: 0,
    total: 0,
    color: 'purple',
    action: '今年',
  },
  {
    title: '今年累计采购',
    dataIndex: 'yearPurchase',
    icon: 'transaction|svg',
    value: 0,
    total: 0,
    color: 'purple',
    action: '今年',
  },
];

export interface XyAxisData {
  xaxisData: string;
  yaxisData: number;
}
// 定义图表数据类型 用于图表数据的展示
export const retailAxisStatisticalData: XyAxisData[] = ref();
export const saleAxisStatisticalData: XyAxisData[] = ref();
export const purchaseAxisStatisticalData: XyAxisData[] = ref();

