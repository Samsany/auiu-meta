package com.auiucloud.admin.modules.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "字典VO")
public class SysDictVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -596133727530673562L;

    @Schema(description = "序号")
    private Long id;
    @Schema(description = "字典类型名称")
    private String dictName;
    @Schema(description = "字典类型")
    private String dictType;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "字典数据")
    private List<SysDictDataVO> dictDataList;

}
