package com.auiucloud.component.cms.vo;

import com.auiucloud.component.sysconfig.domain.SysAttachment;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
public class GalleryUploadBatchVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2015105025813789328L;

    private List<SysAttachment> images;

    private Long userId;

    private String title;

    private Long collectionId;

    private Long tagId;

    private String remark;

    private Integer sort;

}
