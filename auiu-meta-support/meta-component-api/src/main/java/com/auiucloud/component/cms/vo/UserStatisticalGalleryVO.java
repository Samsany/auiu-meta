package com.auiucloud.component.cms.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatisticalGalleryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 367893366012298722L;

    private Long galleryNum;

    private List<GalleryInfo> galleryList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GalleryInfo implements Serializable {

        @Serial
        private static final long serialVersionUID = 4835931390015403680L;

        private Long id;

        private String thumbnail;

    }
}
