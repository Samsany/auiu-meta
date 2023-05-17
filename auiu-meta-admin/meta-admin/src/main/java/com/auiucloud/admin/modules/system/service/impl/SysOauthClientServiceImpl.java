package com.auiucloud.admin.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.admin.modules.system.domain.SysOauthClient;
import com.auiucloud.admin.modules.system.dto.SysOauthClientDTO;
import com.auiucloud.admin.modules.system.mapper.SysOauthClientMapper;
import com.auiucloud.admin.modules.system.service.ISysOauthClientService;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.common.utils.StringPool;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.core.redis.core.RedisService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【sys_oauth_client(客户端表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:25:50
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysOauthClientServiceImpl extends ServiceImpl<SysOauthClientMapper, SysOauthClient>
        implements ISysOauthClientService {

    private final RedisService redisService;

    @NotNull
    private static List<SysOauthClientDTO> convertSysOauthClientDTOList(List<SysOauthClient> oauthClients) {
        return oauthClients.parallelStream().map(it -> {
            SysOauthClientDTO sysOauthClientDTO = new SysOauthClientDTO();
            BeanUtils.copyProperties(it, sysOauthClientDTO);
            String authorizedGrantTypes = it.getAuthorizedGrantTypes();
            if (StringUtil.isNotBlank(authorizedGrantTypes)) {
                sysOauthClientDTO.setAuthorizedGrantTypes(Arrays.asList(authorizedGrantTypes.split(StringPool.COMMA)));
            }
            return sysOauthClientDTO;
        }).collect(Collectors.toList());
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
        IPage<SysOauthClient> page = this.page(PageUtils.getPage(search), queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        List<SysOauthClient> records = Optional.ofNullable(page.getRecords()).orElse(new ArrayList<>());
        // 数据转换
        List<SysOauthClientDTO> oauthClientDTOS = convertSysOauthClientDTOList(records);
        pageUtils.setList(oauthClientDTOS);
        return pageUtils;
    }

    /**
     * 查询客户端列表
     *
     * @param oauthClient 查询参数
     * @return List<SysOauthClient>
     */
    @Override
    public List<SysOauthClientDTO> selectOauthClientList(SysOauthClient oauthClient) {
        List<SysOauthClient> oauthClients = Optional.ofNullable(this.list(buildSearchParams(oauthClient))).orElse(new ArrayList<>());
        return convertSysOauthClientDTOList(oauthClients);
    }

    /**
     * 新增客户端
     *
     * @param oauthClient 参数
     * @return boolean
     */
    @Transactional
    @Override
    public boolean saveSysOauthClient(SysOauthClientDTO oauthClient) {
        SysOauthClient sysOauthClient = new SysOauthClient();
        BeanUtils.copyProperties(oauthClient, sysOauthClient);
        // 设置授权类型
        List<String> authorizedGrantTypes = oauthClient.getAuthorizedGrantTypes();
        String authorizedGrantTypesStr = String.join(StringPool.COMMA, authorizedGrantTypes);
        sysOauthClient.setAuthorizedGrantTypes(authorizedGrantTypesStr);
        return this.save(sysOauthClient);
    }

    /**
     * 编辑客户端
     *
     * @param oauthClient 参数
     * @return boolean
     */
    @Transactional
    @Override
    public boolean editSysOauthClient(SysOauthClientDTO oauthClient) {
        SysOauthClient sysOauthClient = new SysOauthClient();
        BeanUtils.copyProperties(oauthClient, sysOauthClient);
        // 设置授权类型
        List<String> authorizedGrantTypes = oauthClient.getAuthorizedGrantTypes();
        String authorizedGrantTypesStr = String.join(StringPool.COMMA, authorizedGrantTypes);
        sysOauthClient.setAuthorizedGrantTypes(authorizedGrantTypesStr);
        boolean result = this.updateById(sysOauthClient);
        if (result) {
            redisService.del(RedisKeyConstant.cacheClientKey(oauthClient.getClientId()));
        }
        return this.updateById(sysOauthClient);
    }

    /**
     * 根据ID查询客户端详情
     *
     * @param id 客户端ID
     * @return SysOauthClientDTO
     */
    @Override
    public SysOauthClientDTO getSysOauthClientInfoById(Long id) {
        SysOauthClient oauthClient = this.getById(id);
        SysOauthClientDTO oauthClientDTO = new SysOauthClientDTO();
        BeanUtils.copyProperties(oauthClient, oauthClientDTO);
        // 数据转化
        String authorizedGrantTypes = oauthClient.getAuthorizedGrantTypes();
        if (StringUtil.isNotBlank(authorizedGrantTypes)) {
            oauthClientDTO.setAuthorizedGrantTypes(Arrays.asList(authorizedGrantTypes.split(StringPool.COMMA)));
        }
        return oauthClientDTO;
    }

    @Transactional
    @Override
    public boolean setOauthClientStatus(UpdateStatusDTO statusDTO) {
        LambdaUpdateWrapper<SysOauthClient> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysOauthClient::getStatus, statusDTO.getStatus());
        updateWrapper.eq(SysOauthClient::getId, statusDTO.getId());

        boolean result = update(updateWrapper);
        if (result) {
            try {
                SysOauthClient oauthClient = this.getById(statusDTO.getId());
                // 清除缓存记录
                redisService.del(RedisKeyConstant.cacheClientKey(oauthClient.getClientId()));
            } catch (Exception e) {
                log.error("更新客户端状态清除缓存异常: {}", e.getMessage());
            }
        }

        return result;
    }

    @Override
    public boolean removeOauthClientByIds(List<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            LambdaQueryWrapper<SysOauthClient> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(SysOauthClient::getId, ids);
            List<SysOauthClient> list = this.list(queryWrapper);
            list.parallelStream().forEach(it -> {
                // 清除缓存记录
                redisService.del(RedisKeyConstant.cacheClientKey(it.getClientId()));
            });
            return this.removeByIds(ids);
        }

        return true;
    }
}




