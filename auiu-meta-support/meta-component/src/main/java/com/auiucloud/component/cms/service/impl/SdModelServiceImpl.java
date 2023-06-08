package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.cms.domain.SdModel;
import com.auiucloud.component.cms.mapper.SdModelMapper;
import com.auiucloud.component.cms.service.ISdFusionModelService;
import com.auiucloud.component.cms.service.ISdModelService;
import com.auiucloud.component.cms.vo.SdFusionModelVO;
import com.auiucloud.component.cms.vo.SdModelVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.utils.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dries
 * @description 针对表【cms_sd_model(SD模型主题表)】的数据库操作Service实现
 * @createDate 2023-05-21 23:07:20
 */
@Service
@RequiredArgsConstructor
public class SdModelServiceImpl extends ServiceImpl<SdModelMapper, SdModel>
        implements ISdModelService {

    private final ISdFusionModelService sdFusionModelService;

    @Override
    public List<SdModelVO> selectSdModelVOListByCId(Long aiDrawId) {
        // 融合模型
        List<SdFusionModelVO> sdFusionModelList = sdFusionModelService.selectAllSdFusionModelVOList();
        return Optional.ofNullable(this.list(Wrappers.<SdModel>lambdaQuery()
                        .eq(SdModel::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .eq(SdModel::getCateId, aiDrawId)
                        .orderByDesc(SdModel::getSort)
                        .orderByDesc(SdModel::getCreateTime)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(item -> sdFusionModel2VO(item, sdFusionModelList))
                .toList();
    }

    private SdModelVO sdFusionModel2VO(SdModel sdModel, List<SdFusionModelVO> sdFusionModelList) {
        SdModelVO vo = new SdModelVO();
        BeanUtils.copyProperties(sdModel, vo);
        // 组装融合模型
        if (StrUtil.isNotBlank(sdModel.getFusionModal())) {
            List<String> fusionModalIds = Arrays.asList(sdModel.getFusionModal().split(StringPool.COMMA));
            List<SdFusionModelVO> sdFusionModelVOList = sdFusionModelList.parallelStream()
                    .filter(it -> fusionModalIds.contains(String.valueOf(it.getId())))
                    .toList();
            vo.setFusionModelList(sdFusionModelVOList);
        } else {
            vo.setFusionModelList(Collections.emptyList());
        }

        return vo;
    }

}




