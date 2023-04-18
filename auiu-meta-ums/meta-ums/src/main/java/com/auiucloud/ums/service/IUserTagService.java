package com.auiucloud.ums.service;

import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.domain.UserTag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dries
* @description 针对表【ums_user_tag(用户标签表)】的数据库操作Service
* @createDate 2023-04-03 22:22:38
*/
public interface IUserTagService extends IService<UserTag> {

    PageUtils listPage(Search search, UserTag userTag);

    boolean removeUserTagById(Long id);

    boolean checkHasChildTag(Long tagGroupId);
}
