package com.auiucloud.component.sysconfig.service;

import com.auiucloud.component.sysconfig.domain.SysProtocolConfig;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author dries
 * @description 针对表【sys_protocol_config(系统协议配置表)】的数据库操作Service
 * @createDate 2023-05-12 17:26:48
 */
public interface ISysProtocolConfigService extends IService<SysProtocolConfig> {

    PageUtils listPage(Search search, SysProtocolConfig protocolConfig);

    boolean updateSysProtocolConfigById(SysProtocolConfig protocolConfig);

    boolean setStatus(UpdateStatusDTO updateStatusDTO);

    boolean checkProtocolConfigNameExist(SysProtocolConfig protocolConfig);

    SysProtocolConfig getPrivacyAgreementInfo();

    SysProtocolConfig getUserAgreementInfo();

    SysProtocolConfig getPaidMembershipAgreementInfo();

    SysProtocolConfig getPointAgreementInfo();

    SysProtocolConfig getPointRechargeAgreementInfo();

    SysProtocolConfig getUserWriteOffAgreementInfo();

    SysProtocolConfig getFAQInfo();

}
