package com.auiucloud.component.cms.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 作品下载记录表
 * @TableName cms_gallery_download_record
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value ="cms_gallery_download_record")
public class GalleryDownloadRecord extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -1873313212887749476L;

    /**
     * 用户ID
     */
    private Long uId;

    /**
     * 作品ID
     */
    private Long gId;

    /**
     * 下载消耗积分
     */
    private Integer downloadIntegral;

}
