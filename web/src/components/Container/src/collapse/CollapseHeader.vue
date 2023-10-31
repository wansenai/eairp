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

<script lang="tsx">
  import { defineComponent, computed, unref, type ExtractPropTypes, PropType } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { BasicArrow, BasicTitle } from '/@/components/Basic';

  const collapseHeaderProps = {
    prefixCls: String,
    title: String,
    show: Boolean,
    canExpan: Boolean,
    helpMessage: {
      type: [Array, String] as PropType<string[] | string>,
      default: '',
    },
  };

  export type CollapseHeaderProps = ExtractPropTypes<typeof collapseHeaderProps>;

  export default defineComponent({
    name: 'CollapseHeader',
    inheritAttrs: false,
    props: collapseHeaderProps,
    emits: ['expand'],
    setup(props, { slots, attrs, emit }) {
      const { prefixCls } = useDesign('collapse-container');
      const _prefixCls = computed(() => props.prefixCls || unref(prefixCls));
      return () => (
        <div class={[`${unref(_prefixCls)}__header px-2 py-5`, attrs.class]}>
          <BasicTitle helpMessage={props.helpMessage} normal>
            {slots.title?.() || props.title}
          </BasicTitle>

          <div class={`${unref(_prefixCls)}__action`}>
            {slots.action
              ? slots.action({ expand: props.show, onClick: () => emit('expand') })
              : props.canExpan && (
                  <BasicArrow up expand={props.show} onClick={() => emit('expand')} />
                )}
          </div>
        </div>
      );
    },
  });
</script>
