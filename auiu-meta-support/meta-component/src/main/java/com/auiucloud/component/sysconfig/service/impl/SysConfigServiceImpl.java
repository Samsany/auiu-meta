package com.auiucloud.component.sysconfig.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sysconfig.domain.AppletConfigProperties;
import com.auiucloud.component.sysconfig.domain.SysConfig;
import com.auiucloud.component.sysconfig.domain.UserConfigProperties;
import com.auiucloud.component.sysconfig.enums.SysConfigConstants;
import com.auiucloud.component.sysconfig.mapper.SysConfigMapper;
import com.auiucloud.component.sysconfig.service.ISysConfigService;
import com.auiucloud.component.sysconfig.vo.SysConfigTreeVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.ComponentConstant;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.tree.ForestNodeMerger;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.oss.props.OssProperties;
import com.auiucloud.core.redis.core.RedisService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【sys_config(配置表)】的数据库操作Service实现
 * @createDate 2023-03-10 00:29:34
 */
@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig>
        implements ISysConfigService {

    private final RedisService redisService;

    /**
     * 获取系统默认配置（一级配置）
     *
     * @return List<SysConfig>
     */
    @Override
    public List<SysConfig> selectDefaultConfigList() {
        LambdaQueryWrapper<SysConfig> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysConfig::getParentId, CommonConstant.ROOT_NODE_ID);
        queryWrapper.eq(SysConfig::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        queryWrapper.orderByDesc(SysConfig::getSort);
        return this.list(queryWrapper);
    }

    /**
     * 获取所有基础信息配置列表
     *
     * @param config
     * @return List<SysConfig>
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {
        LambdaQueryWrapper<SysConfig> queryWrapper = buildSearchParams(config);
        return this.list(queryWrapper);
    }

    @Override
    public List<SysConfigTreeVO> selectConfigTreeList(SysConfig config) {
        LambdaQueryWrapper<SysConfig> queryWrapper = buildSearchParams(config);
        List<SysConfig> list = Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
        return ForestNodeMerger.merge(
                list.stream().map(it -> {
                    SysConfigTreeVO configTreeVO = new SysConfigTreeVO();
                    BeanUtils.copyProperties(it, configTreeVO);
                    return configTreeVO;
                }).collect(Collectors.toList()));
    }

    @Override
    public PageUtils treePageList(Search search, SysConfig config) {
        config.setParentId(CommonConstant.ROOT_NODE_ID);
        config.setConfigType(CommonConstant.STATUS_NORMAL_VALUE);
        LambdaQueryWrapper<SysConfig> queryWrapper = buildSearchParams(config);
        List<SysConfig> sysConfigList = this.list(queryWrapper);
        PageUtils pageUtils = new PageUtils(search, sysConfigList);

        List<SysConfig> list = Optional.ofNullable(this.list(Wrappers.<SysConfig>lambdaQuery()
                .ne(SysConfig::getParentId, CommonConstant.ROOT_NODE_ID)
                .eq(SysConfig::getConfigType, CommonConstant.STATUS_NORMAL_VALUE))).orElse(Collections.emptyList());
        List<SysConfig> records = Optional.ofNullable(sysConfigList).orElse(Collections.emptyList());
        List<SysConfig> allRecords = new ArrayList<>();
        allRecords.addAll(records);
        allRecords.addAll(list);

        List<SysConfigTreeVO> merge = ForestNodeMerger.merge(
                allRecords.parallelStream().map(it -> {
                    SysConfigTreeVO sysConfigTreeVO = new SysConfigTreeVO();
                    BeanUtils.copyProperties(it, sysConfigTreeVO);
                    return sysConfigTreeVO;
                }).sorted(Comparator.comparing(SysConfigTreeVO::getSort).reversed()).collect(Collectors.toList()));
        merge.parallelStream().forEach(it -> it.setHasChildren(CollUtil.isNotEmpty(it.getChildren())));
        pageUtils.setList(merge);
        return pageUtils;
    }

    /**
     * 分页获取所有配置
     *
     * @param config
     * @return PageUtils
     */
    @Override
    public PageUtils listPage(Search search, SysConfig config) {
        LambdaQueryWrapper<SysConfig> queryWrapper = buildSearchParams(config);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSysConfig(SysConfig config) {
        return this.save(config);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSysConfig(SysConfig config) {
        return this.updateById(config);
    }

    @Override
    public boolean setSysConfigStatus(UpdateStatusDTO statusDTO) {
        LambdaUpdateWrapper<SysConfig> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysConfig::getStatus, statusDTO.getStatus());
        updateWrapper.eq(SysConfig::getId, statusDTO.getId());

        return update(updateWrapper);
    }

    /**
     * 获取数据库中默认SYSTEM配置信息
     *
     * @return SysConfig对象
     */
    public SysConfig getSystemDefaultSysConfig() {
        // 获取默认SYSTEM配置信息
        LambdaQueryWrapper<SysConfig> queryWrapper = Wrappers.<SysConfig>query().lambda()
                .eq(SysConfig::getConfigCode, SysConfigConstants.SettingEnum.BASE_SETTING.getValue());
        return this.getOne(queryWrapper);
    }

    /**
     * 获取默认OssProperties配置信息
     *
     * @return OssProperties
     */
    @Override
    public OssProperties getOssProperties() {
        OssProperties oss = new OssProperties();
        // 获取默认的code值
        String code = getOSSDefaultSysConfig().getConfigValue();
        // 读取默认code的配置参数
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysConfig::getConfigCode, code);
        List<SysConfig> sysConfigList = this.list(queryWrapper);
        oss = listToProps(sysConfigList, oss);
        redisService.set(RedisKeyConstant.OSS_DEFAULT_CONFIG, oss);
        return oss;
    }

    @Override
    public OssProperties getDefaultOssProperties() {
        OssProperties oss = (OssProperties) redisService.get(RedisKeyConstant.OSS_DEFAULT_CONFIG);
        if (oss == null) {
            oss = this.getOssProperties();
        }
        return oss;
    }

    /**
     * 根据code获取主题信息
     *
     * @param code code编码
     * @return OssProperties
     */
    @Override
    public OssProperties getConfigByCode(String code) {
        OssProperties oss = new OssProperties();
        LambdaQueryWrapper<SysConfig> queryWrapper = Wrappers.<SysConfig>query().lambda().eq(SysConfig::getConfigCode, code);
        List<SysConfig> sysConfigList = this.baseMapper.selectList(queryWrapper);
        oss = listToProps(sysConfigList, oss);
        // 对oss部分字段进行隐藏显示，保护隐私
        oss.setSecretKey(StrUtil.hide(oss.getSecretKey(), 3, 23));
        return oss;
    }

    /**
     * 保存配置信息
     *
     * @param ossProperties OssProperties
     * @return boolean
     */
    @Transactional
    @Override
    public boolean saveConfigOss(OssProperties ossProperties) {

        LambdaUpdateWrapper<SysConfig> lsc = Wrappers.<SysConfig>update().lambda()
                .set(SysConfig::getConfigValue, ossProperties.getEndpoint())
                .eq(SysConfig::getConfigCode, ossProperties.getProvider())
                .eq(SysConfig::getConfigKey, ComponentConstant.OSS_ENDPOINT);
        this.update(lsc);

        lsc = Wrappers.<SysConfig>update().lambda()
                .set(SysConfig::getConfigValue, ossProperties.getCustomDomain())
                .eq(SysConfig::getConfigCode, ossProperties.getProvider())
                .eq(SysConfig::getConfigKey, ComponentConstant.OSS_CUSTOM_DOMAIN);
        this.update(lsc);

        lsc = Wrappers.<SysConfig>update().lambda()
                .set(SysConfig::getConfigValue, ossProperties.getAccessKey())
                .eq(SysConfig::getConfigCode, ossProperties.getProvider())
                .eq(SysConfig::getConfigKey, ComponentConstant.OSS_ACCESS_KEY);
        this.update(lsc);

        // 如果key包括*号，则表示未有更新，则忽略更新该数据
        if (!ossProperties.getSecretKey().contains(StringPool.ASTERISK)) {
            lsc = Wrappers.<SysConfig>update().lambda()
                    .set(SysConfig::getConfigValue, ossProperties.getSecretKey())
                    .eq(SysConfig::getConfigCode, ossProperties.getProvider())
                    .eq(SysConfig::getConfigKey, ComponentConstant.OSS_SECRET_KEY);
            this.update(lsc);
        }

        lsc = Wrappers.<SysConfig>update().lambda()
                .set(SysConfig::getConfigValue, ossProperties.getBucketName())
                .eq(SysConfig::getConfigCode, ossProperties.getProvider())
                .eq(SysConfig::getConfigKey, ComponentConstant.OSS_BUCKET_NAME);
        this.update(lsc);
        // 更新配置文件至redis
        return this.saveDefaultOss(ossProperties.getProvider());
    }

    /**
     * 修改默认oss
     *
     * @param value 关键词
     * @return boolean
     */
    @Override
    public boolean saveDefaultOss(String value) {
        LambdaUpdateWrapper<SysConfig> lsc = Wrappers.<SysConfig>update().lambda()
                .set(SysConfig::getConfigValue, value)
                .eq(SysConfig::getConfigKey, ComponentConstant.CODE_DEFAULT)
                .eq(SysConfig::getConfigCode, ComponentConstant.OSS_DEFAULT);
        boolean flag = this.update(lsc);
        if (flag) {
            // 清除缓存
            this.clearOss();
            // 更新配置文件至redis
            this.getOssProperties();
        }

        return flag;
    }

    /**
     * 获取默认oss的code
     *
     * @return code
     */
    @Override
    public String defaultOss() {
        return getOSSDefaultSysConfig().getConfigValue();
    }

    /**
     * 清理Oss缓存
     */
    @Override
    public void clearOss() {
        redisService.del(RedisKeyConstant.OSS_DEFAULT_CONFIG);
    }

    /**
     * 获取数据库中默认OSS配置信息
     *
     * @return SysConfig对象
     */
    public SysConfig getOSSDefaultSysConfig() {
        // 获取默认的oss配置
        LambdaQueryWrapper<SysConfig> lsc = Wrappers.<SysConfig>query().lambda()
                .eq(SysConfig::getConfigCode, ComponentConstant.OSS_DEFAULT);
        return this.getOne(lsc);
    }

    /**
     * 将list转换为OssProperties
     *
     * @param sysConfigList List列表
     * @param oss           　oss属性
     * @return OssProperties
     */
    public OssProperties listToProps(List<SysConfig> sysConfigList, OssProperties oss) {
        // 给OssProperties赋值
        for (SysConfig config : sysConfigList) {
            switch (config.getConfigKey()) {
                case ComponentConstant.OSS_PROVIDER -> oss.setProvider(config.getConfigValue());
                case ComponentConstant.OSS_ENDPOINT -> oss.setEndpoint(config.getConfigValue());
                case ComponentConstant.OSS_CUSTOM_DOMAIN -> oss.setCustomDomain(config.getConfigValue());
                case ComponentConstant.OSS_ACCESS_KEY -> oss.setAccessKey(config.getConfigValue());
                case ComponentConstant.OSS_SECRET_KEY -> oss.setSecretKey(config.getConfigValue());
                case ComponentConstant.OSS_PATH_STYLE_ACCESS ->
                        oss.setPathStyleAccess(Boolean.valueOf(config.getConfigValue()));
                case ComponentConstant.OSS_BUCKET_NAME -> oss.setBucketName(config.getConfigValue());
            }

        }
        return oss;
    }

    /**
     * 获取SYSTEM配置信息
     * 构建查询条件
     *
     * @return SysConfig对象
     */
    private LambdaQueryWrapper<SysConfig> buildSearchParams(SysConfig config) {
        LambdaQueryWrapper<SysConfig> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(config.getConfigName()), SysConfig::getConfigName, config.getConfigName());
        queryWrapper.eq(ObjectUtil.isNotNull(config.getParentId()), SysConfig::getParentId, config.getParentId());
        queryWrapper.eq(ObjectUtil.isNotNull(config.getConfigType()), SysConfig::getConfigType, config.getConfigType());
        queryWrapper.eq(ObjectUtil.isNotNull(config.getStatus()), SysConfig::getStatus, config.getStatus());
        queryWrapper.eq(StrUtil.isNotBlank(config.getConfigKey()), SysConfig::getConfigName, config.getConfigKey());
        queryWrapper.orderByDesc(SysConfig::getSort);

        return queryWrapper;
    }

    // 清理小程序配置缓存
    @Override
    public void clearAppletConfig(String appId) {
        redisService.del(RedisKeyConstant.cacheAppletConfigKey(appId));
    }

    @Override
    public AppletConfigProperties getAppletConfigProperties(String appId) {
        AppletConfigProperties applet = (AppletConfigProperties) redisService.get(RedisKeyConstant.cacheAppletConfigKey(appId));
        if (ObjectUtil.isNotNull(applet)) {
            return applet;
        }

        AtomicReference<AppletConfigProperties> appletConfigProperties = new AtomicReference<>(new AppletConfigProperties());
        // 查询小程序配置列表
        List<SysConfig> list = Optional.ofNullable(this.list(
                Wrappers.<SysConfig>lambdaQuery()
                        .eq(SysConfig::getConfigCode, SysConfigConstants.SettingEnum.APPLET_SETTING.getValue())
                        .eq(SysConfig::getConfigType, SysConfigConstants.SettingTypeEnum.SETTING.getValue())
                        .ne(SysConfig::getParentId, CommonConstant.ROOT_NODE_ID)
        )).orElse(Collections.emptyList());

        Map<String, Object> map = new HashMap<>();
        // 获取小程序配置项
        listToProps(list, map);
        applet = (AppletConfigProperties) map.get(appId);
        redisService.set(RedisKeyConstant.cacheAppletConfigKey(appId), applet);
        return applet;
    }

    /**
     * 将list转换为AppletProperties
     *
     * @param sysConfigList List列表
     */
    public void listToProps(List<SysConfig> sysConfigList, Map<String, Object> appletMap) {
        sysConfigList.parallelStream().forEach(config -> {
            if (config.getConfigType().equals(SysConfigConstants.SettingTypeEnum.SETTING.getValue())) {
                List<SysConfig> children = Optional.ofNullable(this.list(Wrappers.<SysConfig>lambdaQuery()
                        .eq(SysConfig::getParentId, config.getId())
                )).orElse(Collections.emptyList());
                listToProps(children, appletMap);
            } else {
                AppletConfigProperties applet = new AppletConfigProperties();
                switch (config.getConfigKey()) {
                    case SysConfigConstants.APP_ID -> applet.setAppId(config.getConfigValue());
                    case SysConfigConstants.APP_SECRET -> applet.setAppSecret(config.getConfigValue());
                    case SysConfigConstants.APPLET_NAME -> applet.setAppletName(config.getConfigValue());
                    case SysConfigConstants.IS_ENABLE_VIDEO_AD ->
                            applet.setIsEnableVideoAd(Objects.equals(config.getConfigValue(), "0"));
                    case SysConfigConstants.IS_ENABLE_INTERSTITIAL_AD ->
                            applet.setIsEnableInterstitialAd(Objects.equals(config.getConfigValue(), "0"));
                    case SysConfigConstants.REWARDED_VIDEO_AD -> applet.setRewardedVideoAd(config.getConfigValue());
                    case SysConfigConstants.INTERSTITIAL_AD -> applet.setInterstitialAd(config.getConfigValue());
                }
                appletMap.put(applet.getAppId(), applet);
            }

        });

    }

    @Override
    public UserConfigProperties getUserConfigProperties() {
        UserConfigProperties userConfigProperties = (UserConfigProperties) redisService.get(RedisKeyConstant.USER_CONFIG);
        if (ObjectUtil.isNotNull(userConfigProperties)) {
            return userConfigProperties;
        } else {
            userConfigProperties = new UserConfigProperties();
        }

        // 查询用户配置列表
        List<SysConfig> list = Optional.ofNullable(this.list(
                Wrappers.<SysConfig>lambdaQuery()
                        .in(SysConfig::getConfigCode, List.of(
                                SysConfigConstants.SettingEnum.USER_SETTING.getValue(),
                                SysConfigConstants.SettingEnum.FENXIAO_SETTING.getValue()
                        ))
                        .eq(SysConfig::getConfigType, SysConfigConstants.SettingTypeEnum.SETTING.getValue())
                        .ne(SysConfig::getParentId, CommonConstant.ROOT_NODE_ID)
        )).orElse(Collections.emptyList());
        listToUserConfigProps(list, userConfigProperties);
        redisService.set(RedisKeyConstant.USER_CONFIG, userConfigProperties);
        return userConfigProperties;
    }

    /**
     * 将list转换为UserConfigProperties
     *
     * @param sysConfigList List列表
     */
    private void listToUserConfigProps(List<SysConfig> sysConfigList, UserConfigProperties userConfig) {
        sysConfigList.parallelStream().forEach(config -> {
            if (config.getConfigType().equals(SysConfigConstants.SettingTypeEnum.SETTING.getValue())) {
                // 查询子集
                List<SysConfig> children = Optional.ofNullable(this.list(Wrappers.<SysConfig>lambdaQuery()
                        .eq(SysConfig::getParentId, config.getId())
                )).orElse(Collections.emptyList());
                listToUserConfigProps(children, userConfig);
            } else {
                switch (config.getConfigKey()) {
                    case SysConfigConstants.USER_DEFAULT_AVATAR ->
                            userConfig.setUserDefaultAvatar(config.getConfigValue());
                    case SysConfigConstants.USER_DEFAULT_BG_IMAGES ->
                            userConfig.setUserDefaultBgImages(config.getConfigValue());
                    case SysConfigConstants.IS_ENABLE_PAID_MEMBER ->
                            userConfig.setIsEnablePaidMember(Boolean.valueOf(config.getConfigValue()));
                    case SysConfigConstants.IS_ENABLE_POINT_RECHARGE ->
                            userConfig.setIsEnablePointRecharge(Boolean.valueOf(config.getConfigValue()));
                    case SysConfigConstants.POINT_RECHARGE_DESC ->
                            userConfig.setPointRechargeDesc(config.getConfigValue());
                    case SysConfigConstants.USER_STORAGE_LIMIT ->
                            userConfig.setUserStorageLimit(Integer.valueOf(config.getConfigValue()));
                    case SysConfigConstants.MEMBER_STORAGE_LIMIT ->
                            userConfig.setMemberStorageLimit(Integer.valueOf(config.getConfigValue()));
                    case SysConfigConstants.AD_REDEEM_POINT ->
                            userConfig.setAdRedeemPoint(Integer.valueOf(config.getConfigValue()));
                    case SysConfigConstants.POINT_REDEMPTION_RATIO ->
                            userConfig.setPointRedemptionRatio(Integer.valueOf(config.getConfigValue()));
                    case SysConfigConstants.IS_ENABLE_SELF_PURCHASED_REBATE ->
                            userConfig.setIsEnableSelfPurchasedRebate(Boolean.valueOf(config.getConfigValue()));
                    case SysConfigConstants.IS_ENABLE_BUY_PAID_MEMBER_REBATE ->
                            userConfig.setIsEnableBuyPaidMemberRebate(Boolean.valueOf(config.getConfigValue()));
                    case SysConfigConstants.IS_ENABLE_PROMOTE_USER_REBATE ->
                            userConfig.setIsEnablePromoteUserRebate(Boolean.valueOf(config.getConfigValue()));
                    case SysConfigConstants.PROMOTE_USER_AMOUNT ->
                            userConfig.setPromoteUserAmount(config.getConfigValue());
                    case SysConfigConstants.IS_ENABLE_DOWNLOAD_WORK_REBATE ->
                            userConfig.setIsEnableDownloadWorkRebate(Boolean.valueOf(config.getConfigValue()));
                    case SysConfigConstants.DOWNLOAD_WORK_AMOUNT ->
                            userConfig.setDownloadWorkAmount(config.getConfigValue());
                    case SysConfigConstants.FIRST_REBATE_RATIO ->
                            userConfig.setFirstRebateRatio(Integer.valueOf(config.getConfigValue()));
                    case SysConfigConstants.SECOND_REBATE_RATIO ->
                            userConfig.setSecondRebateRatio(Integer.valueOf(config.getConfigValue()));
                    case SysConfigConstants.FREEZE_TIME ->
                            userConfig.setFreezeTime(Integer.valueOf(config.getConfigValue()));
                    case SysConfigConstants.USER_EXTRACT_TYPE -> userConfig.setUserExtractType(config.getConfigValue());
                    case SysConfigConstants.USER_EXTRACT_MIN_PRICE ->
                            userConfig.setUserExtractMinPrice(Integer.valueOf(config.getConfigValue()));
                    case SysConfigConstants.MEMBER_EXTRACT_MIN_PRICE ->
                            userConfig.setMemberExtractMinPrice(Integer.valueOf(config.getConfigValue()));
                }
            }
        });
    }
}





