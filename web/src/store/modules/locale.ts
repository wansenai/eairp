import type { LocaleSetting, LocaleType } from '/#/config';

import { defineStore } from 'pinia';
import { store } from '@/store';

import { LOCALE_KEY } from '@/enums/cacheEnum';
import { createLocalStorage } from '@/utils/cache';
import { localeSetting } from '@/settings/localeSetting';
import {useUserStore} from "@/store/modules/user";
import {updateUser} from "@/api/sys/user";
import {updateUserInfoReq} from "@/api/sys/model/userModel";
import {ref} from "vue";

const ls = createLocalStorage();
const lsLocaleSetting = (ls.get(LOCALE_KEY) || localeSetting) as LocaleSetting;

interface LocaleState {
  localInfo: LocaleSetting;
}

export const useLocaleStore = defineStore({
  id: 'app-locale',
  state: (): LocaleState => ({
    localInfo: lsLocaleSetting,
  }),
  getters: {
    getShowPicker(state): boolean {
      return !!state.localInfo?.showPicker;
    },
    getLocale(state): LocaleType {
      return state.localInfo?.locale ?? 'zh_CN';
    },
  },
  actions: {
    /**
     * Set up multilingual information and cache
     * @param info multilingual info
     */
    setLocaleInfo(info: Partial<LocaleSetting>) {
      this.localInfo = { ...this.localInfo, ...info };
      ls.set(LOCALE_KEY, this.localInfo);
      const userStore = useUserStore();

      const reqLanguage = ref('');
      if(this.localInfo.locale === 'en') {
        reqLanguage.value = 'en_US'
      } else if (this.localInfo.locale === 'zh_CN') {
        reqLanguage.value = 'zh_CN'
      }
      const data: updateUserInfoReq = {
        id: userStore.getUserInfo.id,
        systemLanguage: reqLanguage.value,
        name: userStore.getUserInfo.name
      }
      updateUser(data);
    },
    /**
     * Initialize multilingual information and load the existing configuration from the local cache
     */
    initLocale() {
      this.setLocaleInfo({
        ...localeSetting,
        ...this.localInfo,
      });
    },
  },
});

// Need to be used outside the setup
export function useLocaleStoreWithOut() {
  return useLocaleStore(store);
}
