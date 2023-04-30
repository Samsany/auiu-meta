package com.auiucloud.ums.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.ums.service.IMemberService;
import com.auiucloud.ums.service.IUserFollowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author dries
 **/
@Tag(name = "用户管理开放API")
@RestController
@AllArgsConstructor
@RequestMapping("/ums/user")
public class MemberApiController extends BaseController {

    private final IMemberService memberService;
    private final IUserFollowerService userFollowerService;

    @Log(value = "用户")
    @GetMapping("/attention/page")
    @Operation(summary = "我的关注列表")
    public ApiResult<?> userAttentionPage(Search search) {
        return ApiResult.data(memberService.selectUserAttentionPage(search));
    }

    @Log(value = "用户")
    @GetMapping("/follower/page")
    @Operation(summary = "我的粉丝列表")
    public ApiResult<?> userFollowerPage(Search search) {
        return ApiResult.data(memberService.selectUserFollowerPage(search));
    }

    /**
     * 关注/取消关注
     */
    @Log(value = "用户")
    @PostMapping("/attention/{userId}")
    @Operation(summary = "关注/取消关注")
    @Parameters({
            @Parameter(name = "userId", description = "关注用户ID", in = ParameterIn.PATH, required = true)
    })
    public ApiResult<?> attentionUser(@PathVariable Long userId) {
        return userFollowerService.attentionUser(userId);
    }

}
