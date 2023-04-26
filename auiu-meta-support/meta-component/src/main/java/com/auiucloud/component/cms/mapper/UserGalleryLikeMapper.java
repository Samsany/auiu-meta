package com.auiucloud.component.cms.mapper;

import com.auiucloud.component.cms.domain.UserGalleryLike;
import com.auiucloud.component.cms.vo.UserGalleryLikeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author dries
* @description 针对表【ums_user_gallery_like(我的点赞表)】的数据库操作Mapper
* @createDate 2023-04-24 10:09:18
* @Entity com.auiucloud.component.cms.domain.UserGalleryLike
*/
public interface UserGalleryLikeMapper extends BaseMapper<UserGalleryLike> {

    List<UserGalleryLikeVO> selectGalleryLikeVOListByGalleryIds(List<Long> galleryIds);

    List<UserGalleryLikeVO> selectGalleryLikeVOListByGalleryId(@Param("galleryId") Long galleryId);
}




