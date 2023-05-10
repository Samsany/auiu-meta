package com.auiucloud.admin.modules.system.service;

import com.auiucloud.admin.modules.system.domain.SysUser;
import com.auiucloud.admin.modules.system.dto.SysUserDTO;
import com.auiucloud.core.common.model.dto.UpdatePasswordDTO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.admin.modules.system.vo.SysUserVO;
import com.auiucloud.admin.modules.system.vo.UserInfoVO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_user(系统用户表)】的数据库操作Service
 * @createDate 2022-04-08 14:58:39
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 查询系统用户列表
     *
     * @param search 搜索参数
     * @param sysUser 系统用户
     * @return List<SysUser>
     */
    List<SysUser> selectSysUserList(Search search, SysUser sysUser);

    /**
     * 查询系统用户分页列表
     *
     * @param search 搜索参数
     * @param sysUser 系统用户
     * @return PageUtils
     */
    PageUtils listPage(Search search, SysUser sysUser);

    SysUserVO getSysUserInfoById(Long id);

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return SysUser
     */
    SysUser getUserByUsername(String username);
    /**
     * 根据用户名查找用户
     *
     * @param account 用户账户
     * @return SysUserInfo
     */
    UserInfoVO getUserInfoByUsername(String account);

    /**
     * 第三方用户查询
     * @param openId 用户标识
     * @param source 所属平台
     * @return
     */
    UserInfoVO getSysUserByOpenId2Source(String openId, String source);

    /**
     * 根据部门ID查找用户
     *
     * @param deptId 部门ID
     * @return List<SysUser>
     */
    List<SysUser> selectSysUserByDeptIdList(Long deptId);

    /**
     * 根据用户ID查找用户角色编码
     *
     * @param userId 用户ID
     * @return List<String>
     */
    List<String> getRoleCodeListByUserId(Long userId);

    /**
     * 新增用户
     *
     * @param sysUser 用户信息
     * @return boolean
     */
    boolean saveSysUserVO(SysUserDTO sysUser);

    /**
     * 修改用户
     *
     * @param sysUser 用户信息
     * @return boolean
     */
    boolean updateSysUserVOById(SysUserDTO sysUser);

    /**
     * 根据用户ID修改用户状态
     *
     * @param updateStatusDTO 参数
     * @return boolean
     */
    boolean setUserStatus(UpdateStatusDTO updateStatusDTO);
    /**
     * 根据用户ID修改用户密码
     *
     * @param updatePasswordDTO 参数
     * @return boolean
     */
    boolean setNewPassword(UpdatePasswordDTO updatePasswordDTO);

    /**
     * 批量删除系统用户
     *
     * @param ids 用户ID
     * @return String
     */
    String deleteSysUserByIds(List<Long> ids);

    /**
     * 校验部门是否分配用户
     *
     * @param deptId 部门ID
     * @return boolean
     */
    boolean checkDeptExistUser(Long deptId);

    /**
     * 校验用户账户是否重复
     *
     * @param sysUser 用户信息
     * @return boolean
     */
    boolean checkUsernameExist(SysUser sysUser);
    /**
     * 校验用户手机号是否重复
     *
     * @param sysUser 用户信息
     * @return boolean
     */
    boolean checkUserMobileExist(SysUser sysUser);
    /**
     * 校验用户邮箱是否重复
     *
     * @param sysUser 用户信息
     * @return boolean
     */
    boolean checkUserEmailExist(SysUser sysUser);

}
