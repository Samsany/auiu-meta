package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.component.cms.domain.*;
import com.auiucloud.component.cms.dto.GalleryUpdateDTO;
import com.auiucloud.component.cms.dto.JoinGalleryCollectionDTO;
import com.auiucloud.component.cms.dto.SaveAiDrawGalleryDTO;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.mapper.GalleryMapper;
import com.auiucloud.component.cms.service.*;
import com.auiucloud.component.cms.vo.*;
import com.auiucloud.component.oss.service.ISysAttachmentService;
import com.auiucloud.component.sd.domain.AiDrawImgVO;
import com.auiucloud.component.sd.domain.SdTxt2ImgParams;
import com.auiucloud.component.sd.vo.SdDrawSaveVO;
import com.auiucloud.component.sysconfig.domain.SysAttachment;
import com.auiucloud.component.sysconfig.domain.UserConfigProperties;
import com.auiucloud.component.sysconfig.service.ISysConfigService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.FileUtil;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.common.utils.http.RequestHolder;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.douyin.config.AppletsConfiguration;
import com.auiucloud.core.douyin.service.DouyinAppletsService;
import com.auiucloud.ums.dto.UserBrokerageChangeDTO;
import com.auiucloud.ums.dto.UserPointChangeDTO;
import com.auiucloud.ums.enums.UserBrokerageEnums;
import com.auiucloud.ums.enums.UserPointEnums;
import com.auiucloud.ums.feign.IMemberProvider;
import com.auiucloud.ums.vo.MemberInfoVO;
import com.auiucloud.ums.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
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

    private final IUserGalleryDownloadService galleryDownloadRecordService;
    private final IGalleryCollectionService galleryCollectionService;
    private final IUserGalleryLikeService userGalleryLikeService;
    private final IUserGalleryCollectionService userGalleryCollectionService;
    private final IUserGalleryDownloadService userGalleryDownloadService;
    private final ISysConfigService sysConfigService;
    private final IGalleryAppealService galleryAppealService;
    private final IGalleryReviewService galleryReviewService;

    @Lazy
    @Resource
    private ISysAttachmentService sysAttachmentService;
    @Resource
    private IMemberProvider memberProvider;

    @Override
    public List<GalleryVO> listGalleryVOByGIds(List<Long> galleryIds) {
        if (CollUtil.isNotEmpty(galleryIds)) {
            List<GalleryVO> galleryVOS = Optional.ofNullable(baseMapper.selectGalleryVOListByIds(galleryIds)).orElse(Collections.emptyList());
            List<Long> gIds = galleryVOS.parallelStream().map(GalleryVO::getId).toList();
            List<UserGalleryLikeVO> userGalleryLikeVOS = userGalleryLikeService.selectGalleryLikeVOListByGIds(gIds);
            List<UserGalleryFavoriteVO> userGalleryFavoriteVOS = userGalleryCollectionService.selectGalleryFavoriteVOListByGalleryIds(gIds);
            galleryVOS.parallelStream().forEach(it -> buildUserGalleryAdditionalVO(null, it, userGalleryLikeVOS, userGalleryFavoriteVOS));
            return galleryVOS;
        }

        return Collections.emptyList();
    }

    @Override
    public List<GalleryVO> selectGalleryListByCId(Long cateId) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getCollectionId, cateId);
        queryWrapper.orderByDesc(Gallery::getIsTop);
        queryWrapper.orderByDesc(Gallery::getSort);
        queryWrapper.orderByDesc(Gallery::getCreateTime);
        queryWrapper.orderByDesc(Gallery::getId);
        return getGalleryVOS(queryWrapper);
    }

    @Override
    public List<GalleryVO> selectGalleryListByCId2Limit(Long cateId, Integer limit) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getCollectionId, cateId);
        queryWrapper.orderByDesc(Gallery::getIsTop);
        queryWrapper.orderByDesc(Gallery::getSort);
        queryWrapper.orderByDesc(Gallery::getCreateTime);
        queryWrapper.orderByDesc(Gallery::getId);
        queryWrapper.last(limit != null, "limit " + limit);
        return getGalleryVOS(queryWrapper);
    }

    @Override
    public List<GalleryVO> selectGalleryReCommendList() {
        return baseMapper.selectGalleryReCommendList();
    }

    @Override
    public PageUtils selectSquareGalleryVOPage(Search search, Gallery gallery) {

        if (gallery.getTagId().equals(GalleryEnums.GalleryTagType.COLLECTION.getValue())) {
            // todo 查询合集
            return new PageUtils(search);
        } else {
            // 查询全部
            if (gallery.getTagId().equals(GalleryEnums.GalleryTagType.ALL.getValue())) {
                search.setOrder("cg.sort desc, cg.create_time desc");
            } else {
                search.setOrder("cg.sort desc, cg.download_times desc, cg.create_time desc");
            }

            Long total = baseMapper.countSquareGalleryVOTotal(search, gallery);
            Search s = PageUtils.buildPage(search, Math.toIntExact(total));
            List<GalleryVO> galleryVOS = Optional.ofNullable(baseMapper.selectSquareGalleryVOPage(s, gallery)).orElse(Collections.emptyList());

            List<Long> galleryIds = galleryVOS.parallelStream().map(GalleryVO::getId).collect(Collectors.toList());
            List<UserGalleryLikeVO> userGalleryLikeVOS = userGalleryLikeService.selectGalleryLikeVOListByGIds(galleryIds);
            List<UserGalleryFavoriteVO> userGalleryFavoriteVOs = userGalleryCollectionService.selectGalleryFavoriteVOListByGalleryIds(galleryIds);

            Long userId = SecurityUtil.getUserIdOrDefault();
            galleryVOS.parallelStream().forEach(it -> buildUserGalleryAdditionalVO(userId, it, userGalleryLikeVOS, userGalleryFavoriteVOs));

            return new PageUtils(total, search, galleryVOS);
        }
    }

    @Override
    public PageUtils selectGalleryUserHomePage(Search search, Gallery gallery) {
        search.setOrder("cg.is_top desc, cg.download_times desc, cg.create_time desc");
        Long userId = SecurityUtil.getUserIdOrDefault();
        Long total = baseMapper.countUserHomeGalleryVONum(userId, search, gallery);
        Search s = PageUtils.buildPage(search, Math.toIntExact(total));
        List<GalleryVO> galleryVOS = Optional.ofNullable(baseMapper.selectUserHomeGalleryVOPage(userId, s, gallery)).orElse(Collections.emptyList());

        List<Long> galleryIds = galleryVOS.parallelStream().map(GalleryVO::getId).collect(Collectors.toList());
        List<UserGalleryLikeVO> userGalleryLikeVOS = userGalleryLikeService.selectGalleryLikeVOListByGIds(galleryIds);
        List<UserGalleryFavoriteVO> userGalleryFavoriteVOs = userGalleryCollectionService.selectGalleryFavoriteVOListByGalleryIds(galleryIds);
        galleryVOS.parallelStream().forEach(it -> buildUserGalleryAdditionalVO(userId, it, userGalleryLikeVOS, userGalleryFavoriteVOs));

        return new PageUtils(total, search, galleryVOS);
    }

    @Override
    public PageUtils selectMyLikeGalleryPage(Search search) {
        // 查询我的点赞列表
        LambdaQueryWrapper<UserGalleryLike> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserGalleryLike::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        queryWrapper.eq(UserGalleryLike::getUserId, SecurityUtil.getUserId());
        IPage<UserGalleryLike> page = userGalleryLikeService.page(PageUtils.getPage(search), queryWrapper);
        if (page.getRecords().size() > 0) {
            // 分别取出作品 和 合集
            Map<Integer, List<UserGalleryLike>> map = page.getRecords()
                    .parallelStream()
                    .filter(it -> it.getType() != null)
                    .collect(Collectors.groupingBy(UserGalleryLike::getType));

            // 作品
            List<UserGalleryLike> userGalleryLikes = Optional.ofNullable(map.get(GalleryEnums.GalleryPageType.GALLERY.getValue())).orElse(Collections.emptyList());
            List<Long> galleryIds = userGalleryLikes.parallelStream().map(UserGalleryLike::getPostId).toList();
            List<GalleryVO> galleryList = this.listGalleryVOByGIds(galleryIds);

            // 合集
            List<UserGalleryLike> userGalleryCollectionLikes = Optional.ofNullable(map.get(GalleryEnums.GalleryPageType.GALLERY_COLLECTION.getValue())).orElse(Collections.emptyList());
            List<Long> cIds = userGalleryCollectionLikes.parallelStream().map(UserGalleryLike::getPostId).toList();
            List<GalleryCollectionVO> galleryCollections = galleryCollectionService.listGalleryCollectionVOByCIds(cIds);

            page.convert(it -> {
                UserGallerySimpleVO build = UserGallerySimpleVO.builder()
                        .id(it.getId())
                        .userId(it.getUserId())
                        .postId(it.getPostId())
                        .status(it.getStatus())
                        .type(it.getType())
                        .createTime(it.getCreateTime())
                        .build();
                // 组装额外的信息
                buildSimpleUserGalleryAdditional(build, galleryList, galleryCollections);
                return build;
            });
        }

        return new PageUtils(page);
    }

    @Override
    public PageUtils selectMyFavoriteGalleryPage(Search search) {
        // 查询我的点赞列表
        LambdaQueryWrapper<UserGalleryCollection> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserGalleryCollection::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        queryWrapper.eq(UserGalleryCollection::getUserId, SecurityUtil.getUserId());
        IPage<UserGalleryCollection> page = userGalleryCollectionService.page(PageUtils.getPage(search), queryWrapper);
        if (page.getRecords().size() > 0) {
            // 分别取出作品 和 合集
            Map<Integer, List<UserGalleryCollection>> map = page.getRecords().parallelStream()
                    .filter(it -> it.getType() != null)
                    .collect(Collectors.groupingBy(UserGalleryCollection::getType));

            // 作品
            List<UserGalleryCollection> userGalleryLikes = Optional.ofNullable(map.get(GalleryEnums.GalleryPageType.GALLERY.getValue())).orElse(Collections.emptyList());
            List<Long> galleryIds = userGalleryLikes.parallelStream().map(UserGalleryCollection::getPostId).toList();
            List<GalleryVO> galleryList = this.listGalleryVOByGIds(galleryIds);

            // 合集
            List<UserGalleryCollection> userGalleryCollectionLikes = Optional.ofNullable(map.get(GalleryEnums.GalleryPageType.GALLERY_COLLECTION.getValue())).orElse(Collections.emptyList());
            List<Long> cIds = userGalleryCollectionLikes.parallelStream().map(UserGalleryCollection::getPostId).toList();
            List<GalleryCollectionVO> galleryCollections = galleryCollectionService.listGalleryCollectionVOByCIds(cIds);

            page.convert(it -> {
                UserGallerySimpleVO build = UserGallerySimpleVO.builder()
                        .id(it.getId())
                        .userId(it.getUserId())
                        .postId(it.getPostId())
                        .status(it.getStatus())
                        .type(it.getType())
                        .createTime(it.getCreateTime())
                        .build();
                // 组装额外的信息
                buildSimpleUserGalleryAdditional(build, galleryList, galleryCollections);
                return build;
            });
        }

        return new PageUtils(page);
    }

    @Override
    public PageUtils selectMyDownloadGalleryPage(Search search) {
        // 查询我的下载列表
        LambdaQueryWrapper<UserGalleryDownload> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserGalleryDownload::getUserId, SecurityUtil.getUserId());
        IPage<UserGalleryDownload> page = userGalleryDownloadService.page(PageUtils.getPage(search), queryWrapper);
        if (page.getRecords().size() > 0) {
            List<Long> gIds = page.getRecords()
                    .stream()
                    .map(UserGalleryDownload::getGalleryId)
                    .toList();

            List<GalleryVO> galleryList = this.listGalleryVOByGIds(gIds);
            page.convert(it -> {
                UserGallerySimpleVO build = UserGallerySimpleVO.builder()
                        .id(it.getId())
                        .userId(it.getUserId())
                        .postId(it.getGalleryId())
                        .type(GalleryEnums.GalleryPageType.GALLERY.getValue())
                        .createTime(it.getCreateTime())
                        .build();
                // 组装额外的信息
                buildSimpleUserGalleryAdditional(build, galleryList);
                return build;
            });
        }

        return new PageUtils(page);
    }

    @Override
    public GalleryVO selectGalleryInfoById(Long galleryId) {
        GalleryVO galleryVO = baseMapper.selectGalleryVOById(galleryId);
        return getGalleryVO(galleryVO);
    }

    @Override
    public PageUtils selectGalleryPage(Search search, Gallery gallery) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getUserId, gallery.getUserId());
        queryWrapper.eq(ObjectUtil.isNotNull(gallery.getCollectionId()) && !Objects.equals(gallery.getCollectionId(), CommonConstant.ROOT_NODE_ID), Gallery::getCollectionId, gallery.getCollectionId());
        queryWrapper.orderByDesc(Gallery::getSort);
        queryWrapper.orderByDesc(Gallery::getCreateTime);
        queryWrapper.orderByDesc(Gallery::getId);
        IPage<Gallery> page = this.page(PageUtils.getPage(search), queryWrapper);
        page.convert(it -> {
            GalleryVO galleryVO = new GalleryVO();
            BeanUtils.copyProperties(it, galleryVO);
            return galleryVO;
        });
        return new PageUtils(page);
    }

    @Override
    public PageUtils selectGalleryReviewPage(Search search, Gallery gallery) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), Gallery::getTitle, search.getKeyword());
        queryWrapper.eq(ObjectUtil.isNotNull(gallery.getUserId()), Gallery::getUserId, gallery.getUserId());
        if (!search.getStatus().equals(CommonConstant.SYSTEM_STATUS_VALUE)) {
            queryWrapper.eq(Gallery::getApprovalStatus, search.getStatus());
        } else {
            queryWrapper.isNotNull(Gallery::getApprovalStatus);
        }
        IPage<Gallery> page = this.page(PageUtils.getPage(search), queryWrapper);
        page.convert(it -> {
            GalleryVO galleryVO = new GalleryVO();
            BeanUtils.copyProperties(it, galleryVO);
            try {
                UserInfoVO userInfo = memberProvider.getSimpleUserById(it.getUserId()).getData();
                galleryVO.setNickname(userInfo.getNickname());
                galleryVO.setAvatar(userInfo.getAvatar());
            } catch (Exception e) {
                log.error("获取创作者信息异常，{}", e.getMessage());
            }
            return galleryVO;
        });
        return new PageUtils(page);
    }

    @Override
    public PageUtils selectUserGalleryPage(Search search, Gallery gallery) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), Gallery::getTitle, search.getKeyword());
        queryWrapper.eq(Gallery::getUserId, gallery.getUserId());
        queryWrapper.eq(ObjectUtil.isNotNull(gallery.getCollectionId()) &&
                !gallery.getCollectionId().equals(CommonConstant.ROOT_NODE_ID), Gallery::getCollectionId, gallery.getCollectionId());
        queryWrapper.orderByDesc(Gallery::getIsTop);
        queryWrapper.orderByDesc(Gallery::getSort);
        queryWrapper.orderByDesc(ObjectUtil.isNotNull(gallery.getCollectionId()), Gallery::getJoinCollectionTime);
        queryWrapper.orderByDesc(Gallery::getCreateTime);
        queryWrapper.orderByDesc(Gallery::getId);
        IPage<Gallery> page = this.page(PageUtils.getPage(search), queryWrapper);
        page.convert(it -> {
            GalleryVO galleryVO = new GalleryVO();
            BeanUtils.copyProperties(it, galleryVO);
            try {
                UserInfoVO userInfo = memberProvider.getSimpleUserById(it.getUserId()).getData();
                galleryVO.setNickname(userInfo.getNickname());
                galleryVO.setAvatar(userInfo.getAvatar());
            } catch (Exception e) {
                log.error("获取创作者信息异常，{}", e.getMessage());
            }
            return galleryVO;
        });
        return new PageUtils(page);
    }

    @Override
    public PageUtils galleryNoCollectionPage(Search search, Gallery gallery) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getUserId, SecurityUtil.getUserId());
        queryWrapper.isNull(Gallery::getCollectionId);
        queryWrapper.ne(Gallery::getApprovalStatus, GalleryEnums.GalleryApprovalStatus.REJECT.getValue());
        queryWrapper.orderByDesc(Gallery::getIsTop);
        queryWrapper.orderByDesc(Gallery::getSort);
        queryWrapper.orderByDesc(Gallery::getCreateTime);
        queryWrapper.orderByDesc(Gallery::getId);
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
    public ApiResult<?> upload(MultipartFile file, Long cateId) {
        try {
            UserConfigProperties userConfigProperties = sysConfigService.getUserConfigProperties();
            Map<String, Object> upload = sysAttachmentService.upload(file, 1001L, true, true);
            Integer width = (Integer) upload.get("width");
            Integer height = (Integer) upload.get("height");
            Gallery gallery = Gallery.builder()
                    .userId(SecurityUtil.getUserId())
                    .collectionId(cateId)
                    .pic((String) upload.getOrDefault("url", ""))
                    .thumbUrl((String) upload.getOrDefault("thumbUrl", ""))
                    .width((Integer) upload.getOrDefault("width", CommonConstant.STATUS_NORMAL_VALUE))
                    .height((Integer) upload.getOrDefault("height", CommonConstant.STATUS_NORMAL_VALUE))
                    .size((Long) upload.getOrDefault("size", CommonConstant.ROOT_NODE_ID))
                    .width(width)
                    .height(height)
                    .type(GalleryEnums.GalleryType.WALLPAPER.getValue())
                    .isPublished(GalleryEnums.GalleryIsPublished.NO.getValue())
                    .approvalStatus(GalleryEnums.GalleryApprovalStatus.AWAIT.getValue())
                    .joinCollectionTime(ObjectUtil.isNotNull(cateId) ? LocalDateTime.now() : null)
                    .downloadIntegral(userConfigProperties.getDefaultDownloadIntegral())
                    .build();
            boolean result = this.save(gallery);
            if (result) {
                // 插入审核记录
                GalleryReview galleryReview = GalleryReview.builder()
                        .galleryId(gallery.getId())
                        .userId(gallery.getUserId())
                        .status(GalleryEnums.GalleryApprovalStatus.AWAIT.getValue())
                        .build();
                galleryReviewService.save(galleryReview);
                return ApiResult.data(gallery);
            }
            return ApiResult.fail(ResultCode.USER_ERROR_A0500);
        } catch (Exception e) {
            throw new ApiException(ResultCode.USER_ERROR_A0500);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean uploadGalleryBatch(GalleryUploadBatchVO vo) {
        // 获取默认下载积分
        UserConfigProperties userConfigProperties = sysConfigService.getUserConfigProperties();
        List<Gallery> galleryList = new ArrayList<>();
        // 获取图片
        for (SysAttachment image : vo.getImages()) {
            Gallery build = Gallery.builder()
                    .userId(vo.getUserId())
                    .title(vo.getTitle())
                    .collectionId(vo.getCollectionId())
                    .tagId(vo.getTagId())
                    .remark(vo.getRemark())
                    .sort(vo.getSort())
                    .size(image.getSize())
                    .width(image.getWidth())
                    .height(image.getHeight())
                    .pic(image.getUrl())
                    .thumbUrl(image.getThumbUrl())
                    .type(GalleryEnums.GalleryType.WALLPAPER.getValue())
                    .joinCollectionTime(ObjectUtil.isNotNull(vo.getCollectionId()) ? LocalDateTime.now() : null)
                    .approvalStatus(GalleryEnums.GalleryApprovalStatus.RESOLVE.getValue())
                    .isPublished(GalleryEnums.GalleryIsPublished.YES.getValue())
                    .downloadIntegral(userConfigProperties.getDefaultDownloadIntegral())
                    .build();
            galleryList.add(build);
        }
        return this.saveBatch(galleryList);
    }

    @Override
    public List<Long> saveAiDrawGallery(SaveAiDrawGalleryDTO data) {
        // 获取默认下载积分
        UserConfigProperties userConfigProperties = sysConfigService.getUserConfigProperties();
        List<Long> galleryIds = new ArrayList<>();
        for (int i = 0; i < data.getBatchSize(); i++) {
            Gallery gallery = Gallery.builder()
                    .userId(data.getUserId())
                    .pic("")
                    .thumbUrl("")
                    .width(data.getWidth())
                    .height(data.getHeight())
                    .size(0L)
                    .sdConfig(data.getSdConfig())
                    .type(GalleryEnums.GalleryType.SD_TXT2IMG.getValue())
                    .isPublished(GalleryEnums.GalleryIsPublished.NO.getValue())
                    .status(GalleryEnums.GalleryStatus.AWAIT.getValue())
                    .approvalStatus(GalleryEnums.GalleryApprovalStatus.AWAIT.getValue())
                    .consumeIntegral(data.getConsumeIntegral())
                    .downloadIntegral(userConfigProperties.getDefaultDownloadIntegral())
                    .build();
            boolean result = this.save(gallery);
            if (result) {
                galleryIds.add(gallery.getId());
            }
        }

        return galleryIds;
    }

    @Override
    public void updateAiDrawGalleryStatusByIds(List<String> ids, Integer status) {
        this.update(Wrappers.<Gallery>lambdaUpdate()
                .set(Gallery::getStatus, status)
                .in(Gallery::getId, ids)
        );
    }

    @Override
    public List<AiDrawImgVO> updateAiDrawResult(Long userId, List<AiDrawImgVO> aiDrawImgList) {
        for (AiDrawImgVO aiDrawImg : aiDrawImgList) {
            if (aiDrawImg.getStatus().equals(GalleryEnums.AiDrawStatus.SUCCESS.getValue())) {
                Gallery gallery = Gallery.builder()
                        .id(aiDrawImg.getGalleryId())
                        .status(GalleryEnums.GalleryStatus.SUCCESS.getValue())
                        .build();
                // 上传图片
                String thumbUrl = "";
                try {
                    MultipartFile multipartFile = FileUtil.base64ToMultipartFile(aiDrawImg.getImageData());
                    Map<String, Object> upload = sysAttachmentService.upload(multipartFile, 1001L, true, false);
                    String url = (String) upload.getOrDefault("url", "");
                    thumbUrl = (String) upload.getOrDefault("thumbUrl", "");
                    gallery.setPic(url);
                    gallery.setThumbUrl(thumbUrl);
                } catch (Exception e) {
                    log.error("AI绘画数据上传异常: {}", e.getMessage());
                    gallery.setStatus(GalleryEnums.GalleryStatus.FAIL.getValue());
                    aiDrawImg.setStatus(GalleryEnums.AiDrawStatus.FAIL.getValue());
                }

                boolean result = this.updateById(gallery);
                if (result) {
                    // 插入审核记录
                    GalleryReview galleryReview = GalleryReview.builder()
                            .galleryId(gallery.getId())
                            .userId(userId)
                            .status(GalleryEnums.GalleryApprovalStatus.AWAIT.getValue())
                            .build();
                    galleryReviewService.save(galleryReview);
                    aiDrawImg.setStatus(GalleryEnums.AiDrawStatus.FAIL.getValue());
                }
                aiDrawImg.setUrl(thumbUrl);
            }
        }

        return aiDrawImgList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApiResult<?> publishGallery(GalleryPublishVO galleryVO) {
        Gallery gallery = this.getById(galleryVO.getId());
        if (!gallery.getUserId().equals(SecurityUtil.getUserId())) {
            throw new ApiException(ResultCode.USER_ERROR_A0300);
        }
        if (ObjectUtil.isNull(gallery.getApprovalStatus())
                || gallery.getApprovalStatus().equals(GalleryEnums.GalleryApprovalStatus.AWAIT.getValue())) {
            return ApiResult.fail("作品审核中，请耐心等待");
        }
        if (gallery.getApprovalStatus().equals(GalleryEnums.GalleryApprovalStatus.REJECT.getValue())) {
            return ApiResult.fail("作品涉嫌违规，发布失败");
        }
        if (gallery.getIsPublished().equals(CommonConstant.STATUS_DISABLE_VALUE)) {
            return ApiResult.fail("作品已发布，请勿重复操作");
        }

        try {
            String appId = RequestHolder.getHttpServletRequestHeader("appId");
            Integer loginType = SecurityUtil.getUser().getLoginType();
            if (loginType.equals(AuthenticationIdentityEnum.DOUYIN_APPLET.getValue())) {
                DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
                List<Integer> resultList = douyinAppletService.checkTextList(List.of(
                        galleryVO.getTitle(),
                        galleryVO.getRemark()
                ));
                if (resultList.size() > 0) {
                    return ApiResult.fail(ResultCode.USER_ERROR_A0431);
                }
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }

        LambdaUpdateWrapper<Gallery> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Gallery::getId, gallery.getId());

        updateWrapper.set(Gallery::getTitle, galleryVO.getTitle());
        updateWrapper.set(Gallery::getRemark, galleryVO.getRemark());
        updateWrapper.set(Gallery::getTagId, galleryVO.getTagId());
        updateWrapper.set(Gallery::getPublishedTime, new Date());
        updateWrapper.set(Gallery::getIsPublished, GalleryEnums.GalleryIsPublished.YES.getValue());
        updateWrapper.set(ObjectUtil.isNotNull(galleryVO.getIsTop()), Gallery::getIsTop, galleryVO.getIsTop());
        updateWrapper.set(Gallery::getUpdateBy, SecurityUtil.getUsername());
        return ApiResult.condition(this.update(updateWrapper));
    }

    @Override
    public ApiResult<?> editGallery(GalleryUpdateDTO galleryVO) {
        Gallery gallery = this.getById(galleryVO.getId());
        if (!gallery.getUserId().equals(SecurityUtil.getUserId())) {
            throw new ApiException(ResultCode.USER_ERROR_A0300);
        }

        try {
            String appId = RequestHolder.getHttpServletRequestHeader("appId");
            Integer loginType = SecurityUtil.getUser().getLoginType();
            if (loginType.equals(AuthenticationIdentityEnum.DOUYIN_APPLET.getValue())) {
                DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
                List<Integer> resultList = douyinAppletService.checkTextList(List.of(
                        galleryVO.getTitle(),
                        galleryVO.getRemark()
                ));
                if (resultList.size() > 0) {
                    return ApiResult.fail(ResultCode.USER_ERROR_A0431);
                }
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }

        LambdaUpdateWrapper<Gallery> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Gallery::getId, gallery.getId());

        updateWrapper.set(Gallery::getTitle, galleryVO.getTitle());
        updateWrapper.set(Gallery::getRemark, galleryVO.getRemark());
        updateWrapper.set(Gallery::getTagId, galleryVO.getTagId());

        updateWrapper.set(ObjectUtil.isNotNull(galleryVO.getIsTop()), Gallery::getIsTop, galleryVO.getIsTop());
        updateWrapper.set(Gallery::getUpdateBy, SecurityUtil.getUsername());
        return ApiResult.condition(this.update(updateWrapper));
    }

    /**
     * 隐藏作品
     *
     * @param galleryId 作品ID
     * @return result
     */
    @Override
    public ApiResult<?> hiddenGallery(Long galleryId) {
        Gallery gallery = this.getById(galleryId);
        Long userId = SecurityUtil.getUserId();
        if (!gallery.getUserId().equals(userId)) {
            throw new ApiException(ResultCode.USER_ERROR_A0300);
        }
        return ApiResult.condition(this.update(Wrappers.<Gallery>lambdaUpdate()
                .eq(Gallery::getId, galleryId)
                .set(Gallery::getIsPublished, GalleryEnums.GalleryIsPublished.NO.getValue())
                .set(Gallery::getUpdateBy, SecurityUtil.getUsername())
        ));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean joinGalleryCollection(JoinGalleryCollectionDTO joinGalleryCollectionDTO) {
        GalleryCollection collection = galleryCollectionService.getById(joinGalleryCollectionDTO.getCollectionId());
        if (!collection.getUserId().equals(SecurityUtil.getUserId())) {
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
        if (!gallery.getUserId().equals(SecurityUtil.getUserId())) {
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
                    .userId(userId)
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

    /**
     * 收藏/取消收藏 帖子
     *
     * @param postId 帖子ID
     * @param type   帖子类型
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean favoriteGallery(Long postId, Integer type) {
        Long userId = SecurityUtil.getUserId();
        UserGalleryCollection userGalleryCollection = userGalleryCollectionService.selectGalleryFavoriteByUserId2GalleryId(userId, postId, type);
        if (ObjectUtil.isNull(userGalleryCollection)) {
            userGalleryCollection = UserGalleryCollection.builder()
                    .userId(userId)
                    .postId(postId)
                    .type(type)
                    .status(CommonConstant.STATUS_NORMAL_VALUE)
                    .build();
        } else {
            if (userGalleryCollection.getStatus().equals(CommonConstant.STATUS_NORMAL_VALUE)) {
                userGalleryCollection.setStatus(CommonConstant.STATUS_DISABLE_VALUE);
            } else {
                userGalleryCollection.setStatus(CommonConstant.STATUS_NORMAL_VALUE);
            }
        }
        return userGalleryCollectionService.saveOrUpdate(userGalleryCollection);
    }

    @Override
    public Long countPublishUserGalleryByUId(Long userId) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getUserId, userId);
        queryWrapper.eq(Gallery::getIsPublished, GalleryEnums.GalleryIsPublished.YES.getValue());
        return this.count(queryWrapper);
    }

    @Override
    public Long countUserReceivedLikeNum(Long userId) {
        return userGalleryLikeService.countUserReceivedLikeNum(userId);
    }

    @Override
    public Long countGalleryNumByCId(Long cateId) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getCollectionId, cateId);
        return this.count(queryWrapper);
    }

    @Override
    public Long countPublishedGalleryNumByCId(Long cateId) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getCollectionId, cateId);
        queryWrapper.eq(Gallery::getIsPublished, GalleryEnums.GalleryIsPublished.YES.getValue());
        return this.count(queryWrapper);
    }

    @GlobalTransactional
    @Override
    public ApiResult<?> downLoadUserGallery(Long id) {
        Long userId = SecurityUtil.getUserId();
        // 扣减用户积分
        Gallery gallery = this.getById(id);
        ApiResult<?> apiResult = memberProvider.assignUserPoint(UserPointChangeDTO.builder()
                .userId(userId)
                .integral(gallery.getDownloadIntegral())
                .changeType(UserPointEnums.ChangeTypeEnum.DECREASE.getValue())
                .title(UserPointEnums.ConsumptionEnum.DOWNLOAD_GALLERY.getLabel())
                .status(UserPointEnums.StatusEnum.SUCCESS.getValue())
                .build());
        if (apiResult.successful()) {
            // 增加作品下载次数
            Long downloadTimes = gallery.getDownloadTimes();
            gallery.setDownloadTimes(++downloadTimes);
            this.update(Wrappers.<Gallery>lambdaUpdate()
                    .set(Gallery::getDownloadTimes, downloadTimes)
                    .eq(Gallery::getId, gallery.getId()));

            // 构建用户下载记录
            galleryDownloadRecordService.save(UserGalleryDownload.builder()
                    .userId(userId)
                    .galleryId(gallery.getId())
                    .downloadIntegral(gallery.getDownloadIntegral())
                    .build());

            // 赠送用户佣金
            assignUserBrokerage(userId, gallery);
        }

        return apiResult;
    }

    // 赠送用户佣金
    private void assignUserBrokerage(Long userId, Gallery gallery) {
        // 判断是否开启下载作品赠送佣金
        UserConfigProperties userConfigProperties = sysConfigService.getUserConfigProperties();
        if (userConfigProperties.getIsEnableDownloadWorkRebate()) {
            BigDecimal commission;
            try {
                Integer downloadIntegral = gallery.getDownloadIntegral();
                Integer downloadWorkAmountRatio = userConfigProperties.getDownloadWorkAmountRatio();
                // 计算佣金
                commission = new BigDecimal(downloadIntegral)
                        .multiply(BigDecimal.valueOf(downloadWorkAmountRatio))
                        .divide(new BigDecimal("100"), RoundingMode.DOWN);
            } catch (Exception e) {
                commission = BigDecimal.ZERO;
                log.error("计算佣金异常，作品ID『{}』, 积分『{}』, 下载人『{}』", gallery.getId(), gallery.getDownloadIntegral(), userId);
            }

            // 发放用户佣金
            if (commission.compareTo(BigDecimal.ZERO) > 0) {
                memberProvider.assignUserBrokerage(UserBrokerageChangeDTO.builder()
                        .brokerage(commission)
                        .changeType(UserBrokerageEnums.ChangeTypeEnum.INCREASE.getValue())
                        .userId(gallery.getUserId())
                        .linkId(gallery.getId())
                        .linkType(UserBrokerageEnums.SourceEnum.DOWNLOAD_WORKS.getValue())
                        .title(UserBrokerageEnums.SourceEnum.DOWNLOAD_WORKS.getLabel())
                        .status(UserPointEnums.StatusEnum.SUCCESS.getValue())
                        .build());
            }
        }
    }

    @Override
    public boolean checkUserPointQuantity(Long id) {
        Gallery gallery = this.getById(id);
        if (ObjectUtil.isNotNull(gallery)) {
            // 获取用户信息
            ApiResult<UserInfoVO> simpleUser = memberProvider.getSimpleUserById(SecurityUtil.getUserId());
            if (simpleUser.successful() && simpleUser.getData() != null) {
                UserInfoVO data = simpleUser.getData();
                return data.getIntegral() >= gallery.getDownloadIntegral();
            }
            throw new ApiException(ResultCode.USER_ERROR_A0230);
        }

        throw new ApiException(ResultCode.USER_ERROR_A0500);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeGalleryByIds(List<Long> ids) {
        Long userId = SecurityUtil.getUserId();
        List<Gallery> galleryList = this.listByIds(ids);

        boolean flag = false;
        for (Gallery gallery : galleryList) {
            if (!gallery.getUserId().equals(userId)) {
                flag = true;
                break;
            }
        }

        if (flag) {
            throw new ApiException(ResultCode.USER_ERROR_A0300);
        }
        return this.removeBatchByIds(ids);
    }

    // 组装作品信息
    @NotNull
    private GalleryVO getGalleryVO(GalleryVO galleryVO) {
        Long userId = SecurityUtil.getUserIdOrDefault();
        List<UserGalleryLikeVO> userGalleryLikeVOS = userGalleryLikeService.selectGalleryLikeVOListByGId(galleryVO.getId());
        List<UserGalleryFavoriteVO> userGalleryFavoriteVOS = userGalleryCollectionService.selectGalleryFavoriteVOListByGalleryId(galleryVO.getId());
        buildUserGalleryAdditionalVO(userId, galleryVO, userGalleryLikeVOS, userGalleryFavoriteVOS);
        return galleryVO;
    }

    // 组装作品附加信息
    private void buildUserGalleryAdditionalVO(
            Long userId,
            GalleryVO galleryVO,
            List<UserGalleryLikeVO> userGalleryLikeVOS,
            List<UserGalleryFavoriteVO> userGalleryFavoriteVOS) {

        // 设置点赞信息
        setGalleryLike(userId, galleryVO, userGalleryLikeVOS);

        // 设置收藏信息
        setGalleryFavorite(userId, galleryVO, userGalleryFavoriteVOS);

        // todo 组装下载记录
    }

    // 设置点赞信息
    private void setGalleryLike(Long userId, GalleryVO galleryVO, List<UserGalleryLikeVO> userGalleryLikeVOS) {
        List<UserGalleryLikeVO> likeList = userGalleryLikeVOS.parallelStream()
                .filter(galleryLikeVO -> galleryLikeVO.getPostId().equals(galleryVO.getId())
                        && galleryLikeVO.getType().equals(GalleryEnums.GalleryPageType.GALLERY.getValue()))
                .collect(Collectors.toList());
        galleryVO.setLikeList(likeList);
        galleryVO.setLikeNum(likeList.size());
        galleryVO.setIsLike(Boolean.FALSE);

        if (ObjectUtil.isNotNull(userId)) {
            likeList.parallelStream()
                    .filter(it -> it.getUserId().equals(userId))
                    .findAny()
                    .ifPresent(it -> {
                        galleryVO.setIsLike(Boolean.TRUE);
                    });
        }
    }

    // 设置收藏信息
    private void setGalleryFavorite(Long userId, GalleryVO galleryVO, List<UserGalleryFavoriteVO> userGalleryFavoriteVOS) {
        // 组装收藏信息
        List<UserGalleryFavoriteVO> favoriteList = userGalleryFavoriteVOS.parallelStream()
                .filter(favoriteVO -> favoriteVO.getPostId().equals(galleryVO.getId())
                        && favoriteVO.getType().equals(GalleryEnums.GalleryPageType.GALLERY.getValue()))
                .collect(Collectors.toList());
        galleryVO.setFavoriteList(favoriteList);
        galleryVO.setFavoriteNum(favoriteList.size());
        galleryVO.setIsFavorite(Boolean.FALSE);
        if (ObjectUtil.isNotNull(userId)) {
            favoriteList.parallelStream()
                    .filter(it -> it.getUserId().equals(userId))
                    .findAny()
                    .ifPresent(it -> {
                        galleryVO.setIsFavorite(Boolean.TRUE);
                    });
        }

    }

    // 设置创作者信息
    private void setGalleryCreator(GalleryVO galleryVO, List<MemberInfoVO> memberInfoVOS) {
        if (CollUtil.isNotEmpty(memberInfoVOS)) {
            memberInfoVOS.parallelStream()
                    .filter(it -> it.getUserId().equals(galleryVO.getUserId()))
                    .findAny()
                    .ifPresent(memberInfo -> {
                        galleryVO.setNickname(memberInfo.getNickname());
                        galleryVO.setAvatar(memberInfo.getAvatar());
                    });
        }
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

    private void buildSimpleUserGalleryAdditional(UserGallerySimpleVO ugl, List<GalleryVO> galleryList, List<GalleryCollectionVO> galleryCollections) {
        buildSimpleUserGalleryAdditional(ugl, galleryList);
        buildSimpleUserGalleryCollectionAdditional(ugl, galleryCollections);
    }

    private void buildSimpleUserGalleryAdditional(UserGallerySimpleVO ugl, List<GalleryVO> galleryList) {
        // 组装作品信息
        galleryList.parallelStream()
                .filter(it -> it.getId().equals(ugl.getPostId()))
                .findAny().ifPresent(galleryVO -> {
                    ugl.setAvatar(galleryVO.getAvatar());
                    ugl.setNickname(galleryVO.getNickname());
                    ugl.setTitle(galleryVO.getTitle());
                    ugl.setLikeNum(galleryVO.getLikeNum());
                    ugl.setFavoriteNum(galleryVO.getFavoriteNum());
                    ugl.setCovers(List.of(galleryVO.getThumbUrl()));
                });
    }

    private void buildSimpleUserGalleryCollectionAdditional(UserGallerySimpleVO ugl, List<GalleryCollectionVO> galleryCollections) {
        // 组装合集信息
        galleryCollections.parallelStream()
                .filter(it -> it.getId().equals(ugl.getPostId()))
                .findAny().ifPresent(collectionVO -> {
                    ugl.setAvatar(collectionVO.getAvatar());
                    ugl.setNickname(collectionVO.getNickname());
                    ugl.setTitle(collectionVO.getTitle());
                    ugl.setLikeNum(collectionVO.getLikeNum());
                    ugl.setFavoriteNum(collectionVO.getFavoriteNum());
                    ugl.setCovers(collectionVO.getCovers());
                });
    }


    // 获取用户信息
    @NotNull
    private List<MemberInfoVO> getMemberInfoVOS(List<Long> userIds) {
        List<MemberInfoVO> memberInfoVOS = new ArrayList<>();
        try {
            ApiResult<List<MemberInfoVO>> getUserResult = memberProvider.getUserListByIds(userIds);
            if (ObjectUtil.isNotNull(getUserResult) && getUserResult.successful()) {
                List<MemberInfoVO> memberInfos = getUserResult.getData();
                memberInfoVOS.addAll(memberInfos);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return memberInfoVOS;
    }

    @Override
    public void asyncGalleryWidth2Height() {
        // 同步附件文件宽高
        List<Gallery> list = this.list(Wrappers.<Gallery>lambdaQuery().isNull(Gallery::getWidth));
        for (Gallery gallery : list) {
            try {
                String url = gallery.getPic();
                InputStream inputStream = new URL(url).openStream();
                BufferedImage read = ImageIO.read(inputStream);
                int width = read.getWidth();
                int height = read.getHeight();

                gallery.setWidth(width);
                gallery.setHeight(height);
                this.updateById(gallery);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApiResult<?> galleryAppeal(GallerySubmitAppealVO vo) {
        Long userId = SecurityUtil.getUserId();
        if (!userId.equals(vo.getUserId())) {
            throw new ApiException(ResultCode.USER_ERROR_A0300);
        }

        try {
            String appId = RequestHolder.getHttpServletRequestHeader("appId");
            Integer loginType = SecurityUtil.getUser().getLoginType();
            if (loginType.equals(AuthenticationIdentityEnum.DOUYIN_APPLET.getValue())) {
                DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
                List<Integer> resultList = douyinAppletService.checkTextList(List.of(
                        vo.getReason()
                ));
                if (resultList.size() > 0) {
                    return ApiResult.fail(ResultCode.USER_ERROR_A0431);
                }
            }
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }

        // 查询是否存在待审核的作品, 存在即取消
        GalleryAppeal galleryAppeal = galleryAppealService.selectWaitAppealGalleryByGalleryId2UserId(userId, vo.getGalleryId());
        if (ObjectUtil.isNotNull(galleryAppeal)) {
            galleryAppeal.setStatus(GalleryEnums.GalleryAppealStatus.CANCEL.getValue());
            galleryAppealService.updateById(galleryAppeal);
        }

        GalleryAppeal build = GalleryAppeal.builder()
                .userId(userId)
                .galleryId(vo.getGalleryId())
                .reason(vo.getReason())
                .status(GalleryEnums.GalleryAppealStatus.AWAIT.getValue())
                .build();
        // todo 发送代办提醒
        return ApiResult.condition(galleryAppealService.save(build));
    }

    @Override
    public boolean setGalleryApprovalStatus(UpdateStatusDTO updateStatus) {
        return this.update(Wrappers.<Gallery>lambdaUpdate()
                .eq(Gallery::getId, updateStatus.getId())
                .set(Gallery::getApprovalStatus, updateStatus.getStatus())
        );
    }

    @Override
    public Long selectGalleryWaitReviewCount() {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getApprovalStatus, GalleryEnums.GalleryApprovalStatus.AWAIT.getValue());
        return this.count(queryWrapper);
    }
}




