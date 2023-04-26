package com.auiucloud.component.cms.service.impl;

import com.auiucloud.component.cms.vo.UserGalleryLikeVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.cms.domain.UserGalleryLike;
import com.auiucloud.component.cms.service.IUserGalleryLikeService;
import com.auiucloud.component.cms.mapper.UserGalleryLikeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
        queryWrapper.eq(UserGalleryLike::getUId, userId);
        queryWrapper.eq(UserGalleryLike::getPostId, postId);
        queryWrapper.eq(UserGalleryLike::getType, type);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<UserGalleryLikeVO> selectGalleryLikeVOListByGalleryId(Long galleryId) {
        return baseMapper.selectGalleryLikeVOListByGalleryId(galleryId);
    }

    @Override
    public List<UserGalleryLikeVO> selectGalleryLikeVOListByGalleryIds(List<Long> galleryIds) {
        return baseMapper.selectGalleryLikeVOListByGalleryIds(galleryIds);
    }
}




