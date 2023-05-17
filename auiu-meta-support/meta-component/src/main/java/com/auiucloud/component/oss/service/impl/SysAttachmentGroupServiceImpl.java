package com.auiucloud.component.oss.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.oss.mapper.SysAttachmentGroupMapper;
import com.auiucloud.component.oss.service.ISysAttachmentGroupService;
import com.auiucloud.component.sysconfig.domain.SysAttachmentGroup;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.database.model.Search;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_attachment_group(附件分组表)】的数据库操作Service实现
 * @createDate 2023-03-14 12:47:40
 */
@Service
public class SysAttachmentGroupServiceImpl extends ServiceImpl<SysAttachmentGroupMapper, SysAttachmentGroup> implements ISysAttachmentGroupService {

    @Override
    public List<SysAttachmentGroup> selectAttachmentGroupList(Search search, SysAttachmentGroup attachmentGroup) {
        List<SysAttachmentGroup> attachmentGroupList = new ArrayList<>(List.of(SysAttachmentGroup.builder().id(CommonConstant.ROOT_NODE_ID).name("全部").type(attachmentGroup.getType()).build()));
        LambdaQueryWrapper<SysAttachmentGroup> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SysAttachmentGroup::getName, search.getKeyword());
        queryWrapper.eq(SysAttachmentGroup::getType, attachmentGroup.getType());
        queryWrapper.orderByDesc(SysAttachmentGroup::getSort);
        attachmentGroupList.addAll(this.list(queryWrapper));
        return attachmentGroupList;
    }

    @Override
    public SysAttachmentGroup getUploadGroupById(Long groupId) {
        SysAttachmentGroup group = this.getById(groupId);
        if (ObjectUtil.isNotNull(group)) {
            return group;
        }
        return SysAttachmentGroup.builder()
                .id(0L)
                .bizPath("")
                .name("根目录")
                .build();
    }

    @Override
    public boolean checkAttachmentGroupNameExist(SysAttachmentGroup attachmentGroup) {
        LambdaQueryWrapper<SysAttachmentGroup> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(attachmentGroup.getId()), SysAttachmentGroup::getId, attachmentGroup.getId());
        queryWrapper.and(e -> e.eq(SysAttachmentGroup::getName, attachmentGroup.getName()).eq(SysAttachmentGroup::getType, attachmentGroup.getType()));

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }
}




