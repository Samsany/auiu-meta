package com.auiucloud.component.cms.controller;

import cn.hutool.core.collection.CollUtil;
import com.auiucloud.component.cms.domain.Gallery;
import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.component.cms.dto.GalleryUpdateDTO;
import com.auiucloud.component.cms.dto.JoinGalleryCollectionDTO;
import com.auiucloud.component.cms.service.IGalleryCollectionService;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.vo.GallerySubmitAppealVO;
import com.auiucloud.component.cms.vo.GalleryCollectionVO;
import com.auiucloud.component.cms.vo.GalleryPublishVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.validator.group.InsertGroup;
import com.auiucloud.core.validator.group.UpdateGroup;
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
        galleryCollection.setUserId(userId);
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
        if (!galleryCollection.getUserId().equals(userId)) {
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
    public ApiResult<?> upload(@RequestParam("file") MultipartFile file, @RequestParam(required = false) Long cateId) {
        return galleryService.upload(file, cateId);
    }

    /**
     * 分页查询作品列表
     */
    @Log(value = "作品")
    @GetMapping("/page")
    @Operation(summary = "查询作品列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
    })
    public ApiResult<?> galleryPage(Search search, @Parameter(hidden = true) Gallery gallery) {
        Long userId = SecurityUtil.getUserId();
        gallery.setUserId(userId);
        return ApiResult.data(galleryService.selectUserGalleryPage(search, gallery));
    }

    /**
     * 分页查询未加入合集的作品列表
     */
    @Log(value = "作品")
    @GetMapping("/no-collection/page")
    @Operation(summary = "查询作品列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
    })
    public ApiResult<?> galleryNoCollectionPage(Search search, @Parameter(hidden = true) Gallery gallery) {
        return ApiResult.data(galleryService.galleryNoCollectionPage(search, gallery));
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
     * 发布作品
     */
    @Log(value = "作品")
    @PutMapping("/publish")
    @Operation(summary = "发布作品")
    @Parameters({
            @Parameter(name = "appId", description = "appId", in = ParameterIn.HEADER),
    })
    public ApiResult<?> publishGallery(@Validated @RequestBody GalleryPublishVO gallery) {
        return galleryService.publishGallery(gallery);
    }

    /**
     * 编辑作品
     */
    @Log(value = "作品")
    @PutMapping("/edit")
    @Operation(summary = "编辑作品")
    @Parameters({
            @Parameter(name = "appId", description = "appId", in = ParameterIn.HEADER),
    })
    public ApiResult<?> editGallery(@Validated @RequestBody GalleryUpdateDTO gallery) {
        return galleryService.editGallery(gallery);
    }

    /**
     * 下架作品
     */
    @Log(value = "作品")
    @PutMapping("/hidden/{galleryId}")
    @Operation(summary = "下架作品")
    public ApiResult<?> hiddenGallery(@PathVariable Long galleryId) {
        return galleryService.hiddenGallery(galleryId);
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
     * 查询我的点赞列表
     */
    @Log(value = "作品")
    @GetMapping("/my-like/page")
    @Operation(summary = "查询我的点赞列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
    })
    public ApiResult<?> myLikeGalleryPage(Search search) {
        return ApiResult.data(galleryService.selectMyLikeGalleryPage(search));
    }

    /**
     * 查询我的收藏列表
     */
    @Log(value = "作品")
    @GetMapping("/my-favorite/page")
    @Operation(summary = "查询我的收藏列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
    })
    public ApiResult<?> myFavoriteGalleryPage(Search search) {
        return ApiResult.data(galleryService.selectMyFavoriteGalleryPage(search));
    }

    /**
     * 查询我的下载列表
     */
    @Log(value = "作品")
    @GetMapping("/my-download/page")
    @Operation(summary = "查询我的下载列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
    })
    public ApiResult<?> myDownloadGalleryPage(Search search) {
        return ApiResult.data(galleryService.selectMyDownloadGalleryPage(search));
    }

    /**
     * 点赞/取消点赞 作品/合集
     */
    @Log(value = "作品")
    @GetMapping("/like/{postId}")
    @Operation(summary = "点赞/取消点赞")
    @Parameters({
            @Parameter(name = "postId", description = "帖子ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "type", description = "帖子类型 0-作品 1-合集", in = ParameterIn.QUERY, required = true),
    })
    public ApiResult<?> likeGallery(@PathVariable Long postId, @RequestParam Integer type) {
        return ApiResult.condition(galleryService.likeGallery(postId, type));
    }

    /**
     * 收藏/取消收藏 作品/合集
     */
    @Log(value = "作品")
    @GetMapping("/favorite/{postId}")
    @Operation(summary = "收藏/取消收藏")
    @Parameters({
            @Parameter(name = "postId", description = "帖子ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "type", description = "帖子类型 0-作品 1-合集", in = ParameterIn.QUERY, required = true),
    })
    public ApiResult<?> favoriteGallery(@PathVariable Long postId, @RequestParam Integer type) {
        return ApiResult.condition(galleryService.favoriteGallery(postId, type));
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

    @Log(value = "用户")
    @GetMapping("/check-user-point-quantity")
    @Operation(summary = "校验用户下载作品积分")
    @Parameters({
            @Parameter(name = "id", description = "作品ID", in = ParameterIn.QUERY, required = true),
    })
    public ApiResult<?> checkUserPointQuantity(@RequestParam Long id) {
        return ApiResult.data(galleryService.checkUserPointQuantity(id));
    }

    @Log(value = "用户")
    @GetMapping("/download")
    @Operation(summary = "下载作品")
    @Parameters({
            @Parameter(name = "id", description = "作品ID", in = ParameterIn.QUERY, required = true),
    })
    public ApiResult<?> downLoadUserGallery(@RequestParam Long id) {
        return galleryService.downLoadUserGallery(id);
    }


    @Log(value = "用户")
    @PostMapping("/appeal")
    @Operation(summary = "作品申诉")
    public ApiResult<?> galleryAppeal(@RequestBody GallerySubmitAppealVO vo) {
        return galleryService.galleryAppeal(vo);
    }

}
