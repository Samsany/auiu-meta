package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.Gallery;
import com.auiucloud.component.cms.dto.JoinGalleryCollectionDTO;
import com.auiucloud.component.cms.vo.GalleryVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_gallery(作品表)】的数据库操作Service
* @createDate 2023-04-16 20:56:41
*/
public interface IGalleryService extends IService<Gallery> {

    List<GalleryVO> selectGalleryListByCId(Long cId);

    List<GalleryVO> selectGalleryListByCId2Limit(Long cId, Integer limit);

    GalleryVO selectGalleryInfoById(Long galleryId);

    ApiResult<?> upload(MultipartFile file, Long cId);

    PageUtils selectGalleryPage(Search search, Gallery gallery);

    boolean joinGalleryCollection(JoinGalleryCollectionDTO joinGalleryCollectionDTO);

    boolean removeGalleryByIds(List<Long> ids);

    boolean setGalleryTopStatus(UpdateStatusDTO statusDTO);

}
