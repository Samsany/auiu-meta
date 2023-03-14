package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysOauthClient;
import com.auiucloud.admin.dto.UpdateStatusDTO;
import com.auiucloud.admin.dto.SysOauthClientDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_oauth_client(客户端表)】的数据库操作Service
 * @createDate 2022-05-31 14:25:50
 */
public interface ISysOauthClientService extends IService<SysOauthClient> {

    /**
     * 分页查询客户端
     *
     * @param search 查询参数
     * @param oauthClient 查询参数
     * @return PageUtils
     */
    PageUtils listPage(Search search, SysOauthClient oauthClient);

    /**
     * 查询客户端列表
     *
     * @param oauthClient 查询参数
     * @return List<SysOauthClient>
     */
    List<SysOauthClientDTO> selectOauthClientList(SysOauthClient oauthClient);

    /**
     * 新增客户端
     * @param oauthClient 参数
     * @return boolean
     */
    boolean saveSysOauthClient(SysOauthClientDTO oauthClient);
    /**
     * 编辑客户端
     * @param oauthClient 参数
     * @return boolean
     */
    boolean editSysOauthClient(SysOauthClientDTO oauthClient);

    /**
     * 根据ID查询客户端详情
     * @param id 客户端ID
     * @return SysOauthClientDTO
     */
    SysOauthClientDTO getSysOauthClientInfoById(Long id);

    /**
     * 设置客户端状态
     *
     * @param statusDTO 参数
     * @return boolean
     */
    boolean setOauthClientStatus(UpdateStatusDTO statusDTO);

    boolean removeOauthClientByIds(List<Long> asList);
}
