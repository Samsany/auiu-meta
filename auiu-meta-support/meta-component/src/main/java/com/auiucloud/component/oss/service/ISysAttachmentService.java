package com.auiucloud.component.oss.service;

import com.auiucloud.component.sysconfig.domain.SysAttachment;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
     * @param attachment 参数
     * @return PageUtils
     */
    PageUtils listPage(Search search, SysAttachment attachment);

    /**
     * 附件上传
     *
     * @param file 上传文件
     * @return 上传结果
     */
    Map<String, Object> upload(MultipartFile file);

    Map<String, Object> upload(MultipartFile file, Long groupId);

    Map<String, Object> upload(MultipartFile file, Long groupId, boolean thumb, boolean checkImg);

    /**
     * @param file     上传文件
     * @param groupId  文件分组
     * @param filename 自定义文件名
     * @param thumb    是否压缩
     * @param checkImg 是否安全检测
     * @return Map<String, Object>
     */
    Map<String, Object> upload(MultipartFile file, Long groupId, String filename, boolean thumb, boolean checkImg);

    /**
     * 附件删除
     *
     * @param id 附件ID
     * @return boolean
     */
    boolean removeSysAttachmentById(Long id);

    boolean checkAttachmentGroupHasChild(Long attachmentGroupId);

    /**
     * 将上传成功的文件记录入库
     *
     * @param file     文件
     * @param url      返回的URL
     * @param thumbUrl 缩略图URL
     * @param groupId  分类ID
     * @param filename 新文件名
     * @return boolean
     */
    boolean attachmentLog(MultipartFile file, String url, String thumbUrl, Long groupId, String filename);
}
