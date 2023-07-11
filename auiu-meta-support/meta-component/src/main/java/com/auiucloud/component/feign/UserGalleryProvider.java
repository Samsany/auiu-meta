package com.auiucloud.component.feign;

import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.vo.UserStatisticalGalleryVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "作品接口调用")
public class UserGalleryProvider implements IUserGalleryProvider {

    private final IGalleryService galleryService;

    /**
     * 获取用户作品数量
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @Override
    @GetMapping(ProviderConstant.PROVIDER_USER_GALLERY_NUM)
    public ApiResult<Long> countPublishUserGallery(Long userId) {
        return ApiResult.data(galleryService.countPublishUserGalleryByUId(userId));
    }

    /**
     * 获取用户作品数量
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @Override
    @GetMapping(ProviderConstant.PROVIDER_USER_GALLERY_LIKE_NUM)
    public ApiResult<Long> countUserReceivedLikeNum(Long userId) {
        return ApiResult.data(galleryService.countUserReceivedLikeNum(userId));
    }

    /**
     * 获取用户作品总数 & 作品列表前5个
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @Override
    public ApiResult<UserStatisticalGalleryVO> getUserStatisticalGalleryVOById(Long userId) {
        return ApiResult.data(galleryService.getUserStatisticalGalleryVOById(userId));
    }
}
