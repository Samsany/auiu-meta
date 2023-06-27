package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdDrawStyleCategory;
import com.auiucloud.component.sd.domain.SdFusionModelCategory;
import com.auiucloud.component.sd.vo.SdFusionModelVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.sd.domain.SdFusionModel;
import com.auiucloud.component.sd.service.ISdFusionModelService;
import com.auiucloud.component.sd.mapper.SdFusionModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* @author dries
* @description 针对表【sd_fusion_model(SD融合模型表)】的数据库操作Service实现
* @createDate 2023-05-21 23:07:20
*/
@Service
public class SdFusionModelServiceImpl extends ServiceImpl<SdFusionModelMapper, SdFusionModel>
    implements ISdFusionModelService {

    @Override
    public PageUtils listPage(Search search, SdFusionModel fusionModel) {
        LambdaQueryWrapper<SdFusionModel> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SdFusionModel::getName, search.getKeyword());
        queryWrapper.eq(ObjectUtil.isNotNull(search.getStatus()), SdFusionModel::getStatus, search.getStatus());
        queryWrapper.eq(ObjectUtil.isNotNull(fusionModel.getCateId()), SdFusionModel::getCateId, fusionModel.getCateId());
        queryWrapper.orderByDesc(SdFusionModel::getSort);
        queryWrapper.orderByDesc(SdFusionModel::getCreateTime);
        queryWrapper.orderByDesc(SdFusionModel::getId);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean checkFusionModelNameExist(SdFusionModel fusionModel) {
        LambdaQueryWrapper<SdFusionModel> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(fusionModel.getId()), SdFusionModel::getId, fusionModel.getId());
        queryWrapper.eq(SdFusionModel::getName, fusionModel.getName());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean setFusionModelStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<SdFusionModel> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SdFusionModel::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(SdFusionModel::getId, updateStatusDTO.getId());
        return this.update(wrapper);
    }

    @Override
    public List<SdFusionModelVO> selectAllSdFusionModelVOList() {
        return Optional.ofNullable(this.list(Wrappers.<SdFusionModel>lambdaQuery()
                        .eq(SdFusionModel::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(SdFusionModel::getSort)
                        .orderByDesc(SdFusionModel::getCreateTime)
                        .orderByDesc(SdFusionModel::getId)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::sdFusionModel2VO)
                .toList();
    }

    @Override
    public List<SdFusionModelVO> selectSdFusionModelVOListByIds(List<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            return Optional.ofNullable(this.list(Wrappers.<SdFusionModel>lambdaQuery()
                            .in(SdFusionModel::getId, ids)
                            .eq(SdFusionModel::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                            .orderByDesc(SdFusionModel::getSort)
                            .orderByDesc(SdFusionModel::getCreateTime)
                            .orderByDesc(SdFusionModel::getId)
                    ))
                    .orElse(Collections.emptyList())
                    .parallelStream()
                    .map(this::sdFusionModel2VO)
                    .toList();
        }

        return Collections.emptyList();
    }

    private SdFusionModelVO sdFusionModel2VO(SdFusionModel sdFusionModel) {
        SdFusionModelVO vo = new SdFusionModelVO();
        BeanUtils.copyProperties(sdFusionModel, vo);
        return vo;
    }
}




