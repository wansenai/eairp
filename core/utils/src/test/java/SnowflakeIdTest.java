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

import com.wansenai.utils.SnowflakeIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class SnowflakeIdTest {

    @Test
    public void generateId() {
        long start = System.currentTimeMillis();
        SnowflakeIdUtil idWorker = new SnowflakeIdUtil(5, 9);
        for (int i = 0; i < 50; i++) {
            long id = idWorker.nextId();
            log.info(String.valueOf(id));
        }
        long end = System.currentTimeMillis();
        log.info(String.valueOf(end - start));
    }
}
