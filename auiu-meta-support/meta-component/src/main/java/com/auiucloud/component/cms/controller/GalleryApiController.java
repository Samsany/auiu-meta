package com.auiucloud.component.cms.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import com.auiucloud.component.cms.domain.Gallery;
import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.component.cms.dto.JoinGalleryCollectionDTO;
import com.auiucloud.component.cms.service.IGalleryCollectionService;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.vo.GalleryCollectionVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.context.UserContext;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.validator.InsertGroup;
import com.auiucloud.core.validator.UpdateGroup;
import com.auiucloud.core.web.controller.BaseController;
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
    public ApiResult<?> collectionList(@Parameter(hidden = true) Search search, @Parameter(hidden = true) GalleryCollection galleryCollection) {
        return ApiResult.data(galleryCollectionService.selectUserCollectionApiList(search, galleryCollection));
    }

    /**
     * 分页查询作品合集列表
     */
    @Log(value = "作品")
    @GetMapping("/collection/page")
    @Operation(summary = "分页查询用户作品合集列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
    })
    public ApiResult<?> collectionPage(@Parameter(hidden = true) Search search, @Parameter(hidden = true) GalleryCollection galleryCollection) {
        return ApiResult.data(galleryCollectionService.selectUserCollectionApiPage(search, galleryCollection));
    }

    /**
     * 查询作品合集
     */
    @Log(value = "作品")
    @GetMapping("/collection/info/{collectId}")
    @Operation(summary = "查询作品合集")
    public ApiResult<?> getCollection(@PathVariable Long collectId) {
        return ApiResult.data(galleryCollectionService.getGalleryCollect(collectId));
    }

    /**
     * 新增作品合集
     */
    @Log(value = "作品")
    @PostMapping("/collection/add")
    @Operation(summary = "新增作品合集")
    public ApiResult<?> addCollection(@Validated({InsertGroup.class}) @RequestBody GalleryCollectionVO galleryCollection) {
        Long userId = SecurityUtil.getUserId();
        galleryCollection.setUId(userId);
        return ApiResult.condition(galleryCollectionService.addGalleryCollect(galleryCollection));
    }

    /**
     * 编辑作品合集
     */
    @Log(value = "作品")
    @PutMapping("/collection/edit")
    @Operation(summary = "编辑作品合集")
    public ApiResult<?> editCollection(@Validated({UpdateGroup.class}) @RequestBody GalleryCollectionVO galleryCollection) {
        Long userId = SecurityUtil.getUserId();
        if (!galleryCollection.getUId().equals(userId)) {
            return ApiResult.fail(ResultCode.USER_ERROR_A0300);
        }
        return ApiResult.condition(galleryCollectionService.updateGalleryCollectById(galleryCollection));
    }

    /**
     * 设置作品合集置顶
     */
    @Log(value = "作品")
    @PutMapping("/collection/set-top")
    @Operation(summary = "设置作品合集置顶状态")
    public ApiResult<?> setCollectionTopStatus(@Validated @RequestBody UpdateStatusDTO statusDTO) {
        String message = statusDTO.getStatus() == 0 ? "取消置顶" : "已置顶";
        return ApiResult.condition(message, galleryCollectionService.setCollectionTopStatus(statusDTO));
    }

    /**
     * 作品加入合集
     */
    @Log(value = "作品")
    @PutMapping("/collection/join")
    @Operation(summary = "加入作品合集")
    public ApiResult<?> joinCollection(@Validated @RequestBody JoinGalleryCollectionDTO joinGalleryCollectionDTO) {
        return ApiResult.condition(galleryService.joinGalleryCollection(joinGalleryCollectionDTO));
    }

    /**
     * 删除作品
     */
    @Log(value = "作品")
    @DeleteMapping("/collection/delete")
    @Operation(summary = "批量删除作品合集")
    public ApiResult<?> deleteGalleryCollection(@RequestBody List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            throw new ApiException("请选择要删除的作品合集");
        }
        return ApiResult.data(galleryCollectionService.removeGalleryCollectionByIds(ids));
    }

    @Log(value = "作品")
    @Operation(summary = "上传作品")
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ApiResult<?> upload(@RequestParam("file") MultipartFile file, @RequestParam(required = false)  Long cId) {
        return galleryService.upload(file, cId);
    }

    /**
     * 查询作品列表
     */
    @Log(value = "作品")
    @GetMapping("/list")
    @Operation(summary = "查询作品列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
    })
    public ApiResult<?> galleryPage(Search search, @Parameter(hidden = true) Gallery gallery) {
        return ApiResult.data(galleryService.selectGalleryPage(search, gallery));
    }

    /**
     * 查询作品详情
     */
    @Log(value = "作品")
    @GetMapping("/info/{galleryId}")
    @Operation(summary = "查询作品详情")
    @Parameters({
            @Parameter(name = "galleryId", description = "ID标识", in = ParameterIn.PATH),
    })
    public ApiResult<?> galleryInfo(@PathVariable Long galleryId) {
        return ApiResult.data(galleryService.selectGalleryInfoById(galleryId));
    }

    /**
     * 设置作品置顶
     */
    @Log(value = "作品")
    @PutMapping("/set-top")
    @Operation(summary = "设置作品合集置顶状态")
    public ApiResult<?> setGalleryTopStatus(@Validated @RequestBody UpdateStatusDTO statusDTO) {
        String message = statusDTO.getStatus() == 0 ? "取消置顶" : "已置顶";
        return ApiResult.condition(message, galleryService.setGalleryTopStatus(statusDTO));
    }

    /**
     * 点赞/取消点赞 作品/合集
     */
    @Log(value = "作品")
    @GetMapping("/like/{postId}")
    @Operation(summary = "点赞/取消点赞")
    @Parameters({
            @Parameter(name = "postId", description = "帖子ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "type", description = "帖子类型", in = ParameterIn.QUERY, required = true),
    })
    public ApiResult<?> likeGallery(@PathVariable Long postId, @RequestParam Integer type) {
        return ApiResult.condition(galleryService.likeGallery(postId, type));
    }

    /**
     * 删除作品
     */
    @Log(value = "作品")
    @DeleteMapping("/delete")
    @Operation(summary = "批量删除作品")
    public ApiResult<?> deleteGallery(@RequestBody List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            throw new ApiException("请选择要删除的作品");
        }
        return ApiResult.data(galleryService.removeGalleryByIds(ids));
    }

}
