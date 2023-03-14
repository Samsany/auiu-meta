package com.auiucloud.component.service.impl;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.domain.SysConfig;
import com.auiucloud.component.enums.ConfigTypeEnum;
import com.auiucloud.component.mapper.SysConfigMapper;
import com.auiucloud.component.service.ISysConfigService;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.ComponentConstant;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 获取默认SYSTEM配置信息
     * 构建查询条件
     *
     * @return SysConfig对象
     */
    private LambdaQueryWrapper<SysConfig>  buildSearchParams(SysConfig config) {
        SysConfig defaultSysConfig = getSystemDefaultSysConfig();
        LambdaQueryWrapper<SysConfig> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysConfig::getParentId, defaultSysConfig.getId());
        queryWrapper.like(StrUtil.isNotBlank(config.getConfigName()), SysConfig::getConfigName, config.getConfigName());
        queryWrapper.eq(StrUtil.isNotBlank(config.getConfigKey()), SysConfig::getConfigName, config.getConfigKey());
        queryWrapper.orderByDesc(SysConfig::getSort);

        return queryWrapper;
    }

    /**
     * 获取默认主题
     *
     * @return OssProperties
     */
    @Override
    public OssProperties getOssProperties() {
        OssProperties oss = buildOssProperties();
        // 对oss部分字段进行隐藏显示，保护隐私
        oss.setSecretKey(StrUtil.hide(oss.getSecretKey(), 3, 23));
        return oss;
    }

    @Override
    public OssProperties getDefaultOssProperties() {
        OssProperties oss = (OssProperties) redisService.get(ComponentConstant.OSS_DEFAULT);
        if (oss == null) {
            oss = buildOssProperties();
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

        //如果key包括*号，则表示未有更新，则忽略更新该数据
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
        // this.getOssProperties();
        // return this.update(lsc);
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
            //更新配置文件至redis
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
        redisService.del(ComponentConstant.OSS_DEFAULT);
    }

    /**
     * 获取默认SYSTEM配置信息
     *
     * @return SysConfig对象
     */
    public SysConfig getSystemDefaultSysConfig() {
        // 获取默认SYSTEM配置信息
        LambdaQueryWrapper<SysConfig> queryWrapper = Wrappers.<SysConfig>query().lambda()
                .eq(SysConfig::getConfigCode, ConfigTypeEnum.BASE_SETTING.getValue());
        return this.getOne(queryWrapper);
    }

    /**
     * 获取默认OSS配置信息
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
     * 构造默认OssProperties配置信息
     *
     * @return OssProperties
     */
    private OssProperties buildOssProperties() {
        OssProperties oss = new OssProperties();
        //获取默认的code值
        String code = getOSSDefaultSysConfig().getConfigValue();
        //读取默认code的配置参数
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysConfig::getConfigCode, code);
        List<SysConfig> sysConfigList = this.list(queryWrapper);
        oss = listToProps(sysConfigList, oss);
        redisService.set(ComponentConstant.OSS_DEFAULT, oss);
        return oss;
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
}




