package com.auiucloud.component.cms.mapper;

import com.auiucloud.component.cms.domain.UserGalleryCollection;
import com.auiucloud.component.cms.vo.UserGalleryFavoriteVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author dries
* @description 针对表【ums_user_gallery_collection(我的收藏表)】的数据库操作Mapper
* @createDate 2023-04-24 10:09:18
* @Entity com.auiucloud.component.cms.domain.UserGalleryCollection
*/
public interface UserGalleryCollectionMapper extends BaseMapper<UserGalleryCollection> {

    List<UserGalleryFavoriteVO> selectGalleryFavoriteVOListByPostId2Type(@Param("postId") Long postId, @Param("type") Integer type);
    List<UserGalleryFavoriteVO> selectGalleryFavoriteVOListByPostIds2Type(@Param("postIds") List<Long> postIds, @Param("type") Integer type);

}




