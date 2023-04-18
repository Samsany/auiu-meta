package com.auiucloud.ums.service;

import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.domain.UserGroup;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dries
* @description 针对表【ums_user_group(用户分组表)】的数据库操作Service
* @createDate 2023-04-03 22:21:29
*/
public interface IUserGroupService extends IService<UserGroup> {

    PageUtils listPage(Search search, UserGroup userGroup);

    boolean removeUserGroupById(Long id);

    boolean saveUserGroup(UserGroup userGroup);

    boolean updateUserGroupById(UserGroup userGroup);

    boolean checkUserGroupNameExist(UserGroup userGroup);
}
