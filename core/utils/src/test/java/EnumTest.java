import com.wansenai.utils.enums.RetailCodeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://opensource.wansenai.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
public class EnumTest {

    @Test
    public void retailCodeEnumTest() {
        RetailCodeEnum successEnum = RetailCodeEnum.ADD_RETAIL_SHIPMENTS_SUCCESS;
        assertEquals("R0001", successEnum.getCode());
        assertEquals("成功添加零售出库单据", successEnum.getMsg());

        RetailCodeEnum errorEnum = RetailCodeEnum.ADD_RETAIL_SHIPMENTS_ERROR;
        assertEquals("R0500", errorEnum.getCode());
        assertEquals("系统异常，添加零售出库单据失败", errorEnum.getMsg());

        RetailCodeEnum updateEnum = RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_ERROR;
        assertEquals("R0501", updateEnum.getCode());
        assertEquals("系统异常，修改零售出库单据失败", updateEnum.getMsg());

        RetailCodeEnum deleteSuccessEnum = RetailCodeEnum.DELETE_RETAIL_SHIPMENTS_SUCCESS;
        assertEquals("R0003", deleteSuccessEnum.getCode());
        assertEquals("成功删除零售出库单据", deleteSuccessEnum.getMsg());

        RetailCodeEnum deleteErrorEnum = RetailCodeEnum.DELETE_RETAIL_SHIPMENTS_ERROR_EN;
        assertEquals("R0502", deleteErrorEnum.getCode());
        assertEquals("System exception, failed to delete retail outbound document", deleteErrorEnum.getMsg());
    }
}
