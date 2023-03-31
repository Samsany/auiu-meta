package com.auiucloud.component.oss.service;

import com.auiucloud.component.oss.domain.SysAttachment;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author dries
* @description 针对表【sys_attachment(附件表)】的数据库操作Service
* @createDate 2023-03-14 12:47:40
*/
public interface ISysAttachmentService extends IService<SysAttachment> {

    /**
     * 分页查询附件
     *
     * @param search     参数
     * @param attachment
     * @return PageUtils
     */
    PageUtils listPage(Search search, SysAttachment attachment);

    /**
     * 附件上传
     *
     * @param file      上传文件
     * @return 上传结果
     */
    ApiResult<?> upload(MultipartFile file);
    ApiResult<?> upload(MultipartFile file, Long groupId, String bizPath, String filename);

    /**
     * 附件删除
     *
     * @param id 附件ID
     * @return boolean
     */
    boolean removeSysAttachmentById(Long id);

    boolean checkAttachmentGroupHasChild(Long attachmentGroupId);
}
