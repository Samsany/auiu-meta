package com.auiucloud.component.sd.service;

import com.auiucloud.component.sd.domain.SdDrawStyle;
import com.auiucloud.component.sd.vo.SdDrawStyleVO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【sd_draw_style(SD绘画风格表)】的数据库操作Service
* @createDate 2023-05-21 23:07:20
*/
public interface ISdDrawStyleService extends IService<SdDrawStyle> {

    List<SdDrawStyleVO> selectAllSdDrawVOList();

    List<SdDrawStyleVO> selectSdDrawVOListByIds(List<Long> ids);

    PageUtils listPage(Search search, SdDrawStyle drawStyle);

    boolean checkDrawStyleNameExist(SdDrawStyle drawStyle);

    boolean setDrawStyleStatus(UpdateStatusDTO updateStatusDTO);
}
