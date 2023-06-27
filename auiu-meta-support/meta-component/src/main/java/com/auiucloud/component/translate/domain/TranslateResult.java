package com.auiucloud.component.translate.domain;

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
public class TranslateResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 137424223135278354L;

    /**
     * 源语言 返回用户指定的语言，或者自动检测出的语种（源语言设为 auto 时）
     */
    private String from;

    /**
     * 目标语言 返回用户指定的目标语言
     */
    private String to;

    /**
     * 原文
     */
    private String src;

    /**
     * 译文
     */
    private String dst;

}
