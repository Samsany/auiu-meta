package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.auiucloud.component.sd.component.AiDrawFactory;
import com.auiucloud.component.sd.config.AiDrawConfiguration;
import com.auiucloud.component.sd.domain.SdConfigProperties;
import com.auiucloud.component.sd.service.ISdConfigService;
import com.auiucloud.component.sysconfig.domain.SysConfig;
import com.auiucloud.component.sysconfig.enums.SysConfigConstants;
import com.auiucloud.component.sysconfig.mapper.SysConfigMapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dries
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class SdConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig>
        implements ISdConfigService {

    private final RedisService redisService;

    @Override
    public List<SdConfigProperties> selectSdConfigList() {

        SysConfig sysConfig = this.getOne(
                Wrappers.<SysConfig>lambdaQuery()
                        .eq(SysConfig::getConfigCode, ComponentConstant.SD_DEFAULT)
                        .eq(SysConfig::getConfigType, SysConfigConstants.SettingTypeEnum.SETTING.getValue())
        );
        // 查询所有的SD_CONFIG配置
        List<SysConfig> list = Optional.ofNullable(this.list(
                Wrappers.<SysConfig>lambdaQuery()
                        .eq(SysConfig::getParentId, sysConfig.getId())
        )).orElse(Collections.emptyList());

        return list.parallelStream().map(this::buildSdConfigProperties).toList();
    }

    @Override
    public void clearSdConfig() {
        redisService.del(RedisKeyConstant.SD_DEFAULT_CONFIG);
    }

    @Override
    public SdConfigProperties getSdConfigProperties() {
        SdConfigProperties properties = new SdConfigProperties();
        // 获取默认的code值
        SysConfig sysConfig = getSdDefaultSysConfig();
        properties.setName(sysConfig.getConfigName());
        properties.setCode(sysConfig.getConfigCode());
        properties.setRemark(sysConfig.getRemark());

        // 读取默认code的配置参数
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysConfig::getParentId, sysConfig.getId());
        List<SysConfig> sysConfigList = this.list(queryWrapper);
        properties = listToProps(sysConfigList, properties);
        redisService.set(RedisKeyConstant.SD_DEFAULT_CONFIG, properties);
        return properties;
    }

    @Override
    public SdConfigProperties getConfigByCode(String code) {
        SdConfigProperties properties = new SdConfigProperties();
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

    @Transactional
    @Override
    public boolean saveSdConfig(SdConfigProperties properties) {
        this.clearSdConfig();
        // 查询出组配置
        SysConfig sysConfig = this.getOne(Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getConfigCode, properties.getCode()));

        sysConfig.setRemark(properties.getRemark());
        this.updateById(sysConfig);

        LambdaUpdateWrapper<SysConfig> lsc = Wrappers.<SysConfig>lambdaUpdate()
                .set(SysConfig::getConfigValue, properties.getAppId())
                .eq(SysConfig::getParentId, sysConfig.getId())
                .eq(SysConfig::getConfigKey, ComponentConstant.SD_APP_ID);
        this.update(lsc);

        lsc = Wrappers.<SysConfig>lambdaUpdate()
                .set(SysConfig::getConfigValue, properties.getUrl())
                .eq(SysConfig::getParentId, sysConfig.getId())
                .eq(SysConfig::getConfigKey, ComponentConstant.SD_URL);
        this.update(lsc);

        lsc = Wrappers.<SysConfig>lambdaUpdate()
                .set(SysConfig::getConfigValue, properties.getProvider())
                .eq(SysConfig::getParentId, sysConfig.getId())
                .eq(SysConfig::getConfigKey, ComponentConstant.SD_PROVIDER);
        this.update(lsc);

        // 如果key包括*号，则表示未有更新，则忽略更新该数据
        if (!properties.getAppSecret().contains(StringPool.ASTERISK)) {
            lsc = Wrappers.<SysConfig>lambdaUpdate()
                    .set(SysConfig::getConfigValue, properties.getAppSecret())
                    .eq(SysConfig::getParentId, sysConfig.getId())
                    .eq(SysConfig::getConfigKey, ComponentConstant.SD_APP_SECRET);
            this.update(lsc);
        }

        if (properties.getIsDefault().equals(CommonConstant.YES_VALUE)) {
            // 更新配置文件至redis
            this.saveDefaultSdConfig(properties.getCode());
        }

        // 更新AiFactory
        this.updateAiDrawService(properties);

        return true;
    }

    @Transactional
    @Override
    public boolean saveDefaultSdConfig(String value) {
        LambdaUpdateWrapper<SysConfig> lsc = Wrappers.<SysConfig>update().lambda()
                .set(SysConfig::getConfigValue, value)
                .eq(SysConfig::getConfigCode, ComponentConstant.SD_DEFAULT)
                .eq(SysConfig::getConfigType, CommonConstant.STATUS_NORMAL_VALUE);
        boolean flag = this.update(lsc);
        if (flag) {
            // 清除缓存
            this.clearSdConfig();
            // 更新配置文件至redis
            this.getSdConfigProperties();
        }

        return flag;
    }

    private void updateAiDrawService(SdConfigProperties properties) {
        AiDrawFactory aiDrawFactory = new AiDrawFactory();
        aiDrawFactory.setAppId(properties.getAppId());
        aiDrawFactory.setAppSecret(properties.getAppSecret());
        aiDrawFactory.setUrl(properties.getUrl());
        log.debug("更新前: {}", JSONUtil.toJsonStr(AiDrawConfiguration.AI_DRAW_FACTORY_MAP.get(properties.getCode())));
        AiDrawConfiguration.AI_DRAW_FACTORY_MAP.put(properties.getCode(), aiDrawFactory);
        log.debug("更新后: {}",  JSONUtil.toJsonStr(AiDrawConfiguration.AI_DRAW_FACTORY_MAP.get(properties.getCode())));
    }


    /**
     * 获取数据库中默认配置信息
     *
     * @return SysConfig对象
     */
    public SysConfig getSdDefaultSysConfig() {
        // 获取默认的配置
        LambdaQueryWrapper<SysConfig> lsc = Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getConfigType, CommonConstant.STATUS_NORMAL_VALUE)
                .eq(SysConfig::getConfigCode, ComponentConstant.SD_DEFAULT);
        SysConfig sysConfig = this.getOne(lsc);

        lsc = Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getConfigCode, sysConfig.getConfigValue());
        return this.getOne(lsc);
    }

    public SdConfigProperties buildSdConfigProperties(SysConfig sysConfig) {
        SdConfigProperties properties = new SdConfigProperties();
        // 获取默认的code值
        properties.setName(sysConfig.getConfigName());
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
     * 将list转换为OssProperties
     *
     * @param sysConfigList List列表
     * @param properties    属性
     * @return SdConfigProperties
     */
    public SdConfigProperties listToProps(List<SysConfig> sysConfigList, SdConfigProperties properties) {
        for (SysConfig config : sysConfigList) {
            switch (config.getConfigKey()) {
                case ComponentConstant.SD_PROVIDER -> properties.setProvider(config.getConfigValue());
                case ComponentConstant.SD_URL -> properties.setUrl(config.getConfigValue());
                case ComponentConstant.SD_APP_ID -> properties.setAppId(config.getConfigValue());
                case ComponentConstant.SD_APP_SECRET -> properties.setAppSecret(config.getConfigValue());
            }
        }
        return properties;
    }
}
