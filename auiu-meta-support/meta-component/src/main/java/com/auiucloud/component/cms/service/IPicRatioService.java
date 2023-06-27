package com.auiucloud.component.cms.service;

import com.auiucloud.component.sd.domain.SdPicRatio;
import com.auiucloud.component.sd.vo.SdPicRatioVO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_pic_ratio(图片比例表)】的数据库操作Service
* @createDate 2023-05-21 14:52:48
*/
public interface IPicRatioService extends IService<SdPicRatio> {

    PageUtils listPage(Search search, SdPicRatio ratio);

    List<SdPicRatioVO> selectNormalPicRatioVOList();
    List<SdPicRatioVO> selectPicRatioVOListByIds(List<Long> ids);

    boolean checkPicRatioNameExist(SdPicRatio ratio);

    boolean setPicRatioStatus(UpdateStatusDTO updateStatusDTO);


}
