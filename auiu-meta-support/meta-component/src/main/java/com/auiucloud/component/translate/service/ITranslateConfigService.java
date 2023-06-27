package com.auiucloud.component.translate.service;

import com.auiucloud.component.sysconfig.domain.SysConfig;
import com.auiucloud.component.translate.domain.TranslateProperties;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 **/
public interface ITranslateConfigService extends IService<SysConfig> {

    List<TranslateProperties> selectTranslationConfigList();

    TranslateProperties getTranslationConfigByCode(String configCode);

    TranslateProperties getTranslationProperties();

    TranslateProperties getDefaultTranslationProperties();

    boolean saveConfigTranslation(TranslateProperties properties);

    boolean saveDefaultTranslation(String value);

    void clearTranslation();

}
