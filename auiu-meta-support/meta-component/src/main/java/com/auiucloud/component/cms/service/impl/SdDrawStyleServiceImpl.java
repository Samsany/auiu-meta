package com.auiucloud.component.cms.service.impl;

import com.auiucloud.component.cms.domain.SdDrawStyleCategory;
import com.auiucloud.component.cms.vo.SdDrawStyleCategoryVO;
import com.auiucloud.component.cms.vo.SdDrawStyleVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.cms.domain.SdDrawStyle;
import com.auiucloud.component.cms.service.ISdDrawStyleService;
import com.auiucloud.component.cms.mapper.SdDrawStyleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* @author dries
* @description 针对表【cms_sd_draw_style(SD绘画风格表)】的数据库操作Service实现
* @createDate 2023-05-21 23:07:20
*/
@Service
public class SdDrawStyleServiceImpl extends ServiceImpl<SdDrawStyleMapper, SdDrawStyle>
    implements ISdDrawStyleService {

    @Override
    public List<SdDrawStyleVO> selectAllSdDrawVOList() {
        return Optional.ofNullable(this.list(Wrappers.<SdDrawStyle>lambdaQuery()
                        .eq(SdDrawStyle::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(SdDrawStyle::getSort)
                        .orderByDesc(SdDrawStyle::getCreateTime)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::sdDrawStyle2VO)
                .toList();
    }

    private SdDrawStyleVO sdDrawStyle2VO(SdDrawStyle sdDrawStyle) {
        SdDrawStyleVO vo = new SdDrawStyleVO();
        BeanUtils.copyProperties(sdDrawStyle, vo);
        return vo;
    }
}




