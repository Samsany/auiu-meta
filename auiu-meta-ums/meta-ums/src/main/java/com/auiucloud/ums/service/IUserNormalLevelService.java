package com.auiucloud.ums.service;

import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.domain.UserNormalLevel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dries
* @description 针对表【ums_user_normal_level(普通会员等级表)】的数据库操作Service
* @createDate 2023-04-03 22:22:38
*/
public interface IUserNormalLevelService extends IService<UserNormalLevel> {

    PageUtils listPage(Search search, UserNormalLevel level);

    boolean removeUserNormalLevelById(Long id);

    boolean checkLevelNameExist(UserNormalLevel level);
    boolean checkLevelGradeExist(UserNormalLevel level);
    boolean checkLevelExperienceExist(UserNormalLevel level);
}
