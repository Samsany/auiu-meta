package com.auiucloud.component.cms.dto;

import com.auiucloud.core.validator.group.RejectGroup;
import com.auiucloud.core.validator.group.ResolveGroup;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
public class GalleryReviewBatchDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -9213269109636758743L;

    @NotEmpty(message = "审核单号不存在")
    private List<Long> reviewIds;

    @NotEmpty(message = "作品不存在")
    private List<Long> galleryIds;

    private String reason;

    @NotNull(message = "审核状态异常")
    @Min(value = 0, message = "审核状态异常")
    @Max(value = 1, message = "审核状态异常")
    private Integer status;

    private String remark;

}
