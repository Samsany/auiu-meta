package com.auiucloud.ums.controller;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.log.annotation.Log;
import com.auiucloud.ums.domain.UserFeedback;
import com.auiucloud.ums.service.IUserFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 **/
@Tag(name = "用户反馈")
@RestController
@AllArgsConstructor
@RequestMapping("/user/feedback")
public class UserFeedbackController extends BaseController {

    private final IUserFeedbackService userFeedbackService;

    /**
     * 查询作品合集列表
     */
    @Log(value = "用户反馈")
    @GetMapping("/list")
    @Operation(summary = "查询用户反馈列表")
    public ApiResult<?> listPage(@Parameter(hidden = true) Search search,
                                       @Parameter(hidden = true) UserFeedback userFeedback) {
        return ApiResult.data(userFeedbackService.listPage(search, userFeedback));
    }

}
