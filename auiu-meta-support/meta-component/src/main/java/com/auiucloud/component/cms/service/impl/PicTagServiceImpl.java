package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.cms.domain.PicTag;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.mapper.PicTagMapper;
import com.auiucloud.component.cms.service.IPicTagService;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dries
 * @description 针对表【cms_pic_tag(图片标签表)】的数据库操作Service实现
 * @createDate 2023-04-11 16:03:44
 */
@Service
public class PicTagServiceImpl extends ServiceImpl<PicTagMapper, PicTag> implements IPicTagService {

    @Override
    public PageUtils listPage(Search search, PicTag picTag) {
        LambdaQueryWrapper<PicTag> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), PicTag::getName, search.getKeyword());
        queryWrapper.orderByDesc(PicTag::getSort);
        queryWrapper.orderByDesc(PicTag::getCreateTime);
        queryWrapper.orderByDesc(PicTag::getId);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public List<PicTag> selectRecommendPicTagList() {
        LambdaQueryWrapper<PicTag> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(PicTag::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        queryWrapper.eq(PicTag::getIsHomeDisplay, CommonConstant.STATUS_DISABLE_VALUE);
        queryWrapper.orderByDesc(PicTag::getSort);
        queryWrapper.orderByDesc(PicTag::getCreateTime);
        queryWrapper.orderByDesc(PicTag::getId);
        return this.list(queryWrapper);
    }

    @Override
    public List<PicTag> selectCommonPicTagList(Integer type) {

        List<PicTag> picTagList = new ArrayList<>();

        LambdaQueryWrapper<PicTag> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(PicTag::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        queryWrapper.orderByDesc(PicTag::getSort);
        queryWrapper.orderByAsc(PicTag::getCreateTime);
        queryWrapper.orderByDesc(PicTag::getId);
        List<PicTag> list = Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());

        if (type.equals(CommonConstant.STATUS_DISABLE_VALUE)) {
            PicTag allPicTag = PicTag.builder()
                    .id(GalleryEnums.GalleryTagType.ALL.getValue())
                    .name(GalleryEnums.GalleryTagType.ALL.getLabel())
                    .build();
            picTagList.add(allPicTag);

            //            PicTag collectionPicTag = PicTag.builder()
            //                    .id(GalleryEnums.GalleryTagType.COLLECTION.getValue())
            //                    .name(GalleryEnums.GalleryTagType.COLLECTION.getLabel())
            //                    .build();
            //            picTagList.add(collectionPicTag);
        }
        picTagList.addAll(list);

        return picTagList;
    }

    @Override
    public boolean setStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<PicTag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(PicTag::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(PicTag::getId, updateStatusDTO.getId());
        return this.update(wrapper);
    }

    @Override
    public boolean checkPicTagNameExist(PicTag picTag) {
        LambdaQueryWrapper<PicTag> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(picTag.getId()), PicTag::getId, picTag.getId());
        queryWrapper.eq(PicTag::getName, picTag.getName());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean checkPicTagHasChild(Long picTagId) {
        return true;
    }
}




