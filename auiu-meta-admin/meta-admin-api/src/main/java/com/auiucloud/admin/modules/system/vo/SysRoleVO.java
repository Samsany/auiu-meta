package com.auiucloud.admin.modules.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author dries
 * @createDate 2022-06-08 18-00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "系统角色VO")
public class SysRoleVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3693406136841400226L;

    @Schema(description = "序号")
    private Long id;
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "角色编码")
    private String roleCode;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "创建人")
    private String createBy;
    @Schema(description = "更新人")
    private String updateBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "关联菜单")
    private Set<Long> menuIds;

}
