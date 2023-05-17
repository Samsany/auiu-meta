package com.auiucloud.component.cms.controller;

/**
 * @author dries
 **/

import com.auiucloud.component.cms.domain.Gallery;
import com.auiucloud.component.cms.domain.GalleryCollection;
import com.auiucloud.component.cms.domain.SwiperAdv;
import com.auiucloud.component.cms.service.IGalleryCollectionService;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.service.IPicTagService;
import com.auiucloud.component.cms.service.ISwiperAdvService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 **/
@Tag(name = "内容管理开放API")
@RestController
@AllArgsConstructor
@RequestMapping("/open-api/cms")
public class CmsOpenApiController extends BaseController {

    private final IPicTagService picTagService;
    private final ISwiperAdvService swiperAdvService;
    private final IGalleryCollectionService galleryCollectionService;
    private final IGalleryService galleryService;

    //    private final StreamBridge streamBridge;

    //    @Log(value = "发送信息")
    //    @GetMapping("/send/message")
    //    @Operation(summary = "发送信息")
    //    public ApiResult<?> sendTestMessage(String message) {
    //        streamBridge.send(MessageConstant.SMS_MESSAGE_OUTPUT, message);
    //        return ApiResult.success();
    //    }

    /**
     * 查询轮播广告列表
     */
    @Log(value = "轮播广告")
    @GetMapping("/swiper-adv/list")
    @Operation(summary = "查询轮播广告列表")
    @Parameters({
            @Parameter(name = "type", description = "展示位置, 默认为0-首页轮播Banner", in = ParameterIn.QUERY, required = true, example = "0"),
    })
    public ApiResult<?> swiperAdvList(@Parameter(hidden = true) SwiperAdv swiperAdv) {
        return ApiResult.data(swiperAdvService.selectCommonSwiperAdvList(swiperAdv));
    }

    /**
     * 查询图片标签列表
     */
    @Log(value = "图片标签")
    @GetMapping("/pic-tag/recommend/list")
    @Operation(summary = "查询图片标签推荐列表")
    public ApiResult<?> picTagRecommendList() {
        return ApiResult.data(picTagService.selectRecommendPicTagList());
    }

    /**
     * 查询图片标签列表
     */
    @Log(value = "图片标签")
    @GetMapping("/pic-tag/common/list")
    @Operation(summary = "查询图片标签列表")
    @Parameters({
            @Parameter(name = "type", description = "0-展示所有，1-组装全部、合集", in = ParameterIn.QUERY, required = true, example = "0"),
    })
    public ApiResult<?> picTagCommonList(@Parameter(hidden = true) Integer type) {
        return ApiResult.data(picTagService.selectCommonPicTagList(type));
    }

    /**
     * 查询作品推荐列表
     */
    @Log(value = "作品")
    @GetMapping("/gallery/recommend/list")
    @Operation(summary = "查询作品推荐列表")
    public ApiResult<?> galleryReCommendList() {
        return ApiResult.data(galleryService.selectGalleryReCommendList());
    }

    /**
     * 分页查询作品列表
     */
    @Log(value = "作品")
    @GetMapping("/gallery/page")
    @Operation(summary = "查询作品列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "tagId", description = "作品标签", in = ParameterIn.QUERY),
    })
    public ApiResult<?> galleryCommonPage(Search search, @Parameter(hidden = true) Gallery gallery) {
        return ApiResult.data(galleryService.selectSquareGalleryVOPage(search, gallery));
    }

    /**
     * 分页查询作品合集列表
     */
    @Log(value = "作品")
    @GetMapping("/gallery-collection/user/home/page")
    @Operation(summary = "分页查询用户作品合集列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "tagId", description = "作品标签", in = ParameterIn.QUERY),
            @Parameter(name = "uId", description = "创作者ID", in = ParameterIn.QUERY, required = true),
    })
    public ApiResult<?> collectionPage(@Parameter(hidden = true) Search search, @Parameter(hidden = true) GalleryCollection galleryCollection) {
        return ApiResult.data(galleryCollectionService.selectGalleryCollectionUserHomePage(search, galleryCollection));
    }

    /**
     * 查询作品合集详情
     */
    @Log(value = "作品")
    @GetMapping("/gallery-collection/info/{cId}")
    @Operation(summary = "查询作品合集详情")
    public ApiResult<?> getGalleryCollection(@PathVariable Long cId) {
        return ApiResult.data(galleryCollectionService.selectGalleryCollectionById(cId));
    }


    /**
     * 分页查询作品列表
     */
    @Log(value = "作品")
    @GetMapping("/gallery/user/home/page")
    @Operation(summary = "查询作品列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "tagId", description = "作品标签", in = ParameterIn.QUERY),
            @Parameter(name = "uId", description = "创作者ID", in = ParameterIn.QUERY, required = true),
    })
    public ApiResult<?> galleryUserHomePage(Search search, @Parameter(hidden = true) Gallery gallery) {
        return ApiResult.data(galleryService.selectGalleryUserHomePage(search, gallery));
    }

    /**
     * 查询作品详情
     */
    @Log(value = "作品")
    @GetMapping("/gallery/info/{galleryId}")
    @Operation(summary = "查询作品详情")
    @Parameters({
            @Parameter(name = "galleryId", description = "ID标识", in = ParameterIn.PATH),
    })
    public ApiResult<?> galleryCommonInfo(@PathVariable Long galleryId) {
        return ApiResult.data(galleryService.selectGalleryInfoById(galleryId));
    }

}
