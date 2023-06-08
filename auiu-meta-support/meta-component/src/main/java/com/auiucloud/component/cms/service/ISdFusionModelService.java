package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.SdFusionModel;
import com.auiucloud.component.cms.vo.SdFusionModelVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_sd_fusion_model(SD融合模型表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdFusionModelService extends IService<SdFusionModel> {

    List<SdFusionModelVO> selectAllSdFusionModelVOList();

}
