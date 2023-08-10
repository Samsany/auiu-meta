package com.auiucloud.component.sd.service;

import com.auiucloud.component.sd.domain.SdImageModel;
import com.auiucloud.component.sd.domain.SdModel;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dries
* @description 针对表【sd_image_model(AI绘画图片表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdImageModelService extends IService<SdImageModel> {

    PageUtils listPage(Search search, SdImageModel model);

    PageUtils selectSdImageModelPage(Search search, String tags);

}
