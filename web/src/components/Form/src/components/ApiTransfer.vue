<!--
  - Copyright (C) 2023-2033 WanSen AI Team
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <Transfer
    :data-source="getdataSource"
    :filter-option="filterOption"
    :render="(item) => item.title"
    :showSelectAll="showSelectAll"
    :selectedKeys="selectedKeys"
    :targetKeys="getTargetKeys"
    :showSearch="showSearch"
    @change="handleChange"
  />
</template>

<script lang="ts">
  import { computed, defineComponent, watch, ref, unref, watchEffect, PropType } from 'vue';
  import { Transfer } from 'ant-design-vue';
  import { isFunction } from '/@/utils/is';
  import { get, omit } from 'lodash-es';
  import { propTypes } from '/@/utils/propTypes';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { TransferDirection, TransferItem } from 'ant-design-vue/lib/transfer';

  export default defineComponent({
    name: 'ApiTransfer',
    components: { Transfer },
    props: {
      value: { type: Array as PropType<Array<string>> },
      api: {
        type: Function as PropType<(arg) => Promise<TransferItem[]>>,
        default: null,
      },
      params: { type: Object },
      dataSource: { type: Array as PropType<Array<TransferItem>> },
      immediate: propTypes.bool.def(true),
      alwaysLoad: propTypes.bool.def(false),
      afterFetch: { type: Function },
      resultField: propTypes.string.def(''),
      labelField: propTypes.string.def('title'),
      valueField: propTypes.string.def('key'),
      showSearch: { type: Boolean, default: false },
      disabled: { type: Boolean, default: false },
      filterOption: {
        type: Function as PropType<(inputValue: string, item: TransferItem) => boolean>,
      },
      selectedKeys: { type: Array as PropType<Array<string>> },
      showSelectAll: { type: Boolean, default: false },
      targetKeys: { type: Array as PropType<Array<string>> },
    },
    emits: ['options-change', 'change'],
    setup(props, { attrs, emit }) {
      const _dataSource = ref<TransferItem[]>([]);
      const _targetKeys = ref<string[]>([]);
      const { t } = useI18n();

      const getAttrs = computed(() => {
        return {
          ...(!props.api ? { dataSource: unref(_dataSource) } : {}),
          ...attrs,
        };
      });
      const getdataSource = computed(() => {
        const { labelField, valueField } = props;

        return unref(_dataSource).reduce((prev, next) => {
          if (next) {
            prev.push({
              ...omit(next, [labelField, valueField]),
              title: next[labelField],
              key: next[valueField],
            });
          }
          return prev;
        }, [] as TransferItem[]);
      });
      const getTargetKeys = computed<string[]>(() => {
        if (unref(_targetKeys).length > 0) {
          return unref(_targetKeys);
        }
        if (Array.isArray(props.value)) {
          return props.value;
        }
        if (Array.isArray(props.targetKeys)) {
          return props.targetKeys;
        }
        return [];
      });

      function handleChange(keys: string[], direction: TransferDirection, moveKeys: string[]) {
        _targetKeys.value = keys;
        console.log(direction);
        console.log(moveKeys);
        emit('change', keys);
      }

      watchEffect(() => {
        props.immediate && !props.alwaysLoad && fetch();
      });

      watch(
        () => props.params,
        () => {
          fetch();
        },
        { deep: true },
      );

      async function fetch() {
        const api = props.api;
        if (!api || !isFunction(api)) {
          if (Array.isArray(props.dataSource)) {
            _dataSource.value = props.dataSource;
          }
          return;
        }
        _dataSource.value = [];
        try {
          const res = await api(props.params);
          if (Array.isArray(res)) {
            _dataSource.value = res;
            emitChange();
            return;
          }
          if (props.resultField) {
            _dataSource.value = get(res, props.resultField) || [];
          }
          emitChange();
        } catch (error) {
          console.warn(error);
        }
      }
      function emitChange() {
        emit('options-change', unref(getdataSource));
      }
      return { getTargetKeys, getdataSource, t, getAttrs, handleChange };
    },
  });
</script>
