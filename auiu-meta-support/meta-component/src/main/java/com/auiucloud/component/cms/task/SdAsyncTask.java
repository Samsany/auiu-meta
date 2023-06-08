package com.auiucloud.component.cms.task;

import com.auiucloud.component.cms.domain.SdText2ImgParam;
import com.auiucloud.component.cms.props.SdConstants;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.web.utils.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
     * @param text2ImgParam
     * @return
     */
    @Async("taskExecutor")
    public CompletableFuture<ApiResult<?>> doSdText2ImgTask(SdText2ImgParam text2ImgParam) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Object data = RestTemplateUtil.post("http://43.155.170.143:7893" + SdConstants.text2Img, headers, text2ImgParam, Object.class).getBody();
        return CompletableFuture.completedFuture(ApiResult.data(data));
    }

    /**
     * 获取任务进度
     *
     * @param userId
     */
    @Async("taskExecutor")
    public void doSdProgressTask(String userId) {
        sdProgressTask.setFirstExec(true);
        sdProgressTask.setParameter(userId);
        sdProgressTask.runTask();
        CompletableFuture.completedFuture("Async doSdProgressTask completed.");
    }

    public void stopSdProgressTask() {
        sdProgressTask.stopTask();
    }
}
