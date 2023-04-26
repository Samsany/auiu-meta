package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.component.cms.domain.PicTag;
import com.auiucloud.component.cms.mapper.GalleryCollectionMapper;
import com.auiucloud.component.cms.service.IGalleryCollectionService;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.service.IPicTagService;
import com.auiucloud.component.cms.vo.GalleryCollectionVO;
import com.auiucloud.component.cms.vo.GalleryVO;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author dries
 * @description 针对表【cms_gallery_collection(作品合集表)】的数据库操作Service实现
 * @createDate 2023-04-16 20:56:41
 */
@Service
@RequiredArgsConstructor
public class GalleryCollectionServiceImpl extends ServiceImpl<GalleryCollectionMapper, GalleryCollection>
        implements IGalleryCollectionService {

    @Lazy
    @Resource
    private IGalleryService galleryService;

    private final IPicTagService picTagService;

    @Override
    public List<GalleryCollection> selectUserCollectionApiList(Search search, GalleryCollection galleryCollection) {
        LambdaQueryWrapper<GalleryCollection> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GalleryCollection::getUId, SecurityUtil.getUserId());
        queryWrapper.orderByDesc(GalleryCollection::getIsTop);
        queryWrapper.orderByDesc(GalleryCollection::getSort);
        queryWrapper.orderByDesc(GalleryCollection::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public PageUtils selectUserCollectionApiPage(Search search, GalleryCollection galleryCollection) {
        LambdaQueryWrapper<GalleryCollection> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GalleryCollection::getUId, SecurityUtil.getUserId());
        queryWrapper.orderByDesc(GalleryCollection::getIsTop);
        queryWrapper.orderByDesc(GalleryCollection::getSort);
        queryWrapper.orderByDesc(GalleryCollection::getCreateTime);
        PageUtils.startPage(search);
        List<GalleryCollection> list = Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
        PageUtils pageUtils = new PageUtils(list);
        List<Long> tagIds = list.parallelStream().map(GalleryCollection::getTagId).filter(Objects::nonNull).toList();
        List<GalleryCollectionVO> galleryCollectionVOS = list.stream().map(it -> getGalleryCollectionVO(it, tagIds)).toList();
        pageUtils.setList(galleryCollectionVOS);
        return pageUtils;
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
        List<GalleryVO> galleries = galleryService.selectGalleryListByCId(galleryCollection.getId());
        galleryCollectionVO.setGalleryList(galleries);
        galleryCollectionVO.setGalleryNum(galleries.size());
        if (StrUtil.isBlank(galleryCollection.getCover())) {
            // 组装封面图
            List<String> covers = galleries.stream()
                    .limit(4)
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
        if (!galleryCollection.getUId().equals(SecurityUtil.getUserId())) {
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
        queryWrapper.eq(GalleryCollection::getUId, galleryCollection.getUId());

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
            if (!collection.getUId().equals(userId)) {
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




