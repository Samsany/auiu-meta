package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.cms.domain.PicTag;
import com.auiucloud.component.cms.domain.SwiperAdv;
import com.auiucloud.component.cms.service.IGalleryCollectionService;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.component.cms.mapper.GalleryCollectionMapper;
import org.springframework.stereotype.Service;

/**
* @author dries
* @description 针对表【cms_gallery_collection(作品合集表)】的数据库操作Service实现
* @createDate 2023-04-16 20:56:41
*/
@Service
public class GalleryCollectionServiceImpl extends ServiceImpl<GalleryCollectionMapper, GalleryCollection>
    implements IGalleryCollectionService {

    @Override
    public PageUtils selectCollectionPage(Search search, GalleryCollection galleryCollection) {
        LambdaQueryWrapper<GalleryCollection> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(GalleryCollection::getSort);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean checkGalleryCollectNameExist(GalleryCollection galleryCollection) {
        LambdaQueryWrapper<GalleryCollection> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(galleryCollection.getId()), GalleryCollection::getId, galleryCollection.getId());
        queryWrapper.eq(GalleryCollection::getTitle, galleryCollection.getTitle());
        queryWrapper.eq(GalleryCollection::getUId, galleryCollection.getUId());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }
}




