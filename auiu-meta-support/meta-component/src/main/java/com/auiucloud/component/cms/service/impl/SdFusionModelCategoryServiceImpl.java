package com.auiucloud.component.cms.service.impl;

import com.auiucloud.component.cms.domain.SdFusionModel;
import com.auiucloud.component.cms.vo.SdFusionModelCategoryVO;
import com.auiucloud.component.cms.vo.SdFusionModelVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.cms.domain.SdFusionModelCategory;
import com.auiucloud.component.cms.service.ISdFusionModelCategoryService;
import com.auiucloud.component.cms.mapper.SdFusionModelCategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* @author dries
* @description 针对表【cms_sd_fusion_model_category(SD融合模型分类表)】的数据库操作Service实现
* @createDate 2023-05-21 23:07:20
*/
@Service
public class SdFusionModelCategoryServiceImpl extends ServiceImpl<SdFusionModelCategoryMapper, SdFusionModelCategory>
    implements ISdFusionModelCategoryService {

    @Override
    public List<SdFusionModelCategoryVO> selectAllSdFusionModelCategoryVOList() {
        return Optional.ofNullable(this.list(Wrappers.<SdFusionModelCategory>lambdaQuery()
                        .eq(SdFusionModelCategory::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(SdFusionModelCategory::getSort)
                        .orderByDesc(SdFusionModelCategory::getCreateTime)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::sdFusionModel2VO)
                .toList();
    }

    private SdFusionModelCategoryVO sdFusionModel2VO(SdFusionModelCategory sdFusionModelCategory) {
        SdFusionModelCategoryVO vo = new SdFusionModelCategoryVO();
        BeanUtils.copyProperties(sdFusionModelCategory, vo);
        return vo;
    }
}




