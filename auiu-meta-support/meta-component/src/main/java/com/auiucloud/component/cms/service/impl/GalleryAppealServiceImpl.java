package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.cms.domain.GalleryAppeal;
import com.auiucloud.component.cms.domain.GalleryReview;
import com.auiucloud.component.cms.dto.GalleryAppealDTO;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.mapper.GalleryAppealMapper;
import com.auiucloud.component.cms.service.IGalleryAppealService;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.vo.GalleryAppealVO;
import com.auiucloud.component.cms.vo.GalleryVO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author dries
 * @description 针对表【cms_gallery_appeal(作品申诉表)】的数据库操作Service实现
 * @createDate 2023-06-09 17:46:33
 */
@Service
@RequiredArgsConstructor
public class GalleryAppealServiceImpl extends ServiceImpl<GalleryAppealMapper, GalleryAppeal>
        implements IGalleryAppealService {

    @Lazy
    @Resource
    private IGalleryService galleryService;


    @Override
    public PageUtils listPage(Search search, GalleryAppeal galleryAppeal) {
        LambdaQueryWrapper<GalleryAppeal> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ObjectUtil.isNotNull(galleryAppeal.getStatus()), GalleryAppeal::getStatus, galleryAppeal.getStatus());
        queryWrapper.eq(ObjectUtil.isNotNull(galleryAppeal.getUserId()), GalleryAppeal::getUserId, galleryAppeal.getUserId());
        queryWrapper.eq(ObjectUtil.isNotNull(galleryAppeal.getGalleryId()), GalleryAppeal::getGalleryId, galleryAppeal.getGalleryId());
        queryWrapper.ge(ObjectUtil.isNotNull(search.getStartDate()), GalleryAppeal::getCreateTime, search.getStartDate());
        queryWrapper.le(ObjectUtil.isNotNull(search.getEndDate()), GalleryAppeal::getCreateTime, search.getEndDate());
        IPage<GalleryAppeal> page = this.page(PageUtils.getPage(search), queryWrapper);
        page.convert(it -> {
            GalleryAppealVO galleryAppealVO = new GalleryAppealVO();
            BeanUtils.copyProperties(it, galleryAppealVO);
            // 组装作品信息
            GalleryVO galleryVO = galleryService.selectGalleryInfoById(it.getGalleryId());
            galleryAppealVO.setGallery(galleryVO);
            return galleryAppealVO;
        });
        return new PageUtils(page);
    }

    @Override
    public GalleryAppealVO getGalleryAppealVOById(Long id) {
        GalleryAppeal appeal = this.getById(id);
        GalleryAppealVO galleryAppealVO = new GalleryAppealVO();
        BeanUtils.copyProperties(appeal, galleryAppealVO);
        // 组装作品信息
        GalleryVO galleryVO = galleryService.selectGalleryInfoById(appeal.getGalleryId());
        galleryAppealVO.setGallery(galleryVO);
        return galleryAppealVO;
    }

    @Override
    public GalleryAppeal selectWaitAppealGalleryByGalleryId2UserId(Long userId, Long galleryId) {
        return this.getOne(Wrappers.<GalleryAppeal>lambdaQuery()
                .eq(GalleryAppeal::getGalleryId, galleryId)
                .eq(GalleryAppeal::getUserId, userId)
                .eq(GalleryAppeal::getStatus, GalleryEnums.GalleryAppealStatus.AWAIT.getValue()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean resolveGalleryAppeal(GalleryAppealDTO galleryAppeal) {
        boolean update = this.update(Wrappers.<GalleryAppeal>lambdaUpdate()
                .eq(GalleryAppeal::getId, galleryAppeal.getAppealId())
                .set(GalleryAppeal::getStatus, galleryAppeal.getStatus())
                .set(GalleryAppeal::getRemark, galleryAppeal.getRemark())
                .set(GalleryAppeal::getUpdateBy, SecurityUtil.getUsername())
        );
        if (update) {
            galleryService.setGalleryApprovalStatus(UpdateStatusDTO.builder()
                    .id(galleryAppeal.getGalleryId())
                    .status(GalleryEnums.GalleryAppealStatus.RESOLVE.getValue())
                    .build());
        }
        return update;
    }

    @Override
    public boolean rejectGalleryAppeal(GalleryAppealDTO galleryAppeal) {
        return this.update(Wrappers.<GalleryAppeal>lambdaUpdate()
                .eq(GalleryAppeal::getId, galleryAppeal.getAppealId())
                .set(GalleryAppeal::getStatus, galleryAppeal.getStatus())
                .set(GalleryAppeal::getRemark, galleryAppeal.getRemark())
                .set(GalleryAppeal::getUpdateBy, SecurityUtil.getUsername())
        );
    }
}




