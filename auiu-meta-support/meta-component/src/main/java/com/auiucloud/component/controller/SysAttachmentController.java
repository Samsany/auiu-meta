package com.auiucloud.component.controller;

import com.auiucloud.component.service.ISysAttachmentService;
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

import java.util.Arrays;

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
    @GetMapping("/page")
    @Operation(summary = "附件分页")
    @Parameters({
            @Parameter(name = "pageNum", required = true, description = "当前页", in = ParameterIn.DEFAULT),
            @Parameter(name = "pageSize", required = true, description = "每页显示数据", in = ParameterIn.DEFAULT),
            @Parameter(name = "keyword", required = true, description = "模糊查询关键词", in = ParameterIn.DEFAULT),
            @Parameter(name = "startDate", required = true, description = "创建开始日期", in = ParameterIn.DEFAULT),
            @Parameter(name = "endDate", required = true, description = "创建结束日期", in = ParameterIn.DEFAULT),
    })
    public ApiResult<?> page(Search search) {
        return ApiResult.data(sysAttachmentService.listPage(search));
    }

    @Log(value = "附件上传")
    @Operation(summary = "附件上传")
    @PostMapping("/upload")
    public ApiResult<?> upload(@RequestParam("file") MultipartFile file) {
        return sysAttachmentService.upload(file);
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
