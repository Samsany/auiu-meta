package com.auiucloud.component.sd.service;

import com.auiucloud.component.sd.domain.SdTxt2ImgParams;
import com.auiucloud.component.sd.vo.SdProgressVO;
import com.auiucloud.core.common.api.ApiResult;

/**
 * @author dries
 **/
public interface IAiDrawFactoryService {

    /**
     * 文生图
     *
     * @param text2ImgParam 参数
     * @return Object
     */
    ApiResult<?> sdText2Img(SdTxt2ImgParams text2ImgParam);

    /**
     * 进度条
     *
     * @return SdProgressVO
     */
    SdProgressVO getSdProgress();

}
