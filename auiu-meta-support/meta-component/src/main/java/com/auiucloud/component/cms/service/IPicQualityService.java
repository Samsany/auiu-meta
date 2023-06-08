package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.PicQuality;
import com.auiucloud.component.cms.vo.PicQualityVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_sd_up_scale(SD图片放大算法表)】的数据库操作Service
* @createDate 2023-05-21 20:04:17
*/
public interface IPicQualityService extends IService<PicQuality> {

    List<PicQualityVO> selectNormalPicQualityList();

}
