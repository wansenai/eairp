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

import { defineComponent, computed, ref } from 'vue';
import { BasicTableProps } from './types';
import { basicProps } from './props';
import { ignorePropKeys } from './const';
import { basicEmits } from './emits';
import XEUtils from 'xe-utils';
import type {
  VxeGridInstance,
  VxeGridEventProps,
  GridMethods,
  TableMethods,
  TableEditMethods,
  TableValidatorMethods,
} from 'vxe-table';
import { Grid as VxeGrid } from 'vxe-table';

import { extendSlots } from '/@/utils/helper/tsxHelper';
import { gridComponentMethodKeys } from './methods';
import { omit } from 'lodash-es';

export default defineComponent({
  name: 'VxeBasicTable',
  props: basicProps,
  emits: basicEmits,
  setup(props, { emit, attrs }) {
    const tableElRef = ref<VxeGridInstance>();
    const emitEvents: VxeGridEventProps = {};

    const extendTableMethods = (methodKeys) => {
      const funcs: any = {};
      methodKeys.forEach((name) => {
        funcs[name] = (...args: any[]) => {
          const $vxegrid: any = tableElRef.value;
          if ($vxegrid && $vxegrid[name]) {
            return $vxegrid[name](...args);
          }
        };
      });

      return funcs;
    };

    const gridExtendTableMethods = extendTableMethods(gridComponentMethodKeys) as GridMethods &
      TableMethods &
      TableEditMethods &
      TableValidatorMethods;

    basicEmits.forEach((name) => {
      const type = XEUtils.camelCase(`on-${name}`) as keyof VxeGridEventProps;

      emitEvents[type] = (...args: any[]) => emit(name, ...args);
    });

    /**
     * @description: 二次封装需要的所有属性
     *  1.部分属性需要和全局属性进行合并
     */
    const getBindValues = computed<BasicTableProps>(() => {
      const propsData: BasicTableProps = {
        ...attrs,
        ...props,
      };

      return propsData;
    });

    /**
     * @description: Table 所有属性
     */
    const getBindGridValues = computed(() => {
      const omitProps = omit(getBindValues.value, ignorePropKeys);

      return {
        ...omitProps,
        ...getBindGridEvent,
      };
    });

    /**
     * @description: 组件外层class
     */
    const getWrapperClass = computed(() => {
      return [attrs.class];
    });

    /**
     * @description: 重写Vxe-table 方法
     */
    const getBindGridEvent: VxeGridEventProps = {
      ...emitEvents,
    };

    return {
      getWrapperClass,
      getBindGridValues,
      tableElRef,
      ...gridExtendTableMethods,
    };
  },
  render() {
    const { tableClass, tableStyle } = this.$props;

    return (
      <div class={`h-full flex flex-col bg-white ${this.getWrapperClass}`}>
        <VxeGrid
          ref="tableElRef"
          class={`vxe-grid_scrollbar px-6 py-4 ${tableClass}`}
          style={tableStyle}
          {...this.getBindGridValues}
        >
          {extendSlots(this.$slots)}
        </VxeGrid>
      </div>
    );
  },
});
