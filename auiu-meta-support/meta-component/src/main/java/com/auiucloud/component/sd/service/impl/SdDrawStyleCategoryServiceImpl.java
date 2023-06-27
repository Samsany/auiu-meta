package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdDrawStyle;
import com.auiucloud.component.sd.vo.SdDrawStyleCategoryVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.sd.domain.SdDrawStyleCategory;
import com.auiucloud.component.sd.service.ISdDrawStyleCategoryService;
import com.auiucloud.component.sd.mapper.SdDrawStyleCategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* @author dries
* @description 针对表【sd_draw_style_category(SD绘画风格分类表)】的数据库操作Service实现
* @createDate 2023-05-21 23:07:20
*/
@Service
public class SdDrawStyleCategoryServiceImpl extends ServiceImpl<SdDrawStyleCategoryMapper, SdDrawStyleCategory>
    implements ISdDrawStyleCategoryService {

    @Override
    public PageUtils listPage(Search search, SdDrawStyleCategory drawStyle) {
        LambdaQueryWrapper<SdDrawStyleCategory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SdDrawStyleCategory::getName, search.getKeyword());
        queryWrapper.eq(ObjectUtil.isNotNull(search.getStatus()), SdDrawStyleCategory::getStatus, search.getStatus());
        queryWrapper.orderByDesc(SdDrawStyleCategory::getSort);
        queryWrapper.orderByDesc(SdDrawStyleCategory::getCreateTime);
        queryWrapper.orderByDesc(SdDrawStyleCategory::getId);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean setDrawStyleStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<SdDrawStyleCategory> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SdDrawStyleCategory::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(SdDrawStyleCategory::getId, updateStatusDTO.getId());
        return this.update(wrapper);
    }

    @Override
    public boolean checkDrawStyleNameExist(SdDrawStyleCategory drawStyle) {
        LambdaQueryWrapper<SdDrawStyleCategory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(drawStyle.getId()), SdDrawStyleCategory::getId, drawStyle.getId());
        queryWrapper.eq(SdDrawStyleCategory::getName, drawStyle.getName());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public List<SdDrawStyleCategoryVO> selectAllSdDrawCategoryVOList() {
        return Optional.ofNullable(this.list(Wrappers.<SdDrawStyleCategory>lambdaQuery()
                        .eq(SdDrawStyleCategory::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(SdDrawStyleCategory::getSort)
                        .orderByDesc(SdDrawStyleCategory::getCreateTime)
                        .orderByDesc(SdDrawStyleCategory::getId)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::sdDrawStyleCategory2VO)
                .toList();
    }

    @Override
    public List<SdDrawStyleCategoryVO> selectSdDrawCategoryVOListByIds(List<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            return Optional.ofNullable(this.list(Wrappers.<SdDrawStyleCategory>lambdaQuery()
                            .in(SdDrawStyleCategory::getId, ids)
                            .eq(SdDrawStyleCategory::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                            .orderByDesc(SdDrawStyleCategory::getSort)
                            .orderByDesc(SdDrawStyleCategory::getCreateTime)
                            .orderByDesc(SdDrawStyleCategory::getId)
                    ))
                    .orElse(Collections.emptyList())
                    .parallelStream()
                    .map(this::sdDrawStyleCategory2VO)
                    .toList();
        }

        return Collections.emptyList();
    }

    private SdDrawStyleCategoryVO sdDrawStyleCategory2VO(SdDrawStyleCategory sdDrawStyleCategory) {
        SdDrawStyleCategoryVO vo = new SdDrawStyleCategoryVO();
        BeanUtils.copyProperties(sdDrawStyleCategory, vo);
        return vo;
    }
}




