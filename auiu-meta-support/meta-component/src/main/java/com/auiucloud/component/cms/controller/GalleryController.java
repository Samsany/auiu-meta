package com.auiucloud.component.cms.controller;

import cn.hutool.core.collection.CollUtil;
import com.auiucloud.component.cms.domain.Gallery;
import com.auiucloud.component.cms.service.IGalleryCollectionService;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.vo.GalleryUploadBatchVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.exception.ApiException;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author dries
 **/
@Tag(name = "作品管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gallery")
public class GalleryController {

    private final IGalleryService galleryService;

    /**
     * 分页查询作品列表
     */
    @Log(value = "作品")
    @GetMapping("/user/page")
    @Operation(summary = "查询作品列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
    })
    public ApiResult<?> galleryUserPage(Search search, @Parameter(hidden = true) Gallery gallery) {
        return ApiResult.data(galleryService.selectUserGalleryPage(search, gallery));
    }

    @Log(value = "作品")
    @Operation(summary = "上传作品")
    @PostMapping(value = "/batch-upload")
    public ApiResult<?> uploadGalleryBatch(@Validated @RequestBody GalleryUploadBatchVO vo) {
        return ApiResult.data(galleryService.uploadGalleryBatch(vo));
    }

    /**
     * 删除作品
     */
    @Log(value = "作品")
    @DeleteMapping("/{id}")
    @Operation(summary = "批量删除作品")
    public ApiResult<?> deleteGallery(@PathVariable Long id) {
        return ApiResult.data(galleryService.removeById(id));
    }

}
