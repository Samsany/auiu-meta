package com.auiucloud.component.feign;

import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.core.common.api.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "作品接口调用")
public class UserGalleryProvider implements IUserGalleryProvider{

    private final IGalleryService galleryService;

    /**
     * 获取用户作品数量
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @Override
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
    public ApiResult<Long> countUserReceivedLikeNum(Long userId) {
        return ApiResult.data(galleryService.countUserReceivedLikeNum(userId));
    }
}
