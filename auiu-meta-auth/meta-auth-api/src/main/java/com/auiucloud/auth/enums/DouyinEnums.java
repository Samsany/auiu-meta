package com.auiucloud.auth.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/
public class DouyinEnums {

    @Getter
    @AllArgsConstructor
    public enum ImageDetection implements IBaseEnum<String> {

        PORN("porn", "图片涉黄"),
        CARTOON_LEADER("cartoon_leader", "领导人漫画"),
        ANNIVERSARY_FLAG("anniversary_flag", "特殊标志"),
        SENSITIVE_FLAG("sensitive_flag", "敏感旗帜"),
        SENSITIVE_TEXT("sensitive_text", "敏感文字"),
        LEADER_RECOGNITION("leader_recognition", "敏感人物"),
        BLOODY("bloody", "图片血腥"),
        FANDONGTAIBIAO("fandongtaibiao", "未准入台标"),
        PLANT_PPX("plant_ppx", "图片涉毒"),
        HIGH_RISK_SOCIAL_EVENT("high_risk_social_event", "社会事件"),
        HIGH_RISK_BOOM("high_risk_boom", "爆炸"),
        HIGH_RISK_MONEY("high_risk_money", "人民币"),
        HIGH_RISK_TERRORIST_UNIFORM("high_risk_terrorist_uniform", "极端服饰"),
        HIGH_RISK_SENSITIVE_MAP("high_risk_sensitive_map", "敏感地图"),
        GREAT_HALL("great_hall", "大会堂"),
        CARTOON_PORN("cartoon_porn", "色情动漫"),
        PARTY_FOUNDING_MEMORIAL("party_founding_memorial", "建党纪念"),
        ;

        private final String value;
        private final String label;
    }

}
