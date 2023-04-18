package com.auiucloud.ums.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.enums.QueryModeEnum;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.ums.domain.UserNormalLevel;
import com.auiucloud.ums.domain.UserNormalLevel;
import com.auiucloud.ums.service.IUserNormalLevelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
@Tag(name = "用户等级")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/normal-level")
public class UserNormalLevelController extends BaseController {

    private final IUserNormalLevelService userNormalLevelService;

    /**
     * 查询用户等级列表
     */
    @Log(value = "用户等级", exception = "查询用户等级列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询用户等级列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "name", description = "等级名称", in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "显示状态", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(Search search, @Parameter(hidden = true) UserNormalLevel level) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        switch (mode) {
            case LIST:
                LambdaQueryWrapper<UserNormalLevel> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.like(StrUtil.isNotBlank(level.getName()), UserNormalLevel::getName, level.getName());
                queryWrapper.eq(ObjectUtil.isNotNull(level.getStatus()), UserNormalLevel::getStatus, level.getStatus());
                return ApiResult.data(userNormalLevelService.list(queryWrapper));
            case PAGE:
            default:
                PageUtils list = userNormalLevelService.listPage(search, level);
                return ApiResult.data(list);
        }
    }

    /**
     * 获取用户等级详情
     */
    @Log(value = "用户等级", exception = "获取用户等级详情请求异常")
    @GetMapping(value = "/info/{id}")
    @Operation(summary = "获取用户等级详情", description = "根据id获取用户等级详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(userNormalLevelService.getById(id));
    }

    /**
     * 新增用户等级
     */
    @Log(value = "用户等级", exception = "新增用户等级请求异常")
    @PostMapping
    @Operation(summary = "新增用户等级")
    public ApiResult<?> add(@RequestBody UserNormalLevel level) {
        if (userNormalLevelService.checkLevelNameExist(level)) {
            throw new ApiException("【" + level.getName() + "】已存在");
        } else if(userNormalLevelService.checkLevelGradeExist(level)) {
            throw new ApiException("【等级】已存在");
        } else if (userNormalLevelService.checkLevelExperienceExist(level)) {
            throw new ApiException("【经验值要求】已存在");
        }
        return ApiResult.condition(userNormalLevelService.save(level));
    }

    /**
     * 修改用户等级
     */
    @Log(value = "用户等级", exception = "修改用户等级请求异常")
    @PutMapping
    @Operation(summary = "修改用户等级")
    public ApiResult<?> edit(@RequestBody UserNormalLevel level) {
        if (userNormalLevelService.checkLevelNameExist(level)) {
            throw new ApiException("【" + level.getName() + "】已存在");
        } else if(userNormalLevelService.checkLevelGradeExist(level)) {
            throw new ApiException("【等级】已存在");
        } else if (userNormalLevelService.checkLevelExperienceExist(level)) {
            throw new ApiException("【经验值要求】已存在");
        }
        return ApiResult.condition(userNormalLevelService.updateById(level));
    }

    /**
     * 删除用户等级
     */
    @Log(value = "用户等级", exception = "删除用户等级请求异常")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除用户等级")
    public ApiResult<?> remove(@PathVariable Long id) {
        return ApiResult.condition(userNormalLevelService.removeUserNormalLevelById(id));
    }
}
