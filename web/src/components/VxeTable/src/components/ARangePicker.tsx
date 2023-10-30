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

import { VxeColumnPropTypes, VxeGlobalRendererHandles } from 'vxe-table';
import XEUtils from 'xe-utils';
import {
  createCellRender,
  createEditRender,
  createExportMethod,
  createFormItemRender,
} from './common';

function getRangePickerCellValue(
  renderOpts: VxeColumnPropTypes.EditRender,
  params: VxeGlobalRendererHandles.RenderCellParams | VxeGlobalRendererHandles.ExportMethodParams,
) {
  const { props = {} } = renderOpts;
  const { row, column } = params;
  let cellValue = XEUtils.get(row, column.field as string);
  if (cellValue) {
    cellValue = XEUtils.map(cellValue, (date: any) =>
      date.format(props.format || 'YYYY-MM-DD'),
    ).join(' ~ ');
  }
  return cellValue;
}

export default {
  renderEdit: createEditRender(),
  renderCell: createCellRender(getRangePickerCellValue),
  renderItemContent: createFormItemRender(),
  exportMethod: createExportMethod(getRangePickerCellValue),
};
