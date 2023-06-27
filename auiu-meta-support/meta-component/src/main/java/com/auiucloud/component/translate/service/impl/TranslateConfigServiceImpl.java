package com.auiucloud.component.translate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sysconfig.domain.SysConfig;
import com.auiucloud.component.sysconfig.enums.SysConfigConstants;
import com.auiucloud.component.sysconfig.mapper.SysConfigMapper;
import com.auiucloud.component.translate.domain.TranslateProperties;
import com.auiucloud.component.translate.service.ITranslateConfigService;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.ComponentConstant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.redis.core.RedisService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dries
 **/
@Service("translateConfigService")
@RequiredArgsConstructor
public class TranslateConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig>
        implements ITranslateConfigService {

    private final RedisService redisService;

    @Override
    public List<TranslateProperties> selectTranslationConfigList() {
        SysConfig sysConfig = this.getOne(
                Wrappers.<SysConfig>lambdaQuery()
                        .eq(SysConfig::getConfigCode, ComponentConstant.TRANSLATION_DEFAULT)
                        .eq(SysConfig::getConfigType, SysConfigConstants.SettingTypeEnum.SETTING.getValue())
        );
        // 查询所有的SD_CONFIG配置
        List<SysConfig> list = Optional.ofNullable(this.list(
                Wrappers.<SysConfig>lambdaQuery()
                        .eq(SysConfig::getParentId, sysConfig.getId())
        )).orElse(Collections.emptyList());

        return list.parallelStream()
                .map(this::buildTranslationProperties)
                .toList();
    }

    @Override
    public TranslateProperties getTranslationConfigByCode(String code) {
        TranslateProperties properties = new TranslateProperties();
        SysConfig sysConfig = this.getOne(Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getConfigCode, code)
                .eq(SysConfig::getConfigType, CommonConstant.STATUS_NORMAL_VALUE)
        );

        List<SysConfig> sysConfigList = baseMapper.selectList(Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getParentId, sysConfig.getId())
        );
        properties = listToProps(sysConfigList, properties);
        properties.setName(sysConfig.getConfigName());
        properties.setCode(sysConfig.getConfigCode());
        properties.setRemark(sysConfig.getRemark());
        // 对部分字段进行隐藏显示，保护隐私
        String appSecret = properties.getAppSecret();
        if (StrUtil.isNotBlank(appSecret)) {
            properties.setAppSecret(StrUtil.hide(appSecret, 3, appSecret.length() - 3));
        }
        return properties;
    }

    @Override
    public TranslateProperties getTranslationProperties() {

        TranslateProperties properties = new TranslateProperties();
        // 获取默认的code值
        SysConfig sysConfig = getTranslationDefaultSysConfig();
        properties.setName(sysConfig.getConfigName());
        properties.setCode(sysConfig.getConfigCode());
        properties.setRemark(sysConfig.getRemark());

        // 读取默认code的配置参数
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysConfig::getParentId, sysConfig.getId());
        List<SysConfig> sysConfigList = this.list(queryWrapper);
        properties = listToProps(sysConfigList, properties);
        redisService.set(RedisKeyConstant.TRANSLATION_DEFAULT_CONFIG, properties);
        return properties;
    }

    @Override
    public TranslateProperties getDefaultTranslationProperties() {
        TranslateProperties properties = (TranslateProperties) redisService.get(RedisKeyConstant.TRANSLATION_DEFAULT_CONFIG);
        if (properties == null) {
            properties = this.getTranslationProperties();
        }
        return properties;
    }

    /**
     * 保存配置信息
     *
     * @param properties TranslateProperties
     * @return boolean
     */
    @Transactional
    @Override
    public boolean saveConfigTranslation(TranslateProperties properties) {
        this.clearTranslation();
        // 查询出组配置
        SysConfig sysConfig = this.getOne(Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getConfigCode, properties.getCode()));

        sysConfig.setRemark(properties.getRemark());
        this.updateById(sysConfig);

        LambdaUpdateWrapper<SysConfig> lsc = Wrappers.<SysConfig>update().lambda()
                .set(SysConfig::getConfigValue, properties.getAppId())
                .eq(SysConfig::getParentId, sysConfig.getId())
                .eq(SysConfig::getConfigKey, ComponentConstant.TRANSLATION_APP_ID);
        this.update(lsc);

        // 如果key包括*号，则表示未有更新，则忽略更新该数据
        if (!properties.getAppSecret().contains(StringPool.ASTERISK)) {
            lsc = Wrappers.<SysConfig>update().lambda()
                    .set(SysConfig::getConfigValue, properties.getAppSecret())
                    .eq(SysConfig::getParentId, sysConfig.getId())
                    .eq(SysConfig::getConfigKey, ComponentConstant.TRANSLATION_APP_SECRET);
            this.update(lsc);
        }

        if (properties.getIsDefault().equals(CommonConstant.YES_VALUE)) {
            // 更新配置文件至redis
            return this.saveDefaultTranslation(properties.getProvider());
        }

        return true;
    }

    /**
     * 修改默认oss
     *
     * @param value 关键词
     * @return boolean
     */
    @Transactional
    @Override
    public boolean saveDefaultTranslation(String value) {
        LambdaUpdateWrapper<SysConfig> lsc = Wrappers.<SysConfig>update().lambda()
                .set(SysConfig::getConfigValue, value)
                .eq(SysConfig::getConfigKey, ComponentConstant.CODE_DEFAULT)
                .eq(SysConfig::getConfigCode, ComponentConstant.TRANSLATION_DEFAULT);
        boolean flag = this.update(lsc);
        if (flag) {
            // 清除缓存
            this.clearTranslation();
            // 更新配置文件至redis
            this.getTranslationProperties();
        }

        return flag;
    }


    /**
     * 获取数据库中默认Translation配置信息
     *
     * @return SysConfig对象
     */
    public SysConfig getTranslationDefaultSysConfig() {
        // 获取默认的Translation配置
        LambdaQueryWrapper<SysConfig> lsc = Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getConfigType, CommonConstant.STATUS_NORMAL_VALUE)
                .eq(SysConfig::getConfigCode, ComponentConstant.TRANSLATION_DEFAULT);

        SysConfig sysConfig = this.getOne(lsc);

        lsc = Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getConfigCode, sysConfig.getConfigValue());
        return this.getOne(lsc);
    }

    @Override
    public void clearTranslation() {
        redisService.del(RedisKeyConstant.TRANSLATION_DEFAULT_CONFIG);
    }

    /**
     * TranslateProperties
     *
     * @param sysConfig 配置
     * @return TranslateProperties
     */
    public TranslateProperties buildTranslationProperties(SysConfig sysConfig) {
        TranslateProperties properties = new TranslateProperties();
        properties.setName(sysConfig.getConfigName());
        // 获取默认的code值
        properties.setCode(sysConfig.getConfigCode());
        properties.setRemark(sysConfig.getRemark());
        // 读取默认code的配置参数
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysConfig::getParentId, sysConfig.getId());
        List<SysConfig> sysConfigList = this.list(queryWrapper);
        properties = listToProps(sysConfigList, properties);
        return properties;
    }

    /**
     * 将list转换为Properties
     *
     * @param sysConfigList List列表
     * @param translation   　translation属性
     * @return TranslateProperties
     */
    public TranslateProperties listToProps(List<SysConfig> sysConfigList, TranslateProperties translation) {
        // 给TranslationProperties赋值
        for (SysConfig config : sysConfigList) {
            switch (config.getConfigKey()) {
                case ComponentConstant.TRANSLATION_PROVIDER -> translation.setProvider(config.getConfigValue());
                case ComponentConstant.TRANSLATION_APP_ID -> translation.setAppId(config.getConfigValue());
                case ComponentConstant.TRANSLATION_APP_SECRET -> translation.setAppSecret(config.getConfigValue());
            }

        }
        return translation;
    }

}
