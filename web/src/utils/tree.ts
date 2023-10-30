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

import { DataNode } from 'ant-design-vue/es/vc-tree/interface';
import { map, pick } from 'lodash-es';
import { array2tree } from '@axolo/tree-array';

export interface buildNodeOption {
  labelField: string;
  idKeyField: string;
  valueField: string;
  parentKeyField: string;
  defaultValue?: string | object;
  childrenKeyField: string;
}

export function buildDataNode(data: any, options: buildNodeOption): DataNode[] {
  const treeNodeData = map(data, (obj) => {
    const tmpData = pick(obj, [
      options.labelField,
      options.idKeyField,
      options.valueField,
      options.parentKeyField,
    ]);
    Object.keys(tmpData).forEach((e) => {
      if (e === options.labelField) {
        tmpData['title'] = tmpData[e];
        delete tmpData[e];
      } else if (e === options.valueField) {
        tmpData['key'] = tmpData[e];
        if (e !== options.idKeyField && e !== options.parentKeyField) {
          delete tmpData[e];
        }
      }
    });
    return tmpData;
  });

  const treeConv = array2tree(treeNodeData, {
    idKey: options.idKeyField,
    parentKey: options.parentKeyField,
    childrenKey: options.childrenKeyField,
  });

  // add default label
  if (options.defaultValue) {
    treeConv.push(options.defaultValue);
  }
  return treeConv as DataNode[];
}

// buildTreeNode returns treeData for tree select from data
export function buildTreeNode(data: any, options: buildNodeOption): Recordable[] {
  const treeNodeData = map(data, (obj) => {
    const tmpData = pick(obj, [
      options.labelField,
      options.idKeyField,
      options.valueField,
      options.parentKeyField,
    ]);
    Object.keys(tmpData).forEach((e) => {
      if (e === options.labelField) {
        tmpData['label'] = tmpData[e];
        delete tmpData[e];
      } else if (e === options.valueField) {
        tmpData['value'] = tmpData[e];
        if (e !== options.idKeyField && e !== options.parentKeyField) {
          delete tmpData[e];
        }
      }
    });
    return tmpData;
  });

  const treeConv = array2tree(treeNodeData, {
    idKey: options.idKeyField,
    parentKey: options.parentKeyField,
    childrenKey: options.childrenKeyField,
  });

  // add default label
  if (options.defaultValue) {
    treeConv.push(options.defaultValue);
  }
  return treeConv as Recordable[];
}
