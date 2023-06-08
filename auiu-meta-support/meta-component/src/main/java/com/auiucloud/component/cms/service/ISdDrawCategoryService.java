package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.SdDrawCategory;
import com.auiucloud.component.cms.vo.SdDrawCategoryVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_sd_draw_category(SD绘画类型表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdDrawCategoryService extends IService<SdDrawCategory> {

    List<SdDrawCategoryVO> aiDrawMenuList();

}
