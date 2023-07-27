package com.auiucloud.component.cms.controller;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.cms.domain.PicTag;
import com.auiucloud.component.cms.service.IPicTagService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.core.common.enums.QueryModeEnum;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author dries
 **/
@Tag(name = "作品管理")
@RestController
@AllArgsConstructor
@RequestMapping("/pic-tag")
public class PicTagController extends BaseController {

    private final IPicTagService picTagService;

    /**
     * 查询图片标签列表
     */
    @Log(value = "图片标签", exception = "查询图片标签列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询图片标签列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(@Parameter(hidden = true) Search search, @Parameter(hidden = true) PicTag picTag) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        return switch (mode) {
            case LIST ->
                    ApiResult.data(picTagService.list(Wrappers.<PicTag>lambdaQuery()
                            .like(StrUtil.isNotBlank(picTag.getName()), PicTag::getName, picTag.getName())
                            .eq(PicTag::getStatus, picTag.getStatus())
                            .orderByDesc(PicTag::getSort)
                            .orderByDesc(PicTag::getCreateTime)
                            .orderByDesc(PicTag::getId)
                    ));
            default -> ApiResult.data(picTagService.listPage(search, picTag));
        };
    }

    /**
     * 获取图片标签详情
     */
    @Log(value = "图片标签", exception = "获取图片标签详情请求异常")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取图片标签详情", description = "根据id获取图片标签详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(picTagService.getById(id));
    }

    /**
     * 新增图片标签
     */
    @Log(value = "图片标签", exception = "新增图片标签请求异常")
    @PostMapping
    @Operation(summary = "新增图片标签")
    public ApiResult<?> add(@RequestBody PicTag picTag) {
        if (picTagService.checkPicTagNameExist(picTag)) {
            return ApiResult.fail("新增标签'" + picTag.getName() + "'失败，标签已存在");
        }
        return ApiResult.condition(picTagService.save(picTag));
    }

    /**
     * 修改图片标签
     */
    @Log(value = "图片标签", exception = "修改图片标签请求异常")
    @PutMapping
    @Operation(summary = "修改图片标签")
    public ApiResult<?> edit(@RequestBody PicTag picTag) {
        if (picTagService.checkPicTagNameExist(picTag)) {
            return ApiResult.fail("修改'" + picTag.getName() + "'失败，标签已存在");
        }
        return ApiResult.condition(picTagService.updateById(picTag));
    }

    /**
     * 设置图片标签状态
     */
    @Log(value = "图片标签", exception = "修改图片标签请求异常")
    @PutMapping("/setStatus")
    @Operation(summary = "修改图片标签状态")
    public ApiResult<?> setStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(picTagService.setStatus(updateStatusDTO));
    }

    /**
     * 删除图片标签
     */
    @Log(value = "图片标签", exception = "删除图片标签请求异常")
    @DeleteMapping("/delete/{picTagId}")
    @Parameter(name = "picTagId", description = "图片标签ID", in = ParameterIn.PATH)
    @Operation(summary = "删除图片标签")
    public ApiResult<?> remove(@PathVariable Long picTagId) {
        if (picTagService.checkPicTagHasChild(picTagId)) {
            PicTag picTag = picTagService.getById(picTagId);
            return ApiResult.fail("删除图片标签'" + picTag.getName() + "'失败，该标签已关联图片");
        }
        return ApiResult.condition(picTagService.removeById(picTagId));
    }
}
