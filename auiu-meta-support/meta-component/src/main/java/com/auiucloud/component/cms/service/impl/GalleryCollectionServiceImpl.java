package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.component.cms.domain.PicTag;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.mapper.GalleryCollectionMapper;
import com.auiucloud.component.cms.service.*;
import com.auiucloud.component.cms.vo.GalleryCollectionVO;
import com.auiucloud.component.cms.vo.GalleryVO;
import com.auiucloud.component.cms.vo.UserGalleryFavoriteVO;
import com.auiucloud.component.cms.vo.UserGalleryLikeVO;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【cms_gallery_collection(作品合集表)】的数据库操作Service实现
 * @createDate 2023-04-16 20:56:41
 */
@Service
@RequiredArgsConstructor
public class GalleryCollectionServiceImpl extends ServiceImpl<GalleryCollectionMapper, GalleryCollection>
        implements IGalleryCollectionService {

    private final IPicTagService picTagService;
    @Lazy
    @Resource
    private IGalleryService galleryService;
    @Lazy
    @Resource
    private IUserGalleryLikeService userGalleryLikeService;
    @Lazy
    @Resource
    private IUserGalleryCollectionService userGalleryCollectionService;

    @Override
    public List<GalleryCollectionVO> listGalleryCollectionVOByCIds(List<Long> cIds) {
        if (CollUtil.isNotEmpty(cIds)) {
            List<GalleryCollectionVO> galleryCollections = Optional.ofNullable(baseMapper.selectGalleryCollectionVOByCIds(cIds)).orElse(Collections.emptyList());
            List<Long> ids = galleryCollections.parallelStream().map(GalleryCollectionVO::getId).collect(Collectors.toList());
            List<UserGalleryLikeVO> userGalleryLikeVOS = userGalleryLikeService.selectGalleryLikeVOListByCIds(ids);
            List<UserGalleryFavoriteVO> userGalleryFavoriteVOS = userGalleryCollectionService.selectGalleryFavoriteVOListByCIds(ids);
            galleryCollections.parallelStream().forEach(it -> buildGalleryCollectionAdditionalVO(SecurityUtil.getUserIdOrDefault(), it, userGalleryLikeVOS, userGalleryFavoriteVOS));
            return galleryCollections;
        }

        return Collections.emptyList();
    }

    @Override
    public List<GalleryCollectionVO> selectUserCollectionList(Search search, GalleryCollection galleryCollection) {
        List<GalleryCollectionVO> galleryCollections = new ArrayList<>();
        galleryCollections.add(GalleryCollectionVO.builder()
                .id(CommonConstant.ROOT_NODE_ID)
                .title("全部")
                .build());
        Optional.ofNullable(baseMapper.selectGalleryCollectionVOList(galleryCollection)).ifPresent(galleryCollections::addAll);

        return galleryCollections;
    }

    @Override
    public List<GalleryCollection> selectUserCollectionApiList(Search search, GalleryCollection galleryCollection) {
        LambdaQueryWrapper<GalleryCollection> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GalleryCollection::getUserId, SecurityUtil.getUserId());
        queryWrapper.orderByDesc(GalleryCollection::getIsTop);
        queryWrapper.orderByDesc(GalleryCollection::getSort);
        queryWrapper.orderByDesc(GalleryCollection::getCreateTime);
        queryWrapper.orderByDesc(GalleryCollection::getId);
        return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
    }

    @Override
    public PageUtils selectUserCollectionApiPage(Search search, GalleryCollection galleryCollection) {
        Long userId = SecurityUtil.getUserId();
        LambdaQueryWrapper<GalleryCollection> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GalleryCollection::getUserId, userId);
        queryWrapper.orderByDesc(GalleryCollection::getIsTop);
        queryWrapper.orderByDesc(GalleryCollection::getSort);
        queryWrapper.orderByDesc(GalleryCollection::getCreateTime);
        queryWrapper.orderByDesc(GalleryCollection::getId);
        IPage<GalleryCollection> page = this.page(PageUtils.getPage(search), queryWrapper);
        page.convert(it -> {
            GalleryCollectionVO galleryCollectionVO = new GalleryCollectionVO();
            BeanUtils.copyProperties(it, galleryCollectionVO);
            // 获取Tag
            if (ObjectUtil.isNotNull(it.getTagId())) {
                PicTag tag = picTagService.getById(it.getTagId());
                galleryCollectionVO.setTag(tag.getName());
            }

            // 设置封面信息
            setGalleryCollectionCover(galleryCollectionVO);
            // 设置作品数量
            setGalleryTotal(userId, galleryCollectionVO);
            return galleryCollectionVO;
        });
        return new PageUtils(page);
    }

    @Override
    public PageUtils selectGalleryCollectionUserHomePage(Search search, GalleryCollection galleryCollection) {
        // 查询总条数
        Long total = baseMapper.countUserHomeGalleryCollectionTotal(SecurityUtil.getUserIdOrDefault(), search, galleryCollection);
        Search s = PageUtils.buildPage(search, Math.toIntExact(total));
        List<GalleryCollectionVO> list = Optional.ofNullable(
                baseMapper.selectUserHomeGalleryCollectionPage(
                        SecurityUtil.getUserIdOrDefault(),
                        s,
                        galleryCollection)
        ).orElse(Collections.emptyList());

        Long userId = SecurityUtil.getUserIdOrDefault();
        List<Long> ids = list.parallelStream().map(GalleryCollectionVO::getId).collect(Collectors.toList());
        List<UserGalleryLikeVO> userGalleryLikeVOS = userGalleryLikeService.selectGalleryLikeVOListByCIds(ids);
        List<UserGalleryFavoriteVO> userGalleryFavoriteVOS = userGalleryCollectionService.selectGalleryFavoriteVOListByCIds(ids);

        list.parallelStream().forEach(it -> buildGalleryCollectionAdditionalVO(userId, it, userGalleryLikeVOS, userGalleryFavoriteVOS));
        return new PageUtils(total, search, list);
    }

    @Override
    public GalleryCollectionVO selectGalleryCollectionById(Long cateId) {
        GalleryCollectionVO galleryCollectionVO = baseMapper.selectGalleryCollectionVOById(cateId);
        if (ObjectUtil.isNotNull(galleryCollectionVO)) {
            List<UserGalleryLikeVO> userGalleryLikeVOS = userGalleryLikeService.selectGalleryLikeVOListByCId(cateId);
            List<UserGalleryFavoriteVO> userGalleryFavoriteVOS = userGalleryCollectionService.selectGalleryFavoriteVOListByCId(cateId);
            Long userId = SecurityUtil.getUserIdOrDefault();
            buildGalleryCollectionAdditionalVO(userId, galleryCollectionVO, userGalleryLikeVOS, userGalleryFavoriteVOS);
        }
        return galleryCollectionVO;
    }

    private void buildGalleryCollectionAdditionalVO(Long userId, GalleryCollectionVO galleryCollectionVO, List<UserGalleryLikeVO> userGalleryLikeVOS, List<UserGalleryFavoriteVO> userGalleryFavoriteVOS) {
        // 设置点赞信息
        setGalleryCollectionLike(userId, galleryCollectionVO, userGalleryLikeVOS);

        // 设置收藏信息
        setGalleryCollectionFavorite(userId, galleryCollectionVO, userGalleryFavoriteVOS);

        // 设置封面信息
        setGalleryCollectionCover(galleryCollectionVO);

        // 设置作品数量
        setGalleryTotal(userId, galleryCollectionVO);
    }

    private void setGalleryTotal(Long userId, GalleryCollectionVO galleryCollectionVO) {
        Long total;
        if (ObjectUtil.isNotNull(userId) && userId.equals(galleryCollectionVO.getUserId())) {
            total = galleryService.countGalleryNumByCId(galleryCollectionVO.getId());
        } else {
            total = galleryService.countPublishedGalleryNumByCId(galleryCollectionVO.getId());
        }
        galleryCollectionVO.setGalleryNum(Math.toIntExact(total));
    }

    private void setGalleryCollectionCover(GalleryCollectionVO galleryCollectionVO) {
        List<GalleryVO> galleries = galleryService.selectGalleryListByCId2Limit(galleryCollectionVO.getId(), 4);
        if (CollUtil.isNotEmpty(galleries)) {
            // 组装封面图
            List<String> covers = galleries.stream()
                    .map(GalleryVO::getPic)
                    .toList();
            galleryCollectionVO.setCovers(covers);
        } else {
            galleryCollectionVO.setCovers(Collections.emptyList());
        }
    }

    private void setGalleryCollectionFavorite(Long userId, GalleryCollectionVO galleryCollectionVO, List<UserGalleryFavoriteVO> userGalleryFavoriteVOS) {
        // 组装收藏信息
        List<UserGalleryFavoriteVO> favoriteList = userGalleryFavoriteVOS.parallelStream()
                .filter(favoriteVO -> favoriteVO.getPostId().equals(galleryCollectionVO.getId())
                        && favoriteVO.getType().equals(GalleryEnums.GalleryPageType.GALLERY_COLLECTION.getValue()))
                .collect(Collectors.toList());
        galleryCollectionVO.setFavoriteList(favoriteList);
        galleryCollectionVO.setFavoriteNum(favoriteList.size());
        galleryCollectionVO.setIsFavorite(Boolean.FALSE);

        if (ObjectUtil.isNotNull(userId)) {
            favoriteList.parallelStream()
                    .filter(it -> it.getUserId().equals(userId))
                    .findAny()
                    .ifPresent(it -> {
                        galleryCollectionVO.setIsFavorite(Boolean.TRUE);
                    });
        }
    }

    private void setGalleryCollectionLike(Long userId, GalleryCollectionVO galleryCollectionVO, List<UserGalleryLikeVO> userGalleryLikeVOS) {
        List<UserGalleryLikeVO> likeList = userGalleryLikeVOS.parallelStream()
                .filter(galleryLikeVO -> galleryLikeVO.getPostId().equals(galleryCollectionVO.getId())
                        && galleryLikeVO.getType().equals(GalleryEnums.GalleryPageType.GALLERY_COLLECTION.getValue()))
                .collect(Collectors.toList());
        galleryCollectionVO.setLikeList(likeList);
        galleryCollectionVO.setLikeNum(likeList.size());
        galleryCollectionVO.setIsLike(Boolean.FALSE);

        if (ObjectUtil.isNotNull(userId)) {
            likeList.parallelStream()
                    .filter(it -> it.getUserId().equals(userId))
                    .findAny()
                    .ifPresent(it -> {
                        galleryCollectionVO.setIsLike(Boolean.TRUE);
                    });
        }

    }

    @Override
    public GalleryCollectionVO getGalleryCollect(Long collectId) {
        GalleryCollection galleryCollection = this.getById(collectId);
        List<Long> tagIds = null;
        if (ObjectUtil.isNotNull(galleryCollection.getTagId())) {
            tagIds = List.of(galleryCollection.getTagId());
        }
        return getGalleryCollectionVO(galleryCollection, tagIds);
    }

    @NotNull
    private GalleryCollectionVO getGalleryCollectionVO(GalleryCollection galleryCollection, List<Long> tagIds) {
        GalleryCollectionVO galleryCollectionVO = getGalleryCollectionVO(galleryCollection);
        if (CollUtil.isNotEmpty(tagIds)) {
            List<PicTag> picTagList = Optional.ofNullable(picTagService.listByIds(tagIds)).orElse(Collections.emptyList());
            if (ObjectUtil.isNotNull(galleryCollection.getTagId())) {
                picTagList.parallelStream()
                        .filter(it -> it.getId().equals(galleryCollection.getTagId()))
                        .findAny()
                        .ifPresent(it -> {
                            galleryCollectionVO.setTag(it.getName());
                        });
            }
        }

        return galleryCollectionVO;
    }

    @NotNull
    private GalleryCollectionVO getGalleryCollectionVO(GalleryCollection galleryCollection) {
        GalleryCollectionVO galleryCollectionVO = new GalleryCollectionVO();
        BeanUtils.copyProperties(galleryCollection, galleryCollectionVO);
        // 查询作品
        // List<GalleryVO> galleries = galleryService.selectGalleryListByCId(galleryCollection.getId());
        List<GalleryVO> galleries = galleryService.selectGalleryListByCId2Limit(galleryCollection.getId(), 4);
        Long galleryNum = galleryService.countGalleryNumByCId(galleryCollection.getId());
        // galleryCollectionVO.setGalleryList(galleries);
        galleryCollectionVO.setGalleryNum(Math.toIntExact(galleryNum));
        if (StrUtil.isBlank(galleryCollection.getCover())) {
            // 组装封面图
            List<String> covers = galleries.stream()
                    // .limit(4)
                    .map(GalleryVO::getPic)
                    .toList();
            galleryCollectionVO.setCovers(covers);
        }
        return galleryCollectionVO;
    }

    @Override
    public boolean addGalleryCollect(GalleryCollectionVO collectionVO) {
        GalleryCollection galleryCollection = new GalleryCollection();
        BeanUtils.copyProperties(collectionVO, galleryCollection);
        if (this.checkGalleryCollectNameExist(galleryCollection)) {
            throw new ApiException("新增合集'" + galleryCollection.getTitle() + "'失败，名称已存在");
        }
        return this.save(galleryCollection);
    }

    @Override
    public boolean updateGalleryCollectById(GalleryCollectionVO collectionVO) {
        GalleryCollection galleryCollection = new GalleryCollection();
        BeanUtils.copyProperties(collectionVO, galleryCollection);
        if (this.checkGalleryCollectNameExist(galleryCollection)) {
            throw new ApiException("编辑合集'" + galleryCollection.getTitle() + "'失败，名称已存在");
        }
        return this.updateById(galleryCollection);
    }

    @Override
    public boolean setCollectionTopStatus(UpdateStatusDTO statusDTO) {
        GalleryCollection galleryCollection = this.getById(statusDTO.getId());
        if (!galleryCollection.getUserId().equals(SecurityUtil.getUserId())) {
            throw new ApiException(ResultCode.USER_ERROR_A0300);
        }
        galleryCollection.setIsTop(statusDTO.getStatus());
        return this.updateById(galleryCollection);
    }

    @Override
    public boolean checkGalleryCollectNameExist(GalleryCollection galleryCollection) {
        LambdaQueryWrapper<GalleryCollection> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(galleryCollection.getId()), GalleryCollection::getId, galleryCollection.getId());
        queryWrapper.eq(GalleryCollection::getTitle, galleryCollection.getTitle());
        queryWrapper.eq(GalleryCollection::getUserId, galleryCollection.getUserId());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeGalleryCollectionByIds(List<Long> ids) {
        Long userId = SecurityUtil.getUserId();
        List<GalleryCollection> galleryList = this.listByIds(ids);

        boolean flag = false;
        for (GalleryCollection collection : galleryList) {
            if (!collection.getUserId().equals(userId)) {
                flag = true;
                break;
            }
        }

        if (flag) {
            throw new ApiException(ResultCode.USER_ERROR_A0300);
        }
        return this.removeBatchByIds(ids);
    }
}




