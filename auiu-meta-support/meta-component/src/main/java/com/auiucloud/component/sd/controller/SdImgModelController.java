package com.auiucloud.component.sd.controller;

import com.auiucloud.component.sd.domain.SdImageModel;
import com.auiucloud.component.sd.domain.SdModel;
import com.auiucloud.component.sd.service.ISdImageModelService;
import com.auiucloud.component.sd.service.ISdModelService;
import com.auiucloud.component.sd.vo.SdModelConfigVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author dries
 **/
@Tag(name = "Ai绘画推荐作品管理")
@RestController
@AllArgsConstructor
@RequestMapping("/ai-draw-img")
public class SdImgModelController extends BaseController {

    private final ISdImageModelService sdImageModelService;

    /**
     * 查询Ai绘画推荐作品管理列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询Ai绘画推荐作品列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(@Parameter(hidden = true) Search search, @Parameter(hidden = true) SdImageModel model) {
        return ApiResult.data(sdImageModelService.listPage(search, model));
    }

    /**
     * 获取Ai绘画推荐作品管理详情
     */
    @GetMapping
    @Operation(summary = "获取Ai绘画推荐作品详情", description = "根据id获取Ai绘画推荐作品详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> getInfo(@RequestParam Long id) {
        return ApiResult.data(sdImageModelService.getById(id));
    }

    /**
     * 新增Ai绘画推荐作品管理
     */
    @Log(value = "Ai绘画推荐作品管理", exception = "新增Ai绘画推荐作品请求异常")
    @PostMapping
    @Operation(summary = "新增Ai绘画推荐作品")
    public ApiResult<?> add(@Validated @RequestBody SdImageModel model) {
        return ApiResult.condition(sdImageModelService.save(model));
    }

    /**
     * 修改Ai绘画推荐作品管理
     */
    @Log(value = "Ai绘画推荐作品管理", exception = "修改Ai绘画推荐作品请求异常")
    @PutMapping
    @Operation(summary = "修改Ai绘画推荐作品")
    public ApiResult<?> edit(@Validated @RequestBody SdImageModel model) {
        return ApiResult.condition(sdImageModelService.updateById(model));
    }

    /**
     * 删除Ai绘画推荐作品管理
     */
    @Log(value = "Ai绘画推荐作品管理", exception = "删除Ai绘画推荐作品请求异常")
    @DeleteMapping
    @Parameter(name = "ids", description = "Ai绘画推荐作品ID", in = ParameterIn.QUERY)
    @Operation(summary = "删除Ai绘画推荐作品")
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        return ApiResult.condition(sdImageModelService.removeByIds(Arrays.asList(ids)));
    }

}
