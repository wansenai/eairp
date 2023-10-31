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

import com.wansenai.utils.ComputerInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Slf4j
public class ComputerInfoTest {

    @Test
    public void getMacAddress() throws IOException {
        log.info(ComputerInfo.getMacAddress());
        log.info(ComputerInfo.getComputerName());
        log.info(ComputerInfo.getIpAddr());
        log.info(ComputerInfo.getIpAddrAndName());
    }
}
