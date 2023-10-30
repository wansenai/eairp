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

import { VxeGridPropTypes, VxeTablePropTypes } from 'vxe-table';
import tableProps from 'vxe-table/es/table/src/props';
import { CSSProperties } from 'vue';

/**
 * @description: table二次开发需要后，需要接受的所有prop属性
 */
export const basicProps = {
  ...tableProps,
  columns: Array as PropType<VxeGridPropTypes.Columns>,
  pagerConfig: {
    type: Object as PropType<VxeGridPropTypes.PagerConfig>,
    default: () => ({}),
  },
  proxyConfig: {
    type: Object as PropType<VxeGridPropTypes.ProxyConfig>,
    default: () => ({}),
  },
  toolbarConfig: {
    type: Object as PropType<VxeGridPropTypes.ToolbarConfig>,
    default: () => ({}),
  },
  formConfig: {
    type: Object as PropType<VxeGridPropTypes.FormConfig>,
    default: () => ({}),
  },
  zoomConfig: {
    type: Object as PropType<VxeGridPropTypes.ZoomConfig>,
    default: () => ({}),
  },
  printConfig: {
    type: Object as PropType<VxeTablePropTypes.PrintConfig>,
    default: () => ({}),
  },
  exportConfig: {
    type: Object as PropType<VxeTablePropTypes.ExportConfig>,
    default: () => ({}),
  },
  importConfig: {
    type: Object as PropType<VxeTablePropTypes.ImportConfig>,
    default: () => ({}),
  },
  size: String as PropType<VxeGridPropTypes.Size>,
  tableClass: {
    type: String,
    default: '',
  },
  tableStyle: {
    type: Object as PropType<CSSProperties>,
    default: () => ({}),
  },
};
