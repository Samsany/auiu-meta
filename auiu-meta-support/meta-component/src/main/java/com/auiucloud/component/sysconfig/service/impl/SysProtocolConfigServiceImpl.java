package com.auiucloud.component.sysconfig.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sysconfig.domain.SysProtocolConfig;
import com.auiucloud.component.sysconfig.enums.SysConfigConstants;
import com.auiucloud.component.sysconfig.mapper.SysProtocolConfigMapper;
import com.auiucloud.component.sysconfig.service.ISysProtocolConfigService;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.enums.IBaseEnum;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.redis.core.RedisService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【sys_protocol_config(系统协议配置表)】的数据库操作Service实现
 * @createDate 2023-05-12 17:26:48
 */
@Service
@RequiredArgsConstructor
public class SysProtocolConfigServiceImpl extends ServiceImpl<SysProtocolConfigMapper, SysProtocolConfig>
        implements ISysProtocolConfigService {

    private final RedisService redisService;

    @Override
    public PageUtils listPage(Search search, SysProtocolConfig protocolConfig) {
        LambdaQueryWrapper<SysProtocolConfig> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SysProtocolConfig::getTitle, search.getKeyword());
        queryWrapper.eq(ObjectUtil.isNotNull(search.getStatus()), SysProtocolConfig::getStatus, search.getStatus());
        queryWrapper.orderByAsc(SysProtocolConfig::getSort);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean updateSysProtocolConfigById(SysProtocolConfig protocolConfig) {
        boolean result = this.updateById(protocolConfig);
        if (result) {
            SysProtocolConfig sysProtocolConfig = this.getById(protocolConfig.getId());
            SysConfigConstants.ProtocolTypeEnum enumByValue = IBaseEnum.getEnumByValue(sysProtocolConfig.getType(), SysConfigConstants.ProtocolTypeEnum.class);
            redisService.del(RedisKeyConstant.cacheProtocolConfigKey(enumByValue.name().toLowerCase()));
        }
        return result;
    }

    @Override
    public boolean setStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<SysProtocolConfig> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysProtocolConfig::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(SysProtocolConfig::getId, updateStatusDTO.getId());
        boolean result = this.update(wrapper);
        if (result) {
            SysProtocolConfig protocolConfig = this.getById(updateStatusDTO.getId());
            SysConfigConstants.ProtocolTypeEnum enumByValue = IBaseEnum.getEnumByValue(protocolConfig.getType(), SysConfigConstants.ProtocolTypeEnum.class);
            redisService.del(RedisKeyConstant.cacheProtocolConfigKey(enumByValue.name().toLowerCase()));
        }
        return result;
    }

    @Override
    public SysProtocolConfig getPrivacyAgreementInfo() {
        String type = SysConfigConstants.ProtocolTypeEnum.PRIVACY_AGREEMENT.name().toLowerCase();
        SysProtocolConfig sysProtocolConfig = (SysProtocolConfig) redisService.get(RedisKeyConstant.cacheProtocolConfigKey(type));
        if (ObjectUtil.isNull(sysProtocolConfig)) {
            LambdaUpdateWrapper<SysProtocolConfig> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(SysProtocolConfig::getType, SysConfigConstants.ProtocolTypeEnum.PRIVACY_AGREEMENT.getValue());
            sysProtocolConfig = this.getOne(queryWrapper);
            if (ObjectUtil.isNotNull(sysProtocolConfig)) {
                redisService.set(RedisKeyConstant.cacheProtocolConfigKey(type), sysProtocolConfig);
            }
        }

        return sysProtocolConfig;
    }

    @Override
    public SysProtocolConfig getUserAgreementInfo() {
        String type = SysConfigConstants.ProtocolTypeEnum.USER_AGREEMENT.name().toLowerCase();
        SysProtocolConfig sysProtocolConfig = (SysProtocolConfig) redisService.get(RedisKeyConstant.cacheProtocolConfigKey(type));
        if (ObjectUtil.isNull(sysProtocolConfig)) {
            LambdaUpdateWrapper<SysProtocolConfig> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(SysProtocolConfig::getType, SysConfigConstants.ProtocolTypeEnum.USER_AGREEMENT.getValue());
            sysProtocolConfig = this.getOne(queryWrapper);
            if (ObjectUtil.isNotNull(sysProtocolConfig)) {
                redisService.set(RedisKeyConstant.cacheProtocolConfigKey(type), sysProtocolConfig);
            }
        }

        return sysProtocolConfig;
    }

    @Override
    public SysProtocolConfig getPaidMembershipAgreementInfo() {
        String type = SysConfigConstants.ProtocolTypeEnum.PAID_MEMBERSHIP_AGREEMENT.name().toLowerCase();
        SysProtocolConfig sysProtocolConfig = (SysProtocolConfig) redisService.get(RedisKeyConstant.cacheProtocolConfigKey(type));
        if (ObjectUtil.isNull(sysProtocolConfig)) {
            LambdaUpdateWrapper<SysProtocolConfig> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(SysProtocolConfig::getType, SysConfigConstants.ProtocolTypeEnum.PAID_MEMBERSHIP_AGREEMENT.getValue());
            sysProtocolConfig = this.getOne(queryWrapper);
            if (ObjectUtil.isNotNull(sysProtocolConfig)) {
                redisService.set(RedisKeyConstant.cacheProtocolConfigKey(type), sysProtocolConfig);
            }
        }

        return sysProtocolConfig;
    }

    @Override
    public SysProtocolConfig getPointAgreementInfo() {
        String type = SysConfigConstants.ProtocolTypeEnum.POINT_AGREEMENT.name().toLowerCase();
        SysProtocolConfig sysProtocolConfig = (SysProtocolConfig) redisService.get(RedisKeyConstant.cacheProtocolConfigKey(type));
        if (ObjectUtil.isNull(sysProtocolConfig)) {
            LambdaUpdateWrapper<SysProtocolConfig> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(SysProtocolConfig::getType, SysConfigConstants.ProtocolTypeEnum.POINT_AGREEMENT.getValue());
            sysProtocolConfig = this.getOne(queryWrapper);
            if (ObjectUtil.isNotNull(sysProtocolConfig)) {
                redisService.set(RedisKeyConstant.cacheProtocolConfigKey(type), sysProtocolConfig);
            }
        }

        return sysProtocolConfig;
    }

    @Override
    public SysProtocolConfig getPointRechargeAgreementInfo() {
        String type = SysConfigConstants.ProtocolTypeEnum.POINT_RECHARGE_AGREEMENT.name().toLowerCase();
        SysProtocolConfig sysProtocolConfig = (SysProtocolConfig) redisService.get(RedisKeyConstant.cacheProtocolConfigKey(type));
        if (ObjectUtil.isNull(sysProtocolConfig)) {
            LambdaUpdateWrapper<SysProtocolConfig> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(SysProtocolConfig::getType, SysConfigConstants.ProtocolTypeEnum.POINT_RECHARGE_AGREEMENT.getValue());
            sysProtocolConfig = this.getOne(queryWrapper);
            if (ObjectUtil.isNotNull(sysProtocolConfig)) {
                redisService.set(RedisKeyConstant.cacheProtocolConfigKey(type), sysProtocolConfig);
            }
        }

        return sysProtocolConfig;
    }

    @Override
    public SysProtocolConfig getUserWriteOffAgreementInfo() {
        String type = SysConfigConstants.ProtocolTypeEnum.USER_WRITE_OFF_AGREEMENT.name().toLowerCase();
        SysProtocolConfig sysProtocolConfig = (SysProtocolConfig) redisService.get(RedisKeyConstant.cacheProtocolConfigKey(type));
        if (ObjectUtil.isNull(sysProtocolConfig)) {
            LambdaUpdateWrapper<SysProtocolConfig> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(SysProtocolConfig::getType, SysConfigConstants.ProtocolTypeEnum.USER_WRITE_OFF_AGREEMENT.getValue());
            sysProtocolConfig = this.getOne(queryWrapper);
            if (ObjectUtil.isNotNull(sysProtocolConfig)) {
                redisService.set(RedisKeyConstant.cacheProtocolConfigKey(type), sysProtocolConfig);
            }
        }

        return sysProtocolConfig;
    }

    @Override
    public SysProtocolConfig getFAQInfo() {
        String type = SysConfigConstants.ProtocolTypeEnum.FAQ.name().toLowerCase();
        SysProtocolConfig sysProtocolConfig = (SysProtocolConfig) redisService.get(RedisKeyConstant.cacheProtocolConfigKey(type));
        if (ObjectUtil.isNull(sysProtocolConfig)) {
            LambdaUpdateWrapper<SysProtocolConfig> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(SysProtocolConfig::getType, SysConfigConstants.ProtocolTypeEnum.FAQ.getValue());
            sysProtocolConfig = this.getOne(queryWrapper);
            if (ObjectUtil.isNotNull(sysProtocolConfig)) {
                redisService.set(RedisKeyConstant.cacheProtocolConfigKey(type), sysProtocolConfig);
            }
        }

        return sysProtocolConfig;
    }

    @Override
    public boolean checkProtocolConfigNameExist(SysProtocolConfig protocolConfig) {
        LambdaQueryWrapper<SysProtocolConfig> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.ne(ObjectUtil.isNotNull(protocolConfig.getId()), SysProtocolConfig::getId, protocolConfig.getId());
        queryWrapper.eq(SysProtocolConfig::getTitle, protocolConfig.getTitle());

        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

}




