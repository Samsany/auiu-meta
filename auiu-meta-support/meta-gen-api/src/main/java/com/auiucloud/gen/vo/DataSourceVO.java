package com.auiucloud.gen.vo;

import com.auiucloud.gen.domain.SysDataSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * @author dries
 * @createDate 2022-08-24 13-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ApiModel(value = "DataSourceVO对象", description = "系统数据源视图")
public class DataSourceVO implements Serializable {

    private static final long serialVersionUID = 6166228667793805370L;
    public static Function<SysDataSource, DataSourceVO> convertDataSource = dataSource -> DataSourceVO.builder()
            .id(dataSource.getId())
            .name(dataSource.getName())
            .dbType(dataSource.getDbType())
            .driverClass(dataSource.getDriverClass())
            .url(dataSource.getUrl())
            .port(dataSource.getPort())
            .databaseName(dataSource.getDatabaseName())
            .jdbcParams(dataSource.getJdbcParams())
            .username(dataSource.getUsername())
            .sort(dataSource.getSort())
            .status(dataSource.getStatus())
            .createBy(dataSource.getCreateBy())
            .createTime(dataSource.getCreateTime())
            .updateBy(dataSource.getUpdateBy())
            .updateTime(dataSource.getUpdateTime())
            .remark(dataSource.getRemark())
            .build();
    /**
     * 编号主键标识
     */
    @ApiModelProperty("编号")
    private Long id;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
    /**
     * 数据库类型
     */
    @ApiModelProperty("数据库类型")
    private String dbType;
    /**
     * 驱动类型
     */
    @ApiModelProperty("驱动类型")
    private String driverClass;
    /**
     * 连接地址
     */
    @ApiModelProperty("连接地址")
    private String url;
    /**
     * 端口号
     */
    @ApiModelProperty("端口号")
    private String port;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;
    /**
     * 数据库名
     */
    @ApiModelProperty("数据库名")
    private String databaseName;
    /**
     * Jdbc连接参数
     */
    @ApiModelProperty("Jdbc连接参数")
    private String jdbcParams;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateBy;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
}
