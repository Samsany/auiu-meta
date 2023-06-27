package com.auiucloud.component.translate.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
public class BaiduTextTranslateResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -8746655673104023862L;

    /**
     * 源语言 返回用户指定的语言，或者自动检测出的语种（源语言设为 auto 时）
     */
    private String from;

    /**
     * 目标语言 返回用户指定的目标语言
     */
    private String to;

    /**
     * 翻译结果
     */
    private List<TransResult> trans_result;


    private Integer error_code;

    private String error_msg;

    @Data
    public static class TransResult {
        /**
         * 原文
         */
        private String src;

        /**
         * 译文
         */
        private String dst;
    }
}
