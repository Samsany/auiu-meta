package com.auiucloud.auth.enums;

import com.auiucloud.core.common.constant.Oauth2Constant;

/**
 * @author dries
 **/
public enum ValidateCodeTypeEnum {

    /**
     * 短信验证码
     */
    SMS {
        @Override
        public String getParamNameOnValidate() {
            return Oauth2Constant.DEFAULT_PARAMETER_NAME_CODE_SMS;
        }
    },
    /**
     * 图片验证码
     */
    IMAGE {
        @Override
        public String getParamNameOnValidate() {
            return Oauth2Constant.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
        }
    };

    /**
     * 校验时从请求中获取的参数名字
     */
    public abstract String getParamNameOnValidate();

}
