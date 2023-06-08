package com.auiucloud.component.cms.service.impl;

import com.auiucloud.component.cms.domain.PicRatio;
import com.auiucloud.component.cms.vo.PicRatioVO;
import com.auiucloud.component.cms.vo.SdFusionModelVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.cms.domain.SdFusionModel;
import com.auiucloud.component.cms.service.ISdFusionModelService;
import com.auiucloud.component.cms.mapper.SdFusionModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* @author dries
* @description 针对表【cms_sd_fusion_model(SD融合模型表)】的数据库操作Service实现
* @createDate 2023-05-21 23:07:20
*/
@Service
public class SdFusionModelServiceImpl extends ServiceImpl<SdFusionModelMapper, SdFusionModel>
    implements ISdFusionModelService {

    @Override
    public List<SdFusionModelVO> selectAllSdFusionModelVOList() {
        return Optional.ofNullable(this.list(Wrappers.<SdFusionModel>lambdaQuery()
                        .eq(SdFusionModel::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(SdFusionModel::getSort)
                        .orderByDesc(SdFusionModel::getCreateTime)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::sdFusionModel2VO)
                .toList();
    }

    private SdFusionModelVO sdFusionModel2VO(SdFusionModel sdFusionModel) {
        SdFusionModelVO vo = new SdFusionModelVO();
        BeanUtils.copyProperties(sdFusionModel, vo);
        return vo;
    }
}




