package com.auiucloud.component.oss.controller;

import com.auiucloud.component.oss.domain.SysAttachmentGroup;
import com.auiucloud.component.oss.service.ISysAttachmentGroupService;
import com.auiucloud.component.oss.service.ISysAttachmentService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author dries
 **/
@Tag(name = "附件管理分组")
@RestController
@AllArgsConstructor
@RequestMapping("/attachment-group")
public class SysAttachmentGroupController {

    private final ISysAttachmentGroupService attachmentGroupService;
    private final ISysAttachmentService attachmentService;

    /**
     * 查询附件分组列表
     */
    @Log(value = "附件分组", exception = "查询附件分组列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询附件分组列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
            @Parameter(name = "type", description = "附件类型", in = ParameterIn.QUERY, required = true)
    })
    public ApiResult<?> list(Search search, @Parameter(hidden = true) SysAttachmentGroup attachmentGroup) {
        return ApiResult.data(attachmentGroupService.selectAttachmentGroupList(search, attachmentGroup));
    }

    /**
     * 获取附件分组详情
     */
    @Log(value = "附件分组", exception = "获取附件分组详情请求异常")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取附件分组详情", description = "根据id获取附件分组详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(attachmentGroupService.getById(id));
    }

    /**
     * 新增附件分组
     */
    @Log(value = "附件分组", exception = "新增附件分组请求异常")
    @PostMapping
    @Operation(summary = "新增附件分组")
    public ApiResult<?> add(@RequestBody SysAttachmentGroup attachmentGroup) {
        if (attachmentGroupService.checkAttachmentGroupNameExist(attachmentGroup)) {
            return ApiResult.fail("新增附件分组'" + attachmentGroup.getName() + "'失败，附件分组名称已存在");
        }
        return ApiResult.condition(attachmentGroupService.save(attachmentGroup));
    }

    /**
     * 修改附件分组
     */
    @Log(value = "附件分组", exception = "修改附件分组请求异常")
    @PutMapping
    @Operation(summary = "修改附件分组")
    public ApiResult<?> edit(@RequestBody SysAttachmentGroup attachmentGroup) {
        if (attachmentGroupService.checkAttachmentGroupNameExist(attachmentGroup)) {
            return ApiResult.fail("修改附件分组'" + attachmentGroup.getName() + "'失败，附件分组名称已存在");
        }
        return ApiResult.condition(attachmentGroupService.updateById(attachmentGroup));
    }

    /**
     * 删除附件分组
     */
    @Log(value = "附件分组", exception = "删除附件分组请求异常")
    @DeleteMapping("/delete/{attachmentGroupId}")
    @Parameter(name = "attachmentGroupId", description = "附件分组ID", in = ParameterIn.PATH)
    @Operation(summary = "删除附件分组")
    public ApiResult<?> remove(@PathVariable Long attachmentGroupId) {
        if (attachmentService.checkAttachmentGroupHasChild(attachmentGroupId)) {
            SysAttachmentGroup attachmentGroup = attachmentGroupService.getById(attachmentGroupId);
            return ApiResult.fail("删除附件分组'" + attachmentGroup.getName() + "'失败，该分组内存在文件");
        }
        return ApiResult.condition(attachmentGroupService.removeById(attachmentGroupId));
    }

}
