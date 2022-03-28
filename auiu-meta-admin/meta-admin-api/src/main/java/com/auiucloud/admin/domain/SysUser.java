package com.auiucloud.admin.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 系统用户表
 *
 * @author dries
 */
@Data
@TableName(value = "sys_user")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysUser对象", description = "系统用户表")
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 4340352143097572220L;

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
     * 加密密码
     */
    private String password;

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
     * 性别【0-女性 1-男性 2-未知】
     */
    private Integer gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 注册方式
     */
    private String registerSource;

    /**
     * 城市
     */
    private String city;

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
     * 是否启用 【 0 - 禁用  1 - 启用】
     */
    private boolean enabled;

    /**
     * 账户是否过期 【 1 - 未过期 0- 过期】
     */
    private boolean accountNonExpired;

    /**
     * 账户是否锁定 【 1 - 未锁定 0 - 锁定】
     */
    private boolean accountNonLocked;

    /**
     * 账户是否可用（是否被删除）【 1 - 可用  0- 不可用】
     */
    private boolean credentialsNonExpired;

}
