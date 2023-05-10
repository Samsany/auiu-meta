package com.auiucloud.admin.modules.system.mapper;

import com.auiucloud.admin.modules.system.domain.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author dries
 * @description 针对表【sys_user(系统用户表)】的数据库操作Mapper
 * @createDate 2022-04-08 14:58:39
 * @Entity com.auiucloud.admin.modules.system.domain.SysUser
 */
public interface SysUserMapper extends BaseMapper<SysUser> {


    SysUser getSysUserByOpenId2Source(@Param("openId") String openId, @Param("source") String source);
}




