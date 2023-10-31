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

import { withInstall } from '/@/utils';
import vxeBasicTable from './src/VxeBasicTable';
import { VXETable } from 'vxe-table';
import VXETablePluginAntd from './src/components';
import VXETablePluginExportXLSX from 'vxe-table-plugin-export-xlsx';
import './src/setting';

export const VxeBasicTable = withInstall(vxeBasicTable);
export * from 'vxe-table';
export * from './src/types';

VXETable.use(VXETablePluginAntd).use(VXETablePluginExportXLSX);
