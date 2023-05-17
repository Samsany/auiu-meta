package com.auiucloud.ums.mapper;

import com.auiucloud.ums.domain.Member;
import com.auiucloud.ums.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author dries
 * @description 针对表【ums_member(会员表)】的数据库操作Mapper
 * @createDate 2023-03-28 10:11:41
 * @Entity com.auiucloud.ums.domain.Member
 */
public interface MemberMapper extends BaseMapper<Member> {

    Member getMemberByOpenId2Source(@Param("openId") String openId, @Param("source") String source);

    List<UserInfoVO> selectUserRecommendList(@Param("userId") Long userId);

    List<UserInfoVO> selectUserAttentionList(@Param("userId") Long userId);

    List<UserInfoVO> selectUserFollowerList(@Param("userId") Long userId);

    @Update("update ums_user set integral = integral + #{integral} where id = #{userId}")
    int increaseUserPoint(@Param("userId") Long userId, @Param("integral") Integer integral);

    @Update("update ums_user set integral = integral - #{integral} where id = #{userId}")
    int decreaseUserPoint(@Param("userId") Long userId, @Param("integral") Integer integral);

}




