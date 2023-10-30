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
import { VxeGlobalRendererHandles } from 'vxe-table';
import { getComponent } from './common';

function createEmptyRender() {
  return function (renderOpts: VxeGlobalRendererHandles.RenderEmptyOptions) {
    const { name, attrs, props } = renderOpts;

    const Component = getComponent(name);
    return [
      h(
        'div',
        {
          class: 'flex items-center justify-center',
        },
        h(Component, {
          ...attrs,
          ...props,
        }),
      ),
    ];
  };
}

export default {
  renderEmpty: createEmptyRender(),
};
