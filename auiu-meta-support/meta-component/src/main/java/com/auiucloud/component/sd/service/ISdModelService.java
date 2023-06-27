package com.auiucloud.component.sd.service;

import com.auiucloud.component.sd.domain.SdModel;
import com.auiucloud.component.sd.vo.SdModelConfigVO;
import com.auiucloud.component.sd.vo.SdModelVO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【sd_model(SD模型主题表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdModelService extends IService<SdModel> {

    List<SdModelVO> selectSdModelVOListByCId(Long aiDrawId);

    List<SdModelVO> selectSdModelVOListByCateId(Long cateId);

    PageUtils listPage(Search search, SdModel model);

    SdModelConfigVO getSdModelConfigVOById(Long id);

    boolean saveBySdModelConfigVO(SdModelConfigVO model);

    boolean updateSdModelConfigVOById(SdModelConfigVO model);

    boolean saveOrUpdateSdModelConfigVO(SdModelConfigVO model);

    boolean checkSdModelNameExist(SdModel model);

    boolean setSdModelStatus(UpdateStatusDTO updateStatusDTO);
}
