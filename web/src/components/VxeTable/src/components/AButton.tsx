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
import {
  FormItemContentRenderParams,
  FormItemRenderOptions,
  VxeGlobalRendererHandles,
} from 'vxe-table';
import XEUtils from 'xe-utils';
import { cellText, createEvents, createProps, getComponent } from './common';

const COMPONENT_NAME = 'AButton';

export function createEditRender() {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderEditOptions,
    params: VxeGlobalRendererHandles.RenderEditParams,
  ) {
    const { attrs } = renderOpts;
    const Component = getComponent(COMPONENT_NAME);

    return [
      h(Component, {
        ...attrs,
        ...createProps(renderOpts, null),
        ...createEvents(renderOpts, params),
      }),
    ];
  };
}

export function createDefaultRender() {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderEditOptions,
    params: VxeGlobalRendererHandles.RenderEditParams,
  ) {
    const { attrs } = renderOpts;
    const Component = getComponent(COMPONENT_NAME);

    return [
      h(
        Component,
        {
          ...attrs,
          ...createProps(renderOpts, null),
          ...createEvents(renderOpts, params),
        },
        cellText(renderOpts.content),
      ),
    ];
  };
}

export function createFormItemRender() {
  return function (renderOpts: FormItemRenderOptions, params: FormItemContentRenderParams) {
    const { attrs, content } = renderOpts;
    const { property, $form, data } = params;
    const props = createProps(renderOpts, null);
    const Component = getComponent(COMPONENT_NAME);

    return [
      h(
        Component,
        {
          ...attrs,
          ...props,
          ...createEvents(
            renderOpts,
            params,
            (value: any) => {
              // 处理 model 值双向绑定
              XEUtils.set(data, property, value);
            },
            () => {
              // 处理 change 事件相关逻辑
              $form.updateStatus({
                ...params,
                field: property,
              });
            },
          ),
        },
        {
          default: () => cellText(content || props.content),
        },
      ),
    ];
  };
}

function createToolbarButtonRender() {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderToolOptions,
    params: VxeGlobalRendererHandles.RenderButtonParams,
  ) {
    const { attrs } = renderOpts;
    const { button } = params;
    const props = createProps(renderOpts, null);
    const Component = getComponent(COMPONENT_NAME);

    return [
      h(
        Component,
        {
          ...attrs,
          ...props,
          ...createEvents(renderOpts, params),
        },
        {
          default: () => cellText(button?.content || props.content),
        },
      ),
    ];
  };
}

export default {
  renderEdit: createEditRender(),
  renderDefault: createDefaultRender(),
  renderItemContent: createFormItemRender(),
  renderToolbarButton: createToolbarButtonRender(),
};
