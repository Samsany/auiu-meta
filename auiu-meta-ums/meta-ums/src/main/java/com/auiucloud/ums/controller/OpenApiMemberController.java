package com.auiucloud.ums.controller;

/**
 * @author dries
 **/

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.ums.service.IMemberService;
import com.auiucloud.ums.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dries
 **/
@Tag(name = "会员管理开放API")
@RestController
@AllArgsConstructor
@RequestMapping("/open-api/ums")
public class OpenApiMemberController {

    private final IMemberService memberService;

    /**
     * 每日推荐会员列表
     */
    @Log(value = "会员")
    @GetMapping("/user/recommend/list")
    @Operation(summary = "每日推荐会员列表")
    public ApiResult<?> userRecommendList() {
        List<UserInfoVO> list = memberService.userRecommendList();
        return ApiResult.data(list);
    }

    @Log(value = "会员")
    @GetMapping("/user/page")
    @Operation(summary = "会员分页列表")
    public ApiResult<?> openUserPage(Search search) {
        return ApiResult.data(memberService.selectOpenApiUserPage(search));
    }

}