package com.auiucloud.ums.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.ums.domain.UserGroup;
import com.auiucloud.ums.service.IUserGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author dries
 **/
@Tag(name = "用户分组")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/group")
public class UserGroupController extends BaseController {

    private final IUserGroupService userGroupService;

    /**
     * 查询用户分组列表
     */
    @Log(value = "用户分组", exception = "查询用户分组列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询用户分组列表")
    @Parameters({
            @Parameter(name = "pageNum", required = true, description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", required = true, description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "groupName", description = "分组名称", in = ParameterIn.QUERY)
    })
    public ApiResult<?> list(Search search, @Parameter(hidden = true) UserGroup userGroup) {
        PageUtils list = userGroupService.listPage(search, userGroup);
        return ApiResult.data(list);
    }

    /**
     * 获取用户分组详情
     */
    @Log(value = "用户分组", exception = "获取用户分组详情请求异常")
    @GetMapping(value = "/info/{id}")
    @Operation(summary = "获取用户分组详情", description = "根据id获取用户分组详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(userGroupService.getById(id));
    }

    /**
     * 新增用户分组
     */
    @Log(value = "用户分组", exception = "新增用户分组请求异常")
    @PostMapping
    @Operation(summary = "新增用户分组")
    public ApiResult<?> add(@RequestBody UserGroup userGroup) {
        return ApiResult.condition(userGroupService.saveUserGroup(userGroup));
    }

    /**
     * 修改用户分组
     */
    @Log(value = "用户分组", exception = "修改用户分组请求异常")
    @PutMapping
    @Operation(summary = "修改用户分组")
    public ApiResult<?> edit(@RequestBody UserGroup userGroup) {
        return ApiResult.condition(userGroupService.updateUserGroupById(userGroup));
    }

    /**
     * 删除用户分组
     */
    @Log(value = "用户分组", exception = "删除用户分组请求异常")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除用户分组")
    public ApiResult<?> remove(@PathVariable Long id) {
        return ApiResult.condition(userGroupService.removeUserGroupById(id));
    }

}
