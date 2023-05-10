package com.auiucloud.admin.modules.system.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 字典类型表
 *
 * @TableName sys_dict_type
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_dict_type")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SysDictType对象", description = "字典类型表")
public class SysDictType extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -8712432683840221806L;
    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态(0-正常 1-停用)
     */
    private Integer status;

}
