package com.auiucloud.component.cms.mapper;

import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.component.cms.vo.GalleryCollectionVO;
import com.auiucloud.core.database.model.Search;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dries
 * @description 针对表【cms_gallery_collection(作品合集表)】的数据库操作Mapper
 * @createDate 2023-04-16 20:56:41
 * @Entity com.auiucloud.component.cms.domain.GalleryCollection
 */
public interface GalleryCollectionMapper extends BaseMapper<GalleryCollection> {

    GalleryCollectionVO selectGalleryCollectionVOById(@Param("cId") Long cId);

    Long countUserHomeGalleryCollectionTotal(@Param("userId") Long userId, @Param("search") Search search, @Param("galleryCollection") GalleryCollection galleryCollection);

    List<GalleryCollectionVO> selectUserHomeGalleryCollectionPage(@Param("userId") Long userId, @Param("search") Search search, @Param("galleryCollection") GalleryCollection galleryCollection);

    List<GalleryCollectionVO> selectGalleryCollectionVOByCIds(List<Long> cIds);
}




