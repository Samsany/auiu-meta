package com.auiucloud.component.oss.service;

import com.auiucloud.component.sysconfig.domain.SysAttachmentGroup;
import com.auiucloud.core.database.model.Search;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_attachment_group(附件分组表)】的数据库操作Service
 * @createDate 2023-03-14 12:47:40
 */
public interface ISysAttachmentGroupService extends IService<SysAttachmentGroup> {

    List<SysAttachmentGroup> selectAttachmentGroupList(Search search, SysAttachmentGroup attachmentGroup);

    boolean checkAttachmentGroupNameExist(SysAttachmentGroup attachmentGroup);

    SysAttachmentGroup getUploadGroupById(Long groupId);
}
