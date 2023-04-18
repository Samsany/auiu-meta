package com.auiucloud.component.cms.controller;

import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.component.cms.service.IGalleryCollectionService;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.oss.service.ISysAttachmentService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.oss.core.OssTemplate;
import com.auiucloud.core.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author dries
 **/
@Tag(name = "作品API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cms/gallery")
public class GalleryApiController extends BaseController {

    private final IGalleryCollectionService galleryCollectionService;
    private final IGalleryService galleryService;

    /**
     * 查询作品合集列表
     */
    @Log(value = "作品")
    @GetMapping("/collection/list")
    @Operation(summary = "查询作品合集列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(Search search, @Parameter(hidden = true) GalleryCollection galleryCollection) {
        return ApiResult.data(galleryCollectionService.selectCollectionPage(search, galleryCollection));
    }


    /**
     * 新增作品合集
     */
    @Log(value = "作品")
    @PostMapping("/collection/add")
    @Operation(summary = "新增作品合集")
    public ApiResult<?> addCollection(@RequestBody GalleryCollection galleryCollection) {
        Long userId = SecurityUtil.getUserId();
        galleryCollection.setUId(userId);
        if (galleryCollectionService.checkGalleryCollectNameExist(galleryCollection)) {
            return ApiResult.fail("新增合集'" + galleryCollection.getTitle() + "'失败，标签已存在");
        }
        return ApiResult.condition(galleryCollectionService.save(galleryCollection));
    }

    @Log(value = "作品")
    @Operation(summary = "上传作品")
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ApiResult<?> upload(@RequestParam("file") MultipartFile file) {
        return galleryService.upload(file);
    }

}
