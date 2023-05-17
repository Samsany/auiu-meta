package com.auiucloud.admin.modules.system.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 部门表
 *
 * @TableName sys_dept
 */
@TableName(value = "sys_dept")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDept extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -7337227905171219956L;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 排序
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

}
