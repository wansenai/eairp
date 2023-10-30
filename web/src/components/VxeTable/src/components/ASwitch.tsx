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

import { h } from 'vue';
import XEUtils from 'xe-utils';
import {
  createEditRender,
  createDefaultRender,
  createProps,
  createEvents,
  createDefaultFilterRender,
  createFormItemRender,
  getComponent,
} from './common';

export default {
  renderDefault: createDefaultRender(),
  renderEdit: createEditRender(),
  renderFilter(renderOpts, params) {
    const { column } = params;
    const { name, attrs } = renderOpts;
    const Component = getComponent(name);

    return [
      h(
        'div',
        {
          class: 'vxe-table--filter-antd-wrapper',
        },
        column.filters.map((option, oIndex) => {
          const optionValue = option.data;
          return h(Component, {
            key: oIndex,
            ...attrs,
            ...createProps(renderOpts, optionValue),
            ...createEvents(
              renderOpts,
              params,
              (value: any) => {
                // 处理 model 值双向绑定
                option.data = value;
              },
              () => {
                // 处理 change 事件相关逻辑
                const { $panel } = params;
                $panel.changeOption(null, XEUtils.isBoolean(option.data), option);
              },
            ),
          });
        }),
      ),
    ];
  },
  defaultFilterMethod: createDefaultFilterRender(),
  renderItemContent: createFormItemRender(),
};
