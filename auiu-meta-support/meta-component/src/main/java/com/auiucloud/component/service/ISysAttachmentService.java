package com.auiucloud.component.service;

import com.auiucloud.component.domain.SysAttachment;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import kotlin.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author dries
* @description 针对表【sys_attachment(附件表)】的数据库操作Service
* @createDate 2023-03-14 12:47:40
*/
public interface ISysAttachmentService extends IService<SysAttachment> {

    /**
     * 分页查询附件
     *
     * @param search 参数
     * @return PageUtils
     */
    PageUtils listPage(Search search);

    /**
     * 附件上传
     *
     * @param file 上传文件
     * @return 上传结果
     */
    ApiResult<?> upload(MultipartFile file);

    /**
     * 附件删除
     *
     * @param id 附件ID
     * @return boolean
     */
    boolean removeSysAttachmentById(Long id);
}
