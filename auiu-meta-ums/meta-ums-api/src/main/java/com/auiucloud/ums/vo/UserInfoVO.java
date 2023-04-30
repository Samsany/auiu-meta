package com.auiucloud.ums.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class UserInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4368943137148761820L;

    private Long id;

    private String account;

    private String nickname;

    private String avatar;

    private String bgImg;

    private String invitationCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isAttention;

    // 关注数
    private Long attentionNum;

    // 粉丝数
    private Long followerNum;

    // 获赞数
    private Long receivedLikeNum;

    // 作品数
    @Builder.Default
    private Long galleryNum = 0L;

    private String remark;
}
