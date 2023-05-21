package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.SdText2ImgConfig;
import com.auiucloud.core.common.api.ApiResult;

/**
 * Ai绘画接口
 *
 * @author dries
 **/
public interface IAiDrawService {
    ApiResult<?> sdText2Img(SdText2ImgConfig config);
}
