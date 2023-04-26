package com.auiucloud.core.common.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "状态设置传输对象")
public class UpdateStatusDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -9189414270946835483L;

    @NotNull(message = "ID不能为空")
    private Long id;

    @NotNull
    @Min(value = 0, message = "状态只能为0和1")
    @Max(value = 1, message = "状态只能为0和1")
    private Integer status;

}
