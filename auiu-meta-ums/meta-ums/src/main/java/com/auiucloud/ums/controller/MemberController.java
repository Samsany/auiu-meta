package com.auiucloud.ums.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.enums.QueryModeEnum;
import com.auiucloud.core.common.model.dto.UpdatePasswordDTO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.poi.ExcelUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.validator.group.InsertGroup;
import com.auiucloud.core.validator.group.UpdateGroup;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.ums.domain.Member;
import com.auiucloud.ums.dto.AdjustUserPointDTO;
import com.auiucloud.ums.dto.RegisterMemberDTO;
import com.auiucloud.ums.service.IMemberService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 会员用户控制器
 *
 * @author dries
 **/
@Tag(name = "会员")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController extends BaseController {

    private final IMemberService memberService;

    /**
     * 查询会员列表
     */
    @Log(value = "会员", exception = "查询会员列表请求异常")
    @GetMapping("/list")
    @Operation(summary = "查询会员列表")
    @Parameters({
            @Parameter(name = "queryMode", description = "查询模式", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "模糊搜索", in = ParameterIn.QUERY),
            @Parameter(name = "pageNum", description = "当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "每页显示数据", in = ParameterIn.QUERY),
            @Parameter(name = "account", description = "账户", in = ParameterIn.QUERY),
            @Parameter(name = "nickname", description = "昵称", in = ParameterIn.QUERY),
            @Parameter(name = "mobile", description = "手机号", in = ParameterIn.QUERY),
    })
    public ApiResult<?> list(Search search, @Parameter(hidden = true) Member member) {
        QueryModeEnum mode = QueryModeEnum.getQueryModeByCode(search.getQueryMode());
        return switch (mode) {
            case LIST -> ApiResult.data(memberService.list(Wrappers.<Member>lambdaQuery()
                    .eq(ObjectUtil.isNotNull(search.getStatus()), Member::getStatus, search.getStatus())
                    .and(e -> e.like(StrUtil.isNotBlank(search.getKeyword()), Member::getAccount, search.getKeyword())
                            .or().like(StrUtil.isNotBlank(search.getKeyword()), Member::getNickname, search.getKeyword())
                    )
                    .orderByDesc(Member::getCreateTime)
                    .orderByDesc(Member::getId)
            ));
            default -> ApiResult.data(memberService.listPage(search, member));
        };
    }

    /**
     * 获取会员详情
     */
    @Log(value = "会员", exception = "获取会员详情请求异常")
    @GetMapping(value = "/{id}")
    @Operation(summary = "获取会员详情", description = "根据id获取会员详情")
    @Parameters({
            @Parameter(name = "id", required = true, description = "ID", in = ParameterIn.PATH),
    })
    public ApiResult<?> getInfo(@PathVariable("id") Long id) {
        return ApiResult.data(memberService.getMemberInfoById(id));
    }

    /**
     * 新增会员
     */
    @Log(value = "会员", exception = "新增会员请求异常")
    @PostMapping
    @Operation(summary = "新增会员")
    public ApiResult<?> add(@Validated({InsertGroup.class}) @RequestBody RegisterMemberDTO member) {
        return ApiResult.condition(memberService.saveMember(member));
    }

    /**
     * 修改会员
     */
    @Log(value = "会员", exception = "修改会员请求异常")
    @PutMapping
    @Operation(summary = "修改会员")
    public ApiResult<?> edit(@Validated({UpdateGroup.class}) @RequestBody Member member) {
        return ApiResult.condition(memberService.updateMemberById(member));
    }

    /**
     * 修改系统用户密码
     */
    @Log(value = "系统用户", exception = "修改系统用户请求异常")
    @PutMapping("/setNewPassword")
    @Operation(summary = "修改用户密码")
    public ApiResult<?> setNewPassword(@Validated({UpdatePasswordDTO.SetPasswordGroup.class}) @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return ApiResult.condition(memberService.setNewPassword(updatePasswordDTO));
    }

    /**
     * 校验部门名称重复
     */
    @Log(value = "会员", exception = "校验用户账户重复请求异常")
    @PostMapping(value = "/checkUsernameExist")
    @Operation(summary = "校验用户账户重复")
    @Parameters({
            @Parameter(name = "account", required = true, description = "用户账户", in = ParameterIn.QUERY),
            @Parameter(name = "id", description = "用户ID", in = ParameterIn.QUERY),
    })
    public ApiResult<?> checkUsernameExist(@RequestBody Member member) {
        boolean b = memberService.checkUsernameExist(member);
        if (b) {
            return ApiResult.fail("用户账户已存在");
        }
        return ApiResult.success();
    }


    /**
     * 修改会员状态
     */
    @Log(value = "会员", exception = "修改会员请求异常")
    @PutMapping("/setStatus")
    @Operation(summary = "修改会员状态")
    public ApiResult<?> setUserStatus(@Validated @RequestBody UpdateStatusDTO updateStatusDTO) {
        return ApiResult.condition(memberService.setUserStatus(updateStatusDTO));
    }

    /**
     * 修改会员积分
     */
    @Log(value = "会员", exception = "修改会员积分请求异常")
    @PutMapping("/setIntegral")
    @Operation(summary = "修改会员积分")
    public ApiResult<?> setUserIntegral(@Validated @RequestBody AdjustUserPointDTO adjustUserPoint) {
        return ApiResult.condition(memberService.setUserIntegral(adjustUserPoint));
    }


    //    /**
    //     * 修改会员密码
    //     */
    //    @Log(value = "会员", exception = "修改会员请求异常")
    //    @PutMapping("/setNewPassword")
    //    @Operation(summary = "修改会员密码")
    //    public ApiResult<?> setNewPassword(@Validated({UpdatePasswordDTO.SetPasswordGroup.class}) @RequestBody UpdatePasswordDTO updatePasswordDTO) {
    //        return ApiResult.condition(memberService.setNewPassword(updatePasswordDTO));
    //    }

    /**
     * 删除会员
     */
    //    @Log(value = "会员", exception = "删除会员请求异常")
    //    @DeleteMapping
    //    @Operation(summary = "删除会员")
    //    public ApiResult<?> remove(@RequestBody Long[] ids) {
    //        String msg = memberService.deleteMemberByIds(Arrays.asList(ids));
    //        return ApiResult.success(msg);
    //    }

    /**
     * 导出会员
     */
    @Log(value = "会员", exception = "导出会员请求异常")
    @GetMapping("/export")
    @Operation(summary = "导出会员")
    public void export(Search search, Member Member, HttpServletResponse response) {
        List<Member> list = memberService.selectMemberList(search, Member);
        String fileName = "会员" + System.currentTimeMillis();
        ExcelUtil.exportExcel(list, "sheet1", Member.class, fileName, response);
    }

}
