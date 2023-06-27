package com.auiucloud.component.cms.dto;

import com.auiucloud.core.validator.group.RejectGroup;
import com.auiucloud.core.validator.group.ResolveGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
public class GalleryReviewDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -9213269109636758743L;

    @NotNull(message = "审核单号不存在")
    private Long reviewId;

    @NotNull(message = "作品不存在", groups = {ResolveGroup.class})
    private Long galleryId;

    @NotBlank(message = "请填写驳回理由", groups = {RejectGroup.class})
    private String reason;

    @NotNull(message = "审核状态异常")
    private Integer status;

    private String remark;

}
