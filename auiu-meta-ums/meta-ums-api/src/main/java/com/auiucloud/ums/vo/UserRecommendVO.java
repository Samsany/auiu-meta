package com.auiucloud.ums.vo;

import com.auiucloud.ums.domain.UserRecommend;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author dries
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRecommendVO extends UserRecommend {
    @Serial
    private static final long serialVersionUID = 7430343640100156511L;

    private String avatar;

    private String nickname;

    private String bgImg;

    private String remark;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isAttention;

}
