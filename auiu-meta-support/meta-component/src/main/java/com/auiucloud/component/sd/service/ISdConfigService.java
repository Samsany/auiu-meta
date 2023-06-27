package com.auiucloud.component.sd.service;

import com.auiucloud.component.sd.domain.SdConfigProperties;
import com.auiucloud.component.sysconfig.domain.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 **/
public interface ISdConfigService extends IService<SysConfig> {

    List<SdConfigProperties> selectSdConfigList();

    void clearSdConfig();

    SdConfigProperties getSdConfigProperties();

    SdConfigProperties getConfigByCode(String configCode);

    boolean saveSdConfig(SdConfigProperties properties);

    boolean saveDefaultSdConfig(String value);

}
