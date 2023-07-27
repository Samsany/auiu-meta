package com.auiucloud.component.cms.controller;

import com.auiucloud.component.cms.domain.SwiperAdv;
import com.auiucloud.component.cms.service.ISwiperAdvService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
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
@Tag(name = "轮播广告管理")
@RestController
@AllArgsConstructor
@RequestMapping("/swiper-adv")
public class SwiperAdvController extends BaseController {

    private final ISwiperAdvService swiperAdvService;

    /**
     * 查询轮播广告列表
     */
    @Log(value = "轮播广告", exception = "查询轮播广告列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询轮播广告列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(Search search, @Parameter(hidden = true) SwiperAdv swiperAdv) {
        return ApiResult.data(swiperAdvService.listPage(search, swiperAdv));
    }

    /**
     * 获取轮播广告详情
     */
    @Log(value = "轮播广告", exception = "获取轮播广告详情请求异常")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取轮播广告详情", description = "根据id获取轮播广告详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(swiperAdvService.getById(id));
    }

    /**
     * 新增轮播广告
     */
    @Log(value = "轮播广告", exception = "新增轮播广告请求异常")
    @PostMapping
    @Operation(summary = "新增轮播广告")
    public ApiResult<?> add(@RequestBody SwiperAdv swiperAdv) {
        return ApiResult.condition(swiperAdvService.save(swiperAdv));
    }

    /**
     * 修改轮播广告
     */
    @Log(value = "轮播广告", exception = "修改轮播广告请求异常")
    @PutMapping
    @Operation(summary = "修改轮播广告")
    public ApiResult<?> edit(@RequestBody SwiperAdv swiperAdv) {
        return ApiResult.condition(swiperAdvService.updateById(swiperAdv));
    }

    /**
     * 设置轮播广告状态
     */
    @Log(value = "轮播广告", exception = "修改轮播广告请求异常")
    @PutMapping("/setStatus")
    @Operation(summary = "修改轮播广告状态")
    public ApiResult<?> setStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(swiperAdvService.setStatus(updateStatusDTO));
    }

    /**
     * 删除轮播广告
     */
    @Log(value = "轮播广告", exception = "删除轮播广告请求异常")
    @DeleteMapping("/{swiperAdvId}")
    @Parameter(name = "swiperAdvId", description = "轮播广告ID", in = ParameterIn.PATH)
    @Operation(summary = "删除轮播广告")
    public ApiResult<?> remove(@PathVariable Long swiperAdvId) {
        return ApiResult.condition(swiperAdvService.removeById(swiperAdvId));
    }
}
