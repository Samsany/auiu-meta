package com.auiucloud.ums.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBrokerageChangeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5777960298540636617L;

    /**
     * 标题
     */
    private String title;

    /**
     * 用户ID
     */
    @NotNull
    private Long userId;

    /**
     * 关联ID
     */
    @NotNull
    private Long linkId;

    /**
     * 关联类型
     */
    @NotBlank
    private String linkType;

    /**
     * 佣金
     */
    @NotNull
    private BigDecimal brokerage;

    /**
     * 变动类型(0-增加 1-减少)
     */
    @NotNull
    private Integer changeType;

}
