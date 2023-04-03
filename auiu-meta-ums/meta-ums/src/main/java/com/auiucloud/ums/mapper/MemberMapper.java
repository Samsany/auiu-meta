package com.auiucloud.ums.mapper;

import com.auiucloud.ums.domain.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author dries
* @description 针对表【ums_member(会员表)】的数据库操作Mapper
* @createDate 2023-03-28 10:11:41
* @Entity com.auiucloud.ums.domain.Member
*/
public interface MemberMapper extends BaseMapper<Member> {

    Member getMemberByOpenId2Source(@Param("openId") String openId, @Param("source") String source);

}




