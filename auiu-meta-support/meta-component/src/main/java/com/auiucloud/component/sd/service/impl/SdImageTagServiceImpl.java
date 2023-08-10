package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdDrawCategory;
import com.auiucloud.component.sd.domain.SdImageModel;
import com.auiucloud.component.sd.domain.SdImageTag;
import com.auiucloud.component.sd.mapper.SdImageModelMapper;
import com.auiucloud.component.sd.mapper.SdImageTagMapper;
import com.auiucloud.component.sd.service.ISdImageModelService;
import com.auiucloud.component.sd.service.ISdImageTagService;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【sd_model(AI绘画图片标签表)】的数据库操作Service实现
 * @createDate 2023-05-21 23:07:20
 */
@Service
@RequiredArgsConstructor
public class SdImageTagServiceImpl extends ServiceImpl<SdImageTagMapper, SdImageTag>
        implements ISdImageTagService {


    @Override
    public PageUtils listPage(Search search, SdImageTag tag) {
        LambdaQueryWrapper<SdImageTag> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SdImageTag::getTitle, search.getKeyword());
        queryWrapper.eq(ObjectUtil.isNotNull(search.getStatus()), SdImageTag::getStatus, search.getStatus());
        queryWrapper.orderByDesc(SdImageTag::getSort);
        queryWrapper.orderByDesc(SdImageTag::getId);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean checkSdImageModelTagNameExist(SdImageTag tag) {
        LambdaQueryWrapper<SdImageTag> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(tag.getId()), SdImageTag::getId, tag.getId());
        queryWrapper.eq(SdImageTag::getName, tag.getTitle());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean setSdImageModelTagStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<SdImageTag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SdImageTag::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(SdImageTag::getId, updateStatusDTO.getId());
        return this.update(wrapper);
    }
}




