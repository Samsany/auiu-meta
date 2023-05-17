package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.UserGalleryCollection;
import com.auiucloud.component.cms.vo.UserGalleryFavoriteVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【ums_user_gallery_collection(我的收藏表)】的数据库操作Service
 * @createDate 2023-04-24 10:09:18
 */
public interface IUserGalleryCollectionService extends IService<UserGalleryCollection> {

    List<UserGalleryFavoriteVO> selectGalleryFavoriteVOListByGalleryId(Long galleryId);

    List<UserGalleryFavoriteVO> selectGalleryFavoriteVOListByGalleryIds(List<Long> galleryIds);

    List<UserGalleryFavoriteVO> selectGalleryFavoriteVOListByCId(Long cId);

    List<UserGalleryFavoriteVO> selectGalleryFavoriteVOListByCIds(List<Long> cIds);

    UserGalleryCollection selectGalleryFavoriteByUserId2GalleryId(Long userId, Long postId, Integer type);

}
