package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.component.cms.service.IPicQualityService;
import com.auiucloud.component.cms.service.IPicRatioService;
import com.auiucloud.component.sd.domain.SdModel;
import com.auiucloud.component.sd.domain.SdModelConfig;
import com.auiucloud.component.sd.mapper.SdModelMapper;
import com.auiucloud.component.sd.service.*;
import com.auiucloud.component.sd.vo.*;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.FileUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【sd_model(SD模型主题表)】的数据库操作Service实现
 * @createDate 2023-05-21 23:07:20
 */
@Service
@RequiredArgsConstructor
public class SdModelServiceImpl extends ServiceImpl<SdModelMapper, SdModel>
        implements ISdModelService {

    private final IPicRatioService picRatioService;
    private final IPicQualityService picQualityService;
    private final ISdDrawStyleService sdDrawStyleService;
    private final ISdFusionModelService sdFusionModelService;
    private final ISdDrawStyleCategoryService sdDrawStyleCategoryService;
    private final ISdFusionModelCategoryService sdFusionModelCategoryService;

    @Override
    public List<SdModelVO> selectSdModelVOListByCateId(Long cateId) {
        LambdaQueryWrapper<SdModel> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SdModel::getCateId, cateId);
        queryWrapper.eq(SdModel::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        queryWrapper.orderByDesc(SdModel::getSort);
        queryWrapper.orderByDesc(SdModel::getCreateTime);
        queryWrapper.orderByDesc(SdModel::getId);
        List<SdModel> list = Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
        return list.parallelStream().map(this::covert2SdModelVO).collect(Collectors.toList());
    }

    @Override
    public PageUtils listPage(Search search, SdModel model) {
        LambdaQueryWrapper<SdModel> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SdModel::getName, search.getKeyword());
        queryWrapper.eq(ObjectUtil.isNotNull(search.getStatus()), SdModel::getStatus, search.getStatus());
        queryWrapper.eq(ObjectUtil.isNotNull(model.getCateId()), SdModel::getCateId, model.getCateId());
        queryWrapper.orderByDesc(SdModel::getSort);
        queryWrapper.orderByDesc(SdModel::getCreateTime);
        queryWrapper.orderByDesc(SdModel::getId);
        IPage<SdModel> page = this.page(PageUtils.getPage(search), queryWrapper);
        page.convert(this::covert2SdModelConfigVO);
        return new PageUtils(page);
    }

    @Override
    public SdModelConfigVO getSdModelConfigVOById(Long id) {
        SdModel sdModel = this.getById(id);
        if (ObjectUtil.isNotNull(sdModel)) {
            return covert2SdModelConfigVO(sdModel);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBySdModelConfigVO(SdModelConfigVO model) {
        return this.saveOrUpdateSdModelConfigVO(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSdModelConfigVOById(SdModelConfigVO model) {
        return this.saveOrUpdateSdModelConfigVO(model);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateSdModelConfigVO(SdModelConfigVO model) {
        SdModel sdModel = new SdModel();
        BeanUtils.copyProperties(model, sdModel);
        if (this.checkSdModelNameExist(sdModel)) {
            String operation = "新增";
            if (ObjectUtil.isNotNull(model.getId())) {
                operation = "编辑";
            }
            throw new ApiException(operation + model.getName() + "'失败，名称已存在");
        }

        SdModelConfig sdModelConfig = new SdModelConfig();
        BeanUtils.copyProperties(model, sdModelConfig);
        String jsonStr = JSONUtil.toJsonStr(sdModelConfig);
        sdModel.setConfig(jsonStr);
        String modelHash = model.getSha256();
        if (StrUtil.isNotBlank(modelHash)) {
            sdModel.setModelHash(StrUtil.sub(modelHash, 0, 10));
        }
        sdModel.setModelName(FileUtil.getFileNameNoEx(sdModel.getFilename()));
        return this.saveOrUpdate(sdModel);
    }

    @Override
    public boolean checkSdModelNameExist(SdModel model) {
        LambdaQueryWrapper<SdModel> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(model.getId()), SdModel::getId, model.getId());
        queryWrapper.eq(SdModel::getName, model.getName());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean setSdModelStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<SdModel> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SdModel::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(SdModel::getId, updateStatusDTO.getId());
        return this.update(wrapper);
    }

    @Override
    public List<SdModelVO> selectSdModelVOListByCId(Long aiDrawId) {
        // 融合模型
        return Optional.ofNullable(this.list(Wrappers.<SdModel>lambdaQuery()
                        .eq(SdModel::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .eq(SdModel::getCateId, aiDrawId)
                        .orderByDesc(SdModel::getSort)
                        .orderByDesc(SdModel::getCreateTime)
                        .orderByDesc(SdModel::getId)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::sdFusionModel2VO)
                .toList();
    }

    @NotNull
    private SdModelConfigVO covert2SdModelConfigVO(SdModel sdModel) {
        SdModelConfigVO sdModelConfigVO = new SdModelConfigVO();
        BeanUtils.copyProperties(sdModel, sdModelConfigVO);
        String config = sdModel.getConfig();
        if (StrUtil.isNotBlank(config)) {
            SdModelConfig sdModelConfig = JSONUtil.toBean(config, SdModelConfig.class);
            BeanUtils.copyProperties(sdModelConfig, sdModelConfigVO);
        }
        return sdModelConfigVO;
    }

    private SdModelVO covert2SdModelVO(SdModel sdModel) {
        SdModelVO sdModelVO = new SdModelVO();
        BeanUtils.copyProperties(sdModel, sdModelVO);
        String config = sdModel.getConfig();
        if (StrUtil.isNotBlank(config)) {
            SdModelConfig sdModelConfig = JSONUtil.toBean(config, SdModelConfig.class);
            // 尺寸比例
            List<Long> picRatios = sdModelConfig.getPicRatios();
            List<SdPicRatioVO> picRatioList = picRatioService.selectPicRatioVOListByIds(picRatios);
            sdModelVO.setPicRatioList(picRatioList);

            // 图片质量
            List<Long> picQualities = sdModelConfig.getPicQualities();
            List<SdPicQualityVO> sdPicQualityList = picQualityService.selectPicQualityVOListByIds(picQualities);
            sdModelVO.setPicQualityList(sdPicQualityList);

            // 默认风格styles
            List<Long> defaultStyles = sdModelConfig.getDefaultStyles();
            List<SdDrawStyleVO> sdDrawDefaultStyleList = sdDrawStyleService.selectSdDrawVOListByIds(defaultStyles);
            sdModelVO.setDefaultStyleList(sdDrawDefaultStyleList);

            // 可选风格styles
            List<Long> styles = sdModelConfig.getStyles();
            List<SdDrawStyleVO> sdDrawStyleList = sdDrawStyleService.selectSdDrawVOListByIds(styles);
            sdModelVO.setStyleList(sdDrawStyleList);
            // 风格style分类
            List<Long> cateIds = sdDrawStyleList.parallelStream().map(SdDrawStyleVO::getCateId).toList();
            List<SdDrawStyleCategoryVO> sdDrawCateList = sdDrawStyleCategoryService.selectSdDrawCategoryVOListByIds(cateIds);
            sdModelVO.setSdDrawStyleCateList(sdDrawCateList);

            // 默认Lora融合模型
            List<SdLoraVO> defaultLoraList = sdModelConfig.getDefaultLoraList();
            List<Long> defaultLoraIds = Optional.ofNullable(defaultLoraList).orElse(Collections.emptyList())
                    .parallelStream().map(SdLoraVO::getId).toList();
            List<SdFusionModelVO> defaultSdFusionModels = sdFusionModelService.selectSdFusionModelVOListByIds(defaultLoraIds);
            sdModelVO.setDefaultLoraList(defaultSdFusionModels);

            // 可选的Lora融合模型
            List<SdLoraVO> loraList = sdModelConfig.getLoraList();
            List<Long> loraIds = Optional.ofNullable(loraList).orElse(Collections.emptyList())
                    .parallelStream().map(SdLoraVO::getId).toList();
            List<SdFusionModelVO> sdFusionModels = sdFusionModelService.selectSdFusionModelVOListByIds(loraIds);
            sdFusionModels.parallelStream()
                    .forEach(it -> {
                        if (CollUtil.isNotEmpty(loraList)) {
                            for (SdLoraVO sdLoraVO : loraList) {
                                if (sdLoraVO.getId().equals(it.getId())) {
                                    it.setDefaultValue(Double.valueOf(sdLoraVO.getValue()));
                                    break;
                                }
                            }
                        }
                    });
            sdModelVO.setLoraList(sdFusionModels);
            // Lora融合模型分类
            List<Long> loraCateIds = sdFusionModels.parallelStream().map(SdFusionModelVO::getCateId).toList();
            List<SdFusionModelCategoryVO> fusionModelCateList = sdFusionModelCategoryService.selectSdFusionModelCategoryVOListByIds(loraCateIds);
            sdModelVO.setLoraCateList(fusionModelCateList);

            // 高级设置
            sdModelVO.setRestoreFaces(sdModelConfig.getRestoreFaces().equals(CommonConstant.YES_VALUE));
            sdModelVO.setEnableHr(sdModelConfig.getEnableHr().equals(CommonConstant.YES_VALUE));
            sdModelVO.setTiling(sdModelConfig.getTiling().equals(CommonConstant.YES_VALUE));

            // 采样方法
            sdModelVO.setSampler(sdModelConfig.getDefaultSampler());
            sdModelVO.setSamplerList(sdModelConfig.getSamplerList());

            sdModelVO.setSteps(sdModelConfig.getSteps());
            sdModelVO.setCfgScale(sdModelConfig.getCfgScale());

            sdModelVO.setEmbeddings(sdModelConfig.getEmbeddings());
            sdModelVO.setDefaultEmbeddings(sdModelConfig.getDefaultEmbeddings());
        }
        return sdModelVO;
    }

    private SdModelVO sdFusionModel2VO(SdModel sdModel) {
        SdModelVO vo = new SdModelVO();
        BeanUtils.copyProperties(sdModel, vo);

        return vo;
    }

}




