package com.auiucloud.admin.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UpdateStatusDTO {

    @NotNull(message = "ID不能为空")
    private Long id;

    @NotNull
    @Min(value = 0, message = "状态只能为0和1")
    @Max(value = 1, message = "状态只能为0和1")
    private Integer status;

}
