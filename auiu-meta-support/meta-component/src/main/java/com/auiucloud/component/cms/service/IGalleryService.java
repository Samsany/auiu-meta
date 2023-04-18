package com.auiucloud.component.cms.service;

import com.auiucloud.component.cms.domain.Gallery;
import com.auiucloud.core.common.api.ApiResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author dries
* @description 针对表【cms_gallery(作品表)】的数据库操作Service
* @createDate 2023-04-16 20:56:41
*/
public interface IGalleryService extends IService<Gallery> {

    ApiResult<?> upload(MultipartFile file);
}
