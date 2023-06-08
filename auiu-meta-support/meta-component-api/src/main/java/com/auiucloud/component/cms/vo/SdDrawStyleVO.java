package com.auiucloud.component.cms.vo;

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
public class SdDrawStyleVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7704687812279049748L;

    /**
     * ID
     */
    private Long id;

    /**
     * 预览图
     */
    private String pic;

    /**
     * 名称
     */
    private String name;

    /**
     * 画面描述
     */
    private String prompt;

    /**
     * 反向描述
     */
    private String negativePrompt;

    /**
     * 状态(0-正常 1-隐藏)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

}
