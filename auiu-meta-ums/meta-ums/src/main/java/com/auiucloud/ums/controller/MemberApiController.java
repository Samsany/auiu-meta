package com.auiucloud.ums.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.ums.domain.UserTask;
import com.auiucloud.ums.dto.UpdateUserInfoDTO;
import com.auiucloud.ums.service.*;
import com.auiucloud.ums.vo.UserFeedbackVO;
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
@Tag(name = "用户管理开放API")
@RestController
@AllArgsConstructor
@RequestMapping("/ums/user")
public class MemberApiController extends BaseController {

    private final IMemberService memberService;
    private final IUserFollowerService userFollowerService;
    private final IUserTaskService userTaskService;
    private final IUserIntegralRecordService userIntegralRecordService;
    private final IUserFeedbackService userFeedbackService;

    /**
     * 通过邀请码查询用户详情
     */
    @Log(value = "用户")
    @GetMapping("/info/{invitationCode}")
    @Operation(summary = "根据邀请码查询用户详情")
    @Parameters({
            @Parameter(name = "invitationCode", description = "邀请码", in = ParameterIn.PATH, required = true)
    })
    public ApiResult<?> queryUserInfoVOByInvitationCode(@PathVariable String invitationCode) {
        return ApiResult.data(memberService.queryUserInfoVOByInvitationCode(invitationCode));
    }

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
     * 是否关注用户
     */
    @Log(value = "用户")
    @PostMapping("/check-attention-user/{userId}")
    @Operation(summary = "是否关注用户")
    @Parameters({
            @Parameter(name = "userId", description = "用户ID", in = ParameterIn.PATH, required = true)
    })
    public ApiResult<?> checkAttentionUser(@PathVariable Long userId) {
        return ApiResult.data(userFollowerService.checkedAttentionUser(SecurityUtil.getUserId(), userId));
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

    /**
     * 编辑用户信息
     */
    @Log(value = "用户")
    @PostMapping("/edit")
    @Operation(summary = "编辑用户信息")
    public ApiResult<?> editUserInfo(@Validated @RequestBody UpdateUserInfoDTO userInfoDTO) {
        return memberService.updateAppletUserInfo(userInfoDTO);
    }

    /**
     * 查询会员任务列表
     */
    @Log(value = "用户")
    @GetMapping("/task/list")
    @Operation(summary = "查询任务列表")
    public ApiResult<?> listUserTask(UserTask userTask) {
        return ApiResult.data(userTaskService.listUserTask(userTask));
    }

    /**
     * 完成任务
     */
    @Log(value = "用户")
    @GetMapping("/task/complete/{taskId}")
    @Operation(summary = "完成任务")
    public ApiResult<?> completeUserTask(@PathVariable Long taskId) {
        return ApiResult.condition(userTaskService.completeUserTask(taskId));
    }

    @Log(value = "用户")
    @GetMapping("/point/receive/page")
    @Operation(summary = "我的积分领取列表")
    public ApiResult<?> userPointReceivePage(Search search) {
        return ApiResult.data(userIntegralRecordService.selectUserPointReceivePage(search));
    }

    @Log(value = "用户")
    @GetMapping("/point/consumption/page")
    @Operation(summary = "我的积分消耗列表")
    public ApiResult<?> userPointConsumptionPage(Search search) {
        return ApiResult.data(userIntegralRecordService.selectUserPointConsumptionPage(search));
    }


    @Log(value = "用户")
    @PostMapping("/submit/feedback")
    @Operation(summary = "用户提交使用反馈")
    public ApiResult<?> userSubmitFeedback(@Validated @RequestBody UserFeedbackVO feedbackVO) {
        return ApiResult.condition(userFeedbackService.submitFeedback(feedbackVO));
    }
}
