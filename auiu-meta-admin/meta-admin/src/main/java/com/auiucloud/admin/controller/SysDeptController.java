package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysDept;
import com.auiucloud.admin.service.ISysDeptService;
import com.auiucloud.admin.service.ISysUserService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.enums.QueryModeEnum;
import com.auiucloud.core.common.utils.poi.ExcelUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门控制器
 *
 * @author dries
 */
@Tag(name = "系统部门")
@RestController
@RequestMapping("/dept")
@RequiredArgsConstructor
public class SysDeptController {

    private final ISysDeptService sysDeptService;
    private final ISysUserService sysUserService;

    /**
     * 查询系统部门列表
     */
    @Log(value = "系统部门", exception = "查询系统部门列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询系统部门列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "部门名称", in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "部门状态", in = ParameterIn.QUERY)
    })
    public ApiResult<?> list(Search search) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        switch (mode) {
            case LIST:
                return ApiResult.data(sysDeptService.selectSysDeptList(search));
            case TREE:
                return ApiResult.data(sysDeptService.treeList(search));
            case PAGE:
            default:
                PageUtils list = sysDeptService.listPage(search);
                return ApiResult.data(list);
        }
    }

    /**
     * 获取系统部门详情
     */
    @Log(value = "系统部门", exception = "获取系统部门详情请求异常")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取系统部门详情", description = "根据id获取系统部门详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(sysDeptService.getById(id));
    }

    /**
     * 校验部门名称重复
     */
    @Log(value = "系统部门", exception = "校验部门名称重复请求异常")
    @PostMapping(value = "/checkDeptNameExist")
    @Operation(summary = "校验部门名称重复")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "上级部门ID", in = ParameterIn.QUERY),
            @Parameter(name = "deptName", required = true, description = "部门名称", in = ParameterIn.QUERY),
            @Parameter(name = "id", description = "部门ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> checkDeptNameExist(@RequestBody SysDept sysDept) {
        boolean b = sysDeptService.checkDeptNameExist(sysDept);
        if (b) {
            return ApiResult.fail("部门名称已存在");
        }
        return ApiResult.success();
    }

    /**
     * 新增系统部门
     */
    @Log(value = "系统部门", exception = "新增系统部门请求异常")
    @PostMapping
    @Operation(summary = "新增系统部门")
    public ApiResult<?> add(@RequestBody SysDept sysDept) {
        boolean b = sysDeptService.checkDeptNameExist(sysDept);
        if (b) {
            return ApiResult.fail("新增部门'" + sysDept.getDeptName() + "'失败，部门名称已存在");
        }
        return ApiResult.condition(sysDeptService.insertDept(sysDept));
    }

    /**
     * 修改系统部门
     */
    @Log(value = "系统部门", exception = "修改系统部门请求异常")
    @PutMapping
    @Operation(summary = "修改系统部门")
    public ApiResult<?> edit(@RequestBody SysDept sysDept) {
        boolean b = sysDeptService.checkDeptNameExist(sysDept);
        if (b) {
            return ApiResult.fail("修改部门'" + sysDept.getDeptName() + "'失败，部门名称已存在");
        } else if (sysDept.getParentId().equals(sysDept.getId())) {
            return ApiResult.fail("修改部门'" + sysDept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (sysDept.getStatus().equals(CommonConstant.STATUS_DISABLE_VALUE) && sysDeptService.selectNormalChildrenDeptById(sysDept.getId()) > 0) {
            return ApiResult.fail("该部门包含未停用的子部门！");
        }
        return ApiResult.condition(sysDeptService.updateDept(sysDept));
    }

    /**
     * 删除系统部门
     */
    @Log(value = "系统部门", exception = "删除系统部门请求异常")
    @DeleteMapping("/delete/{deptId}")
    @Parameter(name = "deptId", description = "部门ID", in = ParameterIn.PATH)
    @Operation(summary = "删除系统部门")
    public ApiResult<?> remove(@PathVariable Long deptId) {

        if (sysDeptService.hasChildByDeptId(deptId)) {
            return ApiResult.fail("存在下级部门,不允许删除");
        }
        if (sysUserService.checkDeptExistUser(deptId)) {
            return ApiResult.fail("部门存在用户,不允许删除");
        }

        return ApiResult.condition(sysDeptService.removeDeptById(deptId));
    }

    /**
     * 导出系统部门
     */
    @Log(value = "系统部门", exception = "导出系统部门请求异常")
    @GetMapping("/export")
    @Operation(summary = "导出系统部门")
    public void export(Search search, HttpServletResponse response) {
        List<SysDept> list = sysDeptService.selectSysDeptList(search);
        String fileName = "系统部门" + System.currentTimeMillis();
        ExcelUtil.exportExcel(list, "sheet1", SysDept.class, fileName, response);
    }
}
