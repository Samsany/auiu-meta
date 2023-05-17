package com.auiucloud.component.sysconfig.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 系统协议配置表
 *
 * @TableName sys_protocol_config
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_protocol_config")
public class SysProtocolConfig extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 6511077832684169263L;

    /**
     * 协议名称
     */
    private String title;

    /**
     * 协议内容
     */
    private String content;

    /**
     * 协议类型
     * 0-隐私协议 1-用户协议 2-付费会员协议 3-积分协议 4-积分充值协议 5-注销协议
     */
    private Integer type;

    /**
     * 状态(0-正常 1-隐藏)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 租户ID
     */
    private Integer tenantId;
}
