package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdImageModel;
import com.auiucloud.component.sd.domain.SdImageTag;
import com.auiucloud.component.sd.mapper.SdImageModelMapper;
import com.auiucloud.component.sd.service.ISdImageModelService;
import com.auiucloud.component.sd.service.ISdImageTagService;
import com.auiucloud.component.sd.vo.SdImageModelGroupVO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【sd_model(AI绘画图片表)】的数据库操作Service实现
 * @createDate 2023-05-21 23:07:20
 */
@Service
@RequiredArgsConstructor
public class SdImageModelServiceImpl extends ServiceImpl<SdImageModelMapper, SdImageModel>
        implements ISdImageModelService {

    private final ISdImageTagService sdImageTagService;

    @Override
    public PageUtils listPage(Search search, SdImageModel model) {
        LambdaQueryWrapper<SdImageModel> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SdImageModel::getName, search.getKeyword());
        queryWrapper.like(ObjectUtil.isNotNull(model.getTags()), SdImageModel::getTags, model.getTags());
        queryWrapper.orderByDesc(SdImageModel::getId);
        IPage<SdImageModel> page = this.page(PageUtils.getPage(search), queryWrapper);
        List<SdImageTag> tags = Optional.ofNullable(sdImageTagService.list()).orElse(Collections.emptyList());
        page.convert(sdImageModel -> {
            String[] tagIds = sdImageModel.getTags().split(StringPool.COMMA);
            StringBuilder tagTitle = new StringBuilder();
            Arrays.stream(tagIds).forEach((it) -> tags.parallelStream()
                    .filter(tag -> it.equals(String.valueOf(tag.getId())))
                    .map(SdImageTag::getTitle)
                    .findAny()
                    .ifPresent(tag -> {
                        if (tagTitle.length() > 0) {
                            tagTitle.append(StringPool.COMMA);
                        }
                        tagTitle.append(tag);
                    })
            );
            sdImageModel.setTag(tagTitle.toString());
            return sdImageModel;
        });
        return new PageUtils(page);
    }

    @Override
    public PageUtils selectSdImageModelPage(Search search, String tags) {
        LambdaQueryWrapper<SdImageModel> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.and(e -> {
            if (StrUtil.isNotBlank(tags)) {
                String[] list = tags.split(",");
                for (String s : list) {
                    e.or().like(SdImageModel::getTags, s);
                }
            }
        });

        queryWrapper.select(SdImageModel::getPostId);
        // queryWrapper.isNotNull(SdImageModel::getMetaConfig);
        queryWrapper.isNotNull(SdImageModel::getPostId);
        queryWrapper.groupBy(SdImageModel::getPostId);
        queryWrapper.orderByDesc(SdImageModel::getId);
        IPage<SdImageModel> page = this.page(PageUtils.getPage(search), queryWrapper);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            Set<String> postIds = page.getRecords().parallelStream()
                    .map(SdImageModel::getPostId)
                    .collect(Collectors.toSet());
            LambdaQueryWrapper<SdImageModel> wrapper = Wrappers.lambdaQuery();
            wrapper.in(SdImageModel::getPostId, postIds);
            wrapper.orderByDesc(SdImageModel::getId);
            List<SdImageModel> list = Optional.ofNullable(this.list(wrapper)).orElse(Collections.emptyList());
            page.convert(it -> {
                SdImageModelGroupVO build = SdImageModelGroupVO.builder()
                        .postId(it.getPostId())
                        .build();
                Optional.of(list.parallelStream()
                        .filter(image -> image.getPostId().equals(it.getPostId()))
                        .toList()
                ).ifPresent(images -> {
                    SdImageModel sdImageModel = images.get(0);
                    build.setCover(String.valueOf(sdImageModel.getId()));
                    build.setImages(images);
                });

                return build;
            });
        }

        return new PageUtils(page);
    }

}




