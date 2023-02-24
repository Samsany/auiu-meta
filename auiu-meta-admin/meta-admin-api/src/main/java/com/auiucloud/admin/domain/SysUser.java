package com.auiucloud.admin.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.Date;

/**
 * 系统用户表
 *
 * @author dries
 * @TableName sys_user
 * @createDate 2022-05-31 14:59:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_user")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SysUser对象", description = "系统用户表")
public class SysUser extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -4048530401942903801L;

    /**
     * 账号
     */
    private String account;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别(0-女性 1-男性 2-未知)
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 是否启用(0-正常 1-禁用)
     */
    private Integer status;

    /**
     * 账户是否过期(0-过期 1-未过期)
     */
    private boolean accountNonExpired;

    /**
     * 账户是否锁定(0-锁定 1-未锁定)
     */
    private boolean accountNonLocked;

    /**
     * 证书(密码)是否过期(0-过期 1-未过期)
     */
    private boolean credentialsNonExpired;

    /**
     * 内置用户(0-否 1-是)
     */
    private boolean builtIn;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 登录ip
     */
    private String loginIp;

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
