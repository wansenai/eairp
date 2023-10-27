import { BaseDataResp } from '../model/baseModel';
import { ErrorMessageMode } from '/#/axios';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  GetCaptcha = '/v2/common/captcha'
}

interface CaptchaResp {
  captchaId: string;
  imagePath: string;
}

export function getCaptcha(mode: ErrorMessageMode = 'notice') {
  return defHttp.get<BaseDataResp<CaptchaResp>>(
    {
      url: Api.GetCaptcha,
    },
    {
      errorMessageMode: mode,
    },
  );
}
