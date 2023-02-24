package com.auiucloud.admin.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.admin.domain.SysOauthClient;
import com.auiucloud.admin.dto.UpdateStatusDTO;
import com.auiucloud.admin.mapper.SysOauthClientMapper;
import com.auiucloud.admin.service.ISysOauthClientService;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_oauth_client(客户端表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:25:50
 */
@Service
public class SysOauthClientServiceImpl extends ServiceImpl<SysOauthClientMapper, SysOauthClient>
        implements ISysOauthClientService {

    /**
     * 分页查询客户端列表
     *
     * @param search      查询参数
     * @param oauthClient 查询参数
     * @return PageUtils
     */
    @Override
    public PageUtils listPage(Search search, SysOauthClient oauthClient) {
        LambdaQueryWrapper<SysOauthClient> queryWrapper = buildSearchParams(oauthClient);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    /**
     * 查询客户端列表
     *
     * @param oauthClient 查询参数
     * @return List<SysOauthClient>
     */
    @Override
    public List<SysOauthClient> selectOauthClientList(SysOauthClient oauthClient) {
        return this.list(buildSearchParams(oauthClient));
    }

    @NotNull
    private static LambdaQueryWrapper<SysOauthClient> buildSearchParams(SysOauthClient oauthClient) {
        LambdaQueryWrapper<SysOauthClient> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotBlank(oauthClient.getClientName())) {
            queryWrapper.like(SysOauthClient::getClientName, oauthClient.getClientName());
        }
        queryWrapper.orderByDesc(SysOauthClient::getCreateTime);
        return queryWrapper;
    }

    @Override
    public boolean setOauthClientStatus(UpdateStatusDTO statusDTO) {
        LambdaUpdateWrapper<SysOauthClient> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysOauthClient::getStatus, statusDTO.getStatus());
        updateWrapper.eq(SysOauthClient::getId, statusDTO.getId());
        return update(updateWrapper);
    }
}




