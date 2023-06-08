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
public class PicRatioVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8150818067631919125L;

    /**
     * ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 比例
     */
    private String ratio;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;

    /**
     * 消耗积分
     */
    private Integer consumeIntegral;

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

    /**
     * 是否选中
     */
    @Builder.Default
    private Boolean selected = Boolean.FALSE;
}
