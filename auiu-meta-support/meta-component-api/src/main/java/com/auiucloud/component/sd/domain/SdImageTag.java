package com.auiucloud.component.sd.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="sd_image_tag")
public class SdImageTag implements Serializable {
    @Serial
    private static final long serialVersionUID = -318686887919801416L;

    private Long id;

    private String title;

    private String name;

    private Integer sort;

    private Integer status;

}
