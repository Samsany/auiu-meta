package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.mapper.SdPicQualityMapper;
import com.auiucloud.component.sd.vo.SdPicQualityVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.sd.domain.SdPicQuality;
import com.auiucloud.component.cms.service.IPicQualityService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* @author dries
* @description 针对表【sd_up_scale(SD图片放大算法表)】的数据库操作Service实现
* @createDate 2023-05-21 20:04:17
*/
@Service
public class SdPicQualityServiceImpl extends ServiceImpl<SdPicQualityMapper, SdPicQuality>
    implements IPicQualityService {

    @Override
    public PageUtils listPage(Search search, SdPicQuality quality) {
        LambdaQueryWrapper<SdPicQuality> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SdPicQuality::getTitle, search.getKeyword());
        queryWrapper.eq(ObjectUtil.isNotNull(search.getStatus()), SdPicQuality::getStatus, search.getStatus());
        queryWrapper.orderByDesc(SdPicQuality::getSort);
        queryWrapper.orderByDesc(SdPicQuality::getCreateTime);
        queryWrapper.orderByDesc(SdPicQuality::getId);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean checkPicQualityNameExist(SdPicQuality quality) {
        LambdaQueryWrapper<SdPicQuality> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(quality.getId()), SdPicQuality::getId, quality.getId());
        queryWrapper.eq(SdPicQuality::getTitle, quality.getTitle());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean setPicQualityStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<SdPicQuality> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SdPicQuality::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(SdPicQuality::getId, updateStatusDTO.getId());
        return this.update(wrapper);
    }

    @Override
    public List<SdPicQualityVO> selectNormalPicQualityList() {
        return Optional.ofNullable(this.list(Wrappers.<SdPicQuality>lambdaQuery()
                        .eq(SdPicQuality::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(SdPicQuality::getSort)
                        .orderByDesc(SdPicQuality::getCreateTime)
                        .orderByDesc(SdPicQuality::getId)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::picQuality2VO)
                .toList();
    }

    @Override
    public List<SdPicQualityVO> selectPicQualityVOListByIds(List<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            return Optional.ofNullable(this.list(Wrappers.<SdPicQuality>lambdaQuery()
                            .in(SdPicQuality::getId, ids)
                            .eq(SdPicQuality::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                    ))
                    .orElse(Collections.emptyList())
                    .parallelStream()
                    .map(this::picQuality2VO)
                    .toList();
        }
        return Collections.emptyList();
    }

    private SdPicQualityVO picQuality2VO(SdPicQuality picQuality) {
        SdPicQualityVO picQualityVO = new SdPicQualityVO();
        BeanUtils.copyProperties(picQuality, picQualityVO);
        return picQualityVO;
    }
}




