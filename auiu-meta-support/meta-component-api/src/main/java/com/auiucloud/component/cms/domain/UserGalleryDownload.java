package com.auiucloud.component.cms.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 作品下载记录表
 *
 * @TableName ums_user_gallery_download
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "ums_user_gallery_download")
public class UserGalleryDownload extends BaseEntity {

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
