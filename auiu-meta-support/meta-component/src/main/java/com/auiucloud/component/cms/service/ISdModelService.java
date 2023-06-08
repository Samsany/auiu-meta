package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.SdModel;
import com.auiucloud.component.cms.vo.SdModelVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_sd_model(SD模型主题表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdModelService extends IService<SdModel> {

    List<SdModelVO> selectSdModelVOListByCId(Long aiDrawId);

}
