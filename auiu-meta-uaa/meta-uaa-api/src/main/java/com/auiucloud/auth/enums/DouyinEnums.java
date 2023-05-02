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

        porn("porn", "图片涉黄"),
        cartoon_leader("cartoon_leader", "领导人漫画"),
        anniversary_flag("anniversary_flag", "特殊标志"),
        sensitive_flag("sensitive_flag", "敏感旗帜"),
        sensitive_text("sensitive_text", "敏感文字"),
        leader_recognition("leader_recognition", "敏感人物"),
        bloody("bloody", "图片血腥"),
        fandongtaibiao("fandongtaibiao", "未准入台标"),
        plant_ppx("plant_ppx", "图片涉毒"),
        high_risk_social_event("high_risk_social_event", "社会事件"),
        high_risk_boom("high_risk_boom", "爆炸"),
        high_risk_money("high_risk_money", "人民币"),
        high_risk_terrorist_uniform("high_risk_terrorist_uniform", "极端服饰"),
        high_risk_sensitive_map("high_risk_sensitive_map", "敏感地图"),
        great_hall("great_hall", "大会堂"),
        cartoon_porn("cartoon_porn", "色情动漫"),
        party_founding_memorial("party_founding_memorial", "建党纪念"),
        ;

        private final String value;
        private final String label;
    }

}
