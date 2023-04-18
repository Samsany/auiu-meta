package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.PicTag;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_pic_tag(图片标签表)】的数据库操作Service
* @createDate 2023-04-11 16:03:44
*/
public interface IPicTagService extends IService<PicTag> {

    PageUtils listPage(Search search, PicTag picTag);

    boolean checkPicTagNameExist(PicTag picTag);

    boolean checkPicTagHasChild(Long picTagId);

    boolean setStatus(UpdateStatusDTO updateStatusDTO);

    List<PicTag> selectRecommendPicTagList();
}
