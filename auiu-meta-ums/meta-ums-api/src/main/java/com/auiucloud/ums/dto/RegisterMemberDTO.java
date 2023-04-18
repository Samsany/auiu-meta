package com.auiucloud.ums.dto;

import com.auiucloud.core.validator.InsertGroup;
import com.auiucloud.core.validator.Xss;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author dries
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "后台新增会员信息传输对象")
public class RegisterMemberDTO {

    /**
     * 账号
     */
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账户不能为空")
    @Size(min = 2, max = 30, message = "用户账号长度在2~30个字符之间")
    @Schema(description = "账号")
    private String account;

    /**
     * 真实姓名
     */
    @Xss(message = "用户姓名不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户姓名长度不能超过30个字符")
    @Schema(description = "姓名")
    private String realName;

    /**
     * 用户昵称
     */
    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    @Schema(description = "手机号")
    private String mobile;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = InsertGroup.class)
    @Size(min = 6, max = 18, message = "密码长度不符合规范", groups = InsertGroup.class)
    private String password;
    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空", groups = InsertGroup.class)
    @Size(min = 6, max = 18, message = "密码长度不符合规范", groups = InsertGroup.class)
    private String confirmPassword;

    /**
     * 性别(0-女性 1-男性 2-未知)
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private LocalDate birthday;

    /**
     * 是否启用(0-正常 1-禁用)
     */
    private Integer status;

    /**
     * 账户是否过期(0-过期 1-未过期)
     */
    @Builder.Default
    private boolean accountNonExpired = true;

    /**
     * 账户是否锁定(0-锁定 1-未锁定)
     */
    @Builder.Default
    private boolean accountNonLocked = true;

    /**
     * 证书(密码)是否过期(0-过期 1-未过期)
     */
    @Builder.Default
    private boolean credentialsNonExpired = true;

    /**
     * 身份证号码
     */
    private String cardId;

    /**
     * 用户分组id
     */
    private String groupIds;

    /**
     * 用户标签id
     */
    private String tagIds;

    /**
     * 用户等级
     */
    private Long levelId;

    /**
     * 推广员id
     */
    private Long spreadUid;

    /**
     * 是否为推广员
     */
    private Integer isPromoter;

    /**
     * 注册ip
     */
    private String registerIp;

    /**
     * 注册地址
     */
    private String registerAddress;

    /**
     * 注册方式
     */
    private String registerSource;

}
