export interface BasicPageParams {
  page: number;
  pageSize: number;
}

export interface BasicFetchResult<T> {
  items: T[];
  total: number;
}

export interface BaseDataResp<T> {
  code: string;
  msg: string;
  data: T;
}

export interface BaseResp {
  code?: string;
  msg: string;
}

export interface BaseListResp<T> {
  data: T[];
  total: number;
}
