package com.auiucloud.component.cms.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
public class JoinGalleryCollectionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1581443273170383945L;

    @NotNull(message = "请选择合集")
    private Long collectionId;

    @NotNull(message = "请选择作品")
    private List<Long> galleryIds;
}
