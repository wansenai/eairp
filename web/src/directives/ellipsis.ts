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

import type { CSSProperties, DirectiveBinding, ObjectDirective, App } from 'vue';

interface IValue {
  width?: number;
  line?: number;
}

interface IOptions {
  [key: string]: CSSProperties;
}

const cssProperties: IOptions = {
  single: {
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
  },
  multiple: {
    display: '-webkit-box',
    overflow: 'hidden',
    wordBreak: 'break-all',
  },
};

const Ellipsis: ObjectDirective = {
  mounted(el: HTMLElement, binding: DirectiveBinding<Array<IValue>>) {
    const { value = [100, 1], arg = 'single' } = binding;
    const [width, line] = value;
    Object.entries(cssProperties[arg]).forEach(([key, value]) => {
      el.style[key] = value;
    });
    el.style.width = `${width}px`;
    if (arg === 'multiple') {
      el.style.webkitLineClamp = `${line}`;
      el.style.webkitBoxOrient = 'vertical';
    }
  },
};
export function setupEllipsisDirective(app: App) {
  app.directive('ellipsis', Ellipsis);
}
export default Ellipsis;
