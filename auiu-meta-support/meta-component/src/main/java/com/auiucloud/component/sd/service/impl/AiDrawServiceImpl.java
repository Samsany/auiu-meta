package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.auiucloud.component.cms.dto.SaveAiDrawGalleryDTO;
import com.auiucloud.component.cms.dto.UpdateAiDrawPicDTO;
import com.auiucloud.component.cms.enums.GalleryEnums;
import com.auiucloud.component.cms.service.IGalleryService;
import com.auiucloud.component.cms.service.IPicQualityService;
import com.auiucloud.component.cms.service.IPicRatioService;
import com.auiucloud.component.sd.domain.*;
import com.auiucloud.component.sd.dto.SdTxt2ImgConfigDTO;
import com.auiucloud.component.sd.dto.SdTxt2ImgDTO;
import com.auiucloud.component.sd.enums.SdDrawEnums;
import com.auiucloud.component.sd.service.IAiDrawService;
import com.auiucloud.component.sd.service.ISdModelService;
import com.auiucloud.component.sd.task.SdAsyncTask;
import com.auiucloud.component.sd.vo.SdProgressVO;
import com.auiucloud.component.sd.vo.SdWaitQueueVO;
import com.auiucloud.component.translate.component.TranslateFactory;
import com.auiucloud.component.translate.component.TranslateService;
import com.auiucloud.component.translate.domain.TextTranslateParams;
import com.auiucloud.component.translate.domain.TranslateResult;
import com.auiucloud.component.translate.enums.TranslateEnums;
import com.auiucloud.component.websocket.utils.WebSocketUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.constant.MessageConstant;
import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.common.enums.MessageEnums;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.model.WsMsgModel;
import com.auiucloud.core.common.utils.StringPool;
import com.auiucloud.core.common.utils.StringUtils;
import com.auiucloud.core.douyin.config.AppletsConfiguration;
import com.auiucloud.core.douyin.service.DouyinAppletsService;
import com.auiucloud.core.rabbit.utils.RabbitMqUtils;
import com.auiucloud.ums.dto.UserPointChangeDTO;
import com.auiucloud.ums.enums.UserPointEnums;
import com.auiucloud.ums.feign.IMemberProvider;
import com.auiucloud.ums.vo.UserInfoVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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

    private final StreamBridge streamBridge;
    private final RabbitMqUtils rabbitMqUtils;

    @GlobalTransactional
    @Override
    public void sdText2Img(SdTxt2ImgConfigDTO txt2ImgConfig) {

        String drawIds = txt2ImgConfig.getDrawIds();
        try {
            // 更新文生图状态
            this.updateDrawStatusByIds(drawIds, GalleryEnums.GalleryStatus.IN_PROGRESS.getValue());

            SdTxt2ImgParams params = new SdTxt2ImgParams();
            BeanUtils.copyProperties(txt2ImgConfig, params);
            // 构建关键词
            this.buildTxt2ImgPrompt(txt2ImgConfig, params);
            CompletableFuture<ApiResult<AiDrawResult>> future = sdAsyncTask.doSdText2ImgTask(params);
            // 获取制作进度
            String userIdStr = String.valueOf(txt2ImgConfig.getUserId());
            sdAsyncTask.doSdProgressTask(userIdStr, txt2ImgConfig.getTaskId(), txt2ImgConfig.getDrawIds());
            // 等待全部任务完成
            CompletableFuture.allOf(future).join();
            // SdProgressVO sdProgress = SdProgressVO.builder()
            //         .drawIds(txt2ImgConfig.getDrawIds())
            //         .taskId(txt2ImgConfig.getTaskId())
            //         .progress(CommonConstant.YES_VALUE * 100)
            //         .build();
            // WebSocketUtil.sendMessage(WsMsgModel.builder()
            //         .code(MessageEnums.WsMessageTypeEnum.SD_PROGRESS.getValue())
            //         .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
            //         .to(userIdStr)
            //         .content(ApiResult.data(sdProgress))
            //         .build());
            ApiResult<AiDrawResult> apiResult = future.get();
            // 在这里可以处理全部异步任务完成后的结果 更新作图记录
            if (apiResult.successful()) {
                // 完成任务
                AiDrawResult aiDrawResult = apiResult.getData();
                // 图片处理
                List<AiDrawImgVO> aiDrawImgList = this.updateAiDrawResultByIds(txt2ImgConfig, aiDrawResult.getImages());
                long errCount = aiDrawImgList.parallelStream()
                        .filter(it -> !it.getStatus().equals(GalleryEnums.AiDrawStatus.SUCCESS.getValue()))
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
                WebSocketUtil.sendMessage(WsMsgModel.builder()
                        .code(MessageEnums.WsMessageTypeEnum.SD_TXT2IMG.getValue())
                        .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                        .to(String.valueOf(txt2ImgConfig.getUserId()))
                        .content(ApiResult.data(aiDrawImgList))
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
            WebSocketUtil.sendMessage(WsMsgModel.builder()
                    .code(MessageEnums.WsMessageTypeEnum.ERROR.getValue())
                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                    .to(String.valueOf(txt2ImgConfig.getUserId()))
                    .content(ApiResult.data(e.getMessage()))
                    .build());
        } finally {
            // 停止获取进度任务
            sdAsyncTask.stopSdProgressTask();
        }
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
            if (StringUtils.isContainChinese(prompt)) {
                // 翻译提示词
                TranslateResult result = translateService.textTranslation(TextTranslateParams.builder()
                        .from(TranslateEnums.Lang.ZH.getValue())
                        .to(TranslateEnums.Lang.EN.getValue())
                        .content(prompt)
                        .build()).getData();
                prompt = result.getDst();
            }

            if (StringUtils.isContainChinese(negativePrompt)) {
                // 翻译反向提示词
                TranslateResult result = translateService.textTranslation(TextTranslateParams.builder()
                        .from(TranslateEnums.Lang.ZH.getValue())
                        .to(TranslateEnums.Lang.EN.getValue())
                        .content(negativePrompt)
                        .build()).getData();
                negativePrompt = result.getDst();
            }
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

    @GlobalTransactional
    @Override
    public void sdTxt2ImgHandleMessage(WebSocketSession session, WsMsgModel payload) {
        // 组装Ai绘画生图参数
        try {
            SdTxt2ImgDTO sdDrawParam = JSONUtil.toBean((String) payload.getContent(), SdTxt2ImgDTO.class);
            sdDrawParam.setUserId(Long.valueOf(payload.getFrom()));
            SdTxt2ImgConfigDTO sdDrawImgConfig = this.generatorSdTxt2ImgParams(sdDrawParam);
            // 保存作品
            List<Long> galleryIds = this.saveTxt2ImgRecord(JSONUtil.toJsonStr(payload.getContent()), sdDrawImgConfig);
            sdDrawImgConfig.setDrawIds(galleryIds.parallelStream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(StringPool.COMMA))
            );
            Integer queueMessageCount = rabbitMqUtils.getQueueMessageCount(MessageConstant.SD_TXT2IMG_MESSAGE_QUEUE);
            session.sendMessage(new TextMessage(WebSocketUtil.buildSendMessageModel(
                    WsMsgModel.builder()
                            .code(MessageEnums.WsMessageTypeEnum.SD_TXT2IMG_QUEUE.getValue())
                            .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                            .to(payload.getFrom())
                            .content(SdWaitQueueVO.builder()
                                    .queueMessageCount(queueMessageCount)
                                    .changeType(SdDrawEnums.QueueChangeType.INCREASE.getValue())
                                    .build())
                            .build()
            )));
            payload.setContent(sdDrawImgConfig);
            streamBridge.send(MessageConstant.SD_TXT2IMG_MESSAGE_OUTPUT, payload);
        } catch (Exception e) {
            log.error("[SD文生图异常，失败信息:{}]", e.getMessage());
            ApiResult<?> apiResult = ApiResult.fail(e.getMessage());
            if (e.getMessage().equals(ResultCode.USER_ERROR_A0160.getMessage())) {
                apiResult = WebSocketUtil.buildSendErrorMessageModel(ResultCode.USER_ERROR_A0160);
            }
            WebSocketUtil.sendMessage(WsMsgModel.builder()
                    .code(MessageEnums.WsMessageTypeEnum.ERROR.getValue())
                    .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                    .to(payload.getFrom())
                    .content(apiResult)
                    .build());

            throw new RuntimeException();
        }
    }

    @Override
    public SdTxt2ImgConfigDTO generatorSdTxt2ImgParams(SdTxt2ImgDTO sdDrawParam) {
        // 文本校验
        String prompt = sdDrawParam.getPrompt();
        if (StrUtil.isBlank(prompt)) {
            throw new ApiException("请输入关键词");
        }
        boolean checkPrompt = false;
        String clientId = "";
        String appId = "";
        try {
            String platform = sdDrawParam.getPlatform();
            String[] split = platform.split(StringPool.COLON);
            clientId = split[0];
            appId = split[1];

            if (clientId.equalsIgnoreCase(AuthenticationIdentityEnum.DOUYIN_APPLET.name())) {
                DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
                List<Integer> resultList = douyinAppletService.checkTextList(List.of(
                        sdDrawParam.getPrompt(),
                        sdDrawParam.getNegativePrompt()
                ));
                if (resultList.size() > 0) {
                    checkPrompt = true;
                }
            }
        } catch (Exception e) {
            log.error("文生图参数关键词校验异常: {}", e.getMessage());
            // throw new ApiException(ResultCode.USER_ERROR_A0500);
        }
        if (checkPrompt) {
            throw new ApiException(ResultCode.USER_ERROR_A0431);
        }

        Long userId = sdDrawParam.getUserId();
        UserInfoVO userInfo = memberProvider.getSimpleUserById(userId).getData();
        if (ObjectUtil.isNull(userInfo)) {
            throw new ApiException(ResultCode.USER_ERROR_A0301);
        }
        // 用户的积分
        Integer integral = userInfo.getIntegral();

        // 计算消耗积分
        // 1.获取生图比例
        SdPicRatio picRatio = picRatioService.getById(sdDrawParam.getPicRatioId());
        if (ObjectUtil.isNull(userInfo)) {
            throw new ApiException("绘制画面比例异常，请重试");
        }

        // 2.获取图片质量
        SdPicQuality picQuality = sdUpScaleService.getById(sdDrawParam.getPicQualityId());
        if (ObjectUtil.isNull(userInfo)) {
            throw new ApiException("制作异常，请重试");
        }

        Integer consumeIntegral = (picRatio.getConsumeIntegral() + picQuality.getConsumeIntegral()) * sdDrawParam.getBatchSize();
        if (picQuality.getEnableHr().equals(CommonConstant.YES_VALUE)) {
            consumeIntegral = consumeIntegral * picQuality.getHrScale().intValue();
        }
        if (integral < consumeIntegral) {
            throw new ApiException(ResultCode.USER_ERROR_A0160);
        }

        // 3.获取Model
        SdModel model = sdModelService.getById(sdDrawParam.getModelId());
        if (ObjectUtil.isNull(model) || StrUtil.isBlank(model.getModelName()) || StrUtil.isBlank(model.getModelHash())) {
            throw new ApiException("模型解析异常，请重试");
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
            // txt2ImgConfig.setTaskId(IdUtil.getSnowflakeNextIdStr());
            txt2ImgConfig.setConsumeIntegral(consumeIntegral);
            txt2ImgConfig.setStyle(sdDrawParam.getStyle());
            txt2ImgConfig.setLora(sdDrawParam.getLora());
            txt2ImgConfig.setEmbedding(sdDrawParam.getEmbedding());
            txt2ImgConfig.setRatio(picRatio.getRatio());

            // 覆盖配置 model、clip_skip、eta、ENSD
            JSONObject obj = JSONUtil.createObj();
            // obj.set("sd_model_checkpoint", model.getModelName() + " " + model.getModelHash());
            obj.set("sd_model_checkpoint", model.getTitle());
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

            return txt2ImgConfig;
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
    public void updateDrawStatusByIds(String drawIds, Integer status) {
        if (StrUtil.isNotBlank(drawIds)) {
            List<String> ids = StrUtil.split(drawIds, StringPool.COMMA);
            galleryService.updateAiDrawGalleryStatusByIds(ids, status);
        }
    }

    @Override
    public List<AiDrawImgVO> updateAiDrawResultByIds(SdTxt2ImgConfigDTO txt2ImgConfig, List<String> images) {

        List<AiDrawImgVO> aiDrawImgList = new ArrayList<>();
        String drawIds = txt2ImgConfig.getDrawIds();
        if (StrUtil.isNotBlank(drawIds)) {
            List<String> ids = StrUtil.split(drawIds, StringPool.COMMA);
            for (int i = 0; i < ids.size(); i++) {
                Long id = Long.valueOf(ids.get(i));
                String base64Img = images.get(i);
                AiDrawImgVO aiDrawImg = AiDrawImgVO.builder()
                        .galleryId(id)
                        .imageData("data:image/png;base64," + base64Img)
                        .status(GalleryEnums.AiDrawStatus.SUCCESS.getValue()) // 先默认成功
                        .build();
                try {
                    String platform = txt2ImgConfig.getPlatform();
                    String appId = txt2ImgConfig.getAppId();
                    if (platform.equalsIgnoreCase(AuthenticationIdentityEnum.DOUYIN_APPLET.name())) {
                        DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
                        String result = douyinAppletService.checkImageData(base64Img);
                        if (StrUtil.isNotBlank(result)) {
                            log.error("内容安全检测: {}", result);
                            aiDrawImg.setStatus(GalleryEnums.AiDrawStatus.VIOLATIONS.getValue());
                        }
                    }
                } catch (Exception e) {
                    log.error("内容安全检测异常: {}", e.getMessage());
                }
                aiDrawImgList.add(aiDrawImg);
                // UpdateAiDrawPicDTO data = UpdateAiDrawPicDTO.builder()
                //         .id(Long.valueOf(ids.get(i)))
                //         .pic(images.get(i))
                //         .build();
                // updateAiDrawPicDTOS.add(data);
            }
            aiDrawImgList = galleryService.updateAiDrawResult(txt2ImgConfig.getUserId(), aiDrawImgList);
        }
        return aiDrawImgList;
    }
}
