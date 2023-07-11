package com.auiucloud.ums.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class AdjustUserPointDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -9045614278777257500L;

    @NotNull(message = "请选择用户")
    private Long userId;

    @NotNull(message = "请输入积分数量")
    @Min(value = 0, message = "请输入正确的积分数量")
    private Integer point;

    @NotNull(message = "请选择正确的操作类型")
    @Min(value = 0, message = "请选择正确的操作类型")
    @Max(value = 1, message = "请选择正确的操作类型")
    private Integer status;

    private String remark;
}
