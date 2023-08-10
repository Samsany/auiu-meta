package com.auiucloud.component.cms.controller;

import com.auiucloud.component.cms.domain.GalleryReview;
import com.auiucloud.component.cms.dto.GalleryReviewBatchDTO;
import com.auiucloud.component.cms.dto.GalleryReviewDTO;
import com.auiucloud.component.cms.service.IGalleryReviewService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.validator.group.RejectGroup;
import com.auiucloud.core.validator.group.ResolveGroup;
import com.auiucloud.core.web.controller.BaseController;
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
@RequestMapping("/gallery/review")
public class GalleryReviewController extends BaseController {

    private final IGalleryReviewService galleryReviewService;

    /**
     * 查询作品审核列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询作品审核列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(Search search, @Parameter(hidden = true) GalleryReview galleryReview) {
        return ApiResult.data(galleryReviewService.listPage(search, galleryReview));
    }

    /**
     * 查询作品待审核数量
     */
    @Log(value = "作品审核", exception = "查询作品待审核数量请求异常")
    @GetMapping("/audit/count")
    @Operation(summary = "查询作品待审核数量")
    @Parameters({
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
            @Parameter(name = "startDate", description = "开始时间", in = ParameterIn.QUERY),
            @Parameter(name = "endDate", description = "结束时间", in = ParameterIn.QUERY),
    })
    public ApiResult<?> auditCount(Search search) {
        return ApiResult.data(galleryReviewService.auditCount(search));
    }

    /**
     * 获取作品审核详情
     */
    @Log(value = "作品审核", exception = "获取作品审核详情请求异常")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取作品审核详情", description = "根据id获取作品审核详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(galleryReviewService.getGalleryReviewVOById(id));
    }

    /**
     * 修改作品审核
     */
    @Log(value = "作品审核", exception = "修改作品审核请求异常")
    @PutMapping
    @Operation(summary = "修改作品审核")
    public ApiResult<?> edit(@RequestBody GalleryReview galleryReview) {
        return ApiResult.condition(galleryReviewService.updateById(galleryReview));
    }

    /**
     * 作品批量审核
     */
    @Log(value = "作品审核", exception = "修改作品审核请求异常")
    @PutMapping("/batch")
    @Operation(summary = "作品批量审核")
    public ApiResult<?> reviewBatch(@Validated @RequestBody GalleryReviewBatchDTO galleryReview) {
        return ApiResult.condition(galleryReviewService.galleryReviewBatch(galleryReview));
    }

    /**
     * 通过作品审核
     */
    @Log(value = "作品审核", exception = "修改作品审核请求异常")
    @PutMapping("/resolve")
    @Operation(summary = "通过作品审核")
    public ApiResult<?> resolve(@Validated({ResolveGroup.class}) @RequestBody GalleryReviewDTO galleryReview) {
        return ApiResult.condition(galleryReviewService.resolveGalleryReview(galleryReview));
    }

    /**
     * 驳回作品审核
     */
    @Log(value = "作品审核", exception = "修改作品审核请求异常")
    @PutMapping("/reject")
    @Operation(summary = "驳回作品审核")
    public ApiResult<?> reject(@Validated({RejectGroup.class}) @RequestBody GalleryReviewDTO galleryReview) {
        return ApiResult.condition(galleryReviewService.rejectGalleryReview(galleryReview));
    }

    /**
     * 撤回作品审核操作
     */
    @Log(value = "作品审核", exception = "修改作品审核请求异常")
    @PutMapping("/withdraw")
    @Operation(summary = "撤回作品审核操作")
    public ApiResult<?> withdraw(@Validated @RequestBody GalleryReviewDTO galleryReview) {
        return ApiResult.condition(galleryReviewService.withdrawGalleryReview(galleryReview));
    }

    /**
     * 删除作品审核
     */
    @Log(value = "作品审核", exception = "删除作品审核请求异常")
    @DeleteMapping("/{id}")
    @Parameter(name = "id", description = "作品审核ID", in = ParameterIn.PATH)
    @Operation(summary = "删除作品审核")
    public ApiResult<?> remove(@PathVariable Long id) {
        return ApiResult.condition(galleryReviewService.removeById(id));
    }

}
