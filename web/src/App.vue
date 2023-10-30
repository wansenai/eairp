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
  <ConfigProvider :locale="getAntdLocale" :theme="isDark ? darkTheme : {}">
    <AppProvider>
      <RouterView />
    </AppProvider>
  </ConfigProvider>
</template>

<script lang="ts" setup>
import { ConfigProvider } from 'ant-design-vue';
import { AppProvider } from '@/components/Application';
import { useTitle } from '@/hooks/web/useTitle';
import { useLocale } from '@/locales/useLocale';

import 'dayjs/locale/zh-cn';
import { useDarkModeTheme } from '@/hooks/setting/useDarkModeTheme';

// support Multi-language
const { getAntdLocale } = useLocale();

const { isDark, darkTheme } = useDarkModeTheme();

// Listening to page changes and dynamically changing site titles
useTitle();
</script>
