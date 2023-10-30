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

/**
 * Although it seems that the parameters of the two objects are very similar here,
 * there is no guarantee that new fields will be added to the view object return in the future. For extension purposes,
 * it is still necessary to distinguish between them
 *
 * @author James
 * @since  2023-10-08 17:27
 */

export interface ProductAttributeResp {
    id: number | string;
    attributeName: string;
    attributeValue: string;
    remark: string;
    sort: number;
}

export interface AddOrUpdateProductAttributeReq {
    id: number | string;
    attributeName: string;
    attributeValue: string;
    remark: string;
    sort: number;
}

export interface ProductAttributeListReq {
    attributeName: string | undefined;
    page: number;
    pageSize: number;
}