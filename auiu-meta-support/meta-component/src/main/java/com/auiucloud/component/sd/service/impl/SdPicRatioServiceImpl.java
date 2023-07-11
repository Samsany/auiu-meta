package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdPicRatio;
import com.auiucloud.component.sd.mapper.SdPicRatioMapper;
import com.auiucloud.component.cms.service.IPicRatioService;
import com.auiucloud.component.sd.vo.SdPicRatioVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dries
 * @description 针对表【cms_pic_ratio(图片比例表)】的数据库操作Service实现
 * @createDate 2023-05-21 14:52:48
 */
@Service
public class SdPicRatioServiceImpl extends ServiceImpl<SdPicRatioMapper, SdPicRatio>
        implements IPicRatioService {

    @Override
    public PageUtils listPage(Search search, SdPicRatio ratio) {
        LambdaQueryWrapper<SdPicRatio> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SdPicRatio::getTitle, search.getKeyword());
        queryWrapper.eq(ObjectUtil.isNotNull(search.getStatus()), SdPicRatio::getStatus, search.getStatus());
        queryWrapper.orderByDesc(SdPicRatio::getSort);
        queryWrapper.orderByDesc(SdPicRatio::getCreateTime);
        queryWrapper.orderByDesc(SdPicRatio::getId);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean setPicRatioStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<SdPicRatio> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SdPicRatio::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(SdPicRatio::getId, updateStatusDTO.getId());
        return this.update(wrapper);
    }

    @Override
    public boolean checkPicRatioNameExist(SdPicRatio ratio) {
        LambdaQueryWrapper<SdPicRatio> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(ratio.getId()), SdPicRatio::getId, ratio.getId());
        queryWrapper.eq(SdPicRatio::getTitle, ratio.getTitle());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public List<SdPicRatioVO> selectNormalPicRatioVOList() {
        return Optional.ofNullable(this.list(Wrappers.<SdPicRatio>lambdaQuery()
                        .eq(SdPicRatio::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(SdPicRatio::getSort)
                        .orderByDesc(SdPicRatio::getCreateTime)
                        .orderByDesc(SdPicRatio::getId)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::picRatio2VO)
                .toList();
    }

    @Override
    public List<SdPicRatioVO> selectPicRatioVOListByIds(List<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            return Optional.ofNullable(this.list(Wrappers.<SdPicRatio>lambdaQuery()
                            .in(SdPicRatio::getId, ids)
                            .eq(SdPicRatio::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                    ))
                    .orElse(Collections.emptyList())
                    .parallelStream()
                    .map(this::picRatio2VO)
                    .toList();
        }
        return Collections.emptyList();
    }

    private SdPicRatioVO picRatio2VO(SdPicRatio picRatio) {
        SdPicRatioVO picRatioVO = new SdPicRatioVO();
        BeanUtils.copyProperties(picRatio, picRatioVO);
        return picRatioVO;
    }
}




