package com.auiucloud.component.feign;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.feign.constant.FeignConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 作品远程调用接口
 *
 * @author dries
 **/
@FeignClient(value = FeignConstant.META_CLOUD_COMPONENT)
public interface IUserGalleryProvider {

    /**
     * 获取用户已发布作品数量
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_GALLERY_NUM + "/{userId}")
    ApiResult<Long> countPublishUserGallery(@PathVariable Long userId);

    /**
     * 获取用户作品数量
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_GALLERY_LIKE_NUM + "/{userId}")
    ApiResult<Long> countUserReceivedLikeNum(@PathVariable Long userId);
}
