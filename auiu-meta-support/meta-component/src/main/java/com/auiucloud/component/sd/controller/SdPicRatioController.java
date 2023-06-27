package com.auiucloud.component.sd.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdPicRatio;
import com.auiucloud.component.cms.service.IPicRatioService;
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
@Tag(name = "图片比例管理")
@RestController
@AllArgsConstructor
@RequestMapping("/pic-ratio")
public class SdPicRatioController extends BaseController {

    private final IPicRatioService picRatioService;

    /**
     * 查询图片比例列表
     */
    @Log(value = "图片比例", exception = "查询图片比例列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询图片比例列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(@Parameter(hidden = true) Search search, @Parameter(hidden = true) SdPicRatio ratio) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        return switch (mode) {
            case LIST ->
                    ApiResult.data(picRatioService.list(Wrappers.<SdPicRatio>lambdaQuery()
                            .like(StrUtil.isNotBlank(search.getKeyword()), SdPicRatio::getTitle, search.getKeyword())
                            .eq(ObjectUtil.isNotNull(search.getStatus()), SdPicRatio::getStatus, search.getStatus())
                            .orderByDesc(SdPicRatio::getSort)
                            .orderByDesc(SdPicRatio::getCreateTime)
                            .orderByDesc(SdPicRatio::getId)
                    ));
            default -> ApiResult.data(picRatioService.listPage(search, ratio));
        };
    }

    /**
     * 获取图片比例详情
     */
    @Log(value = "图片比例", exception = "获取图片比例详情请求异常")
    @GetMapping
    @Operation(summary = "获取图片比例详情", description = "根据id获取图片比例详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> getInfo(@RequestParam Long id) {
        return ApiResult.data(picRatioService.getById(id));
    }

    /**
     * 新增图片比例
     */
    @Log(value = "图片比例", exception = "新增图片比例请求异常")
    @PostMapping
    @Operation(summary = "新增图片比例")
    public ApiResult<?> add(@RequestBody SdPicRatio ratio) {
        if (picRatioService.checkPicRatioNameExist(ratio)) {
            return ApiResult.fail("新增'" + ratio.getTitle() + "'失败，名称已存在");
        }
        return ApiResult.condition(picRatioService.save(ratio));
    }

    /**
     * 修改图片比例
     */
    @Log(value = "图片比例", exception = "修改图片比例请求异常")
    @PutMapping
    @Operation(summary = "修改图片比例")
    public ApiResult<?> edit(@RequestBody SdPicRatio ratio) {
        if (picRatioService.checkPicRatioNameExist(ratio)) {
            return ApiResult.fail("修改'" + ratio.getTitle() + "'失败，名称已存在");
        }
        return ApiResult.condition(picRatioService.updateById(ratio));
    }

    /**
     * 设置图片比例状态
     */
    @Log(value = "图片比例", exception = "修改图片比例请求异常")
    @PutMapping("/set-status")
    @Operation(summary = "修改图片比例状态")
    public ApiResult<?> setPicRatioStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(picRatioService.setPicRatioStatus(updateStatusDTO));
    }

    /**
     * 删除图片比例
     */
    @Log(value = "图片比例", exception = "删除图片比例请求异常")
    @DeleteMapping
    @Parameter(name = "id", description = "图片比例ID", in = ParameterIn.QUERY)
    @Operation(summary = "删除图片比例")
    public ApiResult<?> remove(@RequestParam Long id) {
        return ApiResult.condition(picRatioService.removeById(id));
    }
}
