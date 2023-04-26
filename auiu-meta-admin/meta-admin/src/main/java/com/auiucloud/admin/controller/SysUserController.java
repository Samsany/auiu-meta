package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysUser;
import com.auiucloud.admin.dto.SysUserDTO;
import com.auiucloud.core.common.model.dto.UpdatePasswordDTO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.admin.service.ISysUserService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.controller.BaseController;
import com.auiucloud.core.common.utils.poi.ExcelUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.validator.InsertGroup;
import com.auiucloud.core.validator.UpdateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 系统用户控制器
 *
 * @author Dries
 * @date 2022-10-18 15:08:14
 */
@Tag(name = "系统用户")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class SysUserController extends BaseController {

    private final ISysUserService sysUserService;

    /**
     * 查询系统用户列表
     */
    @Log(value = "系统用户", exception = "查询系统用户列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询系统用户列表")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "account",  description = "账户", in = ParameterIn.QUERY),
            @Parameter(name = "nickname",  description = "昵称", in = ParameterIn.QUERY),
            @Parameter(name = "deptId",  description = "部门", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(Search search, SysUser sysUser) {
        PageUtils list = sysUserService.listPage(search, sysUser);
        return ApiResult.data(list);
    }

    /**
     * 获取系统用户详情
     */
    @Log(value = "系统用户", exception = "获取系统用户详情请求异常")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取系统用户详情", description = "根据id获取系统用户详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(sysUserService.getSysUserInfoById(id));
    }

    /**
     * 新增系统用户
     */
    @Log(value = "系统用户", exception = "新增系统用户请求异常")
    @PostMapping
    @Operation(summary = "新增系统用户")
    public ApiResult<?> add(@Validated({InsertGroup.class})  @RequestBody SysUserDTO sysUser) {
        return ApiResult.condition(sysUserService.saveSysUserVO(sysUser));
    }

    /**
     * 修改系统用户
     */
    @Log(value = "系统用户", exception = "修改系统用户请求异常")
    @PutMapping
    @Operation(summary = "修改系统用户")
    public ApiResult<?> edit(@Validated({UpdateGroup.class}) @RequestBody SysUserDTO sysUser) {
        return ApiResult.condition(sysUserService.updateSysUserVOById(sysUser));
    }

    /**
     * 校验部门名称重复
     */
    @Log(value = "系统用户", exception = "校验用户账户重复请求异常")
    @PostMapping(value = "/checkUsernameExist")
    @Operation(summary = "校验用户账户重复")
    @Parameters({
            @Parameter(name = "account", required = true, description = "用户账户", in = ParameterIn.QUERY),
            @Parameter(name = "id", description = "用户ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> checkUsernameExist(@RequestBody SysUser sysUser) {
        boolean b = sysUserService.checkUsernameExist(sysUser);
        if (b) {
            return ApiResult.fail("用户账户已存在");
        }
        return ApiResult.success();
    }


    /**
     * 修改系统用户状态
     */
    @Log(value = "系统用户", exception = "修改系统用户请求异常")
    @PutMapping("/setStatus")
    @Operation(summary = "修改系统用户状态")
    public ApiResult<?> setUserStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(sysUserService.setUserStatus(updateStatusDTO));
    }

    /**
     * 修改系统用户密码
     */
    @Log(value = "系统用户", exception = "修改系统用户请求异常")
    @PutMapping("/setNewPassword")
    @Operation(summary = "修改系统用户密码")
    public ApiResult<?> setNewPassword(@Validated({UpdatePasswordDTO.SetPasswordGroup.class}) @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return ApiResult.condition(sysUserService.setNewPassword(updatePasswordDTO));
    }

    /**
     * 删除系统用户
     */
    @Log(value = "系统用户", exception = "删除系统用户请求异常")
    @DeleteMapping
    @Operation(summary = "删除系统用户")
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        String msg = sysUserService.deleteSysUserByIds(Arrays.asList(ids));
        return ApiResult.success(msg);
    }

    /**
     * 导出系统用户
     */
    @Log(value = "系统用户", exception = "导出系统用户请求异常")
    @GetMapping("/export")
    @Operation(summary = "导出系统用户")
    public void export(Search search, SysUser SysUser, HttpServletResponse response) {
        List<SysUser> list = sysUserService.selectSysUserList(search, SysUser);
        String fileName = "系统用户" + System.currentTimeMillis();
        ExcelUtil.exportExcel(list, "sheet1", SysUser.class, fileName, response);
    }

}
