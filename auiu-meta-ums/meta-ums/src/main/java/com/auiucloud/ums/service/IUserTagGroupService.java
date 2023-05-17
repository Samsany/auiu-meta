package com.auiucloud.ums.service;

import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.domain.UserTagGroup;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author dries
 * @description 针对表【ums_user_tag_group(用户标签分类表)】的数据库操作Service
 * @createDate 2023-04-03 22:22:38
 */
public interface IUserTagGroupService extends IService<UserTagGroup> {

    PageUtils listPage(Search search, UserTagGroup userTagGroup);

    boolean removeUserTagGroupById(Long id);

}
