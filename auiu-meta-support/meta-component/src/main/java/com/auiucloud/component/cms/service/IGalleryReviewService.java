package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.GalleryReview;
import com.auiucloud.component.cms.dto.GalleryReviewBatchDTO;
import com.auiucloud.component.cms.dto.GalleryReviewDTO;
import com.auiucloud.component.cms.vo.GalleryReviewVO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dries
* @description 针对表【cms_gallery_review(作品审核表)】的数据库操作Service
* @createDate 2023-06-13 14:26:52
*/
public interface IGalleryReviewService extends IService<GalleryReview> {

    PageUtils listPage(Search search, GalleryReview galleryReview);

    GalleryReviewVO getGalleryReviewVOById(Long id);

    boolean resolveGalleryReview(GalleryReviewDTO galleryReview);

    boolean rejectGalleryReview(GalleryReviewDTO galleryReview);

    boolean withdrawGalleryReview(GalleryReviewDTO galleryReview);

    boolean galleryReviewBatch(GalleryReviewBatchDTO galleryReview);

    Long auditCount(Search search);
}
