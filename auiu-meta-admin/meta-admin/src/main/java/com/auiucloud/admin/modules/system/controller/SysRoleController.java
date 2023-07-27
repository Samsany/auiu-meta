package com.auiucloud.admin.modules.system.controller;

import com.auiucloud.admin.modules.system.domain.SysRole;
import com.auiucloud.admin.modules.system.service.ISysRoleService;
import com.auiucloud.admin.modules.system.vo.SysRoleVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.core.common.enums.QueryModeEnum;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.poi.ExcelUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.log.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 系统角色控制器
 *
 * @author Dries
 * @date 2022-09-05 18:30:14
 */
@Tag(name = "系统角色")
@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class SysRoleController extends BaseController {

    private final ISysRoleService sysRoleService;

    /**
     * 查询系统全部角色列表
     */
    @Log(value = "系统角色", exception = "查询系统全部角色列表请求异常")
    @GetMapping("/allList")
    @Operation(summary = "查询系统全部角色列表")
    public ApiResult<?> allList() {
        return ApiResult.data(sysRoleService.list());
    }

    /**
     * 查询系统角色列表
     */
    @Log(value = "系统角色", exception = "查询系统角色列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询系统角色列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "模糊查询关键词", in = ParameterIn.QUERY),
            @Parameter(name = "startDate", description = "开始日期", in = ParameterIn.QUERY),
            @Parameter(name = "endDate", description = "结束日期", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(Search search, SysRole sysRole) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        switch (mode) {
            case LIST:
                LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
                return ApiResult.data(sysRoleService.list(queryWrapper));
            case PAGE:
            default:
                PageUtils list = sysRoleService.listPage(search, sysRole);
                return ApiResult.data(list);
        }

    }

    /**
     * 获取系统角色详情
     */
    @Log(value = "系统角色", exception = "获取系统角色详情请求异常")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取系统角色详情", description = "根据id获取系统角色详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(sysRoleService.getRoleInfoById(id));
    }

    /**
     * 新增系统角色
     */
    @Log(value = "系统角色", exception = "新增系统角色请求异常")
    @PostMapping
    @Operation(summary = "新增系统角色")
    public ApiResult<?> add(@RequestBody SysRoleVO sysRole) {
        if (sysRoleService.checkRoleNameUnique(sysRole)) {
            return ApiResult.fail("新增角色'" + sysRole.getRoleName() + "'失败，角色名称已存在");
        }
        if (sysRoleService.checkRoleCodeUnique(sysRole)) {
            return ApiResult.fail("新增角色'" + sysRole.getRoleCode() + "'失败，角色编码已存在");
        }
        return ApiResult.condition(sysRoleService.saveRole(sysRole));
    }

    /**
     * 修改系统角色
     */
    @Log(value = "系统角色", exception = "修改系统角色请求异常")
    @PutMapping
    @Operation(summary = "修改系统角色")
    public ApiResult<?> edit(@RequestBody SysRoleVO sysRole) {
        if (sysRoleService.checkRoleNameUnique(sysRole)) {
            return ApiResult.fail("修改角色'" + sysRole.getRoleName() + "'失败，角色名称已存在");
        }
        if (sysRoleService.checkRoleCodeUnique(sysRole)) {
            return ApiResult.fail("修改角色'" + sysRole.getRoleCode() + "'失败，角色编码已存在");
        }
        return ApiResult.condition(sysRoleService.updateRoleById(sysRole));
    }

    /**
     * 设置系统角色状态
     */
    @Log(value = "系统角色", exception = "修改系统角色请求异常")
    @PutMapping("/setStatus")
    @Operation(summary = "修改系统角色状态")
    public ApiResult<?> setRoleStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(sysRoleService.setRoleStatus(updateStatusDTO));
    }

    /**
     * 删除系统角色
     */
    @Log(value = "系统角色", exception = "删除系统角色请求异常")
    @DeleteMapping
    @Operation(summary = "删除系统角色")
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        return ApiResult.condition(sysRoleService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出系统角色
     */
    @Log(value = "系统角色", exception = "导出系统角色请求异常")
    @GetMapping("/export")
    @Operation(summary = "导出系统角色")
    public void export(Search search, SysRole sysRole, HttpServletResponse response) {
        List<SysRole> list = sysRoleService.selectSysRoleList(search, sysRole);
        String fileName = "系统角色" + System.currentTimeMillis();
        ExcelUtil.exportExcel(list, "sheet1", SysRole.class, fileName, response);
    }
}
