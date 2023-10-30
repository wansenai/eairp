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

import type { Menu as MenuType } from '/@/router/types';
import type { MenuState } from './types';
import { computed, Ref, toRaw, unref } from 'vue';
import { uniq } from 'lodash-es';
import { getAllParentPath } from '/@/router/helper/menuHelper';
import { useTimeoutFn } from '@vben/hooks';
import { useDebounceFn } from '@vueuse/core';

export function useOpenKeys(
  menuState: MenuState,
  menus: Ref<MenuType[]>,
  accordion: Ref<boolean>,
  mixSider: Ref<boolean>,
  collapse: Ref<boolean>,
) {
  const debounceSetOpenKeys = useDebounceFn(setOpenKeys, 50);
  async function setOpenKeys(path: string) {
    const native = !mixSider.value;
    const menuList = toRaw(menus.value);

    const handle = () => {
      if (menuList?.length === 0) {
        menuState.activeSubMenuNames = [];
        menuState.openNames = [];
        return;
      }
      const keys = getAllParentPath(menuList, path);

      if (!unref(accordion)) {
        menuState.openNames = uniq([...menuState.openNames, ...keys]);
      } else {
        menuState.openNames = keys;
      }
      menuState.activeSubMenuNames = menuState.openNames;
    };
    if (native) {
      handle();
    } else {
      useTimeoutFn(handle, 30);
    }
  }

  const getOpenKeys = computed(() => {
    return unref(collapse) ? [] : menuState.openNames;
  });

  return { setOpenKeys: debounceSetOpenKeys, getOpenKeys };
}
