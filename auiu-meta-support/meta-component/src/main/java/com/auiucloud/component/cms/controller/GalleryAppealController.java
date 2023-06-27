package com.auiucloud.component.cms.controller;

import com.auiucloud.component.cms.domain.GalleryAppeal;
import com.auiucloud.component.cms.dto.GalleryAppealDTO;
import com.auiucloud.component.cms.service.IGalleryAppealService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.validator.group.RejectGroup;
import com.auiucloud.core.validator.group.ResolveGroup;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author dries
 **/
@Tag(name = "作品管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gallery/appeal")
public class GalleryAppealController extends BaseController {

    private final IGalleryAppealService galleryAppealService;

    /**
     * 查询作品申诉列表
     */
    @Log(value = "作品申诉", exception = "查询作品申诉列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询作品申诉列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(Search search, @Parameter(hidden = true) GalleryAppeal galleryAppeal) {
        return ApiResult.data(galleryAppealService.listPage(search, galleryAppeal));
    }

    /**
     * 获取作品申诉详情
     */
    @Log(value = "作品申诉", exception = "获取作品申诉详情请求异常")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取作品申诉详情", description = "根据id获取作品申诉详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(galleryAppealService.getGalleryAppealVOById(id));
    }

    /**
     * 修改作品申诉
     */
    @Log(value = "作品申诉", exception = "修改作品申诉请求异常")
    @PutMapping
    @Operation(summary = "修改作品申诉")
    public ApiResult<?> edit(@RequestBody GalleryAppeal galleryAppeal) {
        return ApiResult.condition(galleryAppealService.updateById(galleryAppeal));
    }

    /**
     * 通过作品申诉
     */
    @Log(value = "作品申诉", exception = "修改作品申诉请求异常")
    @PutMapping("/resolve")
    @Operation(summary = "通过作品申诉")
    public ApiResult<?> resolve(@Validated({ResolveGroup.class}) @RequestBody GalleryAppealDTO galleryAppeal) {
        return ApiResult.condition(galleryAppealService.resolveGalleryAppeal(galleryAppeal));
    }

    /**
     * 驳回作品申诉
     */
    @Log(value = "作品申诉", exception = "修改作品申诉请求异常")
    @PutMapping("/reject")
    @Operation(summary = "驳回作品申诉")
    public ApiResult<?> reject(@Validated({RejectGroup.class}) @RequestBody GalleryAppealDTO galleryAppeal) {
        return ApiResult.condition(galleryAppealService.rejectGalleryAppeal(galleryAppeal));
    }

    /**
     * 删除作品申诉
     */
    @Log(value = "作品申诉", exception = "删除作品申诉请求异常")
    @DeleteMapping("/{id}")
    @Parameter(name = "id", description = "作品申诉ID", in = ParameterIn.PATH)
    @Operation(summary = "删除作品申诉")
    public ApiResult<?> remove(@PathVariable Long id) {
        return ApiResult.condition(galleryAppealService.removeById(id));
    }

}
