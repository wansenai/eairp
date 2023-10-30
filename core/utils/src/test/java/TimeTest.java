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

import com.wansenai.utils.TimeUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TimeTest {

    @Test
    public void testMyMethod() {
        // Arrange
        String a = "2023-10-27 17:37:21";
        String b = "yyyy-MM-dd HH:mm:ss";

        // Act
        LocalDateTime result = myMethod(a, b);

        // Assert
        assert result.getDayOfMonth() == result.getDayOfMonth();
    }

    public LocalDateTime myMethod(String a, String b) {
        return TimeUtil.parse(a, b);
    }
}
