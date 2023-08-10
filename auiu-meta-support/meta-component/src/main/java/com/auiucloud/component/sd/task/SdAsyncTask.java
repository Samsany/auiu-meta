package com.auiucloud.component.sd.task;

import com.auiucloud.component.sd.component.AiDrawFactory;
import com.auiucloud.component.sd.config.AiDrawConfiguration;
import com.auiucloud.component.sd.domain.SdDrawResult;
import com.auiucloud.component.sd.domain.SdImg2ImgParams;
import com.auiucloud.component.sd.domain.SdTxt2ImgParams;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.MetaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author dries
 **/
@Component
public class SdAsyncTask {

    private final SdProgressTask sdProgressTask;

    @Autowired
    public SdAsyncTask(SdProgressTask sdProgressTask) {
        this.sdProgressTask = sdProgressTask;
    }

    /**
     * 文生图
     *
     * @param txt2ImgParams
     * @return
     */
    @Async("taskExecutor")
    public CompletableFuture<ApiResult<SdDrawResult>> doSdText2ImgTask(SdTxt2ImgParams txt2ImgParams) {
        AiDrawFactory aiDrawFactory = AiDrawConfiguration.getAiDrawFactory(MetaConstant.SD_DRAW_DEFAULT_ID);
        return CompletableFuture.completedFuture(aiDrawFactory.sdText2Img(txt2ImgParams));
    }

    /**
     * 文生图
     *
     * @param img2ImgParams
     * @return
     */
    @Async("taskExecutor")
    public CompletableFuture<ApiResult<SdDrawResult>> doSdImg2ImgTask(SdImg2ImgParams img2ImgParams) {
        AiDrawFactory aiDrawFactory = AiDrawConfiguration.getAiDrawFactory(MetaConstant.SD_DRAW_DEFAULT_ID);
        return CompletableFuture.completedFuture(aiDrawFactory.sdImg2Img(img2ImgParams));
    }

    /**
     * 获取任务进度
     *
     * @param userId
     */
    @Async("taskExecutor")
    public void doSdProgressTask(String userId, String taskId, String drawIds) {
        // sdProgressTask.setFirstExec(true);
        sdProgressTask.setUserId(userId);
        sdProgressTask.setTaskId(taskId);
        sdProgressTask.setDrawIds(drawIds);
        sdProgressTask.runTask();
        CompletableFuture.completedFuture("Async doSdProgressTask completed.");
    }

    public void stopSdProgressTask() {
        sdProgressTask.stopTask();
    }
}
