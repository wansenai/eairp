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
  isEmptyValue,
  createFormItemRender,
  createExportMethod,
} from './common';

function getTreeSelectCellValue(
  renderOpts: VxeGlobalRendererHandles.RenderOptions,
  params: VxeGlobalRendererHandles.RenderCellParams | VxeGlobalRendererHandles.ExportMethodParams,
) {
  const { props = {} } = renderOpts;
  const { treeData, treeCheckable } = props;
  const { row, column } = params;
  const cellValue = XEUtils.get(row, column.field as string);
  if (!isEmptyValue(cellValue)) {
    return XEUtils.map(treeCheckable ? cellValue : [cellValue], (value) => {
      const matchObj = XEUtils.findTree(treeData, (item: any) => item.value === value, {
        children: 'children',
      });
      return matchObj ? matchObj.item.title : value;
    }).join(', ');
  }
  return cellValue;
}

export default {
  renderEdit: createEditRender(),
  renderCell: createCellRender(getTreeSelectCellValue),
  renderItemContent: createFormItemRender(),
  exportMethod: createExportMethod(getTreeSelectCellValue),
};
