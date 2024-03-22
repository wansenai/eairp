import {ref} from "vue";
import {useI18n} from "@/hooks/web/useI18n";

export const { t } = useI18n();

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
    title: t('home.todayRetail'),
    dataIndex: 'todayRetailSales',
    icon: 'visit-count|svg',
    value: 0,
    total: 0,
    color: 'green',
    action: t('home.today'),
  },
  {
    title: t('home.todaySales'),
    dataIndex: 'todaySales',
    icon: 'visit-count|svg',
    value: 0,
    total: 0,
    color: 'green',
    action: t('home.today'),
  },
  {
    title: t('home.todayPurchase'),
    dataIndex: 'todayPurchase',
    icon: 'visit-count|svg',
    value: 0,
    total: 0,
    color: 'green',
    action: t('home.today'),
  },
];

export const growCardTwoList: GrowCardItem[] = [
  {
    title: t('home.yesterdayRetail'),
    dataIndex: 'yesterdayRetailSales',
    icon: 'visit-count|svg',
    value: 0,
    total: 0,
    color: 'blue',
    action: t('home.yesterday'),
  },
  {
    title: t('home.yesterdaySales'),
    dataIndex: 'yesterdaySales',
    icon: 'total-sales|svg',
    value: 0,
    total: 0,
    color: 'blue',
    action: t('home.yesterday'),
  },
  {
    title: t('home.yesterdayPurchase'),
    dataIndex: 'yesterdayPurchase',
    icon: 'download-count|svg',
    value: 0,
    total: 0,
    color: 'blue',
    action: t('home.yesterday'),
  },
];

export const growCardThreeList: GrowCardItem[] = [
  {
    title: t('home.thisMonthRetail'),
    dataIndex: 'monthRetailSales',
    icon: 'total-sales|svg',
    value: 0,
    total: 0,
    color: 'orange',
    action: t('home.thisMonth'),
  },
  {
    title: t('home.thisMonthSales'),
    dataIndex: 'monthSales',
    icon: 'total-sales|svg',
    value: 0,
    total: 0,
    color: 'orange',
    action: t('home.thisMonth'),
  },
  {
    title: t('home.thisMonthPurchase'),
    dataIndex: 'monthPurchase',
    icon: 'total-sales|svg',
    value: 0,
    total: 0,
    color: 'orange',
    action: t('home.thisMonth'),
  },
];

export const growCardFourList: GrowCardItem[] = [
  {
    title: t('home.thisYearRetail'),
    dataIndex: 'yearRetailSales',
    icon: 'transaction|svg',
    value: 0,
    total: 0,
    color: 'purple',
    action: t('home.thisYear'),
  },
  {
    title: t('home.thisYearSales'),
    dataIndex: 'yearSales',
    icon: 'transaction|svg',
    value: 0,
    total: 0,
    color: 'purple',
    action: t('home.thisYear'),
  },
  {
    title: t('home.thisYearPurchase'),
    dataIndex: 'yearPurchase',
    icon: 'transaction|svg',
    value: 0,
    total: 0,
    color: 'purple',
    action: t('home.thisYear'),
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

