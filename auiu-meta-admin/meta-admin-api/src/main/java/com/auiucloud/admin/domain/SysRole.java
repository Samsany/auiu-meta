package com.auiucloud.admin.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 系统角色表
 *
 * @author dries
 * @TableName sys_role
 * @createDate 2022-05-31 14:59:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_role")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysRole对象", description = "系统角色表")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = -8102632721053269028L;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 后台用户数量
     */
    private Integer adminCount;

    /**
     * 启用状态(0-禁用 1-启用)
     */
    private boolean status;

    /**
     * 内置角色(0-否 1-是)
     */
    private boolean builtIn;

    /**
     * 排序
     */
    private Integer sort;

}
