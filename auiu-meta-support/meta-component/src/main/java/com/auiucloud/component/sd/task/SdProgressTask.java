package com.auiucloud.component.sd.task;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.sd.component.AiDrawFactory;
import com.auiucloud.component.sd.config.AiDrawConfiguration;
import com.auiucloud.component.sd.vo.SdProgressVO;
import com.auiucloud.component.websocket.utils.WebSocketUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.MetaConstant;
import com.auiucloud.core.common.enums.MessageEnums;
import com.auiucloud.core.common.model.WsMsgModel;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dries
 **/
@Data
@Component
public class SdProgressTask {

    // private boolean firstExec = true;
    private String userId;
    private String taskId;
    private String drawIds;
    private Integer errNum = 0;
    private TaskCallback callback;
    private AtomicInteger errCount = new AtomicInteger(0);
    private AtomicInteger progressValue = new AtomicInteger(0);

    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;

    @Autowired
    public SdProgressTask(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    // @Scheduled(fixedDelay = 2000)
    @SneakyThrows
    public void runTask() {
        AiDrawFactory aiDrawFactory = AiDrawConfiguration.getAiDrawFactory(MetaConstant.SD_DRAW_DEFAULT_ID);
        scheduledTask = taskScheduler.schedule(() -> {
            if (StrUtil.isNotBlank(userId)) {
                // 定时任务的逻辑
                SdProgressVO progress = aiDrawFactory.getSdProgress();
                if (ObjectUtil.isNotNull(progress)) {
                    progress.setDrawIds(drawIds);
                    progress.setTaskId(taskId);
                    System.out.println("作图进度条" + progress.getProgress());
                    int i = progressValue.get();
                    if (i > 0 && (progress.getProgress() == CommonConstant.STATUS_DISABLE_VALUE || progress.getProgress() == CommonConstant.STATUS_NORMAL_VALUE)) {
                        // if (callback != null) {
                        //     callback.onTaskStopped("Task stopped.");
                        // }
                        progressValue.set(100);
                        progress.setProgress(100);
                        WebSocketUtil.sendMessage(WsMsgModel.builder()
                                .code(MessageEnums.WsMessageTypeEnum.SD_PROGRESS.getValue())
                                .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                                .to(userId)
                                .content(ApiResult.data(progress))
                                .build());
                        stopTask();
                    } else {
                        progressValue.set((int) (progress.getProgress() * 100));
                        progress.setProgress(progressValue.get());
                        // 发送进度
                        WebSocketUtil.sendMessage(WsMsgModel.builder()
                                .code(MessageEnums.WsMessageTypeEnum.SD_PROGRESS.getValue())
                                .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                                .to(userId)
                                .content(ApiResult.data(progress))
                                .build());
                    }
                } else {
                    if (errCount.get() > 5) {
                        stopTask();
                    } else {
                        errCount.incrementAndGet();
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
            userId = null;
            taskId = null;
            drawIds = null;
            errCount.set(0);
            progressValue.set(0);
            System.out.println("Task stopped.");
        }
    }


    public interface TaskCallback {
        void onTaskResult(String result);

        void onTaskStopped(String message);
    }

}
