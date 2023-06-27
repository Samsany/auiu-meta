package com.auiucloud.component.cms.controller;

import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.component.cms.service.IGalleryCollectionService;
import com.auiucloud.component.cms.vo.GalleryCollectionVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.validator.group.UpdateGroup;
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
@RequestMapping("/gallery-collection")
public class GalleryCollectionController {

    private final IGalleryCollectionService galleryCollectionService;

    /**
     * 查询作品合集列表
     */
    @Log(value = "作品合集")
    @GetMapping("/list")
    @Operation(summary = "查询用户作品合集列表")
    public ApiResult<?> collectionList(@Parameter(hidden = true) Search search, @Parameter(hidden = true) GalleryCollection galleryCollection) {
        return ApiResult.data(galleryCollectionService.selectUserCollectionList(search, galleryCollection));
    }

    /**
     * 获取作品合集详情
     */
    @Log(value = "作品合集", exception = "获取作品合集详情请求异常")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取作品合集详情", description = "根据id获取作品合集详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(galleryCollectionService.getById(id));
    }

    /**
     * 新增作品合集
     */
    @Log(value = "作品合集", exception = "新增作品合集请求异常")
    @PostMapping
    @Operation(summary = "新增作品合集")
    public ApiResult<?> add(@Validated @RequestBody GalleryCollectionVO collection) {
        return ApiResult.condition(galleryCollectionService.addGalleryCollect(collection));
    }

    /**
     * 修改作品合集
     */
    @Log(value = "作品合集", exception = "修改作品合集请求异常")
    @PutMapping
    @Operation(summary = "修改作品合集")
    public ApiResult<?> edit(@Validated({UpdateGroup.class}) @RequestBody GalleryCollectionVO collection) {
        return ApiResult.condition(galleryCollectionService.updateGalleryCollectById(collection));
    }

    /**
     * 删除作品合集
     */
    @Log(value = "作品合集", exception = "删除作品合集请求异常")
    @DeleteMapping("/delete/{id}")
    @Parameter(name = "id", description = "作品合集ID", in = ParameterIn.PATH)
    @Operation(summary = "删除作品合集")
    public ApiResult<?> remove(@PathVariable Long id) {
        return ApiResult.condition(galleryCollectionService.removeById(id));
    }
}
