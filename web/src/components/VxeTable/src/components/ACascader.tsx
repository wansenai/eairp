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

import { VxeGlobalRendererHandles } from 'vxe-table';
import XEUtils from 'xe-utils';
import {
  createEditRender,
  createCellRender,
  createFormItemRender,
  createExportMethod,
} from './common';

function matchCascaderData(index: number, list: any[], values: any[], labels: any[]) {
  const val = values[index];
  if (list && values.length > index) {
    XEUtils.each(list, (item) => {
      if (item.value === val) {
        labels.push(item.label);
        matchCascaderData(++index, item.children, values, labels);
      }
    });
  }
}

function getCascaderCellValue(
  renderOpts: VxeGlobalRendererHandles.RenderOptions,
  params: VxeGlobalRendererHandles.RenderCellParams,
) {
  const { props = {} } = renderOpts;
  const { row, column } = params;
  const cellValue = XEUtils.get(row, column.field as string);
  const values = cellValue || [];
  const labels: Array<any> = [];
  matchCascaderData(0, props.options, values, labels);
  return (
    props.showAllLevels === false ? labels.slice(labels.length - 1, labels.length) : labels
  ).join(` ${props.separator || '/'} `);
}

export default {
  renderEdit: createEditRender(),
  renderCell: createCellRender(getCascaderCellValue),
  renderItemContent: createFormItemRender(),
  exportMethod: createExportMethod(getCascaderCellValue),
};
