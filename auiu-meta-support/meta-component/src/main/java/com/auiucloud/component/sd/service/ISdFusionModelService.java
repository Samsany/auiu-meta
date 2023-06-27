package com.auiucloud.component.sd.service;

import com.auiucloud.component.sd.domain.SdFusionModel;
import com.auiucloud.component.sd.vo.SdFusionModelVO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【sd_fusion_model(SD融合模型表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdFusionModelService extends IService<SdFusionModel> {

    List<SdFusionModelVO> selectAllSdFusionModelVOList();

    List<SdFusionModelVO> selectSdFusionModelVOListByIds(List<Long> loraIds);

    boolean checkFusionModelNameExist(SdFusionModel fusionModel);

    boolean setFusionModelStatus(UpdateStatusDTO updateStatusDTO);

    PageUtils listPage(Search search, SdFusionModel fusionModel);

}
