package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.auiucloud.component.cms.domain.UserGalleryCollection;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.mapper.UserGalleryCollectionMapper;
import com.auiucloud.component.cms.service.IUserGalleryCollectionService;
import com.auiucloud.component.cms.vo.UserGalleryFavoriteVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dries
 * @description 针对表【ums_user_gallery_collection(我的收藏表)】的数据库操作Service实现
 * @createDate 2023-04-24 10:09:18
 */
@Service
public class UserGalleryCollectionServiceImpl extends ServiceImpl<UserGalleryCollectionMapper, UserGalleryCollection>
        implements IUserGalleryCollectionService {

    @Override
    public List<UserGalleryFavoriteVO> selectGalleryFavoriteVOListByGalleryIds(List<Long> galleryIds) {
        if (CollUtil.isNotEmpty(galleryIds)) {
            return Optional.ofNullable(baseMapper.selectGalleryFavoriteVOListByPostIds2Type(galleryIds, GalleryEnums.GalleryPageType.GALLERY.getValue()))
                    .orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<UserGalleryFavoriteVO> selectGalleryFavoriteVOListByGalleryId(Long galleryId) {
        return Optional.ofNullable(baseMapper.selectGalleryFavoriteVOListByPostId2Type(galleryId, GalleryEnums.GalleryPageType.GALLERY.getValue()))
                .orElse(Collections.emptyList());
    }

    @Override
    public List<UserGalleryFavoriteVO> selectGalleryFavoriteVOListByCIds(List<Long> cIds) {
        if (CollUtil.isNotEmpty(cIds)) {
            return Optional.ofNullable(baseMapper.selectGalleryFavoriteVOListByPostIds2Type(cIds, GalleryEnums.GalleryPageType.GALLERY_COLLECTION.getValue()))
                    .orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<UserGalleryFavoriteVO> selectGalleryFavoriteVOListByCId(Long cId) {
        return Optional.ofNullable(baseMapper.selectGalleryFavoriteVOListByPostId2Type(cId, GalleryEnums.GalleryPageType.GALLERY_COLLECTION.getValue()))
                .orElse(Collections.emptyList());
    }

    @Override
    public UserGalleryCollection selectGalleryFavoriteByUserId2GalleryId(Long userId, Long postId, Integer type) {
        LambdaQueryWrapper<UserGalleryCollection> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserGalleryCollection::getUId, userId);
        queryWrapper.eq(UserGalleryCollection::getPostId, postId);
        queryWrapper.eq(UserGalleryCollection::getType, type);
        return this.getOne(queryWrapper);
    }

}




