export interface GrowCardItem {
  icon: string;
  title: string;
  value: number;
  total: number;
  color: string;
  action: string;
}

export const growCardList: GrowCardItem[] = [
  {
    title: '今日销售',
    icon: 'visit-count|svg',
    value: 1750,
    total: 1750,
    color: 'green',
    action: '今日',
  },
  {
    title: '今日零售',
    icon: 'total-sales|svg',
    value: 800,
    total: 800,
    color: 'green',
    action: '今日',
  },
  {
    title: '今日采购',
    icon: 'download-count|svg',
    value: 378,
    total: 378,
    color: 'green',
    action: '今日',
  },
  {
    title: '昨日销售',
    icon: 'transaction|svg',
    value: 2500,
    total: 2500,
    color: 'blue',
    action: '昨日',
  },
];

export const growCardTwoList: GrowCardItem[] = [
  {
    title: '昨日零售',
    icon: 'visit-count|svg',
    value: 1300,
    total: 1300,
    color: 'blue',
    action: '昨日',
  },
  {
    title: '昨日采购',
    icon: 'total-sales|svg',
    value: 20,
    total: 26,
    color: 'blue',
    action: '昨日',
  },
  {
    title: '本月累计销售',
    icon: 'download-count|svg',
    value: 4278,
    total: 4200,
    color: 'orange',
    action: '本月',
  },
  {
    title: '本月累计零售',
    icon: 'transaction|svg',
    value: 3500,
    total: 3500,
    color: 'orange',
    action: '本月',
  },
];

export const growCardThreeList: GrowCardItem[] = [
  {
    title: '本月累计采购',
    icon: 'visit-count|svg',
    value: 1050,
    total: 1050,
    color: 'orange',
    action: '昨日',
  },
  {
    title: '今年累计销售',
    icon: 'transaction|svg',
    value: 57168,
    total: 57168,
    color: 'purple',
    action: '今年',
  },
  {
    title: '今年累计零售',
    icon: 'transaction|svg',
    value: 30821,
    total: 30821,
    color: 'purple',
    action: '今年',
  },
  {
    title: '今年累计采购',
    icon: 'transaction|svg',
    value: 8765,
    total: 8765,
    color: 'purple',
    action: '今年',
  },
];
