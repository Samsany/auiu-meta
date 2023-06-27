package com.auiucloud.component.sd.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdPicQuality;
import com.auiucloud.component.cms.service.IPicQualityService;
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
@Tag(name = "图片质量管理")
@RestController
@AllArgsConstructor
@RequestMapping("/pic-quality")
public class SdPicQualityController extends BaseController {

    private final IPicQualityService picQualityService;

    /**
     * 查询图片质量列表
     */
    @Log(value = "图片质量", exception = "查询图片质量列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询图片质量列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(@Parameter(hidden = true) Search search, @Parameter(hidden = true) SdPicQuality quality) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        return switch (mode) {
            case LIST ->
                    ApiResult.data(picQualityService.list(Wrappers.<SdPicQuality>lambdaQuery()
                            .like(StrUtil.isNotBlank(search.getKeyword()), SdPicQuality::getTitle, search.getKeyword())
                            .eq(ObjectUtil.isNotNull(search.getStatus()), SdPicQuality::getStatus, quality.getStatus())
                            .orderByDesc(SdPicQuality::getSort)
                            .orderByDesc(SdPicQuality::getCreateTime)
                            .orderByDesc(SdPicQuality::getId)
                    ));
            default -> ApiResult.data(picQualityService.listPage(search, quality));
        };
    }

    /**
     * 获取图片质量详情
     */
    @Log(value = "图片质量", exception = "获取图片质量详情请求异常")
    @GetMapping
    @Operation(summary = "获取图片质量详情", description = "根据id获取图片质量详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> getInfo(@RequestParam Long id) {
        return ApiResult.data(picQualityService.getById(id));
    }

    /**
     * 新增图片质量
     */
    @Log(value = "图片质量", exception = "新增图片质量请求异常")
    @PostMapping
    @Operation(summary = "新增图片质量")
    public ApiResult<?> add(@RequestBody SdPicQuality quality) {
        if (picQualityService.checkPicQualityNameExist(quality)) {
            return ApiResult.fail("新增'" + quality.getTitle() + "'失败，名称已存在");
        }
        return ApiResult.condition(picQualityService.save(quality));
    }

    /**
     * 修改图片质量
     */
    @Log(value = "图片质量", exception = "修改图片质量请求异常")
    @PutMapping
    @Operation(summary = "修改图片质量")
    public ApiResult<?> edit(@RequestBody SdPicQuality quality) {
        if (picQualityService.checkPicQualityNameExist(quality)) {
            return ApiResult.fail("修改'" + quality.getTitle() + "'失败，名称已存在");
        }
        return ApiResult.condition(picQualityService.updateById(quality));
    }

    /**
     * 设置图片质量状态
     */
    @Log(value = "图片质量", exception = "修改图片质量请求异常")
    @PutMapping("/set-status")
    @Operation(summary = "修改图片质量状态")
    public ApiResult<?> setPicQualityStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(picQualityService.setPicQualityStatus(updateStatusDTO));
    }

    /**
     * 删除图片质量
     */
    @Log(value = "图片质量", exception = "删除图片质量请求异常")
    @DeleteMapping
    @Parameter(name = "id", description = "图片质量ID", in = ParameterIn.QUERY)
    @Operation(summary = "删除图片质量")
    public ApiResult<?> remove(@RequestParam Long id) {
        return ApiResult.condition(picQualityService.removeById(id));
    }
}
