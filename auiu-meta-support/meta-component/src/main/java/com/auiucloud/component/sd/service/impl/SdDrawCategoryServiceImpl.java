package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdFusionModelCategory;
import com.auiucloud.component.sd.vo.SdDrawCategoryVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.sd.domain.SdDrawCategory;
import com.auiucloud.component.sd.service.ISdDrawCategoryService;
import com.auiucloud.component.sd.mapper.SdDrawCategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* @author dries
* @description 针对表【sd_draw_category(SD绘画类型表)】的数据库操作Service实现
* @createDate 2023-05-21 23:07:20
*/
@Service
public class SdDrawCategoryServiceImpl extends ServiceImpl<SdDrawCategoryMapper, SdDrawCategory>
    implements ISdDrawCategoryService {

    @Override
    public PageUtils listPage(Search search, SdDrawCategory category) {
        LambdaQueryWrapper<SdDrawCategory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SdDrawCategory::getName, search.getKeyword());
        queryWrapper.eq(ObjectUtil.isNotNull(search.getStatus()), SdDrawCategory::getStatus, search.getStatus());
        queryWrapper.orderByDesc(SdDrawCategory::getSort);
        queryWrapper.orderByDesc(SdDrawCategory::getCreateTime);
        queryWrapper.orderByDesc(SdDrawCategory::getId);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean checkSdModelCategoryNameExist(SdDrawCategory category) {
        LambdaQueryWrapper<SdDrawCategory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(category.getId()), SdDrawCategory::getId, category.getId());
        queryWrapper.eq(SdDrawCategory::getName, category.getName());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean setSdModelCategoryStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<SdDrawCategory> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SdDrawCategory::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(SdDrawCategory::getId, updateStatusDTO.getId());
        return this.update(wrapper);
    }

    @Override
    public List<SdDrawCategoryVO> aiDrawMenuList() {
        return Optional.ofNullable(this.list(Wrappers.<SdDrawCategory>lambdaQuery()
                        .eq(SdDrawCategory::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(SdDrawCategory::getSort)
                        .orderByDesc(SdDrawCategory::getCreateTime)
                        .orderByDesc(SdDrawCategory::getId)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::sdDrawCategory2VO)
                .toList();
    }

    private SdDrawCategoryVO sdDrawCategory2VO(SdDrawCategory sdDrawCategory) {
        SdDrawCategoryVO vo = new SdDrawCategoryVO();
        BeanUtils.copyProperties(sdDrawCategory, vo);
        return vo;
    }
}




