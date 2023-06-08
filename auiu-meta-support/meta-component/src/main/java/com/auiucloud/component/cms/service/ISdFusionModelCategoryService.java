package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.SdFusionModelCategory;
import com.auiucloud.component.cms.vo.SdFusionModelCategoryVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_sd_fusion_model_category(SD融合模型分类表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdFusionModelCategoryService extends IService<SdFusionModelCategory> {

    List<SdFusionModelCategoryVO> selectAllSdFusionModelCategoryVOList();

}
