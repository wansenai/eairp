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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static com.wansenai.utils.CommonTools.*;

@Slf4j
public class CommonToolsTest {

    @Test
    public void stringToArray() {
        String str = "这是一个字符串的测试";
        char[] array = str.toCharArray();
        for (char item : array) {
            log.info(String.valueOf(item));
        }
    }

    @Test
    public void getBeforeMonthTest() {
        log.info(getBeforeMonth(1));
    }

    @Test
    public void md5EncrypTest() {
        log.info(md5Encryp("guest"));
        log.info(md5Encryp("admin"));
    }

    @Test
    public void checkNumber() {
        String value = "2333";
        Assert.isTrue(checkStrIsNum(value), "checkStrIsNumber(value) 返回 false");
    }

    @Test
    public void RandomCharTest() {
        for (int i = 0; i < 100; i++) {
            log.info(getRandomChar() + "||");
        }
    }

    @Test
    public void getUUIDTest() {
        log.info(getUUID_32());
    }
}
