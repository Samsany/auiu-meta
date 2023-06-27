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
        COLLECTION(-1L, "合集");

        private final Long value;
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum GalleryType implements IBaseEnum<Integer> {

        WALLPAPER(0, "壁纸"),
        SD_TXT2IMG(1, "SD文生图"),
        AI_WALLPAPER(2, "图生图"),
        ;

        private final Integer value;
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum GalleryStatus implements IBaseEnum<Integer> {

        FAIL(-1, "生成失败"),
        AWAIT(0, "等待生成"),
        IN_PROGRESS(1, "生成中"),
        SUCCESS(2, "已完成"),
        ;

        private final Integer value;
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum AiDrawStatus implements IBaseEnum<Integer> {

        FAIL(-1, "上传失败"),
        AWAIT(0, "待处理"),
        SUCCESS(1, "已完成"),
        VIOLATIONS(2, "图片违规"),
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
        REJECT(2, "已驳回(违规)"),
        ;

        private final Integer value;
        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum GalleryAppealStatus implements IBaseEnum<Integer> {

        CANCEL(-1, "已取消"),
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
        GALLERY_COLLECTION(1, "合集");

        private final Integer value;
        private final String label;
    }

}
