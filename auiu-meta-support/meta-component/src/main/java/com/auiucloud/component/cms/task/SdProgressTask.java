package com.auiucloud.component.cms.task;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.cms.props.SdConstants;
import com.auiucloud.component.cms.vo.SdProgressVO;
import com.auiucloud.component.websocket.utils.WebSocketUtil;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.enums.MessageEnums;
import com.auiucloud.core.common.model.WsMsgModel;
import com.auiucloud.core.web.utils.RestTemplateUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;

/**
 * @author dries
 **/
@Data
@Component
public class SdProgressTask {

    private boolean firstExec = true;
    private String parameter;
    private TaskCallback callback;

    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;

    @Autowired
    public SdProgressTask(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    // @Scheduled(fixedDelay = 2000)
    @SneakyThrows
    public void runTask() {
        scheduledTask = taskScheduler.schedule(() -> {
            if (StrUtil.isNotBlank(parameter)) {
                if (firstExec) {
                    firstExec = false;
                    // 定时任务的逻辑
                    SdProgressVO progress = RestTemplateUtil.get("http://43.155.170.143:7893" + SdConstants.progress + "?skip_current_image=false", SdProgressVO.class).getBody();
                    if (ObjectUtil.isNotNull(progress)) {
                        System.out.println("作图进度条" + progress.getProgress());
                        if (progress.getProgress() == CommonConstant.STATUS_DISABLE_VALUE) {
                            // if (callback != null) {
                            //     callback.onTaskStopped("Task stopped.");
                            // }
                            WebSocketUtil.sendMessage(WsMsgModel.builder()
                                    .code(MessageEnums.WsMessageTypeEnum.SD_PROGRESS.getValue())
                                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                                    .to(parameter)
                                    .content(CommonConstant.STATUS_DISABLE_VALUE)
                                    .build());
                            stopTask();
                        } else {
                            // 发送进度
                            WebSocketUtil.sendMessage(WsMsgModel.builder()
                                    .code(MessageEnums.WsMessageTypeEnum.SD_PROGRESS.getValue())
                                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                                    .to(parameter)
                                    .content(progress.getProgress())
                                    .build());
                            // 执行您的代码逻辑，并在回调中返回结果
                            // System.out.println("Task is running with parameter: " + parameter);
                            // if (callback != null) {
                            //     callback.onTaskResult("Task is running with parameter: " + parameter);
                            // }
                        }
                    } else {
                        stopTask();
                    }
                } else {
                    // 定时任务的逻辑
                    SdProgressVO progress = RestTemplateUtil.get("http://43.155.170.143:7893" + SdConstants.progress + "?skip_current_image=false", SdProgressVO.class).getBody();
                    if (ObjectUtil.isNotNull(progress)) {
                        System.out.println("作图进度条" + progress.getProgress());
                        if (progress.getProgress() == CommonConstant.STATUS_NORMAL_VALUE || progress.getProgress() == CommonConstant.STATUS_DISABLE_VALUE) {
                            WebSocketUtil.sendMessage(WsMsgModel.builder()
                                    .code(MessageEnums.WsMessageTypeEnum.SD_PROGRESS.getValue())
                                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                                    .to(parameter)
                                    .content(CommonConstant.STATUS_DISABLE_VALUE)
                                    .build());
                            stopTask();
                        } else {
                            // 发送进度
                            WebSocketUtil.sendMessage(WsMsgModel.builder()
                                    .code(MessageEnums.WsMessageTypeEnum.SD_PROGRESS.getValue())
                                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                                    .to(parameter)
                                    .content(progress.getProgress())
                                    .build());
                            // 执行您的代码逻辑，并在回调中返回结果
                            // System.out.println("Task is running with parameter: " + parameter);
                        }
                    } else {
                        stopTask();
                    }
                }
            } else {
                stopTask();
            }
        }, new CronTrigger("0/2 * * * * *")); // 每1秒执行一次
    }

    public void stopTask() {
        // 停止任务的逻辑
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            firstExec = true;
            parameter = null;
            System.out.println("Task stopped.");
        }
    }


    public interface TaskCallback {
        void onTaskResult(String result);

        void onTaskStopped(String message);
    }

}
