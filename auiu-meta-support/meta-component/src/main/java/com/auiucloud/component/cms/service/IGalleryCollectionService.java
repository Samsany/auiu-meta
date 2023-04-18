package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dries
* @description 针对表【cms_gallery_collection(作品合集表)】的数据库操作Service
* @createDate 2023-04-16 20:56:41
*/
public interface IGalleryCollectionService extends IService<GalleryCollection> {

    PageUtils selectCollectionPage(Search search, GalleryCollection galleryCollection);

    boolean checkGalleryCollectNameExist(GalleryCollection galleryCollection);
}
