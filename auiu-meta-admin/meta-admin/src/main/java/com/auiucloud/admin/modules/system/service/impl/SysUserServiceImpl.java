package com.auiucloud.admin.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.admin.modules.system.domain.SysRole;
import com.auiucloud.admin.modules.system.domain.SysRoleMenu;
import com.auiucloud.admin.modules.system.domain.SysUser;
import com.auiucloud.admin.modules.system.domain.SysUserRole;
import com.auiucloud.admin.modules.system.dto.SysUserDTO;
import com.auiucloud.admin.modules.system.dto.SysUserRoleDTO;
import com.auiucloud.admin.modules.system.service.*;
import com.auiucloud.core.common.model.dto.UpdatePasswordDTO;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.admin.modules.system.mapper.SysUserMapper;
import com.auiucloud.admin.modules.system.vo.SysDeptVO;
import com.auiucloud.admin.modules.system.vo.SysRoleVO;
import com.auiucloud.admin.modules.system.vo.SysUserVO;
import com.auiucloud.admin.modules.system.vo.UserInfoVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.MetaConstant;
import com.auiucloud.core.common.constant.Oauth2Constant;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author dries
 * @description 针对表【sys_user(系统用户表)】的数据库操作Service实现
 * @createDate 2022-04-08 14:58:39
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final ISysRoleService sysRoleService;
    private final ISysUserRoleService sysUserRoleService;
    private final ISysMenuService sysMenuService;
    private final ISysRoleMenuService sysRoleMenuService;
    private final ISysDeptService sysDeptService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<SysUser> selectSysUserList(Search search, SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = buildSearchParams(search, sysUser);
        return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
    }

    @Override
    public PageUtils listPage(Search search, SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = buildSearchParams(search, sysUser);
        IPage<SysUser> page = this.page(PageUtils.getPage(search), queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        List<SysUser> records = (List<SysUser>) pageUtils.getList();
        List<SysUserVO> userVOList = Optional.ofNullable(records).orElse(new ArrayList<>())
                .stream()
                .map(this::buildSysUserInfoVO)
                .toList();
        pageUtils.setList(userVOList);
        return pageUtils;
    }

    @Override
    public SysUserVO getSysUserInfoById(Long id) {
        SysUser sysUser = this.getById(id);
        return this.buildSysUserInfoVO(sysUser);
    }

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return SysUser
     */
    @Override
    public SysUser getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, username);
        return this.getOne(queryWrapper);
    }

    /**
     * 根据用户名查找用户
     *
     * @param account 用户账户
     * @return SysUserInfo
     */
    @Override
    public UserInfoVO getUserInfoByUsername(String account) {
        SysUser sysUser = this.getUserByUsername(account);
        // 获取用户角色信息
        return this.buildUserInfoVo(sysUser);
    }

    /**
     * 第三方用户查询
     *
     * @param openId 用户标识
     * @param source 所属平台
     * @return
     */
    @Override
    public UserInfoVO getSysUserByOpenId2Source(String openId, String source) {
        SysUser sysUser = this.baseMapper.getSysUserByOpenId2Source(openId, source);
        return this.buildUserInfoVo(sysUser);
    }

    /**
     * 组装LambdaQueryWrapper查询参数
     *
     * @param search  查询参数
     * @param sysUser 查询参数
     * @return queryWrapper
     */
    private LambdaQueryWrapper<SysUser> buildSearchParams(Search search, SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotBlank(search.getStartDate())) {
            queryWrapper.between(SysUser::getCreateTime, search.getStartDate(), search.getEndDate());
        }
        if (StringUtil.isNotBlank(sysUser.getAccount())) {
            queryWrapper.like(SysUser::getAccount, sysUser.getAccount());
        }
        if (StringUtil.isNotBlank(sysUser.getRealName())) {
            queryWrapper.like(SysUser::getRealName, sysUser.getRealName());
        }
        if (StringUtil.isNotBlank(sysUser.getNickname())) {
            queryWrapper.like(SysUser::getNickname, sysUser.getNickname());
        }
        if (ObjectUtil.isNotNull(sysUser.getDeptId())) {
            queryWrapper.eq(SysUser::getDeptId, sysUser.getDeptId());
        }
        if (ObjectUtil.isNotNull(search.getStatus())) {
            queryWrapper.eq(SysUser::getStatus, search.getStatus());
        }
        queryWrapper.orderByDesc(SysUser::getCreateTime);

        return queryWrapper;
    }

    /**
     * 根据部门ID查找用户
     *
     * @param deptId 用户名
     * @return SysUser
     */
    @Override
    public List<SysUser> selectSysUserByDeptIdList(Long deptId) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getDeptId, deptId);
        return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
    }

    @Override
    public List<String> getRoleCodeListByUserId(Long userId) {
        List<SysUserRole> roleListByUserId = sysUserRoleService.getSysUserRoleListByUserId(userId);
        List<Long> roleIds = Optional.ofNullable(roleListByUserId).orElse(Collections.emptyList()).parallelStream()
                .map(SysUserRole::getRoleId)
                .toList();

        List<SysRole> sysRoles = sysRoleService.getRoleListByIds(roleIds);
        return Optional.ofNullable(sysRoles).orElse(Collections.emptyList()).parallelStream()
                .map(SysRole::getRoleCode)
                .toList();
    }

    /**
     * 新增用户
     *
     * @param sysUser 用户信息
     * @return boolean
     */
    @Transactional
    @Override
    public boolean saveSysUserVO(SysUserDTO sysUser) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(sysUser, user);
        if (this.checkUsernameExist(user)) {
            throw new ApiException("该账户已注册");
        }
        if (this.checkUserMobileExist(user)) {
            throw new ApiException("该手机号已注册");
        }
        if (this.checkUserEmailExist(user)) {
            throw new ApiException("该邮箱已注册");
        }

        user.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        boolean result = this.save(user);
        if (result) {
            // 插入角色信息
            SysUserRoleDTO userRoleDTO = SysUserRoleDTO.builder()
                    .userId(user.getId())
                    .roleIds(sysUser.getRoleIds())
                    .build();
            sysUserRoleService.batchAddSysUserRole(userRoleDTO);
        }
        return result;
    }

    /**
     * 修改用户
     *
     * @param sysUser 用户信息
     * @return boolean
     */
    @Transactional
    @Override
    public boolean updateSysUserVOById(SysUserDTO sysUser) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(sysUser, user);
        if (this.checkUsernameExist(user)) {
            throw new ApiException("该账户已注册");
        }
        if (this.checkUserMobileExist(user)) {
            throw new ApiException("该手机号已注册");
        }
        if (this.checkUserEmailExist(user)) {
            throw new ApiException("该邮箱已注册");
        }

        boolean result = this.updateById(user);
        if (result) {
            // 删除已有的角色
            sysUserRoleService.removeSysUserRoleByUserId(user.getId());
            // 插入角色信息
            SysUserRoleDTO userRoleDTO = SysUserRoleDTO.builder()
                    .userId(user.getId())
                    .roleIds(sysUser.getRoleIds())
                    .build();
            sysUserRoleService.batchAddSysUserRole(userRoleDTO);
        }
        return result;
    }

    /**
     * 根据用户ID修改用户状态
     *
     * @param updateStatusDTO 参数
     * @return boolean
     */
    @Override
    public boolean setUserStatus(UpdateStatusDTO updateStatusDTO) {
        SysUser sysUser = this.getById(updateStatusDTO.getId());
        if (sysUser.isBuiltIn()
                && !SecurityUtil.getUser().getRoles().contains(MetaConstant.SUPER_ADMIN_CODE)
                && Objects.equals(sysUser.getId(), SecurityUtil.getUserId())) {
            throw new ApiException("系统内置账户，不可修改");
        }

        LambdaUpdateWrapper<SysUser> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.set(SysUser::getStatus, updateStatusDTO.getStatus());
        queryWrapper.eq(SysUser::getId, updateStatusDTO.getId());
        return this.update(queryWrapper);
    }

    /**
     * 根据用户ID修改用户密码
     *
     * @param updatePasswordDTO 参数
     * @return boolean
     */
    @Override
    public boolean setNewPassword(UpdatePasswordDTO updatePasswordDTO) {
        LambdaUpdateWrapper<SysUser> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.set(SysUser::getPassword, passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        queryWrapper.eq(SysUser::getId, updatePasswordDTO.getId());
        return this.update(queryWrapper);
    }

    /**
     * 批量删除系统用户
     *
     * @param ids 用户ID
     * @return String
     */
    @Transactional
    @Override
    public String deleteSysUserByIds(List<Long> ids) {
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        if (CollUtil.isNotEmpty(ids)) {
            List<SysUser> userList = this.listByIds(ids);
            for (SysUser sysUser : userList) {
                if (sysUser.isBuiltIn()) {
                    failureNum++;
                    failureMsg.append(CommonConstant.BR)
                            .append("<div style=\"color: red\">")
                            .append("【No.")
                            .append(failureNum).append(" ")
                            .append(sysUser.getAccount())
                            .append("】为系统内置用户,不允许删除</div>");
                } else {
                    boolean result = this.removeById(sysUser.getId());
                    if (result) {
                        successNum++;
                        successMsg.append(CommonConstant.BR)
                                .append("【No.")
                                .append(successNum).append(" ")
                                .append(sysUser.getAccount())
                                .append("】用户删除成功");
                    } else {
                        failureNum++;
                        failureMsg.append(CommonConstant.BR)
                                .append("<div style=\"color: red\">")
                                .append("【No.")
                                .append(failureNum).append(" ")
                                .append("网络异常,用户【")
                                .append(sysUser.getAccount())
                                .append("】删除失败</div>");
                    }
                }
            }

            if (failureNum > 0) {
                failureMsg.insert(0, "用户删除处理成功，成功删除" + successNum + " 条数据，删除失败" + failureNum + " 条数据，删除错误内容如下：");
                return failureMsg.toString();
            } else {
                successMsg.insert(0, "删除成功！共 " + successNum + " 条，数据如下：");
                return successMsg.toString();
            }
        }

        throw new ApiException("数据异常，请重新尝试");
    }

    @Override
    public boolean checkDeptExistUser(Long deptId) {
        List<SysUser> userList = this.selectSysUserByDeptIdList(deptId);
        return CollUtil.isNotEmpty(userList);
    }

    /**
     * 校验用户账户是否重复
     *
     * @param sysUser 用户信息
     * @return boolean
     */
    @Override
    public boolean checkUsernameExist(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, sysUser.getAccount());
        queryWrapper.ne(sysUser.getId() != null, SysUser::getId, sysUser.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    /**
     * 校验用户账户是否重复
     *
     * @param sysUser 用户信息
     * @return boolean
     */
    @Override
    public boolean checkUserMobileExist(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getMobile, sysUser.getMobile());
        queryWrapper.ne(sysUser.getId() != null, SysUser::getId, sysUser.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    /**
     * 校验用户邮箱是否重复
     *
     * @param sysUser 用户信息
     * @return boolean
     */
    @Override
    public boolean checkUserEmailExist(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getEmail, sysUser.getEmail());
        queryWrapper.ne(sysUser.getId() != null, SysUser::getId, sysUser.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    /**
     * 用户信息组装
     *
     * @param sysUser 用户信息
     * @return SysUserVO
     */
    private SysUserVO buildSysUserInfoVO(SysUser sysUser) {
        if (ObjectUtil.isNotNull(sysUser)) {
            SysUserVO sysUserVO = new SysUserVO();
            BeanUtils.copyProperties(sysUser, sysUserVO);
            // 根据用户ID获取角色信息
            List<SysUserRole> roleListByUserId = sysUserRoleService.getSysUserRoleListByUserId(sysUser.getId());
            List<Long> roleIds = roleListByUserId.parallelStream()
                    .map(SysUserRole::getRoleId)
                    .toList();
            List<SysRoleVO> sysRoles = sysRoleService.getSysRoleVOListByIds(roleIds);
            sysUserVO.setRoles(sysRoles);

            // 根据用户ID获取部门信息
            SysDeptVO sysDeptVO = sysDeptService.getSysDeptVOInfoById(sysUser.getDeptId());
            sysUserVO.setDepart(sysDeptVO);
            return sysUserVO;
        }
        return null;
    }

    /**
     * 用户信息组装
     *
     * @param sysUser 用户信息
     * @return SysUserInfo
     */
    private UserInfoVO buildUserInfoVo(SysUser sysUser) {
        if (ObjectUtil.isNotNull(sysUser)) {
            // 根据用户ID获取角色信息
            List<SysUserRole> roleListByUserId = sysUserRoleService.getSysUserRoleListByUserId(sysUser.getId());
            List<Long> roleIds = roleListByUserId.parallelStream()
                    .map(SysUserRole::getRoleId)
                    .toList();
            List<SysRole> sysRoles = sysRoleService.getRoleListByIds(roleIds);
            List<String> roles = sysRoles.parallelStream()
                    .map(SysRole::getRoleCode)
                    .toList();
            // 超级管理员返回全部权限
            List<String> permissions;
            if (roles.contains(MetaConstant.SUPER_ADMIN_CODE)) {
                permissions = List.of(Oauth2Constant.ALL_PERMISSION);
            } else {
                // 根据用户角色信息获取用户权限
                // 获取用户角色信息
                List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.getRoleMenusByRoleIds(roleIds);
                List<Long> menuIds = sysRoleMenus.parallelStream()
                        .map(SysRoleMenu::getMenuId)
                        .toList();
                permissions = sysMenuService.getSysMenuPermissionById(menuIds);
            }

            // 构建用户信息
            UserInfoVO userInfo = UserInfoVO.builder()
                    .userId(sysUser.getId())
                    .username(sysUser.getAccount())
                    .roles(roles)
                    .permissions(permissions)
                    .sysUser(sysUser)
                    .tenantId("")
                    .build();
            log.debug("feign调用：userInfo:{}", userInfo);
            return userInfo;
        }
        return null;
    }

}




