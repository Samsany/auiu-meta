package com.auiucloud.ums.controller;

import com.auiucloud.core.web.controller.BaseController;
import com.auiucloud.ums.service.IUserFollowerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 **/
@Tag(name = "用户关注粉丝")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/follower")
public class UserFollowerController extends BaseController {

    private final IUserFollowerService userFollowerService;


}
