package com.auiucloud.component.cms.vo;

import com.auiucloud.component.cms.domain.UserGalleryLike;
import com.auiucloud.ums.domain.UserFollower;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author dries
 **/
@Data
public class GalleryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3368465545838247840L;

    /**
     * 编号主键标识
     */
    private Long id;

    /**
     * 创建人ID
     */
    private Long uId;

    /**
     * 创建人昵称
     */
    private String nickname;

    /**
     * 创建人头像
     */
    private String avatar;

    /**
     * 标题
     */
    private String title;

    /**
     * 原图
     */
    private String pic;

    /**
     * 缩略图
     */
    private String thumbUrl;

    /**
     * 画面描述
     */
    private String prompt;

    /**
     * 反面描述
     */
    private String negativePrompt;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;

    /**
     * 大小
     */
    private Long size;

    /**
     * 随机种子
     */
    private Long seed;

    /**
     * 模型主题
     */
    private String model;

    /**
     * 融合模型
     */
    private String fusionModel;

    /**
     * 采样迭代步数
     */
    private Integer steps;

    /**
     * 采样方法
     */
    private String sampler;

    /**
     * 提示词相关性
     */
    private Integer cfgScale;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 标签
     */
    private String tag;

    /**
     * 合集ID
     */
    private Long collectionId;

    /**
     * 合集名称
     */
    private String collection;

    /**
     * 是否发布(0-否 1-是)
     */
    private Integer isPublished;

    /**
     * 是否置顶(0-否 1-是)
     */
    private Integer isTop;

    /**
     * 下载积分(开通会员可设置)
     */
    private Integer downloadIntegral;

    /**
     * 下载次数
     */
    private Long downloadTimes;

    /**
     * 浏览次数
     */
    private Long views;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 审核状态(0-待审核 1-审核通过 2-已驳回)
     */
    private Integer approvalStatus;

    /**
     * 作品类型(0-壁纸 1-AI创作)
     */
    private Integer type;

    /**
     * 发布时间
     */
    private Date publishedTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否选中，默认 false
     */
    private Boolean checked = false;

    /**
     * 是否点赞，默认false
     */
    private Boolean isLike = false;

    /**
     * 点赞数
     */
    private Integer likeNum;

    /**
     * 点赞列表
     */
    private List<UserGalleryLikeVO> likeList;

    /**
     * 是否收藏，默认false
     */
    private Boolean isFavorite = false;

    /**
     * 收藏数
     */
    private Integer favoriteNum;

    /**
     * 收藏列表
     */
    private List<UserGalleryFavoriteVO> favoriteList;
}
