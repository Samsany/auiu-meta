package com.auiucloud.component.sd.service;

import com.auiucloud.component.sd.domain.SdFusionModelCategory;
import com.auiucloud.component.sd.vo.SdFusionModelCategoryVO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【sd_fusion_model_category(SD融合模型分类表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdFusionModelCategoryService extends IService<SdFusionModelCategory> {

    List<SdFusionModelCategoryVO> selectAllSdFusionModelCategoryVOList();

    List<SdFusionModelCategoryVO> selectSdFusionModelCategoryVOListByIds(List<Long> ids);

    PageUtils listPage(Search search, SdFusionModelCategory category);

    boolean checkFusionModelCategoryNameExist(SdFusionModelCategory category);

    boolean setFusionModelCategoryStatus(UpdateStatusDTO updateStatusDTO);

}
