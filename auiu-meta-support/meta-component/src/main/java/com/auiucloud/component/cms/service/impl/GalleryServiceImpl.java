package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.auiucloud.component.cms.domain.*;
import com.auiucloud.component.cms.dto.JoinGalleryCollectionDTO;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.mapper.GalleryMapper;
import com.auiucloud.component.cms.service.*;
import com.auiucloud.component.cms.vo.*;
import com.auiucloud.component.oss.service.ISysAttachmentService;
import com.auiucloud.component.sysconfig.domain.UserConfigProperties;
import com.auiucloud.component.sysconfig.service.ISysConfigService;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final IPicTagService picTagService;
    private final IUserGalleryLikeService userGalleryLikeService;
    private final IUserGalleryCollectionService userGalleryCollectionService;
    private final IUserGalleryDownloadService userGalleryDownloadService;
    private final ISysConfigService sysConfigService;
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
    public PageUtils selectSquareGalleryVOPage(Search search, Gallery gallery) {

        if (gallery.getTagId().equals(GalleryEnums.GalleryTagType.COLLECTION.getValue())) {
            // todo 查询合集
            return new PageUtils(Collections.emptyList());
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
        queryWrapper.eq(UserGalleryLike::getUId, SecurityUtil.getUserId());
        IPage<UserGalleryLike> page = userGalleryLikeService.page(PageUtils.getPage(search), queryWrapper);
        if (page.getRecords().size() > 0) {
            // 分别取出作品 和 合集
            Map<Integer, List<UserGalleryLike>> map = page.getRecords().parallelStream()
                    .filter(it -> it.getType() != null)
                    .collect(Collectors.groupingBy(UserGalleryLike::getType));

            // 作品
            List<UserGalleryLike> userGalleryLikes = map.get(GalleryEnums.GalleryPageType.GALLERY.getValue());
            List<Long> galleryIds = userGalleryLikes.parallelStream().map(UserGalleryLike::getPostId).toList();
            List<GalleryVO> galleryList = this.listGalleryVOByGIds(galleryIds);

            // 合集
            List<UserGalleryLike> userGalleryCollectionLikes = map.get(GalleryEnums.GalleryPageType.GALLERY_COLLECTION.getValue());
            List<Long> cIds = userGalleryCollectionLikes.parallelStream().map(UserGalleryLike::getPostId).toList();
            List<GalleryCollectionVO> galleryCollections = galleryCollectionService.listGalleryCollectionVOByCIds(cIds);

            page.convert(it -> {
                UserGallerySimpleVO build = UserGallerySimpleVO.builder()
                        .id(it.getId())
                        .uId(it.getUId())
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
        queryWrapper.eq(UserGalleryCollection::getUId, SecurityUtil.getUserId());
        IPage<UserGalleryCollection> page = userGalleryCollectionService.page(PageUtils.getPage(search), queryWrapper);
        if (page.getRecords().size() > 0) {
            // 分别取出作品 和 合集
            Map<Integer, List<UserGalleryCollection>> map = page.getRecords().parallelStream()
                    .filter(it -> it.getType() != null)
                    .collect(Collectors.groupingBy(UserGalleryCollection::getType));

            // 作品
            List<UserGalleryCollection> userGalleryLikes = map.get(GalleryEnums.GalleryPageType.GALLERY.getValue());
            List<Long> galleryIds = userGalleryLikes.parallelStream().map(UserGalleryCollection::getPostId).toList();
            List<GalleryVO> galleryList = this.listGalleryVOByGIds(galleryIds);

            // 合集
            List<UserGalleryCollection> userGalleryCollectionLikes = map.get(GalleryEnums.GalleryPageType.GALLERY_COLLECTION.getValue());
            List<Long> cIds = userGalleryCollectionLikes.parallelStream().map(UserGalleryCollection::getPostId).toList();
            List<GalleryCollectionVO> galleryCollections = galleryCollectionService.listGalleryCollectionVOByCIds(cIds);

            page.convert(it -> {
                UserGallerySimpleVO build = UserGallerySimpleVO.builder()
                        .id(it.getId())
                        .uId(it.getUId())
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
        queryWrapper.eq(UserGalleryDownload::getUId, SecurityUtil.getUserId());
        IPage<UserGalleryDownload> page = userGalleryDownloadService.page(PageUtils.getPage(search), queryWrapper);
        if (page.getRecords().size() > 0) {
            List<Long> gIds = page.getRecords()
                    .stream()
                    .map(UserGalleryDownload::getGId)
                    .toList();

            List<GalleryVO> galleryList = this.listGalleryVOByGIds(gIds);
            page.convert(it -> {
                UserGallerySimpleVO build = UserGallerySimpleVO.builder()
                        .id(it.getId())
                        .uId(it.getUId())
                        .postId(it.getGId())
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
        if (ObjectUtil.isNotNull(gallery.getApprovalStatus())
                && gallery.getApprovalStatus().equals(GalleryEnums.GalleryApprovalStatus.AWAIT.getValue())) {
            return ApiResult.fail("作品已提交，等待审核中");
        }

        try {
            String appId = RequestHolder.getHttpServletRequestHeader("appId");
            Integer loginType = SecurityUtil.getUser().getLoginType();
            if (loginType.equals(AuthenticationIdentityEnum.DOUYIN_APPLET.getValue())) {
                DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
                List<Integer> resultList = douyinAppletService.checkTextList(List.of(
                        galleryVO.getTitle(),
                        galleryVO.getPrompt(),
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
        updateWrapper.set(Gallery::getTitle, galleryVO.getTitle());
        updateWrapper.set(Gallery::getRemark, galleryVO.getRemark());
        updateWrapper.set(Gallery::getPrompt, galleryVO.getPrompt());
        updateWrapper.set(Gallery::getTagId, galleryVO.getTagId());
        updateWrapper.set(Gallery::getPublishedTime, new Date());
        // 默认已发布，无需审核
        // updateWrapper.set(Gallery::getApprovalStatus, GalleryEnums.GalleryApprovalStatus.AWAIT.getValue());
        updateWrapper.set(Gallery::getApprovalStatus, GalleryEnums.GalleryApprovalStatus.RESOLVE.getValue());
        updateWrapper.set(Gallery::getIsPublished, GalleryEnums.GalleryIsPublished.YES.getValue());
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
                    .uId(userId)
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
        queryWrapper.eq(Gallery::getUId, userId);
        queryWrapper.eq(Gallery::getIsPublished, GalleryEnums.GalleryIsPublished.YES.getValue());
        return this.count(queryWrapper);
    }

    @Override
    public Long countUserReceivedLikeNum(Long userId) {
        return userGalleryLikeService.countUserReceivedLikeNum(userId);
    }

    @Override
    public Long countGalleryNumByCId(Long cId) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getCollectionId, cId);
        return this.count(queryWrapper);
    }

    @Override
    public Long countPublishedGalleryNumByCId(Long cId) {
        LambdaQueryWrapper<Gallery> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Gallery::getCollectionId, cId);
        queryWrapper.eq(Gallery::getIsPublished, GalleryEnums.GalleryIsPublished.YES.getValue());
        return this.count(queryWrapper);
    }

    @GlobalTransactional
    @Override
    public ApiResult<?> downLoadUserGallery(Long id) {
        Long userId = SecurityUtil.getUserId();
        // 扣减用户积分
        Gallery gallery = this.getById(id);
        ApiResult<Boolean> apiResult = memberProvider.decreaseUserPoint(UserPointChangeDTO.builder()
                .userId(userId)
                .integral(gallery.getDownloadIntegral())
                .changeType(UserPointEnums.ChangeTypeEnum.DECREASE.getValue())
                .build());
        if (apiResult.successful() && apiResult.getData()) {
            // 增加作品下载次数
            Long downloadTimes = gallery.getDownloadTimes();
            gallery.setDownloadTimes(++downloadTimes);
            this.update(Wrappers.<Gallery>lambdaUpdate()
                    .set(Gallery::getDownloadTimes, downloadTimes)
                    .eq(Gallery::getId, gallery.getId()));

            // 构建用户下载记录
            galleryDownloadRecordService.save(UserGalleryDownload.builder()
                    .uId(userId)
                    .gId(gallery.getId())
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
                ApiResult<Boolean> apiResult = memberProvider.assignUserBrokerage(UserBrokerageChangeDTO.builder()
                        .brokerage(commission)
                        .changeType(UserBrokerageEnums.ChangeTypeEnum.INCREASE.getValue())
                        .userId(gallery.getUId())
                        .linkId(gallery.getId())
                        .linkType(UserBrokerageEnums.SourceEnum.DOWNLOAD_WORKS.getValue())
                        .title(UserBrokerageEnums.SourceEnum.DOWNLOAD_WORKS.getLabel())
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
                    .filter(it -> it.getUId().equals(userId))
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
                    .filter(it -> it.getUId().equals(userId))
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
                    .filter(it -> it.getUserId().equals(galleryVO.getUId()))
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
}




