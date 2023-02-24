package com.auiucloud.admin.controller;

import com.auiucloud.admin.domain.SysOauthClient;
import com.auiucloud.admin.dto.UpdateStatusDTO;
import com.auiucloud.admin.service.ISysOauthClientService;
import com.auiucloud.admin.dto.SysOauthClientDTO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.enums.QueryModeEnum;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * 系统用户控制器
 *
 * @author Dries
 * @date 2023-02-17 15:08:14
 */
@Tag(name = "系统用户")
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth-client")
public class SysOauthClientController {

    private final ISysOauthClientService sysOauthClientService;

    /**
     * 查询客户端列表
     */
    @Log(value = "客户端", exception = "查询客户端列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询客户端列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "clientName",  description = "客户端名称", in = ParameterIn.QUERY)
    })
    public ApiResult<?> list(Search search, SysOauthClient oauthClient) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        switch (mode) {
            case LIST:
                return ApiResult.data(sysOauthClientService.selectOauthClientList(oauthClient));
            case PAGE:
            default:
                PageUtils list = sysOauthClientService.listPage(search, oauthClient);
                return ApiResult.data(list);
        }
    }

    /**
     * 获取客户端详情
     */
    @Log(value = "客户端", exception = "获取客户端详情请求异常")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取客户端详情", description = "根据id获取客户端详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(sysOauthClientService.getSysOauthClientInfoById(id));
    }

    /**
     * 新增客户端
     */
    @Log(value = "客户端", exception = "新增客户端请求异常")
    @PostMapping
    @Operation(summary = "新增客户端")
    public ApiResult<?> add(@RequestBody SysOauthClientDTO oauthClient) {
        return ApiResult.condition(sysOauthClientService.saveSysOauthClient(oauthClient));
    }

    /**
     * 修改客户端
     */
    @Log(value = "客户端", exception = "修改客户端请求异常")
    @PutMapping
    @Operation(summary = "修改客户端")
    public ApiResult<?> edit(@RequestBody SysOauthClientDTO oauthClient) {
        return ApiResult.condition(sysOauthClientService.editSysOauthClient(oauthClient));
    }

    /**
     * 设置客户端状态
     */
    @Log(value = "客户端", exception = "修改客户端请求异常")
    @GetMapping("/setStatus")
    @Operation(summary = "修改客户端状态")
    public ApiResult<?> setOauthClientStatus(@Validated UpdateStatusDTO statusDTO) {
        return ApiResult.condition(sysOauthClientService.setOauthClientStatus(statusDTO));
    }

    /**
     * 删除客户端
     */
    @Log(value = "客户端", exception = "删除客户端请求异常")
    @DeleteMapping
    @Operation(summary = "删除客户端")
    public ApiResult<?> remove(@RequestBody Long[] ids) {
        return ApiResult.condition(sysOauthClientService.removeByIds(Arrays.asList(ids)));
    }

}
