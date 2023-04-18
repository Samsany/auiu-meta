package com.auiucloud.component.cms.service.impl;

import com.auiucloud.component.cms.domain.Gallery;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.mapper.GalleryMapper;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.oss.service.ISysAttachmentService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.context.UserContext;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author dries
 * @description 针对表【cms_gallery(作品表)】的数据库操作Service实现
 * @createDate 2023-04-16 20:56:41
 */
@Service
@RequiredArgsConstructor
public class GalleryServiceImpl extends ServiceImpl<GalleryMapper, Gallery>
        implements IGalleryService {

    @Lazy
    @Resource
    private ISysAttachmentService sysAttachmentService;

    @Transactional
    @Override
    public ApiResult<?> upload(MultipartFile file) {
        Map<String, Object> upload = sysAttachmentService.upload(file, 1001L);
        Gallery gallery = Gallery.builder()
                .uId(SecurityUtil.getUserId())
                .pic((String) upload.getOrDefault("pic", ""))
                .width((Integer) upload.getOrDefault("width", CommonConstant.STATUS_NORMAL_VALUE))
                .height((Integer) upload.getOrDefault("height", CommonConstant.STATUS_NORMAL_VALUE))
                .size((Long) upload.getOrDefault("size", CommonConstant.ROOT_NODE_ID))
                .type(GalleryEnums.GalleryType.WALLPAPER.getValue())
                .approvalStatus(GalleryEnums.GalleryApprovalStatus.AWAIT.getValue())
                .build();
        boolean result = this.save(gallery);
        if (result) {
            return ApiResult.data(gallery);
        }
        throw new ApiException(ResultCode.USER_ERROR_A0500);
    }
}




