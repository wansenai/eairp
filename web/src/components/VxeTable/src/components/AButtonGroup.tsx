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

import {
  FormItemContentRenderParams,
  FormItemRenderOptions,
  VxeGlobalRendererHandles,
} from 'vxe-table';
import { createDefaultRender, createEditRender, createFormItemRender } from './AButton';

function createEditButtonRender() {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderEditOptions,
    params: VxeGlobalRendererHandles.RenderEditParams,
  ) {
    const buttonEditRender = createEditRender();
    const { children } = renderOpts;
    if (children) {
      return children.map(
        (childRenderOpts: VxeGlobalRendererHandles.RenderEditOptions) =>
          buttonEditRender(childRenderOpts, params)[0],
      );
    }
    return [];
  };
}

function createDefaultButtonRender() {
  return function (
    renderOpts: VxeGlobalRendererHandles.RenderDefaultOptions,
    params: VxeGlobalRendererHandles.RenderDefaultParams,
  ) {
    const buttonDefaultRender = createDefaultRender();
    const { children } = renderOpts;
    if (children) {
      return children.map(
        (childRenderOpts: VxeGlobalRendererHandles.RenderDefaultOptions) =>
          buttonDefaultRender(childRenderOpts, params)[0],
      );
    }
    return [];
  };
}

function createButtonItemRender() {
  return function (renderOpts: FormItemRenderOptions, params: FormItemContentRenderParams) {
    const buttonItemRender = createFormItemRender();
    const { children } = renderOpts;
    if (children) {
      return children.map(
        (childRenderOpts: FormItemRenderOptions) => buttonItemRender(childRenderOpts, params)[0],
      );
    }
    return [];
  };
}

export default {
  renderEdit: createEditButtonRender(),
  renderDefault: createDefaultButtonRender(),
  renderItemContent: createButtonItemRender(),
};
