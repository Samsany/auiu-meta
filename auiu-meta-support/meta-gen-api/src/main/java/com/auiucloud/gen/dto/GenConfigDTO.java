package com.auiucloud.gen.dto;

import com.baomidou.mybatisplus.annotation.DbType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dries
 * @createDate 2022-08-16 12-19
 */
@Data
@ApiModel(value = "GenConfigDTO对象", description = "数据源DTO")
public class GenConfigDTO {

    @ApiModelProperty(value = "包路径")
    private String packageName;
    @ApiModelProperty(value = "表前缀")
    private String prefix;
    @ApiModelProperty(value = "模块名")
    private String modelName;
    @ApiModelProperty(value = "表名")
    private String tableName;
    @ApiModelProperty(value = "数据库类型")
    private DbType dbType;
    @ApiModelProperty(value = "连接地址")
    private String url;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "驱动类型")
    private String driverName;
    @ApiModelProperty(value = "输出路径")
    private String outputDir;


}
