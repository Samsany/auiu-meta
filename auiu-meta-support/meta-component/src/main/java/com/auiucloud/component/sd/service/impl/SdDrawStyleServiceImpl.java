package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdPicQuality;
import com.auiucloud.component.sd.domain.SdPicRatio;
import com.auiucloud.component.sd.vo.SdDrawStyleVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.sd.domain.SdDrawStyle;
import com.auiucloud.component.sd.service.ISdDrawStyleService;
import com.auiucloud.component.sd.mapper.SdDrawStyleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* @author dries
* @description 针对表【sd_draw_style(SD绘画风格表)】的数据库操作Service实现
* @createDate 2023-05-21 23:07:20
*/
@Service
public class SdDrawStyleServiceImpl extends ServiceImpl<SdDrawStyleMapper, SdDrawStyle>
    implements ISdDrawStyleService {

    @Override
    public PageUtils listPage(Search search, SdDrawStyle drawStyle) {
        LambdaQueryWrapper<SdDrawStyle> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SdDrawStyle::getName, search.getKeyword());
        queryWrapper.eq(ObjectUtil.isNotNull(search.getStatus()), SdDrawStyle::getStatus, search.getStatus());
        queryWrapper.eq(ObjectUtil.isNotNull(drawStyle.getCateId()), SdDrawStyle::getCateId, drawStyle.getCateId());
        queryWrapper.orderByDesc(SdDrawStyle::getSort);
        queryWrapper.orderByDesc(SdDrawStyle::getCreateTime);
        queryWrapper.orderByDesc(SdDrawStyle::getId);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean checkDrawStyleNameExist(SdDrawStyle drawStyle) {
        LambdaQueryWrapper<SdDrawStyle> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(drawStyle.getId()), SdDrawStyle::getId, drawStyle.getId());
        queryWrapper.eq(SdDrawStyle::getName, drawStyle.getName());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean setDrawStyleStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<SdDrawStyle> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SdDrawStyle::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(SdDrawStyle::getId, updateStatusDTO.getId());
        return this.update(wrapper);
    }

    @Override
    public List<SdDrawStyleVO> selectAllSdDrawVOList() {
        return Optional.ofNullable(this.list(Wrappers.<SdDrawStyle>lambdaQuery()
                        .eq(SdDrawStyle::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(SdDrawStyle::getSort)
                        .orderByDesc(SdDrawStyle::getCreateTime)
                        .orderByDesc(SdDrawStyle::getId)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::sdDrawStyle2VO)
                .toList();
    }

    @Override
    public List<SdDrawStyleVO> selectSdDrawVOListByIds(List<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            return Optional.ofNullable(this.list(Wrappers.<SdDrawStyle>lambdaQuery()
                            .in(SdDrawStyle::getId, ids)
                            .eq(SdDrawStyle::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                            .orderByDesc(SdDrawStyle::getSort)
                            .orderByDesc(SdDrawStyle::getCreateTime)
                            .orderByDesc(SdDrawStyle::getId)
                    ))
                    .orElse(Collections.emptyList())
                    .parallelStream()
                    .map(this::sdDrawStyle2VO)
                    .toList();
        }

        return Collections.emptyList();
    }

    private SdDrawStyleVO sdDrawStyle2VO(SdDrawStyle sdDrawStyle) {
        SdDrawStyleVO vo = new SdDrawStyleVO();
        BeanUtils.copyProperties(sdDrawStyle, vo);
        return vo;
    }
}




