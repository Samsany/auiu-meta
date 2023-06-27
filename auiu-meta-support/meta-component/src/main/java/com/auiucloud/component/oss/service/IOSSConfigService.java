package com.auiucloud.component.oss.service;

import com.auiucloud.component.sysconfig.domain.SysConfig;
import com.auiucloud.core.oss.props.OssProperties;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author dries
 **/
public interface IOSSConfigService extends IService<SysConfig> {

    /**
     * 获取默认主题
     *
     * @return OssProperties
     */
    OssProperties getOssProperties();

    OssProperties getDefaultOssProperties();

    /**
     * 根据code获取主题信息
     *
     * @param code code编码
     * @return OssProperties
     */
    OssProperties getConfigByCode(String code);

    /**
     * 保存配置信息
     *
     * @param ossProperties OssProperties
     * @return boolean
     */
    boolean saveConfigOss(OssProperties ossProperties);

    /**
     * 修改默认oss
     *
     * @param code 关键词
     * @return boolean
     */
    boolean saveDefaultOss(String code);

    /**
     * 获取默认oss的code
     *
     * @return code
     */
    String defaultOss();

    /**
     * 清理Oss缓存
     */
    void clearOss();

}
