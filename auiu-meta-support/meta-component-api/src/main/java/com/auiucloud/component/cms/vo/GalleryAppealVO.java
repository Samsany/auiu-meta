package com.auiucloud.component.cms.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "作品申诉VO")
public class GalleryAppealVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 9115395963856349727L;

    private Long id;

    /**
     * 作品ID
     */
    private Long galleryId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 申诉理由
     */
    private String reason;

    /**
     * 状态(0-待处理 1-审核通过 2-驳回)
     */
    private Integer status;

    private GalleryVO gallery;
}
