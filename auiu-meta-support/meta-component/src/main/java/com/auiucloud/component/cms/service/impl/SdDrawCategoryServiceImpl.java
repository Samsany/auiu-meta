package com.auiucloud.component.cms.service.impl;

import com.auiucloud.component.cms.domain.PicRatio;
import com.auiucloud.component.cms.vo.PicRatioVO;
import com.auiucloud.component.cms.vo.SdDrawCategoryVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.cms.domain.SdDrawCategory;
import com.auiucloud.component.cms.service.ISdDrawCategoryService;
import com.auiucloud.component.cms.mapper.SdDrawCategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* @author dries
* @description 针对表【cms_sd_draw_category(SD绘画类型表)】的数据库操作Service实现
* @createDate 2023-05-21 23:07:20
*/
@Service
public class SdDrawCategoryServiceImpl extends ServiceImpl<SdDrawCategoryMapper, SdDrawCategory>
    implements ISdDrawCategoryService {

    @Override
    public List<SdDrawCategoryVO> aiDrawMenuList() {
        return Optional.ofNullable(this.list(Wrappers.<SdDrawCategory>lambdaQuery()
                        .eq(SdDrawCategory::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(SdDrawCategory::getSort)
                        .orderByDesc(SdDrawCategory::getCreateTime)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::sdDrawCategory2VO)
                .toList();
    }

    private SdDrawCategoryVO sdDrawCategory2VO(SdDrawCategory sdDrawCategory) {
        SdDrawCategoryVO vo = new SdDrawCategoryVO();
        BeanUtils.copyProperties(sdDrawCategory, vo);
        return vo;
    }
}




