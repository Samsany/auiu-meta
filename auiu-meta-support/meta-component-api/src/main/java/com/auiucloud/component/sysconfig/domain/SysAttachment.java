package com.auiucloud.component.sysconfig.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 附件表
 *
 * @TableName sys_attachment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_attachment")
public class SysAttachment extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -6406455747963034423L;
    /**
     * 存储ID
     */
    private Long storageId;

    /**
     * 组ID
     */
    private Long attachmentGroupId;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 宽
     */
    private Integer width;

    /**
     * 高
     */
    private Integer height;

    /**
     * 文件地址
     */
    private String url;

    /**
     * 缩略图地址
     */
    private String thumbUrl;

    /**
     * 上传文件名
     */
    private String fileName;

    /**
     * 类型
     */
    private Integer fileType;

    /**
     *
     */
    private Long tenantId;

    /**
     * 是否加入回收站(0-否 1-是)
     */
    private Integer isRecycle;

}
