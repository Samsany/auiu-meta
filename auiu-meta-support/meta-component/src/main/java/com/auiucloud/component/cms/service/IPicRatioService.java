package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.PicRatio;
import com.auiucloud.component.cms.vo.PicRatioVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_pic_ratio(图片比例表)】的数据库操作Service
* @createDate 2023-05-21 14:52:48
*/
public interface IPicRatioService extends IService<PicRatio> {

    List<PicRatioVO> selectNormalPicRatioVOList();

}
