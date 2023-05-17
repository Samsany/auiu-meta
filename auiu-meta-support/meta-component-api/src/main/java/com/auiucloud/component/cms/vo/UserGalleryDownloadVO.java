package com.auiucloud.component.cms.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGalleryDownloadVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5244850791819536361L;

    /**
     * 主键标识
     */
    private Long id;

    /**
     * 下载用户ID
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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
