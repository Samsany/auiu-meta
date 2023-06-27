package com.auiucloud.component.sd.service;

import com.auiucloud.component.sd.domain.SdDrawStyleCategory;
import com.auiucloud.component.sd.vo.SdDrawStyleCategoryVO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【sd_draw_style_category(SD绘画风格分类表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdDrawStyleCategoryService extends IService<SdDrawStyleCategory> {

    List<SdDrawStyleCategoryVO> selectAllSdDrawCategoryVOList();

    List<SdDrawStyleCategoryVO> selectSdDrawCategoryVOListByIds(List<Long> ids);

    PageUtils listPage(Search search, SdDrawStyleCategory drawStyle);

    boolean setDrawStyleStatus(UpdateStatusDTO updateStatusDTO);

    boolean checkDrawStyleNameExist(SdDrawStyleCategory drawStyle);

}
