package com.auiucloud.component.sd.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SdProgressVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1523166203124443153L;

    private String drawIds;
    private String taskId;
    private double progress;
    private double eta_relative;
    private State state;
    private String current_image;
    private String textinfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class State {
        private boolean skipped;
        private boolean interrupted;
        private String job;
        private int job_count;
        private String job_timestamp;
        private int job_no;
        private int sampling_step;
        private int sampling_steps;
    }
}
