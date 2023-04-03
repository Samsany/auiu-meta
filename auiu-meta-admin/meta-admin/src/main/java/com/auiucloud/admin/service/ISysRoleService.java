package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysRole;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.admin.vo.SysRoleVO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_role(角色表)】的数据库操作Service
 * @createDate 2022-05-31 14:26:03
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 查询系统角色分页列表
     *
     * @param search 搜索参数
     * @param sysRole 系统角色
     * @return PageUtils
     */
    PageUtils listPage(Search search, SysRole sysRole);

    /**
     * 查询系统角色列表
     *
     * @param search 搜索参数
     * @param sysRole 系统角色
     * @return List<SysRole>
     */
    List<SysRole> selectSysRoleList(Search search, SysRole sysRole);

    /**
     * 根据角色ID列表查询角色信息
     *
     * @param roleIds 角色Ids
     * @return List<SysRole>
     */
    List<SysRole> getRoleListByIds(List<Long> roleIds);
    List<SysRoleVO> getSysRoleVOListByIds(List<Long> roleIds);

    /**
     * 根据角色编码列表查询角色信息
     *
     * @param roleCodeList 角色编码
     * @return List<SysRole>
     */
    List<SysRole> getRoleIdsByRoles(List<String> roleCodeList);

    /**
     * 根据角色ID获取角色详情
     *
     * @param id 角色ID
     * @return SysRoleVO
     */
    SysRoleVO getRoleInfoById(Long id);

    /**
     * 根据角色ID设置角色状态
     *
     * @param updateStatusDTO 参数
     * @return boolean
     */
    boolean setRoleStatus(UpdateStatusDTO updateStatusDTO);

    boolean saveRole(SysRoleVO sysRole);

    boolean updateRoleById(SysRoleVO sysRole);

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return boolean
     */
    boolean checkRoleNameUnique(SysRoleVO role);

    /**
     * 校验角色编码是否唯一
     *
     * @param role 角色信息
     * @return boolean
     */
    boolean checkRoleCodeUnique(SysRoleVO role);

}
