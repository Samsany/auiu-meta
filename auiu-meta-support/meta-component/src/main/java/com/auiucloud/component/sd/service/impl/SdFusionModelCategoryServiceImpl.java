package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdDrawStyleCategory;
import com.auiucloud.component.sd.domain.SdFusionModel;
import com.auiucloud.component.sd.vo.SdFusionModelCategoryVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.sd.domain.SdFusionModelCategory;
import com.auiucloud.component.sd.service.ISdFusionModelCategoryService;
import com.auiucloud.component.sd.mapper.SdFusionModelCategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* @author dries
* @description 针对表【sd_fusion_model_category(SD融合模型分类表)】的数据库操作Service实现
* @createDate 2023-05-21 23:07:20
*/
@Service
public class SdFusionModelCategoryServiceImpl extends ServiceImpl<SdFusionModelCategoryMapper, SdFusionModelCategory>
    implements ISdFusionModelCategoryService {

    @Override
    public PageUtils listPage(Search search, SdFusionModelCategory category) {
        LambdaQueryWrapper<SdFusionModelCategory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SdFusionModelCategory::getName, search.getKeyword());
        queryWrapper.eq(ObjectUtil.isNotNull(search.getStatus()), SdFusionModelCategory::getStatus, search.getStatus());
        queryWrapper.orderByDesc(SdFusionModelCategory::getSort);
        queryWrapper.orderByDesc(SdFusionModelCategory::getCreateTime);
        queryWrapper.orderByDesc(SdFusionModelCategory::getId);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean checkFusionModelCategoryNameExist(SdFusionModelCategory category) {
        LambdaQueryWrapper<SdFusionModelCategory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(category.getId()), SdFusionModelCategory::getId, category.getId());
        queryWrapper.eq(SdFusionModelCategory::getName, category.getName());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean setFusionModelCategoryStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<SdFusionModelCategory> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SdFusionModelCategory::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(SdFusionModelCategory::getId, updateStatusDTO.getId());
        return this.update(wrapper);
    }

    @Override
    public List<SdFusionModelCategoryVO> selectAllSdFusionModelCategoryVOList() {
        return Optional.ofNullable(this.list(Wrappers.<SdFusionModelCategory>lambdaQuery()
                        .eq(SdFusionModelCategory::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(SdFusionModelCategory::getSort)
                        .orderByDesc(SdFusionModelCategory::getCreateTime)
                        .orderByDesc(SdFusionModelCategory::getId)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::sdFusionModel2VO)
                .toList();
    }

    @Override
    public List<SdFusionModelCategoryVO> selectSdFusionModelCategoryVOListByIds(List<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            return Optional.ofNullable(this.list(Wrappers.<SdFusionModelCategory>lambdaQuery()
                            .in(SdFusionModelCategory::getId, ids)
                            .eq(SdFusionModelCategory::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                            .orderByDesc(SdFusionModelCategory::getSort)
                            .orderByDesc(SdFusionModelCategory::getCreateTime)
                            .orderByDesc(SdFusionModelCategory::getId)
                    ))
                    .orElse(Collections.emptyList())
                    .parallelStream()
                    .map(this::sdFusionModel2VO)
                    .toList();
        }

        return Collections.emptyList();
    }

    private SdFusionModelCategoryVO sdFusionModel2VO(SdFusionModelCategory sdFusionModelCategory) {
        SdFusionModelCategoryVO vo = new SdFusionModelCategoryVO();
        BeanUtils.copyProperties(sdFusionModelCategory, vo);
        return vo;
    }
}




