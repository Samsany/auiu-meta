package com.auiucloud.component.oss.controller;

import com.auiucloud.component.oss.domain.SysAttachment;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * @author dries
 **/
@Tag(name = "附件管理")
@RestController
@AllArgsConstructor
@RequestMapping("/attachment")
public class SysAttachmentController {

    private final ISysAttachmentService sysAttachmentService;

    @Log(value = "附件分页")
    @GetMapping("/list")
    @Operation(summary = "附件分页")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.DEFAULT),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.DEFAULT),
            @Parameter(name = "keyword", description = "模糊查询关键词", in = ParameterIn.DEFAULT),
            @Parameter(name = "fileType", required = true, description = "文件类型", in = ParameterIn.DEFAULT),
            @Parameter(name = "attachmentGroupId", required = true, description = "附件分组", in = ParameterIn.DEFAULT)
    })
    public ApiResult<?> list(Search search,  @Parameter(hidden = true) SysAttachment attachment) {
        return ApiResult.data(sysAttachmentService.listPage(search, attachment));
    }

    @Log(value = "附件上传")
    @Operation(summary = "附件上传")
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ApiResult<?> upload(@RequestParam("file") MultipartFile file,
                               @RequestParam(required = false, defaultValue = "0") Long groupId,
                               @RequestParam(required = false, defaultValue = "") String bizPath,
                               @RequestParam(required = false) String filename

    ) {
        return sysAttachmentService.upload(file, groupId, bizPath, filename);
    }

    @Log(value = "删除文件")
    @Parameters({
            @Parameter(name = "ids", required = true, example = "[1,2,3]", in = ParameterIn.DEFAULT)
    })
    @DeleteMapping
    @Operation(summary = "删除文件")
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        for (Long id : ids) {
            sysAttachmentService.removeSysAttachmentById(id);
        }
        return ApiResult.success("删除成功");
    }

}
