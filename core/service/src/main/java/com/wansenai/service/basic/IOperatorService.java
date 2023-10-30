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
package com.wansenai.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.utils.response.Response;
import com.wansenai.dto.basic.AddOrUpdateOperatorDTO;
import com.wansenai.dto.basic.QueryOperatorDTO;
import com.wansenai.entities.basic.Operator;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansenai.vo.basic.OperatorVO;

import java.util.List;

public interface IOperatorService extends IService<Operator> {

    Response<Page<OperatorVO>> getOperatorPageList(QueryOperatorDTO queryOperatorDTO);

    Response<String> addOrUpdateOperator(AddOrUpdateOperatorDTO addOrUpdateOperatorDTO);

    Response<String> deleteBatchOperator(List<Long> ids);

    Response<String> updateOperatorStatus(List<Long> ids, Integer status);

    Operator getOperatorById(Long id);

    Response<List<OperatorVO>> getOperatorListByType(String type);
}
