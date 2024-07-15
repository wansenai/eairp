export interface ListItem {
  id: string;
  avatar: string;
  // 通知的标题内容
  title: string;
  // 是否在标题上显示删除线
  titleDelete?: boolean;
  datetime: string;
  type: string;
  read?: boolean;
  description: string;
  clickClose?: boolean;
  extra?: string;
  color?: string;
}

export interface TabItem {
  key: string;
  name: string;
  list: ListItem[];
  unreadlist?: ListItem[];
}

export const tabListData: TabItem[] = [
  {
    key: '1',
    name: '通知',
    list: [
      {
        id: '000000001',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png',
        title: '物品XXXXX的采购订单已完成',
        description: '采购订单单据:79165111336',
        datetime: '2024-07-04',
        type: '1',
      },
      {
        id: '000000002',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png',
        title: '你添加的商品已经通过审核',
        description: '',
        datetime: '2024-07-01',
        type: '1',
      },
      // {
      //   id: '000000003',
      //   avatar: 'https://gw.alipayobjects.com/zos/rmsportal/kISTdvpyTAhtGxpovNWd.png',
      //   title: '这种模板可以区分多种通知类型',
      //   description: '',
      //   datetime: '2017-08-07',
      //   // read: true,
      //   type: '1',
      // },
      // {
      //   id: '000000004',
      //   avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
      //   title: '左侧图标用于区分不同的类型',
      //   description: '',
      //   datetime: '2017-08-07',
      //   type: '1',
      // },
      // {
      //   id: '000000005',
      //   avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
      //   title:
      //     '标题可以设置自动显示省略号，本例中标题行数已设为1行，如果内容超过1行将自动截断并支持tooltip显示完整标题。',
      //   description: '',
      //   datetime: '2017-08-07',
      //   type: '1',
      // },
      // {
      //   id: '000000006',
      //   avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
      //   title: '左侧图标用于区分不同的类型',
      //   description: '',
      //   datetime: '2017-08-07',
      //   type: '1',
      // },
      // {
      //   id: '000000007',
      //   avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
      //   title: '左侧图标用于区分不同的类型',
      //   description: '',
      //   datetime: '2017-08-07',
      //   type: '1',
      // },
      // {
      //   id: '000000008',
      //   avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
      //   title: '左侧图标用于区分不同的类型',
      //   description: '',
      //   datetime: '2017-08-07',
      //   type: '1',
      // },
      // {
      //   id: '000000009',
      //   avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
      //   title: '左侧图标用于区分不同的类型',
      //   description: '',
      //   datetime: '2017-08-07',
      //   type: '1',
      // },
      // {
      //   id: '000000010',
      //   avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
      //   title: '左侧图标用于区分不同的类型',
      //   description: '',
      //   datetime: '2017-08-07',
      //   type: '1',
      // },
    ],
  },
  {
    key: '2',
    name: '消息',
    list: [
    ],
  },
  {
    key: '3',
    name: '待办',
    list: [
      {
        id: '000000009',
        avatar: '',
        title: '采购订单',
        description: '任务需要部门经理xxxx审核',
        datetime: '',
        extra: '未审核',
        color: '',
        type: '3',
      },
      {
        id: '000000010',
        avatar: '',
        title: '销售出库',
        description: '您需要对单据XXXXX进行审核处理并核对出库信息',
        datetime: '',
        extra: '马上到期',
        color: 'red',
        type: '3',
      },
      // {
      //   id: '000000011',
      //   avatar: '',
      //   title: '零售订单',
      //   description: '指派竹尔于 2017-01-09 前完成更新并发布',
      //   datetime: '',
      //   extra: '已耗时 8 天',
      //   color: 'gold',
      //   type: '3',
      // },
      {
        id: '000000012',
        avatar: '',
        title: '采购入库',
        description: 'XXXXXXX指派竹尔于 2024-07-09 前完成XXX商品入库',
        datetime: '',
        extra: '进行中',
        color: 'blue',
        type: '3',
      },
    ],
  },
];
