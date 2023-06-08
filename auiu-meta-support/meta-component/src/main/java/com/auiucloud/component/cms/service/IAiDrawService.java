package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.SdDrawCategory;
import com.auiucloud.component.cms.domain.SdText2ImgParam;
import com.auiucloud.component.cms.vo.SdDrawCategoryVO;
import com.auiucloud.component.cms.vo.SdDrawParamVO;
import com.auiucloud.component.cms.vo.SdText2ImgConfigVO;
import com.auiucloud.core.common.api.ApiResult;

import java.util.List;

/**
 * Ai绘画接口
 *
 * @author dries
 **/
public interface IAiDrawService {

    List<SdDrawCategoryVO> aiDrawMenuList();

    SdText2ImgConfigVO sdText2ImgConfig(Long aiDrawMenuId);

    ApiResult<?> sdText2Img(SdDrawParamVO paramVO);

}
