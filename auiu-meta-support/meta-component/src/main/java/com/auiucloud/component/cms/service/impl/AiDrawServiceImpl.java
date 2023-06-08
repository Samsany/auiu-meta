package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.auiucloud.component.cms.domain.*;
import com.auiucloud.component.cms.task.SdAsyncTask;
import com.auiucloud.component.cms.task.SdProgressTask;
import com.auiucloud.component.cms.props.SdConstants;
import com.auiucloud.component.cms.service.*;
import com.auiucloud.component.cms.vo.*;
import com.auiucloud.component.websocket.utils.WebSocketUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.api.ResultCode;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.enums.AuthenticationIdentityEnum;
import com.auiucloud.core.common.enums.MessageEnums;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.model.WsMsgModel;
import com.auiucloud.core.common.utils.StringPool;
import com.auiucloud.core.douyin.config.AppletsConfiguration;
import com.auiucloud.core.douyin.service.DouyinAppletsService;
import com.auiucloud.core.web.utils.RestTemplateUtil;
import com.auiucloud.ums.dto.UserPointChangeDTO;
import com.auiucloud.ums.enums.UserPointEnums;
import com.auiucloud.ums.feign.IMemberProvider;
import com.auiucloud.ums.vo.UserInfoVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    private final ISdDrawCategoryService sdDrawCategoryService;
    private final IPicRatioService picRatioService;
    private final IPicQualityService sdUpScaleService;
    private final ISdModelService sdModelService;
    private final ISdDrawStyleCategoryService sdDrawStyleCategoryService;
    private final ISdDrawStyleService sdDrawStyleService;
    private final ISdFusionModelCategoryService sdFusionModelCategoryService;
    private final SdAsyncTask sdAsyncTask;

    @Override
    public List<SdDrawCategoryVO> aiDrawMenuList() {
        return sdDrawCategoryService.aiDrawMenuList();
    }

    @Override
    public SdText2ImgConfigVO sdText2ImgConfig(Long aiDrawMenuId) {

        // 尺寸比例
        List<PicRatioVO> picRatioList = picRatioService.selectNormalPicRatioVOList();

        // 大模型 + Lora模型
        List<SdModelVO> sdModelList = sdModelService.selectSdModelVOListByCId(aiDrawMenuId);

        // Lora融合模型分类
        List<SdFusionModelCategoryVO> fusionModelCateList = sdFusionModelCategoryService.selectAllSdFusionModelCategoryVOList();

        // 默认风格style分类
        List<SdDrawStyleCategoryVO> sdDrawStyleCateList = sdDrawStyleCategoryService.selectAllSdDrawCategoryVOList();
        // 默认风格styles
        List<SdDrawStyleVO> sdDrawStyleList = sdDrawStyleService.selectAllSdDrawVOList();

        // 图片质量
        List<PicQualityVO> sdUpScaleList = sdUpScaleService.selectNormalPicQualityList();

        // todo 高级设置

        return SdText2ImgConfigVO.builder()
                .picRatioList(picRatioList)
                .sdUpScaleList(sdUpScaleList)
                .sdModelList(sdModelList)
                .fusionModelCateList(fusionModelCateList)
                .sdDrawStyleCateList(sdDrawStyleCateList)
                .sdDrawStyleList(sdDrawStyleList)
                .build();
    }

    @GlobalTransactional
    @Override
    public ApiResult<?> sdText2Img(SdDrawParamVO param) {

        // 文本校验
        try {
            String platform = param.getPlatform();
            String[] split = platform.split(StringPool.COLON);
            String clientId = split[0];
            String appId = split[1];

            if (clientId.equalsIgnoreCase(AuthenticationIdentityEnum.DOUYIN_APPLET.name())) {
                DouyinAppletsService douyinAppletService = AppletsConfiguration.getDouyinAppletService(appId);
                List<Integer> resultList = douyinAppletService.checkTextList(List.of(
                        param.getPrompt(),
                        param.getNegativePrompt()
                ));
                if (resultList.size() > 0) {
                    return ApiResult.fail(ResultCode.USER_ERROR_A0431);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.fail(ResultCode.USER_ERROR_A0500);
        }

        Long userId = param.getUserId();
        UserInfoVO userInfo = memberProvider.getSimpleUserById(userId).getData();
        if (ObjectUtil.isNull(userInfo)) {
            throw new ApiException(ResultCode.USER_ERROR_A0301);
        }
        // 用户的积分
        Integer integral = userInfo.getIntegral();

        // 计算消耗积分
        // 1.获取生图比例
        PicRatio picRatio = picRatioService.getById(param.getRatioId());
        if (ObjectUtil.isNull(userInfo)) {
            throw new ApiException("绘制画面比例异常，请重试");
        }

        // 2.获取图片质量
        PicQuality picQuality = sdUpScaleService.getById(param.getPicQualityId());
        if (ObjectUtil.isNull(userInfo)) {
            throw new ApiException("制作异常，请重试");
        }

        Integer consumeIntegral = picRatio.getConsumeIntegral() + picQuality.getConsumeIntegral();
        if (integral < consumeIntegral) {
            throw new ApiException(ResultCode.USER_ERROR_A0160);
        }

        // 3.获取Model
        SdModel model = sdModelService.getById(param.getModelId());
        if (ObjectUtil.isNull(model) || StrUtil.isBlank(model.getModelName())) {
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
            // 覆盖配置 model、clip_skip、eta、ENSD
            JSONObject obj = JSONUtil.createObj();
            obj.set("sd_model_checkpoint", model.getModelHash());

            SdText2ImgParam text2ImgParam = new SdText2ImgParam();
            text2ImgParam.setPrompt(Convert.toDBC(param.getPrompt()));
            text2ImgParam.setOverride_settings(obj);
            // 获取默认的风格
            SdDrawStyle style = sdDrawStyleService.getById(CommonConstant.NODE_ONE_ID);
            text2ImgParam.setPrompt(style.getPrompt().concat(StringPool.COMMA + Convert.toDBC(param.getPrompt())));
            text2ImgParam.setNegative_prompt(style.getNegativePrompt().concat(StringPool.COMMA + Convert.toDBC(param.getNegativePrompt())));

            text2ImgParam.setStyles(param.getStyles());
            text2ImgParam.setWidth(picRatio.getWidth());
            text2ImgParam.setHeight(picRatio.getHeight());
            // todo 是否开启修脸
            // text2ImgParam.setRestore_faces(Boolean.FALSE);

            // 是否开启高清修复
            if (picQuality.getEnableHr().equals(CommonConstant.YES_VALUE)) {
                text2ImgParam.setEnable_hr(Boolean.TRUE);
                text2ImgParam.setHr_scale(picQuality.getHrScale());
                text2ImgParam.setHr_upscaler(picQuality.getName());
                // text2ImgParam.getHr_second_pass_steps();
                text2ImgParam.setDenoising_strength(picQuality.getDenoisingStrength());
                text2ImgParam.setHr_resize_x(picQuality.getHrResizeX());
                text2ImgParam.setHr_resize_y(picQuality.getHrResizeY());
                // text2ImgParam.setFirstphase_width();
                // text2ImgParam.setFirstphase_height();
            } else {
                if (picQuality.getHrScale() > 0f) {
                    text2ImgParam.setWidth((int) (text2ImgParam.getWidth() * picQuality.getHrScale()));
                    text2ImgParam.setHeight((int) (text2ImgParam.getHeight() * picQuality.getHrScale()));
                }
            }

            // todo 高级设置（负面描述 描述词相关性 采样迭代步数 采样方法）
            if (StrUtil.isNotBlank(param.getNegativePrompt())) {
                String negativePrompt = Convert.toDBC(param.getNegativePrompt());
                text2ImgParam.setNegative_prompt(negativePrompt);
            }

            text2ImgParam.setCfg_scale(7f);
            text2ImgParam.setSteps(30);
            text2ImgParam.setBatch_size(param.getBatchSize());
            text2ImgParam.setSampler_index(param.getSamplerName());

            // 设置请求头
            // HttpHeaders headers = new HttpHeaders();
            // headers.setContentType(MediaType.APPLICATION_JSON);
            // Object data = RestTemplateUtil.post("http://43.155.170.143:7893" + SdConstants.text2Img, headers, text2ImgParam, Object.class).getBody();
            CompletableFuture<ApiResult<?>> future = sdAsyncTask.doSdText2ImgTask(text2ImgParam);
            // 获取制作进度
            String userIdStr = String.valueOf(param.getUserId());
            sdAsyncTask.doSdProgressTask(userIdStr);
            // Object o = future1.get();
            // return ApiResult.data(data);
            CompletableFuture.allOf(future).join();
            // 等待全部任务完成
            try {
                // System.out.println("Async task 1 result: " + result1);
                // 在这里可以处理全部异步任务完成后的结果
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                sdAsyncTask.stopSdProgressTask();
                // 完成任务
                WebSocketUtil.sendMessage(WsMsgModel.builder()
                        .code(MessageEnums.WsMessageTypeEnum.SD_PROGRESS.getValue())
                        .from(String.valueOf(CommonConstant.SYSTEM_NODE_ID))
                        .to(userIdStr)
                        .content(CommonConstant.STATUS_DISABLE_VALUE)
                        .build());
            }
        }

        return apiResult;
    }

}
