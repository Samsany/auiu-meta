package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.PicTag;
import com.auiucloud.component.cms.domain.SwiperAdv;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_swiper_adv(轮播广告表)】的数据库操作Service
* @createDate 2023-04-11 16:03:44
*/
public interface ISwiperAdvService extends IService<SwiperAdv> {

    PageUtils listPage(Search search, SwiperAdv swiperAdv);

    boolean setStatus(UpdateStatusDTO updateStatusDTO);

    List<SwiperAdv> selectCommonSwiperAdvList(SwiperAdv swiperAdv);

}
