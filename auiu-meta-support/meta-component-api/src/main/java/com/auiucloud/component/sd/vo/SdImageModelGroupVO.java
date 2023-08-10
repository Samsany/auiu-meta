package com.auiucloud.component.sd.vo;

import com.auiucloud.component.sd.domain.SdImageModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SdImageModelGroupVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4341231013243449624L;

    private String postId;

    private String cover;

    private List<SdImageModel> images;

}
