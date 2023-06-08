package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.SdDrawStyle;
import com.auiucloud.component.cms.vo.SdDrawStyleVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_sd_draw_style(SD绘画风格表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdDrawStyleService extends IService<SdDrawStyle> {

    List<SdDrawStyleVO> selectAllSdDrawVOList();

}
