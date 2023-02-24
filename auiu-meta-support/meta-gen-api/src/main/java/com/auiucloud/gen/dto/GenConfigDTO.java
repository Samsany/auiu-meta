package com.auiucloud.gen.dto;

import com.baomidou.mybatisplus.annotation.DbType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author dries
 * @createDate 2022-08-16 12-19
 */
@Data
@Schema(name = "GenConfigDTO对象", description = "数据源DTO")
public class GenConfigDTO {

    @Schema(description = "包路径")
    private String packageName;
    @Schema(description = "表前缀")
    private String prefix;
    @Schema(description = "模块名")
    private String modelName;
    @Schema(description = "表名")
    private String tableName;
    @Schema(description = "数据库类型")
    private DbType dbType;
    @Schema(description = "连接地址")
    private String url;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "驱动类型")
    private String driverName;
    @Schema(description = "输出路径")
    private String outputDir;


}
