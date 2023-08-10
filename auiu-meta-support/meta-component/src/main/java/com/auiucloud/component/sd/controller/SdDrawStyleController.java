package com.auiucloud.component.sd.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.domain.SdDrawStyle;
import com.auiucloud.component.sd.service.ISdDrawStyleService;
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
@Tag(name = "绘画风格管理")
@RestController
@AllArgsConstructor
@RequestMapping("/draw-style")
public class SdDrawStyleController extends BaseController {

    private final ISdDrawStyleService sdDrawStyleService;

    /**
     * 查询绘画风格列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询绘画风格列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(@Parameter(hidden = true) Search search, @Parameter(hidden = true) SdDrawStyle drawStyle) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        return switch (mode) {
            case LIST ->
                    ApiResult.data(sdDrawStyleService.list(Wrappers.<SdDrawStyle>lambdaQuery()
                            .like(StrUtil.isNotBlank(search.getKeyword()), SdDrawStyle::getName, search.getKeyword())
                            .eq(ObjectUtil.isNotNull(search.getStatus()), SdDrawStyle::getStatus, drawStyle.getStatus())
                            .eq(ObjectUtil.isNotNull(drawStyle.getCateId()), SdDrawStyle::getCateId, drawStyle.getCateId())
                            .orderByDesc(SdDrawStyle::getSort)
                            .orderByDesc(SdDrawStyle::getCreateTime)
                            .orderByDesc(SdDrawStyle::getId)
                    ));
            default -> ApiResult.data(sdDrawStyleService.listPage(search, drawStyle));
        };
    }

    /**
     * 获取绘画风格详情
     */
    @GetMapping
    @Operation(summary = "获取绘画风格详情", description = "根据id获取绘画风格详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> getInfo(@RequestParam Long id) {
        return ApiResult.data(sdDrawStyleService.getById(id));
    }

    /**
     * 新增绘画风格
     */
    @Log(value = "绘画风格", exception = "新增绘画风格请求异常")
    @PostMapping
    @Operation(summary = "新增绘画风格")
    public ApiResult<?> add(@RequestBody SdDrawStyle drawStyle) {
        if (sdDrawStyleService.checkDrawStyleNameExist(drawStyle)) {
            return ApiResult.fail("新增'" + drawStyle.getName() + "'失败，名称已存在");
        }
        return ApiResult.condition(sdDrawStyleService.save(drawStyle));
    }

    /**
     * 修改绘画风格
     */
    @Log(value = "绘画风格", exception = "修改绘画风格请求异常")
    @PutMapping
    @Operation(summary = "修改绘画风格")
    public ApiResult<?> edit(@RequestBody SdDrawStyle drawStyle) {
        if (sdDrawStyleService.checkDrawStyleNameExist(drawStyle)) {
            return ApiResult.fail("修改'" + drawStyle.getName() + "'失败，名称已存在");
        }
        return ApiResult.condition(sdDrawStyleService.updateById(drawStyle));
    }

    /**
     * 设置绘画风格状态
     */
    @Log(value = "绘画风格", exception = "修改绘画风格请求异常")
    @PutMapping("/set-status")
    @Operation(summary = "修改绘画风格状态")
    public ApiResult<?> setDrawStyleStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(sdDrawStyleService.setDrawStyleStatus(updateStatusDTO));
    }

    /**
     * 删除绘画风格
     */
    @Log(value = "绘画风格", exception = "删除绘画风格请求异常")
    @DeleteMapping
    @Parameter(name = "id", description = "绘画风格ID", in = ParameterIn.QUERY)
    @Operation(summary = "删除绘画风格")
    public ApiResult<?> remove(@RequestParam Long id) {
        return ApiResult.condition(sdDrawStyleService.removeById(id));
    }
}
