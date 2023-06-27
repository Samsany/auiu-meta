package com.auiucloud.component.cms.mapper;

import com.auiucloud.component.cms.domain.GalleryReview;
import com.auiucloud.component.cms.vo.GalleryReviewVO;
import com.auiucloud.core.database.model.Search;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_gallery_review(作品审核表)】的数据库操作Mapper
* @createDate 2023-06-13 14:26:52
* @Entity com.auiucloud.component.cms.domain.GalleryReview
*/
public interface GalleryReviewMapper extends BaseMapper<GalleryReview> {

    Long countGalleryReviewVOTotal(@Param("search") Search search, @Param("params") GalleryReview params);

    List<GalleryReviewVO> selectGalleryReviewVOPage(@Param("search") Search search, @Param("params") GalleryReview params);

    GalleryReviewVO selectGalleryReviewVOById(@Param("id") Long id);

}




