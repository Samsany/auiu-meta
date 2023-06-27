package com.auiucloud.component.sd.service;

import com.auiucloud.component.sd.domain.SdDrawCategory;
import com.auiucloud.component.sd.vo.SdDrawCategoryVO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【sd_draw_category(SD绘画类型表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdDrawCategoryService extends IService<SdDrawCategory> {

    List<SdDrawCategoryVO> aiDrawMenuList();

    PageUtils listPage(Search search, SdDrawCategory category);

    boolean checkSdModelCategoryNameExist(SdDrawCategory category);

    boolean setSdModelCategoryStatus(UpdateStatusDTO updateStatusDTO);
}
