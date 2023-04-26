package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.UserGalleryLike;
import com.auiucloud.component.cms.vo.UserGalleryLikeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【ums_user_gallery_like(我的点赞表)】的数据库操作Service
* @createDate 2023-04-24 10:09:18
*/
public interface IUserGalleryLikeService extends IService<UserGalleryLike> {

    /**
     *
     * @param userId 用户
     * @param postId 帖子ID
     * @param type 帖子类型
     * @return UserGalleryLike
     */
    UserGalleryLike selectGalleryLikeByUserId2GalleryId(Long userId, Long postId, Integer type);

    List<UserGalleryLikeVO> selectGalleryLikeVOListByGalleryId(Long galleryId);

    List<UserGalleryLikeVO> selectGalleryLikeVOListByGalleryIds(List<Long> galleryIds);

}
