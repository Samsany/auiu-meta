package com.auiucloud.component.cms.vo;

import com.auiucloud.core.validator.group.UpdateGroup;
import com.auiucloud.core.validator.Xss;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "合集VO")
public class GalleryCollectionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3368754556576307955L;

    /**
     * 编号主键标识
     */
    @NotNull(message = "参数异常", groups = UpdateGroup.class)
    private Long id;

    /**
     * 创建人ID
     */
    private Long userId;
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
    @Xss(message = "标题不能包含脚本字符")
    @NotBlank(message = "标题不能为空")
    @Size(min = 2, max = 100, message = "标题长度在2~100个字符之间")
    private String title;

    /**
     * 封面
     */
    private List<String> covers;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 标签
     */
    private String tag;

    /**
     * 作品数量
     */
    private Integer galleryNum;

    /**
     * 作品
     */
    private List<GalleryVO> galleryList;

    /**
     * 是否置顶(0-否 1-是)
     */
    private Integer isTop;

    /**
     * 状态(0-仅自己可见 1-广场可见)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    @Xss(message = "描述不能包含脚本字符")
    @Size(min = 0, max = 150, message = "描述长度在0~150个字符之间")
    private String remark;

    /**
     * 是否选中，默认 false
     */
    @Builder.Default
    private Boolean checked = false;

    /**
     * 是否收藏，默认false
     */
    @Builder.Default
    private Boolean isFavorite = false;
    /**
     * 收藏数
     */
    private Integer favoriteNum = 0;
    /**
     * 收藏列表
     */
    private List<UserGalleryFavoriteVO> favoriteList;

    /**
     * 是否点赞，默认false
     */
    @Builder.Default
    private Boolean isLike = false;
    /**
     * 点赞数
     */
    @Builder.Default
    private Integer likeNum = 0;
    /**
     * 点赞列表
     */
    private List<UserGalleryLikeVO> likeList;
}
