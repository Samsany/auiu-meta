package com.auiucloud.ums.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.ums.domain.UserTag;
import com.auiucloud.ums.domain.UserTag;
import com.auiucloud.ums.service.IUserTagService;
import com.auiucloud.ums.service.IUserTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author dries
 **/
@Tag(name = "标签管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/tag")
public class UserTagController extends BaseController {

    private final IUserTagService userTagService;

    /**
     * 查询用户标签列表
     */
    @Log(value = "用户标签", exception = "查询用户标签列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询用户标签列表")
    @Parameters({
            @Parameter(name = "pageNum", required = true, description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", required = true, description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "groupId", required = true, description = "标签分组ID", in = ParameterIn.QUERY),
            @Parameter(name = "name", description = "标签名称", in = ParameterIn.QUERY)
    })
    public ApiResult<?> list(Search search, @Parameter(hidden = true)  UserTag userTag) {
        PageUtils list = userTagService.listPage(search, userTag);
        return ApiResult.data(list);
    }

    /**
     * 获取用户标签详情
     */
    @Log(value = "用户标签", exception = "获取用户标签详情请求异常")
    @GetMapping(value = "/info/{id}")
    @Operation(summary = "获取用户标签详情", description = "根据id获取用户标签详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(userTagService.getById(id));
    }

    /**
     * 新增用户标签
     */
    @Log(value = "用户标签", exception = "新增用户标签请求异常")
    @PostMapping
    @Operation(summary = "新增用户标签")
    public ApiResult<?> add(@RequestBody UserTag userTag) {
        return ApiResult.condition(userTagService.save(userTag));
    }

    /**
     * 修改用户标签
     */
    @Log(value = "用户标签", exception = "修改用户标签请求异常")
    @PutMapping
    @Operation(summary = "修改用户标签")
    public ApiResult<?> edit(@RequestBody UserTag userTag) {
        return ApiResult.condition(userTagService.updateById(userTag));
    }

    /**
     * 删除用户标签
     */
    @Log(value = "用户标签", exception = "删除用户标签请求异常")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除用户标签")
    public ApiResult<?> remove(@PathVariable Long id) {
        return ApiResult.condition(userTagService.removeUserTagById(id));
    }

}
