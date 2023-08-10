package com.auiucloud.component.sd.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdModel;
import com.auiucloud.component.sd.service.ISdModelService;
import com.auiucloud.component.sd.vo.SdModelConfigVO;
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
@Tag(name = "Ai绘画模型管理")
@RestController
@AllArgsConstructor
@RequestMapping("/ai-draw")
public class SdModelController extends BaseController {

    private final ISdModelService sdModelService;

    /**
     * 查询Ai绘画模型管理列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询Ai绘画模型列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(@Parameter(hidden = true) Search search, @Parameter(hidden = true) SdModel model) {
        return ApiResult.data(sdModelService.listPage(search, model));
    }

    /**
     * 获取Ai绘画模型管理详情
     */
    @GetMapping
    @Operation(summary = "获取Ai绘画模型详情", description = "根据id获取Ai绘画模型详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> getInfo(@RequestParam Long id) {
        return ApiResult.data(sdModelService.getSdModelConfigVOById(id));
    }

    /**
     * 新增Ai绘画模型管理
     */
    @Log(value = "Ai绘画模型管理", exception = "新增Ai绘画模型请求异常")
    @PostMapping
    @Operation(summary = "新增Ai绘画模型")
    public ApiResult<?> add(@Validated @RequestBody SdModelConfigVO model) {
        return ApiResult.condition(sdModelService.saveBySdModelConfigVO(model));
    }

    /**
     * 修改Ai绘画模型管理
     */
    @Log(value = "Ai绘画模型管理", exception = "修改Ai绘画模型请求异常")
    @PutMapping
    @Operation(summary = "修改Ai绘画模型")
    public ApiResult<?> edit(@Validated @RequestBody SdModelConfigVO model) {
        return ApiResult.condition(sdModelService.updateSdModelConfigVOById(model));
    }

    /**
     * 设置Ai绘画模型管理状态
     */
    @Log(value = "Ai绘画模型管理", exception = "修改Ai绘画模型管理请求异常")
    @PutMapping("/set-status")
    @Operation(summary = "修改Ai绘画模型状态")
    public ApiResult<?> setSdModelStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(sdModelService.setSdModelStatus(updateStatusDTO));
    }

    /**
     * 删除Ai绘画模型管理
     */
    @Log(value = "Ai绘画模型管理", exception = "删除Ai绘画模型请求异常")
    @DeleteMapping
    @Parameter(name = "id", description = "Ai绘画模型ID", in = ParameterIn.QUERY)
    @Operation(summary = "删除Ai绘画模型")
    public ApiResult<?> remove(@RequestParam Long id) {
        return ApiResult.condition(sdModelService.removeById(id));
    }
}
