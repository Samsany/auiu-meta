package com.auiucloud.component.cms.service;

import com.auiucloud.component.sd.domain.SdPicQuality;
import com.auiucloud.component.sd.vo.SdPicQualityVO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【sd_up_scale(SD图片放大算法表)】的数据库操作Service
* @createDate 2023-05-21 20:04:17
*/
public interface IPicQualityService extends IService<SdPicQuality> {

    List<SdPicQualityVO> selectNormalPicQualityList();

    PageUtils listPage(Search search, SdPicQuality quality);

    List<SdPicQualityVO> selectPicQualityVOListByIds(List<Long> ids);

    boolean checkPicQualityNameExist(SdPicQuality quality);

    boolean setPicQualityStatus(UpdateStatusDTO updateStatusDTO);

}
