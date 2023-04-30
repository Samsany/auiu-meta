package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.auiucloud.auth.dto.DouyinContentCheckDTO;
import com.auiucloud.auth.feign.IDouyinProvider;
import com.auiucloud.component.cms.domain.Gallery;
import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.component.cms.domain.UserGalleryLike;
import com.auiucloud.component.cms.dto.JoinGalleryCollectionDTO;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.mapper.GalleryMapper;
import com.auiucloud.component.cms.service.IGalleryCollectionService;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.service.IPicTagService;
import com.auiucloud.component.cms.service.IUserGalleryLikeService;
import com.auiucloud.component.cms.vo.GalleryPublishVO;
import com.auiucloud.component.cms.vo.GalleryVO;
import com.auiucloud.component.cms.vo.UserGalleryLikeVO;
import com.auiucloud.component.oss.service.ISysAttachmentService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.common.utils.http.RequestHolder;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.feign.IMemberProvider;
import com.auiucloud.ums.vo.MemberInfoVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【cms_gallery(作品表)】的数据库操作Service实现
 * @createDate 2023-04-16 20:56:41
 */
@Slf4j
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
    private final IUserGalleryLikeService userGalleryLikeService;
    private final IDouyinProvider douyinProvider;

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
    public List<GalleryVO> selectGalleryReCommendList() {
        return baseMapper.selectGalleryReCommendList();
    }

    @Override
    public PageUtils selectCommonGalleryPage(Search search, Gallery gallery) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();

        // 查询全部
        if (gallery.getTagId().equals(GalleryEnums.GalleryTagType.ALL.getValue())) {
            queryWrapper.orderByDesc(Gallery::getCreateTime);
        } else if (gallery.getTagId().equals(GalleryEnums.GalleryTagType.ALL.getValue())) {
            // todo 查询合集
        } else {
            queryWrapper.eq(Gallery::getTagId, gallery.getTagId());
            // queryWrapper.orderByDesc(Gallery::getDownloadTimes);
            // queryWrapper.orderByDesc(Gallery::getSort);
            queryWrapper.orderByDesc(Gallery::getCreateTime);
        }

        PageUtils.startPage(search);
        List<Gallery> list = Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
        PageUtils pageUtils = new PageUtils(list);

        List<Long> userIds = list.parallelStream().map(Gallery::getUId).toList();
        List<MemberInfoVO> memberInfoVOS = getMemberInfoVOS(userIds);

        List<Long> galleryIds = list.parallelStream().map(Gallery::getId).toList();
        List<UserGalleryLikeVO> userGalleryLikeVOS = new ArrayList<>();
        if (CollUtil.isNotEmpty(galleryIds)) {
            userGalleryLikeVOS.addAll(userGalleryLikeService.selectGalleryLikeVOListByGalleryIds(galleryIds));
        }

        List<GalleryVO> collect = list
                .stream().map(it -> {
                    GalleryVO galleryVO = new GalleryVO();
                    BeanUtils.copyProperties(it, galleryVO);

                    buildUserGalleryLikeVO(galleryVO, userGalleryLikeVOS);
                    buildGalleryUserVO(galleryVO, memberInfoVOS);
                    return galleryVO;
                })
                .toList();
        pageUtils.setList(collect);
        return pageUtils;
    }

    @Override
    public GalleryVO selectGalleryInfoById(Long galleryId) {
        GalleryVO galleryVO = baseMapper.selectGalleryVOById(galleryId);

        // 组装点赞信息
        List<UserGalleryLikeVO> userGalleryLikeVOS = userGalleryLikeService.selectGalleryLikeVOListByGalleryId(galleryId);
        buildUserGalleryLikeVO(galleryVO, userGalleryLikeVOS);
        // todo 组装下载记录
        return getGalleryVO(galleryVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApiResult<?> upload(MultipartFile file, Long cId) {
        try {
            Map<String, Object> upload = sysAttachmentService.upload(file, 1001L, true, true);
            Gallery gallery = Gallery.builder()
                    .uId(SecurityUtil.getUserId())
                    .collectionId(cId)
                    .pic((String) upload.getOrDefault("url", ""))
                    .thumbUrl((String) upload.getOrDefault("thumbUrl", ""))
                    .width((Integer) upload.getOrDefault("width", CommonConstant.STATUS_NORMAL_VALUE))
                    .height((Integer) upload.getOrDefault("height", CommonConstant.STATUS_NORMAL_VALUE))
                    .size((Long) upload.getOrDefault("size", CommonConstant.ROOT_NODE_ID))
                    .type(GalleryEnums.GalleryType.WALLPAPER.getValue())
                    .isPublished(GalleryEnums.GalleryIsPublished.NO.getValue())
                    .joinCollectionTime(ObjectUtil.isNotNull(cId) ? LocalDateTime.now() : null)
                    .build();
            boolean result = this.save(gallery);
            if (result) {
                return ApiResult.data(gallery);
            }
            return ApiResult.fail(ResultCode.USER_ERROR_A0500);
        } catch (Exception e) {
            throw new ApiException(ResultCode.USER_ERROR_A0500);
        }
    }

    @Override
    public PageUtils selectGalleryPage(Search search, Gallery gallery) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getUId, SecurityUtil.getUserId());
        queryWrapper.eq(ObjectUtil.isNotNull(gallery.getCollectionId()), Gallery::getCollectionId, gallery.getCollectionId());
        queryWrapper.orderByDesc(Gallery::getIsTop);
        queryWrapper.orderByDesc(Gallery::getSort);
        queryWrapper.orderByDesc(ObjectUtil.isNotNull(gallery.getCollectionId()), Gallery::getJoinCollectionTime);
        queryWrapper.orderByDesc(Gallery::getCreateTime);
        IPage<Gallery> page = this.page(PageUtils.getPage(search), queryWrapper);
        page.convert(it -> {
            GalleryVO galleryVO = new GalleryVO();
            BeanUtils.copyProperties(it, galleryVO);
            return galleryVO;
        });
        return new PageUtils(page);
    }

    @Override
    public PageUtils galleryNoCollectionPage(Search search, Gallery gallery) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getUId, SecurityUtil.getUserId());
        queryWrapper.isNull(Gallery::getCollectionId);
        queryWrapper.orderByDesc(Gallery::getIsTop);
        queryWrapper.orderByDesc(Gallery::getSort);
        queryWrapper.orderByDesc(Gallery::getCreateTime);
        IPage<Gallery> page = this.page(PageUtils.getPage(search), queryWrapper);
        page.convert(it -> {
            GalleryVO galleryVO = new GalleryVO();
            BeanUtils.copyProperties(it, galleryVO);
            return galleryVO;
        });
        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApiResult<?> publishGallery(GalleryPublishVO galleryVO) {
        Gallery gallery = this.getById(galleryVO.getId());
        if (!gallery.getUId().equals(SecurityUtil.getUserId())) {
            throw new ApiException(ResultCode.USER_ERROR_A0300);
        }
        if (gallery.getIsPublished().equals(CommonConstant.STATUS_DISABLE_VALUE)) {
            return ApiResult.fail("作品已发布，请勿重复操作");
        }
        if (ObjectUtil.isNotNull(gallery.getApprovalStatus()) && gallery.getApprovalStatus().equals(GalleryEnums.GalleryApprovalStatus.AWAIT.getValue())) {
            return ApiResult.fail("作品已提交，等待审核中");
        }

        try {
            String appId = RequestHolder.getHttpServletRequestHeader("appId");
            Integer loginType = SecurityUtil.getUser().getLoginType();
            if (loginType.equals(AuthenticationIdentityEnum.DOUYIN_APPLET.getValue())) {
                ApiResult<List<Integer>> listApiResult = douyinProvider.checkTextList(DouyinContentCheckDTO.builder()
                        .appId(appId)
                        .contents(List.of(
                                galleryVO.getTitle(),
                                galleryVO.getPrompt(),
                                galleryVO.getRemark()
                        ))
                        .build());
                if (listApiResult.successful()) {
                    if (listApiResult.getData().size() > 0) {
                        return ApiResult.fail("内容存在违规信息, 请重新输入");
                    }
                } else {
                    return listApiResult;
                }
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }

        LambdaUpdateWrapper<Gallery> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(Gallery::getTitle, galleryVO.getTitle());
        updateWrapper.set(Gallery::getRemark, galleryVO.getRemark());
        updateWrapper.set(Gallery::getPrompt, galleryVO.getPrompt());
        updateWrapper.set(Gallery::getTagId, galleryVO.getTagId());
        updateWrapper.set(Gallery::getPublishedTime, new Date());
        updateWrapper.set(Gallery::getApprovalStatus, GalleryEnums.GalleryApprovalStatus.AWAIT.getValue());
        updateWrapper.eq(Gallery::getId, gallery.getId());
        return ApiResult.condition(this.update(updateWrapper));
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
            List<Gallery> galleryCollections = Optional.ofNullable(this.listByIds(galleryIds)).orElse(Collections.emptyList());
            List<Gallery> galleryList = galleryCollections.parallelStream()
                    .map(it -> Gallery.builder()
                            .collectionId(collection.getId())
                            .tagId(collection.getTagId())
                            .id(it.getId())
                            .joinCollectionTime(LocalDateTime.now())
                            .build()).collect(Collectors.toList());
            return this.updateBatchById(galleryList);
        }
        return false;
    }

    @Override
    public boolean setGalleryTopStatus(UpdateStatusDTO statusDTO) {
        Gallery gallery = this.getById(statusDTO.getId());
        if (!gallery.getUId().equals(SecurityUtil.getUserId())) {
            throw new ApiException(ResultCode.USER_ERROR_A0300);
        }
        gallery.setIsTop(statusDTO.getStatus());
        return this.updateById(gallery);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean likeGallery(Long postId, Integer type) {
        Long userId = SecurityUtil.getUserId();
        UserGalleryLike userGalleryLike = userGalleryLikeService.selectGalleryLikeByUserId2GalleryId(userId, postId, type);
        if (ObjectUtil.isNull(userGalleryLike)) {
            userGalleryLike = UserGalleryLike.builder()
                    .uId(userId)
                    .postId(postId)
                    .type(type)
                    .status(CommonConstant.STATUS_NORMAL_VALUE)
                    .build();
        } else {
            if (userGalleryLike.getStatus().equals(CommonConstant.STATUS_NORMAL_VALUE)) {
                userGalleryLike.setStatus(CommonConstant.STATUS_DISABLE_VALUE);
            } else {
                userGalleryLike.setStatus(CommonConstant.STATUS_NORMAL_VALUE);
            }
        }
        return userGalleryLikeService.saveOrUpdate(userGalleryLike);
    }

    @Override
    public Long countUserGalleryByUId(Long userId) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getUId, userId);
        queryWrapper.eq(Gallery::getIsPublished, GalleryEnums.GalleryIsPublished.YES.getValue());
        return this.count(queryWrapper);
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

    // 组装作品信息
    @Nullable
    private GalleryVO getGalleryVO(GalleryVO galleryVO) {
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
        return galleryVO;
    }

    // 组装作品用户信息
    private void buildGalleryUserVO(GalleryVO galleryVO, List<MemberInfoVO> memberInfoVOS) {
        if (CollUtil.isNotEmpty(memberInfoVOS)) {
            memberInfoVOS.parallelStream()
                    .filter(it -> it.getUserId().equals(galleryVO.getUId()))
                    .findAny()
                    .ifPresent(memberInfo -> {
                        galleryVO.setNickname(memberInfo.getNickname());
                        galleryVO.setAvatar(memberInfo.getAvatar());
                    });
        }
    }

    private void buildUserGalleryLikeVO(GalleryVO galleryVO, List<UserGalleryLikeVO> userGalleryLikeVOS) {
        List<UserGalleryLikeVO> likeList = userGalleryLikeVOS.parallelStream()
                .filter(galleryLikeVO -> galleryLikeVO.getPostId().equals(galleryVO.getId())
                        && galleryLikeVO.getType().equals(GalleryEnums.GalleryLikeType.GALLERY.getValue()))
                .collect(Collectors.toList());
        galleryVO.setLikeList(likeList);
        galleryVO.setLikeNum(likeList.size());

        // 组装收藏信息
        List<UserGalleryLikeVO> favoriteList = userGalleryLikeVOS.parallelStream()
                .filter(galleryLikeVO -> galleryLikeVO.getPostId().equals(galleryVO.getId())
                        && galleryLikeVO.getType().equals(GalleryEnums.GalleryLikeType.GALLERY_COLLECTION.getValue()))
                .collect(Collectors.toList());
        galleryVO.setFavoriteList(favoriteList);
        galleryVO.setFavoriteNum(favoriteList.size());
        try {
            Long userId = SecurityUtil.getUserId();
            List<Long> likeUserIds = likeList.parallelStream().map(UserGalleryLikeVO::getUId).toList();
            List<Long> favoriteUserIds = favoriteList.parallelStream().map(UserGalleryLikeVO::getUId).toList();

            galleryVO.setIsLike(likeUserIds.contains(userId));
            galleryVO.setIsFavorite(favoriteUserIds.contains(userId));
        } catch (Exception ignored) {
        }
    }

    // 获取用户信息
    @NotNull
    private List<MemberInfoVO> getMemberInfoVOS(List<Long> userIds) {
        List<MemberInfoVO> memberInfoVOS = new ArrayList<>();
        try {
            ApiResult<List<MemberInfoVO>> getUserResult = memberProvider.getUserListByIds(userIds);
            if (ObjectUtil.isNotNull(getUserResult) && getUserResult.successful() && CollUtil.isNotEmpty(getUserResult.getData())) {
                List<MemberInfoVO> memberInfos = getUserResult.getData();
                memberInfoVOS.addAll(memberInfos);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return memberInfoVOS;
    }
}




