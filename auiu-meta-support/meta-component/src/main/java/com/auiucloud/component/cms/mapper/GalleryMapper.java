package com.auiucloud.component.cms.mapper;

import com.auiucloud.component.cms.domain.Gallery;
import com.auiucloud.component.cms.vo.GalleryVO;
import com.auiucloud.core.database.model.Search;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dries
 * @description 针对表【cms_gallery(作品表)】的数据库操作Mapper
 * @createDate 2023-04-16 20:56:41
 * @Entity com.auiucloud.component.cms.domain.Gallery
 */
public interface GalleryMapper extends BaseMapper<Gallery> {

    GalleryVO selectGalleryVOById(@Param("galleryId") Long galleryId);

    List<GalleryVO> selectGalleryReCommendList();

    Long countSquareGalleryVOTotal(@Param("search") Search search, @Param("gallery") Gallery gallery);

    List<GalleryVO> selectSquareGalleryVOPage(@Param("search") Search search, @Param("gallery") Gallery gallery);

    Long countUserHomeGalleryVONum(@Param("userId") Long userId, @Param("search") Search search, @Param("gallery") Gallery gallery);

    List<GalleryVO> selectUserHomeGalleryVOPage(@Param("userId") Long userId, @Param("search") Search search, @Param("gallery") Gallery gallery);

    List<GalleryVO> selectGalleryVOListByIds(List<Long> galleryIds);
}




