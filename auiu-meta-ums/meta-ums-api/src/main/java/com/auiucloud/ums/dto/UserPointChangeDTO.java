package com.auiucloud.ums.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPointChangeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5777960298540636617L;

    /**
     * 用户ID
     */
    @NotNull
    private Long userId;

    /**
     * 积分
     */
    @NotNull
    private Integer integral;

    /**
     * 变动类型(0-增加 1-减少)
     */
    @NotNull
    private Integer changeType;

}
