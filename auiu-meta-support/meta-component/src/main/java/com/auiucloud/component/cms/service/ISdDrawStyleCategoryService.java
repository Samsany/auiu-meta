package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.SdDrawStyleCategory;
import com.auiucloud.component.cms.vo.SdDrawStyleCategoryVO;
import com.auiucloud.component.cms.vo.SdDrawStyleVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_sd_draw_style_category(SD绘画风格分类表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdDrawStyleCategoryService extends IService<SdDrawStyleCategory> {

    List<SdDrawStyleCategoryVO> selectAllSdDrawCategoryVOList();

}
