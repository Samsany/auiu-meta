package com.auiucloud.component.cms.service.impl;

import com.auiucloud.component.cms.domain.SdFusionModelCategory;
import com.auiucloud.component.cms.vo.SdDrawStyleCategoryVO;
import com.auiucloud.component.cms.vo.SdFusionModelCategoryVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.cms.domain.SdDrawStyleCategory;
import com.auiucloud.component.cms.service.ISdDrawStyleCategoryService;
import com.auiucloud.component.cms.mapper.SdDrawStyleCategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* @author dries
* @description 针对表【cms_sd_draw_style_category(SD绘画风格分类表)】的数据库操作Service实现
* @createDate 2023-05-21 23:07:20
*/
@Service
public class SdDrawStyleCategoryServiceImpl extends ServiceImpl<SdDrawStyleCategoryMapper, SdDrawStyleCategory>
    implements ISdDrawStyleCategoryService {

    @Override
    public List<SdDrawStyleCategoryVO> selectAllSdDrawCategoryVOList() {
        return Optional.ofNullable(this.list(Wrappers.<SdDrawStyleCategory>lambdaQuery()
                        .eq(SdDrawStyleCategory::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(SdDrawStyleCategory::getSort)
                        .orderByDesc(SdDrawStyleCategory::getCreateTime)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::sdDrawStyleCategory2VO)
                .toList();
    }

    private SdDrawStyleCategoryVO sdDrawStyleCategory2VO(SdDrawStyleCategory sdDrawStyleCategory) {
        SdDrawStyleCategoryVO vo = new SdDrawStyleCategoryVO();
        BeanUtils.copyProperties(sdDrawStyleCategory, vo);
        return vo;
    }
}




