/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

interface GroupItem {
  title: string;
  icon: string;
  color: string;
  desc: string;
  date: string;
  group: string;
}

interface NavItem {
  title: string;
  icon: string;
  color: string;
}

interface DynamicInfoItem {
  avatar: string;
  name: string;
  date: string;
  desc: string;
}

export const navItems: NavItem[] = [
  {
    title: '首页',
    icon: 'ion:home-outline',
    color: '#1fdaca',
  },
  {
    title: '仪表盘',
    icon: 'ion:grid-outline',
    color: '#bf0c2c',
  },
  {
    title: 'OA内部办公系统',
    icon: 'ion:layers-outline',
    color: '#e18525',
  },
  {
    title: '系统管理',
    icon: 'ion:settings-outline',
    color: '#3fb27f',
  },
  {
    title: '权限管理',
    icon: 'ion:key-outline',
    color: '#4daf1bc9',
  },
  {
    title: '报表查询',
    icon: 'ion:bar-chart-outline',
    color: '#00d8ff',
  },
];

export const dynamicInfoItems: DynamicInfoItem[] = [
  {
    avatar: 'dynamic-avatar-1|svg',
    name: '小李',
    date: '刚刚',
    desc: `在 <a>A仓库</a> 采购了100个零件 <a>H3C</a>`,
  },
  {
    avatar: 'dynamic-avatar-2|svg',
    name: '艾文',
    date: '1个小时前',
    desc: `关注了 <a>小李</a> `,
  },
  {
    avatar: 'dynamic-avatar-3|svg',
    name: '克里斯',
    date: '1天前',
    desc: `发布了 <a>本月采购物料表</a> `,
  },
  {
    avatar: 'dynamic-avatar-4|svg',
    name: '赵伟',
    date: '2天前',
    desc: `发表文章 <a>企业如何集成GPT微调模型配合ERP使用</a> `,
  },
  {
    avatar: 'dynamic-avatar-5|svg',
    name: '皮特',
    date: '3天前',
    desc: `回复了 <a>赵伟</a> 的问题 <a>如何让用户能直接微调模型？</a>`,
  },
  {
    avatar: 'dynamic-avatar-6|svg',
    name: '杰克',
    date: '1周前',
    desc: `添加了会员 <a>检测公司的会员</a> `,
  },
];

export const groupItems: GroupItem[] = [
  {
    title: 'WanSen ERP Core',
    icon: 'carbon:logo-github',
    color: '',
    desc: 'ERP系统的API',
    group: '万森智能开源组',
    date: '2023-09-28',
  },
  {
    title: 'WanSen ERP',
    icon: 'ion:logo-vue',
    color: '#3fb27f',
    desc: '该项目使用了Vue3+Vite+Ant-Design',
    group: '前端开发小组',
    date: '2023-09-28',
  },
  {
    title: 'Html 5',
    icon: 'ion:logo-html5',
    color: '#e18525',
    desc: '该项目也使用了HTML5',
    group: '前端开发小组',
    date: '2023-10-01',
  },
  {
    title: 'Angular',
    icon: 'ion:logo-angular',
    color: '#bf0c2c',
    desc: '设计新的页面交互',
    group: 'UI组',
    date: '2023-10-01',
  },
  {
    title: 'React',
    icon: 'bx:bxl-react',
    color: '#00d8ff',
    desc: '下一步计划支持React重构',
    group: '前端开发小组',
    date: '2023-10-02',
  },
  {
    title: 'JavaScript',
    icon: 'ion:logo-javascript',
    color: '#EBD94E',
    desc: '我们也可使用javascript进行重写',
    group: '前端开发小组',
    date: '2023-10-03',
  },
];
