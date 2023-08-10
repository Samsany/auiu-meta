package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.auiucloud.component.cms.dto.SaveAiDrawGalleryDTO;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.service.IPicQualityService;
import com.auiucloud.component.cms.service.IPicRatioService;
import com.auiucloud.component.sd.domain.*;
import com.auiucloud.component.sd.dto.SdImg2ImgConfigDTO;
import com.auiucloud.component.sd.dto.SdImg2ImgDTO;
import com.auiucloud.component.sd.dto.SdTxt2ImgConfigDTO;
import com.auiucloud.component.sd.dto.SdTxt2ImgDTO;
import com.auiucloud.component.sd.enums.SdDrawEnums;
import com.auiucloud.component.sd.service.IAiDrawService;
import com.auiucloud.component.sd.service.ISdDrawStyleService;
import com.auiucloud.component.sd.service.ISdFusionModelService;
import com.auiucloud.component.sd.service.ISdModelService;
import com.auiucloud.component.sd.task.SdAsyncTask;
import com.auiucloud.component.sd.vo.*;
import com.auiucloud.component.translate.component.TranslateFactory;
import com.auiucloud.component.translate.component.TranslateService;
import com.auiucloud.component.translate.domain.TextTranslateParams;
import com.auiucloud.component.translate.domain.TranslateResult;
import com.auiucloud.component.translate.enums.TranslateEnums;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.MessageConstant;
import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.common.enums.WsMessageEnums;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.model.WsMsgModel;
import com.auiucloud.core.common.utils.FileUtil;
import com.auiucloud.core.common.utils.StringPool;
import com.auiucloud.core.common.utils.StringUtils;
import com.auiucloud.core.common.utils.ThumbUtil;
import com.auiucloud.core.douyin.config.AppletsConfiguration;
import com.auiucloud.core.douyin.service.DouyinAppletsService;
import com.auiucloud.core.rabbit.utils.RabbitMqUtils;
import com.auiucloud.ums.dto.UserPointChangeDTO;
import com.auiucloud.ums.enums.UserPointEnums;
import com.auiucloud.ums.feign.IMemberProvider;
import com.auiucloud.ums.vo.UserInfoVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * AI 绘画实现类
 *
 * @author dries
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AiDrawServiceImpl implements IAiDrawService {
    @Resource
    private IMemberProvider memberProvider;
    private final IPicRatioService picRatioService;
    private final IPicQualityService sdUpScaleService;
    private final ISdModelService sdModelService;
    private final SdAsyncTask sdAsyncTask;
    private final IGalleryService galleryService;
    private final ISdDrawStyleService sdDrawStyleService;
    private final ISdFusionModelService sdFusionModelService;

    private final StreamBridge streamBridge;
    private final RabbitMqUtils rabbitMqUtils;

    private final TaskExecutor taskExecutor;

    @GlobalTransactional
    @Override
    public void sdTxt2ImgHandleMessage(WebSocketSession session, WsMsgModel payload) {
        // 获取队列大小
        Integer queueMessageCount = rabbitMqUtils.getQueueMessageCount(MessageConstant.SD_TXT2IMG_MESSAGE_QUEUE);

        String userId = payload.getFrom();
        // 组装Ai绘画生图参数
        SdTxt2ImgDTO sdDrawParam = JSONUtil.toBean((String) payload.getContent(), SdTxt2ImgDTO.class);
        sdDrawParam.setUserId(Long.valueOf(userId));
        SdTxt2ImgConfigDTO sdDrawImgConfig = this.generatorSdTxt2ImgParams(sdDrawParam);
        // 保存作品
        List<Long> galleryIds = this.saveTxt2ImgRecord(JSONUtil.toJsonStr(payload.getContent()), sdDrawImgConfig);
        sdDrawImgConfig.setDrawIds(galleryIds.parallelStream()
                .map(String::valueOf)
                .collect(Collectors.joining(StringPool.COMMA))
        );
        // 发送AI绘画任务消息
        streamBridge.send(MessageConstant.SD_TXT2IMG_MESSAGE_OUTPUT, WsMsgModel.builder()
                .code(WsMessageEnums.TypeEnum.SD_TXT2IMG_QUEUE.getValue())
                .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                .to(userId)
                .content(sdDrawImgConfig)
                .build());

        log.debug("当前队列数量: {}", queueMessageCount);
        streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                .code(WsMessageEnums.TypeEnum.SD_TXT2IMG_QUEUE.getValue())
                .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                .to(userId)
                .content(ApiResult.data(SdWaitQueueVO.builder()
                        .taskId(sdDrawImgConfig.getTaskId())
                        .drawIds(sdDrawImgConfig.getDrawIds())
                        .queueMessageCount(queueMessageCount + 1)
                        .changeType(SdDrawEnums.QueueChangeType.INCREASE.getValue())
                        .build()))
                .build());
    }

    @GlobalTransactional
    @Override
    public void sdImg2ImgHandleMessage(WebSocketSession session, WsMsgModel payload) {
        // 获取队列大小
        Integer queueMessageCount = rabbitMqUtils.getQueueMessageCount(MessageConstant.SD_TXT2IMG_MESSAGE_QUEUE);

        String userId = payload.getFrom();
        // 组装Ai绘画生图参数
        SdImg2ImgDTO sdDrawParam = JSONUtil.toBean((String) payload.getContent(), SdImg2ImgDTO.class);
        sdDrawParam.setUserId(Long.valueOf(userId));
        SdImg2ImgConfigDTO sdDrawImgConfig = this.generatorSdImg2ImgParams(sdDrawParam);
        // 保存作品
        List<Long> galleryIds = this.saveImg2ImgRecord(JSONUtil.toJsonStr(payload.getContent()), sdDrawImgConfig);
        sdDrawImgConfig.setDrawIds(galleryIds.parallelStream()
                .map(String::valueOf)
                .collect(Collectors.joining(StringPool.COMMA))
        );
        // 发送AI绘画任务消息
        streamBridge.send(MessageConstant.SD_TXT2IMG_MESSAGE_OUTPUT, WsMsgModel.builder()
                .code(WsMessageEnums.TypeEnum.SD_IMG2IMG.getValue())
                .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                .to(userId)
                .content(sdDrawImgConfig)
                .build());

        log.debug("当前队列数量: {}", queueMessageCount);
        streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                .code(WsMessageEnums.TypeEnum.SD_TXT2IMG_QUEUE.getValue())
                .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                .to(userId)
                .content(ApiResult.data(SdWaitQueueVO.builder()
                        .taskId(sdDrawImgConfig.getTaskId())
                        .drawIds(sdDrawImgConfig.getDrawIds())
                        .queueMessageCount(queueMessageCount + 1)
                        .changeType(SdDrawEnums.QueueChangeType.INCREASE.getValue())
                        .build()))
                .build());
    }

    @GlobalTransactional(timeoutMills = 60 * 60 * 1000)
    @Override
    public void sdText2Img(SdTxt2ImgConfigDTO txt2ImgConfig) {

        String taskId = txt2ImgConfig.getTaskId();
        String drawIds = txt2ImgConfig.getDrawIds();

        // 通知当前用户当前队列数量
        streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                .code(WsMessageEnums.TypeEnum.SD_TXT2IMG_QUEUE.getValue())
                .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                .to(String.valueOf(txt2ImgConfig.getUserId()))
                .content(ApiResult.data(SdWaitQueueVO.builder()
                        .taskId(taskId)
                        .queueMessageCount(CommonConstant.STATUS_NORMAL_VALUE)
                        .changeType(SdDrawEnums.QueueChangeType.DECREASE.getValue())
                        .build()))
                .build());

        try {
            // 更新文生图状态
            CompletableFuture.runAsync(() -> this.updateDrawStatusByIds(drawIds, GalleryEnums.GalleryStatus.IN_PROGRESS.getValue()), taskExecutor);

            SdTxt2ImgParams params = new SdTxt2ImgParams();
            BeanUtils.copyProperties(txt2ImgConfig, params);
            // 构建关键词
            this.buildTxt2ImgPrompt(txt2ImgConfig, params);
            CompletableFuture<ApiResult<SdDrawResult>> future = sdAsyncTask.doSdText2ImgTask(params);
            // 获取制作进度
            String userIdStr = String.valueOf(txt2ImgConfig.getUserId());
            sdAsyncTask.doSdProgressTask(userIdStr, txt2ImgConfig.getTaskId(), txt2ImgConfig.getDrawIds());
            // 等待全部任务完成
            CompletableFuture.allOf(future).join();
            ApiResult<SdDrawResult> apiResult = future.get();
            // 在这里可以处理全部异步任务完成后的结果 更新作图记录
            if (apiResult.successful()) {
                // 完成任务
                SdDrawResult sdDrawResult = apiResult.getData();
                // 图片处理
                List<AiDrawInfo> aiDrawImgList = this.updateAiDrawResultByIds(txt2ImgConfig, sdDrawResult);
                long errCount = aiDrawImgList.parallelStream()
                        .filter(it -> it.getStatus().equals(GalleryEnums.GalleryStatus.FAIL.getValue()))
                        .count();
                if (errCount > CommonConstant.ROOT_NODE_ID) {
                    // 退回用户部分积分
                    memberProvider.assignUserPoint(UserPointChangeDTO.builder()
                            .userId(txt2ImgConfig.getUserId())
                            .integral((int) (txt2ImgConfig.getConsumeIntegral() / errCount))
                            .changeType(UserPointEnums.ChangeTypeEnum.INCREASE.getValue())
                            .title(UserPointEnums.ConsumptionEnum.AI_TEXT2IMG_FAIL.getLabel())
                            .status(UserPointEnums.StatusEnum.SUCCESS.getValue())
                            .build());
                }
                // 通知结果
                streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                        .code(WsMessageEnums.TypeEnum.SD_TXT2IMG.getValue())
                        .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                        .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                        .to(String.valueOf(txt2ImgConfig.getUserId()))
                        .content(ApiResult.data(AiDrawResult.builder()
                                .taskId(taskId)
                                .drawIds(drawIds)
                                .aiDrawList(aiDrawImgList)
                                .build()))
                        .build());
            } else {
                log.error("[SD文生图异常，失败信息:{}, {}]", apiResult.getCode(), apiResult.getMessage());
                throw new ApiException(apiResult.getMessage());
            }
        } catch (Exception e) {
            log.error("[SD文生图异常，失败信息:{}]", e.getMessage());
            this.updateDrawStatusByIds(drawIds, GalleryEnums.GalleryStatus.FAIL.getValue());
            // 退还用户积分
            memberProvider.assignUserPoint(UserPointChangeDTO.builder()
                    .userId(txt2ImgConfig.getUserId())
                    .integral(txt2ImgConfig.getConsumeIntegral())
                    .changeType(UserPointEnums.ChangeTypeEnum.INCREASE.getValue())
                    .title(UserPointEnums.ConsumptionEnum.AI_TEXT2IMG_FAIL.getLabel())
                    .status(UserPointEnums.StatusEnum.SUCCESS.getValue())
                    .build());

            ApiResult<Object> apiResult = ApiResult.fail(e.getMessage());
            apiResult.setData(JSONUtil.createObj().set("taskId", taskId));
            streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                    .code(WsMessageEnums.TypeEnum.SD_TXT2IMG.getValue())
                    .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                    .to(String.valueOf(txt2ImgConfig.getUserId()))
                    .content(apiResult)
                    .build());
        } finally {
            // 停止获取进度任务
            sdAsyncTask.stopSdProgressTask();
            // 通知所有用户当前队列数量 -1
            streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                    .code(WsMessageEnums.TypeEnum.SD_TXT2IMG_QUEUE.getValue())
                    .sendType(WsMessageEnums.SendTypeEnum.ALL.getValue())
                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                    .to(String.valueOf(txt2ImgConfig.getUserId()))
                    .content(ApiResult.data(SdWaitQueueVO.builder()
                            .queueMessageCount(CommonConstant.STATUS_DISABLE_VALUE)
                            .changeType(SdDrawEnums.QueueChangeType.DECREASE.getValue())
                            .build()))
                    .build());
        }
    }

    @GlobalTransactional(timeoutMills = 60 * 60 * 1000)
    @Override
    public void sdImg2Img(SdImg2ImgConfigDTO img2ImgConfig) {
        String taskId = img2ImgConfig.getTaskId();
        String drawIds = img2ImgConfig.getDrawIds();

        // 通知当前用户当前队列数量
        streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                .code(WsMessageEnums.TypeEnum.SD_TXT2IMG_QUEUE.getValue())
                .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                .to(String.valueOf(img2ImgConfig.getUserId()))
                .content(ApiResult.data(SdWaitQueueVO.builder()
                        .taskId(taskId)
                        .queueMessageCount(CommonConstant.STATUS_NORMAL_VALUE)
                        .changeType(SdDrawEnums.QueueChangeType.DECREASE.getValue())
                        .build()))
                .build());

        try {
            // 更新文生图状态
            CompletableFuture.runAsync(() -> this.updateDrawStatusByIds(drawIds, GalleryEnums.GalleryStatus.IN_PROGRESS.getValue()), taskExecutor);

            SdImg2ImgParams params = new SdImg2ImgParams();
            BeanUtils.copyProperties(img2ImgConfig, params);
            // 构建关键词
            this.buildImg2ImgPrompt(img2ImgConfig, params);
            CompletableFuture<ApiResult<SdDrawResult>> future = sdAsyncTask.doSdImg2ImgTask(params);
            // 获取制作进度
            String userIdStr = String.valueOf(img2ImgConfig.getUserId());
            sdAsyncTask.doSdProgressTask(userIdStr, img2ImgConfig.getTaskId(), img2ImgConfig.getDrawIds());
            // 等待全部任务完成
            CompletableFuture.allOf(future).join();
            ApiResult<SdDrawResult> apiResult = future.get();
            // 在这里可以处理全部异步任务完成后的结果 更新作图记录
            if (apiResult.successful()) {
                // 完成任务
                SdDrawResult sdDrawResult = apiResult.getData();
                // 图片处理
                List<AiDrawInfo> aiDrawImgList = this.updateAiDrawResultByIds(img2ImgConfig.getPlatform(),
                        img2ImgConfig.getAppId(),
                        drawIds,
                        taskId,
                        img2ImgConfig.getUserId(),
                        sdDrawResult
                );
                long errCount = aiDrawImgList.parallelStream()
                        .filter(it -> it.getStatus().equals(GalleryEnums.GalleryStatus.FAIL.getValue()))
                        .count();
                if (errCount > CommonConstant.ROOT_NODE_ID) {
                    // 退回用户部分积分
                    memberProvider.assignUserPoint(UserPointChangeDTO.builder()
                            .userId(img2ImgConfig.getUserId())
                            .integral((int) (img2ImgConfig.getConsumeIntegral() / errCount))
                            .changeType(UserPointEnums.ChangeTypeEnum.INCREASE.getValue())
                            .title(UserPointEnums.ConsumptionEnum.AI_IMG2IMG_FAIL.getLabel())
                            .status(UserPointEnums.StatusEnum.SUCCESS.getValue())
                            .build());
                }
                // 通知结果
                streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                        .code(WsMessageEnums.TypeEnum.SD_IMG2IMG.getValue())
                        .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                        .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                        .to(String.valueOf(img2ImgConfig.getUserId()))
                        .content(ApiResult.data(AiDrawResult.builder()
                                .taskId(taskId)
                                .drawIds(drawIds)
                                .aiDrawList(aiDrawImgList)
                                .build()))
                        .build());
            } else {
                log.error("[SD图生图异常，失败信息:{}, {}]", apiResult.getCode(), apiResult.getMessage());
                throw new ApiException(apiResult.getMessage());
            }
        } catch (Exception e) {
            log.error("[SD图生图异常，失败信息:{}]", e.getMessage());
            this.updateDrawStatusByIds(drawIds, GalleryEnums.GalleryStatus.FAIL.getValue());
            // 退还用户积分
            memberProvider.assignUserPoint(UserPointChangeDTO.builder()
                    .userId(img2ImgConfig.getUserId())
                    .integral(img2ImgConfig.getConsumeIntegral())
                    .changeType(UserPointEnums.ChangeTypeEnum.INCREASE.getValue())
                    .title(UserPointEnums.ConsumptionEnum.AI_IMG2IMG_FAIL.getLabel())
                    .status(UserPointEnums.StatusEnum.SUCCESS.getValue())
                    .build());

            ApiResult<Object> apiResult = ApiResult.fail(e.getMessage());
            apiResult.setData(JSONUtil.createObj().set("taskId", taskId));
            streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                    .code(WsMessageEnums.TypeEnum.SD_IMG2IMG.getValue())
                    .sendType(WsMessageEnums.SendTypeEnum.USER.getValue())
                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                    .to(String.valueOf(img2ImgConfig.getUserId()))
                    .content(apiResult)
                    .build());
        } finally {
            // 停止获取进度任务
            sdAsyncTask.stopSdProgressTask();
            // 通知所有用户当前队列数量 -1
            streamBridge.send(MessageConstant.NOTICE_MESSAGE_OUTPUT, WsMsgModel.builder()
                    .code(WsMessageEnums.TypeEnum.SD_TXT2IMG_QUEUE.getValue())
                    .sendType(WsMessageEnums.SendTypeEnum.ALL.getValue())
                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                    .to(String.valueOf(img2ImgConfig.getUserId()))
                    .content(ApiResult.data(SdWaitQueueVO.builder()
                            .queueMessageCount(CommonConstant.STATUS_DISABLE_VALUE)
                            .changeType(SdDrawEnums.QueueChangeType.DECREASE.getValue())
                            .build()))
                    .build());
        }
    }

    @SneakyThrows
    @Override
    public SdTxt2ImgConfigDTO generatorSdTxt2ImgParams(SdTxt2ImgDTO sdDrawParam) {

        // 文本校验
        String prompt = sdDrawParam.getPrompt();
        if (StrUtil.isBlank(prompt)) {
            throw new ApiException("请输入关键词");
        }
        String platform = sdDrawParam.getPlatform();
        String[] split = platform.split(StringPool.COLON);
        String clientId = split[0];
        String appId = split[1];
        CompletableFuture<Void> checkTextFuture = CompletableFuture.runAsync(() -> {
            boolean checkPrompt = false;
            try {
                List<Integer> resultList = checkTextContent(clientId, appId, sdDrawParam.getPrompt(), sdDrawParam.getNegativePrompt());
                if (CollUtil.isNotEmpty(resultList)) {
                    checkPrompt = true;
                }
            } catch (Exception e) {
                log.error("文生图参数关键词校验异常: {}", e.getMessage());
                // throw new ApiException(ResultCode.USER_ERROR_A0500);
            }
            if (checkPrompt) {
                throw new ApiException(ResultCode.USER_ERROR_A0431);
            }
        }, taskExecutor);


        Long userId = sdDrawParam.getUserId();
        // 用户
        CompletableFuture<UserInfoVO> getUserFuture = CompletableFuture.supplyAsync(() -> {
            UserInfoVO userInfo = memberProvider.getSimpleUserById(userId).getData();
            if (ObjectUtil.isNull(userInfo)) {
                throw new ApiException(ResultCode.USER_ERROR_A0301);
            }
            return userInfo;
        }, taskExecutor);

        // 计算消耗积分
        // 1.获取生图比例
        CompletableFuture<SdPicRatio> getPicRatioFuture = CompletableFuture.supplyAsync(() -> {
            SdPicRatio picRatio = picRatioService.getById(sdDrawParam.getPicRatioId());
            if (ObjectUtil.isNull(picRatio)) {
                throw new ApiException("绘制画面比例异常，请重试");
            }
            return picRatio;
        }, taskExecutor);


        // 2.获取图片质量
        CompletableFuture<SdPicQuality> getPicQualityFuture = CompletableFuture.supplyAsync(() -> {
            SdPicQuality picQuality = sdUpScaleService.getById(sdDrawParam.getPicQualityId());
            if (ObjectUtil.isNull(picQuality)) {
                throw new ApiException("制作异常，请重试");
            }
            return picQuality;
        }, taskExecutor);

        // 3.获取Model
        CompletableFuture<SdModel> getSdModelFuture = CompletableFuture.supplyAsync(() -> {
            SdModel model = sdModelService.getById(sdDrawParam.getModelId());
            if (ObjectUtil.isNull(model) || StrUtil.isBlank(model.getModelName()) || StrUtil.isBlank(model.getModelHash())) {
                throw new ApiException("模型解析异常，请重试");
            }
            return model;
        }, taskExecutor);

        CompletableFuture.allOf(checkTextFuture, getUserFuture, getPicRatioFuture, getPicQualityFuture, getSdModelFuture).get();
        UserInfoVO userInfo = getUserFuture.get();
        SdPicRatio picRatio = getPicRatioFuture.get();
        SdPicQuality picQuality = getPicQualityFuture.get();
        SdModel model = getSdModelFuture.get();

        Integer integral = userInfo.getIntegral();
        Integer consumeIntegral = (picRatio.getConsumeIntegral() + picQuality.getConsumeIntegral()) * sdDrawParam.getBatchSize();
        if (picQuality.getEnableHr().equals(CommonConstant.YES_VALUE)) {
            consumeIntegral = consumeIntegral * picQuality.getHrScale().intValue();
        }
        if (integral < consumeIntegral) {
            throw new ApiException(ResultCode.USER_ERROR_A0160);
        }

        // 扣减用户积分
        ApiResult<?> apiResult = memberProvider.assignUserPoint(UserPointChangeDTO.builder()
                .userId(userId)
                .integral(consumeIntegral)
                .changeType(UserPointEnums.ChangeTypeEnum.DECREASE.getValue())
                .title(UserPointEnums.ConsumptionEnum.AI_TEXT2IMG.getLabel())
                .status(UserPointEnums.StatusEnum.SUCCESS.getValue())
                .build());

        if (apiResult.successful()) {
            SdTxt2ImgConfigDTO txt2ImgConfig = new SdTxt2ImgConfigDTO();
            txt2ImgConfig.setPlatform(clientId);
            txt2ImgConfig.setAppId(appId);
            txt2ImgConfig.setUserId(userId);
            txt2ImgConfig.setSdDrawType(SdDrawEnums.DrawType.TXT2IMG.getValue());
            txt2ImgConfig.setTaskId(sdDrawParam.getTaskId());
            txt2ImgConfig.setConsumeIntegral(consumeIntegral);
            txt2ImgConfig.setStyle(sdDrawParam.getStyle());
            txt2ImgConfig.setLora(sdDrawParam.getLora());
            txt2ImgConfig.setEmbedding(sdDrawParam.getEmbedding());
            txt2ImgConfig.setRatio(picRatio.getRatio());

            // 覆盖配置 model、clip_skip、eta、ENSD
            JSONObject obj = JSONUtil.createObj();

            // 获取模型
            String filename = model.getFilename();
            if (StrUtil.isBlank(filename)) {
                throw new ApiException("模型参数异常，请联系管理员");
            }
            if (StrUtil.isNotBlank(model.getFilePath())) {
                filename = model.getFilePath() + StringPool.SLASH + filename;
            }

            obj.set("sd_model_checkpoint", filename);
            // obj.set("filter_nsfw", true);
            obj.set("CLIP_stop_at_last_layers", 2);
            // obj.set("sd_vae", "");
            txt2ImgConfig.setOverride_settings(obj);
            txt2ImgConfig.setAlwayson_scripts(JSONUtil.createObj());

            txt2ImgConfig.setPrompt(sdDrawParam.getPrompt());
            txt2ImgConfig.setNegative_prompt(sdDrawParam.getNegativePrompt());

            txt2ImgConfig.setBatch_size(sdDrawParam.getBatchSize());
            txt2ImgConfig.setWidth(picRatio.getWidth());
            txt2ImgConfig.setHeight(picRatio.getHeight());

            txt2ImgConfig.setRestore_faces(sdDrawParam.getRestoreFaces());
            txt2ImgConfig.setTiling(sdDrawParam.getTiling());
            txt2ImgConfig.setEnable_hr(sdDrawParam.getEnableHr());

            // 是否开启高清修复
            if (picQuality.getEnableHr().equals(CommonConstant.YES_VALUE)) {
                txt2ImgConfig.setEnable_hr(Boolean.TRUE);
                txt2ImgConfig.setHr_scale(picQuality.getHrScale());
                txt2ImgConfig.setHr_upscaler(picQuality.getName());
                txt2ImgConfig.setHr_second_pass_steps(picQuality.getSteps());
                txt2ImgConfig.setDenoising_strength(picQuality.getDenoisingStrength());
                txt2ImgConfig.setHr_resize_x(picQuality.getHrResizeX());
                txt2ImgConfig.setHr_resize_y(picQuality.getHrResizeY());
                // text2ImgParam.setFirstphase_width(picRatio.getWidth());
                // text2ImgParam.setFirstphase_height(picRatio.getHeight());
                txt2ImgConfig.setWidth((int) (txt2ImgConfig.getWidth() * picQuality.getHrScale()));
                txt2ImgConfig.setHeight((int) (txt2ImgConfig.getHeight() * picQuality.getHrScale()));
            }

            // todo 判断是否为会员 否则使用默认配置
            SdModelConfig sdModelConfig = JSONUtil.toBean(model.getConfig(), SdModelConfig.class);
            txt2ImgConfig.setCfg_scale(sdModelConfig.getCfgScale());
            txt2ImgConfig.setSteps(sdModelConfig.getSteps());
            txt2ImgConfig.setSampler_index(sdModelConfig.getDefaultSampler());
            txt2ImgConfig.setSampler_name(sdModelConfig.getDefaultSampler());
            txt2ImgConfig.setRestore_faces(sdModelConfig.getRestoreFaces().equals(CommonConstant.YES_VALUE));
            txt2ImgConfig.setTiling(sdModelConfig.getTiling().equals(CommonConstant.YES_VALUE));

            return txt2ImgConfig;
        }

        throw new ApiException(apiResult.getMessage());
    }

    @SneakyThrows
    @Override
    public SdImg2ImgConfigDTO generatorSdImg2ImgParams(SdImg2ImgDTO sdDrawParam) {

        // 参考图
        String initImage = sdDrawParam.getInitImages().get(0);
        // MultipartFile multipartFile = FileUtil.base64ToMultipartFile(initImage);
        byte[] bytes = ThumbUtil.compressPicForScale(initImage.getBytes(), 1024);
        String base64Img = FileUtil.byteToBase64(bytes);

        // 校验内容
        String platform = sdDrawParam.getPlatform();
        String[] split = platform.split(StringPool.COLON);
        String clientId = split[0];
        String appId = split[1];
        CompletableFuture<Void> checkImgFuture = CompletableFuture.runAsync(() -> {
            boolean checkPrompt = false;
            try {
                if (clientId.equalsIgnoreCase(AuthenticationIdentityEnum.DOUYIN_APPLET.name())) {
                    DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
                    String result = douyinAppletService.checkBase64Image(base64Img);
                    if (StrUtil.isNotBlank(result)) {
                        checkPrompt = true;
                    }
                }
            } catch (Exception e) {
                log.error("图生图参考图校验异常: {}", e.getMessage());
                // throw new ApiException(ResultCode.USER_ERROR_A0500);
            }
            if (checkPrompt) {
                throw new ApiException(ResultCode.USER_ERROR_A0432);
            }
        }, taskExecutor);

        CompletableFuture<Void> checkTextFuture = CompletableFuture.runAsync(() -> {
            boolean checkPrompt = false;
            try {
                List<Integer> resultList = checkTextContent(clientId, appId, sdDrawParam.getPrompt(), sdDrawParam.getNegativePrompt());
                if (CollUtil.isNotEmpty(resultList)) {
                    checkPrompt = true;
                }
            } catch (Exception e) {
                log.error("图生图参数关键词校验异常: {}", e.getMessage());
                // throw new ApiException(ResultCode.USER_ERROR_A0500);
            }
            if (checkPrompt) {
                throw new ApiException(ResultCode.USER_ERROR_A0431);
            }
        }, taskExecutor);

        Long userId = sdDrawParam.getUserId();
        // 用户
        CompletableFuture<UserInfoVO> getUserFuture = CompletableFuture.supplyAsync(() -> {
            UserInfoVO userInfo = memberProvider.getSimpleUserById(userId).getData();
            if (ObjectUtil.isNull(userInfo)) {
                throw new ApiException(ResultCode.USER_ERROR_A0301);
            }
            return userInfo;
        }, taskExecutor);

        // 3.获取Model
        CompletableFuture<SdModel> getSdModelFuture = CompletableFuture.supplyAsync(() -> {
            SdModel model = sdModelService.getById(sdDrawParam.getModelId());
            if (ObjectUtil.isNull(model) || StrUtil.isBlank(model.getModelName()) || StrUtil.isBlank(model.getModelHash())) {
                throw new ApiException("模型解析异常，请重试");
            }
            return model;
        }, taskExecutor);

        CompletableFuture.allOf(checkImgFuture, checkTextFuture, getUserFuture, getSdModelFuture).get();
        UserInfoVO userInfo = getUserFuture.get();
        SdModel model = getSdModelFuture.get();
        SdModelVO sdModelVO = this.covert2SdModelVO(model);

        Integer integral = userInfo.getIntegral();
        Integer consumeIntegral = sdModelVO.getConsumeIntegral();
        if (integral < consumeIntegral) {
            throw new ApiException(ResultCode.USER_ERROR_A0160);
        }

        // 扣减用户积分
        ApiResult<?> apiResult = memberProvider.assignUserPoint(UserPointChangeDTO.builder()
                .userId(userId)
                .integral(consumeIntegral)
                .changeType(UserPointEnums.ChangeTypeEnum.DECREASE.getValue())
                .title(UserPointEnums.ConsumptionEnum.AI_IMG2IMG.getLabel())
                .status(UserPointEnums.StatusEnum.SUCCESS.getValue())
                .build());

        if (apiResult.successful()) {
            SdImg2ImgConfigDTO config = new SdImg2ImgConfigDTO();
            config.setPlatform(clientId);
            config.setAppId(appId);
            config.setUserId(userId);
            config.setSdDrawType(SdDrawEnums.DrawType.TXT2IMG.getValue());
            config.setTaskId(sdDrawParam.getTaskId());
            config.setConsumeIntegral(consumeIntegral);
            config.setInit_images(List.of(base64Img));

            // 覆盖配置 model、clip_skip、eta、ENSD
            JSONObject obj = JSONUtil.createObj();

            // 获取模型
            String filename = model.getFilename();
            if (StrUtil.isBlank(filename)) {
                throw new ApiException("模型参数异常，请联系管理员");
            }
            if (StrUtil.isNotBlank(model.getFilePath())) {
                filename = model.getFilePath() + StringPool.SLASH + filename;
            }

            obj.set("sd_model_checkpoint", filename);
            // obj.set("filter_nsfw", true);
            obj.set("CLIP_stop_at_last_layers", 2);
            // obj.set("sd_vae", "");
            config.setOverride_settings(obj);
            config.setAlwayson_scripts(JSONUtil.createObj());

            config.setPrompt(sdDrawParam.getPrompt());
            config.setNegative_prompt(sdDrawParam.getNegativePrompt());

            // config.setBatch_size(sdDrawParam.getBatchSize());
            // config.setWidth(picRatio.getWidth());
            // config.setHeight(picRatio.getHeight());

            config.setRestore_faces(sdDrawParam.getRestoreFaces());
            config.setTiling(sdDrawParam.getTiling());

            // todo 判断是否为会员 否则使用默认配置
            config.setCfg_scale(sdModelVO.getCfgScale());
            config.setSteps(sdModelVO.getSteps());
            config.setSampler_index(sdModelVO.getSampler());
            config.setSampler_name(sdModelVO.getSampler());
            config.setRestore_faces(sdModelVO.getRestoreFaces());
            config.setTiling(sdModelVO.getTiling());

            return config;
        }

        throw new ApiException(apiResult.getMessage());
    }

    @Override
    public List<Long> saveTxt2ImgRecord(String config, SdTxt2ImgConfigDTO params) {
        SaveAiDrawGalleryDTO build = SaveAiDrawGalleryDTO.builder()
                .userId(params.getUserId())
                .batchSize(params.getBatch_size())
                .ratio(params.getRatio())
                .width(params.getWidth())
                .height(params.getHeight())
                .consumeIntegral(params.getConsumeIntegral())
                .sdConfig(config)
                .build();
        return galleryService.saveAiDrawGallery(build);
    }

    @Override
    public List<Long> saveImg2ImgRecord(String config, SdImg2ImgConfigDTO params) {
        SaveAiDrawGalleryDTO build = SaveAiDrawGalleryDTO.builder()
                .userId(params.getUserId())
                .batchSize(params.getBatch_size())
                .ratio("")
                .width(params.getWidth())
                .height(params.getHeight())
                .consumeIntegral(params.getConsumeIntegral())
                .sdConfig(config)
                .build();
        return galleryService.saveAiDrawGallery(build);
    }

    @Override
    public void updateDrawStatusByIds(String drawIds, Integer status) {
        if (StrUtil.isNotBlank(drawIds)) {
            List<String> ids = StrUtil.split(drawIds, StringPool.COMMA);
            galleryService.updateAiDrawGalleryStatusByIds(ids, status);
        }
    }

    @Override
    public List<AiDrawInfo> updateAiDrawResultByIds(String platform,
                                                    String appId,
                                                    String drawIds,
                                                    String taskId,
                                                    Long userId,
                                                    SdDrawResult sdDrawResult) {

        List<AiDrawInfo> aiDrawImgList = new ArrayList<>();
        List<String> images = sdDrawResult.getImages();
        if (StrUtil.isNotBlank(drawIds)) {
            List<String> ids = StrUtil.split(drawIds, StringPool.COMMA);
            for (int i = 0; i < ids.size(); i++) {
                Long id = Long.valueOf(ids.get(i));
                String base64Img = images.get(i);
                AiDrawInfo aiDrawImg = AiDrawInfo.builder()
                        .taskId(taskId)
                        .galleryId(id)
                        .url("")
                        .imageData("data:image/png;base64," + base64Img)
                        .info(sdDrawResult.getInfo())
                        .status(GalleryEnums.GalleryStatus.SUCCESS.getValue()) // 先默认成功
                        .build();
                try {
                    if (platform.equalsIgnoreCase(AuthenticationIdentityEnum.DOUYIN_APPLET.name())) {
                        DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
                        // 压缩图片
                        byte[] compressedBytes = ThumbUtil.compressPicForScale(Base64.decode(base64Img), 50);
                        String result = douyinAppletService.checkBase64Image(Base64.encode(compressedBytes));
                        if (StrUtil.isNotBlank(result)) {
                            log.error("内容安全检测: {}", result);
                            aiDrawImg.setStatus(GalleryEnums.GalleryStatus.VIOLATIONS.getValue());
                        }
                    }
                } catch (Exception e) {
                    log.error("内容安全检测异常: {}", e.getMessage());
                }
                aiDrawImgList.add(aiDrawImg);
            }
            galleryService.updateAiDrawResult(userId, aiDrawImgList);
        }
        return aiDrawImgList;
    }

    @Override
    public List<AiDrawInfo> updateAiDrawResultByIds(SdTxt2ImgConfigDTO txt2ImgConfig, SdDrawResult sdDrawResult) {

        List<AiDrawInfo> aiDrawImgList = new ArrayList<>();
        String drawIds = txt2ImgConfig.getDrawIds();
        List<String> images = sdDrawResult.getImages();
        if (StrUtil.isNotBlank(drawIds)) {
            List<String> ids = StrUtil.split(drawIds, StringPool.COMMA);
            for (int i = 0; i < ids.size(); i++) {
                Long id = Long.valueOf(ids.get(i));
                String base64Img = images.get(i);
                AiDrawInfo aiDrawImg = AiDrawInfo.builder()
                        .taskId(txt2ImgConfig.getTaskId())
                        .galleryId(id)
                        .url("")
                        .imageData("data:image/png;base64," + base64Img)
                        .info(sdDrawResult.getInfo())
                        .status(GalleryEnums.GalleryStatus.SUCCESS.getValue()) // 先默认成功
                        .build();
                try {
                    String platform = txt2ImgConfig.getPlatform();
                    String appId = txt2ImgConfig.getAppId();
                    if (platform.equalsIgnoreCase(AuthenticationIdentityEnum.DOUYIN_APPLET.name())) {
                        DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
                        // 压缩图片
                        byte[] compressedBytes = ThumbUtil.compressPicForScale(Base64.decode(base64Img), 50);
                        String result = douyinAppletService.checkBase64Image(Base64.encode(compressedBytes));
                        if (StrUtil.isNotBlank(result)) {
                            log.error("内容安全检测: {}", result);
                            aiDrawImg.setStatus(GalleryEnums.GalleryStatus.VIOLATIONS.getValue());
                        }
                    }
                } catch (Exception e) {
                    log.error("内容安全检测异常: {}", e.getMessage());
                }
                aiDrawImgList.add(aiDrawImg);
            }
            galleryService.updateAiDrawResult(txt2ImgConfig.getUserId(), aiDrawImgList);
        }
        return aiDrawImgList;
    }

    private SdModelVO covert2SdModelVO(SdModel sdModel) {
        SdModelVO sdModelVO = new SdModelVO();
        BeanUtils.copyProperties(sdModel, sdModelVO);
        String config = sdModel.getConfig();
        if (StrUtil.isNotBlank(config)) {
            SdModelConfig sdModelConfig = JSONUtil.toBean(config, SdModelConfig.class);

            // 默认风格styles
            List<Long> defaultStyles = sdModelConfig.getDefaultStyles();
            List<SdDrawStyleVO> sdDrawDefaultStyleList = sdDrawStyleService.selectSdDrawVOListByIds(defaultStyles);
            sdModelVO.setDefaultStyleList(sdDrawDefaultStyleList);

            // 默认Lora融合模型
            List<SdLoraVO> defaultLoraList = sdModelConfig.getDefaultLoraList();
            List<Long> defaultLoraIds = Optional.ofNullable(defaultLoraList).orElse(Collections.emptyList())
                    .parallelStream().map(SdLoraVO::getId).toList();
            List<SdFusionModelVO> defaultSdFusionModels = sdFusionModelService.selectSdFusionModelVOListByIds(defaultLoraIds);
            sdModelVO.setDefaultLoraList(defaultSdFusionModels);

            // 高级设置
            sdModelVO.setRestoreFaces(sdModelConfig.getRestoreFaces().equals(CommonConstant.YES_VALUE));
            // sdModelVO.setEnableHr(sdModelConfig.getEnableHr().equals(CommonConstant.YES_VALUE));
            sdModelVO.setTiling(sdModelConfig.getTiling().equals(CommonConstant.YES_VALUE));

            // 采样方法
            sdModelVO.setSampler(sdModelConfig.getDefaultSampler());
            sdModelVO.setSamplerList(sdModelConfig.getSamplerList());

            sdModelVO.setSteps(sdModelConfig.getSteps());
            sdModelVO.setCfgScale(sdModelConfig.getCfgScale());

            sdModelVO.setEmbeddings(sdModelConfig.getEmbeddings());
            sdModelVO.setDefaultEmbeddings(sdModelConfig.getDefaultEmbeddings());

            sdModelVO.setConsumeIntegral(5);
        }
        return sdModelVO;
    }

    private List<Integer> checkTextContent(String clientId, String appId, String prompt, String negativePrompt) {
        if (clientId.equalsIgnoreCase(AuthenticationIdentityEnum.DOUYIN_APPLET.name())) {
            DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
            return douyinAppletService.checkTextList(List.of(
                    prompt,
                    negativePrompt
            ));
        }

        return Collections.emptyList();
    }

    /**
     * 构建文本关键词
     *
     * @param txt2ImgConfig 参数
     * @param params        文生图参数
     */
    private void buildTxt2ImgPrompt(SdTxt2ImgConfigDTO txt2ImgConfig, SdTxt2ImgParams params) {
        // 开始组装提示词  风格 + 提示词 + embedding + lora
        StringBuilder promptBuffer = new StringBuilder();
        StringBuilder negativePromptBuffer = new StringBuilder();

        // 获取提示词
        String prompt = txt2ImgConfig.getPrompt();
        String negativePrompt = txt2ImgConfig.getNegative_prompt();
        // 获取风格
        SdTxt2ImgDTO.SdStyle style = txt2ImgConfig.getStyle();
        // 获取embedding
        SdTxt2ImgDTO.SdEmbedding embedding = txt2ImgConfig.getEmbedding();
        // 获取Lora
        String lora = txt2ImgConfig.getLora();
        try {
            TranslateService translateService = TranslateFactory.build();
            prompt = translateTextContent(translateService, prompt);
            negativePrompt = translateTextContent(translateService, negativePrompt);
        } catch (Exception e) {
            log.error("翻译提示词异常：{}", e.getMessage());
        }

        String promptStyle = style.getPrompt();
        if (StrUtil.isNotBlank(promptStyle)) {
            promptBuffer.append(promptStyle);
        }
        if (StrUtil.isNotBlank(prompt)) {
            if (promptBuffer.length() > CommonConstant.STATUS_DISABLE_VALUE) {
                promptBuffer.append(StringPool.COMMA);
            }
            promptBuffer.append(prompt);
        }
        String promptEmbedding = embedding.getPrompt();
        if (StrUtil.isNotBlank(promptEmbedding)) {
            if (promptBuffer.length() > CommonConstant.STATUS_DISABLE_VALUE) {
                promptBuffer.append(StringPool.COMMA);
            }
            promptBuffer.append(promptEmbedding);
        }
        if (StrUtil.isNotBlank(lora)) {
            if (promptBuffer.length() > CommonConstant.STATUS_DISABLE_VALUE) {
                promptBuffer.append(StringPool.COMMA);
            }
            promptBuffer.append(lora);
        }

        String negativePromptStyle = style.getNegativePrompt();
        if (StrUtil.isNotBlank(negativePromptStyle)) {
            negativePromptBuffer.append(negativePromptStyle);
        }

        if (StrUtil.isNotBlank(negativePrompt)) {
            if (negativePromptBuffer.length() > CommonConstant.STATUS_DISABLE_VALUE) {
                negativePromptBuffer.append(StringPool.COMMA);
            }
            negativePromptBuffer.append(negativePrompt);
        }

        String negativePromptEmbedding = embedding.getNegativePrompt();
        if (StrUtil.isNotBlank(negativePromptEmbedding)) {
            if (negativePromptBuffer.length() > CommonConstant.STATUS_DISABLE_VALUE) {
                negativePromptBuffer.append(StringPool.COMMA);
            }
            negativePromptBuffer.append(negativePromptEmbedding);
        }

        params.setPrompt(promptBuffer.toString());
        params.setNegative_prompt(negativePromptBuffer.toString());
    }

    /**
     * 构建图生图关键词
     *
     * @param img2ImgConfig 参数
     * @param params        图生图参数
     */
    private void buildImg2ImgPrompt(SdImg2ImgConfigDTO img2ImgConfig, SdImg2ImgParams params) {
        // 开始组装提示词  风格 + 提示词 + embedding + lora
        StringBuilder promptBuffer = new StringBuilder();
        StringBuilder negativePromptBuffer = new StringBuilder();

        // 获取提示词
        String prompt = img2ImgConfig.getPrompt();
        String negativePrompt = img2ImgConfig.getNegative_prompt();
        // 获取风格
        SdImg2ImgDTO.SdStyle style = img2ImgConfig.getStyle();
        // 获取embedding
        SdImg2ImgDTO.SdEmbedding embedding = img2ImgConfig.getEmbedding();
        // 获取Lora
        String lora = img2ImgConfig.getLora();
        try {
            TranslateService translateService = TranslateFactory.build();
            prompt = translateTextContent(translateService, prompt);
            negativePrompt = translateTextContent(translateService, negativePrompt);
        } catch (Exception e) {
            log.error("翻译提示词异常：{}", e.getMessage());
        }

        String promptStyle = style.getPrompt();
        if (StrUtil.isNotBlank(promptStyle)) {
            promptBuffer.append(promptStyle);
        }
        if (StrUtil.isNotBlank(prompt)) {
            if (promptBuffer.length() > CommonConstant.STATUS_DISABLE_VALUE) {
                promptBuffer.append(StringPool.COMMA);
            }
            promptBuffer.append(prompt);
        }
        String promptEmbedding = embedding.getPrompt();
        if (StrUtil.isNotBlank(promptEmbedding)) {
            if (promptBuffer.length() > CommonConstant.STATUS_DISABLE_VALUE) {
                promptBuffer.append(StringPool.COMMA);
            }
            promptBuffer.append(promptEmbedding);
        }
        if (StrUtil.isNotBlank(lora)) {
            if (promptBuffer.length() > CommonConstant.STATUS_DISABLE_VALUE) {
                promptBuffer.append(StringPool.COMMA);
            }
            promptBuffer.append(lora);
        }

        String negativePromptStyle = style.getNegativePrompt();
        if (StrUtil.isNotBlank(negativePromptStyle)) {
            negativePromptBuffer.append(negativePromptStyle);
        }

        if (StrUtil.isNotBlank(negativePrompt)) {
            if (negativePromptBuffer.length() > CommonConstant.STATUS_DISABLE_VALUE) {
                negativePromptBuffer.append(StringPool.COMMA);
            }
            negativePromptBuffer.append(negativePrompt);
        }

        String negativePromptEmbedding = embedding.getNegativePrompt();
        if (StrUtil.isNotBlank(negativePromptEmbedding)) {
            if (negativePromptBuffer.length() > CommonConstant.STATUS_DISABLE_VALUE) {
                negativePromptBuffer.append(StringPool.COMMA);
            }
            negativePromptBuffer.append(negativePromptEmbedding);
        }

        params.setPrompt(promptBuffer.toString());
        params.setNegative_prompt(negativePromptBuffer.toString());
    }

    /**
     * 翻译文本
     *
     * @param translateService 服务
     * @param content 内容
     * @return 翻译内容
     */
    private String translateTextContent(TranslateService translateService, String content) {
        if (StrUtil.isNotBlank(content) && StringUtils.isContainChinese(content)) {
            // 翻译提示词
            TranslateResult result = translateService.textTranslation(TextTranslateParams.builder()
                    .from(TranslateEnums.Lang.ZH.getValue())
                    .to(TranslateEnums.Lang.EN.getValue())
                    .content(content)
                    .build()).getData();
            content = result.getDst();
        }

        return content;
    }


}
