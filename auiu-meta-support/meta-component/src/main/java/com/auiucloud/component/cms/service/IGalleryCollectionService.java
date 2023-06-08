package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.component.cms.vo.GalleryCollectionVO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【cms_gallery_collection(作品合集表)】的数据库操作Service
 * @createDate 2023-04-16 20:56:41
 */
public interface IGalleryCollectionService extends IService<GalleryCollection> {

    List<GalleryCollectionVO> listGalleryCollectionVOByCIds(List<Long> cIds);

    List<GalleryCollectionVO> selectUserCollectionList(Search search, GalleryCollection galleryCollection);

    List<GalleryCollection> selectUserCollectionApiList(Search search, GalleryCollection galleryCollection);

    PageUtils selectUserCollectionApiPage(Search search, GalleryCollection galleryCollection);

    PageUtils selectGalleryCollectionUserHomePage(Search search, GalleryCollection galleryCollection);

    GalleryCollectionVO selectGalleryCollectionById(Long cateId);

    GalleryCollectionVO getGalleryCollect(Long collectId);

    boolean checkGalleryCollectNameExist(GalleryCollection galleryCollection);

    boolean addGalleryCollect(GalleryCollectionVO galleryCollection);

    boolean updateGalleryCollectById(GalleryCollectionVO galleryCollection);

    boolean removeGalleryCollectionByIds(List<Long> ids);

    boolean setCollectionTopStatus(UpdateStatusDTO statusDTO);

}
