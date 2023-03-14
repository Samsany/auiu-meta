package com.auiucloud.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "字典数据VO")
public class SysDictDataVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5529800843779511337L;

    @Schema(description = "序号")
    private Long id;
    @Schema(description = "字典标签")
    private String dictLabel;
    @Schema(description = "字典键值")
    private String dictValue;
    @Schema(description = "字典类型")
    private String dictType;
    @Schema(description = "样式属性（其他样式扩展）")
    private String cssClass;
    @Schema(description = "表格回显样式")
    private String listClass;
    @Schema(description = "是否默认")
    private Integer isDefault;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "排序")
    private Integer sort;

}
