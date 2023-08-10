package com.auiucloud.component.sd.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdFusionModel;
import com.auiucloud.component.sd.domain.SdFusionModel;
import com.auiucloud.component.sd.service.ISdFusionModelService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.enums.QueryModeEnum;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.web.controller.BaseController;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author dries
 **/
@Tag(name = "融合模型管理")
@RestController
@AllArgsConstructor
@RequestMapping("/lora")
public class SdFusionModelController extends BaseController {

    private final ISdFusionModelService sdFusionModelService;

    /**
     * 查询融合模型列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询融合模型列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(@Parameter(hidden = true) Search search, @Parameter(hidden = true) SdFusionModel fusionModel) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        return switch (mode) {
            case LIST ->
                    ApiResult.data(sdFusionModelService.list(Wrappers.<SdFusionModel>lambdaQuery()
                            .like(StrUtil.isNotBlank(search.getKeyword()), SdFusionModel::getName, search.getKeyword())
                            .eq(ObjectUtil.isNotNull(search.getStatus()), SdFusionModel::getStatus, fusionModel.getStatus())
                            .eq(ObjectUtil.isNotNull(fusionModel.getCateId()), SdFusionModel::getCateId, fusionModel.getCateId())
                            .orderByDesc(SdFusionModel::getSort)
                            .orderByDesc(SdFusionModel::getCreateTime)
                            .orderByDesc(SdFusionModel::getId)
                    ));
            default -> ApiResult.data(sdFusionModelService.listPage(search, fusionModel));
        };
    }

    /**
     * 获取融合模型详情
     */
    @GetMapping
    @Operation(summary = "获取融合模型详情", description = "根据id获取融合模型详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> getInfo(@RequestParam Long id) {
        return ApiResult.data(sdFusionModelService.getById(id));
    }

    /**
     * 新增融合模型
     */
    @Log(value = "融合模型", exception = "新增融合模型请求异常")
    @PostMapping
    @Operation(summary = "新增融合模型")
    public ApiResult<?> add(@RequestBody SdFusionModel fusionModel) {
        if (sdFusionModelService.checkFusionModelNameExist(fusionModel)) {
            return ApiResult.fail("新增'" + fusionModel.getName() + "'失败，名称已存在");
        }
        return ApiResult.condition(sdFusionModelService.save(fusionModel));
    }

    /**
     * 修改融合模型
     */
    @Log(value = "融合模型", exception = "修改融合模型请求异常")
    @PutMapping
    @Operation(summary = "修改融合模型")
    public ApiResult<?> edit(@RequestBody SdFusionModel fusionModel) {
        if (sdFusionModelService.checkFusionModelNameExist(fusionModel)) {
            return ApiResult.fail("修改'" + fusionModel.getName() + "'失败，名称已存在");
        }
        return ApiResult.condition(sdFusionModelService.updateById(fusionModel));
    }

    /**
     * 设置融合模型状态
     */
    @Log(value = "融合模型", exception = "修改融合模型请求异常")
    @PutMapping("/set-status")
    @Operation(summary = "修改融合模型状态")
    public ApiResult<?> setFusionModelStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(sdFusionModelService.setFusionModelStatus(updateStatusDTO));
    }

    /**
     * 删除融合模型
     */
    @Log(value = "融合模型", exception = "删除融合模型请求异常")
    @DeleteMapping
    @Parameter(name = "id", description = "融合模型ID", in = ParameterIn.QUERY)
    @Operation(summary = "删除融合模型")
    public ApiResult<?> remove(@RequestParam Long id) {
        return ApiResult.condition(sdFusionModelService.removeById(id));
    }
}
