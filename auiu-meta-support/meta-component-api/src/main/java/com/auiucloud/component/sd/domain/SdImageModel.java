package com.auiucloud.component.sd.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
@TableName(value ="sd_image")
public class SdImageModel implements Serializable {
    @Serial
    private static final long serialVersionUID = -6549957757377328561L;

    private Long id;

    private String url;

    private String name;

    private String nsfw;

    private Integer width;

    private Integer height;

    private String hash;

    private String generation_process;

    private LocalDateTime createdAt;

    private LocalDateTime scannedAt;

    private LocalDateTime publishedAt;

    private String mimeType;

    private String needsReview;

    private String postId;

    private String tags;

    @TableField(exist = false)
    private String tag;

    private String modelVersionId;

    private String user;

    private String stats;

    private String reactions;

    private String report;

    private Integer hideMeta;

    private String metaConfig;
}
