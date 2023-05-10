package com.auiucloud.component.cms.enums;

import com.auiucloud.core.common.enums.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dries
 **/
public class GalleryEnums {

    @Getter
    @AllArgsConstructor
    public enum GalleryTagType implements IBaseEnum<Long> {

        ALL(0L, "全部"),
        COLLECTION(-1L, "合集")
        ;

        private final Long value;
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum GalleryType implements IBaseEnum<Integer> {

        WALLPAPER(0, "壁纸"),
        AI_WALLPAPER(1, "AI创作")
        ;

        private final Integer value;
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum GalleryIsPublished implements IBaseEnum<Integer> {

        NO(0, "未发布"),
        YES(1, "已发布"),
        ;

        private final Integer value;
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum GalleryApprovalStatus implements IBaseEnum<Integer> {

        AWAIT(0, "待审核"),
        RESOLVE(1, "审核通过"),
        REJECT(2, "已驳回"),
        ;

        private final Integer value;
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum GalleryCollectionStatus implements IBaseEnum<Integer> {

        YOURSELF_VISIBLE(0, "自己可见"),
        SQUARE_VISIBLE(1, "广场可见"),
        ;

        private final Integer value;
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum GalleryPageType implements IBaseEnum<Integer> {

        GALLERY(0, "作品"),
        GALLERY_COLLECTION(1, "合集")
        ;

        private final Integer value;
        private final String label;
    }

}
