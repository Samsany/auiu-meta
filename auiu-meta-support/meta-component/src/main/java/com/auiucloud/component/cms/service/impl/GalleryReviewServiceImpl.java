package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.cms.domain.GalleryReview;
import com.auiucloud.component.cms.dto.GalleryReviewBatchDTO;
import com.auiucloud.component.cms.dto.GalleryReviewDTO;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.mapper.GalleryReviewMapper;
import com.auiucloud.component.cms.service.IGalleryReviewService;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.vo.GalleryAppealVO;
import com.auiucloud.component.cms.vo.GalleryReviewVO;
import com.auiucloud.component.cms.vo.GalleryVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dries
 * @description 针对表【cms_gallery_review(作品审核表)】的数据库操作Service实现
 * @createDate 2023-06-13 14:26:52
 */
@Service
public class GalleryReviewServiceImpl extends ServiceImpl<GalleryReviewMapper, GalleryReview>
        implements IGalleryReviewService {

    @Lazy
    @Resource
    private IGalleryService galleryService;

    @Override
    public PageUtils listPage(Search search, GalleryReview galleryReview) {
        Long total = baseMapper.countGalleryReviewVOTotal(search, galleryReview);
        Search s = PageUtils.buildPage(search, Math.toIntExact(total));
        List<GalleryReviewVO> galleryReviewVOPage = baseMapper.selectGalleryReviewVOPage(s, galleryReview);
        return new PageUtils(total, search, galleryReviewVOPage);
    }

    @Override
    public GalleryReviewVO getGalleryReviewVOById(Long id) {
        return baseMapper.selectGalleryReviewVOById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean resolveGalleryReview(GalleryReviewDTO galleryReview) {
        boolean update = this.update(Wrappers.<GalleryReview>lambdaUpdate()
                .eq(GalleryReview::getId, galleryReview.getReviewId())
                .set(GalleryReview::getStatus, GalleryEnums.GalleryAppealStatus.RESOLVE.getValue())
                .set(GalleryReview::getReason, galleryReview.getReason())
                .set(GalleryReview::getRemark, galleryReview.getRemark())
                .set(GalleryReview::getUpdateBy, SecurityUtil.getUsername())
        );
        if (update) {
            galleryService.setGalleryApprovalStatus(UpdateStatusDTO.builder()
                    .id(galleryReview.getGalleryId())
                    .status(GalleryEnums.GalleryAppealStatus.RESOLVE.getValue())
                    .build());
        }
        return update;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean rejectGalleryReview(GalleryReviewDTO galleryReview) {
        boolean update = this.update(Wrappers.<GalleryReview>lambdaUpdate()
                .eq(GalleryReview::getId, galleryReview.getReviewId())
                .set(GalleryReview::getStatus, GalleryEnums.GalleryAppealStatus.REJECT.getValue())
                .set(GalleryReview::getReason, galleryReview.getReason())
                .set(GalleryReview::getRemark, galleryReview.getRemark())
                .set(GalleryReview::getUpdateBy, SecurityUtil.getUsername())
        );
        if (update) {
            // 设置作品审核状态
            galleryService.setGalleryApprovalStatus(UpdateStatusDTO.builder()
                    .id(galleryReview.getGalleryId())
                    .status(GalleryEnums.GalleryAppealStatus.REJECT.getValue())
                    .build());
        }
        return update;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean galleryReviewBatch(GalleryReviewBatchDTO galleryReview) {
        boolean update = this.update(Wrappers.<GalleryReview>lambdaUpdate()
                .in(GalleryReview::getId, galleryReview.getReviewIds())
                .set(GalleryReview::getStatus, galleryReview.getStatus())
                .set(GalleryReview::getReason, galleryReview.getReason())
                .set(GalleryReview::getRemark, galleryReview.getRemark())
                .set(GalleryReview::getUpdateBy, SecurityUtil.getUsername())
        );
        // 同步更新作品信息
        if (update) {
            for (Long galleryId : galleryReview.getGalleryIds()) {
                // 设置作品审核状态
                galleryService.setGalleryApprovalStatus(UpdateStatusDTO.builder()
                        .id(galleryId)
                        .status(galleryReview.getStatus())
                        .build());
            }
        }
        return update;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean withdrawGalleryReview(GalleryReviewDTO galleryReview) {
        boolean update = this.update(Wrappers.<GalleryReview>lambdaUpdate()
                .eq(GalleryReview::getId, galleryReview.getReviewId())
                .set(GalleryReview::getStatus, GalleryEnums.GalleryAppealStatus.AWAIT.getValue())
                .set(GalleryReview::getReason, galleryReview.getReason())
                .set(GalleryReview::getRemark, galleryReview.getRemark())
                .set(GalleryReview::getUpdateBy, SecurityUtil.getUsername())
        );
        if (update) {
            // 设置作品审核状态
            galleryService.setGalleryApprovalStatus(UpdateStatusDTO.builder()
                    .id(galleryReview.getGalleryId())
                    .status(GalleryEnums.GalleryAppealStatus.AWAIT.getValue())
                    .build());
        }
        return update;
    }

    @Override
    public Long auditCount(Search search) {
        return null;
    }
}




