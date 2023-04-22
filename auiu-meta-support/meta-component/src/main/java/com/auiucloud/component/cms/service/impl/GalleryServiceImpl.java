package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.shaded.com.google.protobuf.Api;
import com.auiucloud.component.cms.domain.Gallery;
import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.component.cms.dto.JoinGalleryCollectionDTO;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.mapper.GalleryMapper;
import com.auiucloud.component.cms.service.IGalleryCollectionService;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.service.IPicTagService;
import com.auiucloud.component.cms.vo.GalleryVO;
import com.auiucloud.component.oss.service.ISysAttachmentService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.feign.IMemberProvider;
import com.auiucloud.ums.vo.MemberInfoVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【cms_gallery(作品表)】的数据库操作Service实现
 * @createDate 2023-04-16 20:56:41
 */
@Service
@RequiredArgsConstructor
public class GalleryServiceImpl extends ServiceImpl<GalleryMapper, Gallery>
        implements IGalleryService {

    @Lazy
    @Resource
    private ISysAttachmentService sysAttachmentService;
    private final IGalleryCollectionService galleryCollectionService;
    private final IMemberProvider memberProvider;
    private final IPicTagService picTagService;

    @Override
    public List<GalleryVO> selectGalleryListByCId(Long cId) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getCollectionId, cId);
        queryWrapper.orderByDesc(Gallery::getIsTop);
        queryWrapper.orderByDesc(Gallery::getSort);
        queryWrapper.orderByDesc(Gallery::getCreateTime);
        return getGalleryVOS(queryWrapper);
    }

    @Override
    public List<GalleryVO> selectGalleryListByCId2Limit(Long cId, Integer limit) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getCollectionId, cId);
        queryWrapper.orderByDesc(Gallery::getIsTop);
        queryWrapper.orderByDesc(Gallery::getSort);
        queryWrapper.orderByDesc(Gallery::getCreateTime);
        queryWrapper.last(limit != null, "limit " + limit);
        return getGalleryVOS(queryWrapper);
    }

    @Override
    public GalleryVO selectGalleryInfoById(Long galleryId) {

        GalleryVO galleryVO = baseMapper.selectGalleryVOById(galleryId);
        // GalleryVO galleryVO = new GalleryVO();
        // BeanUtils.copyProperties(gallery, galleryVO);

        // 组装信息
        // 1.1 用户信息

        try {
            ApiResult<MemberInfoVO> getUserResult = memberProvider.getUserByUsername(galleryVO.getCreateBy());
            if (ObjectUtil.isNotNull(getUserResult) && getUserResult.successful() && ObjectUtil.isNotNull(getUserResult.getData())) {
                MemberInfoVO memberInfo = getUserResult.getData();
                galleryVO.setNickname(memberInfo.getNickname());
                galleryVO.setAvatar(memberInfo.getAvatar());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // todo 设置下载积分
        return galleryVO;
    }

    @Transactional
    @Override
    public ApiResult<?> upload(MultipartFile file, Long cId) {
        Map<String, Object> upload = sysAttachmentService.upload(file, 1001L);
        Gallery gallery = Gallery.builder()
                .uId(SecurityUtil.getUserId())
                .collectionId(cId)
                .pic((String) upload.getOrDefault("url", ""))
                .width((Integer) upload.getOrDefault("width", CommonConstant.STATUS_NORMAL_VALUE))
                .height((Integer) upload.getOrDefault("height", CommonConstant.STATUS_NORMAL_VALUE))
                .size((Long) upload.getOrDefault("size", CommonConstant.ROOT_NODE_ID))
                .type(GalleryEnums.GalleryType.WALLPAPER.getValue())
                .approvalStatus(GalleryEnums.GalleryApprovalStatus.AWAIT.getValue())
                .build();
        boolean result = this.save(gallery);
        if (result) {
            return ApiResult.data(gallery);
        }
        throw new ApiException(ResultCode.USER_ERROR_A0500);
    }

    @Override
    public PageUtils selectGalleryPage(Search search, Gallery gallery) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getUId, SecurityUtil.getUserId());
        queryWrapper.eq(ObjectUtil.isNotNull(gallery.getCollectionId()), Gallery::getCollectionId, gallery.getCollectionId());
        queryWrapper.orderByDesc(Gallery::getIsTop);
        queryWrapper.orderByDesc(Gallery::getSort);
        queryWrapper.orderByDesc(Gallery::getCreateTime);
        PageUtils.startPage(search);
        List<Gallery> list = this.list(queryWrapper);
        PageUtils pageUtils = new PageUtils(list);
        List<GalleryVO> collect = Optional.ofNullable(list).orElse(Collections.emptyList())
                .stream().map(it -> {
                    GalleryVO galleryVO = new GalleryVO();
                    BeanUtils.copyProperties(it, galleryVO);
                    return galleryVO;
                })
                .toList();
        pageUtils.setList(collect);
        return pageUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean joinGalleryCollection(JoinGalleryCollectionDTO joinGalleryCollectionDTO) {
        GalleryCollection collection = galleryCollectionService.getById(joinGalleryCollectionDTO.getCollectionId());
        if (!collection.getUId().equals(SecurityUtil.getUserId())) {
            throw new ApiException(ResultCode.USER_ERROR_A0300);
        }
        if (ObjectUtil.isNotNull(collection)) {
            List<Long> galleryIds = joinGalleryCollectionDTO.getGalleryIds();
            // 批量查询作品
            List<GalleryCollection> galleryCollections = Optional.ofNullable(galleryCollectionService.listByIds(galleryIds)).orElse(Collections.emptyList());
            List<Gallery> galleryList = galleryCollections.parallelStream()
                    .map(it -> {
                        Gallery build = Gallery.builder()
                                .collectionId(collection.getId())
                                .tagId(collection.getTagId())
                                .id(it.getId())
                                .build();
                        if (ObjectUtil.isNull(it.getTagId())) {
                            build.setTagId(collection.getTagId());
                        }
                        return build;
                    }).collect(Collectors.toList());
            return this.updateBatchById(galleryList);
        }
        return false;
    }

    @Override
    public boolean setGalleryTopStatus(UpdateStatusDTO statusDTO) {
        Gallery gallery = this.getById(statusDTO.getId());
        if(!gallery.getUId().equals(SecurityUtil.getUserId())) {
            throw new ApiException(ResultCode.USER_ERROR_A0300);
        }
        gallery.setIsTop(statusDTO.getStatus());
        return this.updateById(gallery);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeGalleryByIds(List<Long> ids) {
        Long userId = SecurityUtil.getUserId();
        List<Gallery> galleryList = this.listByIds(ids);

        boolean flag = false;
        for (Gallery gallery : galleryList) {
            if (!gallery.getUId().equals(userId)) {
                flag = true;
                break;
            }
        }

        if (flag) {
            throw new ApiException(ResultCode.USER_ERROR_A0300);
        }
        return this.removeBatchByIds(ids);
    }

    @NotNull
    private List<GalleryVO> getGalleryVOS(LambdaQueryWrapper<Gallery> queryWrapper) {
        List<Gallery> galleryList = Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
        return galleryList.stream()
                .map(it -> {
                    GalleryVO galleryVO = new GalleryVO();
                    BeanUtils.copyProperties(it, galleryVO);
                    return galleryVO;
                })
                .toList();
    }
}




