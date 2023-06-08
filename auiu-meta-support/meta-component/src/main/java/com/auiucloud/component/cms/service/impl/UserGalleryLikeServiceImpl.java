package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.auiucloud.component.cms.domain.UserGalleryLike;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.mapper.UserGalleryLikeMapper;
import com.auiucloud.component.cms.service.IUserGalleryLikeService;
import com.auiucloud.component.cms.vo.UserGalleryLikeVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dries
 * @description 针对表【ums_user_gallery_like(我的点赞表)】的数据库操作Service实现
 * @createDate 2023-04-24 10:09:18
 */
@Service
public class UserGalleryLikeServiceImpl extends ServiceImpl<UserGalleryLikeMapper, UserGalleryLike>
        implements IUserGalleryLikeService {

    @Override
    public UserGalleryLike selectGalleryLikeByUserId2GalleryId(Long userId, Long postId, Integer type) {
        LambdaQueryWrapper<UserGalleryLike> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserGalleryLike::getUserId, userId);
        queryWrapper.eq(UserGalleryLike::getPostId, postId);
        queryWrapper.eq(UserGalleryLike::getType, type);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<UserGalleryLikeVO> selectGalleryLikeVOListByGId(Long galleryId) {
        return Optional.ofNullable(baseMapper.selectGalleryLikeVOListByPostId2Type(galleryId, GalleryEnums.GalleryPageType.GALLERY.getValue()))
                .orElse(Collections.emptyList());
    }

    @Override
    public List<UserGalleryLikeVO> selectGalleryLikeVOListByGIds(List<Long> galleryIds) {
        if (CollUtil.isNotEmpty(galleryIds)) {
            return Optional.ofNullable(baseMapper.selectGalleryLikeVOListByPostIds2Type(galleryIds, GalleryEnums.GalleryPageType.GALLERY.getValue())).orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<UserGalleryLikeVO> selectGalleryLikeVOListByCId(Long cateId) {
        return Optional.ofNullable(baseMapper.selectGalleryLikeVOListByPostId2Type(cateId, GalleryEnums.GalleryPageType.GALLERY_COLLECTION.getValue())).orElse(Collections.emptyList());
    }

    @Override
    public List<UserGalleryLikeVO> selectGalleryLikeVOListByCIds(List<Long> cIds) {
        if (CollUtil.isNotEmpty(cIds)) {
            return Optional.ofNullable(baseMapper.selectGalleryLikeVOListByPostIds2Type(cIds, GalleryEnums.GalleryPageType.GALLERY_COLLECTION.getValue())).orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }

    @Override
    public Long countUserReceivedLikeNum(Long userId) {
        return baseMapper.countUserReceivedLikeNum(userId);
    }
}




