package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.GalleryAppeal;
import com.auiucloud.component.cms.dto.GalleryAppealDTO;
import com.auiucloud.component.cms.vo.GalleryAppealVO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_gallery_appeal(作品申诉表)】的数据库操作Service
* @createDate 2023-06-09 17:46:33
*/
public interface IGalleryAppealService extends IService<GalleryAppeal> {

    GalleryAppeal selectWaitAppealGalleryByGalleryId2UserId(Long userId, Long galleryId);

    PageUtils listPage(Search search, GalleryAppeal galleryAppeal);

    GalleryAppealVO getGalleryAppealVOById(Long id);

    boolean resolveGalleryAppeal(GalleryAppealDTO galleryAppeal);

    boolean rejectGalleryAppeal(GalleryAppealDTO galleryAppeal);
}
